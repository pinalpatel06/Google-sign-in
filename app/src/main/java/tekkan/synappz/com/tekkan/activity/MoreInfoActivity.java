package tekkan.synappz.com.tekkan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.fragment.MoreInfoFragment;

/**
 * Created by Tejas Sherdiwala on 4/22/2017.
 * &copy; Knoxpo
 */

public class MoreInfoActivity extends ToolbarActivity {
    @Override
    protected Fragment getFragment() {
        return new MoreInfoFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setTitle(getString(R.string.more_info_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
