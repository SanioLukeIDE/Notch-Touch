package com.notchtouch.appwake.andriod.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.databinding.ActivityTouchEventsBinding;

public class TouchEventsActivity extends AppCompatActivity {

    ActivityTouchEventsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityTouchEventsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(this).load(R.drawable.event_single_touch).into(binding.touchEventSingleTouchAnim);
        Glide.with(this).load(R.drawable.event_long_touch).into(binding.touchEventLongTouchAnim);
        Glide.with(this).load(R.drawable.event_double_touch).into(binding.touchEventDoubleClickAnim);
        Glide.with(this).load(R.drawable.event_swipe_left).into(binding.touchEventSwipeLeftAnim);
        Glide.with(this).load(R.drawable.event_swipe_right).into(binding.touchEventSwipeRightAnim);
    }
}