package com.openclassrooms.realestatemanager.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.slider.RangeSlider;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.RealEstate;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;


public class SearchModal extends DialogFragment {

    private RangeSeekBar<Integer> surfaceSeekBar;
    private RangeSeekBar<Integer>  priceSeekBar;
    private EditText listedWeeksEditText;
    private EditText soldWeeksEditText;
    private Button searchButton;

    private EditText estateNameSearchEditText;

    public SearchModal() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the theme of the dialog fragment
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogTheme);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create the dialog view programmatically
        LinearLayout dialogView = new LinearLayout(getActivity());
        dialogView.setOrientation(LinearLayout.VERTICAL);
        dialogView.setPadding(16, 16, 16, 16);

        // Add a range seek bar to select the surface range
        TextView estateNameTextView = new TextView(getActivity());
        estateNameTextView.setText(R.string.estate_name_search_text_view);
        dialogView.addView(estateNameTextView);

        estateNameSearchEditText = new EditText(getActivity());
        estateNameSearchEditText.setHint(R.string.search_name_hint);
        estateNameSearchEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogView.addView(estateNameSearchEditText);

        TextView surfaceSeekBarTextView = new TextView(getActivity());
        surfaceSeekBarTextView.setText(R.string.choose_the_surface_range_m);
        dialogView.addView(surfaceSeekBarTextView);

        surfaceSeekBar = new RangeSeekBar<>(getActivity());
        surfaceSeekBar.setRangeValues(10000, 1000000);
        surfaceSeekBar.setSelectedMinValue(10000);
        surfaceSeekBar.setSelectedMaxValue(1000000);
        dialogView.addView(surfaceSeekBar);

        NumberFormat numberFormat = NumberFormat.getInstance();
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.setGroupingSize(3);
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setDecimalSeparatorAlwaysShown(false);

        TextView surfaceSeekBarValuesTextView = new TextView(getActivity());
        surfaceSeekBarValuesTextView.setText(getString(R.string.surface_values,numberFormat.format(surfaceSeekBar.getSelectedMinValue()),numberFormat.format(surfaceSeekBar.getSelectedMaxValue())));
        dialogView.addView(surfaceSeekBarValuesTextView);

        // Add a range seek bar to select the price range

        TextView priceSeekBarTextView = new TextView(getActivity());
        priceSeekBarTextView.setText(R.string.choose_the_price_range);
        dialogView.addView(priceSeekBarTextView);
        priceSeekBar = new RangeSeekBar<>(requireContext());
        priceSeekBar.setRangeValues(10000000,1000000000);
        priceSeekBar.setSelectedMinValue(10000000);
        priceSeekBar.setSelectedMaxValue(1000000000);
        dialogView.addView(priceSeekBar);



        surfaceSeekBar.setNotifyWhileDragging(true);
        surfaceSeekBar.setOnRangeSeekBarChangeListener((bar, minValue, maxValue) -> surfaceSeekBarValuesTextView.setText(getString(R.string.surface_values,numberFormat.format(minValue),numberFormat.format(maxValue))));



        TextView priceSeekBarValuesTextView = new TextView(getActivity());
        priceSeekBarValuesTextView.setText(getString(R.string.price_values,numberFormat.format(priceSeekBar.getSelectedMinValue()),numberFormat.format(priceSeekBar.getSelectedMaxValue())));
        dialogView.addView(priceSeekBarValuesTextView);

        priceSeekBar.setNotifyWhileDragging(true);
        priceSeekBar.setOnRangeSeekBarChangeListener((bar, minValue, maxValue) -> priceSeekBarValuesTextView.setText(getString(R.string.price_values,numberFormat.format(minValue),numberFormat.format(maxValue))));
        priceSeekBar.setNotifyWhileDragging(true);
        // Add edit texts for the listed and sold weeks
        TextView listedWeeksTextView = new TextView(getActivity());
        listedWeeksTextView.setText(R.string.weeks_since_listed);
        dialogView.addView(listedWeeksTextView);
        listedWeeksEditText = new EditText(getActivity());
        listedWeeksEditText.setHint(R.string.weeks_since_listed);
        listedWeeksEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogView.addView(listedWeeksEditText);

        TextView soldWeeksTextView = new TextView(requireContext());
        soldWeeksTextView.setText(R.string.weeks_since_sold);
        dialogView.addView(soldWeeksTextView);
        soldWeeksEditText = new EditText(getActivity());
        soldWeeksEditText.setHint(R.string.weeks_since_sold);
        soldWeeksEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogView.addView(soldWeeksEditText);

        // Add a button to perform the search
        searchButton = new Button(getActivity());
        searchButton.setText(R.string.search);
        dialogView.addView(searchButton);

        // Set up the button click listener
        searchButton.setOnClickListener(v -> {
            // Retrieve the search parameters and perform the search
            int minSurface = surfaceSeekBar.getSelectedMinValue();
            int maxSurface = surfaceSeekBar.getSelectedMaxValue();
            int minPrice = priceSeekBar.getSelectedMinValue();
            int maxPrice = priceSeekBar.getSelectedMaxValue();

            String name= estateNameSearchEditText.getText().toString();

            int listedWeeks;
            if(listedWeeksEditText.getText().toString().equals(""))
            {
                listedWeeks = 0;
            }
            else listedWeeks = Integer.parseInt(listedWeeksEditText.getText().toString());
            int soldWeeks;
            if(soldWeeksEditText.getText().toString().equals("")) {
                soldWeeks = 0;
            }
            else soldWeeks = Integer.parseInt(soldWeeksEditText.getText().toString());


            performSearch(name,minSurface, maxSurface, minPrice, maxPrice, listedWeeks, soldWeeks);

            // Dismiss the dialog
            dismiss();
        });

        // Set up the dialog window
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setView(dialogView);
        return builder.create();
    }

    private void performSearch(String name,int minSurface, int maxSurface, int minPrice, int maxPrice, int listedWeeks, int soldWeeks) {
        RealEstateViewModel mRealEstateViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(requireContext())).get(RealEstateViewModel.class);

        Calendar calendar = Calendar.getInstance(), calendar1 = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -soldWeeks);
        calendar1.add(Calendar.WEEK_OF_YEAR,-listedWeeks);
        Date maxSaleDate = null;
        if(soldWeeks != 0)
            maxSaleDate = calendar.getTime();

        Date minListingDate = null;
        if(listedWeeks != 0)
            minListingDate = calendar1.getTime();

        mRealEstateViewModel.filterEstates(name,maxSaleDate,minListingDate,maxPrice,minPrice,maxSurface,minSurface).observe(this, realEstates -> {
            Intent intent = new Intent(requireContext(), MainActivity.class);

            Log.d("TAG", "performSearch: " + realEstates );
            intent.putParcelableArrayListExtra("filteredEstates",(ArrayList)realEstates);
            Log.d("TAG", "performSearch: FILTER ");
            startActivity(intent);
        });
    }
}

