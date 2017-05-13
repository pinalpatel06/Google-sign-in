package tekkan.synappz.com.tekkan.fragment.product;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.CircleNetworkImageView;
import tekkan.synappz.com.tekkan.custom.nestedfragments.CommonNodeInterface;
import tekkan.synappz.com.tekkan.custom.nestedfragments.ContainerNodeListFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.NestedFragmentUtil;
import tekkan.synappz.com.tekkan.custom.network.TekenErrorListener;
import tekkan.synappz.com.tekkan.custom.network.TekenJsonArrayRequest;
import tekkan.synappz.com.tekkan.custom.network.TekenResponseListener;
import tekkan.synappz.com.tekkan.model.ProductItem;
import tekkan.synappz.com.tekkan.utils.Constants;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

public class ProductTabFragment extends ContainerNodeListFragment<ProductTabFragment.ProductsListItem, ProductTabFragment.ProductVH> {

    private static final String TAG = ProductTabFragment.class.getSimpleName();

    private static final int
            INDEX_PRODUCT = 0,
            INDEX_INFO_ARTICLE = 1,
            INDEX_SCIENTIFIC = 2;

    private ArrayList<ProductsListItem> listItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchProduct();
    }

    @Override
    public List<ProductsListItem> onCreateItems(Bundle savedInstanceState) {
        listItems = new ArrayList<>();

      /*  listItems.add(new ProductsListItem("Product", getString(R.string.dummy_text)));
        listItems.add(new ProductsListItem("Informatie artikelen", getString(R.string.dummy_text)));
        listItems.add(new ProductsListItem("Wetenschappelijk", getString(R.string.dummy_text)));
*/
        return listItems;
    }

    private void fetchProduct() {
        TekenJsonArrayRequest request = new TekenJsonArrayRequest(
                Request.Method.GET,
                Constants.Api.getUrl(Constants.Api.FUNC_GET_PRODUCTS),
                new TekenResponseListener<JSONArray>() {
                    @Override
                    public void onResponse(int requestCode, JSONArray response) {
                        Log.d(TAG, "Success" + response.length());
                        listItems = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = response.getJSONObject(i);
                                listItems.add(new ProductsListItem(jsonObject));
                                loadNewItems(listItems);

                            } catch (JSONException e) {
                                continue;
                            }
                        }

                    }
                },
                new TekenErrorListener() {
                    @Override
                    public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {
                        Log.d(TAG, error.toString());
                    }
                },
                0
        );
        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_information_fragment;
    }

    @Override
    public ProductVH onCreateViewHolder(View v, int viewType) {
        return new ProductVH(v);
    }

    @Override
    public void onBindViewHolder(ProductVH holder, ProductsListItem item) {
        holder.bind(item);
    }

    @Override
    public String getTitle() {
        return NestedFragmentUtil.getTitle(getChildFragmentManager(), getString(R.string.title_product), getContainerId());
    }

    public class ProductVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_product_title)
        TextView mTitleTV;
        @BindView(R.id.tv_product_details)
        TextView mDescriptionTV;
        @BindView(R.id.iv_product_image)
        CircleNetworkImageView mProductImageIV;
        private ProductsListItem mItem;

        public ProductVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ProductsListItem item) {
            mItem = item;
            mTitleTV.setText(item.getTitle());
            mDescriptionTV.setText(item.getDescription());
            mProductImageIV.setImageUrl(item.getProfileUrl(), VolleyHelper.getInstance(getActivity()).getImageLoader());
            mProductImageIV.setDefaultImageResId(R.drawable.ic_splash_pets);
            itemView.setTag(item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            CommonNodeInterface fragment = null;

            switch (getAdapterPosition()) {
                case INDEX_PRODUCT:
                    fragment = ProductListFragment.newInstance(mItem.getProductList());
                    break;
                case INDEX_INFO_ARTICLE:
                    fragment = new InformationListFragment();
                    break;
                case INDEX_SCIENTIFIC:
                    fragment = new InformationDetailFragment();
                    break;
                default:
                    throw new UnsupportedOperationException("No such view to display");
            }
            setChild(fragment);
        }
    }

    class ProductsListItem {
        private String mCategoryId;
        private String mTitle;
        private String mDescription;
        private String mProfileUrl;
        private ArrayList<ProductItem> mProductList;

        private static final String
                JSON_S_CATEGORY_ID = "category_id",
                JSON_S_CATEGORY_NAME = "category_name",
                JSON_S_CATEGORY_DESCRIPTION = "category_description",
                JSON_S_CATEGORY_PHOTO = "category_photo",
                JSON_L_PRODUCTS = "products";

        private ProductsListItem(JSONObject jsonObject) {
            mCategoryId = jsonObject.optString(JSON_S_CATEGORY_ID);
            mTitle = jsonObject.optString(JSON_S_CATEGORY_NAME);
            mDescription = jsonObject.optString(JSON_S_CATEGORY_DESCRIPTION);
            mProfileUrl = jsonObject.optString(JSON_S_CATEGORY_PHOTO);
            mProductList = toProductList(jsonObject.optJSONArray(JSON_L_PRODUCTS));
        }

        private ArrayList<ProductItem> toProductList(JSONArray array) {
            if (array != null) {
                ArrayList<ProductItem> list = new ArrayList<>();

                for (int i = 0; i < array.length(); i++) {
                    list.add(new ProductItem(array.optJSONObject(i)));
                }
                return list;
            }
            return null;
        }

        String getTitle() {
            return mTitle;
        }

        String getDescription() {
            return mDescription;
        }

        public String getCategoryId() {
            return mCategoryId;
        }

        public String getProfileUrl() {
            return mProfileUrl;
        }

        public ArrayList<ProductItem> getProductList() {
            return mProductList;
        }
    }
}
