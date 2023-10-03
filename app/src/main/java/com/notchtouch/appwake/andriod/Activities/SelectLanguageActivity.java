package com.notchtouch.appwake.andriod.Activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivitySelectLanguageBinding;

public class SelectLanguageActivity extends AppCompatActivity {

    ActivitySelectLanguageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.lightBackgroundStatusBarDesign(this);
        binding = ActivitySelectLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int selected_lang = Functions.getSharedPref(this, Functions.APP_SETTINGS_PREF_NAME, Functions.LANGUAGE_SELECTED, "int", 0);
        if (selected_lang > 0) updateLanguageTilesUI(selected_lang, false);

        boolean isFirst = getIntent().getBooleanExtra("isFirst", true);
        binding.selectLanguageBackButton.setVisibility(!isFirst ? View.VISIBLE : View.GONE);
        binding.selectLanguageBackButton.setOnClickListener(v -> onBackPressed());

        binding.selectLanguageAmericaLang.setOnClickListener(v -> updateLanguageTilesUI(1, true));
        binding.selectLanguageIndiaLang.setOnClickListener(v -> updateLanguageTilesUI(2, true));
        binding.selectLanguageFranceLang.setOnClickListener(v -> updateLanguageTilesUI(3, true));
        binding.selectLanguageSpainLang.setOnClickListener(v -> updateLanguageTilesUI(4, true));
        binding.selectLanguageRussiaLang.setOnClickListener(v -> updateLanguageTilesUI(5, true));
        binding.selectLanguageGermanyLang.setOnClickListener(v -> updateLanguageTilesUI(6, true));
        binding.selectLanguagePortugalLang.setOnClickListener(v -> updateLanguageTilesUI(7, true));
        binding.selectLanguageItalyLang.setOnClickListener(v -> updateLanguageTilesUI(8, true));
        binding.selectLanguageKoreaLang.setOnClickListener(v -> updateLanguageTilesUI(9, true));
        binding.selectLanguageUaeLang.setOnClickListener(v-> updateLanguageTilesUI(10, true));
        binding.selectLanguageBengalLang.setOnClickListener(v-> updateLanguageTilesUI(11, true));
    }

    private void updateLanguageTilesUI(int selected, boolean isClicked) {
        for(int i=1;i<=11;i++){
            if(i == selected){
                Functions.putSharedPref(this, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_SELECTLANGUAGE_COMPLETE, "boolean", true);
                Functions.putSharedPref(this, Functions.APP_SETTINGS_PREF_NAME, Functions.LANGUAGE_SELECTED, "int", selected);
                binding.selectLanguageAmericaLang.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.themeColor)));
                binding.selectLanguageAmericaText.setTextColor(ContextCompat.getColor(this, R.color.white));
                binding.selectLanguageAmericaForward.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
            }
            else{
                binding.selectLanguageAmericaLang.setBackgroundTintList(null);
                binding.selectLanguageAmericaText.setTextColor(ContextCompat.getColor(this, R.color.black));
                binding.selectLanguageAmericaForward.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
            }
        }
        if(isClicked){
            Class<?> class_name= getIntent().getBooleanExtra("isUpating", false) ? HomeActivity.class : TermsOfServicesActivity.class;
            startActivity(new Intent(getApplicationContext(), class_name));
            finish();
        }
    }
}