package tekkan.synappz.com.tekkan.fragment.product;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.CircleNetworkImageView;
import tekkan.synappz.com.tekkan.custom.nestedfragments.CommonNodeInterface;
import tekkan.synappz.com.tekkan.custom.nestedfragments.ContainerNodeListFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.FragmentChangeCallback;
import tekkan.synappz.com.tekkan.custom.nestedfragments.NestedFragmentUtil;
import tekkan.synappz.com.tekkan.model.ProductItem;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends ContainerNodeListFragment<ProductItem, ProductListFragment.ProductVH> {
    private static final String
            TAG = ProductTabFragment.class.getSimpleName(),
            ARGS_PRODUCT_LIST = TAG + ".ARGS_PRODUCT_LIST";

    private FragmentChangeCallback mCallback;
    private ArrayList<ProductItem> mProductList;

    public static ProductListFragment newInstance(ArrayList<ProductItem> productList) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(ARGS_PRODUCT_LIST, productList);
        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (FragmentChangeCallback) getActivity();
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
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
    protected int getViewLayoutId() {
        return R.layout.fragment_product;
    }

    @Override
    public List<ProductItem> onCreateItems(Bundle savedInstanceState) {
        mProductList = getArguments().getParcelableArrayList(ARGS_PRODUCT_LIST);
        return mProductList;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_product_fragment;
    }

    @Override
    public ProductListFragment.ProductVH onCreateViewHolder(View v, int viewType) {
        return new ProductListFragment.ProductVH(v);
    }

    @Override
    public void onBindViewHolder(ProductVH holder, ProductItem item) {
        holder.bind(item);
    }

    @Override
    public String getTitle() {
        return NestedFragmentUtil.getTitle(getChildFragmentManager(), getString(R.string.product_list_title), getContainerId());
    }

    @Override
    public boolean onBackPressed() {
        return NestedFragmentUtil.onBackPressed(getContainerId(), getChildFragmentManager(), getChangeCallback());
    }

    @Override
    public void setChild(CommonNodeInterface fragment) {
        NestedFragmentUtil.setChild(fragment, getContainerId(), getChildFragmentManager(), getChangeCallback());
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public FragmentChangeCallback getChangeCallback() {
        return mCallback;
    }


    public class ProductVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_product_title)
        TextView mTitleTV;
        @BindView(R.id.tv_product_details)
        TextView mDescriptionTV;
        @BindView(R.id.iv_product_image)
        CircleNetworkImageView mProductImageIV;
        private ProductItem mItem;

        public ProductVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ProductItem item) {
            mItem = item;
            mTitleTV.setText(item.getTitle());
            mDescriptionTV.setText(item.getDescription());
            itemView.setTag(item);
            mProductImageIV.setImageUrl(item.getProfileUrl(), VolleyHelper.getInstance(getActivity()).getImageLoader());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            setChild(ProductDetailsFragment.newInstance(mItem));
        }
    }
}
