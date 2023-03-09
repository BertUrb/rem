package com.openclassrooms.realestatemanager.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.mapbox.api.staticmap.v1.MapboxStaticMap;
import com.mapbox.api.staticmap.v1.StaticMapCriteria;
import com.mapbox.geojson.Point;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.SearchEngineSettings;
import com.mapbox.search.SearchOptions;
import com.mapbox.search.SearchSelectionCallback;
import com.mapbox.search.common.AsyncOperationTask;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;
import com.openclassrooms.realestatemanager.MediaGalleryAdapter;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.SaveImageTask;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.databinding.ActivityDetailsBinding;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.model.RealEstateMedia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class DetailsFragment extends Fragment {

    private SearchEngine searchEngine;
    private AsyncOperationTask searchRequestTask;

    private final SearchSelectionCallback searchCallback = new SearchSelectionCallback() {



        @Override
        public void onSuggestions(@NonNull List<SearchSuggestion> suggestions, @NonNull ResponseInfo responseInfo) {
            if (suggestions.isEmpty()) {
                Log.i("SearchApiExample", "No suggestions found");
            } else {
                Log.i("SearchApiExample", "Search suggestions: " + suggestions + "\nSelecting first...");
                searchRequestTask = searchEngine.select(suggestions.get(0), this);
            }
        }

        @Override
        public void onResult(@NonNull SearchSuggestion suggestion, @NonNull SearchResult result, @NonNull ResponseInfo info) {
            Point centerPoint = result.getCoordinate();
            MapboxStaticMap staticMap = MapboxStaticMap.builder()
                    .accessToken(requireActivity().getString(R.string.mapbox_access_token))
                    .styleId(StaticMapCriteria.STREET_STYLE)
                    .cameraPoint(centerPoint)
                    .cameraZoom(16)
                    .width(600)
                    .height(600)
                    .retina(true)
                    .build();
            SaveImageTask saveImageTask = new SaveImageTask(requireContext());
            saveImageTask.execute(staticMap.url().toString(),mEstate.getLocation() + " " + mEstate.getRegion());
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
                //TODO
                break;
            case 4 : //search
                //todo
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

        //MAP
        File file = new File(requireContext().getFilesDir(),mEstate.getLocation() + " " + mEstate.getRegion());

        if(file.exists()){
            Glide.with(requireContext())
                    .load(file)
                    .into(mBinding.staticMap);
        } else if (Utils.isInternetAvailable(requireContext())) {
            searchEngine = SearchEngine.createSearchEngineWithBuiltInDataProviders(
                    new SearchEngineSettings(getString(R.string.mapbox_access_token))
            );

            final SearchOptions options = new SearchOptions.Builder()
                    .limit(5)
                    .build();

            searchRequestTask = searchEngine.search(mEstate.getLocation(), options, searchCallback);



        }


        return mBinding.getRoot();


    }

    private void mediaObserver(List<RealEstateMedia> mediaList) {
        mEstate.setMediaList(mediaList);
        MediaGalleryAdapter mediaGalleryAdapter= new MediaGalleryAdapter(mEstate.getMediaList());
        mBinding.mediaGallery.setAdapter(mediaGalleryAdapter);
        mBinding.mediaGallery.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


    }


}