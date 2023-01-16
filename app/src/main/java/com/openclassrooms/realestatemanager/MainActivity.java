package com.openclassrooms.realestatemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.model.RealEstate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());

        List<RealEstate> realEstateList = new ArrayList<>();
        realEstateList.add( new RealEstate("One","region one","dummyurl",100000000));
        realEstateList.add( new RealEstate("Two","region 2","dummyurl",10000000));
        realEstateList.add( new RealEstate("Threeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee","region 3","dummyurl",1000000));
        realEstateList.add( new RealEstate("Four","region 4","dummyurl",100000));
        realEstateList.add( new RealEstate("Five","region 5","dummyurl",10000));

        mBinding.realEstateListRv.setAdapter(new RealEstateRvAdapter(realEstateList));


        LinearLayout detailViewContainer = mBinding.detailViewContainer;
        if (Utils.isDeviceTablet(getApplicationContext())) {
            getLayoutInflater().inflate(R.layout.activity_details,detailViewContainer);
            detailViewContainer.setVisibility(View.VISIBLE);


        } else {
            detailViewContainer.setVisibility(View.GONE);

        }

        setContentView(mBinding.getRoot());
    }
}