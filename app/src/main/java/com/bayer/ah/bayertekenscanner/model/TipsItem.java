package com.bayer.ah.bayertekenscanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Tejas Sherdiwala on 6/2/2017.
 * &copy; Knoxpo
 */

public class TipsItem implements Parcelable {
    private String mId;
    private String mTipsTitle;
    private String mTipsDescription;
    private String mType;
    private String mContent;
    private String mBannerUrl;


    private static final String
            JSON_ID = "id",
            JSON_TITLE = "title",
            JSON_DESCRIPTION = "description",
            JSON_TYPE = "type",
            JSON_CONTENT = "content",
            JSON_BANNER = "banner";

    public TipsItem(String title, String details) {
        mTipsTitle = title;
        mTipsDescription = details;
    }

    public TipsItem(JSONObject jsonObject) {
        mId = jsonObject.optString(JSON_ID);
        mTipsTitle = jsonObject.optString(JSON_TITLE);
        mTipsDescription = jsonObject.optString(JSON_DESCRIPTION);
        mType = jsonObject.optString(JSON_TYPE);
        mContent = jsonObject.optString(JSON_CONTENT);
        mBannerUrl = jsonObject.optString(JSON_BANNER);
    }


    protected TipsItem(Parcel in) {
        mId = in.readString();
        mTipsTitle = in.readString();
        mTipsDescription = in.readString();
        mType = in.readString();
        mContent = in.readString();
        mBannerUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTipsTitle);
        dest.writeString(mTipsDescription);
        dest.writeString(mType);
        dest.writeString(mContent);
        dest.writeString(mBannerUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TipsItem> CREATOR = new Creator<TipsItem>() {
        @Override
        public TipsItem createFromParcel(Parcel in) {
            return new TipsItem(in);
        }

        @Override
        public TipsItem[] newArray(int size) {
            return new TipsItem[size];
        }
    };

    public String getTipsTitle() {
        return mTipsTitle;
    }

    public String getTipsDescription() {
        return mTipsDescription;
    }

    public String getId() {
        return mId;
    }

    public String getContent() {
        return mContent;
    }

    public String getBannerUrl() {
        return mBannerUrl;
    }

    public String getType() {
        return mType;
    }


}
