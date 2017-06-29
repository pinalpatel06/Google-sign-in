package com.bayer.ah.bayertekenscanner.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bayer.ah.bayertekenscanner.custom.network.TekenErrorListener;
import com.bayer.ah.bayertekenscanner.custom.network.TekenJsonObjectRequest;
import com.bayer.ah.bayertekenscanner.custom.network.TekenResponseListener;
import com.bayer.ah.bayertekenscanner.custom.network.TekenStringRequest;
import com.bayer.ah.bayertekenscanner.model.User;
import org.json.JSONObject;
import java.security.Key;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Tejas Sherdiwala on 5/9/2017.
 * &copy; Knoxpo
 */

public class LoginUtils {
    private static final int
            REQUEST_LOGIN = 0,
            REQUEST_USER = 1;

    public interface Listener {
        void onSuccess();

        void onFailure(VolleyError error, int errorCode, String errorString);
    }

    private static final String
            PARAM_EMAIL = "email",
            PARAM_PASSWORD = "password";

    public static void logIn(final Context context, final String email, final String password, final LoginUtils.Listener callback) {
        String encrPassword = encode(password);
        TekenStringRequest request = new TekenStringRequest(
                Request.Method.POST,
                Constants.Api.getUrl(Constants.Api.FUNC_LOGIN),
                new TekenResponseListener<String>() {
                    @Override
                    public void onResponse(int requestCode, String response) {
                        getUserDetails(context,email,callback);
                    }
                },
                new TekenErrorListener() {
                    @Override
                    public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {
                        callback.onFailure(error,status,message);
                    }
                },
                REQUEST_LOGIN
        );
        request.addParam(PARAM_EMAIL, email);
        request.addParam(PARAM_PASSWORD, encrPassword);
        VolleyHelper.getInstance(context).addToRequestQueue(request);
    }

    private static void getUserDetails(final Context context , String email, final LoginUtils.Listener callback){

        TekenJsonObjectRequest request = new TekenJsonObjectRequest(
                Request.Method.GET,
                Constants.Api.getUrl(Constants.Api.FUNC_USER),
                new TekenResponseListener<JSONObject>() {
                    @Override
                    public void onResponse(int requestCode, JSONObject response) {
                        User user = User.getInstance(context);
                        user.loadUser(response);
                        PreferenceManager.getDefaultSharedPreferences(context).edit()
                                .putString(Constants.SP.JSON_USER,user.toJSON())
                                .apply();
                        callback.onSuccess();
                    }
                },
                new TekenErrorListener() {
                    @Override
                    public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {
                        callback.onFailure(error,status,message);
                    }
                },
                REQUEST_USER
        );
        request.addParam(Constants.Api.QUERY_PARAMETER1, email);
        VolleyHelper.getInstance(context).addToRequestQueue(request);
    }

    public static String encode(String pwd) {
        try {
            String key = "Hf3rf8KB4"; // 128 bit key
            byte[] bKeys = key.getBytes();


            Key aesKey = new SecretKeySpec(Arrays.copyOf(bKeys, 16), "AES");
            Cipher cipher = Cipher.getInstance("AES");


            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            return Base64.encodeToString(cipher.doFinal(pwd.getBytes()), Base64.DEFAULT);
            //return new String(encodedBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
