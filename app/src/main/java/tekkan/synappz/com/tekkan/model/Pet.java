package tekkan.synappz.com.tekkan.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tejas Sherdiwala on 5/12/2017.
 * &copy; Knoxpo
 */

public class Pet implements Parcelable {
    private String mName;
    private int mPetId;
    private long mDateOfBirth;
    private String mGender;
    private String mProfileImgUrl;
    private int mBreedId;
    private int mWeight;
    private String mAnimalType;

    private static final String
            JSON_ID = "id",
            JSON_BIRTHDATE = "birthdate",
            JSON_GENDER = "gender",
            JSON_TYPE = "type",
            JSON_BREED_ID = "breed_id",
            JSON_PHOTO = "photo",
            JSON_NAME = "name",
            JSON_AGE = "age",
            JSON_WEIGHT = "weight",
            JSON_RESEARCH = "research";

    public Pet(){}

    public Pet(JSONObject jSonObject) {
        mPetId = jSonObject.optInt(JSON_ID);
        mName = jSonObject.optString(JSON_NAME);
        String dateStr = jSonObject.optString(JSON_BIRTHDATE);
        Calendar c = parseDate(dateStr);
        mDateOfBirth = c.getTimeInMillis();
        mGender = jSonObject.optString(JSON_GENDER);
        mProfileImgUrl = jSonObject.optString(JSON_PHOTO);
        mBreedId = jSonObject.optInt(JSON_BREED_ID);
        mWeight = jSonObject.optInt(JSON_WEIGHT);
        mAnimalType = jSonObject.optString(JSON_TYPE);
    }

    protected Pet(Parcel in) {
        mName = in.readString();
        mPetId = in.readInt();
        mDateOfBirth = in.readLong();
        mGender = in.readString();
        mProfileImgUrl = in.readString();
        mBreedId = in.readInt();
        mWeight = in.readInt();
        mAnimalType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mPetId);
        dest.writeLong(mDateOfBirth);
        dest.writeString(mGender);
        dest.writeString(mProfileImgUrl);
        dest.writeInt(mBreedId);
        dest.writeInt(mWeight);
        dest.writeString(mAnimalType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };

    public static final SimpleDateFormat FULL_DATE_FORMAT
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    public static Calendar parseDate(String dateStr) {
        try {
            Date date = FULL_DATE_FORMAT.parse(dateStr);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c;
        } catch (ParseException e) {
            return null;
        }
    }

    public static String toStringDate(long date){
        Calendar c =Calendar.getInstance();
        c.setTimeInMillis(date);
        return FULL_DATE_FORMAT.format(c.getTime());
    }

    public Pet(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public int getPetId() {
        return mPetId;
    }

    public long getDateOfBirth() {
        return mDateOfBirth;
    }

    public String getGender() {
        return mGender;
    }

    public String getProfileImgUrl() {
        return mProfileImgUrl;
    }

    public int getBreedId() {
        return mBreedId;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setPetId(int petId) {
        mPetId = petId;
    }

    public void setDateOfBirth(long dateOfBirth) {
        mDateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        mProfileImgUrl = profileImgUrl;
    }

    public void setBreedId(int breedId) {
        mBreedId = breedId;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public String getAnimalType() {
        return mAnimalType;
    }

    public void setAnimalType(String animalType) {
        mAnimalType = animalType;
    }
}
