package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.databinding.RealEstateListItemBinding;
import com.openclassrooms.realestatemanager.event.OnRealEstateClickListener;
import com.openclassrooms.realestatemanager.model.RealEstate;

import java.util.List;

public class RealEstateRvAdapter extends RecyclerView.Adapter<RealEstateViewHolder> {
    private List<RealEstate> mRealEstateList;
    private RealEstateListItemBinding mRealEstateListItemBinding;
    private final OnRealEstateClickListener mOnRealEstateClickListener;
    Context mContext;

    public RealEstateRvAdapter(List<RealEstate> realEstateList, OnRealEstateClickListener onRealEstateClickListener)
    {
        mRealEstateList = realEstateList;
        mOnRealEstateClickListener = onRealEstateClickListener;
    }
    @NonNull
    @Override
    public RealEstateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mRealEstateListItemBinding = RealEstateListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        mContext = parent.getContext();
        return new RealEstateViewHolder(mRealEstateListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RealEstateViewHolder holder, int position) {
        Log.d("TAG", "onBindViewHolder: " + mRealEstateList.get(position).getName());
        holder.getRealEstateName().setText(mRealEstateList.get(position).getName());
        holder.getRealEstateRegion().setText(mRealEstateList.get(position).getRegion());
        holder.getRealEstatePrice().setText(mRealEstateList.get(position).getPrice() + " €");

        Glide.with(mContext)
                .load(mRealEstateList.get(position).getMediaList().get(mRealEstateList.get(position).getFeaturedMediaIndex()).getMediaUrl())
                .into(holder.getRealEstateImageView());

        holder.itemView.setOnClickListener(v -> {
            mOnRealEstateClickListener.OnRealEstateClick(position);
        });


    }

    @Override
    public int getItemCount() {
        return mRealEstateList.size();
    }

}
