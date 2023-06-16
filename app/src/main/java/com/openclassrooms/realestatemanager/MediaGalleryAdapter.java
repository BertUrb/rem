package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.databinding.MediaListItemBinding;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;
import com.openclassrooms.realestatemanager.ui.RealEstateViewModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class MediaGalleryAdapter extends RecyclerView.Adapter<MediaGalleryViewHolder> {

    Context mContext;
    List<RealEstateMedia> mMediaList;
    MediaListItemBinding mBinding;

    public MediaGalleryAdapter(List<RealEstateMedia> mediaList) {
        mMediaList = mediaList;
    }

    @NonNull
    @Override
    public MediaGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = MediaListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        mContext = parent.getContext();
        return new MediaGalleryViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaGalleryViewHolder holder, int position) {
        Glide.with(mContext)
                .load(mMediaList.get(position).getMediaUrl())
                .into(holder.getImage());

        holder.getCaption().setText(mMediaList.get(position).getMediaCaption());
    }



    @Override
    public int getItemCount() {
        return mMediaList.size();
    }
}
