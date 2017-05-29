package tekkan.synappz.com.tekkan.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.fragment.EditPetFragment;
import tekkan.synappz.com.tekkan.model.Pet;

import static java.security.AccessController.getContext;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class EditPetActivity extends ToolbarActivity {

    private static final String TAG = EditPetActivity.class.getSimpleName();
    public static final String EXTRA_PET = TAG + ".EXTRA_PET";
    private EditPetFragment mEditPetFragment;

    @Override
    protected Fragment getFragment() {
        if (getIntent().hasExtra(EXTRA_PET)) {
            mEditPetFragment = EditPetFragment.newInstance((Pet)getIntent().getParcelableExtra(EXTRA_PET));
            return mEditPetFragment;
        } else {
            mEditPetFragment = new EditPetFragment();
            return mEditPetFragment;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.pet_profile_app_title));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                              Intent cameraIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                mEditPetFragment.startActivityForResult(
                        cameraIntent,
                        EditPetFragment.CAMERA_REQUEST_CODE);

            }
        }
    }

}

