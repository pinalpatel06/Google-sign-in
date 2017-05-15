package tekkan.synappz.com.tekkan.fragment.teekMelden;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.ApplyPetAgreementActivity;
import tekkan.synappz.com.tekkan.custom.CircleNetworkImageView;
import tekkan.synappz.com.tekkan.fragment.TestKitResearchList;
import tekkan.synappz.com.tekkan.model.Pet;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class ApplyPetFragment extends Fragment {
    private static final String
            TAG = ApplyPetFragment.class.getSimpleName(),
            ARGS_PET_BUNDLE = TAG + ".ARGS_PET_BUNDLE";

    @BindView(R.id.btn_send_request)
    Button mSendRequestBtn; @BindView(R.id.tv_pet_name)
    TextView mPetNameTV;
    @BindView(R.id.iv_pet_image)
    CircleNetworkImageView mPetIV;

    private Pet mPet;

    public static ApplyPetFragment newInstance(Bundle bundle) {

        Bundle args = new Bundle();
        args.putBundle(ARGS_PET_BUNDLE, bundle);
        ApplyPetFragment fragment = new ApplyPetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
        View v = inflater.inflate(R.layout.fragment_apply_pet, container, false);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        init(v);
        setHasOptionsMenu(true);
        updateUI();
        return v;
    }

    private void init(View v){
        ButterKnife.bind(this, v);
        Bundle bundle = getArguments().getBundle(ARGS_PET_BUNDLE);
        if (bundle != null) {
            mPet = bundle.getParcelable(TestKitResearchList.ARGS_PET_DATA);
        }
    }

    private void updateUI(){
        mPetNameTV.setText(mPet.getName());
        mPetIV.setDefaultImageResId(R.drawable.ic_splash_pets);
        if(mPet.getPhoto() != null) {
            mPetIV.setImageUrl(mPet.getPhoto(), VolleyHelper.getInstance(getActivity()).getImageLoader());
        }
    }

    @OnClick(R.id.btn_send_request)
    public void showAgreement() {
        Intent intent = new Intent(getActivity(), ApplyPetAgreementActivity.class);
        intent.putExtra(ApplyPetAgreementActivity.EXTRA_PET_BUNDLE, getArguments().getBundle(ARGS_PET_BUNDLE));
        startActivity(intent);
    }
}
