package tekkan.synappz.com.tekkan.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.ApplyForPetActivity;
import tekkan.synappz.com.tekkan.activity.EditPetActivity;
import tekkan.synappz.com.tekkan.custom.CircleNetworkImageView;
import tekkan.synappz.com.tekkan.custom.ListFragment;
import tekkan.synappz.com.tekkan.model.Pet;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

/**
 * A simple {@link Fragment} subclass.
 */

//Allow user with test click to choose the animal for research

public class TestKitResearchList extends ListFragment<Object, RecyclerView.ViewHolder> implements Response.Listener<JSONArray>, Response.ErrorListener {
    private static final String
            TAG = TestKitResearchList.class.getSimpleName(),
            ARGS_TEEK_BUNDLE = TAG + ".ARGS_TEEK_BUNDLE";

    public static final String
            ARGS_PET_ID = TAG + ".ARGS_PET_ID",
            ARGS_PET_DATA = TAG + ".ARGS_PET_DATA";

    private static final int
            TYPE_HEADER = 0,
            TYPE_PET = 1;

    private ArrayList<Object> mListItems;

    public static TestKitResearchList newInstance(Bundle bundle) {

        Bundle args = new Bundle();
        args.putBundle(ARGS_TEEK_BUNDLE, bundle);
        TestKitResearchList fragment = new TestKitResearchList();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchUserPetData();
    }

    @Override
    public List<Object> onCreateItems(Bundle savedInstanceState) {
        mListItems = new ArrayList<>();

        //first item null to accommodate header
        mListItems.add(null);
/*
        for (int i = 0; i < 4; i++) {
            mListItems.add(new TestKitResearchList.Pet("Pet #" + (i + 1)));
        }*/
        return mListItems;
    }

    @Override
    protected int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_PET;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return R.layout.item_research_header_with_test_kit;
            case TYPE_PET:
                return R.layout.item_pet;
            default:
                throw new UnsupportedOperationException("No such view type:" + viewType);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View v, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new TestKitResearchList.ProfileFieldVH(v);
            case TYPE_PET:
                return new TestKitResearchList.PetVH(v);
            default:
                throw new UnsupportedOperationException("No such view type:" + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Object item) {
        if (holder instanceof TestKitResearchList.PetVH && item instanceof Pet) {
            ((TestKitResearchList.PetVH) holder).bind((Pet) item);
        }
    }

    private void fetchUserPetData() {
        /*String email = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Constants.SP.STRING_USER_EMAIL, null);

        if (email != null) {
            TekenJsonArrayRequest request = new TekenJsonArrayRequest(
                    Request.Method.GET,
                    Constants.Api.getUrl(Constants.Api.FUNC_GET_ANIMALS_BY_USER),
                    this,
                    this
            );

            request.addParam(Constants.Api.QUERY_PARAMETER1, email);
            VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
        }*/

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, error.getMessage());
    }

    @Override
    public void onResponse(JSONArray response) {
        Log.d(TAG, response.length() + "");
        mListItems = new ArrayList<>();
        mListItems.add(null);
        for (int i = 0; i < response.length(); i++) {

            try {
                JSONObject jsonObject = response.getJSONObject(i);
                mListItems.add(new Pet(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        loadNewItems(mListItems);
    }

    class ProfileFieldVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_investigate_title)
        TextView mTitleTV;
        @BindView(R.id.tv_my_pet)
        TextView mMyPetTV;

        ProfileFieldVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.tv_my_pet)
        void showPetProfile() {
            Intent intent = new Intent(getActivity(), EditPetActivity.class);
            startActivity(intent);
        }
    }

    class PetVH extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_pet_name)
        TextView mPetNameTV;
        @BindView(R.id.rt_item_pet)
        RelativeLayout mRelativeLayout;
        @BindView(R.id.iv_pet_image)
        CircleNetworkImageView mPetIV;
        private Pet mItem;

        PetVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Pet pet) {
            mItem = pet;
            mPetNameTV.setText(pet.getName());
            mPetIV.setDefaultImageResId(R.drawable.ic_splash_pets);

            if (pet.getPhoto() != null) {
                Log.d(TAG, pet.getPhoto());
                mPetIV.setImageUrl(pet.getPhoto(), VolleyHelper.getInstance(getActivity()).getImageLoader());
            }
        }

        @OnClick(R.id.rt_item_pet)
        public void showApplyPetActivity() {
            Intent intent = new Intent(getActivity(), ApplyForPetActivity.class);
            Bundle bundle = getArguments().getBundle(ARGS_TEEK_BUNDLE);
            bundle.putLong(ARGS_PET_ID, mItem.getId());
            bundle.putParcelable(ARGS_PET_DATA,mItem);
            intent.putExtra(ApplyForPetActivity.EXTRA_PET_BUNDLE, bundle);
            startActivity(intent);
        }

    }
}
