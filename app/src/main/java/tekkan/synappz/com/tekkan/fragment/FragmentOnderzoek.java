package tekkan.synappz.com.tekkan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;

/**
 * Created by Tejas Sherdiwala on 4/22/2017.
 * &copy; Knoxpo
 */

public class FragmentOnderzoek extends Fragment {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;

    private ArrayList<TipsItem> mTipsItems;
    private Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_onderzoek, container, false);
        init(v);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private void init(View v) {
        ButterKnife.bind(this, v);
        mTipsItems = new ArrayList<>();
        mAdapter = new Adapter();
        onCreateItems();
    }

    private void onCreateItems() {

        for (int i = 0; i < 3; i++) {
            mTipsItems.add(
                    new TipsItem("TIP 1", "Secondary line text Lorem ipsum dolor sit amet, consectetur adipisscing elit, Nam massa quam.")
            );
        }

    }

    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater mLayoutInflater;

        Adapter() {
            mLayoutInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = mLayoutInflater.inflate(R.layout.item_tips, parent, false);
            return new TipsVH(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((TipsVH) holder).bind(mTipsItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mTipsItems.size();
        }
    }

    public class TipsVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_tips_title)
        TextView mTipsTitleTV;
        @BindView(R.id.tv_tips_details)
        TextView mTipsDetailTV;

        TipsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(TipsItem item) {
            mTipsTitleTV.setText(item.getTipsTitle());
            mTipsDetailTV.setText(item.getTipsDetails());
        }
    }

    public class TipsItem {
        private String mTipsTitle;
        private String mTipsDetails;


        public TipsItem(String title, String details) {
            mTipsTitle = title;
            mTipsDetails = details;
        }


        public String getTipsTitle() {
            return mTipsTitle;
        }

        public String getTipsDetails() {
            return mTipsDetails;
        }
    }
}
