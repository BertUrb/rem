package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.model.RealEstate;

import java.util.Date;
import java.util.List;
@Dao
public interface RealEstateDao {

    @Query("SELECT * FROM RealEstate")
    LiveData<List<RealEstate>> getAllRealEstate();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createOrUpdateRealEstate(RealEstate realEstate);

    @Delete
    void deleteRealEstate(RealEstate realEstate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleRealEstates(List<RealEstate> realEstates) ;

    @Query("SELECT * From RealEstate WHERE mID= :id")
    Cursor getRealEstateWithCursor(long id);

    @Query("SELECT * FROM RealEstate WHERE (:name IS NULL OR mName LIKE '%' || :name || '%') " +
            "AND (:maxPrice IS NULL OR mPrice <= :maxPrice) " +
            "AND (:minPrice IS NULL OR mPrice >= :minPrice) " +
            "AND (:maxSurface IS NULL OR mSurface <= :maxSurface) " +
            "AND (:minSurface IS NULL OR mSurface >= :minSurface) " +
            "AND (:maxSaleDate IS NULL OR sale_date > :maxSaleDate) " +
            "AND (:minListingDate IS NULL OR listing_date > :minListingDate)")
    LiveData<List<RealEstate>> filterRealEstates(String name, Date maxSaleDate, Date minListingDate, int maxPrice, int minPrice, int maxSurface, int minSurface);


}




