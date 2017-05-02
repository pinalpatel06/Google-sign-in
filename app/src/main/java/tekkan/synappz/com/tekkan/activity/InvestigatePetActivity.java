package tekkan.synappz.com.tekkan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.fragment.TestKitStep2Fragment;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class InvestigatePetActivity extends ToolbarActivity {

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
        return new TestKitStep2Fragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setTitle(getString(R.string.investigate_app_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
