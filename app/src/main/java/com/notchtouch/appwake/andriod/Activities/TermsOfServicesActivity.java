package com.notchtouch.appwake.andriod.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.notchtouch.appwake.andriod.R;
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
            Functions.putSharedPref(this, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_TERMSOFSERVICES_COMPLETE, "boolean", true);
            startActivity(new Intent(getApplicationContext(), PermissionsActivity.class));
            finish();
        });

        binding.tosHyperLinkPrivacyPolicy.setOnClickListener(v->{
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Functions.PRIVACY_POLICY)));
        });

        binding.tosHyperLinkTermsConditions.setOnClickListener(v-> startActivity(new Intent(getApplicationContext(), TermsConditionsActivity.class)));
    }
}