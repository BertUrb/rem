package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.openclassrooms.realestatemanager.event.OnMapCreated;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SaveImageTask {
    private final Context mContext;
    private final OnMapCreated mMapCreated;
    private File mFile;

    public SaveImageTask(Context context, OnMapCreated mapCreated) {
        mContext = context;
        mMapCreated = mapCreated;
    }

    public void execute(String imageUrl, String name) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Create a Target to download the Bitmap
            FutureTarget<Bitmap> target = Glide.with(mContext)
                    .asBitmap()
                    .load(imageUrl)
                    .submit();

            try {
                // Retrieve the Bitmap from the Target
                Bitmap bitmap = target.get();

                // Save the Bitmap to a file
                mFile = new File(mContext.getFilesDir(), name);
                FileOutputStream fos = new FileOutputStream(mFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();

                mMapCreated.onMapCreated(mFile);
            } catch (InterruptedException | ExecutionException | IOException e) {
                e.printStackTrace();
                // Handle error if necessary
            }
        });
    }
}


