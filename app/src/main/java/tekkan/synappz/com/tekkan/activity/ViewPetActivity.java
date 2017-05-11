package tekkan.synappz.com.tekkan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.fragment.ViewPetFragment;

import static tekkan.synappz.com.tekkan.fragment.EditPetFragment.ARGS_PET_PROFILE;

/**
 * Created by Admin on 04/05/17.
 */

public class ViewPetActivity extends ToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.pet_profile_app_title));

    }


    @Override
    protected Fragment getFragment() {
        ViewPetFragment viewPetFragment = new ViewPetFragment();
        viewPetFragment.setArguments(getIntent().getBundleExtra(ARGS_PET_PROFILE));
        return viewPetFragment;
    }
}
