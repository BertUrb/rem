package com.openclassrooms.realestatemanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.openclassrooms.realestatemanager.databinding.ActivitySupportBinding;
import com.openclassrooms.realestatemanager.model.RealEstate;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySupportBinding binder = ActivitySupportBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());
        Bundle bundle = new Bundle();
        RealEstate realEstate = getIntent().getParcelableExtra("REAL_ESTATE");
        bundle.putParcelable("REAL_ESTATE", realEstate );


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(bundle);

        transaction.replace(binder.SupportFrame.getId(), fragment);
        getSupportActionBar().setTitle(realEstate.getName());
        transaction.commit();

    }
}