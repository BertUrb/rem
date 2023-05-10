package com.openclassrooms.realestatemanager.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.realestatemanager.Utils;
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

    public long createOrUpdateRealEstate(RealEstate estate) {
        return mRealEstateDao.createOrUpdateRealEstate(estate);

    }
}
