package tekkan.synappz.com.tekkan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.ListFragment;

/**
 * Created by Tejas Sherdiwala on 4/24/2017.
 * &copy; Knoxpo
 */

public class FragmentAnimalTips extends ListFragment<FragmentAnimalTips.TipsItem,FragmentAnimalTips.TipsVH> {

    private static final String
            TAG = FragmentAnimalTips.class.getSimpleName(),
            ARGS_ANIMAL_TYPE = TAG + ".ARGS_ANIMAL_TYPE";

    private ArrayList<TipsItem> mTipsItems;
    private String mAnimalType;


    public static FragmentAnimalTips newInstance(String animalTypes) {
        Log.d(TAG, animalTypes);
        Bundle args = new Bundle();
        args.putString(ARGS_ANIMAL_TYPE,animalTypes);
        FragmentAnimalTips fragment = new FragmentAnimalTips();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAnimalType = getArguments().getString(ARGS_ANIMAL_TYPE);
    }

    @Override
    public List<TipsItem> onCreateItems(Bundle savedInstanceState) {
        mTipsItems = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mTipsItems.add(
                    new TipsItem("TIP " + (i+1), mAnimalType + ", Secondary line text Lorem ipsum dolor sit amet, consectetur adipisscing elit, Nam massa quam.")
            );
        }
        return mTipsItems;
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_onderzoek_list;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_tips;
    }

    @Override
    public TipsVH onCreateViewHolder(View v, int viewType) {
        return new TipsVH(v);
    }

    @Override
    public void onBindViewHolder(TipsVH holder, TipsItem item) {
        holder.bind(item);
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


        TipsItem(String title, String details) {
            mTipsTitle = title;
            mTipsDetails = details;
        }


        String getTipsTitle() {
            return mTipsTitle;
        }

        String getTipsDetails() {
            return mTipsDetails;
        }
    }
}
