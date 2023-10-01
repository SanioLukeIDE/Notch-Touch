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
    String ob_title1 = "Enhance User-experience ";
    String ob_subtitle1 = "use the notch for fast actions and seamless mobile experience.";
    String ob_title2 = "Customize Your Controls";
    String ob_subtitle2 = "Swipe, tap, or hold the notch for different and quick actions";
    int slider_anim1= R.drawable.sample_onboarding_anim1;
    int slider_anim2= R.drawable.sample_onboarding_anim2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Functions.lightBackgroundStatusBarDesign(this);
        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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