package com.openclassrooms.realestatemanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivitySupportBinding;
import com.openclassrooms.realestatemanager.model.RealEstate;

import java.util.Objects;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySupportBinding binder = ActivitySupportBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());

        RealEstate realEstate = getIntent().getParcelableExtra("REAL_ESTATE");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(realEstate != null) {
            //real estate
            Bundle bundle = new Bundle();
            bundle.putParcelable("REAL_ESTATE", realEstate);


             DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(bundle);

            transaction.replace(binder.SupportFrame.getId(), fragment);

            int color = Color.BLUE;
            String title = realEstate.getName();
            if (realEstate.getSaleDate() != null) {
                color = Color.RED;
                title += " " + getString(R.string.sold, realEstate.getSaleDate());
            }
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(color));
            Objects.requireNonNull(getSupportActionBar()).setTitle(title);

            transaction.commit();

        }




    }
}