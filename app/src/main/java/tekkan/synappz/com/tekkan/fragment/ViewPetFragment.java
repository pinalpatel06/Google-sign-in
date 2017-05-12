package tekkan.synappz.com.tekkan.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.EditPetActivity;
import tekkan.synappz.com.tekkan.custom.CircleNetworkImageView;
import tekkan.synappz.com.tekkan.model.Pet;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPetFragment extends Fragment {
    private static final String
            TAG = ViewPetFragment.class.getSimpleName(),
            ARGS_PET_DATA = TAG + ".ARGS_PET_DATA";


    Bundle mArgs;
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

    public static ViewPetFragment newInstance(Pet pet) {

        Bundle args = new Bundle();
        args.putParcelable(ARGS_PET_DATA, pet);
        ViewPetFragment fragment = new ViewPetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_pet, container, false);
        init(v);
        if (mPet != null) {
            updateUI();
        }
        setHasOptionsMenu(true);
        return v;
    }

    private void init(View v) {
        ButterKnife.bind(this, v);
        mPet = getArguments().getParcelable(ARGS_PET_DATA);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pet_profile_update, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem editItem = menu.findItem(R.id.action_edit);
        MenuItem doneItem = menu.findItem(R.id.action_done);
        doneItem.setVisible(false);
        editItem.setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_edit:
                getActivity().invalidateOptionsMenu();
                Intent intent = new Intent(getActivity(), EditPetActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mArgs = args;
    }


    private void updateUI() {
        mPetPicIV.setDefaultImageResId(R.drawable.ic_splash_pets);
        mPetPicIV.setImageUrl(mPet.getProfileImgUrl(), VolleyHelper.getInstance(getActivity()).getImageLoader());
        mPetNameTV.setText(mPet.getName());
        mIsCatOrDogTv.setText(mPet.getAnimalType());
        mBreedTV.setText(String.valueOf(mPet.getBreedId()));
        mDateOfBirthTv.setText(Pet.toStringDate(mPet.getDateOfBirth()));
        mGenderTv.setText(String.valueOf(mPet.getGender()));
        mWeightTv.setText(String.valueOf(mPet.getWeight()));
    }

}
