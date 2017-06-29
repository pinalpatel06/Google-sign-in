package com.bayer.ah.bayertekenscanner.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Tejas Sherdiwala on 5/9/2017.
 * &copy; Knoxpo
 */

public class ProgressDialogFragment extends DialogFragment {
    private static final String
            TAG = ProgressDialogFragment.class.getSimpleName(),
            ARGS_MESSAGE = TAG + ".ARGS_MESSAGE";

    public static ProgressDialogFragment newInstance(String progressMsg) {

        Bundle args = new Bundle();
        args.putString(ARGS_MESSAGE , progressMsg );
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();

        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage(arguments.getString(ARGS_MESSAGE));
        dialog.setCancelable(false);
        setCancelable(false);
        return dialog;
    }

}
