package com.openclassrooms.realestatemanager.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMapBinding;
import com.openclassrooms.realestatemanager.model.RealEstate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    private static final String sEstate = "ESTATE";

    private RealEstate mEstate;
    FragmentMapBinding mMapBinding;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(RealEstate estate) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        if(estate != null)
            args.putParcelable(sEstate, estate);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        mEstate = (RealEstate) getArguments().get(sEstate);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bitmap redMarkerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red_marker);
        mMapBinding = FragmentMapBinding.inflate(inflater,container,false);
        mMapBinding.map.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(12.0).center(Point.fromJson(mEstate.getJsonPoint())).build());
        AnnotationPlugin annotationAPI = AnnotationPluginImplKt.getAnnotations(mMapBinding.map);
        PointAnnotationManager pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationAPI, (AnnotationConfig) null);
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(Point.fromJson(mEstate.getJsonPoint()))
                .withIconImage(redMarkerBitmap).withTextField(mEstate.getName());
        pointAnnotationManager.create(pointAnnotationOptions);


        return mMapBinding.getRoot();
    }
}