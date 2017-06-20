package tekkan.synappz.com.tekkan.fragment.advices;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.ListFragment;
import tekkan.synappz.com.tekkan.custom.network.TekenErrorListener;
import tekkan.synappz.com.tekkan.custom.network.TekenJsonArrayRequest;
import tekkan.synappz.com.tekkan.custom.network.TekenResponseListener;
import tekkan.synappz.com.tekkan.model.TipsItem;
import tekkan.synappz.com.tekkan.utils.Constants;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

import static tekkan.synappz.com.tekkan.fragment.advices.TipsTabFragment.ARGS_TIPS;

/**
 * Created by Tejas Sherdiwala on 4/24/2017.
 * &copy; Knoxpo
 * <p>
 * Show in the view pager on tab research
 */

public class AnimalTipsListFragment extends ListFragment<Object, RecyclerView.ViewHolder> {

    private static final String
            TAG = AnimalTipsListFragment.class.getSimpleName(),
            TAG_CHILD_FRAGMENT = TAG + ".TAG_CHILD_FRAGMENT",
            ARGS_ANIMAL_TYPE = TAG + ".ARGS_ANIMAL_TYPE";


    public static final String
            ANIMAL_TYPE = TAG + ".ANIMAL_TYPE",
            ANIMAL_DOG = TAG + ".ANIMAL_DOG",
            ANIMAL_CAT = TAG + ".ANIMAL_CAT";

    private static final int REQUEST_FETCH_TIPS = 0;

    public interface Callback {
        void onListItemClicked(int type, Bundle details);
    }

    private Callback mCallback;


    private ArrayList<Object> mTipsItems;
    private String mAnimalType;
    private boolean isPetInfoAvailable = true;
    private static final int
            TYPE_TITLE = 2;

    public static final int
            TYPE_PET = 3,
            TYPE_TIPS = 4;

