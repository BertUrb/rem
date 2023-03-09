package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
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

    public long addRealEstateMedia(RealEstateMedia media) {
        return mRealEstateMediaDao.addMedia(media);
    }

    public int deleteAllMediaByRealEstateID(long realEstateId) {
        return mRealEstateMediaDao.deleteAllMediaByRealEstateId(realEstateId);
    }




}
