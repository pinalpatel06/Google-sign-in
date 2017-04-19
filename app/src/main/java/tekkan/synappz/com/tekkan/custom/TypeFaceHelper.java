package tekkan.synappz.com.tekkan.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.util.Hashtable;
import java.util.Locale;

import tekkan.synappz.com.tekkan.R;

/**
 * Created by Tejas Sherdiwala on 4/19/2017.
 * &copy; Knoxpo
 */

public class TypeFaceHelper {

    private static Hashtable<String, Typeface> sFontCache = new Hashtable<>();

    public static Typeface get(String name, Context context) {
        Typeface tf = sFontCache.get(name);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            } catch (Exception e) {
                return null;
            }
            sFontCache.put(name, tf);
        }
        return tf;
    }


    public static final int
            BERTHOLD_AKZIDENZ_GROTESK_BE_REGULAR = 0,
            DIMBO_REGULAR = 1;

    public enum CustomTypeFace {
        _0("fonts/Berthold Akzidenz Grotesk BE Regular.otf"),
        _1("fonts/Dimbo Regular.ttf");

        private final String mFileName;

        private Hashtable<String, Typeface> mFont = new Hashtable<String, Typeface>();

        CustomTypeFace(String fileName) {
            this.mFileName = fileName;
        }

        static CustomTypeFace fromString(String fontName) {
            return CustomTypeFace.valueOf(fontName.toUpperCase(Locale.US));
        }

        public Typeface asTypeface(Context context) {
            return TypeFaceHelper.get(mFileName, context);
        }

        public static CustomTypeFace fromInt(int fontName) {
            return CustomTypeFace.valueOf("_"+fontName);
        }
    }

    public static Typeface getTypeFaceFromAttr(Context context, AttributeSet attrs, int defaultFont) {
        int fontName = defaultFont;
        if (attrs != null) {

            int[] requireAttributeValues = new int[]{R.attr.customfont};

            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    requireAttributeValues,
                    0,
                    0
            );
            //0 = index
            fontName = a.getInt(0, defaultFont);

            a.recycle();
        }

        return TypeFaceHelper.CustomTypeFace.fromInt(fontName).asTypeface(context);
    }
}
