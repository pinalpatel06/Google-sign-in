package com.bayer.ah.bayertekenscanner.model;

import com.bayer.ah.bayertekenscanner.cluster.clustering.ClusterItem;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Tejas Sherdiwala on 7/4/2017.
 * &copy; Knoxpo
 */

public class LatLngItem implements ClusterItem {

    private LatLng mLatLng;
    private int mDiseaseId;

    public LatLngItem(LatLng latLng,int diseaseId){
        mLatLng = latLng;
        mDiseaseId = diseaseId;
    }

    @Override
    public LatLng getPosition() {
        return mLatLng;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public int getDiseaseId() {
        return mDiseaseId;
    }

    public void setDiseaseId(int diseaseId) {
        mDiseaseId = diseaseId;
    }
}
