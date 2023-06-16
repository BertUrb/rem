package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.model.RealEstate;

import java.util.Date;
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

    public LiveData<List<RealEstate>> filterRealEstates(String name,Date maxSaleDate, Date minListingDate, int maxPrice, int minPrice, int maxSurface, int minSurface) {
        return mRealEstateDao.filterRealEstates(name,maxSaleDate,minListingDate,maxPrice,minPrice,maxSurface,minSurface);
    }

    public void updateFeaturedMediaUrl(String oldUrl, String mediaUrl) {
        mRealEstateDao.updateFeaturedMediaUrl(oldUrl,mediaUrl);
    }
}
