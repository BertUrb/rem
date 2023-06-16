package com.openclassrooms.realestatemanager;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class UploadImageToFirestore {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private StorageReference imagesRef = storageRef.child("images");

    public MutableLiveData<String> uploadFromImageView(ImageView imageView, String name) {
        StorageReference mediaRef = imagesRef.child(name + ".jpg");

        MutableLiveData<String> url = new MutableLiveData<>();
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();

        Drawable drawable = imageView.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mediaRef.putBytes(data);
            uploadTask.addOnFailureListener(e -> Log.e("TAG", "uploadFromImageView: ", e))
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.d("TAG", "uploadFromImageView: Upload SUCCESSFUL");
                        mediaRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            url.setValue(downloadUrl);
                        });
                    });
        } else {
            Log.e("TAG", "uploadFromImageView: Invalid ImageView drawable");
        }

        return url;
    }

    public MutableLiveData<String> uploadFromDrawable(Drawable drawable, String name) {
        Log.d("TAG", "uploadFromDrawable: ");
        StorageReference mediaRef = imagesRef.child(sanitizeName(name) + ".jpg");

        MutableLiveData<String> url = new MutableLiveData<>();

        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mediaRef.putBytes(data);
            uploadTask.addOnFailureListener(e -> Log.e("TAG", "uploadFromDrawable: ", e))
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.d("TAG", "uploadFromDrawable: Upload SUCCESSFUL");
                        mediaRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            url.setValue(downloadUrl);
                            Log.d("TAG", "uploadFromDrawable: " + downloadUrl);
                        });
                    });
        } else {
            Log.e("TAG", "uploadFromDrawable: Invalid drawable");
        }

        return url;
    }
    private String sanitizeName(String ori) {
        return ori.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
    public MutableLiveData<File> downloadImage(String url) throws IOException {
        Log.d("TAG", "downloadImage: " + url);
        StorageReference httpsReference = storage.getReferenceFromUrl(url);

        File localFile = File.createTempFile("images", "jpg");
        MutableLiveData<File> res = new MutableLiveData<>();

        httpsReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            try {
                res.setValue(localFile);
                Log.d("TAG", "downloadImage: DOWNLOAD OK ");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).addOnFailureListener(e -> {
            Log.e("TAG", "downloadImage: ", e);
            res.setValue(null); // Set the value to null to indicate failure
        });

        return res;
    }

}
