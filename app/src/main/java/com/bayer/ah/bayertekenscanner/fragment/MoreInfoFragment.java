package com.bayer.ah.bayertekenscanner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bayer.ah.bayertekenscanner.R;


/**
 * Created by Tejas Sherdiwala on 4/22/2017.
 * &copy; Knoxpo
 */

public class MoreInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more_info, container, false);
    }
}
