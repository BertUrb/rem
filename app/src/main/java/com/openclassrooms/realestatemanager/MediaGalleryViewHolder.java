package com.openclassrooms.realestatemanager;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.databinding.MediaListItemBinding;
import com.openclassrooms.realestatemanager.event.OnItemClickListener;

public class MediaGalleryViewHolder extends RecyclerView.ViewHolder {
    private final MediaListItemBinding mBinding;

    private final OnItemClickListener mOnItemClickListener;
    public MediaGalleryViewHolder(@NonNull MediaListItemBinding binding,OnItemClickListener onItemClickListener) {
        super(binding.getRoot());
        mBinding = binding;
        mOnItemClickListener = onItemClickListener;
        mBinding.getRoot().setOnClickListener(v-> {
            if (mOnItemClickListener != null) {
                MediaGalleryAdapter adapter = (MediaGalleryAdapter) getBindingAdapter();

                assert adapter != null;
                mOnItemClickListener.onItemClick(adapter.getMedia(getBindingAdapterPosition()));
            }
        });
    }

    public ImageView getImage() {
        return mBinding.image;
    }

    public TextView getCaption() {
        return mBinding.caption;
    }
}
