package com.openclassrooms.realestatemanager.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.database.dao.RealEstateMediaDao;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Database(entities = {RealEstate.class, RealEstateMedia.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class SaveRealEstateDB extends RoomDatabase {

    private static volatile SaveRealEstateDB INSTANCE;

    public abstract RealEstateDao realEstateDao();

    public abstract RealEstateMediaDao realEstateMediaDao();

    public static SaveRealEstateDB getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SaveRealEstateDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    SaveRealEstateDB.class, "MyDatabase.db")
                            .build();

                    prepopulateDatabase(INSTANCE, context);
                }
            }
        }
        return INSTANCE;
    }

    private static void prepopulateDatabase(SaveRealEstateDB database, Context context) {
        List<RealEstate> realEstateList = new ArrayList<>(); // = RealEstate.getDataExample();
        List<RealEstateMedia> realEstateMedias = new ArrayList<>(); // RealEstateMedia.getMediaExamples();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        if (Utils.isInternetAvailable(context)) {
            FirebaseFirestore dbFs = FirebaseFirestore.getInstance();
            AtomicInteger i = new AtomicInteger(1);

            dbFs.collection("estates").get().addOnCompleteListener(task -> {


                for (QueryDocumentSnapshot document : task.getResult()) {
                    RealEstate estate = RealEstate.fromQueryDocumentSnapshot(document);
                    estate.setID(i.get());
                    List<RealEstateMedia> mediaList = new ArrayList<>();
                    int id = Integer.parseInt(document.getId().substring(estate.getAgentName().length()));

                    dbFs.collection("medias").whereGreaterThanOrEqualTo(FieldPath.documentId(), estate.getAgentName())
                            .whereLessThan(FieldPath.documentId(), estate.getAgentName() + "\uf8ff")
                            .get()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    for (QueryDocumentSnapshot mediaDocument : task1.getResult()) {
                                        RealEstateMedia media = RealEstateMedia.fromQueryDocumentSnapshot(mediaDocument);
                                        if (media.getRealEstateId() == id) {
                                            media.setRealEstateId(i.get());
                                            realEstateMedias.add(media);
                                            mediaList.add(media);
                                            Log.d("TAG", "prepopulateDatabase: " + media.getMediaUrl());
                                        }
                                    }
                                } else {
                                    Log.d("TAG", "Error while retrieving documents:", task1.getException());
                                }
                            });
                    estate.setMediaList(mediaList);
                    realEstateList.add(estate);
                    i.incrementAndGet();
                }
                if (realEstateList.isEmpty()) {
                    realEstateList.addAll(RealEstate.getDataExample());
                }
                executor.execute(() -> database.realEstateDao().insertMultipleRealEstates(realEstateList));

                if (realEstateMedias.isEmpty()) {
                    realEstateMedias.addAll(RealEstateMedia.getMediaExamples());
                }
                executor.execute(() -> database.realEstateMediaDao().insertMultipleMedia(realEstateMedias));
                executor.shutdown();


            });


        }

    }


}







