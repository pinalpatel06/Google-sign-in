package tekkan.synappz.com.tekkan.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Tejas Sherdiwala on 5/11/2017.
 * &copy; Knoxpo
 */

public class ConfirmDialogFragment extends DialogFragment implements Dialog.OnClickListener{
    private static final String
            TAG = ConfirmDialogFragment.class.getSimpleName(),
            ARGS_TITLE = TAG + ".ARGS_TITLE",
            ARGS_MESSAGE = TAG + ".ARGS_MESSAGE",
            ARGS_POSITIVE_TEXT = TAG + ".ARGS_POSITIVE_TEXT",
            ARGS_NEGATIVE_TEXT = TAG + ".ARGS_NEGATIVE_TEXT";

    private static final int
            NO_TITLE = -1;

    public interface ConfirmDialogFragmentListener {
        void onPositiveClicked(DialogInterface dialog);

        void onNegativeClicked();
    }

    private ConfirmDialogFragmentListener mListener;

    public static ConfirmDialogFragment newInstance(int message) {
        return newInstance(NO_TITLE, message);
    }

    public static ConfirmDialogFragment newInstance(int title, int message) {
        return newInstance(title, message, android.R.string.ok, android.R.string.cancel);
    }

    public static ConfirmDialogFragment newInstance(int message, int positiveText, int negativeText) {
        return newInstance(NO_TITLE, message, positiveText, negativeText);
    }

    public static ConfirmDialogFragment newInstance(int title, int message, int positiveText, int negativeText) {
        Bundle args = new Bundle();
        args.putInt(ARGS_TITLE, title);
        args.putInt(ARGS_MESSAGE, message);
        args.putInt(ARGS_POSITIVE_TEXT, positiveText);
        args.putInt(ARGS_NEGATIVE_TEXT, negativeText);
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity())
                .setMessage(arguments.getInt(ARGS_MESSAGE))
                .setPositiveButton(arguments.getInt(ARGS_POSITIVE_TEXT), this)
                .setNegativeButton(arguments.getInt(ARGS_NEGATIVE_TEXT), this);

        int title = arguments.getInt(ARGS_TITLE, NO_TITLE);
        if (title != NO_TITLE) {
            builder.setTitle(title);
        }

        return builder.create();
    }

    public void setListener(ConfirmDialogFragmentListener listener) {
        mListener = listener;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == Dialog.BUTTON_POSITIVE) {
            if (mListener != null) {
                mListener.onPositiveClicked(dialog);
            }
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            if (mListener != null) {
                mListener.onNegativeClicked();
            }
        }
    }
}
