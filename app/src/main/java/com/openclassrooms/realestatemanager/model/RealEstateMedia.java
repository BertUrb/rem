package com.openclassrooms.realestatemanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = RealEstate.class,parentColumns = "mID",childColumns = "mRealEstateId"))
public class RealEstateMedia implements Parcelable {
    @NonNull
    private String mMediaUrl, mMediaCaption;

    private long mRealEstateId;

    @PrimaryKey(autoGenerate = true)
    private long mID;

    public RealEstateMedia(long mID,long realEstateId, @NonNull String mediaUrl, @NonNull String mediaCaption) {
        this.mID = mID;
        mRealEstateId = realEstateId;
        mMediaUrl = mediaUrl;
        mMediaCaption = mediaCaption;
    }

    protected RealEstateMedia(Parcel in) {
        mMediaUrl = in.readString();
        mMediaCaption = in.readString();
        mRealEstateId = in.readLong();
        mID = in.readLong();
    }

    public static final Creator<RealEstateMedia> CREATOR = new Creator<RealEstateMedia>() {
        @Override
        public RealEstateMedia createFromParcel(Parcel in) {
            return new RealEstateMedia(in);
        }

        @Override
        public RealEstateMedia[] newArray(int size) {
            return new RealEstateMedia[size];
        }
    };

    public long getRealEstateId() {
        return mRealEstateId;
    }

    public void setRealEstateId(long realEstateId) {
        mRealEstateId = realEstateId;
    }

    public long getID() {
        return mID;
    }

    public void setID(long ID) {
        mID = ID;
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

    public static RealEstateMedia[] getMediaExamples() {
        return new RealEstateMedia[] {

        new RealEstateMedia(0,1,"https://aaronkirman.com/wp-content/uploads/2022/01/The-One-Gallery-1.jpg","Whole house"),
        new RealEstateMedia(0,1,"https://aaronkirman.com/wp-content/uploads/2022/01/The-One-Gallery-13.jpg","Play Room"),
        new RealEstateMedia(0,1,"https://aaronkirman.com/wp-content/uploads/2022/01/The-One-Gallery-16.jpg","Library"),
        new RealEstateMedia(0,1,"https://aaronkirman.com/wp-content/uploads/2022/01/The-One-Gallery-24.jpg","Bedroom"),
        new RealEstateMedia(0,2,"https://aaronkirman.com/wp-content/uploads/2022/04/163A5194.jpg","Outside"),
        new RealEstateMedia(0,2,"https://aaronkirman.com/wp-content/uploads/2022/04/163A3878.jpg","Inside 1"),
        new RealEstateMedia(0,2,"https://aaronkirman.com/wp-content/uploads/2022/04/163A3930.jpg","Inside 2"),
        new RealEstateMedia(0,2,"https://aaronkirman.com/wp-content/uploads/2022/04/163A4070.jpg","Inside 3"),
        new RealEstateMedia(0,3,"https://aaronkirman.com/wp-content/uploads/2022/01/DJI_0648.jpg","Outside"),
        new RealEstateMedia(0,3,"https://aaronkirman.com/wp-content/uploads/2022/01/163A0232.jpg","Inside 1"),
        new RealEstateMedia(0,3,"https://aaronkirman.com/wp-content/uploads/2022/01/163A0320.jpg","Inside 2")
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(mMediaUrl);
        parcel.writeString(mMediaCaption);
        parcel.writeLong(mRealEstateId);
        parcel.writeLong(mID);
    }
}
