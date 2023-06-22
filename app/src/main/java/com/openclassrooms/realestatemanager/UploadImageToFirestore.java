package com.openclassrooms.realestatemanager;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class UploadImageToFirestore {
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef = storage.getReference();
    private final StorageReference imagesRef = storageRef.child("images");

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
