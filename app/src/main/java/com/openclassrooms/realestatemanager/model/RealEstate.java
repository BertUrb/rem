package com.openclassrooms.realestatemanager.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
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
        isSync = tmpIsSync == 0 ? null : tmpIsSync == 1;
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
        dest.writeByte((byte) (isSync == null ? 0 : isSync ? 1 : 2));
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


    public static List<RealEstate> getDataExample() {

        String agentName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();


        List<RealEstate> estates = new ArrayList<>();
        String desc1 = "One of the finest properties in America, The One is situated on a four-acre Bel Air promontory, featuring the best views of Los Angeles. A moat that encompasses the property gives the impression that it’s floating on water. The 100,000-square-foot glass and marble compound holds 20 bedrooms, each with sweeping views of Los Angeles and the ocean, and 30 bathrooms, as well as every amenity in the world. It features a 30-car garage, four swimming pools, a two-story waterfall, two restaurant-grade kitchens, an indoor/outdoor nightclub with its own VIP room, a movie theater, charitable organization rooms, a bowling alley, a library with floor-to-ceiling windows, a full beauty salon, a spa with a steam room and jacuzzi, a cigar lounge, and a gym. Three smaller villas also sit on the property, spread across the four acres. The 5,000-square-foot master suite is an oasis within the mansion. Designed with its own separate office and walk-in closet, the suite also has its own pool and kitchen. To guarantee privacy, it’s isolated from the rest of the house.The mansion includes five elevators and LED ceilings that display images of moving clouds. The One features state-of-the-art technology, with a full security center. The design encompasses secondary corridors for staff to use.";
        String desc2 = "Privately situated in the exclusive Moraga Estates guard-gated community in Bel Air, this ground-up, newly built mansion is the epitome of luxury. With peaceful views of the manicured gardens, pool, and mountains, this French inspired estate was meticulously designed to satisfy Feng Shui principles and built with quality comparable to the traditional chateaus. With over 18,000 square feet the estate boasts the finest finishes throughout sourcing antique materials from Europe; the French antique light fixtures, doors and windows, and the 18th century, wood-burning fireplaces are art pieces in themselves. Through the dramatic entry with soaring 30' ceilings and a grand staircase, the formal living room with equally high ceilings leads into the backyard with a sparkling infinity edge pool and spa, mature landscaping, and a lanai with an outdoor bar and fireplace. The chef's kitchen features professional grade appliances and two prep kitchens adjacent to the opulent formal dining room. In a world of its own surrounded by greenery, the exquisite master suite features incredible wood detail, 18' high ceilings, dual closets, a bathroom fit for a queen, and an expansive terrace to enjoy morning coffee. Among others, amenities include a beautiful library, a massive soundproof gym/spa flex space that could be converted to a music studio, an architecturally stunning staircase that leads into the basement to enjoy the home theater and entertainment space. In addition to the detail experienced at every turn, this property includes a 3 car garage with high ceilings allowing space for a lift to house up to 6 cars, and a fully detached two bedroom guest house. This French Chateau must be seen to truly recognize the detail and beauty of this once in a lifetime estate";
        String desc3 = "Poised high atop a promontory, this palatial European Estate boasts jaw-dropping 360 degree views of all of Los Angeles. Rich in privacy behind gates and up the long tree-lined driveway, this fortress of unparalleled magnitude is revealed. Situated in a world of its own overlooking the stunning gardens and city, this home is the epitome of royal living in the most sought-after city in the country. High ceilings, ornate details and grand-scale rooms are showcased beyond the luxurious foyer and Imperial Staircase through the massive double door entrance. Highlights from the main floor include an industrial grade chef's kitchen with an adjacent light and bright breakfast room, a formal dining room with double doors made with Venetian stained glass windows, an impressive living room with a gorgeous bar and fireplace, a billiards room, and a formal sitting room. Among the many jewels of this home is the two story library which includes a steel spiral staircase and a vibrant irreplaceable stained glass light fixture. Upstairs, the master bedroom rivals that of a European castle with multiple private terraces, city and nature views, his and hers closets, a massage room and a luxurious bathroom made with lapis stone. In addition to the master suite there are four oversized en suite bedrooms each with beautiful views and walk in closets. The lower level is an entertainer's paradise with a theater, ballroom that opens up to a sprawling terrace where your guests can soak in the picturesque views, and a wine cellar that's authentically outfitted as the storefront of an early 18th century English saloon. The backyard consists of a sparkling pool and spa, outdoor kitchen, putting green, a fruit and vegetable garden, tennis court, koi pond, and endless places for dining al fresco. In addition to the various amenities, the home is also equipped with an oversized gym. This tranquil oasis is quintessential to the highest class of luxury living, built for those who hold history, design, and beauty in the highest regard.";


        estates.add(new RealEstate(1, "The one", "BEL AIR, CA 90077", "The one BEL AIR, CA 90077", desc1, 280000000, 50000, 56, 12, 24, "https://aaronkirman.com/wp-content/uploads/2022/01/The-One-Gallery-1.jpg", agentName));
        estates.add(new RealEstate(2, "11630 Moraga Ln", "LOS ANGELES, CA 90049", "11630 Moraga Ln LOS ANGELES, CA 90049", desc2, 18800000, 46000, 10, 10, 10, "https://aaronkirman.com/wp-content/uploads/2022/04/163A3878.jpg", agentName));
        estates.add(new RealEstate(3, "1420 Davies Dr", "BEVERLY HILLS, CA 90210", "1420 Davies Dr BEVERLY HILLS, CA 90210", desc3, 87000000, 113000, 17, 12, 12, "https://aaronkirman.com/wp-content/uploads/2022/01/163A0320.jpg", agentName));


        return estates;

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

