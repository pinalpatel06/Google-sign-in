package tekkan.synappz.com.tekkan.fragment.research;

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
import tekkan.synappz.com.tekkan.custom.nestedfragments.CommonNodeInterface;

/**
 * Created by Tejas Sherdiwala on 4/25/2017.
 * &copy; Knoxpo
 */

public class FragmentAnimalTipsDetails extends Fragment implements CommonNodeInterface {
    private static final String
            TAG = FragmentAnimalTipsDetails.class.getSimpleName(),
            ARGS_ANIMAL_TYPE = TAG + ".ARGS_ANIMAL_TYPE";

    @BindView(R.id.iv_tips_image)
    ImageView mTipsIV;

    public static FragmentAnimalTipsDetails newInstance(String animalType) {
        Bundle args = new Bundle();
        args.putString(ARGS_ANIMAL_TYPE , animalType);
        FragmentAnimalTipsDetails fragment = new FragmentAnimalTipsDetails();
        fragment.setArguments(args);
        return fragment;
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
       /* String animalType = getArguments().getString(ARGS_ANIMAL_TYPE);
        if("KAT".equals(animalType)){
            mTipsIV.setVisibility(View.GONE);
        }*/
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_tips);
    }
}
