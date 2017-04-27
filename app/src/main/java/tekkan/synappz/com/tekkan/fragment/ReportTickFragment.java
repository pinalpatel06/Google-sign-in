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
public class ReportTickFragment extends Fragment {


    public ReportTickFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
              return inflater.inflate(R.layout.fragment_report_tick_step1, container, false);
    }

}
