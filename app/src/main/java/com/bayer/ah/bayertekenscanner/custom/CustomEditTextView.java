package com.bayer.ah.bayertekenscanner.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by Tejas Sherdiwala on 4/21/2017.
 * &copy; Knoxpo
 */

public class CustomEditTextView extends AppCompatEditText {

    private static final int DEFAULT_FONT_FACE = TypeFaceHelper.OPENSANS_REGULAR;

    public CustomEditTextView(Context context) {
        super(context);
        setTypeface(TypeFaceHelper.getTypeFaceFromAttr(context, null, DEFAULT_FONT_FACE));
    }

    public CustomEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(TypeFaceHelper.getTypeFaceFromAttr(context, attrs, DEFAULT_FONT_FACE));
    }

    public CustomEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(TypeFaceHelper.getTypeFaceFromAttr(context, attrs, DEFAULT_FONT_FACE));
    }
}
