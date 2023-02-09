package com.openclassrooms.realestatemanager.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.openclassrooms.realestatemanager.databinding.ActivityRealEstateEditorBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RealEstateEditor extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGES = 1;
    private static final int REQUEST_IMAGE_CAPTURE=2,
                            REQUEST_PERMISSION_CODE =3;
    private List<Uri> mSelectedImagesUri = new ArrayList<>();

    ActivityRealEstateEditorBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityRealEstateEditorBinding.inflate(getLayoutInflater());
        mBinding.ivPhoto.setOnClickListener(view -> checkPermissions());
        setContentView(mBinding.getRoot());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                photoOrGalleryDialog();
            } else {
                // permission denied, boo! Disable the functionality that depends on this permission.
                //Todo
            }

        }
    }

    private void pickImages() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Images"), REQUEST_CODE_PICK_IMAGES);
    }
    private void photoOrGalleryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");
        builder.setMessage("Take a photo or choose from gallery?");

        builder.setPositiveButton("Take a photo", (dialog, which) -> takePhoto());

        builder.setNegativeButton("Choose from gallery", (dialog, which) -> pickImages());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGES && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        mSelectedImagesUri.add(imageUri);
                    }
                } else {
                    Uri imageUri = data.getData();
                    mSelectedImagesUri.add(imageUri);
                }


            }
        }
        else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            assert data != null;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            saveImageToGallery(imageBitmap);

        }
        mBinding.rvSelectedPhotos.setAdapter(new RealEstateEditorRvAdapter(mSelectedImagesUri));
    }
    private void saveImageToGallery(Bitmap bitmap) {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // Create a file to save the image
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "IMG_" + timeStamp + ".jpeg");
        try {
            // Create an output stream and write the bitmap to it
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            // Add the image to the gallery so it is accessible from other apps
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mSelectedImagesUri.add(contentUri);

            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        }
    }

}