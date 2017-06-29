package com.bayer.ah.bayertekenscanner.fragment.teekMelden;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.custom.nestedfragments.CommonNodeInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tejas Sherdiwala on 4/27/2017.
 * &copy; Knoxpo
 */

public class OtherPetSelectedFragment extends Fragment implements CommonNodeInterface {

    @BindView(R.id.btn_previous)
    Button mPreviousBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_other_pet_selected,container, false);
        ButterKnife.bind(this,v);
        v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_blurred));
        return v;
    }

    @OnClick(R.id.btn_previous)
    public void onPreviousClicked(){
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commitNow();
        }
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public boolean shouldDisplayHomeAsUpEnabled() {
        return true;
    }
}
