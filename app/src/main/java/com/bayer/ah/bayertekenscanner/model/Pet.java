package com.bayer.ah.bayertekenscanner.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.bayer.ah.bayertekenscanner.utils.Constants;
import com.bayer.ah.bayertekenscanner.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


/**
 * Created by Tejas Sherdiwala on 12/05/17.
 */

public class Pet implements Parcelable {

    private static final String
            JSON_N_ID = "id",
            JSON_S_BIRTHDATE = "birthdate",
            JSON_S_GENDER = "gender",
            JSON_S_TYPE = "type",
            JSON_N_BREED_ID = "breed_id",
            JSON_S_PHOTO = "photo",
            JSON_S_NAME = "name",
            JSON_N_AGE = "age",
            JSON_N_WEIGHT = "weight",
            JSON_B_RESEARCH = "research",
            JSON_B_TICK = "tick",
            JSON_A_DISEASES = "diseases",
            JSON_S_STADIUM = "stadium",
            JSON_B_CONTAMINATED = "contaminated",
            JSON_S_PATHOGENES = "pathogenes",
            JSON_S_SUBTYPES = "subtypes",
            JSON_S_COMMENT = "comment";

    private long mId, mBreedId;
    private Date mBirthDate;

    public int[] getDisease() {
        return mDisease;
    }

    public void setDisease(int[] disease) {
        mDisease = disease;
    }

    private Constants.Gender mGender;
    private Constants.PetType mType;
    private String mPhoto, mName;
    private int mAge, mWeight;
    private boolean mResearch, mHasTick, mIsContaminated;
    private String mComment, mStadium, mSubTypes;
    private int[] mDisease;


    public Pet() {
    }


    public Pet(JSONObject object) {
        mId = object.optLong(JSON_N_ID);
        mBreedId = object.optLong(JSON_N_BREED_ID);
        Log.d("Pet", object.optString(JSON_S_BIRTHDATE));
        mBirthDate = DateUtils.toDate(object.optString(JSON_S_BIRTHDATE));
        Log.d("Pet = ", mBirthDate.toString());
        String gender = object.optString(JSON_S_GENDER);
        if (Constants.Gender.MALE.toApi().equalsIgnoreCase(gender)) {
            mGender = Constants.Gender.MALE;
        } else if (Constants.Gender.FEMALE.toApi().equalsIgnoreCase(gender)) {
            mGender = Constants.Gender.FEMALE;
        }

        String type = object.optString(JSON_S_TYPE);
        if (Constants.PetType.CAT.toApi().equalsIgnoreCase(type)) {
            mType = Constants.PetType.CAT;
        } else if (Constants.PetType.DOG.toApi().equalsIgnoreCase(type)) {
            mType = Constants.PetType.DOG;
        }

        mPhoto = object.optString(JSON_S_PHOTO);
        mName = object.optString(JSON_S_NAME);
        mAge = object.optInt(JSON_N_AGE);
        mWeight = object.optInt(JSON_N_WEIGHT);

        mResearch = object.optBoolean(JSON_B_RESEARCH);
        mHasTick = object.optBoolean(JSON_B_TICK);
        mIsContaminated = object.optBoolean(JSON_B_CONTAMINATED);

        JSONArray diseaseArray = object.optJSONArray(JSON_A_DISEASES);
        mDisease = new int[diseaseArray.length()];
        for (int i = 0; i < diseaseArray.length(); i++) {
            try {
                mDisease[i] = diseaseArray.getInt(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mStadium = object.optString(JSON_S_STADIUM);
        mSubTypes = object.optString(JSON_S_SUBTYPES);
        mComment = object.optString(JSON_S_COMMENT);

        //disease pending..........
    }

    protected Pet(Parcel in) {
        mId = in.readLong();
        mBreedId = in.readLong();
        mPhoto = in.readString();
        mName = in.readString();
        mAge = in.readInt();
        mWeight = in.readInt();
        long birthTime = in.readLong();
        // if(birthTime > -1){
        mBirthDate = new Date(birthTime);
        // }
        mGender = Constants.Gender.valueOf(in.readString());
        mType = Constants.PetType.valueOf(in.readString());

        mResearch = in.readInt() == 1;
        mHasTick = in.readInt() == 1;
        mIsContaminated = in.readInt() == 1;

        in.readIntArray(getDisease());

        mStadium = in.readString();
        mSubTypes = in.readString();
        mComment = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mBreedId);
        dest.writeString(mPhoto);
        dest.writeString(mName);
        dest.writeInt(mAge);
        dest.writeInt(mWeight);
        dest.writeLong(mBirthDate == null ? -1 : mBirthDate.getTime());
        dest.writeString(mGender.name());
        dest.writeString(mType.name());
        dest.writeInt(mResearch ? 1 : 0);
        dest.writeInt(mHasTick ? 1 : 0);
        dest.writeInt(mIsContaminated ? 1 : 0);
        dest.writeIntArray(mDisease);
        dest.writeString(mStadium);
        dest.writeString(mSubTypes);
        dest.writeString(mComment);
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

    public long getId() {
        return mId;
    }

    public long getBreedId() {
        return mBreedId;
    }

    public Date getBirthDate() {
        return mBirthDate;
    }

    public Constants.Gender getGender() {
        return mGender;
    }

    public Constants.PetType getType() {
        return mType;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public String getName() {
        return mName;
    }

    public int getAge() {
        return mAge;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setBirthDate(Date birthDate) {
        mBirthDate = birthDate;
    }

    public void setBreedId(long breedId) {
        mBreedId = breedId;
    }

    public void setGender(Constants.Gender gender) {
        mGender = gender;
    }

    public void setType(Constants.PetType type) {
        mType = type;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setAge(int age) {
        mAge = age;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public void setId(long id) {
        mId = id;
    }

    public boolean isResearch() {
        return mResearch;
    }

    public void setResearch(boolean research) {
        mResearch = research;
    }

    public boolean isHasTick() {
        return mHasTick;
    }

    public void setHasTick(boolean hasTick) {
        mHasTick = hasTick;
    }

    public boolean isContaminated() {
        return mIsContaminated;
    }

    public void setContaminated(boolean contaminated) {
        mIsContaminated = contaminated;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public String getStadium() {
        return mStadium;
    }

    public void setStadium(String stadium) {
        mStadium = stadium;
    }

    public String getSubTypes() {
        return mSubTypes;
    }

    public void setSubTypes(String subTypes) {
        mSubTypes = subTypes;
    }
}
