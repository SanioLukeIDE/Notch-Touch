package com.notchtouch.appwake.andriod.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.databinding.ActivityTermsOfServicesBinding;

public class TermsOfServicesActivity extends AppCompatActivity {

    ActivityTermsOfServicesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityTermsOfServicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}