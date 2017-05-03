package tekkan.synappz.com.tekkan.fragment.product;


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
import tekkan.synappz.com.tekkan.custom.nestedfragments.ContainerNodeListFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.NestedFragmentUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationListFragment extends ContainerNodeListFragment<InformationListFragment.ProductsItem, InformationListFragment.ProductVH> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.white));
        v.setClickable(true);
        return v;
    }

    @Override
    public List<InformationListFragment.ProductsItem> onCreateItems(Bundle savedInstanceState) {
        ArrayList<InformationListFragment.ProductsItem> listItems = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            listItems.add(new InformationListFragment.ProductsItem("Product #" + (i + 1), getString(R.string.dummy_text)));
        }

        return listItems;
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
    public void onBindViewHolder(InformationListFragment.ProductVH holder, InformationListFragment.ProductsItem item) {
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
        ImageView mProductImageIV;

        public ProductVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(InformationListFragment.ProductsItem item) {
            mTitleTV.setText(item.getTitle());
            mDescriptionTV.setText(item.getDescription());
            itemView.setTag(item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            setChild(new InformationDetailFragment());
        }
    }

    class ProductsItem {

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
