package com.openclassrooms.realestatemanager.ui;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.RealEstateRvAdapter;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.event.OnRealEstateClickListener;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.RealEstate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private RealEstateViewModel mRealEstateViewModel;
    private List<RealEstate> mEstates = new ArrayList<>();

    private int selectedPosition = -1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mRealEstateViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(RealEstateViewModel.class);
        mRealEstateViewModel.getRealEstates().observe(this,this::getEstatesObserver);


    }

    private void getEstatesObserver(List<RealEstate> estates) {
        mEstates.clear();
        mEstates.addAll(estates);
        if(estates.size()>0)
            updateEstates();

    }


    private void updateEstates() {
        OnRealEstateClickListener onRealEstateClickListener = position -> {
            RealEstate realEstate = mEstates.get(position);
             selectedPosition = position;


            if(mBinding.detailViewContainer.getVisibility() == View.VISIBLE) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("REAL_ESTATE", realEstate);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);
                getSupportActionBar().setTitle(realEstate.getName());





                transaction.replace(mBinding.FragmentContainer.getId(), fragment);
                transaction.commit();

            }
            else {
                Intent intent = new Intent(this, SupportActivity.class);
                intent.putExtra("REAL_ESTATE", realEstate);
                startActivity(intent);

            }


            Toast.makeText(getApplicationContext(),"position:" + position,Toast.LENGTH_SHORT).show();


        };
        mBinding.realEstateListRv.setAdapter(new RealEstateRvAdapter(mEstates, onRealEstateClickListener));


        ScrollView detailViewContainer = mBinding.detailViewContainer;
        if (Utils.isDeviceTablet(getApplicationContext())) {
            //  getLayoutInflater().inflate(R.layout.activity_details,detailViewContainer);
            detailViewContainer.setVisibility(View.VISIBLE);
            onRealEstateClickListener.OnRealEstateClick(0);



        } else {
            detailViewContainer.setVisibility(View.GONE);

        }

        setContentView(mBinding.getRoot());

    }
}