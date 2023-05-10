package com.openclassrooms.realestatemanager.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.RealEstateRvAdapter;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.event.OnRealEstateClickListener;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.RealEstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private final List<RealEstate> mEstates = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        RealEstateViewModel realEstateViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(RealEstateViewModel.class);
        realEstateViewModel.getRealEstates().observe(this, this::getEstatesObserver);


    }

    private void getEstatesObserver(List<RealEstate> estates) {
        mEstates.clear();
        mEstates.addAll(estates);
        if (estates.size() > 0)
            updateEstates();

    }


    private void updateEstates() {
        OnRealEstateClickListener onRealEstateClickListener = position -> {
            RealEstate realEstate = mEstates.get(position);


            if (mBinding.detailViewContainer.getVisibility() == View.VISIBLE) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("REAL_ESTATE", realEstate);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);
                int color = Color.BLUE;
                String title = realEstate.getName();
                if (realEstate.getSaleDate() != null) {
                    color = Color.RED;
                    title += " " + getString(R.string.sold, realEstate.getSaleDate());
                }
                Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(color));
                getSupportActionBar().setTitle(title);

                transaction.replace(mBinding.FragmentContainer.getId(), fragment);
                transaction.commit();

            } else {
                Intent intent = new Intent(this, SupportActivity.class);
                intent.putExtra("REAL_ESTATE", realEstate);
                startActivity(intent);

            }

        };
        mBinding.realEstateListRv.setAdapter(new RealEstateRvAdapter(mEstates, onRealEstateClickListener));


        ScrollView detailViewContainer = mBinding.detailViewContainer;
        if (Utils.isDeviceTablet(getApplicationContext())) {
            detailViewContainer.setVisibility(View.VISIBLE);
            onRealEstateClickListener.OnRealEstateClick(0);


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
            db.collection("estates").addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    Log.w("TAG", "Erreur lors de l'écoute des modifications", e);
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        RealEstate estate = RealEstate.fromQueryDocumentSnapshot(dc.getDocument());
                        if (!mEstates.contains(estate)) {
                            mEstates.add(estate);
                        }
                    }
                }
            });
        }
    }
}