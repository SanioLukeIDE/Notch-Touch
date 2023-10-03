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
        Functions.lightBackgroundStatusBarDesign(this);
        binding= ActivityTouchEventsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.touchEventsBackButton.setOnClickListener(v-> onBackPressed());

        Glide.with(this).load(R.drawable.event_single_touch).into(binding.touchEventSingleTouchAnim);
        Glide.with(this).load(R.drawable.event_long_touch).into(binding.touchEventLongTouchAnim);
        Glide.with(this).load(R.drawable.event_double_touch).into(binding.touchEventDoubleClickAnim);
        Glide.with(this).load(R.drawable.event_swipe_left).into(binding.touchEventSwipeLeftAnim);
        Glide.with(this).load(R.drawable.event_swipe_right).into(binding.touchEventSwipeRightAnim);

        binding.touchEventsSingleTouchBtn.setOnClickListener(v-> proceedToActionPage(1, "Single Touch"));
        binding.touchEventsLongTouchBtn.setOnClickListener(v-> proceedToActionPage(2, "Long Touch"));
        binding.touchEventsDoubleClickBtn.setOnClickListener(v-> proceedToActionPage(3, "Double Click"));
        binding.touchEventsSwipeLeftBtn.setOnClickListener(v-> proceedToActionPage(4, "Swipe Right to Left"));
        binding.touchEventsSwipeRightBtn.setOnClickListener(v-> proceedToActionPage(5, "Swipe Left to Right"));
    }

    private void proceedToActionPage(int event, String event_name){
        Intent intent= new Intent(getApplicationContext(), EventActionsActivity.class);
        intent.putExtra("event", event);
        intent.putExtra("event_name", event_name);
        startActivity(intent);
    }
}