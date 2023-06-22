package com.openclassrooms.realestatemanager.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.database.dao.RealEstateMediaDao;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;

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
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SaveRealEstateDB.class, "MyDatabase.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}











