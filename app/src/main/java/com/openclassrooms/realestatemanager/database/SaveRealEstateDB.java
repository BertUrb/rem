package com.openclassrooms.realestatemanager.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                            .addCallback(prepopulateDatabase(INSTANCE,context))

                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase(SaveRealEstateDB database, Context context) {
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                List<RealEstate> realEstateList = new ArrayList<>(); // = RealEstate.getDataExample();
                List<RealEstateMedia> realEstateMedias = new ArrayList<>(); // RealEstateMedia.getMediaExamples();
                ExecutorService executor = Executors.newSingleThreadExecutor();

                if (Utils.isInternetAvailable(context)) {
                     FirebaseFirestore dbFs = FirebaseFirestore.getInstance();

                    dbFs.collection("estates").get().addOnCompleteListener(task -> {


                        for (QueryDocumentSnapshot document : task.getResult()) {
                            realEstateList.add(RealEstate.fromQueryDocumentSnapshot(document));
                        }
                        if (realEstateList.isEmpty()) {
                            realEstateList.addAll(RealEstate.getDataExample());
                        }
                        executor.execute(() -> database.realEstateDao().insertMultipleRealEstates(realEstateList));

                        dbFs.collection("medias").get().addOnCompleteListener(mediaTask -> {
                            for (QueryDocumentSnapshot document : mediaTask.getResult()) {
                                //realEstateMedias.add(RealEstateMedia.fromQueryDocumentSnapshot(document));
                            }
                            if (realEstateMedias.isEmpty()) {
                                realEstateMedias.addAll(RealEstateMedia.getMediaExamples());
                            }
                            executor.execute(() -> database.realEstateMediaDao().insertMultipleMedia(realEstateMedias));
                            executor.shutdown();


                        });

                    });


                    super.onCreate(db);
                }

            }


        };


    }
}




