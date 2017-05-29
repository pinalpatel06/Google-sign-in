package tekkan.synappz.com.tekkan.model;

import org.json.JSONObject;

/**
 * Created by Tejas Sherdiwala on 5/15/2017.
 * &copy; Knoxpo
 */

public class BaseProduct {
    private String mCategoryId;
    private String mTitle;
    private String mDescription;
    private String mProfileUrl;


    private static final String
            JSON_S_CATEGORY_ID = "category_id",
            JSON_S_CATEGORY_NAME = "category_name",
            JSON_S_CATEGORY_DESCRIPTION = "category_description",
            JSON_S_CATEGORY_PHOTO = "category_photo";

    public BaseProduct(JSONObject jsonObject) {
        mCategoryId = jsonObject.optString(JSON_S_CATEGORY_ID);
        mTitle = jsonObject.optString(JSON_S_CATEGORY_NAME);
        mDescription = jsonObject.optString(JSON_S_CATEGORY_DESCRIPTION);
        mProfileUrl = jsonObject.optString(JSON_S_CATEGORY_PHOTO);
    }

    public String getCategoryId() {
        return mCategoryId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getProfileUrl() {
        return mProfileUrl;
    }
}
