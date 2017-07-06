package com.bayer.ah.bayertekenscanner.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

/**
 * Created by Tejas Sherdiwala on 6/30/2017.
 * &copy; Knoxpo
 */

public class Common {
    public static void openAppDetails(Context context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    public static void shoToast(Context context , int StringId){
        Toast.makeText(
                context,
                StringId,
                Toast.LENGTH_SHORT
        ).show();
    }

}
