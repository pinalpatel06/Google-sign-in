package com.bayer.ah.bayertekenscanner.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.activity.FilterOptionActivity;
import com.bayer.ah.bayertekenscanner.cluster.clustering.Cluster;
import com.bayer.ah.bayertekenscanner.cluster.clustering.ClusterManager;
import com.bayer.ah.bayertekenscanner.cluster.clustering.view.DefaultClusterRenderer;
import com.bayer.ah.bayertekenscanner.cluster.ui.IconGenerator;
import com.bayer.ah.bayertekenscanner.custom.network.TekenErrorListener;
import com.bayer.ah.bayertekenscanner.custom.network.TekenJsonArrayRequest;
import com.bayer.ah.bayertekenscanner.custom.network.TekenResponseListener;
import com.bayer.ah.bayertekenscanner.dialogs.ConfirmDialogFragment;
import com.bayer.ah.bayertekenscanner.model.LatLngItem;
import com.bayer.ah.bayertekenscanner.utils.Common;
import com.bayer.ah.bayertekenscanner.utils.Constants;
import com.bayer.ah.bayertekenscanner.utils.DateUtils;
import com.bayer.ah.bayertekenscanner.utils.VolleyHelper;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Tejas Sherdiwala on 4/20/2017.
 * &copy; Knoxpo
 */

