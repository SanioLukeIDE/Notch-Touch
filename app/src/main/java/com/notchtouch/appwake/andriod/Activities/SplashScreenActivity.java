package com.notchtouch.appwake.andriod.Activities;

import static com.notchtouch.appwake.andriod.SingletonClasses.LifeCycleOwner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.adsmodule.api.adsModule.models.AdsDataRequestModel;
import com.adsmodule.api.adsModule.retrofit.AdsApiHandler;
import com.adsmodule.api.adsModule.utils.AdUtils;
import com.adsmodule.api.adsModule.utils.Constants;
import com.adsmodule.api.adsModule.utils.Globals;
import com.notchtouch.appwake.andriod.Utils.AppInterfaces;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;

    boolean isOnBoardingCompleted = false;
    boolean isSelectLanguageCompleted = false;
    boolean isTermsofservicesCompleted = false;
    Class<?> intentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.lightBackgroundStatusBarDesign(this);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(Globals.isConnectingToInternet(SplashScreenActivity.this)){
            AdsApiHandler.callAdsApi(activity, Constants.BASE_URL, new AdsDataRequestModel(getPackageName(), ""), adsResponseModel -> {
                if(adsResponseModel!=null){
                    AdUtils.showAppStartAd(activity, adsResponseModel, isLoaded -> nextActivity());
                }
                else new Handler().postDelayed(this::nextActivity, 1500);
            });
        }
        else new Handler().postDelayed(this::nextActivity, 1500);
    }

    private void nextActivity() {
        Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.DEVICE_WIDTH, "int", Functions.getDeviceWidth(this));
        Functions.getDisplayCutout(this, new AppInterfaces.NotchInfoCallback() {
            @Override
            public void onNotchInfoAvailable(int notchLeft, int notchTop, int notchRight, int notchBottom) {
                Log.e("notch_value", "Notch Left: " + notchLeft + "\nNotch Top: " + notchTop +
                        "\nNotch Right: " + notchRight + "\nNotch Bottom: " + notchBottom);
                Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.NOTCH_LEFT, "int", notchLeft);
                Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.NOTCH_TOP, "int", notchTop);
                Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.NOTCH_RIGHT, "int", notchRight);
                Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.NOTCH_BOTTOM, "int", notchBottom);

                Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.NOTCH_WIDTH, "int", notchRight-notchLeft);
                Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.NOTCH_HEIGHT, "int", notchBottom-notchTop);

            }

            @Override
            public void onNoNotch() {
                Log.e("Notch", "No display cutout (notch) found on this device.");
            }
        });

        isOnBoardingCompleted = Functions.getSharedPref(this, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_ONBOARDING_COMPLETE, "boolean", false);
        isSelectLanguageCompleted = Functions.getSharedPref(this, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_SELECTLANGUAGE_COMPLETE, "boolean", false);
        isTermsofservicesCompleted = Functions.getSharedPref(this, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_TERMSOFSERVICES_COMPLETE, "boolean", false);

        if (!isTermsofservicesCompleted) {
            intentClass = TermsOfServicesActivity.class;
        }
        else {
            if (!isSelectLanguageCompleted) {
                intentClass = SelectLanguageActivity.class;
            } else {
                if (!isOnBoardingCompleted) {
                    intentClass = OnBoardingActivity.class;
                } else {
                    boolean isAccessibilityEnabled = Settings.Secure.getInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0) == 1;
                    boolean isOverlayEnabled = Settings.canDrawOverlays(this);
                    if (!isAccessibilityEnabled || !isOverlayEnabled) {
                    // if (!isOverlayEnabled) {
                        intentClass = PermissionsActivity.class;
                    } else {
                        intentClass = HomeActivity.class;
                    }
                }
            }
        }
        startActivity(new Intent(getApplicationContext(), intentClass));
        finish();
    }
}