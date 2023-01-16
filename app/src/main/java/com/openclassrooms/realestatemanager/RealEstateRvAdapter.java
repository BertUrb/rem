package com.openclassrooms.realestatemanager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.databinding.RealEstateListItemBinding;
import com.openclassrooms.realestatemanager.model.RealEstate;

import java.util.List;

public class RealEstateRvAdapter extends RecyclerView.Adapter<RealEstateViewHolder> {
    List<RealEstate> mRealEstateList;
    RealEstateListItemBinding mRealEstateListItemBinding;

    public RealEstateRvAdapter(List<RealEstate> realEstateList)
    {
        mRealEstateList = realEstateList;
    }
    @NonNull
    @Override
    public RealEstateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mRealEstateListItemBinding = RealEstateListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new RealEstateViewHolder(mRealEstateListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RealEstateViewHolder holder, int position) {
        Log.d("TAG", "onBindViewHolder: " + mRealEstateList.get(position).getName());
        holder.getRealEstateName().setText(mRealEstateList.get(position).getName());
        holder.getRealEstateRegion().setText(mRealEstateList.get(position).getRegion());
        holder.getRealEstatePrice().setText(mRealEstateList.get(position).getPrice() + " €");

    }

    @Override
    public int getItemCount() {
        return mRealEstateList.size();
    }
}
