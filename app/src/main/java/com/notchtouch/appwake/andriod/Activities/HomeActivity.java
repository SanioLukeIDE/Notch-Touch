package com.notchtouch.appwake.andriod.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.adsmodule.api.adsModule.utils.AdUtils;
import com.adsmodule.api.adsModule.utils.Constants;
import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivityHomeBinding;
import com.notchtouch.appwake.andriod.databinding.ExitDialogBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.loadLocale(this);
        Functions.lightBackgroundStatusBarDesign(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.homeExploreButton.setOnClickListener(v -> {
            AdUtils.showInterstitialAd(HomeActivity.this, isLoaded -> {
                startActivity(new Intent(getApplicationContext(), TouchEventsActivity.class));
            });
        });

        binding.homeSettingsBtn.setOnClickListener(v -> {
            AdUtils.showInterstitialAd(HomeActivity.this, isLoaded -> {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            });
        });

        binding.homeShareappBtn.setOnClickListener(v -> {
            try {
                Functions.sendFlurryLog("The Notch App shared initialized");
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share " + getString(R.string.app_name));
                String shareMessage = getString(R.string.home_share_app_content, getString(R.string.app_name));
                shareMessage += "Download Link: https://play.google.com/store/apps/details?id=" + getPackageName();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share " + getString(R.string.app_name)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.homePrivcaypolicyBtn.setOnClickListener(v -> {
            Functions.sendFlurryLog("The Notch App - Privacy Policy clicked");
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Functions.PRIVACY_POLICY)));
        });
    }

    @Override
    public void onBackPressed() {
        ExitDialogBinding permissionNavDialogBinding;
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        permissionNavDialogBinding = ExitDialogBinding.inflate(LayoutInflater.from(HomeActivity.this));
        builder.setView(permissionNavDialogBinding.getRoot());
        AlertDialog dialog = builder.create();
        builder.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        permissionNavDialogBinding.exitRateLay.setOnClickListener(v1-> {
            Functions.sendFlurryLog("Rate Us Clicked");
            Functions.reviewDialog(HomeActivity.this);
        });

        permissionNavDialogBinding.exitDialogProceedButton.setOnClickListener(view -> super.onBackPressed());
        permissionNavDialogBinding.exitDialogCancelButton.setOnClickListener(view -> dialog.cancel());
    }
}