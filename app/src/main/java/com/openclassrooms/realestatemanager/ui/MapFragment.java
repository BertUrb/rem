package com.openclassrooms.realestatemanager.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.mapbox.geojson.Point;
import com.openclassrooms.realestatemanager.LocationHelper;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMapBinding;
import com.openclassrooms.realestatemanager.model.RealEstate;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    private static final String sEstate = "ESTATE";

    private RealEstate mEstate;
    private List<RealEstate> mEstates;
    private GeoPoint mCenterPoint;
    private IMapController mMapController;
    private MapView mMap;
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
        if (estate != null)
            args.putParcelable(sEstate, estate);

        fragment.setArguments(args);
        return fragment;
    }

    public static MapFragment newInstance(List<RealEstate> estates) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        if (estates != null)
            args.putParcelableArrayList(sEstate + "S", (ArrayList<RealEstate>) estates);

        fragment.setArguments(args);
        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mEstate = (RealEstate) getArguments().get(sEstate);
            mEstates = (List<RealEstate>) getArguments().get(sEstate + "S");
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMapBinding = FragmentMapBinding.inflate(inflater, container, false);
        Point centerPoint;
        if (mEstate != null) {
            centerPoint = Point.fromJson(mEstate.getJsonPoint());
            mCenterPoint = new GeoPoint(centerPoint.latitude(), centerPoint.longitude());
        } else {
            LocationHelper locationHelper = new LocationHelper(getActivity(), requireContext());
            mCenterPoint = new GeoPoint(locationHelper.getLocation().getLatitude(), locationHelper.getLocation().getLongitude());

            locationHelper.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
                mCenterPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                mMapController.setCenter(mCenterPoint);

                for (RealEstate estate : mEstates) {

                    MyMarker marker = new MyMarker(mMap);
                    Point estatePoint = Point.fromJson(estate.getJsonPoint());
                    marker.setPosition(new GeoPoint(estatePoint.latitude(), estatePoint.longitude()));
                    marker.setTitle(estate.getName());
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    Drawable d = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_location_on_24);
                    Objects.requireNonNull(d).setTint(Color.BLACK);
                    if(estate.getSaleDate() != null){
                        Objects.requireNonNull(d).setTint(Color.RED);
                    }
                    marker.setIcon(d);
                    marker.setOnMarkerClickListener((marker1, mapView) -> {
                        Intent intent = new Intent(getActivity(), SupportActivity.class);
                        for (RealEstate estate1 : mEstates) {
                            if (marker1.getTitle().equals(estate1.getName())) {
                                intent.putExtra("REAL_ESTATE", estate1);
                                break;
                            }

                        }

                        startActivity(intent);

                        return false;
                    });
                    mMap.getOverlays().add(marker);


                }

            });
        }

        mMap = mMapBinding.map;
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMap.setMultiTouchControls(true);


        mMapController = mMap.getController();
        mMapController.setCenter(mCenterPoint);


        Marker marker = new Marker(mMap);
        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()));
        marker.setPosition(mCenterPoint);
        if (mEstate != null) {
            marker.setTitle(mEstate.getName());
        } else {
            marker.setTitle(getString(R.string.my_location));
        }

        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);


        marker.setIcon(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_location_on_24));
        mMap.getOverlays().add(marker);

        mMapController.setZoom(14.0);
        Log.d("TAG", "onCreateView: " + mCenterPoint);
        Log.d("TAG", "onCreateView: " + mMap.getHeight() + " container:" + container.getHeight());


        mMap.setOnTouchListener((view, motionEvent) -> {

            view.getParent().requestDisallowInterceptTouchEvent(true);

            return false;
        });


        return mMapBinding.getRoot();
    }

    private static class MyMarker extends Marker {

        public MyMarker(MapView mapView) {
            super(mapView);
        }

        @Override
        public boolean onLongPress(MotionEvent event, MapView mapView) {
            showInfoWindow();
            return super.onLongPress(event, mapView);


        }

    }
}

