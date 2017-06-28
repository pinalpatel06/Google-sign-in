package com.bayer.ah.bayertekenscanner.custom.network;

/**
 * Created by Tejas Sherdiwala on 13/05/17.
 */

public interface TekenResponseListener<T> {
    public void onResponse(int requestCode, T response);
}
