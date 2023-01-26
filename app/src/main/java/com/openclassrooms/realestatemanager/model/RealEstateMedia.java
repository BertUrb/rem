package com.openclassrooms.realestatemanager.model;

public class RealEstateMedia {
    private String mMediaUrl, mMediaCaption;

    public RealEstateMedia(String mediaUrl, String mediaCaption) {
        mMediaUrl = mediaUrl;
        mMediaCaption = mediaCaption;
    }

    public String getMediaUrl() {
        return mMediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        mMediaUrl = mediaUrl;
    }

    public String getMediaCaption() {
        return mMediaCaption;
    }

    public void setMediaCaption(String mediaCaption) {
        mMediaCaption = mediaCaption;
    }
}
