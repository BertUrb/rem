package com.openclassrooms.realestatemanager;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.databinding.MediaListItemBinding;

public class MediaGalleryViewHolder extends RecyclerView.ViewHolder {
    private MediaListItemBinding mBinding;
    public MediaGalleryViewHolder(@NonNull MediaListItemBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public ImageView getImage() {
        return mBinding.image;
    }

    public TextView getCaption() {
        return mBinding.caption;
    }
}
