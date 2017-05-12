package tekkan.synappz.com.tekkan.model;

import android.content.Context;

import org.json.JSONObject;

import tekkan.synappz.com.tekkan.utils.Constants;

/**
 * Created by Tejas Sherdiwala on 12/05/17.
 */

public class User {

    private static final String
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
            mFirstName,
            mLastName,
            mStreet,
            mPostalCode,
            mPostalAddress,
            mEmail;

    private long mMobile;

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
    }

    public void loadUser(JSONObject object) {
        mFirstName = object.optString(JSON_S_FIRSTNAME);
        mLastName = object.optString(JSON_S_LASTNAME);
        mStreet = object.optString(JSON_S_LASTNAME);
        mPostalCode = object.optString(JSON_S_POSTALCODE);
        mPostalAddress = object.optString(JSON_S_POSTALADDRESS);
        mEmail = object.optString(JSON_S_EMAIL);
        mMobile = object.optLong(JSON_N_MOBILE);

        String gender = object.optString(JSON_S_GENDER);
        if (Constants.Gender.MALE.toApi().equalsIgnoreCase(gender)) {
            mGender = Constants.Gender.MALE;
        } else if (Constants.Gender.FEMALE.toApi().equalsIgnoreCase(gender)) {
            mGender = Constants.Gender.FEMALE;
        }
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
}