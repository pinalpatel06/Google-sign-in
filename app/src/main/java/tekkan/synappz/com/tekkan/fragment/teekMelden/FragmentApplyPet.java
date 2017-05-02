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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.ApplyPetAgreementActivity;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class FragmentApplyPet extends Fragment {

    @BindView(R.id.btn_send_resuest)
    Button mSendRequestBtn;

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
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        return v;
    }

    @OnClick(R.id.btn_send_resuest)
    public void showAgreement(){
        Intent intent = new Intent(getActivity(), ApplyPetAgreementActivity.class);
        startActivity(intent);
    }
}
