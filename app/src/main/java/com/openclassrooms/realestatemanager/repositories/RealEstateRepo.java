package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.model.RealEstate;

import java.util.List;

public class RealEstateRepo {
    private final RealEstateDao mRealEstateDao;

    public RealEstateRepo(RealEstateDao realEstateDao) {
        mRealEstateDao = realEstateDao;
    }

    public LiveData<List<RealEstate>> getAllRealEstates() {
        return mRealEstateDao.getAllRealEstate();
    }
}
