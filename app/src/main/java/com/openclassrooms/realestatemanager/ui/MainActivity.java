package com.openclassrooms.realestatemanager.ui;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.RealEstateRvAdapter;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.event.OnRealEstateClickListener;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private boolean filtered = false;
    private final List<RealEstate> mEstates = new ArrayList<>();
    private RealEstate mEstate;
    private RealEstateViewModel mRealEstateViewModel;


    private final ActivityResultLauncher<Intent> mEditRealEstateLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            RealEstate editedEstate = result.getData().getParcelableExtra("EDITED_REAL_ESTATE");
                            mRealEstateViewModel.createOrUpdateRealEstate(editedEstate);


                        }
                    });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem searchMenuItem = menu.findItem(R.id.menu_search_button);

        if(filtered)
        {

            searchMenuItem.setIcon(R.drawable.baseline_close_24);

        }
        else
            searchMenuItem.setIcon(R.drawable.baseline_search_24);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, RealEstateEditor.class);

        switch (item.getOrder()) {
            case 1 : // edit
                intent.putExtra("REAL_ESTATE",mEstate);
                mEditRealEstateLauncher.launch(intent);

                break;
            case 2 : // new
                mEditRealEstateLauncher.launch(intent);
                break;

            case 3 : //sell
                Calendar cal = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                    cal.set(year,month, dayOfMonth);
                    mEstate.setSaleDate(cal.getTime());
                    mRealEstateViewModel.createOrUpdateRealEstate(mEstate);
                }, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
            case 4 : //search


                if(filtered) {
                   startActivity(new Intent(this, MainActivity.class));
                }
                else {
                    SearchModal searchModal = new SearchModal();
                    searchModal.show(getSupportFragmentManager(), "searchModal");


                }

                break;

            case 5 : // map
                if(Utils.isInternetAvailable(this)){
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 10);
                    }
                    else {
                        Intent mapActivityIntent = new Intent(this, MapActivity.class);
                        startActivity(mapActivityIntent);
                    }
                }
                else {
                    Toast.makeText(this,getString(R.string.internet_is_required),Toast.LENGTH_LONG).show();
                }
                break;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mRealEstateViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(RealEstateViewModel.class);

        Intent intent = getIntent();

        if(intent.getParcelableArrayListExtra("filteredEstates") != null)
        {
            mEstates.clear();
            mEstates.addAll(intent.getParcelableArrayListExtra("filteredEstates"));
            Log.d("TAG", "FILTERED ");
            updateEstates();
            filtered = true;
        }
        else {
            mRealEstateViewModel.getRealEstates().observe(this, this::getEstatesObserver);
            filtered = false;
            Log.d("TAG", "PAS FILTERED ");
        }

    }

    private void checkFirestore() {
        if(Utils.isInternetAvailable(this)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();



            db.collection("estates")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        Log.d("TAG", "onCreate: DB SUCCESS ");
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            List<RealEstateMedia> mediaList = new ArrayList<>();
                            RealEstate estate = RealEstate.fromQueryDocumentSnapshot(documentSnapshot);
                            Log.d("TAG", "QuerySnapshot: " + estate.getName());

                            if (!mEstates.contains(estate)) {
                                Log.d("TAG", "New estate !! ");
                               mRealEstateViewModel.createOrUpdateRealEstate(estate);


                                db.collection("medias").whereEqualTo("realEstateId",estate.getID()).get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "MEDIA OK");

                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            RealEstateMedia media = RealEstateMedia.fromQueryDocumentSnapshot(document);
                                            Log.d("TAG", "media id: " + media.getID());

                                                mediaList.add(media);


                                        }
                                        estate.setMediaList(mediaList);
                                        mRealEstateViewModel.createOrUpdateRealEstate(estate);

                                    } else {
                                        Log.d("TAG", "Error getting documents: ", task.getException());
                                    }
                                });
                                mEstates.add(estate);

                                updateEstates();

                            }
                        }


                    });




            db.collection("estates").addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    Log.w("TAG", "Erreur lors de l'écoute des modifications", e);
                    return;
                }

                assert snapshots != null;
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        RealEstate estate = RealEstate.fromQueryDocumentSnapshot(dc.getDocument());
                        if (!mEstates.contains(estate)) {
                            mEstates.add(estate);
                        }
                    }
                }
            });
            SyncDB();
        }
    }

    private void getEstatesObserver(List<RealEstate> estates) {
        mEstates.clear();
        mEstates.addAll(estates);

        if (estates.size() > 0)
            updateEstates();

        checkFirestore();

    }


    private void updateEstates() {
        OnRealEstateClickListener onRealEstateClickListener = position -> {
            mEstate = mEstates.get(position);


            if (mBinding.detailViewContainer.getVisibility() == View.VISIBLE) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("REAL_ESTATE", mEstate);
                DetailsFragment fragment = new DetailsFragment();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                fragment.setArguments(bundle);
                int color = Color.BLUE;
                String title = mEstate.getName();
                if (mEstate.getSaleDate() != null) {
                    color = Color.RED;
                    title += " " + getString(R.string.sold, mEstate.getSaleDate());
                }
                Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(color));
                getSupportActionBar().setTitle(title);


                transaction.replace(mBinding.FragmentContainer.getId(), fragment);
                transaction.commit();


            } else {
                Intent intent = new Intent(this, SupportActivity.class);
                intent.putExtra("REAL_ESTATE", mEstate);
                startActivity(intent);

            }

        };
        mBinding.realEstateListRv.setAdapter(new RealEstateRvAdapter(mEstates, onRealEstateClickListener));


        ScrollView detailViewContainer = mBinding.detailViewContainer;
        if (Utils.isDeviceTablet(getApplicationContext())) {
            detailViewContainer.setVisibility(View.VISIBLE);
            if(!mEstates.isEmpty()) {
                mBinding.noResultsTextView.setVisibility(View.GONE);
                onRealEstateClickListener.OnRealEstateClick(0);

            } else {
                mBinding.noResultsTextView.setVisibility(View.VISIBLE);
            }

        } else {
            detailViewContainer.setVisibility(View.GONE);

        }


        setContentView(mBinding.getRoot());
        SyncDB();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SyncDB();
    }

    private void SyncDB() {

        if (Utils.isInternetAvailable(this)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            for (RealEstate estate : mEstates) {
                if (!estate.getSync()) {
                    db.collection("estates").document(estate.getAgentName() + estate.getID()).set(estate.toHashMap())
                            .addOnCompleteListener(task -> {
                                estate.setSync(true);
                                if (!task.isSuccessful()) {
                                    estate.setSync(false);
                                }
                            });

                }
            }

        }
    }
}