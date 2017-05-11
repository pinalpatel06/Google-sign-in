package tekkan.synappz.com.tekkan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.fragment.EditPetFragment;
import tekkan.synappz.com.tekkan.fragment.ViewPetFragment;

import static tekkan.synappz.com.tekkan.fragment.EditPetFragment.ARGS_PET_PROFILE;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class EditPetActivity extends ToolbarActivity {

    private static final String TAG = EditPetActivity.class.getSimpleName();
    Bundle mArgs;

    @Override
    protected Fragment getFragment() {
        EditPetFragment editPetFragment = new EditPetFragment();
        editPetFragment.setArguments(getIntent().getBundleExtra(ARGS_PET_PROFILE));
        return editPetFragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.pet_profile_app_title));
    }


}

