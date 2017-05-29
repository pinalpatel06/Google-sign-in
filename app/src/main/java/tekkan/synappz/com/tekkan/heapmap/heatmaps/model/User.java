package tekkan.synappz.com.tekkan.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import tekkan.synappz.com.tekkan.utils.Constants;

/**
 * Created by Tejas Sherdiwala on 12/05/17.
 */

public class User {

    private static final String
            TAG = User.class.getSimpleName(),
            JSON_S_GENDER = "gender",
            JSON_S_FIRSTNAME = "firstname",
            JSON_S_LASTNAME = "lastname",
            JSON_S_STREET = "street",
            JSON_S_POSTALCODE = "postalcode",
            JSON_S_POSTALADDRESS = "postaladdress",
            JSON_S_EMAIL = "email",
            JSON_N_MOBILE = "mobile";

    private Constants.Gender mGender;
    private String
            mFirstName;
    private String mLastName;
    private String mStreet;
    private String mPostalCode;
    private String mPostalAddress;
    private String mEmail;

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    private String mPassword;

    private long mMobile;

    private boolean mIsLoaded = false;

    private static User sUser;

    private Context mContext;

    public static User getInstance(Context context) {
        if (sUser == null) {
            sUser = new User(context);
        }
        return sUser;
    }

    private User(Context context) {
        mContext = context.getApplicationContext();
        try{
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String userJson = preferences.getString(Constants.SP.JSON_USER,null);
            loadUser(new JSONObject(userJson));
        }catch (Exception e){
            Log.e(TAG, "Could not load user");
        }
    }

    public void loadUser(JSONObject object) {
        try{
            mFirstName = object.getString(JSON_S_FIRSTNAME);
            mLastName = object.getString(JSON_S_LASTNAME);
            mStreet = object.getString(JSON_S_STREET);
            mPostalCode = object.getString(JSON_S_POSTALCODE);
            mPostalAddress = object.getString(JSON_S_POSTALADDRESS);
            mEmail = object.getString(JSON_S_EMAIL);
            mMobile = object.getLong(JSON_N_MOBILE);

            String gender = object.getString(JSON_S_GENDER);
            if (Constants.Gender.MALE.toApi().equalsIgnoreCase(gender)) {
                mGender = Constants.Gender.MALE;
            } else if (Constants.Gender.FEMALE.toApi().equalsIgnoreCase(gender)) {
                mGender = Constants.Gender.FEMALE;
            }
            mIsLoaded = true;
        }catch (JSONException e){
            e.printStackTrace();
            mIsLoaded = false;
        }

    }

    public void unloadUser(){
        sUser = null;
    }

    public Constants.Gender getGender() {
        return mGender;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getStreet() {
        return mStreet;
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public String getPostalAddress() {
        return mPostalAddress;
    }

    public String getEmail() {
        return mEmail;
    }

    public long getMobile() {
        return mMobile;
    }

    public boolean isLoaded() {
        return mIsLoaded;
    }

    public String toJSON() {
        try {
            JSONObject object = new JSONObject();
            object.put(JSON_S_GENDER, mGender.toApi());
            object.put(JSON_S_FIRSTNAME, mFirstName);
            object.put(JSON_S_LASTNAME, mLastName);
            object.put(JSON_S_STREET, mStreet);
            object.put(JSON_S_POSTALCODE, mPostalCode);
            object.put(JSON_S_POSTALADDRESS, mPostalAddress);
            object.put(JSON_S_EMAIL, mEmail);
            object.put(JSON_N_MOBILE, mMobile);
            return object.toString();
        } catch (JSONException e) {
            return "";
        }
    }

    public void setGender(Constants.Gender gender) {
        mGender = gender;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public void setStreet(String street) {
        mStreet = street;
    }

    public void setPostalCode(String postalCode) {
        mPostalCode = postalCode;
    }

    public void setPostalAddress(String postalAddress) {
        mPostalAddress = postalAddress;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setMobile(long mobile) {
        mMobile = mobile;
    }

    public void setLoaded(boolean loaded) {
        mIsLoaded = loaded;
    }
}