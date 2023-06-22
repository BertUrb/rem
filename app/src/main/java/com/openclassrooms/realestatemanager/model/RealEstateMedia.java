package com.openclassrooms.realestatemanager.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Objects;

@Entity(foreignKeys = @ForeignKey(entity = RealEstate.class,parentColumns = "mID",childColumns = "mRealEstateId"),
        indices = @Index(value = "mRealEstateId"))
public class RealEstateMedia implements Parcelable {
    @NonNull
    private String mMediaUrl;
    @NonNull
    private String mMediaCaption;

    private long mRealEstateId;


    @PrimaryKey(autoGenerate = true)
    private long mID;

    @Ignore
    private Boolean isSync = false;
    private String mFirestoreUrl = "";

    protected RealEstateMedia(Parcel in) {
        mMediaUrl = in.readString();
        mMediaCaption = in.readString();
        mRealEstateId = in.readLong();
        mID = in.readLong();
        byte tmpIsSync = in.readByte();
        isSync = tmpIsSync == 0 ? null : tmpIsSync == 1;
        mFirestoreUrl = in.readString();
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

    public Boolean getSync() {
        return isSync;
    }

   public String getFirestoreUrl() {
        return mFirestoreUrl;
   }


    public void setSync(Boolean sync) {
        isSync = sync;
    }

    public RealEstateMedia(long mID, long realEstateId, @NonNull String mediaUrl, @NonNull String mediaCaption,String firestoreUrl) {
        this.mID = mID;
        mRealEstateId = realEstateId;
        mMediaUrl = mediaUrl;
        mMediaCaption = mediaCaption;
        mFirestoreUrl = firestoreUrl;
    }

    @Ignore
    public RealEstateMedia(@NonNull String mediaUrl, @NonNull String mediaCaption) {
        mMediaUrl = mediaUrl;
        mMediaCaption = mediaCaption;

    }
    @Ignore
    public RealEstateMedia(long realEstateId, @NonNull String mediaUrl, @NonNull String mediaCaption) {
        mRealEstateId = realEstateId;
        mMediaUrl = mediaUrl;
        mMediaCaption = mediaCaption;

    }

    public long getRealEstateId() {
        return mRealEstateId;
    }

    public void setRealEstateId(long realEstateId) {
        mRealEstateId = realEstateId;
    }

    public long getID() {
        return mID;
    }

    @NonNull
    public String getMediaUrl() {
        return mMediaUrl;
    }

    @NonNull
    public String getMediaCaption() {
        return mMediaCaption;
    }

    public void setMediaCaption(@NonNull String mediaCaption) {
        mMediaCaption = mediaCaption;
    }

    public HashMap<String, Object> toHashMap(String agentName) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("mediaUrl", mFirestoreUrl);
        map.put("mediaCaption",mMediaCaption);
        map.put("realEstateId",agentName + mRealEstateId);

        return map;

    }

    public static RealEstateMedia fromQueryDocumentSnapshot(QueryDocumentSnapshot document,String agent) {

        String idS = Objects.requireNonNull(document.getString("realEstateId")).substring(agent.length());

        Log.d("TAG", "fromQueryDocumentSnapshot: " + idS);

            int id = Integer.parseInt(idS);


        RealEstateMedia media = new  RealEstateMedia(id,
                Objects.requireNonNull(document.getString("mediaUrl")),
                Objects.requireNonNull(document.getString("mediaCaption")));

        media.setSync(false);

        return media;
    }


    public void setMediaURL(String url) {
        mMediaUrl = url;
    }


    public void setFirestoreUrl(String url) {
        mFirestoreUrl = url;
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
        parcel.writeByte((byte) (isSync == null ? 0 : isSync ? 1 : 2));
        parcel.writeString(mFirestoreUrl);
    }
}
