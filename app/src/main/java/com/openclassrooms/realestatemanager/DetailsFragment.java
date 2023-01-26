package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassrooms.realestatemanager.databinding.ActivityDetailsBinding;
import com.openclassrooms.realestatemanager.model.RealEstate;

public class DetailsFragment extends Fragment {

    RealEstate mEstate;
    ActivityDetailsBinding mBinding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = ActivityDetailsBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        mEstate = bundle.getParcelable("REAL_ESTATE");

        MediaGalleryAdapter mediaGalleryAdapter= new MediaGalleryAdapter(mEstate.getMediaList());
        mBinding.mediaGallery.setAdapter(mediaGalleryAdapter);
        mBinding.mediaGallery.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        Log.d("TAG", "onCreateView: FRAGMENT");

        return mBinding.getRoot();
    }


}