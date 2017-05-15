package tekkan.synappz.com.tekkan.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Tejas Sherdiwala on 5/9/2017.
 * &copy; Knoxpo
 */

public class AlertDialogFragment extends DialogFragment {
    private static final String
            TAG = AlertDialogFragment.class.getSimpleName(),
            ARGS_MESSAGE = TAG + ".ARGS_MESSAGE",
            ARGS_TITLE = TAG + ".ARGS_TITLE";

    private static final int
            NULL_STRING = -1;

    public static AlertDialogFragment newInstance(int title, int message) {
        Bundle args = new Bundle();
        args.putInt(ARGS_TITLE, title);
        args.putInt(ARGS_MESSAGE, message);
        AlertDialogFragment fragment = new AlertDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AlertDialogFragment newInstance(int message) {
        return newInstance(NULL_STRING, message);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity());

        int message = arguments.getInt(ARGS_MESSAGE);
        int title = arguments.getInt(ARGS_TITLE);

        if (message > 0) {
            builder.setMessage(message);
        }
        if (title > 0) {
            builder.setTitle(title);
        }
        builder.setPositiveButton(android.R.string.ok, null);
        return builder.create();
    }
}