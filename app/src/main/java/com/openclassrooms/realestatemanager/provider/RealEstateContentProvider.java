package com.openclassrooms.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.database.SaveRealEstateDB;
import com.openclassrooms.realestatemanager.model.RealEstate;

public class RealEstateContentProvider  extends ContentProvider {


    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider";

    public static final String TABLE_NAME = RealEstate.class.getSimpleName();

    public static final Uri URI_ITEM = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (getContext() != null) {

            long userId = ContentUris.parseId(uri);

            final Cursor cursor = SaveRealEstateDB.getInstance(getContext()).realEstateDao().getRealEstateWithCursor(userId);

            cursor.setNotificationUri(getContext().getContentResolver(), uri);

            return cursor;

        }

        throw new IllegalArgumentException("Failed to query row for uri " + uri);

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        //provider is read only
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        //provider is read only
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        //provider is read only
        return 0;
    }
}
