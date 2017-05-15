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
import tekkan.synappz.com.tekkan.dialogs.ProgressDialogFragment;
import tekkan.synappz.com.tekkan.model.BaseProduct;
import tekkan.synappz.com.tekkan.model.ProductItem;
import tekkan.synappz.com.tekkan.utils.Constants;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

import static tekkan.synappz.com.tekkan.fragment.LoginFragment.TAG_PROGRESS_DIALOG;

public class ProductTabFragment extends ContainerNodeListFragment<Object, ProductTabFragment.ProductVH> {

    private static final String TAG = ProductTabFragment.class.getSimpleName();

    private static final int
            REQUEST_FETCH_PRODUCT = 0,
            REQUEST_FETCH_INFORMATION = 1;

    private ArrayList<Object> listItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchProduct();
    }

    @Override
    public List<Object> onCreateItems(Bundle savedInstanceState) {
        listItems = new ArrayList<>();
        return listItems;
    }

    private void fetchProduct() {

        ProgressDialogFragment fragment = ProgressDialogFragment.newInstance(getString(R.string.fetch_product));
        fragment.show(getFragmentManager(), TAG_PROGRESS_DIALOG);

        TekenJsonArrayRequest request = new TekenJsonArrayRequest(
                Request.Method.GET,
                Constants.Api.getUrl(Constants.Api.FUNC_GET_PRODUCTS),
                new TekenResponseListener<JSONArray>() {
                    @Override
                    public void onResponse(int requestCode, JSONArray response) {
                        fetchInformation();
                        Log.d(TAG, "Success" + response.length());
                        listItems = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = response.getJSONObject(i);
                                listItems.add(new ProductsListItem(jsonObject));
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
                        ProgressDialogFragment fragment = (ProgressDialogFragment) getFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
                        fragment.dismiss();

                    }
                },
                REQUEST_FETCH_PRODUCT
        );
        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void fetchInformation() {
        TekenJsonArrayRequest request = new TekenJsonArrayRequest(
                Request.Method.GET,
                Constants.Api.getUrl(Constants.Api.FUNC_GET_INFORMATIONS),
                new TekenResponseListener<JSONArray>() {
                    @Override
                    public void onResponse(int requestCode, JSONArray response) {
                        Log.d(TAG, "Success for Information" + response.length());

                        ProgressDialogFragment fragment = (ProgressDialogFragment) getFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
                        fragment.dismiss();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = response.getJSONObject(i);
                                listItems.add(new InformationListItem(jsonObject));
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
                        ProgressDialogFragment fragment = (ProgressDialogFragment) getFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
                        fragment.dismiss();

                    }
                },
                REQUEST_FETCH_INFORMATION
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
    public void onBindViewHolder(ProductVH holder, Object item) {
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
        private InformationListItem mInfoList;

        public ProductVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Object item) {
            if (item instanceof ProductsListItem) {
                mItem = (ProductsListItem) item;
                mTitleTV.setText(mItem.getTitle());
                mDescriptionTV.setText(mItem.getDescription());
                if (!mItem.getProfileUrl().equals("null")) {
                    mProductImageIV.setImageUrl(mItem.getProfileUrl(), VolleyHelper.getInstance(getActivity()).getImageLoader());
                }
                mProductImageIV.setDefaultImageResId(R.drawable.ic_splash_pets);
            } else if (item instanceof InformationListItem) {
                mInfoList = (InformationListItem) item;
                mTitleTV.setText(mInfoList.getTitle());
                mDescriptionTV.setText(mInfoList.getDescription());
                if (!mInfoList.getProfileUrl().equals("null")) {
                    mProductImageIV.setImageUrl(mInfoList.getProfileUrl(), VolleyHelper.getInstance(getActivity()).getImageLoader());
                }
                mProductImageIV.setDefaultImageResId(R.drawable.ic_splash_pets);
            }
            itemView.setTag(item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            CommonNodeInterface fragment = null;
            if (mItem != null) {
                fragment = ProductListFragment.newInstance(mItem.getProductList());
            } else if (mInfoList != null) {
                fragment = InformationListFragment.newInstance(mInfoList.getInformationList());
            }
            setChild(fragment);
        }
    }

    class ProductsListItem extends BaseProduct {

        private ArrayList<ProductItem> mProductList;

        private static final String
                JSON_L_PRODUCTS = "products";

        private ProductsListItem(JSONObject jsonObject) {
            super(jsonObject);
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

        public ArrayList<ProductItem> getProductList() {
            return mProductList;
        }
    }

    class InformationListItem extends BaseProduct {

        private ArrayList<ProductItem> mInformationList;

        private static final String
                JSON_L_PAGES = "pages";

        private InformationListItem(JSONObject jsonObject) {
            super(jsonObject);
            mInformationList = toProductList(jsonObject.optJSONArray(JSON_L_PAGES));
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

        public ArrayList<ProductItem> getInformationList() {
            return mInformationList;
        }
    }
}
