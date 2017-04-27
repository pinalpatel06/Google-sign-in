package tekkan.synappz.com.tekkan.fragment.teekMelden;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.nestedfragments.ContainerNodeFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.FragmentChangeCallback;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Tejas Sherdiwala on 4/26/2017.
 * &copy; Knoxpo
 */

public class FragmentTeekMap extends ContainerNodeFragment
        implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback,
        LocationListener, GoogleMap.OnMapClickListener {

    private static final String
            TAG = FragmentTeekMap.class.getSimpleName(),
            ARGS_LOCATION_TYPE = TAG + ".ARGS_LOCATION_TYPE";

    private int mLocationType = LOCATION_CURRENT;

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    private static final int
            LOCATION_CURRENT = 0,
            LOCATION_CUSTOM = 1,
            NO_BOTTOM_VIEW = 2;

    private static final int
            LOCATION_INTERVAL = 10000,
            ANIMATION_DURATION = 400,
            REQUEST_LOCATION = 0,
            REQUEST_GPS = 1;

    @BindView(R.id.lt_teek_melden_map_bottom_view)
    LinearLayout mBottomViewLT;
    @BindView(R.id.rt_teek_melden_map_bottom_view)
    RelativeLayout mCustomBottomView;
    @BindView(R.id.btn_location_close)
    Button mCloseLocationBtn;
    @BindView(R.id.btn_customize)
    Button mCustomizeLocationBtn;
    @BindView(R.id.iv_close)
    ImageView mCloseBottomViewIV;
    @BindView(R.id.fb_my_location)
    FloatingActionButton mShowMyLocationFB;
   // private int mMidlleLayoutHeight, mBottomLayoutHeight;

    public static FragmentTeekMap newInstance(int locationType) {

        Bundle args = new Bundle();
        args.putInt(ARGS_LOCATION_TYPE, locationType);
        FragmentTeekMap fragment = new FragmentTeekMap();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mLocationType = args.getInt(ARGS_LOCATION_TYPE);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onStart() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_teek_map, container, false);
        init(v);

        v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_blurred));

       /* ViewTreeObserver vto = mCustomBottomView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mCustomBottomView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mBottomLayoutHeight = mCustomBottomView.getHeight();
                mCustomBottomView.getLayoutParams().height = 0;
                mCustomBottomView.requestLayout();
            }
        });
    */

        if (savedInstanceState != null) {
            mLocationType = savedInstanceState.getInt(ARGS_LOCATION_TYPE);
        }
        updateUI();
        return v;
    }

    private void init(View v) {
        ButterKnife.bind(this, v);
        initMap();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void initMap() {
        mMapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map));
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARGS_LOCATION_TYPE, mLocationType);
    }

    private void updateUI() {
        switch (mLocationType) {
            case 0:
                mBottomViewLT.setVisibility(View.VISIBLE);
                mCustomBottomView.setVisibility(View.GONE);
                break;
            case 1:
                mBottomViewLT.setVisibility(View.GONE);
                mCustomBottomView.setVisibility(View.VISIBLE);
                break;
            case 2:
                mBottomViewLT.setVisibility(View.GONE);
                mCustomBottomView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_location_close)
    public void onCloseLocationClick() {
        setChild(new FragmentResearchToolkit());
    }

    @OnClick(R.id.btn_customize)
    public void onCustomizeLocationClick() {
        mLocationType = LOCATION_CUSTOM;
        updateUI();

       /* ValueAnimator va = null;
        ValueAnimator va1 = null;
        va = ValueAnimator.ofInt(0, mBottomLayoutHeight);
        va1 = ValueAnimator.ofInt((int)mBottomViewLT.getY(),(int)mBottomViewLT.getY() + mBottomViewLT.getHeight());*/
       /* if (mSelectedSearchSells.size() == 1) {
            //it has newly added element hence slide in
            va = ValueAnimator.ofInt(0, mSummaryHeight);
        } else if (mSelectedSearchSells.size() == 0) {
            //it has gone to 0 and hence slide out
            va = ValueAnimator.ofInt(mSummaryHeight, 0);
        }*/

        /*if (va != null) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(va);
            animatorSet.playTogether(va1);
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorSet.setDuration(2000);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    mBottomViewLT.getLayoutParams().height = value.intValue();
                    mBottomViewLT.requestLayout();
                }
            });
            va1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    mCustomBottomView.getLayoutParams().height = value.intValue();
                    mCustomBottomView.requestLayout();
                }
            });
            animatorSet.start();
        }*/
    }

    @OnClick(R.id.iv_close)
    public void closeBottomView() {
        mLocationType = NO_BOTTOM_VIEW;
        updateUI();
       /* ObjectAnimator animation = ObjectAnimator.ofFloat(mCustomBottomView,"y",mCustomBottomView.getY() + mCustomBottomView.getHeight());
        animation.setDuration(1000);
        animation.start();
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBottomViewLT.setVisibility(View.GONE);
                mCustomBottomView.setVisibility(View.GONE);
            }
        });*/
    }

    @OnClick(R.id.fb_my_location)
    public void setShowMyLocationFB() {
        checkLocationSettings();
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public FragmentChangeCallback getChangeCallback() {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkLocationSettings();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocationOnMap();
    }

    private void updateLocationOnMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermission();

        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                LatLng lastLocationLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                CameraPosition myPosition = new CameraPosition.Builder().target(lastLocationLatLng).zoom(18).bearing(0).tilt(0).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(myPosition));
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        updateLocationOnMap();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(
                                    getActivity(),
                                    REQUEST_GPS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Toast.makeText(
                                getActivity(),
                                R.string.error_cant_change_location_setting,
                                Toast.LENGTH_SHORT
                        ).show();
                        break;
                }
            }
        });
    }

    private void requestPermission() {
        requestPermissions(
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                REQUEST_LOCATION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                boolean hasGranted = true;
                for (int i = 0; i < grantResults.length; i++) {
                    hasGranted &= grantResults[i] == PackageManager.PERMISSION_GRANTED;

                    if (!hasGranted) {
                        break;
                    }
                }
                if (hasGranted) {
                    updateLocationOnMap();
                } else if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        && !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Log.d(TAG, "Show dialog to go to settings to enable location");
                } else {
                    requestPermission();
                    Log.d(TAG, "Retry to grant request");
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_GPS:
                switch (resultCode) {
                    case RESULT_OK:
                        updateLocationOnMap();
                        break;
                    case RESULT_CANCELED:
                        Log.d(TAG, "GPS not enabled");
                        break;
                }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_tick_map_marker))
                .position(latLng)
        );
    }
}
