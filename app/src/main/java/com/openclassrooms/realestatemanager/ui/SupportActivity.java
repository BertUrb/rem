package com.openclassrooms.realestatemanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.openclassrooms.realestatemanager.databinding.ActivitySupportBinding;
import com.openclassrooms.realestatemanager.model.RealEstate;

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
            getSupportActionBar().setTitle(realEstate.getName());
            transaction.commit();

        }
        else {
            //map
           /* MapFragment mapFragment = MapFragment.newInstance(null);

            // Set the layout parameters for the fragment

            transaction.replace(binder.SupportFrame.getId(), mapFragment);
            getSupportActionBar().setTitle("Map");
            transaction.commit();
*/

        }




    }
}