package tekkan.synappz.com.tekkan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.fragment.ProfileYourPetFragment;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class PetProfileActivity extends ToolbarActivity {
    @Override
    protected Fragment getFragment() {
        return new ProfileYourPetFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setTitle(getString(R.string.pet_profile_app_title));
    }
}
