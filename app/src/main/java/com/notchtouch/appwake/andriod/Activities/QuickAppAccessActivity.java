package com.notchtouch.appwake.andriod.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.databinding.ActivityQuickAppAccessBinding;

public class QuickAppAccessActivity extends AppCompatActivity {

    ActivityQuickAppAccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityQuickAppAccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}