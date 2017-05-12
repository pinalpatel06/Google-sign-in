package tekkan.synappz.com.tekkan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.fragment.ViewPetFragment;
import tekkan.synappz.com.tekkan.model.Pet;

/**
 * Created by Admin on 04/05/17.
 */

public class ViewPetActivity extends ToolbarActivity {

    private static final String TAG = ViewPetActivity.class.getSimpleName();
    public static final String EXTRA_PET_DATA = TAG + ".EXTRA_PET_DATA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.pet_profile_app_title));

    }

    @Override
    protected Fragment getFragment() {
        return ViewPetFragment.newInstance((Pet)getIntent().getParcelableExtra(EXTRA_PET_DATA));
    }
}
