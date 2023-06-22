package com.openclassrooms.realestatemanager.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
public class RealEstate implements Parcelable {

    @ColumnInfo(name = "listing_date")
    Date mListingDate;
    @ColumnInfo(name = "sale_date")
    Date mSaleDate;
    @PrimaryKey(autoGenerate = true)
    private long mID;
    private String mName;

    private String mJsonPoint;

    private String mRegion;
    private String mLocation;
    private String mDescription;
    private String mFeaturedMediaUrl;
    @Ignore
    private Boolean isSync = false;

    private String mAgentName;
    private int mPrice,
            mSurface,
            mRooms,
            mBathrooms,
            mBedrooms;
    @Ignore
    private List<RealEstateMedia> mMediaList;

    protected RealEstate(Parcel in) {
        mID = in.readLong();
        mName = in.readString();
        mJsonPoint = in.readString();
        mRegion = in.readString();
        mLocation = in.readString();
        mDescription = in.readString();
        mFeaturedMediaUrl = in.readString();
        byte tmpIsSync = in.readByte();
        isSync = tmpIsSync == 1;
        mAgentName = in.readString();
        mPrice = in.readInt();
        mSurface = in.readInt();
        mRooms = in.readInt();
        mBathrooms = in.readInt();
        mBedrooms = in.readInt();
        mMediaList = in.createTypedArrayList(RealEstateMedia.CREATOR);

        long l = in.readLong();
        if(l != 0) {
            mSaleDate = new Date(l);
            Log.d("TAG", "read: " + mSaleDate);
        }
        else {
            mSaleDate = null;
        }
        l = in.readLong();
        if(l != 0) {
            mListingDate = new Date(l);
        }
        else {
            mListingDate = null;
        }

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mID);
        dest.writeString(mName);
        dest.writeString(mJsonPoint);
        dest.writeString(mRegion);
        dest.writeString(mLocation);
        dest.writeString(mDescription);
        dest.writeString(mFeaturedMediaUrl);
        dest.writeByte((byte) (isSync != null && isSync ? 1 : 0));
        dest.writeString(mAgentName);
        dest.writeInt(mPrice);
        dest.writeInt(mSurface);
        dest.writeInt(mRooms);
        dest.writeInt(mBathrooms);
        dest.writeInt(mBedrooms);
        dest.writeTypedList(mMediaList);
        if(mSaleDate == null)
        {
            dest.writeLong(0);

        }
        else {
            Log.d("TAG", "writeToParcel: " + mSaleDate);
            dest.writeLong(mSaleDate.getTime());
        }

        if (mListingDate != null)
        {
            dest.writeLong(mListingDate.getTime());
        }
        else {
            dest.writeLong(0);
        }

    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getJsonPoint() {
        return mJsonPoint;
    }

    public void setJsonPoint(String jsonPoint) {
        mJsonPoint = jsonPoint;
    }



    public Boolean getSync() {
        return isSync;
    }

    public void setSync(Boolean sync) {
        isSync = sync;
    }

    public void setListingDate(Date listingDate) {
        mListingDate = listingDate;
    }

    public String getAgentName() {
        return mAgentName;
    }

    public void setAgentName(String agentName) {
        mAgentName = agentName;
    }




    public RealEstate() {
        // require empty constructor
    }
    @Ignore
    public RealEstate(long mID, String name, String region, String location, String description, int price, int surface, int rooms, int bathrooms, int bedrooms, String featuredMediaUrl, String agentName) {
        this.mID = mID;
        mName = name;
        mRegion = region;
        mLocation = location;
        mDescription = description;
        mPrice = price;
        mSurface = surface;
        mRooms = rooms;
        mBathrooms = bathrooms;
        mBedrooms = bedrooms;
        mFeaturedMediaUrl = featuredMediaUrl;
        mListingDate = new Date();
        mAgentName = agentName;
    }

    @Ignore
    public RealEstate(long mID, String name, String region, int price, String featuredMediaUrl) {
        this.mID = mID;
        mName = name;
        mRegion = region;
        mPrice = price;
        mFeaturedMediaUrl = featuredMediaUrl;
    }

