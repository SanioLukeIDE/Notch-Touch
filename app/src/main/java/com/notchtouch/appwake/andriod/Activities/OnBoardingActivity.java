package com.notchtouch.appwake.andriod.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.databinding.ActivityOnBoardingBinding;

public class OnBoardingActivity extends AppCompatActivity {

    ActivityOnBoardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}