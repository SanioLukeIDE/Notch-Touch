package com.notchtouch.appwake.andriod.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivityTouchEventsBinding;

public class TouchEventsActivity extends AppCompatActivity {

    ActivityTouchEventsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.loadLocale(this);
        Functions.lightBackgroundStatusBarDesign(this);
        binding= ActivityTouchEventsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.touchEventsBackButton.setOnClickListener(v-> onBackPressed());

        Glide.with(this).load(R.drawable.event_single_touch).into(binding.touchEventSingleTouchAnim);
        Glide.with(this).load(R.drawable.event_long_touch).into(binding.touchEventLongTouchAnim);
        Glide.with(this).load(R.drawable.event_double_touch).into(binding.touchEventDoubleClickAnim);
        Glide.with(this).load(R.drawable.event_swipe_left).into(binding.touchEventSwipeLeftAnim);
        Glide.with(this).load(R.drawable.event_swipe_right).into(binding.touchEventSwipeRightAnim);

        binding.touchEventsSingleTouchBtn.setOnClickListener(v-> proceedToActionPage(1, getString(R.string.touch_events_single_touch)));
        binding.touchEventsLongTouchBtn.setOnClickListener(v-> proceedToActionPage(2, getString(R.string.touch_events_long_touch)));
        binding.touchEventsDoubleClickBtn.setOnClickListener(v-> proceedToActionPage(3, getString(R.string.touch_events_double_click)));
        binding.touchEventsSwipeLeftBtn.setOnClickListener(v-> proceedToActionPage(4, getString(R.string.touch_events_swipe_left)));
        binding.touchEventsSwipeRightBtn.setOnClickListener(v-> proceedToActionPage(5, getString(R.string.touch_events_swipe_right)));
    }

    private void proceedToActionPage(int event, String event_name){
        Intent intent= new Intent(getApplicationContext(), EventActionsActivity.class);
        intent.putExtra("event", event);
        intent.putExtra("event_name", event_name);
        startActivity(intent);
    }
}