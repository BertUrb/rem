package com.openclassrooms.realestatemanager.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.slider.RangeSlider;
import com.openclassrooms.realestatemanager.R;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import javax.annotation.Nullable;


public class SearchModal extends DialogFragment {

    private RangeSeekBar<Integer> surfaceSeekBar;
    private RangeSeekBar<Integer>  priceSeekBar;
    private EditText listedWeeksEditText;
    private EditText soldWeeksEditText;
    private Button searchButton;

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



        surfaceSeekBar = new RangeSeekBar<>(requireContext());
        surfaceSeekBar.setRangeValues(0, 1000);
        surfaceSeekBar.setSelectedMinValue(200);
        surfaceSeekBar.setSelectedMaxValue(800);
        dialogView.addView(surfaceSeekBar);

        // Add a range seek bar to select the price range
        priceSeekBar = new RangeSeekBar<>(requireContext());
        priceSeekBar.setRangeValues(100000,10000000);
        priceSeekBar.setSelectedMinValue(300000);
        priceSeekBar.setSelectedMaxValue(8000000);
        dialogView.addView(priceSeekBar);

        // Add edit texts for the listed and sold weeks
        listedWeeksEditText = new EditText(getActivity());
        listedWeeksEditText.setHint("Number of weeks since listed");
        dialogView.addView(listedWeeksEditText);

        soldWeeksEditText = new EditText(getActivity());
        soldWeeksEditText.setHint("Number of weeks since sold");
        dialogView.addView(soldWeeksEditText);

        // Add a button to perform the search
        searchButton = new Button(getActivity());
        searchButton.setText("Search");
        dialogView.addView(searchButton);

        // Set up the button click listener
        searchButton.setOnClickListener(v -> {
            // Retrieve the search parameters and perform the search
            int minSurface = surfaceSeekBar.getSelectedMinValue();
            int maxSurface = surfaceSeekBar.getSelectedMaxValue();
            int minPrice = priceSeekBar.getSelectedMinValue();
            int maxPrice = priceSeekBar.getSelectedMaxValue();
            int listedWeeks = Integer.parseInt(listedWeeksEditText.getText().toString());
            int soldWeeks = Integer.parseInt(soldWeeksEditText.getText().toString());
            performSearch(minSurface, maxSurface, minPrice, maxPrice, listedWeeks, soldWeeks);

            // Dismiss the dialog
            dismiss();
        });

        // Set up the dialog window
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setView(dialogView);
        return builder.create();
    }

    private void performSearch(int minSurface, int maxSurface, int minPrice, int maxPrice, int listedWeeks, int soldWeeks) {
        //TODO
    }
}

