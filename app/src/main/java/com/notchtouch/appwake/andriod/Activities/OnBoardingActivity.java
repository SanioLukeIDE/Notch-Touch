package com.notchtouch.appwake.andriod.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adsmodule.api.adsModule.AdUtils;
import com.adsmodule.api.adsModule.utils.Constants;
import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivityOnBoardingBinding;

public class OnBoardingActivity extends AppCompatActivity {

    ActivityOnBoardingBinding binding;

    int slider_page = 0;
    String ob_title1;
    String ob_subtitle1;
    String ob_title2;
    String ob_subtitle2;
    int slider_anim1= R.drawable.sample_onboarding_anim1;
    int slider_anim2= R.drawable.sample_onboarding_anim2;
    String ob_anim1;
    String ob_anim2;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.loadLocale(this);
        Functions.lightBackgroundStatusBarDesign(this);
        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ob_title1 = getString(R.string.onboarding_title1);
        ob_subtitle1 = getString(R.string.onboarding_subtitle1);
        ob_title2 = getString(R.string.onboarding_title2);
        ob_subtitle2 = getString(R.string.onboarding_subtitle2);

        ob_anim1 = "android.resource://"+getPackageName()+"/"+R.raw.onboarding_anim1;
        ob_anim2 = "android.resource://"+getPackageName()+"/"+R.raw.onboarding_anim2;

        uri = Uri.parse(ob_anim1);
        binding.onBoardingSliderAnimation.setVideoURI(uri);
        binding.onBoardingSliderAnimation.start();

        binding.onBoardingSliderTitle.setText(ob_title1);
        binding.onBoardingSliderSubtitle.setText(ob_subtitle1);
        binding.onBoardingSliderIndicator.setRotationY(180);

        binding.onBoardingNextBtn.setOnClickListener(v -> {
            if(slider_page == 0){
                binding.onBoardingSliderTitle.setText(ob_title2);
                binding.onBoardingSliderSubtitle.setText(ob_subtitle2);
                binding.onBoardingSliderIndicator.setRotationY(0);
                uri = Uri.parse(ob_anim2);
                binding.onBoardingSliderAnimation.setVideoURI(uri);
                binding.onBoardingSliderAnimation.start();
                slider_page++;
            }
            else{
                Functions.putSharedPref(OnBoardingActivity.this, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_ONBOARDING_COMPLETE, "boolean", true);
                startActivity(new Intent(getApplicationContext(), PermissionsActivity.class));
                finish();
            }
        });

        binding.onBoardingSliderAnimation.setOnCompletionListener(mp -> {
            binding.onBoardingSliderAnimation.seekTo(0);
            binding.onBoardingSliderAnimation.start();
        });
    }

    @Override
    public void onBackPressed() {
        if(slider_page > 0){
            binding.onBoardingSliderTitle.setText(ob_title1);
            binding.onBoardingSliderSubtitle.setText(ob_subtitle1);
            binding.onBoardingSliderIndicator.setRotationY(180);
            uri = Uri.parse(ob_anim1);
            binding.onBoardingSliderAnimation.setVideoURI(uri);
            binding.onBoardingSliderAnimation.start();
            slider_page--;
        }
        else AdUtils.showBackPressAds(OnBoardingActivity.this, Constants.adsResponseModel.getApp_open_ads().getAdx(), state_load -> super.onBackPressed());
    }

}