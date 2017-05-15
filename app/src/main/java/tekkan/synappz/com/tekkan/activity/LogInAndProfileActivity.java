package tekkan.synappz.com.tekkan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.fragment.teekMelden.LogInAndProfileFragment;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class LogInAndProfileActivity extends ToolbarActivity {
    private static final String TAG = LogInAndProfileActivity.class.getSimpleName();

    public static final String EXTRA_TEEK_BUNDLE = TAG + ".EXTRA_TEEK_BUNDLE";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Fragment getFragment() {
        return LogInAndProfileFragment.newInstance(getIntent().getBundleExtra(EXTRA_TEEK_BUNDLE));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setTitle(getString(R.string.log_in_title));
    }
}
