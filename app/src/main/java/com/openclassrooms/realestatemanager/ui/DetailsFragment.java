package com.openclassrooms.realestatemanager.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mapbox.api.staticmap.v1.MapboxStaticMap;
import com.mapbox.api.staticmap.v1.StaticMapCriteria;
import com.mapbox.api.staticmap.v1.models.StaticMarkerAnnotation;
import com.mapbox.geojson.Point;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.SearchEngineSettings;
import com.mapbox.search.SearchOptions;
import com.mapbox.search.SearchSelectionCallback;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;
import com.openclassrooms.realestatemanager.MediaGalleryAdapter;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.SaveImageTask;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.databinding.ActivityDetailsBinding;
import com.openclassrooms.realestatemanager.event.OnMapCreated;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class DetailsFragment extends Fragment{

    private SearchEngine searchEngine;
    private Point mCenterPoint;

    OnMapCreated mOnMapCreated = this::updateMap;

    private final SearchSelectionCallback searchCallback = new SearchSelectionCallback() {
        @Override
        public void onSuggestions(@NonNull List<SearchSuggestion> suggestions, @NonNull ResponseInfo responseInfo) {
            if (suggestions.isEmpty()) {
                Log.i("SearchApiExample", "No suggestions found");
            } else {
                Log.i("SearchApiExample", "Search suggestions: " + suggestions + "\nSelecting first...");
                searchEngine.select(suggestions.get(0), this);
            }
        }

        @Override
        public void onResult(@NonNull SearchSuggestion suggestion, @NonNull SearchResult result, @NonNull ResponseInfo info) {
            mCenterPoint = result.getCoordinate();
            mEstate.setJsonPoint(mCenterPoint.toJson());
            mRealEstateViewModel.createOrUpdateRealEstate(mEstate);
            List<StaticMarkerAnnotation> staticMarkerAnnotations = new ArrayList<>();
            staticMarkerAnnotations.add(StaticMarkerAnnotation.builder()
                    .lnglat(mCenterPoint)
                    .color(255, 0, 0)
                    .build());

            MapboxStaticMap staticMap = MapboxStaticMap.builder()
                    .accessToken(requireActivity().getString(R.string.mapbox_access_token))
                    .styleId(StaticMapCriteria.STREET_STYLE)
                    .cameraPoint(mCenterPoint)
                    .cameraZoom(10)
                    .width(400)
                    .height(400)
                    .retina(true)
                    .staticMarkerAnnotations(staticMarkerAnnotations)
                    .build();
            SaveImageTask saveImageTask = new SaveImageTask(requireContext(),mOnMapCreated);
            saveImageTask.execute(staticMap.url().toString(),mEstate.getLocation() + ".jpg");

        }


        @Override
        public void onCategoryResult(@NonNull SearchSuggestion suggestion, @NonNull List<SearchResult> results, @NonNull ResponseInfo responseInfo) {
            Log.i("SearchApiExample", "Category search results: " + results);
        }
        @Override
        public void onError(@NonNull Exception e) {
            e.printStackTrace();
        }

    };

    RealEstate mEstate;
    ActivityDetailsBinding mBinding;

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(getContext(), RealEstateEditor.class);

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                    cal.set(year,month, dayOfMonth);
                    mEstate.setSaleDate(cal.getTime());
                    mRealEstateViewModel.createOrUpdateRealEstate(mEstate);
                }, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
            case 4 : //search
                //todo
                break;

            case 5 : // map
                if(Utils.isInternetAvailable(requireContext())){
                    Intent mapActivityIntent = new Intent(requireContext(),MapActivity.class);
                    startActivity(mapActivityIntent);
                }
                else {
                    Toast.makeText(requireContext(),requireContext().getString(R.string.internet_is_required),Toast.LENGTH_LONG).show();
                }
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.main_menu,menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = ActivityDetailsBinding.inflate(inflater, container, false);
        mRealEstateViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getContext())).get(RealEstateViewModel.class);
        Bundle bundle = getArguments();
        assert bundle != null;
        mEstate = bundle.getParcelable("REAL_ESTATE");
        mRealEstateViewModel.getRealEstateMediasByID(mEstate.getID()).observe(getViewLifecycleOwner(),this::mediaObserver);
        mBinding.address.setText(mEstate.getLocation());
        mBinding.description.setText(mEstate.getDescription());
        mBinding.surface.setText(getString(R.string.surface,mEstate.getSurface()));
        mBinding.bathrooms.setText(getString(R.string.number_of_bathrooms,mEstate.getBathrooms()));
        mBinding.rooms.setText(getString(R.string.number_of_rooms,mEstate.getRooms()));
        mBinding.agentTextView.setText(getString(R.string.agent, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName()));

        //MAP


        return mBinding.getRoot();


    }

    private void updateMap(File file) {
        if(isAdded()) {
            Glide.with(requireActivity())
                    .load(file)
                    .override(Target.SIZE_ORIGINAL)
                    .into(mBinding.staticMap);
        }
    }

    private void mediaObserver(List<RealEstateMedia> mediaList) {
        mEstate.setMediaList(mediaList);
        MediaGalleryAdapter mediaGalleryAdapter= new MediaGalleryAdapter(mEstate.getMediaList());
        mBinding.mediaGallery.setAdapter(mediaGalleryAdapter);
        mBinding.mediaGallery.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        SyncDB();

    }

    private void SyncDB() {
        if (Utils.isInternetAvailable(requireContext())) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if(!mEstate.getSync()) {
                db.collection("estates")
                        .document(mEstate.getAgentName() + mEstate.getID()).set(mEstate.toHashMap())
                        .addOnCompleteListener(task -> {
                            mEstate.setSync(true);
                            if (!task.isSuccessful()) {
                                mEstate.setSync(false);
                            }
                        });
            }

            List<RealEstateMedia> mediaList = mEstate.getMediaList();
            if (mediaList != null) {
                for (RealEstateMedia media : mediaList) {
                    if (!media.getSync()) {
                        db.collection("medias")
                                .document(mEstate.getAgentName() + media.getID())
                                .set(media.toHashMap())
                                .addOnCompleteListener(mediaTask -> {
                                    media.setSync(true);
                                    if (!mediaTask.isSuccessful()) {
                                        media.setSync(false);
                                    }
                                });
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        File file = new File(requireContext().getFilesDir(),mEstate.getLocation()+ ".jpg");

        if(file.exists()){
            updateMap(file);
        } else if (Utils.isInternetAvailable(requireContext())) {
            searchEngine = SearchEngine.createSearchEngineWithBuiltInDataProviders(
                    new SearchEngineSettings(getString(R.string.mapbox_access_token))
            );

            final SearchOptions options = new SearchOptions.Builder()
                    .limit(1)
                    .build();

          searchEngine.search(mEstate.getLocation(), options, searchCallback);
        }

        mBinding.staticMap.setOnClickListener(v2 -> {
            mCenterPoint = Point.fromJson(mEstate.getJsonPoint());

            if(Utils.isInternetAvailable(requireContext())) {
                mBinding.staticMap.setClickable(false);
                mBinding.staticMap.setFocusable(false);
                mBinding.staticMap.setOnClickListener(null);
                MapFragment mapFragment = MapFragment.newInstance(mEstate);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(mBinding.mapContainer.getId(), mapFragment);
                transaction.commit();
                Log.d("TAG", "onCreateView: CLICKED");

                // Disable scrolling in rootCL when MapView has focus
                mBinding.rootCL.setOnTouchListener((v, event) -> {


                    if (mapFragment.requireView().hasFocus()) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        Log.d("TAG", "FOCUS");
                    } else {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        Log.d("TAG", "PAS FOCUS");

                    }
                    return false;

                });


            }


        });
    }
}