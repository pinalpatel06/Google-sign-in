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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.nestedfragments.CommonNodeInterface;
import tekkan.synappz.com.tekkan.custom.nestedfragments.ContainerNodeListFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.FragmentChangeCallback;
import tekkan.synappz.com.tekkan.custom.nestedfragments.NestedFragmentUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends ContainerNodeListFragment<ProductListFragment.ProductsItem, ProductListFragment.ProductVH> {

    private FragmentChangeCallback mCallback;

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
        ButterKnife.bind(this,v);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.white));
        return v;
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_product;
    }

    @Override
    public List<ProductListFragment.ProductsItem> onCreateItems(Bundle savedInstanceState) {
        ArrayList<ProductListFragment.ProductsItem> listItems = new ArrayList<>();

        for(int i=0;i<4;i++){
            listItems.add(new ProductListFragment.ProductsItem("Product #"+(i+1), getString(R.string.dummy_text)));
        }

        return listItems;
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
    public void onBindViewHolder(ProductVH holder, ProductsItem item) {
        holder.bind(item);
    }

    @Override
    public String getTitle() {
        return NestedFragmentUtil.getTitle(getChildFragmentManager(), "Producten", getContainerId());
    }

    @Override
    public boolean onBackPressed() {
        return NestedFragmentUtil.onBackPressed(getContainerId(),getChildFragmentManager(),getChangeCallback());
    }

    @Override
    public void setChild(CommonNodeInterface fragment) {
        NestedFragmentUtil.setChild(fragment,getContainerId(),getChildFragmentManager(),getChangeCallback());
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
        ImageView mProductImageIV;

        public ProductVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ProductListFragment.ProductsItem item) {
            mTitleTV.setText(item.getTitle());
            mDescriptionTV.setText(item.getDescription());
            itemView.setTag(item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            setChild(new ProductDetailsFragment());
        }
    }



    public class ProductsItem {

        private String mTitle;
        private String mDescription;

        public ProductsItem(String title, String description) {
            mTitle = title;
            mDescription = description;
        }

        public String getTitle() {
            return mTitle;
        }

        public String getDescription() {
            return mDescription;
        }


    }
}
