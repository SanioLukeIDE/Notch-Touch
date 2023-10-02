package com.notchtouch.appwake.andriod.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.lightBackgroundStatusBarDesign(this);
        binding= ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.homeExploreBtn.setOnClickListener(v-> startActivity(new Intent(getApplicationContext(), TouchEventsActivity.class)));

        binding.homeSettingsBtn.setOnClickListener(v-> startActivity(new Intent(getApplicationContext(), SettingsActivity.class)));

        binding.homeShareappBtn.setOnClickListener(v-> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share " + getString(R.string.app_name));
                String shareMessage = "Check out " + getString(R.string.app_name) + " - the best notch touch app in Android. Customize your phone's notch gestures with our Notch Touch App and perform your favorite actions with a events like tap or double-click, etc on the notch. Try it now!\n\n";
                shareMessage += "Download Link: https://play.google.com/store/apps/details?id=" + getPackageName();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share " + getString(R.string.app_name)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.homePrivcaypolicyBtn.setOnClickListener(v-> {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://editvistaproductions.blogspot.com/p/privacy-policy.html")));
        });
    }
}