package com.notchtouch.appwake.andriod.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivityEventActionsBinding;

public class EventActionsActivity extends AppCompatActivity {

    ActivityEventActionsBinding binding;
    Intent intentData;
    int event = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.lightBackgroundStatusBarDesign(this);
        binding = ActivityEventActionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intentData= getIntent();
        event = intentData.getIntExtra("event", 1);
        String title = intentData.getStringExtra("event_name");
        binding.eventActionsTitle.setText(title != null && !title.isEmpty() ? title : getString(R.string.app_name));

        binding.eventActionsBackButton.setOnClickListener(v-> onBackPressed());

        binding.eventActionsBtn.setOnClickListener(v -> proceedToEventOptionsPage(1, "Action"));
        binding.eventAccessBtn.setOnClickListener(v -> proceedToEventOptionsPage(2, "Access"));
        binding.eventModesBtn.setOnClickListener(v -> proceedToEventOptionsPage(3, "Modes"));
        binding.eventToolsBtn.setOnClickListener(v -> proceedToEventOptionsPage(4, "Tools"));
        binding.eventCommunicationBtn.setOnClickListener(v -> proceedToEventOptionsPage(5, "Communication"));
        binding.eventMediaBtn.setOnClickListener(v -> proceedToEventOptionsPage(6, "Media"));
        binding.eventSystemBtn.setOnClickListener(v -> proceedToEventOptionsPage(7, "System"));
    }

    private void proceedToEventOptionsPage(int action, String action_name) {
        Intent intent = new Intent(getApplicationContext(), ActionOptionsActivity.class);
        intent.putExtra("event", event);
        intent.putExtra("action", action);
        intent.putExtra("action_name", action_name);
        startActivity(intent);
    }
}