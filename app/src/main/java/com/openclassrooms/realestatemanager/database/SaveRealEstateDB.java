package com.openclassrooms.realestatemanager.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),  SaveRealEstateDB.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase(context))
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase(Context context) {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {

                final List<RealEstate> realEstateList = new ArrayList<>(); // = RealEstate.getDataExample();
                final List<RealEstateMedia> realEstateMedias = new ArrayList<>(); // RealEstateMedia.getMediaExamples();
                final ExecutorService executor = Executors.newSingleThreadExecutor();

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
                                                    media.setSync(true);
                                                    mediaList.add(media);

                                                    Log.d("TAG", "prepopulateDatabase: " + media.getMediaUrl());
                                                }
                                            }
                                        } else {
                                            Log.d("TAG", "Error while retrieving documents:", task1.getException());
                                        }
                                    });
                            estate.setMediaList(mediaList);
                            estate.setSync(true);
                            realEstateList.add(estate);
                            i.incrementAndGet();
                        }
                        if (realEstateList.isEmpty()) {
                            realEstateList.addAll(RealEstate.getDataExample());
                        }
                        executor.execute(() -> INSTANCE.realEstateDao().insertMultipleRealEstates(realEstateList));

                        if (realEstateMedias.isEmpty()) {
                            realEstateMedias.addAll(RealEstateMedia.getMediaExamples());
                        }
                        executor.execute(() -> INSTANCE.realEstateMediaDao().insertMultipleMedia(realEstateMedias));
                        setDatabaseIsPrepopulated(context);
                        executor.shutdown();


                    });


                }
                super.onCreate(db);
            }
        };
    }


    public static boolean isDatabasePrepopulated(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("DatabasePrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isPrepopulated", false);
    }

    private static void setDatabaseIsPrepopulated(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("DatabasePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isPrepopulated", true);
        editor.apply();
    }


}











