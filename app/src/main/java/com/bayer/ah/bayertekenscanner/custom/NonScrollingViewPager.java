package com.bayer.ah.bayertekenscanner.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Tejas Sherdiwala on 4/20/2017.
 * &copy; Knoxpo
 */

public class NonScrollingViewPager extends ViewPager {
    public NonScrollingViewPager(Context context) {
        super(context);
    }

    public NonScrollingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
