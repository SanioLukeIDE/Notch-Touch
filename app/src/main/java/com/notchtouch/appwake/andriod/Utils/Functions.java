package com.notchtouch.appwake.andriod.Utils;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.notchtouch.appwake.andriod.Models.AppsModel;
import com.notchtouch.appwake.andriod.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Functions {

    public static final String APP_SETTINGS_PREF_NAME= "AppDetails";
    public static final String IS_ONBOARDING_COMPLETE= "isBoardingCompleted";
    public static final String IS_TERMSOFSERVICES_COMPLETE= "isTermsofservicesCompleted";
    public static final String IS_SELECTLANGUAGE_COMPLETE= "isSelectlanguageCompleted";
    public static final String LANGUAGE_SELECTED= "selectedLanguage";

    public static final String EVENT_SELECTED= "selectedEvent";
    public static final String ACTION_SELECTED= "selectedAction";
    public static final String OPTION_SELECTED= "selectedOption";
    public static final String OPEN_WEBSITE_LINK= "openWebsiteLink";
    public static final String OPEN_DIAL_NUMBER= "openDialNumber";
    public static final String BRIGHTNESS_VALUE= "brightnessValue";
    public static final String OPEN_SELECTED_APP= "openSelectedApp";

    public static final String NOTCH_HEIGHT = "notchHeight";
    public static final String NOTCH_WIDTH = "notchWidth";

    public static void darkBackgroundStatusBarDesign(@NotNull Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.black));
    }

    public static void lightBackgroundStatusBarDesign(Activity activity) {
        int flags = activity.getWindow().getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        activity.getWindow().setStatusBarColor(Color.WHITE);
        activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.white));
        activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
    }

    public static void coloredStatusBarDarkTextDesign(@NotNull Activity activity, int status_color_id, int navigation_color_id) {
        int flags = activity.getWindow().getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(status_color_id));
        activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity.getApplicationContext(), navigation_color_id));
        activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility() |
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
    }

    public static void coloredStatusBarLightTextDesign(@NotNull Activity activity, int status_color_id, int navigation_color_id) {
        activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility() &
                ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity.getApplicationContext(), navigation_color_id));
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(status_color_id));
    }

    public static <Any> void putSharedPref(@NotNull Context context, String prefs_name, String prefs_objname, @NotNull String type, Any set_val) {

        SharedPreferences.Editor pref_edit = context.getSharedPreferences(prefs_name, MODE_PRIVATE).edit();
        switch (type) {
            case "string":
                pref_edit.putString(prefs_objname, (String) set_val);
                break;

            case "int":
                pref_edit.putInt(prefs_objname, (Integer) set_val);
                break;

            case "boolean":
                pref_edit.putBoolean(prefs_objname, (Boolean) set_val);
                break;

            case "float":
                pref_edit.putFloat(prefs_objname, (Float) set_val);
                break;

            case "long":
                pref_edit.putLong(prefs_objname, (Long) set_val);
                break;
        }
        pref_edit.apply();
    }

    public static void clearSharedPref(Context context, String pref_name) {
        SharedPreferences pref = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        pref.edit().clear().commit();
    }

    public static <Any> Any getSharedPref(@NotNull Context context, String prefs_name, String prefs_objname, @NotNull String type, Any default_val) {

        SharedPreferences pref = context.getSharedPreferences(prefs_name, MODE_PRIVATE);
        switch (type) {

            case "string":
                String stringval = pref.getString(prefs_objname, (String) default_val);
                return ((Any) (String) stringval);

            case "int":
                int intval = pref.getInt(prefs_objname, (Integer) default_val);
                return ((Any) (Integer) intval);

            case "boolean":
                Boolean boolval = pref.getBoolean(prefs_objname, (Boolean) default_val);
                return ((Any) (Boolean) boolval);

            case "float":
                Float floatval = pref.getFloat(prefs_objname, (Float) default_val);
                return ((Any) (Float) floatval);

            case "long":
                Long longval = pref.getLong(prefs_objname, (Long) default_val);
                return ((Any) (Long) longval);

            default:
                return null;
        }
    }

    public static boolean isNonNull(String val) {
        return val == null || val.trim().equalsIgnoreCase("") || val.trim().equalsIgnoreCase("null") || val.trim().equals("") || val.trim().equals("null");
    }

    public static Dialog createDialogBox(Activity activity, int view_id, boolean isclose) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(view_id);
        dialog.setCanceledOnTouchOutside(isclose);
        dialog.setCancelable(isclose);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        return dialog;
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }

        return false;
    }

    private static float brightnessNormalize(float x, float inMin, float inMax, float outMin, float outMax) {
        float outRange = outMax - outMin;
        float inRange  = inMax - inMin;
        return (x - inMin) *outRange / inRange + outMin;
    }

    public static ArrayList<AppsModel> getAppsData(Context context) {
        ArrayList<AppsModel> appsModels = new ArrayList<>();
        @SuppressLint("QueryPermissionsNeeded") List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            ApplicationInfo appInfo = packageInfos.get(i).applicationInfo;
            String package_name = packageInfos.get(i).packageName;
            if(package_name.contains("lens")) Log.e("packages", "Packages are - "+package_name);
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(package_name);
            if (launchIntent != null) {
                String name = appInfo.loadLabel(context.getPackageManager()).toString();
                Drawable icon = appInfo.loadIcon(context.getPackageManager());
                appsModels.add(new AppsModel(name, icon, package_name));
            }
        }
        Collections.sort(appsModels, Comparator.comparing(AppsModel::getAppName, String.CASE_INSENSITIVE_ORDER));
        return appsModels;
    }

    private void openGoogleLens(@NonNull Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent lensIntent = packageManager.getLaunchIntentForPackage("com.google.ar.lens");

        if (lensIntent != null) {
            lensIntent.putExtra("launchLens", true);
            context.startActivity(lensIntent);
        } else {
            Uri playStoreUri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.ar.lens");
            Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, playStoreUri);
            context.startActivity(playStoreIntent);
        }
    }

    public static void setStatusBarHeight(Activity activity) {
        Rect notchArea = new Rect(0,0,0,0);
        DisplayCutout displayCutout = null;
        Display defaultDisplay = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (defaultDisplay == null) {
            defaultDisplay = ((DisplayManager) activity.getSystemService(Context.DISPLAY_SERVICE)).getDisplay(0);
        }
        if (defaultDisplay != null) {
            try {
                displayCutout = defaultDisplay.getCutout();
            } catch (Throwable unused) {
                if (activity.getWindow() != null && activity.getWindow().getDecorView().getRootWindowInsets() != null) {
                    displayCutout = activity.getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
                }
            }
        }
        if (displayCutout != null) {
            List<Rect> boundingRects = displayCutout.getBoundingRects();
            if (!boundingRects.isEmpty()) {
                for (Rect rect : boundingRects) {
                    notchArea = new Rect(rect.left, rect.top, rect.right, rect.bottom);
                }
            }
        }

        Log.e("notch_area", "Left : " + notchArea.left + " & Right : " + notchArea.right
                + " & Top : " + notchArea.top + " & Bottom : " + notchArea.bottom);
        int customNotchWidth = (int) pxToDp(activity.getApplicationContext(), (notchArea.right - notchArea.left));
        Functions.putSharedPref(activity, Functions.APP_SETTINGS_PREF_NAME, Functions.NOTCH_WIDTH, "int", customNotchWidth);
        int customNotchHeight = (int) pxToDp(activity.getApplicationContext(), (notchArea.bottom - notchArea.top));
        Functions.putSharedPref(activity, Functions.APP_SETTINGS_PREF_NAME, Functions.NOTCH_HEIGHT, "int", customNotchHeight);

    }

    public static float pxToDp(Context context, float px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return px / displayMetrics.density;
    }

    public static int getNotchHeight(Context context) {
        return Functions.getSharedPref(context, Functions.APP_SETTINGS_PREF_NAME, Functions.NOTCH_HEIGHT, "int", 24);
    }

    public static int getNotchWidth(Context context) {
        return Functions.getSharedPref(context, Functions.APP_SETTINGS_PREF_NAME, Functions.NOTCH_WIDTH, "int", 24);
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

}
