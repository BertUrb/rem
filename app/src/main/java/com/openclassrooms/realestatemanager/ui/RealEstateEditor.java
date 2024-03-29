package com.openclassrooms.realestatemanager.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.realestatemanager.databinding.ActivityRealEstateEditorBinding;
import com.openclassrooms.realestatemanager.event.EditTextFocusListener;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class RealEstateEditor extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGES = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2,
            REQUEST_PERMISSION_CODE = 3;
    private ActivityRealEstateEditorBinding mBinding;
    private RealEstate mRealEstate;
    private List<RealEstateMedia> mRealEstateMedias = new ArrayList<>();

    private final ActivityResultLauncher<Intent> mPickImagesLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    assert result.getData() != null;
                    if (result.getData().getClipData() != null) {
                        for (int i = 0; i < result.getData().getClipData().getItemCount(); i++) {
                            Uri uri = result.getData().getClipData().getItemAt(i).getUri();
                            try {
                                String name=  UUID.randomUUID().toString();
                                String path = getFilesDir() + "/" + name  ;
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                File file = new File(this.getFilesDir(), name);
                                FileOutputStream fos = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                fos.flush();
                                fos.close();
                                RealEstateMedia media;
                                if (mRealEstate == null) {
                                    media = new RealEstateMedia(path, "");
                                } else {
                                    media = new RealEstateMedia(mRealEstate.getID(), path, "");
                                }
                                Log.d("TAG", "file abs path: " + path);
                                mRealEstateMedias.add(media);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        try {
                            Uri uri = result.getData().getData();
                            String name=  UUID.randomUUID().toString();
                            String path = getFilesDir() + "/" + name  ;
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            File file = new File(this.getFilesDir(), name);

                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();
                            RealEstateMedia media;
                            if (mRealEstate == null) {
                                media = new RealEstateMedia(path, "");
                            } else {
                                media = new RealEstateMedia(mRealEstate.getID(), path, "");
                                Log.d("TAG", "file abs path: " + path);
                            }
                            mRealEstateMedias.add(media);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }


                    }


                }
            });


    private AlertDialog.Builder mBuilder;
    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Handle the result of the picture capture here

                    onActivityResult(REQUEST_IMAGE_CAPTURE, Activity.RESULT_OK, result.getData());

                }
            });

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Add Photo");
        mBuilder.setMessage("Take a photo or choose from gallery?");

        mBuilder.setPositiveButton("Take a photo", (dialog, which) -> takePhoto());

        mBuilder.setNegativeButton("Choose from gallery", (dialog, which) -> pickImages());

        return super.onCreateView(parent, name, context, attrs);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityRealEstateEditorBinding.inflate(getLayoutInflater());
        mBinding.ivPhoto.setOnClickListener(view -> checkPermissions());
        setContentView(mBinding.getRoot());

        mBinding.etBathrooms.setOnFocusChangeListener(new EditTextFocusListener());
        mBinding.etPrice.setOnFocusChangeListener(new EditTextFocusListener());
        mBinding.etLocation.setOnFocusChangeListener(new EditTextFocusListener());
        mBinding.etSurface.setOnFocusChangeListener(new EditTextFocusListener());
        mBinding.etRooms.setOnFocusChangeListener(new EditTextFocusListener());
        mBinding.etBedrooms.setOnFocusChangeListener(new EditTextFocusListener());
        mBinding.etName.setOnFocusChangeListener(new EditTextFocusListener());
        mBinding.etRegion.setOnFocusChangeListener(new EditTextFocusListener());


        mRealEstate = getIntent().getParcelableExtra("REAL_ESTATE");
        if (mRealEstate != null) {
            mBinding.etBathrooms.setText(String.format(Locale.getDefault(), "%d", mRealEstate.getBathrooms()));
            mBinding.etBedrooms.setText(String.format(Locale.getDefault(), "%d", mRealEstate.getBedrooms()));
            mBinding.etRooms.setText(String.format(Locale.getDefault(), "%d", mRealEstate.getRooms()));
            mBinding.etName.setText(mRealEstate.getName());
            mBinding.etRegion.setText(mRealEstate.getRegion());
            mBinding.textInputEditTextDescription.setText(mRealEstate.getDescription());
            mRealEstateMedias = mRealEstate.getMediaList();
            mBinding.rvSelectedPhotos.setAdapter(new RealEstateEditorRvAdapter(mRealEstateMedias));
            mBinding.etSurface.setText(String.format(Locale.getDefault(), "%d", mRealEstate.getSurface()));
            mBinding.etLocation.setText(mRealEstate.getLocation());
            mBinding.etPrice.setText(String.format(Locale.getDefault(), "%d", mRealEstate.getPrice()));
        }
        mBinding.btSave.setOnClickListener(view -> btSaveClick());


    }

    private void btSaveClick() {
        for (RealEstateMedia media : mRealEstateMedias) {
            int index = mRealEstateMedias.indexOf(media);
            if (index >= 0 && index < mBinding.rvSelectedPhotos.getChildCount()) {
                media.setMediaCaption(((RealEstateEditorRvViewHolder) Objects.requireNonNull(mBinding.rvSelectedPhotos.findViewHolderForAdapterPosition(index))).getCaption().getText().toString());
            }
        }
        RealEstate realEstate = new RealEstate(mBinding.etName.getText().toString(),
                mBinding.etRegion.getText().toString(),
                mBinding.etLocation.getText().toString(),
                Objects.requireNonNull(mBinding.textInputEditTextDescription.getText()).toString(),
                mRealEstateMedias.get(0).getMediaUrl(),
                Integer.parseInt(mBinding.etPrice.getText().toString()),
                Integer.parseInt(mBinding.etSurface.getText().toString()),
                Integer.parseInt(mBinding.etRooms.getText().toString()),
                Integer.parseInt(mBinding.etBathrooms.getText().toString()),
                Integer.parseInt(mBinding.etBedrooms.getText().toString()),
                mRealEstateMedias);

        realEstate.setAgentName(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());

        if (mRealEstate != null) {
            realEstate.setID(mRealEstate.getID());

            Log.d("TAG", "onCreate: " + mRealEstate.getSaleDate());

            if (mRealEstate.getSaleDate() != null) {
                realEstate.setSaleDate(mRealEstate.getSaleDate());

            }
            if (mRealEstate.getListingDate() != null) {
                realEstate.setListingDate(mRealEstate.getListingDate());
            }
            else {
                realEstate.setListingDate(new Date());
            }
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("EDITED_REAL_ESTATE", realEstate);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                photoOrGalleryDialog();
            }

        }
    }

    private void pickImages() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");

        mPickImagesLauncher.launch(Intent.createChooser(intent, "Select Images"));
    }

    private void photoOrGalleryDialog() {
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void takePhoto() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
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
                        RealEstateMedia media;
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        getContentResolver().takePersistableUriPermission(imageUri, (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));

                        if (mRealEstate == null) {
                            media = new RealEstateMedia(imageUri.toString(), "");
                        } else {
                            media = new RealEstateMedia(mRealEstate.getID(), imageUri.toString(), "");
                        }
                        mRealEstateMedias.add(media);
                    }
                } else {
                    RealEstateMedia media;
                    Uri imageUri = data.getData();
                    getContentResolver().takePersistableUriPermission(imageUri, (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));

                    if (mRealEstate == null) {
                        media = new RealEstateMedia(data.getData().toString(), "");
                    } else {
                        media = new RealEstateMedia(mRealEstate.getID(), data.getData().toString(), "");
                    }
                    mRealEstateMedias.add(media);
                }


            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            assert data != null;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            saveImageToGallery(imageBitmap);

        }
        mBinding.rvSelectedPhotos.setAdapter(new RealEstateEditorRvAdapter(mRealEstateMedias));
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
            mRealEstateMedias.add(new RealEstateMedia(contentUri.toString(), ""));

            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_MEDIA_IMAGES},
                        REQUEST_PERMISSION_CODE);
            }
        } else {
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
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        REQUEST_PERMISSION_CODE);

            }
        }

    }
}