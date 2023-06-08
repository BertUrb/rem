package com.openclassrooms.realestatemanager.event;

import android.os.Build;
import android.view.View;
import android.widget.EditText;

public class EditTextFocusListener implements View.OnFocusChangeListener{
    @Override
    public void onFocusChange(View view, boolean b) {
        EditText editText = (EditText) view;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (b && editText.getTooltipText()!= null) {
                editText.performLongClick();

        }
    }
    }
}
