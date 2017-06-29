package com.bayer.ah.bayertekenscanner.fragment.advices;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.custom.CircleNetworkImageView;
import com.bayer.ah.bayertekenscanner.custom.ListFragment;
import com.bayer.ah.bayertekenscanner.custom.network.TekenErrorListener;
import com.bayer.ah.bayertekenscanner.custom.network.TekenJsonArrayRequest;
import com.bayer.ah.bayertekenscanner.custom.network.TekenResponseListener;
import com.bayer.ah.bayertekenscanner.model.Pet;
import com.bayer.ah.bayertekenscanner.model.TipsItem;
import com.bayer.ah.bayertekenscanner.model.User;
import com.bayer.ah.bayertekenscanner.utils.Constants;
import com.bayer.ah.bayertekenscanner.utils.VolleyHelper;

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

import static com.bayer.ah.bayertekenscanner.fragment.advices.TipsTabFragment.ARGS_TIPS;

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

    private static final int
            REQUEST_FETCH_TIPS = 0,
            REQUEST_PET = 1;

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
        if (User.getInstance(getActivity()).isLoaded()) {
            fetchPetData();
        } else {
            fetchTipsData();
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
        return mTipsItems;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        if (viewType == TYPE_TITLE) {
            return R.layout.item_animal_tips_title;
        } else if (viewType == TYPE_PET) {
            return R.layout.item_pet_with_count;
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
        } else if (mTipsItems.get(position) instanceof Pet) {
            return TYPE_PET;
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
        } else if (item instanceof Pet) {
            ((PetInfoVH) holder).bind((Pet) item);
        } else if (item instanceof TipsItem) {
            ((TipsVH) holder).bind((TipsItem) item);
        }
    }


    private void fetchPetData() {
        TekenJsonArrayRequest request = new TekenJsonArrayRequest(
                Request.Method.GET,
                Constants.Api.getUrl(Constants.Api.FUNC_GET_ANIMALS_BY_USER),
                new TekenResponseListener<JSONArray>() {
                    @Override
                    public void onResponse(int requestCode, JSONArray response) {
                        mTipsItems = new ArrayList<>();
                        Log.d(TAG, response.toString());
                        fetchTipsData();
                        JSONArray jsonArray = response;
                        Pet pet;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                pet = new Pet(jsonObject);
                                if (pet.getType().toApi().equals(mAnimalType)) {
                                    mTipsItems.add(new Pet(jsonObject));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (mTipsItems.size() > 1) {
                            mTipsItems.add(0, getString(R.string.title_research_outcome));
                        }
                    }
                },
                new TekenErrorListener() {
                    @Override
                    public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {
                        Log.d(TAG, status + " " + message);
                    }
                },
                REQUEST_PET
        );

        request.addParam(Constants.Api.QUERY_PARAMETER1, User.getInstance(getActivity()).getEmail());
        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void fetchTipsData() {
        TekenJsonArrayRequest request = new TekenJsonArrayRequest(
                Request.Method.GET,
                Constants.Api.getUrl(Constants.Api.FUNC_GET_TIPS),
                new TekenResponseListener<JSONArray>() {
                    @Override
                    public void onResponse(int requestCode, JSONArray response) {
                        if (mTipsItems.size() == 0) {
                            mTipsItems = new ArrayList<>();
                        }
                        Log.d(TAG, "Success" + response.length());
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = response.getJSONObject(i);

                                TipsInfo tipsInfo = new TipsInfo(jsonObject);

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
                        if (mTipsItems.size() > 0) {
                            loadNewItems(mTipsItems);
                        } else {
                            updateEmptyView();
                        }
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
            mTipsTitleTV.setText(item.getTipsTitle());
            mTipsDetailTV.setText(item.getTipsDescription());
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

    public class PetInfoVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_pet_name)
        TextView mPetNameTV;
        @BindView(R.id.iv_pet_image)
        CircleNetworkImageView mPetImageIV;
        @BindView(R.id.tv_pet_count)
        TextView mCountTV;
        Pet mPet;

        PetInfoVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Pet item) {
            mPet = item;
            mPetNameTV.setText(item.getName());

            if (item.getType().equals(Constants.PetType.DOG)) {
                mPetImageIV.setDefaultImageResId(R.drawable.ic_dog_placeholder);
                mPetImageIV.setErrorImageResId(R.drawable.ic_dog_placeholder);
            } else {
                mPetImageIV.setDefaultImageResId(R.drawable.ic_cat_placeholder);
                mPetImageIV.setErrorImageResId(R.drawable.ic_cat_placeholder);
            }

            if (!item.getPhoto().isEmpty() && !item.getPhoto().equals("null")) {
                mPetImageIV.setImageUrl(item.getPhoto(), VolleyHelper.getInstance(getActivity()).getImageLoader());
            }

            mCountTV.setText(String.valueOf(getAdapterPosition()));
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(TipsTabFragment.ARGS_TIPS, mPet);
            mCallback.onListItemClicked(TYPE_PET, bundle);
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
