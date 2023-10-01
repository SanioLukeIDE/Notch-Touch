package com.notchtouch.appwake.andriod.Utils;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.notchtouch.appwake.andriod.R;

import org.jetbrains.annotations.NotNull;

public class Functions {

    public static final String APP_SETTINGS_PREF_NAME= "AppDetails";
    public static final String IS_ONBOARDING_COMPLETE= "isBoardingCompleted";
    public static final String IS_TERMSOFSERVICES_COMPLETE= "isTermsofservicesCompleted";
    public static final String IS_SELECTLANGUAGE_COMPLETE= "isSelectlanguageCompleted";
    public static final String LANGUAGE_SELECTED= "selectedLanguage";

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

    public static int dpToPx(@NonNull Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
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


}
