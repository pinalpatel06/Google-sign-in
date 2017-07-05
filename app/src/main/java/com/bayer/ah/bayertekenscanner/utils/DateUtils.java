package com.bayer.ah.bayertekenscanner.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tejas Sherdiwala on 12/05/17.
 */

public class DateUtils {

    private static SimpleDateFormat
            sFullDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH),
            sDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH),
            sDrugDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);


    public static Date toDate(String string) {
        try {
            return sFullDateTimeFormat.parse(string);
        } catch (ParseException e) {
            throw new UnsupportedOperationException(e.getMessage() + " :" + string);
        }
    }

    public static String toApi(Date date) {
        return sDateFormat.format(date);
    }

    public static String toDrugDate(Date date) {
        return sDrugDateFormat.format(date);
    }

    public static String toPetApi(Date date) {
        return sFullDateTimeFormat.format(date);
    }
}
