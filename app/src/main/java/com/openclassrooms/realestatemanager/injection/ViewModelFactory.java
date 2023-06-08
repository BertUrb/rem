package com.openclassrooms.realestatemanager.injection;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.database.SaveRealEstateDB;
import com.openclassrooms.realestatemanager.repositories.RealEstateMediaRepo;
import com.openclassrooms.realestatemanager.repositories.RealEstateRepo;
import com.openclassrooms.realestatemanager.ui.RealEstateViewModel;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private static volatile ViewModelFactory sModelFactory;
    private final RealEstateRepo mRealEstateRepo;
    private final RealEstateMediaRepo mRealEstateMediaRepo;
    private final Executor mExecutor;

    private ViewModelFactory(Context context) {
        SaveRealEstateDB db = SaveRealEstateDB.getInstance(context);
        mRealEstateRepo = new RealEstateRepo(db.realEstateDao());
        mRealEstateMediaRepo = new RealEstateMediaRepo(db.realEstateMediaDao());
        mExecutor = Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory getInstance(Context context) {
        if (sModelFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (sModelFactory == null) {
                    sModelFactory = new ViewModelFactory(context);
                }
            }
        }
        return sModelFactory;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RealEstateViewModel.class)) {
            return Objects.requireNonNull(modelClass.cast(new RealEstateViewModel(mRealEstateRepo, mRealEstateMediaRepo, mExecutor)));
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}