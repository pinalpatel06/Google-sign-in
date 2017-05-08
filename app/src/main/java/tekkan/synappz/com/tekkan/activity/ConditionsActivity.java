package tekkan.synappz.com.tekkan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.fragment.ProfileConditionsFragment;


/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class ConditionsActivity extends ToolbarActivity {
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
        return new ProfileConditionsFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setTitle(getString(R.string.conditions_title_text));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}