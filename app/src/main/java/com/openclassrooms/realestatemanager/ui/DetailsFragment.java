package com.openclassrooms.realestatemanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassrooms.realestatemanager.MediaGalleryAdapter;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityDetailsBinding;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;

import java.util.List;

public class DetailsFragment extends Fragment {

    RealEstate mEstate;
    ActivityDetailsBinding mBinding;
    private RealEstateViewModel mRealEstateViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = ActivityDetailsBinding.inflate(inflater, container, false);
        mRealEstateViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getContext())).get(RealEstateViewModel.class);
        Bundle bundle = getArguments();
        mEstate = bundle.getParcelable("REAL_ESTATE");
        mRealEstateViewModel.getRealEstateMediasByID(mEstate.getID()).observe(this,this::mediaObserver);
        mBinding.address.setText(String.format(getString(R.string.address),mEstate.getLocation()));
        mBinding.description.setText(mEstate.getDescription());
        String surface = getString(R.string.surface);
        String formattedSurface = String.format(surface,Integer.toString(mEstate.getSurface()),"m²");
        mBinding.surface.setText(formattedSurface);
        mBinding.bathrooms.setText(String.format(getString(R.string.number_of_bathrooms),Integer.toString(mEstate.getBathrooms())));
        mBinding.rooms.setText(String.format(getString(R.string.number_of_rooms),Integer.toString(mEstate.getRooms())));

        return mBinding.getRoot();
    }

    private void mediaObserver(List<RealEstateMedia> mediaList) {
        mEstate.setMediaList(mediaList);
        MediaGalleryAdapter mediaGalleryAdapter= new MediaGalleryAdapter(mEstate.getMediaList());
        mBinding.mediaGallery.setAdapter(mediaGalleryAdapter);
        mBinding.mediaGallery.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


    }


}