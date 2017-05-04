package tekkan.synappz.com.tekkan.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.fragment.EditPetFragment;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class EditPetActivity extends ToolbarActivity {

    private static final String TAG = EditPetActivity.class.getSimpleName();

    public static final String
            ARGS_LOG_IN = TAG + ".ARGS_LOG_IN",
            ARGS_PROFILE = TAG + ".ARGS_PROFILE";

    @Override
    protected Fragment getFragment() {
        return new EditPetFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setTitle(getString(R.string.pet_profile_app_title));
    }



}

