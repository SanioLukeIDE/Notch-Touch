package com.notchtouch.appwake.andriod.Activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.loadLocale(this);
        Functions.lightBackgroundStatusBarDesign(this);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.settingsBackButton.setOnClickListener(v -> onBackPressed());

        setSettingsUI();

        binding.settingsHapticFeedbackLay.setOnClickListener(v -> updateHapticFeedback(!binding.settingsHapticFeedbackCheckBox.isChecked()));
        binding.settingsHapticFeedbackCheckBox.setOnCheckedChangeListener((compoundButton, b) -> updateHapticFeedback(b));

        binding.settingsLandscapeOptionLay.setOnClickListener(v -> updateLandscapeUI(!binding.settingsLandscapeOptionCheckBox.isChecked()));
        binding.settingsLandscapeOptionCheckBox.setOnCheckedChangeListener((compoundButton, b) -> updateLandscapeUI(b));

        binding.settingsTouchNotchLay.setOnClickListener(v -> updateTouchTypeUI(false));
        binding.settingsTouchBarLay.setOnClickListener(v -> updateTouchTypeUI(true));
    }

    private void setSettingsUI() {
        boolean isTouchStatusBar = Boolean.TRUE.equals(Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.IS_TOUCH_STATUS_BAR, "boolean", false));
        binding.settingsTouchNotchRadioImage.setImageTintList(ColorStateList.valueOf(
                ContextCompat.getColor(getApplicationContext(), isTouchStatusBar ? R.color.greyColor : R.color.themeColor)));
        binding.settingsTouchBarRadioImage.setImageTintList(ColorStateList.valueOf(
                ContextCompat.getColor(getApplicationContext(), isTouchStatusBar ? R.color.themeColor : R.color.greyColor)));

        binding.settingsLanguageButton.setOnClickListener(v-> startActivity(
                new Intent(getApplicationContext(), SelectLanguageActivity.class).putExtra("isUpdating", true)));

        boolean isHapticFeedbackEnabled = Boolean.TRUE.equals(Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.IS_VIBRATION_MODE_ENABLED, "boolean", false));
        binding.settingsHapticFeedbackCheckBox.setChecked(isHapticFeedbackEnabled);

        boolean isLandscapeRestrictionEnabled = Boolean.TRUE.equals(Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.IS_LANDSCAPE_RESTRICT_MODE_ENABLED, "boolean", false));
        binding.settingsLandscapeOptionCheckBox.setChecked(isLandscapeRestrictionEnabled);
    }

    private void updateTouchTypeUI(boolean isStatusBar) {
        binding.settingsTouchNotchRadioImage.setImageTintList(ColorStateList.valueOf(
                ContextCompat.getColor(getApplicationContext(), isStatusBar ? R.color.greyColor : R.color.themeColor)));
        binding.settingsTouchBarRadioImage.setImageTintList(ColorStateList.valueOf(
                ContextCompat.getColor(getApplicationContext(), isStatusBar ? R.color.themeColor : R.color.greyColor)));
        Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.IS_TOUCH_STATUS_BAR, "boolean", isStatusBar);
        Functions.updateAccessibilityService(this);
        String touch_status_msg = !isStatusBar ? getString(R.string.settings_touch_notch_msg) : getString(R.string.settings_touch_bar_msg);
        Snackbar.make(binding.getRoot(), touch_status_msg, Snackbar.LENGTH_SHORT).show();
    }

    private void updateLandscapeUI(boolean check) {
        binding.settingsLandscapeOptionCheckBox.setChecked(check);
        Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.IS_LANDSCAPE_RESTRICT_MODE_ENABLED, "boolean", check);
        String lanscape_msg = check ? getString(R.string.settings_lr_enabled) : getString(R.string.settings_lr_disabled);
        Snackbar.make(binding.getRoot(), lanscape_msg, Snackbar.LENGTH_SHORT).show();
    }

    private void updateHapticFeedback(boolean check) {
        binding.settingsHapticFeedbackCheckBox.setChecked(check);
        Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.IS_VIBRATION_MODE_ENABLED, "boolean", check);
        String haptic_msg = check ? getString(R.string.settings_vibration_enabled) : getString(R.string.settings_vibration_disabled);
        Snackbar.make(binding.getRoot(), haptic_msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Functions.ACCESSIBILITY_PERMISSION)
            Functions.updateAccessibilityService(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSettingsUI();
    }
}