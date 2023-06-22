package com.openclassrooms.realestatemanager.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;

public class ImagePopupWindow {
    public void showPopup(View anchorView, String url) {
        LayoutInflater inflater = (LayoutInflater) anchorView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_image, (ViewGroup) anchorView.getParent(), false);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);

        ImageView imageView = popupView.findViewById(R.id.imageView);
        Glide.with(anchorView.getRootView()).load(url).into(imageView);

    }
}
