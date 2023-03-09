package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SaveImageTask extends AsyncTask<String, Void, Boolean> {
    private Context mContext;

    public SaveImageTask(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String imageUrl = params[0];
        String name = params[1];

        // Create a FutureTarget to download the Bitmap
        FutureTarget<Bitmap> futureTarget =
                Glide.with(mContext)
                        .asBitmap()
                        .load(imageUrl)
                        .submit();

        try {
            // Retrieve the Bitmap from the FutureTarget
            Bitmap bitmap = futureTarget.get();

            // Save the Bitmap to a file
            File file = new File(mContext.getFilesDir(), name);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            return true;

        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
      }
}

