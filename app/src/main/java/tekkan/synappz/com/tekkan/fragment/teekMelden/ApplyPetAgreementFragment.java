package tekkan.synappz.com.tekkan.fragment.teekMelden;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.BarcodeCaptureActivity;
import tekkan.synappz.com.tekkan.activity.MainActivity;
import tekkan.synappz.com.tekkan.custom.network.TekenStringRequest;
import tekkan.synappz.com.tekkan.fragment.TestKitResearchList;
import tekkan.synappz.com.tekkan.utils.Constants;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class ApplyPetAgreementFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener {
    private static final String
            TAG = ApplyPetAgreementFragment.class.getSimpleName(),
            ARGS_PET_BUNDLE = TAG + ".ARGS_PET_BUNDLE",
            TAG_ALERT_DIALOG = TAG + ".TAG_ALERT_DIALOG";

    @BindView(R.id.btn_ready)
    Button mReadyBtn;

    public static ApplyPetAgreementFragment newInstance(Bundle bundle) {
        Bundle args = new Bundle();
        args.putBundle(ARGS_PET_BUNDLE, bundle);
        ApplyPetAgreementFragment fragment = new ApplyPetAgreementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_confirm_agreement, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    private static final String
            PARAM_ANIMALS_ID = "animals_id",
            PARAM_RESEARCH = "research",
            PARAM_COOR_LAT = "coor_lat",
            PARAM_COOR_LNG = "coor_lng",
            PARAM_COOR_ZOOM = "coor_zoom",
            PARAM_CODE = "code";

    @OnClick(R.id.btn_ready)
    public void closeActivity() {
        Bundle bundle = getArguments().getBundle(ARGS_PET_BUNDLE);
        TekenStringRequest request = new TekenStringRequest(
                Request.Method.POST,
                Constants.Api.getUrl(Constants.Api.FUNC_CREATE_TICK__REPORT),
                this,
                this
        );

        request.addParam(PARAM_ANIMALS_ID, String.valueOf(bundle.getInt(TestKitResearchList.ARGS_PET_ID)));
        request.addParam(PARAM_RESEARCH, String.valueOf(bundle.getString(TickReportConfirmFragment.ARGS_APPLY_RESEARCH)));
        LatLng latLng = bundle.getParcelable(TickMapFragment.ARGS_LOCATION_LATLNG);
        request.addParam(PARAM_COOR_LAT, String.valueOf(latLng.latitude));
        request.addParam(PARAM_COOR_LNG, String.valueOf(latLng.longitude));
        request.addParam(PARAM_COOR_ZOOM, String.valueOf((int) bundle.getFloat(TickMapFragment.ARGS_ZOOM_LEVEL)));
        if (bundle.getString(BarcodeCaptureActivity.ARGS_QR_CODE) != null) {
            request.addParam(PARAM_CODE, bundle.getString(BarcodeCaptureActivity.ARGS_QR_CODE));
        }

        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "Failure");
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onResponse(String response) {
        Log.d(TAG, "Success " + response);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
