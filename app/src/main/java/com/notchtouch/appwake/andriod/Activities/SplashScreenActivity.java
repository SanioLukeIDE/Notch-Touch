package com.notchtouch.appwake.andriod.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivitySplashScreenBinding;

@SuppressLint("CustomSplashScreen")
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

        isOnBoardingCompleted = Functions.getSharedPref(this, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_ONBOARDING_COMPLETE, "boolean", false);
        isSelectLanguageCompleted = Functions.getSharedPref(this, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_SELECTLANGUAGE_COMPLETE, "boolean", false);
        isTermsofservicesCompleted = Functions.getSharedPref(this, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_TERMSOFSERVICES_COMPLETE, "boolean", false);

        new Handler().postDelayed(() -> {

            if (!isOnBoardingCompleted) {
                intentClass = OnBoardingActivity.class;
            } else {
                if (!isSelectLanguageCompleted) {
                    intentClass = SelectLanguageActivity.class;
                } else {
                    if (!isTermsofservicesCompleted) {
                        intentClass = TermsOfServicesActivity.class;
                    } else {
                        boolean isAccessibilityEnabled = Settings.Secure.getInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0) == 1;
                        boolean isOverlayEnabled = Settings.canDrawOverlays(this);
//                        if (!isAccessibilityEnabled || !isOverlayEnabled) {
                        if (!isOverlayEnabled) {
                            intentClass = PermissionsActivity.class;
                        } else {
                            intentClass = HomeActivity.class;
                        }
                    }
                }
            }
            startActivity(new Intent(getApplicationContext(), intentClass));
            finish();
        }, 3000);
    }
}