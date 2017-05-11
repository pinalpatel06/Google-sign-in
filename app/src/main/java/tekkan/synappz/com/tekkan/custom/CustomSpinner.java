package tekkan.synappz.com.tekkan.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;

/**
 * Created by Tejas Sherdiwala on 5/10/2017.
 * &copy; Knoxpo
 */

public class CustomSpinner extends AppCompatSpinner {

    public interface OnItemChosenListener {
        void onItemChosen(int position, int id);
    }

    private OnItemChosenListener mItemChosenListener;

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (mItemChosenListener != null) {
            mItemChosenListener.onItemChosen(position,getId());
        }
    }

    public void setOnItemChoosenListener(OnItemChosenListener listener) {
        mItemChosenListener = listener;
    }
}
