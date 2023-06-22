package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.databinding.MediaListItemBinding;
import com.openclassrooms.realestatemanager.event.OnItemClickListener;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;

import java.util.List;

public class MediaGalleryAdapter extends RecyclerView.Adapter<MediaGalleryViewHolder> {

    Context mContext;
    List<RealEstateMedia> mMediaList;
    MediaListItemBinding mBinding;
    private final OnItemClickListener mOnItemClickListener;



    public MediaGalleryAdapter(List<RealEstateMedia> mediaList,OnItemClickListener onItemClickListener) {
        mMediaList = mediaList;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MediaGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = MediaListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        mContext = parent.getContext();
        return new MediaGalleryViewHolder(mBinding,mOnItemClickListener);
    }
    public String getMediaUrl(int position) {
        return mMediaList.get(position).getMediaUrl();
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

