package com.notchtouch.appwake.andriod.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.databinding.ActivitySelectLanguageBinding;

public class SelectLanguageActivity extends AppCompatActivity {

    ActivitySelectLanguageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySelectLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}