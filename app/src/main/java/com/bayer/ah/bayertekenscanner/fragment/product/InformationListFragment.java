package com.bayer.ah.bayertekenscanner.fragment.product;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.custom.CircleNetworkImageView;
import com.bayer.ah.bayertekenscanner.custom.nestedfragments.ContainerNodeListFragment;
import com.bayer.ah.bayertekenscanner.custom.nestedfragments.NestedFragmentUtil;
import com.bayer.ah.bayertekenscanner.model.ProductItem;
import com.bayer.ah.bayertekenscanner.utils.VolleyHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationListFragment extends ContainerNodeListFragment<ProductItem, InformationListFragment.ProductVH> {
    private static final String
            TAG = InformationListFragment.class.getSimpleName(),
            ARGS_INFORMATION_LIST = TAG + ".ARGS_INFORMATION_LIST";
    private ArrayList<ProductItem> mProductList;

    public static InformationListFragment newInstance(ArrayList<ProductItem> productList) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARGS_INFORMATION_LIST, productList);
        InformationListFragment fragment = new InformationListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, v);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        return v;
    }

    @Override
    public List<ProductItem> onCreateItems(Bundle savedInstanceState) {
        mProductList = getArguments().getParcelableArrayList(ARGS_INFORMATION_LIST);
        return mProductList;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_information_fragment;
    }

    @Override
    public InformationListFragment.ProductVH onCreateViewHolder(View v, int viewType) {
        return new InformationListFragment.ProductVH(v);
    }

    @Override
    public void onBindViewHolder(InformationListFragment.ProductVH holder, ProductItem item) {
        holder.bind(item);
    }

    @Override
    public String getTitle() {
        return NestedFragmentUtil.getTitle(getChildFragmentManager(), "Informatie artikelen", getContainerId());
    }

    class ProductVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_product_title)
        TextView mTitleTV;
        @BindView(R.id.tv_product_details)
        TextView mDescriptionTV;
        @BindView(R.id.iv_product_image)
        CircleNetworkImageView mProductImageIV;
        ProductItem mProductItem;

        public ProductVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ProductItem item) {
            mProductItem = item;
            mTitleTV.setText(item.getTitle());
            mDescriptionTV.setText(item.getDescription());
            itemView.setTag(item);
            mProductImageIV.setDefaultImageResId(R.drawable.ic_splash_pets);
            if (item.getProfileUrl().equals("null")) {
                mProductImageIV.setImageUrl(item.getProfileUrl(), VolleyHelper.getInstance(getActivity()).getImageLoader());
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            setChild(InformationDetailFragment.newInstance(mProductItem));
        }
    }

}
