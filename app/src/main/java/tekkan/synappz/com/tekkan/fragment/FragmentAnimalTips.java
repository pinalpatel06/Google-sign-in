package tekkan.synappz.com.tekkan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.ListFragment;

/**
 * Created by Tejas Sherdiwala on 4/24/2017.
 * &copy; Knoxpo
 */

public class FragmentAnimalTips extends ListFragment<Object, RecyclerView.ViewHolder> implements AnimalTipsCallback {

    private static final String
            TAG = FragmentAnimalTips.class.getSimpleName(),
            TAG_CHILD_FRAGMENT = TAG + ".TAG_CHILD_FRAGMENT",
            ARGS_ANIMAL_TYPE = TAG + ".ARGS_ANIMAL_TYPE";

    private ArrayList<Object> mTipsItems;
    private String mAnimalType;
    private boolean isPetInfoAvailable = true;
    private static final int
            TYPE_TITLE = 2,
            TYPE_PET = 3,
            TYPE_TIPS = 4;

    private AnimalTipsCallback mCallback;

    public static FragmentAnimalTips newInstance(String animalTypes) {
        Log.d(TAG, animalTypes);
        Bundle args = new Bundle();
        args.putString(ARGS_ANIMAL_TYPE, animalTypes);
        FragmentAnimalTips fragment = new FragmentAnimalTips();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (AnimalTipsCallback) getParentFragment();
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAnimalType = getArguments().getString(ARGS_ANIMAL_TYPE);
        if (("HOND").equals(mAnimalType)) {
            isPetInfoAvailable = true;
        } else {
            isPetInfoAvailable = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        return v;
    }

    @Override
    public List<Object> onCreateItems(Bundle savedInstanceState) {
        mTipsItems = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (i == 0 && isPetInfoAvailable) {
                mTipsItems.add("UITSTAG ONDERZOEK");
            } else if (i == 0 && !isPetInfoAvailable) {
                mTipsItems.add("TIPS EN ADVIES");
            } else if (i == 1 && isPetInfoAvailable) {
                mTipsItems.add(
                        new PetInfoItem("Hummer", "Secondary line text Lorem ipsum dapibus,neque id cursus")
                );
            } else if (i == 2 && isPetInfoAvailable) {
                mTipsItems.add("TIPS EN ADVIES");
            } else {
                mTipsItems.add(
                        new TipsItem("TIP " + (i + 1), mAnimalType + ", Secondary line text Lorem ipsum dolor sit amet, consectetur adipisscing elit, Nam massa quam.")
                );
            }
        }
        return mTipsItems;
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_onderzoek_list;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        if (viewType == TYPE_TITLE) {
            return R.layout.item_animal_tips_title;
        } else if (viewType == TYPE_PET) {
            return R.layout.item_pet_info;
        } else if (viewType == TYPE_TIPS) {
            return R.layout.item_tips;
        } else {
            return 0;
        }
    }

    @Override
    protected int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TITLE;
        } else if (position == 1 && isPetInfoAvailable) {
            return TYPE_PET;
        } else if (position == 2 && isPetInfoAvailable) {
            return TYPE_TITLE;
        } else {
            return TYPE_TIPS;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View v, int viewType) {
        if (viewType == TYPE_TITLE) {
            return new StringVH(v);
        } else if (viewType == TYPE_PET) {
            return new PetInfoVH(v);
        } else if (viewType == TYPE_TIPS) {
            return new TipsVH(v);
        } else {
            return new StringVH(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Object item) {
        if (item instanceof String) {
            ((StringVH) holder).bind((String) item);
        } else if (item instanceof PetInfoItem) {
            ((PetInfoVH) holder).bind((PetInfoItem) item);
        } else if (item instanceof TipsItem) {
            ((TipsVH) holder).bind((TipsItem) item);
        }
    }

    private void setChildFragment(Fragment fragment, String tag) {
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void setTabLayoutVisibility(boolean isOn) {
        mCallback.setTabLayoutVisibility(isOn);
    }

    public class TipsVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_tips_title)
        TextView mTipsTitleTV;
        @BindView(R.id.tv_tips_details)
        TextView mTipsDetailTV;
        @BindView(R.id.lt_item_tips)
        LinearLayout mLayout;

        TipsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(TipsItem item) {
            mTipsTitleTV.setText(item.getTipsTitle());
            mTipsDetailTV.setText(item.getTipsDetails());
        }

        @OnClick(R.id.lt_item_tips)
        public void OnTipsItemViewClicked() {
            setChildFragment(FragmentAnimalTipsDetails.newInstance(mAnimalType), TAG_CHILD_FRAGMENT);
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

    public class PetInfoVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_pet_name)
        TextView mPetNameTV;
        @BindView(R.id.tv_pet_details)
        TextView mPetDetailTV;
        @BindView(R.id.gl_pet_info)
        GridLayout mGridLayout;

        PetInfoVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(PetInfoItem item) {
            mPetNameTV.setText(item.getPetName());
            mPetDetailTV.setText(item.getPetDetails());
        }

        @OnClick(R.id.gl_pet_info)
        public void onClickPetInfo() {
            setChildFragment(new FragmentResearchOutcome(), TAG_CHILD_FRAGMENT);
        }
    }

    public class PetInfoItem {
        public String getPetName() {
            return mPetName;
        }

        public String getPetDetails() {
            return mPetDetails;
        }

        private String mPetName, mPetDetails;

        PetInfoItem(String name, String petDetails) {
            mPetName = name;
            mPetDetails = petDetails;
        }
    }

    public class StringVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_animal_tips_title)
        TextView mAnimalTipsTV;

        StringVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String item) {
            mAnimalTipsTV.setText(item);
        }
    }
}
