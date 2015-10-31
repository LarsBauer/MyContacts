package de.hska.mycontacts.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.widget.EditText;

import java.util.List;

/**
 * Created by larsbauer on 31.10.15.
 */
public class Util {

    public static boolean isIntentSupported(Context ctx, Intent intent) {
        PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        return !activities.isEmpty();
    }

    public static String getStringValue(Activity ctx, int id) {
        View field = ctx.findViewById(id);
        if (field instanceof EditText) {
            EditText textField = (EditText) field;
            return textField.getText().toString();
        }
        return "";
    }

}
