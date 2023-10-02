package com.notchtouch.appwake.andriod.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivityActionOptionsBinding;

public class ActionOptionsActivity extends AppCompatActivity {

    ActivityActionOptionsBinding binding;
    int event = 1;
    int action = 1;
    String action_name;
    LinearLayout selected_action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.lightBackgroundStatusBarDesign(this);
        binding = ActivityActionOptionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        event = getIntent().getIntExtra("event", 1);
        action = getIntent().getIntExtra("action", 1);
        action_name= getIntent().getStringExtra("action_name");
        binding.actionOptionsTitle.setText(action_name != null && !action_name.isEmpty() ? action_name : getString(R.string.app_name));

        LinearLayout[] layoutElements = new LinearLayout[]{ binding.aoActionLay, binding.aoAccessLay, binding.aoModesLay,
                binding.aoToolsLay, binding.aoCommunicationLay, binding.aoMediaLay, binding.aoSystemLay };
        for (View element : layoutElements) element.setVisibility(View.GONE);
        selected_action= layoutElements[action - 1];
        selected_action.setVisibility(View.VISIBLE);


    }
}