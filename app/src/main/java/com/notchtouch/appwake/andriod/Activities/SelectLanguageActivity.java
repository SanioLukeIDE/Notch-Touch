package com.notchtouch.appwake.andriod.Activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.adsmodule.api.adsModule.utils.AdUtils;
import com.adsmodule.api.adsModule.utils.Constants;
import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivitySelectLanguageBinding;

public class SelectLanguageActivity extends AppCompatActivity {

    ActivitySelectLanguageBinding binding;
    boolean isUpdating = false;

    RelativeLayout[] lang_lays_list;
    TextView[] lang_txt_list;
    ImageView[] lang_image_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.loadLocale(this);
        Functions.lightBackgroundStatusBarDesign(this);
        binding = ActivitySelectLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lang_lays_list = new RelativeLayout[]{binding.selectLanguageAmericaLang, binding.selectLanguageIndiaLang,
                binding.selectLanguageFranceLang, binding.selectLanguageSpainLang, binding.selectLanguageRussiaLang,
                binding.selectLanguageGermanyLang, binding.selectLanguagePortugalLang, binding.selectLanguageItalyLang,
                binding.selectLanguageKoreaLang, binding.selectLanguageUaeLang, binding.selectLanguageBengalLang};

        lang_txt_list = new TextView[]{binding.selectLanguageAmericaText, binding.selectLanguageIndiaText,
                binding.selectLanguageFranceText, binding.selectLanguageSpainText, binding.selectLanguageRussiaText,
                binding.selectLanguageGermanyText, binding.selectLanguagePortugalText, binding.selectLanguageItalyText,
                binding.selectLanguageKoreaText, binding.selectLanguageUaeText, binding.selectLanguageBengalText};

        lang_image_list = new ImageView[]{binding.selectLanguageAmericaForward, binding.selectLanguageIndiaForward,
                binding.selectLanguageFranceForward, binding.selectLanguageSpainForward, binding.selectLanguageRussiaForward,
                binding.selectLanguageGermanyForward, binding.selectLanguagePortugalForward, binding.selectLanguageItalyForward,
                binding.selectLanguageKoreaForward, binding.selectLanguageUaeForward, binding.selectLanguageBengalForward};

        int selected_lang = Functions.getSharedPref(this, Functions.APP_SETTINGS_PREF_NAME, Functions.LANGUAGE_SELECTED, "int", 0);
        lang_lays_list[selected_lang].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.themeColor)));
        lang_txt_list[selected_lang].setTextColor(ContextCompat.getColor(this, R.color.white));
        lang_image_list[selected_lang].setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));

        isUpdating = getIntent().getBooleanExtra("isUpdating", false);
        binding.selectLanguageBackButton.setVisibility(isUpdating ? View.VISIBLE : View.GONE);
        binding.selectLanguageBackButton.setOnClickListener(v -> onBackPressed());

        binding.selectLanguageAmericaLang.setOnClickListener(v -> updateLanguageTilesUI(0));
        binding.selectLanguageIndiaLang.setOnClickListener(v -> updateLanguageTilesUI(1));
        binding.selectLanguageFranceLang.setOnClickListener(v -> updateLanguageTilesUI(2));
        binding.selectLanguageSpainLang.setOnClickListener(v -> updateLanguageTilesUI(3));
        binding.selectLanguageRussiaLang.setOnClickListener(v -> updateLanguageTilesUI(4));
        binding.selectLanguageGermanyLang.setOnClickListener(v -> updateLanguageTilesUI(5));
        binding.selectLanguagePortugalLang.setOnClickListener(v -> updateLanguageTilesUI(6));
        binding.selectLanguageItalyLang.setOnClickListener(v -> updateLanguageTilesUI(7));
        binding.selectLanguageKoreaLang.setOnClickListener(v -> updateLanguageTilesUI(8));
        binding.selectLanguageUaeLang.setOnClickListener(v -> updateLanguageTilesUI(9));
        binding.selectLanguageBengalLang.setOnClickListener(v -> updateLanguageTilesUI(10));
    }

    private void updateLanguageTilesUI(int selected) {
        for (int i = 0; i < lang_lays_list.length; i++) {
            if (i == selected) {
                Functions.sendFlurryLog("The Notch Language Selected : "+Functions.lang_list[selected]);
                Functions.putSharedPref(this, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_SELECTLANGUAGE_COMPLETE, "boolean", true);
                Functions.putSharedPref(this, Functions.APP_SETTINGS_PREF_NAME, Functions.LANGUAGE_SELECTED, "int", selected);
            }
            lang_lays_list[i].setBackgroundTintList(i == selected ? ColorStateList.valueOf(ContextCompat.getColor(this, R.color.themeColor)) : null);
            lang_txt_list[i].setTextColor(ContextCompat.getColor(this, i == selected ? R.color.white : R.color.black));
            lang_image_list[i].setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, i == selected ? R.color.white : R.color.black)));
        }

        AdUtils.showInterstitialAd(SelectLanguageActivity.this, isLoaded -> {
            Class<?> class_name = isUpdating ? HomeActivity.class : OnBoardingActivity.class;
            startActivity(new Intent(getApplicationContext(), class_name));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        AdUtils.showBackPressAd(SelectLanguageActivity.this, state_load -> super.onBackPressed());
    }
}