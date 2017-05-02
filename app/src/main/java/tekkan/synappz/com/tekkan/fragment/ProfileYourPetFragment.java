package tekkan.synappz.com.tekkan.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileYourPetFragment extends Fragment {

    private static final String TAG = ProfileYourPetFragment.class.getSimpleName();
    public static final String
            ARGS_LOG_IN = TAG + ".ARGS_LOG_IN",
            ARGS_PROFILE = TAG + ".ARGS_PROFILE";

    private boolean mIsUpdate=false;
    private boolean mIsDone = true;


    public static ProfileYourPetFragment newInstance() {
        
        Bundle args = new Bundle();
        ProfileYourPetFragment fragment = new ProfileYourPetFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pet_profile_update, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem editItem = menu.findItem(R.id.action_edit);
        MenuItem doneItem = menu.findItem(R.id.action_done);
        editItem.setVisible(mIsUpdate);
        doneItem.setVisible(mIsDone);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_done:
                mIsUpdate = true;
                mIsDone = false;
                getActivity().invalidateOptionsMenu();
               // getActivity().finish();
                return true;
            case R.id.action_edit:
                mIsDone = true;
                mIsUpdate = false;
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_your_pet, container, false);
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        return v;
    }

}
