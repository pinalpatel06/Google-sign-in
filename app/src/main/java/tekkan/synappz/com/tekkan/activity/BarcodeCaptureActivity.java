package tekkan.synappz.com.tekkan.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.dialogs.ConfirmDialogFragment;
import tekkan.synappz.com.tekkan.ui.camera.CameraSource;
import tekkan.synappz.com.tekkan.ui.camera.CameraSourcePreview;
import tekkan.synappz.com.tekkan.ui.camera.GraphicOverlay;

/**
 * Created by Tejas Sherdiwala on 5/11/2017.
 * &copy; Knoxpo
 */

public class BarcodeCaptureActivity extends AppCompatActivity implements ConfirmDialogFragment.ConfirmDialogFragmentListener {

    private static final String
            TAG = BarcodeCaptureActivity.class.getSimpleName(),
            PERMISSION_DIALOG = TAG + ".PERMISSION_DIALOG";

    public static final String
            EXTRA_TEEK_BUNDLE = TAG + ".EXTRA_TEEK_BUNDLE",
            ARGS_QR_CODE = TAG + ".ARGS_QR_CODE";

    private static final int
            RC_HANDLE_GMS = 9001,
            RC_CAMERA_PERMISSION = 0;

    private static final int
            NO_DIALOG = 0,
            DIALOG_PERMISSION = 1,
            DIALOG_SETTING = 2;

    private static int mDialog = NO_DIALOG;
    private CameraSource mCameraSource;

    @BindView(R.id.preview)
    CameraSourcePreview mSourcePreview;
    @BindView(R.id.graphicOverlay)
    GraphicOverlay mGraphicOverlay;


    @Override
    public void onStart() {
        super.onStart();
        startCameraSource();
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (mDialog) {
            case DIALOG_PERMISSION:
                ConfirmDialogFragment fragment = ConfirmDialogFragment.newInstance(
                        R.string.confirm_camera_request_permission,
                        android.R.string.yes,
                        android.R.string.no);
                fragment.show(getSupportFragmentManager(), PERMISSION_DIALOG);
                fragment.setListener(this);
                break;
            case DIALOG_SETTING:
                // permission from settings
                break;
            case NO_DIALOG:
                break;
        }
        mDialog = NO_DIALOG;
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mSourcePreview != null) {
            mSourcePreview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSourcePreview != null) {
            mSourcePreview.release();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_capture);
        ButterKnife.bind(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(true, false);
        } else {
            requestPermission();
        }
    }

    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();

        // A barcode detector is created to track barcodes.  An associated multi-processor instance
        // is set to receive the barcode detection results, track the barcodes, and maintain
        // graphics for each barcode on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each barcode.
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0) {
                    Log.d(TAG, barcodes.valueAt(0).rawValue);
                    Intent intent = new Intent(BarcodeCaptureActivity.this, InvestigatePetActivity.class);
                    Bundle bundle = getIntent().getBundleExtra(EXTRA_TEEK_BUNDLE);
                    bundle.putString(ARGS_QR_CODE, barcodes.valueAt(0).rawValue);
                    intent.putExtra(InvestigatePetActivity.EXTRA_TEEK_BUNDLE, bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });


        if (!barcodeDetector.isOperational()) {
            // Note: The first time that an app using the barcode or face API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any barcodes
            // and/or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowStorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowStorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the barcode detector to detect small barcodes
        // at long distances.
        CameraSource.Builder builder = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(15.0f);

        // make sure that auto focus is an available option

        builder = builder.setFocusMode(
                autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);

        mCameraSource = builder
                .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .build();

    }

    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                this);
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mSourcePreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    public void requestPermission() {
        requestPermissions(
                new String[]{Manifest.permission.CAMERA},
                RC_CAMERA_PERMISSION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_CAMERA_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createCameraSource(true, false);
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    mDialog = DIALOG_PERMISSION;
                } else {
                    mDialog = DIALOG_SETTING;
                }
        }
    }


    @Override
    public void onPositiveClicked(DialogInterface dialog) {
        requestPermission();
    }

    @Override
    public void onNegativeClicked() {

    }
}
