package com.bayer.ah.bayertekenscanner.fragment.advices;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.custom.nestedfragments.CommonNodeInterface;

import butterknife.ButterKnife;


/**
 * Created by Tejas Sherdiwala on 4/25/2017.
 * &copy; Knoxpo
 */

public class ResearchOutcomeFragment extends Fragment implements CommonNodeInterface {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragement_research_outcome, container, false);
        ButterKnife.bind(this, v);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_research_outcome);
    }

    @Override
    public boolean shouldDisplayHomeAsUpEnabled() {
        return true;
    }
}
