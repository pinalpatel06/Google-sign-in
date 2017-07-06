package com.bayer.ah.bayertekenscanner.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.activity.EditPetActivity;
import com.bayer.ah.bayertekenscanner.activity.IsCureTakenActivity;
import com.bayer.ah.bayertekenscanner.custom.CircleNetworkImageView;
import com.bayer.ah.bayertekenscanner.custom.ListFragment;
import com.bayer.ah.bayertekenscanner.custom.network.TekenErrorListener;
import com.bayer.ah.bayertekenscanner.custom.network.TekenJsonArrayRequest;
import com.bayer.ah.bayertekenscanner.custom.network.TekenResponseListener;
import com.bayer.ah.bayertekenscanner.model.Pet;
import com.bayer.ah.bayertekenscanner.model.User;
import com.bayer.ah.bayertekenscanner.utils.Constants;
import com.bayer.ah.bayertekenscanner.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */

//Allow user with test click to choose the animal for research

public class TestKitResearchList extends ListFragment<Object, RecyclerView.ViewHolder> implements TekenResponseListener, TekenErrorListener {
    private static final String
            TAG = TestKitResearchList.class.getSimpleName(),
            ARGS_TEEK_BUNDLE = TAG + ".ARGS_TEEK_BUNDLE";

    public static final String
            ARGS_PET_ID = TAG + ".ARGS_PET_ID",
            ARGS_PET_DATA = TAG + ".ARGS_PET_DATA";

    private static final int
            TYPE_HEADER = 0,
            TYPE_PET = 1,
            REQUEST_PET = 2;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchUserPetData();
    }

    @Override
    public List<Object> onCreateItems(Bundle savedInstanceState) {
        mListItems = new ArrayList<>();

        //first item null to accommodate header
        mListItems.add(null);
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
        if (User.getInstance(getActivity()).getEmail() != null) {
            TekenJsonArrayRequest request = new TekenJsonArrayRequest(
                    Request.Method.GET,
                    Constants.Api.getUrl(Constants.Api.FUNC_GET_ANIMALS_BY_USER),
                    this,
                    this,
                    REQUEST_PET
            );
            Log.d(TAG , User.getInstance(getActivity()).getEmail());
            request.addParam(Constants.Api.QUERY_PARAMETER1, User.getInstance(getActivity()).getEmail());
            VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
        }
    }

    @Override
    public void onResponse(int requestCode, Object response) {
        JSONArray jsonArray = (JSONArray) response;
        mListItems = new ArrayList<>();
        mListItems.add(null);
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mListItems.add(new Pet(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        loadNewItems(mListItems);
    }

    @Override
    public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {
        Log.d(TAG , status + " " + message + error);
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
        @BindView(R.id.view_disable)
        View mDisableView;

        private Pet mItem;

        PetVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Pet pet) {
            mItem = pet;
            mPetNameTV.setText(pet.getName());
            mPetIV.setDefaultImageResId(R.drawable.ic_splash_pets);

            if (!pet.getPhoto().equals("null")) {
                Log.d(TAG, pet.getPhoto());
                mPetIV.setImageUrl(pet.getPhoto(), VolleyHelper.getInstance(getActivity()).getImageLoader());
            }

            if(pet.isResearch()){
                mDisableView.setVisibility(View.VISIBLE);
            }
        }

        @OnClick(R.id.rt_item_pet)
        public void showApplyPetActivity() {
           /* Intent intent = new Intent(getActivity(), ApplyForPetActivity.class);
            Bundle bundle = getArguments().getBundle(ARGS_TEEK_BUNDLE);
            bundle.putLong(ARGS_PET_ID, mItem.getId());
            bundle.putParcelable(ARGS_PET_DATA,mItem);
            intent.putExtra(ApplyForPetActivity.EXTRA_PET_BUNDLE, bundle);
            startActivity(intent);*/

            Intent intent = new Intent(getActivity(), IsCureTakenActivity.class);
            Bundle bundle = getArguments().getBundle(ARGS_TEEK_BUNDLE);
            bundle.putLong(ARGS_PET_ID, mItem.getId());
            bundle.putParcelable(ARGS_PET_DATA,mItem);
            intent.putExtra(IsCureTakenActivity.EXTRA_PET_BUNDLE, bundle);
            startActivity(intent);
        }

    }
}
