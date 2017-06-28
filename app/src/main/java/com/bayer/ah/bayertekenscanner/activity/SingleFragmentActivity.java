package com.bayer.ah.bayertekenscanner.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.bayer.ah.bayertekenscanner.R;

/**
 * Created by Tejas Sherdiwala on 4/19/2017.
 * &copy; Knoxpo
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment getFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment existingFragment = fm.findFragmentById(getFragmentContainerId());
        if(existingFragment == null){
            fm.beginTransaction()
                    .replace(getFragmentContainerId(),getFragment())
                    .commit();
        }
    }
    protected  int getContentLayoutId(){
        return R.layout.activity_single_fragment;
    }

    protected int getFragmentContainerId(){
        return R.id.fragment_container;
    }
}
