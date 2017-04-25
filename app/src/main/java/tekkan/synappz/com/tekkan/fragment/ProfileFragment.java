package tekkan.synappz.com.tekkan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.adapters.AddPetAdapter;


public class ProfileFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private AddPetAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,container,false);

        init(v);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.add_new_pet_list);

       // mAdapter = new AddPetAdapter(mList);
        mAdapter = new AddPetAdapter();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private void init(View v){
        ButterKnife.bind(this,v);
    }
}
