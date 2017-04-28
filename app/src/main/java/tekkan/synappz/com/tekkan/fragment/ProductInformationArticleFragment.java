package tekkan.synappz.com.tekkan.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.ListFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductInformationArticleFragment extends ListFragment<ProductInformationArticleFragment.ProductsItem, ProductInformationArticleFragment.ProductVH> {

    public ProductInformationArticleFragment() {
        // Required empty public constructor
    }

    @Override
    public List<ProductInformationArticleFragment.ProductsItem> onCreateItems(Bundle savedInstanceState) {
        ArrayList<ProductInformationArticleFragment.ProductsItem> listItems = new ArrayList<>();

        for(int i=0;i<4;i++){
            listItems.add(new ProductInformationArticleFragment.ProductsItem("Product #"+(i+1), getString(R.string.dummy_text)));
        }

        return listItems;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_information_fragment;
    }

    @Override
    public ProductInformationArticleFragment.ProductVH onCreateViewHolder(View v, int viewType) {
        return new ProductInformationArticleFragment.ProductVH(v);
    }

    @Override
    public void onBindViewHolder(ProductInformationArticleFragment.ProductVH holder, ProductInformationArticleFragment.ProductsItem item) {
        holder.bind(item);
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

        public void bind(ProductInformationArticleFragment.ProductsItem item) {
            mTitleTV.setText(item.getTitle());
            mDescriptionTV.setText(item.getDescription());
            itemView.setTag(item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(),"item_clicked",Toast.LENGTH_SHORT).show();
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
