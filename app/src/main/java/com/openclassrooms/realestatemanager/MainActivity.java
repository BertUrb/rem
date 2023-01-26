package com.openclassrooms.realestatemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.event.OnRealEstateClickListener;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());

        List<RealEstateMedia> mediaList = new ArrayList<>();

        mediaList.add(new RealEstateMedia("https://img2.freepng.fr/20171216/f4b/number-1-png-5a3526108c5491.2637864415134325925748.jpg","First media"));
        mediaList.add(new RealEstateMedia("https://upload.wikimedia.org/wikipedia/commons/thumb/c/cd/Number_2_in_light_blue_rounded_square.svg/2048px-Number_2_in_light_blue_rounded_square.svg.png","Second media"));

        List<RealEstate> realEstateList = new ArrayList<>();
        realEstateList.add( new RealEstate("One","region one",100000000,mediaList,0));
        realEstateList.add( new RealEstate("Two","region 2",10000000,mediaList,1));
        realEstateList.add( new RealEstate("Threeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee","region 3",1000000,mediaList,0));
        realEstateList.add( new RealEstate("Four","region 4",100000,mediaList,1));
        realEstateList.add( new RealEstate("Five","region 5",10000,mediaList,0));

        OnRealEstateClickListener onRealEstateClickListener = position -> {

            if(mBinding.detailViewContainer.getVisibility() == View.VISIBLE) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("REAL_ESTATE", realEstateList.get(position));

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);

                transaction.replace(mBinding.detailViewContainer.getId(), fragment);
                transaction.commit();
                Toast.makeText(getApplicationContext(),"position:" + position,Toast.LENGTH_SHORT).show();
            }

        };
        mBinding.realEstateListRv.setAdapter(new RealEstateRvAdapter(realEstateList, onRealEstateClickListener));


        LinearLayout detailViewContainer = mBinding.detailViewContainer;
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