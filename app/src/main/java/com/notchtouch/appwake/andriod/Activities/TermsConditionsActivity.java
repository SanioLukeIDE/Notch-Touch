package com.notchtouch.appwake.andriod.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adsmodule.api.adsModule.AdUtils;
import com.adsmodule.api.adsModule.utils.Constants;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivityTermsConditionsBinding;

public class TermsConditionsActivity extends AppCompatActivity {

    ActivityTermsConditionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.loadLocale(this);
        Functions.lightBackgroundStatusBarDesign(this);
        binding= ActivityTermsConditionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Functions.sendFlurryLog("The Notch : Terms & Conditions Viewed");
        binding.termsConditionsBackButton.setOnClickListener(v-> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        AdUtils.showBackPressAds(TermsConditionsActivity.this, Constants.adsResponseModel.getApp_open_ads().getAdx(), state_load -> super.onBackPressed());
    }
}