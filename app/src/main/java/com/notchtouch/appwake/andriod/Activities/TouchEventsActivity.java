package com.notchtouch.appwake.andriod.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.adsmodule.api.adsModule.AdUtils;
import com.adsmodule.api.adsModule.utils.Constants;
import com.bumptech.glide.Glide;
import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivityTouchEventsBinding;

public class TouchEventsActivity extends AppCompatActivity {

    ActivityTouchEventsBinding binding;
    String[] options_list;
    TextView[] events_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.loadLocale(this);
        Functions.lightBackgroundStatusBarDesign(this);
        binding= ActivityTouchEventsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.touchEventsBackButton.setOnClickListener(v-> onBackPressed());
        options_list= Functions.getOptionsList(this);

        events_list= new TextView[]{binding.touchEventsSingleTouchActionText, binding.touchEventsLongTouchActionText, binding.touchEventsDoubleClickActionText,
                binding.touchEventsSwipeLeftActionText,binding.touchEventsSwipeRightActionText};

        Glide.with(this).load(R.drawable.event_single_touch).into(binding.touchEventSingleTouchAnim);
        Glide.with(this).load(R.drawable.event_long_touch).into(binding.touchEventLongTouchAnim);
        Glide.with(this).load(R.drawable.event_double_touch).into(binding.touchEventDoubleClickAnim);
        Glide.with(this).load(R.drawable.event_swipe_left).into(binding.touchEventSwipeLeftAnim);
        Glide.with(this).load(R.drawable.event_swipe_right).into(binding.touchEventSwipeRightAnim);

        updateEventBoxTitles();

        binding.touchEventsSingleTouchBtn.setOnClickListener(v-> proceedToActionPage(1, getString(R.string.touch_events_single_touch)));
        binding.touchEventsLongTouchBtn.setOnClickListener(v-> proceedToActionPage(2, getString(R.string.touch_events_long_touch)));
        binding.touchEventsDoubleClickBtn.setOnClickListener(v-> proceedToActionPage(3, getString(R.string.touch_events_double_click)));
        binding.touchEventsSwipeLeftBtn.setOnClickListener(v-> proceedToActionPage(4, getString(R.string.touch_events_swipe_left)));
        binding.touchEventsSwipeRightBtn.setOnClickListener(v-> proceedToActionPage(5, getString(R.string.touch_events_swipe_right)));
    }

    private void updateEventBoxTitles() {
        int selected_event= Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.EVENT_SELECTED, "int", 1);
        int selected_option= Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.OPTION_SELECTED, "int", 0);
        for(int i=1;i<=5;i++){
            if(i==selected_event) events_list[i-1].setText(options_list[selected_option]);
            else events_list[i-1].setText(getString(R.string.touch_events_default_content));
        }
    }

    private void proceedToActionPage(int event, String event_name){
        AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), TouchEventsActivity.this, isLoaded -> {
            Intent intent = new Intent(getApplicationContext(), EventActionsActivity.class);
            intent.putExtra("event", event);
            intent.putExtra("event_name", event_name);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateEventBoxTitles();
    }

    @Override
    public void onBackPressed() {
        AdUtils.showBackPressAds(TouchEventsActivity.this, Constants.adsResponseModel.getApp_open_ads().getAdx(), state_load -> super.onBackPressed());
    }
}