package tekkan.synappz.com.tekkan.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.EditPetActivity;
import tekkan.synappz.com.tekkan.custom.CircleNetworkImageView;
import tekkan.synappz.com.tekkan.custom.network.TekenErrorListener;
import tekkan.synappz.com.tekkan.custom.network.TekenJsonArrayRequest;
import tekkan.synappz.com.tekkan.custom.network.TekenResponseListener;
import tekkan.synappz.com.tekkan.model.Pet;
import tekkan.synappz.com.tekkan.utils.Constants;
import tekkan.synappz.com.tekkan.utils.DateUtils;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPetFragment extends Fragment implements TekenResponseListener, TekenErrorListener {
    private static final String
            TAG = ViewPetFragment.class.getSimpleName(),
            ARGS_PET_DATA = TAG + ".ARGS_PET_DATA";

    private static final int REQUEST_BREED = 0;

    @BindView(R.id.iv_pet_image)
    CircleNetworkImageView mPetPicIV;
    @BindView(R.id.tv_pet_name)
    TextView mPetNameTV;
    @BindView(R.id.tv_date_of_birth)
    TextView mDateOfBirthTv;
    @BindView(R.id.tv_animal_type)
    TextView mIsCatOrDogTv;
    @BindView(R.id.tv_breed)
    TextView mBreedTV;
    @BindView(R.id.tv_weight)
    TextView mWeightTv;
    @BindView(R.id.tv_gender)
    TextView mGenderTv;

    private Pet mPet;
    private HashMap<Long, String> mBreedList;

    public static ViewPetFragment newInstance(Pet pet) {

        Bundle args = new Bundle();
        args.putParcelable(ARGS_PET_DATA, pet);
        ViewPetFragment fragment = new ViewPetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pet_profile_view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(getActivity(), EditPetActivity.class);
                intent.putExtra(EditPetActivity.EXTRA_PET, mPet);
                getActivity().finish();
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_pet, container, false);
        init(v);
        if (mPet != null) {
            fetchBreed();
            updateUI();
        }
        setHasOptionsMenu(true);
        return v;
    }

    private void init(View v) {
        ButterKnife.bind(this, v);
        mPet = getArguments().getParcelable(ARGS_PET_DATA);
    }

    private void updateUI() {

        if(mPet.getType() == Constants.PetType.DOG){
            mPetPicIV.setErrorImageResId(R.drawable.ic_dog_placeholder);
        }else{
            mPetPicIV.setErrorImageResId(R.drawable.ic_cat_placeholder);
        }

        mPetPicIV.setImageUrl(mPet.getPhoto(), VolleyHelper.getInstance(getActivity()).getImageLoader());

        mPetNameTV.setText(mPet.getName());
        mIsCatOrDogTv.setText(
                mPet.getType().toApi()
                        .equalsIgnoreCase(Constants.PetType.DOG.toApi())
                        ? getString(R.string.dog)
                        : getString(R.string.cat)
        );
        if (mBreedList != null) {
            String breed = mBreedList.get(mPet.getBreedId());
            mBreedTV.setText(breed);
        }


        mDateOfBirthTv.setText(DateUtils.toApi(mPet.getBirthDate()));

        mGenderTv.setText(String.valueOf(mPet.getGender()));
        mWeightTv.setText(String.valueOf(mPet.getWeight()));
    }

    private void fetchBreed() {
        TekenJsonArrayRequest request = new TekenJsonArrayRequest(
                Request.Method.GET,
                Constants.Api.getUrl(Constants.Api.FUNC_GET_BREEDS),
                (TekenResponseListener<JSONArray>) this,
                this,
                REQUEST_BREED
        );

        if (mPet.getType().equals(Constants.PetType.CAT)) {
            request.addParam(Constants.Api.QUERY_PARAMETER1, Constants.PetType.CAT.toApi());
        } else {
            request.addParam(Constants.Api.QUERY_PARAMETER1, Constants.PetType.DOG.toApi());
        }

        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    private static final String
            JSON_ID = "id",
            JSON_NAME = "name";

    @Override
    public void onResponse(int requestCode, Object response) {
        JSONArray jsonArray = (JSONArray) response;
        if (jsonArray.length() > 0) {
            mBreedList = new HashMap<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    mBreedList.put(jsonObject.optLong(JSON_ID), jsonObject.optString(JSON_NAME));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            updateUI();
        }
    }

    @Override
    public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {
        Log.d(TAG, status + " " + message);
    }
}
