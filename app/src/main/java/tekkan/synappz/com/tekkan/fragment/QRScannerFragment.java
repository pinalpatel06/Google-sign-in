package tekkan.synappz.com.tekkan.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.BarcodeCaptureActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class QRScannerFragment extends Fragment{
    private static final String
            TAG = QRScannerFragment.class.getSimpleName();

    @BindView(R.id.btn_send)
    Button mScanBtn;
    @BindView(R.id.iv_code)
    ImageView mCodeIV;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile_setup, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_qrscanner, container, false);
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        return v;
    }

    @OnClick(R.id.btn_send)
    public void scanQRCode() {
        Intent intent = new Intent(getActivity(), BarcodeCaptureActivity.class);
        startActivity(intent);
    }
}