public class HeatMapFragment extends Fragment implements SeekBar.OnSeekBarChangeListener,
        GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, LocationListener,
        TabLayout.OnTabSelectedListener, TextView.OnEditorActionListener {

    private static final String
            TAG = HeatMapFragment.class.getSimpleName(),
            DIALOG_PERMISSION = TAG + ".DIALOG_PERMISSION",
            DIALOG_SETTINGS = TAG + ".DIALOG_SETTINGS";

    SimpleDateFormat DATE_FORMAT
            = new SimpleDateFormat("MM dd yyyy", Locale.ENGLISH);

    private static final int
            LOCATION_INTERVAL = 10000,
            ANIMATION_DURATION = 400,
            REQUEST_LOCATION = 0,
            REQUEST_GPS = 1,
            REQUEST_FILTER_OPTION = 2,
            DIALOG_NONE = 3,
            DIALOG_PERMISSION_RETRY = 4,
            DIALOG_PERMISSION_SETTING = 5;

    private int mDialogToDisplay = DIALOG_NONE;

    private ClusterManager<LatLngItem> mClusterManager;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.tv_title_filter)
    TextView mBottomSheetTitle;
    @BindView(R.id.gl_bottom_sheet)
    GridLayout mBottomSheetGL;
    @BindView(R.id.seek_bar)
    SeekBar mSeekBar;
    @BindView(R.id.tv_year_left)
    TextView mCurrentYearTV;
    @BindView(R.id.tv_year_right)
    TextView mNextYearTV;
    @BindView(R.id.tv_seekbar_value)
    TextView mSeekValueTV;
    @BindView(R.id.tv_month_0)
    TextView mMonth0TV;
    @BindView(R.id.tv_month_1)
    TextView mMonth1TV;
    @BindView(R.id.tv_month_2)
    TextView mMonth2TV;
    @BindView(R.id.tv_month_3)
    TextView mMonth3TV;
    @BindView(R.id.tv_month_4)
    TextView mMonth4TV;
    @BindView(R.id.tv_month_5)
    TextView mMonth5TV;
    @BindView(R.id.tv_month_6)
    TextView mMonth6TV;
    @BindView(R.id.tv_month_7)
    TextView mMonth7TV;
    @BindView(R.id.tv_month_8)
    TextView mMonth8TV;
    @BindView(R.id.tv_month_9)
    TextView mMonth9TV;
    @BindView(R.id.tv_month_10)
    TextView mMonth10TV;
    @BindView(R.id.tv_month_11)
    TextView mMonth11TV;
    @BindView(R.id.tv_month_12)
    TextView mMonth12TV;
    @BindView(R.id.et_search_bar)
    EditText mSearchBarET;
    @BindView(R.id.iv_close)
    ImageView mCloseIV;

    private String[] mMonthName;
    private BottomSheetBehavior mBottomSheetBehavior;
    private int mScreenMinValue, mScreenMaxValue;
    private int mProgressMaxValue;

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    private ArrayList<LatLng>
            mLatLngListDisease1,
            mLatLngListDisease2,
            mLatLngListDisease3,
            mLatLngListDisease4;

    private static final int REQUEST_TICKS = 0;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_teken_scanner, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                Intent intent = new Intent(getActivity(), FilterOptionActivity.class);
                startActivityForResult(intent, REQUEST_FILTER_OPTION);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_teken_scanner, container, false);
        init(v);
        initMap();
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetGL);
        mSeekBar.setOnSeekBarChangeListener(this);
        ViewTreeObserver vto = mBottomSheetTitle.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBottomSheetTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mBottomSheetBehavior.setPeekHeight(mBottomSheetTitle.getHeight());
                mScreenMaxValue = mBottomSheetTitle.getWidth();
                mScreenMinValue = (int) mBottomSheetTitle.getX();
            }
        });
        updateUI();
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onStart() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        switch (mDialogToDisplay) {
            case DIALOG_NONE:
                break;
            case DIALOG_PERMISSION_RETRY:
                ConfirmDialogFragment fragment = ConfirmDialogFragment.newInstance(
                        R.string.location_permission_retry,
                        android.R.string.yes,
                        android.R.string.no);
                fragment.show(getFragmentManager(), DIALOG_PERMISSION);
                fragment.setTargetFragment(this, DIALOG_PERMISSION_RETRY);
                break;
            case DIALOG_PERMISSION_SETTING:
                ConfirmDialogFragment settingDialog = ConfirmDialogFragment.newInstance(
                        R.string.setting_dialog,
                        android.R.string.yes,
                        android.R.string.no
                );
                settingDialog.show(getFragmentManager(), DIALOG_PERMISSION);
                settingDialog.setTargetFragment(this, DIALOG_PERMISSION_SETTING);
        }
        mDialogToDisplay = DIALOG_NONE;
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    private void init(View v) {
        ButterKnife.bind(this, v);
        mProgressMaxValue = calcNoOfWeek();
        mSeekBar.setMax(mProgressMaxValue);
        mSeekBar.setProgress(mProgressMaxValue);
        mMonthName = getResources().getStringArray(R.array.month_name);
        mTabLayout.addOnTabSelectedListener(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mSearchBarET.setOnEditorActionListener(this);
    }

    private void initMap() {
        mMapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map));
        mMapFragment.getMapAsync(this);
    }


    private void updateUI() {
        Calendar c = Calendar.getInstance();
        mSeekValueTV.setText(DateUtils.toApi(c.getTime()));
        mCurrentYearTV.setText(String.valueOf(c.get(Calendar.YEAR)));
        mMonth12TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, -1);
        mMonth11TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, -1);
        mMonth10TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, -1);
        mMonth9TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, -1);
        mMonth8TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, -1);
        mMonth7TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, -1);
        mMonth6TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, -1);
        mMonth5TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, -1);
        mMonth4TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, -1);
        mMonth3TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, -1);
        mMonth2TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, -1);
        mMonth1TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, -1);
        mMonth0TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        mNextYearTV.setText(String.valueOf(c.get(Calendar.YEAR)));
    }

    private int calcNoOfWeek() {
        Calendar cal = Calendar.getInstance();

        int weeksInYear = cal.getActualMaximum(Calendar.WEEK_OF_YEAR);
        int currentWeekNo = cal.get(Calendar.WEEK_OF_YEAR);
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
        int nextYearWeek = cal.get(Calendar.WEEK_OF_YEAR);
        return weeksInYear - currentWeekNo + nextYearWeek;
    }

    @OnClick({R.id.tv_title_filter,R.id.iv_close})
    public void showBottomSheet(View v) {
        switch (v.getId()){
            case R.id.tv_title_filter:
                if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.iv_close:
                mSearchBarET.setText("");
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Calendar c = Calendar.getInstance();
        progress = mProgressMaxValue - progress;
        c.add(Calendar.DAY_OF_WEEK_IN_MONTH, -progress);
        mSeekValueTV.setText(DateUtils.toApi(c.getTime()));
        Rect rect = mSeekBar.getThumb().getBounds();
        int x = rect.left - mSeekValueTV.getWidth() / 5;
        if (x >= mScreenMinValue && x < (mScreenMaxValue - (mSeekValueTV.getWidth()))) {
            mSeekValueTV.setX((x));
        }
        mSeekValueTV.invalidate();
        updateHeatMap();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
        drawHeatMapForAllDisease();


        mClusterManager = new ClusterManager<LatLngItem>(getActivity(), mMap);
        mClusterManager.setRenderer(new ClusterRenderer());
        mMap.setOnCameraIdleListener(mClusterManager);

        readItems();
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
                //LatLng lastLocationLatLng = new LatLng(51.802750948, 5.271852985);
                CameraPosition myPosition = new CameraPosition.Builder().target(lastLocationLatLng).zoom(7).bearing(0).tilt(0).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                // getTicks();
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
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                        && shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    mDialogToDisplay = DIALOG_PERMISSION_RETRY;
                } else {
                    mDialogToDisplay = DIALOG_PERMISSION_SETTING;
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
                break;
            case REQUEST_FILTER_OPTION:
                if (resultCode == Activity.RESULT_OK) {
                    selectHeatMap();
                }
                break;
            case DIALOG_PERMISSION_RETRY:
                if (resultCode == Activity.RESULT_OK) {
                    requestPermission();
                }
                break;
            case DIALOG_PERMISSION_SETTING:
                if (resultCode == Activity.RESULT_OK) {
                    Common.openAppDetails(getActivity());
                }
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        switch (tab.getPosition()) {
            case 0:
                drawHeatMapForAllDisease();
                break;
            case 1:
                selectHeatMap();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    private void drawHeatMapForAllDisease() {
        if (mMap != null) {
            mMap.clear();
        }
        if (mLatLngListDisease1 != null && mLatLngListDisease1.size() > 0) {
            drawHeatMap(mLatLngListDisease1);
        }
        if (mLatLngListDisease2 != null && mLatLngListDisease2.size() > 0) {
            drawHeatMap(mLatLngListDisease2);
        }
        if (mLatLngListDisease3 != null && mLatLngListDisease3.size() > 0) {
            drawHeatMap(mLatLngListDisease3);
        }
        if (mLatLngListDisease4 != null && mLatLngListDisease4.size() > 0) {
            drawHeatMap(mLatLngListDisease4);
        }
    }

    private void drawHeatMap(ArrayList<LatLng> list) {
       /* HeatmapTileProvider provider = null;
        Gradient gradient = new Gradient(colors,
                ALT_HEATMAP_GRADIENT_START_POINTS);


        provider = new HeatmapTileProvider.Builder().data(list).build();
        mTileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));

        provider.setGradient(gradient);
        mTileOverlay.clearTileCache();
        provider.setOpacity(1.0f);
        mTileOverlay.clearTileCache();
        provider.setRadius(6);
        mTileOverlay.clearTileCache();*/

        /*for (int i = 0; i < list.size(); i++) {
            drawMarker(list.get(i));
        }*/
    }

    private void drawMarker(LatLng latLng) {

        float zoomLevel = mMap.getCameraPosition().zoom;
        float contentSize = (float) Math.floor(Math.min(20, Math.max(8, zoomLevel)));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        contentSize = contentSize * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.heatmap_marker);

        Bitmap b = Bitmap.createBitmap((int) contentSize, (int) contentSize, Bitmap.Config.ARGB_8888);
        drawable.setBounds(0, 0, b.getHeight(), b.getWidth());
        drawable.draw(new Canvas(b));

        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        if (bounds.contains(latLng)) {
            mMap.addMarker(new MarkerOptions().position(latLng))
                    .setIcon(BitmapDescriptorFactory.fromBitmap(b));
        }

    }

    private void updateHeatMap() {
        drawHeatMapForAllDisease();
    }

    private void selectHeatMap() {
        mMap.clear();
        for (int i = 0; i < Constants.DiseaseList.values().length; i++) {
            switch (i) {
                case 0:
                    if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(String.valueOf(i), false)) {
                        drawHeatMap(mLatLngListDisease1);
                    }
                    break;
                case 1:
                    if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(String.valueOf(i), false)) {
                        drawHeatMap(mLatLngListDisease2);
                    }
                    break;
                case 2:
                    if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(String.valueOf(i), false)) {
                        drawHeatMap(mLatLngListDisease3);
                    }
                    break;
                case 3:
                    if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(String.valueOf(i), false)) {
                        drawHeatMap(mLatLngListDisease4);
                    }
                    break;
            }
        }
    }

    private void readItems() {

        InputStream inputStream = getResources().openRawResource(R.raw.dummy_ticks);
        String jsonString = new Scanner(inputStream).useDelimiter("\\A").next();

        final String JSON_DISEASE = "disease",
                JSON_COOR_LAT = "coor_lat",
                JSON_COOR_LNG = "coor_lng";

        mLatLngListDisease1 = new ArrayList<>();
        mLatLngListDisease2 = new ArrayList<>();
        mLatLngListDisease3 = new ArrayList<>();
        mLatLngListDisease4 = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int disease = jsonObject.optInt(JSON_DISEASE);
                double lat = jsonObject.optDouble(JSON_COOR_LAT);
                double lng = jsonObject.optDouble(JSON_COOR_LNG);
                switch (disease) {
                    case 0:
                        mLatLngListDisease1.add(new LatLng(lat, lng));
                        break;
                    case 1:
                        mLatLngListDisease2.add(new LatLng(lat, lng));
                        break;
                    case 2:
                        mLatLngListDisease3.add(new LatLng(lat, lng));
                        break;
                    case 3:
                        mLatLngListDisease4.add(new LatLng(lat, lng));
                        break;
                }
                mClusterManager.addItem(new LatLngItem(new LatLng(jsonObject.optDouble(JSON_COOR_LAT), jsonObject.optDouble(JSON_COOR_LNG))));
            }
            if (mMap != null) {
                drawHeatMapForAllDisease();
                mClusterManager.cluster();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String addressToSearch;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            addressToSearch = v.getText().toString();
            if (!addressToSearch.isEmpty()) {
                getCoordinateFromAddress(addressToSearch);
            }
            return true;
        }
        return false;
    }

    private void getCoordinateFromAddress(final String address) {

        Geocoder coder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = coder.getFromLocationName(address, 1);
            centerMap(address, new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void centerMap(String address, LatLng latLng) {
        if (address != null && latLng != null) {
            CameraPosition myPosition = new CameraPosition.Builder().target(latLng).zoom(10).bearing(0).tilt(0).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(myPosition));
        }
    }

    private void getTicks() {
        String PARAM_COOR_LAT = "coor_lat",
                PARAM_COOR_LNG = "coor_lng",
                PARAM_COOR_RADIUS = "coor_radius",
                PARAM_START_DATE = "startdate",
                PARAM_END_DATE = "enddate";

        TekenJsonArrayRequest request = new TekenJsonArrayRequest(
                Request.Method.POST,
                Constants.Api.getUrl(Constants.Api.FUNC_GET_TICK),
                new TekenResponseListener<JSONArray>() {
                    @Override
                    public void onResponse(int requestCode, JSONArray response) {
                        if (response != null) {
                            Log.d(TAG, response.toString());
                            parseJsonArray(response);
                        }
                    }
                },
                new TekenErrorListener() {
                    @Override
                    public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {
                        Log.d(TAG, status + " " + error);
                        if(status < 0){
                            Common.shoToast(getActivity(),R.string.no_connectivity);
                        }
                    }
                },
                REQUEST_TICKS
        );

        //temporary lat & lng data
       /* request.addParam(PARAM_COOR_LAT, "52.5");
        request.addParam(PARAM_COOR_LNG, "52.5");
        request.addParam(PARAM_COOR_RADIUS, "6350");
        request.addParam(PARAM_START_DATE, "1970-01-01");
        request.addParam(PARAM_END_DATE, "2018-12-01");*/

        request.addParam(PARAM_COOR_LAT, String.valueOf(mLastLocation.getLatitude()));
        request.addParam(PARAM_COOR_LNG, String.valueOf(mLastLocation.getLongitude()));
        request.addParam(PARAM_COOR_RADIUS, "6350");
        request.addParam(PARAM_START_DATE, "1970-01-01");
        request.addParam(PARAM_END_DATE, "2018-12-01");

        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void parseJsonArray(JSONArray response) {
        final String JSON_DISEASE = "diseases",
                JSON_COOR_LAT = "coor_lat",
                JSON_COOR_LNG = "coor_lng",
                JSON_CODE = "code",
                JSON_TYPE = "type",
                JSON_DISTANCE = "distance";

        if (response.length() > 0) {
            mLatLngListDisease1 = new ArrayList<>();
            mLatLngListDisease2 = new ArrayList<>();
            mLatLngListDisease3 = new ArrayList<>();
            mLatLngListDisease4 = new ArrayList<>();

            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.optJSONObject(i);
                JSONArray diseaseArray = jsonObject.optJSONArray(JSON_DISEASE);
                for (int j = 0; j < diseaseArray.length(); j++) {
                    switch (diseaseArray.optInt(j)) {
                        case 1:
                            mLatLngListDisease1.add(new LatLng(jsonObject.optDouble(JSON_COOR_LAT), jsonObject.optDouble(JSON_COOR_LNG)));
                            break;
                        case 2:
                            mLatLngListDisease2.add(new LatLng(jsonObject.optDouble(JSON_COOR_LAT), jsonObject.optDouble(JSON_COOR_LNG)));
                            break;
                        case 3:
                            mLatLngListDisease3.add(new LatLng(jsonObject.optDouble(JSON_COOR_LAT), jsonObject.optDouble(JSON_COOR_LNG)));
                            break;
                        case 4:
                            mLatLngListDisease4.add(new LatLng(jsonObject.optDouble(JSON_COOR_LAT), jsonObject.optDouble(JSON_COOR_LNG)));
                            break;
                    }

                    mClusterManager.addItem(new LatLngItem(new LatLng(jsonObject.optDouble(JSON_COOR_LAT), jsonObject.optDouble(JSON_COOR_LNG))));
                }
            }

            if (mMap != null) {
                drawHeatMapForAllDisease();
                mClusterManager.cluster();
            }
        }
    }

    private class ClusterRenderer extends DefaultClusterRenderer<LatLngItem> {
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getActivity());

        public ClusterRenderer() {
            super(getActivity(), mMap, mClusterManager);
            setMinClusterSize(1);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<LatLngItem> cluster, MarkerOptions markerOptions) {
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        private int getTileSize() {
            float zoomLevel = mMap.getCameraPosition().zoom;
            int contentSize = (int) Math.floor(Math.min(20, Math.max(8, zoomLevel)));
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            contentSize = contentSize * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
            return contentSize;
        }

        @Override
        protected void onBeforeClusterItemRendered(LatLngItem item, MarkerOptions markerOptions) {
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.heatmap_marker);
            int size = getTileSize();
            Bitmap b = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            drawable.setBounds(0, 0, b.getHeight(), b.getWidth());
            drawable.draw(new Canvas(b));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
        }
    }
}
