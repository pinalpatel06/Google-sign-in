package tekkan.synappz.com.tekkan.fragment.teekMelden;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class FragmentApplyPetAgreement extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_apply_pet_agreement,container,false);
        ButterKnife.bind(this,v);
        return v;
    }
}
