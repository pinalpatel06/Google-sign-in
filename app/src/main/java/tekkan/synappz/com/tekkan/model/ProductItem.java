package tekkan.synappz.com.tekkan.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

/**
 * Created by Tejas Sherdiwala on 5/10/2017.
 * &copy; Knoxpo
 */

public class ProductItem implements Parcelable {
    private String mId;
    private String mTitle;
    private String mDescription;
    private String mContent;
    private String mProfileUrl;
    private String mBannerUrl;
    private String mDetailPageLink;

    private static final String
            JSON_S_ID = "id",
            JSON_S_TITLE = "title",
            JSON_S_DESCRIPTION = "description",
            JSON_S_CONTENT = "content",
            JSON_S_BANER = "banner",
            JSON_S_PHOTO = "photo",
            JSON_S_DETAIL_PAGE = "detailpage";

    public ProductItem(JSONObject jsonObject) {
        mId = jsonObject.optString(JSON_S_ID);
        mTitle = jsonObject.optString(JSON_S_TITLE);
        mDescription = jsonObject.optString(JSON_S_DESCRIPTION);
        mContent = jsonObject.optString(JSON_S_CONTENT);
        mProfileUrl = jsonObject.optString(JSON_S_PHOTO);
        mBannerUrl = jsonObject.optString(JSON_S_BANER);
        mDetailPageLink = jsonObject.optString(JSON_S_DETAIL_PAGE);
    }

    protected ProductItem(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();
        mContent = in.readString();
        mProfileUrl = in.readString();
        mBannerUrl = in.readString();
        mDetailPageLink = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeString(mContent);
        dest.writeString(mProfileUrl);
        dest.writeString(mBannerUrl);
        dest.writeString(mDetailPageLink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductItem> CREATOR = new Creator<ProductItem>() {
        @Override
        public ProductItem createFromParcel(Parcel in) {
            return new ProductItem(in);
        }

        @Override
        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
        }
    };

    public String getCatagoryId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getContent() {
        return mContent;
    }

    public String getProfileUrl() {
        return mProfileUrl;
    }

    public String getBannerUrl() {
        return mBannerUrl;
    }

    public String getDetailPageLink() {
        return mDetailPageLink;
    }
}
