package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.databinding.MediaListItemBinding;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;

import java.util.ArrayList;
import java.util.List;

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
        mBinding = MediaListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        mContext = parent.getContext();
        return new MediaGalleryViewHolder(mBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull MediaGalleryViewHolder holder, int position) {

            Glide.with(mContext)
                    .load(mMediaList.get(position).getMediaUrl())
                    .into(holder.getImage());

            Log.d("TAG", "onBindViewHolder: " + mMediaList.get(position).getMediaUrl());

            holder.getCaption().setText(mMediaList.get(position).getMediaCaption());

        }

        @Override
        public int getItemCount() {
            return mMediaList.size();
        }
    }
