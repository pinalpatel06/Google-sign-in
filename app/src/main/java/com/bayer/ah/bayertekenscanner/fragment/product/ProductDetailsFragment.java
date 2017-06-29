package com.bayer.ah.bayertekenscanner.fragment.product;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.custom.nestedfragments.CommonNodeInterface;
import com.bayer.ah.bayertekenscanner.model.ProductItem;
import com.bayer.ah.bayertekenscanner.utils.VolleyHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailsFragment extends Fragment implements CommonNodeInterface {
    private static final String
            TAG = ProductDetailsFragment.class.getSimpleName(),
            ARGS_PRODUCT_DETAIL = TAG + ".ARGS_PRODUCT_DETAIL";
    private ProductItem mProduct;

    @BindView(R.id.iv_product_details_pic)
    NetworkImageView mBannerIV;
    @BindView(R.id.tv_product_description)
    TextView mProductDescriptionTV;
    @BindView(R.id.tv_product_content)
    TextView mProductContentTV;
    @BindView(R.id.btn_more_info)
    Button mMoreInfoBtn;


    public static ProductDetailsFragment newInstance(ProductItem item) {

        Bundle args = new Bundle();
        args.putParcelable(ARGS_PRODUCT_DETAIL, item);
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product_details, container, false);
        ButterKnife.bind(this, v);
        mProduct = getArguments().getParcelable(ARGS_PRODUCT_DETAIL);
        updateUI();
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        return v;
    }

    private void updateUI() {
        if(!mProduct.getBannerUrl().equals("null")) {
            mBannerIV.setImageUrl(mProduct.getBannerUrl(), VolleyHelper.getInstance(getActivity()).getImageLoader());
        }
        mBannerIV.setDefaultImageResId(R.drawable.ic_splash_pets);
        mProductDescriptionTV.setText(mProduct.getDescription());
        mProductContentTV.setText(mProduct.getContent());
        if (mProduct.getDetailPageLink().equals("null")) {
            mMoreInfoBtn.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_more_info)
    public void showDetail() {
        try {
            String url = mProduct.getDetailPageLink();
            if (url.length() > 0) {
                Intent intentWeb;
                if (url.startsWith("https:") || url.startsWith("http:")) {
                    intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(mProduct.getDetailPageLink()));
                } else {
                    intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + url));
                }
                startActivity(intentWeb);
            } else {
                Toast.makeText(
                        getActivity(),
                        R.string.link_err,
                        Toast.LENGTH_SHORT
                ).show();
            }
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "Not Activity found to handle this intent");
        }
    }

    @Override
    public String getTitle() {
        return mProduct.getTitle();
    }

    @Override
    public boolean shouldDisplayHomeAsUpEnabled() {
        return true;
    }
}
