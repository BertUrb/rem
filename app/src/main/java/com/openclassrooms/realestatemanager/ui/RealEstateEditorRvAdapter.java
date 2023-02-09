package com.openclassrooms.realestatemanager.ui;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.databinding.AddMediaListItemBinding;

import java.util.List;

public class RealEstateEditorRvAdapter extends RecyclerView.Adapter<RealEstateEditorRvViewHolder> {
    List<Uri> mSelectedMediaUriList;
    private AddMediaListItemBinding mMediaListItemBinding;
    private Context mContext;

    public RealEstateEditorRvAdapter(List<Uri> selectedMediaUriList) {
        mSelectedMediaUriList = selectedMediaUriList;
    }

    @NonNull
    @Override
    public RealEstateEditorRvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mMediaListItemBinding = AddMediaListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        mContext = parent.getContext();
        return new RealEstateEditorRvViewHolder(mMediaListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RealEstateEditorRvViewHolder holder, int position) {
        Glide.with(mContext)
                .load(mSelectedMediaUriList.get(position))
                .into(holder.getImage());

        holder.getButton().setOnClickListener(view -> {
            if (position < mSelectedMediaUriList.size()) {
                mSelectedMediaUriList.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSelectedMediaUriList.size();
    }
}
