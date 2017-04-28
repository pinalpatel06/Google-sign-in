package tekkan.synappz.com.tekkan.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.nestedfragments.CommonNodeInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailsFragment extends Fragment implements CommonNodeInterface {


    public ProductDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_product_details, container, false);
        ButterKnife.bind(this,v);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.white));
        return v;
    }

    @Override
    public String getTitle() {
        return "Advantage Hond";
    }
}
