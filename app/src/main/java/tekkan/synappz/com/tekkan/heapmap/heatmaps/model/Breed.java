package tekkan.synappz.com.tekkan.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Tejas Sherdiwala on 12/05/17.
 */

public class Breed implements Parcelable {

    private static final String
            JSON_N_ID = "id",
            JSON_S_NAME = "name";

    private long mId;
    private String mName;

    public Breed(JSONObject object) {
        mId = object.optLong(JSON_N_ID);
        mName = object.optString(JSON_S_NAME);
    }

    protected Breed(Parcel in) {
        mId = in.readLong();
        mName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Breed> CREATOR = new Creator<Breed>() {
        @Override
        public Breed createFromParcel(Parcel in) {
            return new Breed(in);
        }

        @Override
        public Breed[] newArray(int size) {
            return new Breed[size];
        }
    };

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return getName();
    }
}
