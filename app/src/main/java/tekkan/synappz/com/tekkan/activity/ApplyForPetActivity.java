package tekkan.synappz.com.tekkan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.fragment.teekMelden.FragmentApplyPet;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class ApplyForPetActivity extends ToolbarActivity {
    @Override
    protected Fragment getFragment() {
        return new FragmentApplyPet();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setTitle(getString(R.string.investigate_app_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
