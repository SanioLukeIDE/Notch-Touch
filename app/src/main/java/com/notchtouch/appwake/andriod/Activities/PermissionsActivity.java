package com.notchtouch.appwake.andriod.Activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Services.MyAccessibilityService;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivityPermissionsBinding;

public class PermissionsActivity extends AppCompatActivity {

    ActivityPermissionsBinding binding;

    private static final int OVERLAY_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.loadLocale(this);
        Functions.lightBackgroundStatusBarDesign(this);
        binding= ActivityPermissionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        updateSwitchUI();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !Functions.checkPostNotificationPermissionGranted(this)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, Functions.POST_NOTIFICATION_PERMISSION_CODE);
        }

        binding.permissionAccessibilityLay.setOnClickListener(v-> updateAccessibilitySwitch());
        binding.permissionAccessibilitySwitch.setOnCheckedChangeListener((compoundButton, b) -> updateAccessibilitySwitch());

        binding.permissionOverlayLay.setVisibility(View.GONE);

        binding.permissionOverlayLay.setOnClickListener(v-> updateOverlaySwitch());
        binding.permissionOverlaySwitch.setOnCheckedChangeListener((compoundButton, b) -> updateOverlaySwitch());

        binding.permissionProceedBtn.setOnClickListener(v-> {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        });
    }

    private void updateOverlaySwitch() {
        boolean isOverlayEnabled = Settings.canDrawOverlays(this);
        binding.permissionOverlaySwitch.setChecked(isOverlayEnabled);
        if (!isOverlayEnabled) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION);
        }
    }

    private void updateAccessibilitySwitch() {
        /*boolean isOverlayEnabled = Settings.canDrawOverlays(this);
        if (!isOverlayEnabled) {
            Snackbar.make(binding.getRoot(), "Please enable the Display Overlay Permission first !!", Snackbar.LENGTH_SHORT).show();
        } else {
            boolean accessibilityEnabled = Settings.Secure.getInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0) == 1;
            binding.permissionAccessibilitySwitch.setChecked(accessibilityEnabled);
            if (!accessibilityEnabled) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivityForResult(intent, Functions.ACCESSIBILITY_PERMISSION);
            }
        }*/
        boolean accessibilityEnabled = Settings.Secure.getInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0) == 1;
        binding.permissionAccessibilitySwitch.setChecked(accessibilityEnabled);
        if (!accessibilityEnabled) {
            Functions.showAccessibilityPermissionDialog(PermissionsActivity.this, result -> {
                if(result){
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivityForResult(intent, Functions.ACCESSIBILITY_PERMISSION);
                }
                else{
                    Functions.putSharedPref(PermissionsActivity.this, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_ONBOARDING_COMPLETE, "boolean", false);
                    startActivity(new Intent(PermissionsActivity.this, OnBoardingActivity.class));
                    finish();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Functions.ACCESSIBILITY_PERMISSION) updateSwitchUI();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Functions.POST_NOTIFICATION_PERMISSION_CODE){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !Functions.checkPostNotificationPermissionGranted(this)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, Functions.POST_NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSwitchUI();
    }

    private void updateSwitchUI() {
        boolean isAccessibilityEnabled = Settings.Secure.getInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0) == 1;
        boolean isOverlayEnabled= Settings.canDrawOverlays(PermissionsActivity.this);
        binding.permissionAccessibilitySwitch.setChecked(isAccessibilityEnabled);
        binding.permissionOverlaySwitch.setChecked(isOverlayEnabled);
        // binding.permissionProceedBtn.setEnabled((isAccessibilityEnabled && isOverlayEnabled));
        // binding.permissionProceedBtn.setClickable((isAccessibilityEnabled && isOverlayEnabled));
        binding.permissionProceedBtn.setEnabled(isAccessibilityEnabled);
        binding.permissionProceedBtn.setClickable(isAccessibilityEnabled);

        /*binding.permissionProceedBtn.setEnabled(isOverlayEnabled);
        binding.permissionProceedBtn.setClickable(isOverlayEnabled);*/

        if(isAccessibilityEnabled){
            binding.permissionAccessibilitySwitch.setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.themeColor)));
            binding.permissionAccessibilitySwitch.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
            if(!Functions.isServiceRunning(PermissionsActivity.this, MyAccessibilityService.class)){
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.notchtouch.appwake.andriod", "com.notchtouch.appwake.andriod.Services.MyAccessibilityService"));
                startService(intent);
            }
        }
        else{
            binding.permissionAccessibilitySwitch.setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
            binding.permissionAccessibilitySwitch.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.transparent)));
        }

        /*if(isOverlayEnabled){
            binding.permissionOverlaySwitch.setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.themeColor)));
            binding.permissionOverlaySwitch.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
        }
        else{
            binding.permissionOverlaySwitch.setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
            binding.permissionOverlaySwitch.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.transparent)));
        }*/
    }
}
