package tekkan.synappz.com.tekkan.fragment.teekMelden;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.nestedfragments.ContainerNodeFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.FragmentChangeCallback;

/**
 * Created by Tejas Sherdiwala on 4/26/2017.
 * &copy; Knoxpo
 */

public class FragmentAnimalSelection extends ContainerNodeFragment {

    private static final int
            PET_HOND = 0,
            PET_KAT = 1,
            PET_OTHERS = 2;

    @BindView(R.id.btn_pet_hond)
    Button mPetHondBtn;
    @BindView(R.id.btn_pet_kat)
    Button mPetKatBtn;
    @BindView(R.id.btn_pet_others)
    Button mPetOthersBtn;


    private FragmentChangeCallback mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (FragmentChangeCallback) getActivity();
    }

    @Override
    public void onDetach() {
        mCallback = null;
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
        View v = inflater.inflate(R.layout.fragment_pet_selection, container,false);
        ButterKnife.bind(this,v);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        return v;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public FragmentChangeCallback getChangeCallback() {
        return mCallback;
    }

    @OnClick(R.id.btn_pet_hond)
    public void onPetHondSelected(){
        setChild(FragmentPetLocationSelection.newInstance(PET_HOND));
    }

    @OnClick(R.id.btn_pet_kat)
    public void onPetKatSelected(){
        setChild(FragmentPetLocationSelection.newInstance(PET_KAT));
    }

    @OnClick(R.id.btn_pet_others)
    public void onPetOthersSelected(){
        setChild(new FragmentOtherPetSelected());
    }
}
