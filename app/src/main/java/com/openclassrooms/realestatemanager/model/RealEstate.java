package com.openclassrooms.realestatemanager.model;

public class RealEstate {
    private String mName,
                    mRegion,
                    mImageUrl;
    private int mPrice;

    public RealEstate(String name, String region, String imageUrl, int price) {
        mName = name;
        mRegion = region;
        mImageUrl = imageUrl;
        mPrice = price;
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

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }
}
