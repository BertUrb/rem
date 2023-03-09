package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.model.RealEstate;

import java.util.List;
@Dao
public interface RealEstateDao {

    @Query("SELECT * FROM RealEstate")
    LiveData<List<RealEstate>> getAllRealEstate();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createOrUpdateRealEstate(RealEstate realEstate);

    @Delete
    int deleteRealEstate(RealEstate realEstate);



}
