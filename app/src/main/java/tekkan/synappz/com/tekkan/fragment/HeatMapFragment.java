package tekkan.synappz.com.tekkan.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.SeekBar;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.FilterOptionActivity;
import tekkan.synappz.com.tekkan.heapmap.heatmaps.Gradient;
import tekkan.synappz.com.tekkan.heapmap.heatmaps.HeatmapTileProvider;
import tekkan.synappz.com.tekkan.utils.Constants;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Tejas Sherdiwala on 4/20/2017.
 * &copy; Knoxpo
 */

public class HeatMapFragment extends Fragment implements SeekBar.OnSeekBarChangeListener,
        GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, LocationListener, TabLayout.OnTabSelectedListener {

    private static final String TAG = HeatMapFragment.class.getSimpleName();
    SimpleDateFormat DATE_FORMAT
            = new SimpleDateFormat("MM dd yyyy", Locale.ENGLISH);

    private static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = {
            1.0f
    };

    private static final int[] ALT_HEATMAP_GRADIENT_COLORS_DISEASE1 = {
            Color.rgb(10, 125, 224)
    },
            ALT_HEATMAP_GRADIENT_COLORS_DISEASE2 = {
                    Color.rgb(13, 201, 194)},
            ALT_HEATMAP_GRADIENT_COLORS_DISEASE3 = {
                    Color.rgb(224, 10, 150)
            },
            ALT_HEATMAP_GRADIENT_COLORS_DISEASE4 = {
                    Color.rgb(113, 29, 213)
            };

    private static final int
            LOCATION_INTERVAL = 10000,
            ANIMATION_DURATION = 400,
            REQUEST_LOCATION = 0,
            REQUEST_GPS = 1,
            REQUEST_FILTER_OPTION = 2;

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

    private HashMap<Integer, Boolean> mList;

    private TileOverlay mTileOverlay;

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
        readItems();
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
    }

    private void initMap() {
        mMapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map));
        mMapFragment.getMapAsync(this);
    }


    private void updateUI() {
        Calendar c = Calendar.getInstance();
        mSeekValueTV.setText(DATE_FORMAT.format(c.getTime()));
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
        Log.d(TAG, weeksInYear + " ");
        return weeksInYear - currentWeekNo + nextYearWeek;
    }

    @OnClick(R.id.tv_title_filter)
    public void showBottomSheet() {
        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Calendar c = Calendar.getInstance();
        progress = mProgressMaxValue - progress;
        c.add(Calendar.DAY_OF_WEEK_IN_MONTH, -progress);
        mSeekValueTV.setText(DATE_FORMAT.format(c.getTime()));
        Rect rect = mSeekBar.getThumb().getBounds();
        int x = rect.left - mSeekValueTV.getWidth() / 5;
        if (x >= mScreenMinValue && x < (mScreenMaxValue - (mSeekValueTV.getWidth()))) {
            mSeekValueTV.setX((x));
        }
        mSeekValueTV.invalidate();
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
                // LatLng lastLocationLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                LatLng lastLocationLatLng = new LatLng(51.802750948, 5.271852985);
                CameraPosition myPosition = new CameraPosition.Builder().target(lastLocationLatLng).zoom(7).bearing(0).tilt(0).build();
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
                break;
            case REQUEST_FILTER_OPTION:
                switch (resultCode) {
                    case RESULT_OK:
                        selectHeatMap();
                        break;
                }

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
        mMap.clear();
        if (mLatLngListDisease1 != null) {
            drawHeatMap(mLatLngListDisease1, ALT_HEATMAP_GRADIENT_COLORS_DISEASE1);
        }
        if (mLatLngListDisease2 != null) {
            drawHeatMap(mLatLngListDisease2, ALT_HEATMAP_GRADIENT_COLORS_DISEASE2);
        }
        if (mLatLngListDisease3 != null) {
            drawHeatMap(mLatLngListDisease3, ALT_HEATMAP_GRADIENT_COLORS_DISEASE3);
        }
        if (mLatLngListDisease4 != null) {
            drawHeatMap(mLatLngListDisease4, ALT_HEATMAP_GRADIENT_COLORS_DISEASE4);
        }
    }

    private void drawHeatMap(ArrayList<LatLng> list, int[] colors) {
        HeatmapTileProvider provider = null;
        Gradient gradient = new Gradient(colors,
                ALT_HEATMAP_GRADIENT_START_POINTS);


        provider = new HeatmapTileProvider.Builder().data(list).build();
        mTileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));

        provider.setGradient(gradient);
        provider.setOpacity(1.0f);
        mTileOverlay.clearTileCache();
    }

    private void selectHeatMap() {
        mMap.clear();
        mTileOverlay.clearTileCache();
        for (int i = 0; i < Constants.DiseaseList.values().length; i++) {
            switch (i) {
                case 0:
                    if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(String.valueOf(i), false)) {
                        drawHeatMap(mLatLngListDisease1, ALT_HEATMAP_GRADIENT_COLORS_DISEASE1);
                    }
                    break;
                case 1:
                    if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(String.valueOf(i), false)) {
                        drawHeatMap(mLatLngListDisease2, ALT_HEATMAP_GRADIENT_COLORS_DISEASE2);
                    }
                    break;
                case 2:
                    if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(String.valueOf(i), false)) {
                        drawHeatMap(mLatLngListDisease3, ALT_HEATMAP_GRADIENT_COLORS_DISEASE3);
                    }
                    break;
                case 3:
                    if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(String.valueOf(i), false)) {
                        drawHeatMap(mLatLngListDisease4, ALT_HEATMAP_GRADIENT_COLORS_DISEASE4);
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
