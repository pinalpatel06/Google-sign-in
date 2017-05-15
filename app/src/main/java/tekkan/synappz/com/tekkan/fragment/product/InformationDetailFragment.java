package tekkan.synappz.com.tekkan.fragment.product;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.nestedfragments.CommonNodeInterface;
import tekkan.synappz.com.tekkan.model.ProductItem;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationDetailFragment extends Fragment implements CommonNodeInterface {
    private static final String
            TAG = InformationListFragment.class.getSimpleName(),
            ARGS_PRODUCT_DETAIL = TAG + ".ARGS_PRODUCT_DETAIL";

    private ProductItem mProduct;

    @BindView(R.id.iv_banner)
    NetworkImageView mBannerIV;
    @BindView(R.id.tv_title)
    TextView mProductDescriptionTV;
    @BindView(R.id.tv_content)
    TextView mProductContentTV;

    public static InformationDetailFragment newInstance(ProductItem item) {

        Bundle args = new Bundle();
        args.putParcelable(ARGS_PRODUCT_DETAIL, item);
        InformationDetailFragment fragment = new InformationDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_information_article_details, container, false);
        ButterKnife.bind(this,v);
        mProduct = getArguments().getParcelable(ARGS_PRODUCT_DETAIL);
        updateUI();
        return v;
    }

    private void updateUI() {
        mBannerIV.setDefaultImageResId(R.drawable.ic_splash_pets);
        if(!mProduct.getBannerUrl().equals("null")) {
            mBannerIV.setImageUrl(mProduct.getBannerUrl(), VolleyHelper.getInstance(getActivity()).getImageLoader());
        }
        mProductDescriptionTV.setText(mProduct.getDescription());
        mProductContentTV.setText(mProduct.getContent());
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