    public RealEstate(String name, String region, String location, String description, String featuredMediaUrl, int price, int surface, int rooms, int bathrooms, int bedrooms, List<RealEstateMedia> mediaList) {
        mName = name;
        mRegion = region;
        mLocation = location;
        mDescription = description;
        mFeaturedMediaUrl = featuredMediaUrl;
        mPrice = price;
        mSurface = surface;
        mRooms = rooms;
        mBathrooms = bathrooms;
        mBedrooms = bedrooms;
        mMediaList = mediaList;
    }


    public Date getListingDate() {
        return mListingDate;
    }

    public Date getSaleDate() {
        return mSaleDate;
    }

    public void setSaleDate(Date saleDate) {
        mSaleDate = saleDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public long getID() {
        return mID;
    }

    public void setID(long ID) {
        mID = ID;
    }

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

    public String getFeaturedMediaUrl() {
        return mFeaturedMediaUrl;
    }

    public void setFeaturedMediaUrl(String featuredMediaUrl) {
        mFeaturedMediaUrl = featuredMediaUrl;
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

    public void setPrice(int priceInDollar) {
        mPrice = priceInDollar;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("listingDate", mListingDate);
        map.put("saleDate",mSaleDate);
        map.put("name", mName);
        map.put("jsonPoint", mJsonPoint);
        map.put("region", mRegion);
        map.put("location", mLocation);
        map.put("description", mDescription);
        map.put("featuredMediaUrl", mFeaturedMediaUrl);
        map.put("agentName", mAgentName);
        map.put("price", mPrice);
        map.put("Surface", mSurface);
        map.put("rooms", mRooms);
        map.put("bathrooms", mBathrooms);
        map.put("bedrooms", mBedrooms);


        return map;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealEstate estate = (RealEstate) o;
        return getName().equals(estate.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }

    public static RealEstate fromQueryDocumentSnapshot(QueryDocumentSnapshot document) {

        String agentName = document.getString("agentName");
        assert agentName != null;

        RealEstate realEstate = new RealEstate(
                document.getString("name"),
                document.getString("region"), document.getString("location"), document.getString("description"),
                document.getString("featuredMediaUrl"),
                Objects.requireNonNull(document.getLong("price")).intValue(),
                Objects.requireNonNull(document.getLong("Surface")).intValue(),
                Objects.requireNonNull(document.getLong("rooms")).intValue(),
                Objects.requireNonNull(document.getLong("bathrooms")).intValue(),
                Objects.requireNonNull(document.getLong("bedrooms")).intValue(),
                null);
        realEstate.setAgentName(agentName);

        realEstate.setSync(true);
        realEstate.setListingDate(document.getDate("listingDate"));

        return realEstate;

    }

    public static RealEstate fromContentValues(ContentValues values) {
        final RealEstate estate = new RealEstate();

        if(values.containsKey("mID")) estate.setID(values.getAsLong("mID"));

        if(values.containsKey("mName")) estate.setName(values.getAsString("mName"));

        if(values.containsKey("mJsonPoint")) estate.setJsonPoint(values.getAsString("mJsonPoint"));

        if(values.containsKey("mRegion")) estate.setRegion(values.getAsString("mRegion"));

        if(values.containsKey("mLocation")) estate.setLocation(values.getAsString("mLocation"));


        if(values.containsKey("mDescription")) estate.setDescription(values.getAsString("mDescription"));

        if(values.containsKey("mFeaturedMediaUrl")) estate.setFeaturedMediaUrl(values.getAsString("mFeaturedMediaUrl"));

        if(values.containsKey("mAgentName")) estate.setAgentName(values.getAsString("mAgentName"));

        if(values.containsKey("mPrice")) estate.setPrice(values.getAsInteger("mPrice"));

        if (values.containsKey("mSurface")) estate.setSurface(values.getAsInteger("mSurface"));

        if(values.containsKey("mRooms")) estate.setRooms(values.getAsInteger("mRooms"));

        if(values.containsKey("mBathrooms")) estate.setBathrooms(values.getAsInteger("mBathrooms"));

        if(values.containsKey("mBedrooms")) estate.setBedrooms(values.getAsInteger("mBedrooms"));

        if(values.containsKey("listing_date")) estate.setListingDate(new Date(values.getAsLong("listing_date")));

        if(values.containsKey("sale_date")) estate.setSaleDate(new Date(values.getAsLong("sale_date")));

        return estate;
    }

}

