package tekkan.synappz.com.tekkan.fragment.teekMelden;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.nestedfragments.CommonNodeInterface;

/**
 * Created by Tejas Sherdiwala on 4/27/2017.
 * &copy; Knoxpo
 */

public class OtherPetSelectedFragment extends Fragment implements CommonNodeInterface {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_other_pet_selected,container, false);
        ButterKnife.bind(this,v);
        v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_blurred));
        return v;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public boolean shouldDisplayHomeAsUpEnabled() {
        return true;
    }
}
