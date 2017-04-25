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
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;

/**
 * Created by Tejas Sherdiwala on 4/25/2017.
 * &copy; Knoxpo
 */

public class FragmentAnimalTipsDetails extends Fragment {
    private static final String
            TAG = FragmentAnimalTipsDetails.class.getSimpleName(),
            ARGS_ANIMAL_TYPE = TAG + ".ARGS_ANIMAL_TYPE";

    @BindView(R.id.iv_tips_image)
    ImageView mTipsIV;
    private AnimalTipsCallback mCallback;

    public static FragmentAnimalTipsDetails newInstance(String animalType) {
        Bundle args = new Bundle();
        args.putString(ARGS_ANIMAL_TYPE , animalType);
        FragmentAnimalTipsDetails fragment = new FragmentAnimalTipsDetails();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((OnderzoekCallback) getActivity()).onChildFragmentDisplayed("Title Tips");
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
        View v = inflater.inflate(R.layout.fragment_animal_tips_details,container,false);
        ButterKnife.bind(this,v);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        updateUI();
        setHasOptionsMenu(true);
        return v;
    }

    private void updateUI(){
        mCallback.setTabLayoutVisibility(false);
        String animalType = getArguments().getString(ARGS_ANIMAL_TYPE);
        if("KAT".equals(animalType)){
            mTipsIV.setVisibility(View.GONE);
        }
    }
}
