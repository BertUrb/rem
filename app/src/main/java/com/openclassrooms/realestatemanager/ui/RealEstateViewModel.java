package com.openclassrooms.realestatemanager.ui;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;
import com.openclassrooms.realestatemanager.repositories.RealEstateMediaRepo;
import com.openclassrooms.realestatemanager.repositories.RealEstateRepo;

import java.util.Date;
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
        return mRealEstateMediaRepo.getRealEstateMediaByRealEstateId(id);
    }

    public void createOrUpdateRealEstate(RealEstate estate) {
        mExecutor.execute(()-> {
            long id = mRealEstateRepo.createOrUpdateRealEstate(estate);
            Log.d("TAG", "createOrUpdateRealEstate: ID:" + estate.getID() + "id:" + id);
            mExecutor.execute(() -> mRealEstateMediaRepo.deleteAllMediaByRealEstateID(id));
            if(estate.getMediaList()!= null) {
                for (RealEstateMedia media : estate.getMediaList()) {
                    media.setRealEstateId(id);
                    mExecutor.execute(() -> mRealEstateMediaRepo.addRealEstateMedia(media));
                }
            }

        });



    }

    public void updateMedia(RealEstateMedia media) {
        mExecutor.execute( () -> mRealEstateMediaRepo.addRealEstateMedia(media));
    }

    public LiveData<List<RealEstate>> filterEstates(String name,Date maxSaleDate, Date minListingDate, int maxPrice, int minPrice, int  maxSurface, int minSurface)
    {

        return mRealEstateRepo.filterRealEstates(name,maxSaleDate,minListingDate,maxPrice,minPrice,maxSurface,minSurface);

    }


    public void updateEstateFeaturedMediaUrl(String oldUrl, String mediaUrl) {
        mExecutor.execute(()-> mRealEstateRepo.updateFeaturedMediaUrl(oldUrl,mediaUrl));
    }
}
