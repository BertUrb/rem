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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
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
import com.openclassrooms.realestatemanager.UploadImageToFirestore;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.databinding.ActivityDetailsBinding;
import com.openclassrooms.realestatemanager.event.OnItemClickListener;
import com.openclassrooms.realestatemanager.event.OnMapCreated;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DetailsFragment extends Fragment implements OnItemClickListener {

    private SearchEngine searchEngine;
    private Point mCenterPoint;
    private ImagePopupWindow mImagePopupWindow;

    OnMapCreated mOnMapCreated = this::updateMap;
    RealEstate mEstate;
    ActivityDetailsBinding mBinding;

    private RealEstateViewModel mRealEstateViewModel;
    private LiveData<List<RealEstateMedia>> mLiveData;
    private Observer<List<RealEstateMedia>> mObserver;
    private final ActivityResultLauncher<Intent> mEditRealEstateLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            RealEstate editedEstate = result.getData().getParcelableExtra("EDITED_REAL_ESTATE");
                            mRealEstateViewModel.createOrUpdateRealEstate(editedEstate);
                            mEstate.clone(editedEstate);
                            updateUi();
                        }
                    });

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
            if (getContext() != null) {
                mCenterPoint = result.getCoordinate();
                mEstate.setJsonPoint(mCenterPoint.toJson());
                mRealEstateViewModel.createOrUpdateRealEstate(mEstate);
                List<StaticMarkerAnnotation> staticMarkerAnnotations = new ArrayList<>();
                staticMarkerAnnotations.add(StaticMarkerAnnotation.builder()
                        .lnglat(mCenterPoint)
                        .color(255, 0, 0)
                        .build());

                MapboxStaticMap staticMap = MapboxStaticMap.builder()
                        .accessToken(requireContext().getString(R.string.mapbox_access_token))
                        .styleId(StaticMapCriteria.STREET_STYLE)
                        .cameraPoint(mCenterPoint)
                        .cameraZoom(10)
                        .width(400)
                        .height(400)
                        .retina(true)
                        .staticMarkerAnnotations(staticMarkerAnnotations)
                        .build();
                SaveImageTask saveImageTask = new SaveImageTask(requireContext(), mOnMapCreated);
                saveImageTask.execute(staticMap.url().toString(), mEstate.getLocation() + ".jpg");

            }
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mImagePopupWindow = new ImagePopupWindow();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = ActivityDetailsBinding.inflate(inflater, container, false);
        mRealEstateViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getContext())).get(RealEstateViewModel.class);
        Bundle bundle = getArguments();
        assert bundle != null;
        mEstate = bundle.getParcelable("REAL_ESTATE");


        updateUi();


        return mBinding.getRoot();


    }

    private void updateUi() {
        mLiveData = mRealEstateViewModel.getRealEstateMediasByID(mEstate.getID());
        mObserver = this::mediaObserver;
        mLiveData.observe(getViewLifecycleOwner(), mObserver);
        mBinding.address.setText(mEstate.getLocation());
        mBinding.description.setText(mEstate.getDescription());
        mBinding.surface.setText(getString(R.string.surface, mEstate.getSurface()));
        mBinding.bathrooms.setText(getString(R.string.number_of_bathrooms, mEstate.getBathrooms()));
        mBinding.bedrooms.setText(getString(R.string.number_of_bedrooms,mEstate.getBedrooms()));
        mBinding.rooms.setText(getString(R.string.number_of_rooms, mEstate.getRooms()));
        mBinding.agentTextView.setText(getString(R.string.agent, mEstate.getAgentName()));

        Log.d("TAG", "onCreateView: " + mEstate.toString());
    }

    private void updateMap(File file) {
        if (isAdded()) {
            if (getActivity() != null) {
                requireActivity().runOnUiThread(() -> Glide.with(requireActivity())
                        .load(file)
                        .override(Target.SIZE_ORIGINAL)
                        .into(mBinding.staticMap));
            }
        }
    }

    private void mediaObserver(List<RealEstateMedia> mediaList) {
        mLiveData.removeObserver(mObserver);
        mEstate.setMediaList(mediaList);
        MediaGalleryAdapter mediaGalleryAdapter = new MediaGalleryAdapter(mEstate.getMediaList(),this);

        mBinding.mediaGallery.setAdapter(mediaGalleryAdapter);
        mBinding.mediaGallery.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        if (Utils.isInternetAvailable(requireContext())) {
            UploadImageToFirestore uploadImageToFirestore = new UploadImageToFirestore();

            int size = mediaList.size();
            int i = 0;
            for (RealEstateMedia media : mediaList) {
                i++;
                if (media.getFirestoreUrl() == null || media.getFirestoreUrl().isEmpty()) {
                    Log.d("TAG", "mediaObserver: FIRESTORE URL EMPTY " + media.getMediaUrl());

                    ImageLoader imageLoader = new ImageLoader();
                    final int finalI = i;


                    imageLoader.loadImage(media.getMediaUrl(), drawable -> requireActivity().runOnUiThread(() -> uploadImageToFirestore.uploadFromDrawable(drawable, media.getMediaUrl()).observe(requireActivity(), url -> {
                        Log.d("TAG", "mediaObserver: FIRESTORE URL " + url + " </>");
                        media.setFirestoreUrl(url);


                        downloadFromFirestore(uploadImageToFirestore, size, media, finalI, url, mEstate.getAgentName());
                    })));


                } else {
                    if (!(new File(media.getMediaUrl())).exists()) {
                        downloadFromFirestore(uploadImageToFirestore, size, media, i, media.getFirestoreUrl(), mEstate.getAgentName());
                    } else if (i == 1) {
                        String oldUrl = mEstate.getFeaturedMediaUrl();
                        mEstate.setFeaturedMediaUrl(media.getMediaUrl());
                        mRealEstateViewModel.updateEstateFeaturedMediaUrl(oldUrl, media.getMediaUrl());
                    }
                    else if (i == size)
                        SyncDB();
                }

            }


        }


    }

    private void downloadFromFirestore(UploadImageToFirestore uploadImageToFirestore, int size, RealEstateMedia media, int finalI, String url, String agentName) {
        try {




            uploadImageToFirestore.downloadImage(url).observe(requireActivity(), tempFile -> {

                media.setMediaURL(tempFile.getAbsolutePath());
                media.setSync(false);
                mRealEstateViewModel.updateMedia(media);
                String name = mEstate.getAgentName() + media.getID();
                Log.d("TAG", "SyncDB: ATTEMPT TO UPDATE " + name );

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("medias")
                        .document( name)
                        .set(media.toHashMap(agentName))
                        .addOnCompleteListener(mediaTask -> {
                            Log.d("TAG", "SyncDB: UPDATE OK ");
                            media.setSync(true);
                            if (!mediaTask.isSuccessful()) {
                                Log.d("TAG", "SyncDB: UPDATE KO ");
                                media.setSync(false);
                            }
                        });


                if (finalI == size) {
                    SyncDB();
                }
                Log.d("TAG", "onImageLoaded: UPDATED" + tempFile.getAbsolutePath());

            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void SyncDB() {
        if (Utils.isInternetAvailable(requireContext())) {
            if(mLiveData.hasActiveObservers())
            {
                mLiveData.removeObservers(getViewLifecycleOwner());
            }
            Log.d("TAG", "SyncDB:" + mEstate);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (mEstate != null) {
                if (!mEstate.getSync()) {


                    Log.d("TAG", "SyncDB: " + mEstate.getAgentName() + mEstate.getID());

                    db.collection("estates")
                            .document(mEstate.getAgentName() + mEstate.getID()).set(mEstate.toHashMap())
                            .addOnCompleteListener(task -> {
                                mEstate.setSync(true);
                                if (!task.isSuccessful()) {
                                    mEstate.setSync(false);
                                }
                            });
                }

                int totalMediaCount = mEstate.getMediaList().size();
                AtomicInteger completedMediaCount = new AtomicInteger();


                for (RealEstateMedia media : mEstate.getMediaList()) {
                    Log.d("TAG", "Before SyncMedia " + media.getMediaCaption());
                    if (!media.getSync()) {
                        Log.d("TAG", "SyncMedia " + media.getID());
                        String name = mEstate.getAgentName() + media.getID();
                        Log.d("TAG", "SyncMedia " + name);
                        db.collection("medias").document( name)
                                .set(media.toHashMap(mEstate.getAgentName())).addOnCompleteListener(t -> {
                                    media.setSync(true);
                                    Log.d("TAG", "SyncMedia : " + media.getMediaCaption() + "OK ?");
                                    if (!t.isSuccessful()) {
                                       media.setSync(false);
                                        Log.d("TAG", "SyncMedia : " + media.getMediaCaption() + "KO");
                                    }
                                    completedMediaCount.getAndIncrement();

                                    if (completedMediaCount.get() == totalMediaCount && !mLiveData.hasActiveObservers()) {
                                        mLiveData.observe(this, mObserver);
                                        Log.d("TAG", "All medias added to firestore");
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

        File file = new File(requireContext().getFilesDir(), mEstate.getLocation() + ".jpg");

        if (file.exists() && mEstate.getJsonPoint() != null) {
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

            if (Utils.isInternetAvailable(requireContext())) {
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(!Utils.isDeviceTablet(requireContext())) {
            inflater.inflate(R.menu.edit_menu_phone, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(requireContext(), RealEstateEditor.class);

        switch (item.getOrder()) {
            case 1: // edit
                intent.putExtra("REAL_ESTATE", mEstate);
                mEditRealEstateLauncher.launch(intent);
                break;

            case 3: //sell
                Calendar cal = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                    cal.set(year, month, dayOfMonth);
                    mEstate.setSaleDate(cal.getTime());
                    mRealEstateViewModel.createOrUpdateRealEstate(mEstate);
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(RealEstateMedia media) {
        mImagePopupWindow.showPopup(mBinding.getRoot(),media);

    }
}