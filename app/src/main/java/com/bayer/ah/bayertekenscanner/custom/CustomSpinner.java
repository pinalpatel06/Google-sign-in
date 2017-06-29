package com.bayer.ah.bayertekenscanner.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.AdapterView;

/**
 * Created by Tejas Sherdiwala on 5/10/2017.
 * &copy; Knoxpo
 */

public class CustomSpinner extends AppCompatSpinner {

    public interface OnItemChosenListener {
        void onItemChosen(AdapterView<?> parent, int position);
    }

    private OnItemChosenListener mItemChosenListener;

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (mItemChosenListener != null) {
            mItemChosenListener.onItemChosen(this,position);
        }
    }

    public void setOnItemChosenListener(OnItemChosenListener listener) {
        mItemChosenListener = listener;
    }
}