    public static AnimalTipsListFragment newInstance(String animalTypes) {
        Log.d(TAG, animalTypes);
        Bundle args = new Bundle();
        args.putString(ARGS_ANIMAL_TYPE, animalTypes);
        AnimalTipsListFragment fragment = new AnimalTipsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (Callback) getParentFragment();
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
       /* if (ANIMAL_DOG.equals(mAnimalType)) {
            isPetInfoAvailable = true;
        } else {
            isPetInfoAvailable = false;
        }*/
        fetchTipsData();
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
        return mTipsItems;
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
        /*if (position == 0) {
            return TYPE_TITLE;
        } else if (position == 1 && isPetInfoAvailable) {
            return TYPE_PET;
        } else if (position == 2 && isPetInfoAvailable) {
            return TYPE_TITLE;
        } else {
            return TYPE_TIPS;
        }*/

        if (mTipsItems.get(position) instanceof String) {
            return TYPE_TITLE;
        } else if (mTipsItems.get(position) instanceof TipsItem) {
            return TYPE_TIPS;
        } else {
            throw new UnsupportedOperationException("No view found");
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


    private void fetchTipsData() {
        TekenJsonArrayRequest request = new TekenJsonArrayRequest(
                Request.Method.GET,
                Constants.Api.getUrl(Constants.Api.FUNC_GET_TIPS),
                new TekenResponseListener<JSONArray>() {
                    @Override
                    public void onResponse(int requestCode, JSONArray response) {
                        Log.d(TAG, "Success" + response.length());
                        mTipsItems = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = response.getJSONObject(i);

                                TipsInfo tipsInfo = new TipsInfo(jsonObject);

                               /* mTipsItems.add(tipsInfo.getName());

                                if (tipsInfo.getTipsItems().size() > 0) {
                                    for (int j = 0; j < tipsInfo.size(); j++) {
                                        TipsItem item = tipsInfo.getTipsItems().get(j);
                                        if (mAnimalType.equals(item.getType())) {
                                            mTipsItems.add(item);
                                        }
                                    }
                                }*/

                                if (mAnimalType.equals(Constants.PetType.DOG.toApi()) && tipsInfo.getDogTipsList().size() > 0) {
                                    Set<String> title = tipsInfo.getDogTipsList().keySet();
                                    for (String t :
                                            title) {
                                        mTipsItems.add(t);
                                        mTipsItems.add(tipsInfo.getDogTipsList().get(t));
                                    }
                                } else if (mAnimalType.equals(Constants.PetType.CAT.toApi()) && tipsInfo.getCatTipsList().size() > 0) {
                                   Set<String> title = tipsInfo.getCatTipsList().keySet();

                                    for (String t :
                                            title) {
                                        mTipsItems.add(t);
                                        mTipsItems.add(tipsInfo.getCatTipsList().get(t));
                                    }
                                }

                            } catch (JSONException e) {
                                continue;
                            }
                        }
                        loadNewItems(mTipsItems);
                    }
                },
                new TekenErrorListener() {
                    @Override
                    public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {
                        Log.d(TAG, error.toString());
                    }
                },
                REQUEST_FETCH_TIPS
        );
        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);

    }

    public class TipsVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_tips_title)
        TextView mTipsTitleTV;
        @BindView(R.id.tv_tips_details)
        TextView mTipsDetailTV;
        @BindView(R.id.lt_item_tips)
        LinearLayout mLayout;
        private TipsItem mTipsItem;

        TipsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(TipsItem item) {
            mTipsItem = item;
            if (mAnimalType.equals(Constants.PetType.DOG.toApi())) {
                if (item.getType().equals(Constants.PetType.DOG.toApi())) {
                    mTipsTitleTV.setText(item.getTipsTitle());
                    mTipsDetailTV.setText(item.getTipsDescription());
                }
            } else if (mAnimalType.equals(Constants.PetType.CAT.toApi())) {
                if (item.getType().equals(Constants.PetType.CAT.toApi())) {
                    mTipsTitleTV.setText(item.getTipsTitle());
                    mTipsDetailTV.setText(item.getTipsDescription());
                }
            }

        }

        @OnClick(R.id.lt_item_tips)
        public void OnTipsItemViewClicked() {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARGS_TIPS, mTipsItem);
            mCallback.onListItemClicked(TYPE_TIPS, bundle);
        }

    }


    private class TipsInfo {
        public String getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }

        public String getDescription() {
            return mDescription;
        }

        private String mId;
        private String mName;
        private String mDescription;
        private ArrayList<TipsItem> mTipsItems;

        public LinkedHashMap<String, TipsItem> getDogTipsList() {
            return mDogTipsList;
        }

        public LinkedHashMap<String, TipsItem> getCatTipsList() {
            return mCatTipsList;
        }

        private LinkedHashMap<String, TipsItem> mDogTipsList;
        private LinkedHashMap<String, TipsItem> mCatTipsList;

        private static final String
                JSON_CATAGORY_ID = "category_id",
                JSON_CATAGORY_NAME = "category_name",
                JSON_CATAGORY_DESCRIPTION = "category_description",
                JSON_TIPS = "tips";

        TipsInfo(JSONObject jsonObject) {
            mId = jsonObject.optString(JSON_CATAGORY_ID);
            mName = jsonObject.optString(JSON_CATAGORY_NAME);
            mDescription = jsonObject.optString(JSON_CATAGORY_DESCRIPTION);
            mTipsItems = new ArrayList<>();
            mDogTipsList = new LinkedHashMap<>();
            mCatTipsList = new LinkedHashMap<>();
            toTipsList(jsonObject.optJSONArray(JSON_TIPS));
        }

        private void toTipsList(JSONArray array) {
            if (array != null) {
                TipsItem item;
                for (int i = 0; i < array.length(); i++) {
                    item = new TipsItem(array.optJSONObject(i));
                    if (item.getType().equals(Constants.PetType.DOG.toApi())) {
                        mDogTipsList.put(mName, item);
                    } else if (item.getType().equals(Constants.PetType.CAT.toApi())) {
                        mCatTipsList.put(mName, item);
                    }
                    mTipsItems.add(item);
                }
            }
        }

        public ArrayList<TipsItem> getTipsItems() {
            return mTipsItems;
        }

        public int size() {
            if (mTipsItems != null) {
                return mTipsItems.size();
            }
            return -1;
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
            //setChildFragment(new ResearchOutcomeFragment(), TAG_CHILD_FRAGMENT);
            Bundle bundle = new Bundle();
            bundle.putString(ANIMAL_TYPE, mAnimalType);
            mCallback.onListItemClicked(TYPE_PET, bundle);
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
