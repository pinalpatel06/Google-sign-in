package tekkan.synappz.com.tekkan.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tekkan.synappz.com.tekkan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationArticleDetailsFragment extends Fragment {


    public InformationArticleDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information_article_details, container, false);
    }

}
