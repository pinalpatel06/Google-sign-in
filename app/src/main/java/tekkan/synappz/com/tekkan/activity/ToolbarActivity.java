package tekkan.synappz.com.tekkan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import tekkan.synappz.com.tekkan.R;

/**
 * Created by Tejas Sherdiwala on 4/19/2017.
 * &copy; Knoxpo
 */

public abstract class ToolbarActivity extends SingleFragmentActivity {

    private android.support.v7.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setSupportActionBar(mToolbar);
    }

    private void init(){
        mToolbar = (Toolbar)findViewById(getToolbarId());
    }

    protected int getToolbarId(){
        return R.id.toolbar;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_toolbar;
    }

    @Override
    public void setTitle(CharSequence title) {
        mToolbar.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        mToolbar.setTitle(titleId);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
