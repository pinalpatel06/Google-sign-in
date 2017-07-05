package com.bayer.ah.bayertekenscanner.model;

import com.bayer.ah.bayertekenscanner.cluster.clustering.ClusterItem;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Tejas Sherdiwala on 7/4/2017.
 * &copy; Knoxpo
 */

public class LatLngItem implements ClusterItem {

    private LatLng mLatLng;

    public LatLngItem(LatLng latLng){
        mLatLng = latLng;
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
}
