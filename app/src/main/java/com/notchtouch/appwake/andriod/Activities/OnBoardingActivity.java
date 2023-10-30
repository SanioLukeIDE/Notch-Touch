package com.notchtouch.appwake.andriod.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
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

        Glide.with(OnBoardingActivity.this).load(slider_anim1).into(binding.onBoardingSliderAnimation);
        binding.onBoardingSliderTitle.setText(ob_title1);
        binding.onBoardingSliderSubtitle.setText(ob_subtitle1);
        binding.onBoardingSliderIndicator.setRotationY(0);

        binding.onBoardingNextBtn.setOnClickListener(v -> {
            if(slider_page == 0){
                Glide.with(OnBoardingActivity.this).load(slider_anim2).into(binding.onBoardingSliderAnimation);
                binding.onBoardingSliderTitle.setText(ob_title2);
                binding.onBoardingSliderSubtitle.setText(ob_subtitle2);
                binding.onBoardingSliderIndicator.setRotationY(180);
                slider_page++;
            }
            else{
                Functions.putSharedPref(OnBoardingActivity.this, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_ONBOARDING_COMPLETE, "boolean", true);
                startActivity(new Intent(getApplicationContext(), SelectLanguageActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(slider_page > 0){
            Glide.with(OnBoardingActivity.this).load(slider_anim1).into(binding.onBoardingSliderAnimation);
            binding.onBoardingSliderTitle.setText(ob_title1);
            binding.onBoardingSliderSubtitle.setText(ob_subtitle1);
            binding.onBoardingSliderIndicator.setRotationY(0);
            slider_page--;
        }
        else super.onBackPressed();
    }
}