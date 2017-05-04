package tekkan.synappz.com.tekkan.fragment.product;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.nestedfragments.CommonNodeInterface;
import tekkan.synappz.com.tekkan.custom.nestedfragments.ContainerNodeListFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.NestedFragmentUtil;

public class ProductTabFragment extends ContainerNodeListFragment<ProductTabFragment.ProductsItem, ProductTabFragment.ProductVH> {

    private static final int
            INDEX_PRODUCT = 0,
            INDEX_INFO_ARTICLE = 1,
            INDEX_SCIENTIFIC = 2;

    @Override
    public List<ProductsItem> onCreateItems(Bundle savedInstanceState) {
        ArrayList<ProductsItem> listItems = new ArrayList<>();

        listItems.add(new ProductsItem("Product", getString(R.string.dummy_text)));
        listItems.add(new ProductsItem("Informatie artikelen", getString(R.string.dummy_text)));
        listItems.add(new ProductsItem("Wetenschappelijk", getString(R.string.dummy_text)));

        return listItems;
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
    public void onBindViewHolder(ProductVH holder, ProductsItem item) {
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
        ImageView mProductImageIV;

        public ProductVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ProductsItem item) {
            mTitleTV.setText(item.getTitle());
            mDescriptionTV.setText(item.getDescription());
            itemView.setTag(item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            CommonNodeInterface fragment = null;

            switch (getAdapterPosition()) {
                case INDEX_PRODUCT:
                    fragment = new ProductListFragment();
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

    class ProductsItem {

        private String mTitle;
        private String mDescription;

        ProductsItem(String title, String description) {
            mTitle = title;
            mDescription = description;
        }

        String getTitle() {
            return mTitle;
        }

        String getDescription() {
            return mDescription;
        }
    }

}
