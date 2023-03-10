package com.openclassrooms.realestatemanager.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;
import com.openclassrooms.realestatemanager.repositories.RealEstateMediaRepo;
import com.openclassrooms.realestatemanager.repositories.RealEstateRepo;

import java.util.List;
import java.util.concurrent.Executor;

public class RealEstateViewModel extends ViewModel {
    private final RealEstateRepo mRealEstateRepo;
    private final RealEstateMediaRepo mRealEstateMediaRepo;
    private final Executor mExecutor;

    public RealEstateViewModel(RealEstateRepo realEstateRepo, RealEstateMediaRepo realEstateMediaRepo, Executor executor) {
        mRealEstateRepo = realEstateRepo;
        mRealEstateMediaRepo = realEstateMediaRepo;
        mExecutor = executor;
    }

    public LiveData<List<RealEstate>> getRealEstates() {
        return mRealEstateRepo.getAllRealEstates();
    }

    public LiveData<List<RealEstateMedia>> getRealEstateMediasByID(long id) {
        return mRealEstateMediaRepo.getRealEstateMediaByMediaId(id);
    }
}
