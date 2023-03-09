package com.openclassrooms.realestatemanager.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.database.dao.RealEstateMediaDao;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;

import java.util.concurrent.Executors;

@Database(entities = {RealEstate.class, RealEstateMedia.class}, version = 1, exportSchema = false)
public abstract  class SaveRealEstateDB extends RoomDatabase {

    private static  volatile SaveRealEstateDB INSTANCE;

    public abstract RealEstateDao realEstateDao();
    public abstract RealEstateMediaDao ralEstateMediaDao();

    public static SaveRealEstateDB getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SaveRealEstateDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    SaveRealEstateDB.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())

                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase() {
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                RealEstate[] realEstateList = RealEstate.getDataExample();
                for (RealEstate realEstate: realEstateList) {
                    Executors.newSingleThreadExecutor().execute(
                            () -> INSTANCE.realEstateDao().createOrUpdateRealEstate(realEstate)
                    );
                }
                RealEstateMedia[] realEstateMedias = RealEstateMedia.getMediaExamples();
                for (RealEstateMedia realEstateMedia: realEstateMedias) {
                    Executors.newSingleThreadExecutor().execute(
                            () -> INSTANCE.ralEstateMediaDao().addMedia(realEstateMedia)
                    );
                }
                super.onCreate(db);
            }

        };
    }


}




