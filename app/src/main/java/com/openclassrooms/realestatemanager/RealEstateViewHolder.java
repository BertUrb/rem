package com.openclassrooms.realestatemanager;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.databinding.RealEstateListItemBinding;

public class RealEstateViewHolder extends RecyclerView.ViewHolder{
    private final RealEstateListItemBinding mBinding;
    public RealEstateViewHolder(@NonNull RealEstateListItemBinding realEstateListItemBinding) {
        super(realEstateListItemBinding.getRoot());
        mBinding = realEstateListItemBinding;
    }

    public ImageView getRealEstateImageView()
    {
        return  mBinding.realEstateImage;
    }

    public TextView getRealEstateName()
    {
        return mBinding.realEstateName;
    }

    public TextView getRealEstateRegion() {
        return  mBinding.realEstateRegion;
    }

    public TextView getRealEstatePrice() {
        return  mBinding.realEstatePrice;
    }
}
