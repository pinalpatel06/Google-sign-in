package tekkan.synappz.com.tekkan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.fragment.EditPetFragment;
import tekkan.synappz.com.tekkan.model.Pet;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class EditPetActivity extends ToolbarActivity {

    private static final String TAG = EditPetActivity.class.getSimpleName();
    public static final String EXTRA_PET = TAG + ".EXTRA_PET";

    @Override
    protected Fragment getFragment() {
        if (getIntent().hasExtra(EXTRA_PET)) {
            return EditPetFragment.newInstance((Pet)getIntent().getParcelableExtra(EXTRA_PET));
        } else {
            return new EditPetFragment();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.pet_profile_app_title));
    }


}

