package com.bayer.ah.bayertekenscanner.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Tejas Sherdiwala on 5/9/2017.
 * &copy; Knoxpo
 */

public class Constants {

    public static class Api {

        private static final String
                SCHEME = "http",
                END_POINT = "dev.bayerpetcare.nl",
                PATH_SCRIPT = "scripts",
                PATH_WEB_SERVICES = "webservice.app.php",
                QUERY_PARAMETER_KEY = "m";

        public static final String
                QUERY_PARAMETER1 = "p1",
                QUERY_PARAMETER2 = "p2";

        private static final Uri
                BASE_URI = new Uri.Builder()
                .scheme(SCHEME)
                .authority(END_POINT)
                .appendPath(PATH_SCRIPT)
                .appendPath(PATH_WEB_SERVICES)
                .build();

        public static final String
                FUNC_LOGIN = "Login",
                FUNC_USER = "GetUser",
                FUNC_CREATE_USER = "CreateUser",
                FUNC_EDIT_USER = "EditUser",
                FUNC_CREATE_TICK__REPORT = "CreateTickReport",
                FUNC_COUNT_FREE_CODES = "CountFreeCodes",
                FUNC_GET_TICK = "GetTickReports",
                FUNC_GET_PRODUCTS = "GetProducts",
                FUNC_GET_INFORMATIONS = "GetInformations",
                FUNC_GET_TIPS = "GetTips",
                FUNC_GET_BREEDS = "GetBreeds",
                FUNC_EDIT_ANIMAL = "EditAnimal",
                FUNC_DELETE_ANIMAL = "DeleteAnimal",
                FUNC_GET_ANIMALS_BY_USER = "GetAnimalsbyUser",
                FUNC_CREATE_ANIMAL = "CreateAnimal";

        public static String getUrl(String funcName) {
            Uri.Builder builder = BASE_URI.buildUpon();
            builder.appendQueryParameter(QUERY_PARAMETER_KEY, funcName);
            return builder.build().toString();
        }
    }

    public static class SP {
        public static final String
                JSON_USER = "JSON_USER",
                HAS_ACCEPTED_DISCLAIMER = "HAS_ACCEPTED_DISCLAIMER",
                IS_PROFILE_SETUP_FINISHED = "IS_PROFILE_SETUP_FINISHED";

    }

    public enum Gender {
        MALE("M"),
        FEMALE("V");

        private String mString;

        Gender(String string) {
            mString = string;
        }

        public String toApi() {
            return mString;
        }
    }

    public enum PetType {
        DOG("D"),
        CAT("C");

        private String mString;

        PetType(String string) {
            mString = string;
        }

        public String toApi() {
            return mString;
        }
    }

    public enum DiseaseList {
        DISEASE1("Ziekte van Lyme"),
        DISEASE2("Babesiosis of tekenkoorts"),
        DISEASE3("Anaplasmosis"),
        DISEASE4("Ehrlechiosis");

        private String mString;

        DiseaseList(String string) {
            mString = string;
        }

        public String toApi() {
            return mString;
        }
    }

    private class Disease{
        private String mDiseaseName;
        private Drawable mDiseaseColor;

        public Disease(String diseaseName, Drawable diseaseColor) {
            mDiseaseName = diseaseName;
            mDiseaseColor = diseaseColor;
        }
    }

    public static void closeKeyBoard(Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
