package tekkan.synappz.com.tekkan.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Date;

import tekkan.synappz.com.tekkan.utils.Constants;
import tekkan.synappz.com.tekkan.utils.DateUtils;

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
            JSON_S_RESEARCH = "research",
            JSON_S_TICK = "tick",
            JSON_S_DISEASES = "diseases",
            JSON_S_STADIUM = "stadium",
            JSON_S_CONTAMINATED = "contaminated",
            JSON_S_PATHOGENES = "pathogenes",
            JSON_S_SUBTYPES = "subtypes",
            JSON_S_COMMENT = "comment";

    private long mId, mBreedId;
    private Date mBirthDate;
    private Constants.Gender mGender;
    private Constants.PetType mType;
    private String mPhoto, mName;
    private int mAge, mWeight;

    public Pet(){
    }


    public Pet(JSONObject object){
        mId = object.optLong(JSON_N_ID);
        mBreedId = object.optLong(JSON_N_BREED_ID);
        mBirthDate = DateUtils.toDate(object.optString(JSON_S_BIRTHDATE));

        String gender = object.optString(JSON_S_GENDER);
        if(Constants.Gender.MALE.toApi().equalsIgnoreCase(gender)){
            mGender = Constants.Gender.MALE;
        }else if(Constants.Gender.FEMALE.toApi().equalsIgnoreCase(gender)){
            mGender = Constants.Gender.FEMALE;
        }

        String type = object.optString(JSON_S_TYPE);
        if(Constants.PetType.CAT.toApi().equalsIgnoreCase(type)){
            mType = Constants.PetType.CAT;
        }else if(Constants.PetType.DOG.toApi().equalsIgnoreCase(type)){
            mType = Constants.PetType.DOG;
        }

        mPhoto = object.optString(JSON_S_PHOTO);
        mName = object.optString(JSON_S_NAME);
        mAge = object.optInt(JSON_N_AGE);
        mWeight = object.optInt(JSON_N_WEIGHT);
    }

    protected Pet(Parcel in) {
        mId = in.readLong();
        mBreedId = in.readLong();
        mPhoto = in.readString();
        mName = in.readString();
        mAge = in.readInt();
        mWeight = in.readInt();
        long birthTime = in.readLong();
        if(birthTime > 0){
            mBirthDate = new Date(birthTime);
        }
        mGender = Constants.Gender.valueOf(in.readString());
        mType = Constants.PetType.valueOf(in.readString());
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
}
