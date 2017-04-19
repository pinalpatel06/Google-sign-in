package tekkan.synappz.com.tekkan.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Tejas Sherdiwala on 4/19/2017.
 * &copy; Knoxpo
 */

public class CustomFontTextView extends AppCompatTextView {

    private static final int DEFAULT_FONT_FACE = TypeFaceHelper.BERTHOLD_AKZIDENZ_GROTESK_BE_REGULAR;

    public CustomFontTextView(Context context) {
        super(context);
        setTypeface(TypeFaceHelper.getTypeFaceFromAttr(context, null, DEFAULT_FONT_FACE));
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(TypeFaceHelper.getTypeFaceFromAttr(context, attrs, DEFAULT_FONT_FACE));
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(TypeFaceHelper.getTypeFaceFromAttr(context, attrs, DEFAULT_FONT_FACE));
    }
}
