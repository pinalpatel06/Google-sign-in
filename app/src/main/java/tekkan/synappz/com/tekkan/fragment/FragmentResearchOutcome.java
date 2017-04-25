package tekkan.synappz.com.tekkan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;

/**
 * Created by Tejas Sherdiwala on 4/25/2017.
 * &copy; Knoxpo
 */

public class FragmentResearchOutcome extends Fragment {

    private AnimalTipsCallback mCallback;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((OnderzoekCallback) getActivity()).onChildFragmentDisplayed("Uitslag onderzoek");
        mCallback = (AnimalTipsCallback) getParentFragment();
    }

    @Override
    public void onDetach() {
        ((AnimalTipsCallback) getParentFragment()).setTabLayoutVisibility(true);
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragement_research_outcome,container,false);
        ButterKnife.bind(this,v);
        mCallback.setTabLayoutVisibility(false);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        setHasOptionsMenu(true);
        return v;
    }
}
