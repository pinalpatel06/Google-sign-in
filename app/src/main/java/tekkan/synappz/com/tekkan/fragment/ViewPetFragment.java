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

import static tekkan.synappz.com.tekkan.fragment.EditPetFragment.TAG_BREED;
import static tekkan.synappz.com.tekkan.fragment.EditPetFragment.TAG_DOB;
import static tekkan.synappz.com.tekkan.fragment.EditPetFragment.TAG_GENDER;
import static tekkan.synappz.com.tekkan.fragment.EditPetFragment.TAG_IS_CAT_OR_DOG;
import static tekkan.synappz.com.tekkan.fragment.EditPetFragment.TAG_WEIGHT;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPetFragment extends Fragment {

//    @BindView(R.id.et_pet_name)
//    EditText mPetNameEt;

    Bundle mArgs;

    @BindView(R.id.tv_date_of_birth)
    TextView mDateOfBirthTv;

    @BindView(R.id.tv_is_cat_or_dog)
    TextView mIsCatOrDogTv;

    @BindView(R.id.tv_breed)
    TextView mBreedTv;

    @BindView(R.id.tv_weight)
    TextView mWeightTv;

    @BindView(R.id.tv_gender)
    TextView mGenderTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_pet, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, v);
        if (mArgs != null) {
            updateUI();
        }
        return v;
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
        mIsCatOrDogTv.setText(mArgs.getString(TAG_IS_CAT_OR_DOG));
        mBreedTv.setText(mArgs.getString(TAG_BREED));
        mDateOfBirthTv.setText(mArgs.getString(TAG_DOB));
        mGenderTv.setText(mArgs.getString(TAG_GENDER));
        mWeightTv.setText(mArgs.getString(TAG_WEIGHT));

    }

}