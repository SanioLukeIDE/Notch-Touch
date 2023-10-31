package com.notchtouch.appwake.andriod.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adsmodule.api.adsModule.AdUtils;
import com.adsmodule.api.adsModule.utils.Constants;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivityTermsOfServicesBinding;

public class TermsOfServicesActivity extends AppCompatActivity {

    ActivityTermsOfServicesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.loadLocale(this);
        Functions.lightBackgroundStatusBarDesign(this);
        binding= ActivityTermsOfServicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tosAcceptButton.setOnClickListener(v->{
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), TermsOfServicesActivity.this, isLoaded -> {
                Functions.putSharedPref(this, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_TERMSOFSERVICES_COMPLETE, "boolean", true);
                startActivity(new Intent(getApplicationContext(), SelectLanguageActivity.class));
                finish();
            });
        });

        binding.tosHyperLinkPrivacyPolicy.setOnClickListener(v->{
            Functions.sendFlurryLog("The Notch : Privacy Policy Clicked - From Terms Of Use");
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Functions.PRIVACY_POLICY)));
        });

        binding.tosHyperLinkTermsConditions.setOnClickListener(v-> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), TermsOfServicesActivity.this, isLoaded -> {
                startActivity(new Intent(getApplicationContext(), TermsConditionsActivity.class));
            });
        });
    }
}