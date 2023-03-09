package com.openclassrooms.realestatemanager.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.database.dao.RealEstateMediaDao;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Database(entities = {RealEstate.class, RealEstateMedia.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract  class SaveRealEstateDB extends RoomDatabase {

    private static  volatile SaveRealEstateDB INSTANCE;

    public abstract RealEstateDao realEstateDao();
    public abstract RealEstateMediaDao realEstateMediaDao();

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
                List<RealEstate> realEstateList = RealEstate.getDataExample();
                List<RealEstateMedia> realEstateMedias = RealEstateMedia.getMediaExamples();
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> INSTANCE.realEstateDao().insertMultipleRealEstates(realEstateList));
                executor.execute(() -> INSTANCE.realEstateMediaDao().insertMultipleMedia(realEstateMedias));
                executor.shutdown();

                super.onCreate(db);
            }

        };
    }


}




