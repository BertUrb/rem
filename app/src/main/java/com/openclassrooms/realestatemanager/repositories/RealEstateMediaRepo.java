package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.RealEstateMediaDao;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;

import java.util.List;

public class RealEstateMediaRepo {
    private final RealEstateMediaDao mRealEstateMediaDao;

    public RealEstateMediaRepo(RealEstateMediaDao realEstateDao) {
        mRealEstateMediaDao = realEstateDao;

    }

    public LiveData<List<RealEstateMedia>> getRealEstateMediaByRealEstateId(long realEstateId) {
        return mRealEstateMediaDao.getMediaByRealEstateId(realEstateId);
    }

    public void addRealEstateMedia(RealEstateMedia media) {
        mRealEstateMediaDao.addMedia(media);
    }

    public void deleteAllMediaByRealEstateID(long realEstateId) {
        mRealEstateMediaDao.deleteAllMediaByRealEstateId(realEstateId);
    }




}
