package com.notchtouch.appwake.andriod.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.databinding.ActivityEventActionsBinding;

public class EventActionsActivity extends AppCompatActivity {

    ActivityEventActionsBinding binding;
    Intent intentData;
    int event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityEventActionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        event= intentData.getIntExtra("event", 0);
        String title= intentData.getStringExtra("action_name");
        binding.eventActionsTitle.setText(title!=null && !title.isEmpty() ? title : getString(R.string.app_name));


    }
}