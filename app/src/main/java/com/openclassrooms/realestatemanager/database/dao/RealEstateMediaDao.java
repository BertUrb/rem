package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.model.RealEstateMedia;

import java.util.List;
@Dao
public interface RealEstateMediaDao {
    @Query("SELECT * FROM RealEstateMedia WHERE mRealEstateId = :realEstateID")
    LiveData<List<RealEstateMedia>> getMediaByRealEstateId(long realEstateID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addMedia(RealEstateMedia realEstateMedia);

    @Delete
    int deleteMedia(RealEstateMedia realEstateMedia);

}
