package com.openclassrooms.realestatemanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class RealEstate implements Parcelable {
    private String mName,
                    mRegion,
                    mLocation;
    private int mPrice,
                mSurface,
                mRooms,
                mBathrooms,
                mBedrooms,
                mFeaturedMediaIndex;

    private List<RealEstateMedia> mMediaList = new ArrayList<>();

    public RealEstate(String name, String region, int price, List<RealEstateMedia> mediaList, int featuredMediaIndex) {
        mName = name;
        mRegion = region;
        mPrice = price;
        mMediaList = mediaList;
        mFeaturedMediaIndex = featuredMediaIndex;
    }

    protected RealEstate(Parcel in) {
        mName = in.readString();
        mRegion = in.readString();
        mLocation = in.readString();
        mPrice = in.readInt();
        mSurface = in.readInt();
        mRooms = in.readInt();
        mBathrooms = in.readInt();
        mBedrooms = in.readInt();
        mFeaturedMediaIndex = in.readInt();
    }

    public static final Creator<RealEstate> CREATOR = new Creator<RealEstate>() {
        @Override
        public RealEstate createFromParcel(Parcel in) {
            return new RealEstate(in);
        }

        @Override
        public RealEstate[] newArray(int size) {
            return new RealEstate[size];
        }
    };

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public int getSurface() {
        return mSurface;
    }

    public void setSurface(int surface) {
        mSurface = surface;
    }

    public int getRooms() {
        return mRooms;
    }

    public void setRooms(int rooms) {
        mRooms = rooms;
    }

    public int getBathrooms() {
        return mBathrooms;
    }

    public void setBathrooms(int bathrooms) {
        mBathrooms = bathrooms;
    }

    public int getBedrooms() {
        return mBedrooms;
    }

    public void setBedrooms(int bedrooms) {
        mBedrooms = bedrooms;
    }

    public int getFeaturedMediaIndex() {
        return mFeaturedMediaIndex;
    }

    public void setFeaturedMediaIndex(int featuredMediaIndex) {
        mFeaturedMediaIndex = featuredMediaIndex;
    }

    public List<RealEstateMedia> getMediaList() {
        return mMediaList;
    }

    public void setMediaList(List<RealEstateMedia> mediaList) {
        mMediaList = mediaList;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getRegion() {
        return mRegion;
    }

    public void setRegion(String region) {
        mRegion = region;
    }


    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mRegion);
        parcel.writeString(mLocation);
        parcel.writeInt(mPrice);
        parcel.writeInt(mSurface);
        parcel.writeInt(mRooms);
        parcel.writeInt(mBathrooms);
        parcel.writeInt(mBedrooms);
        parcel.writeInt(mFeaturedMediaIndex);
    }
}
