package com.notchtouch.appwake.andriod.Activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.DisplayCutout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Services.MyAccessibilityService;
import com.notchtouch.appwake.andriod.Services.NotchService;
import com.notchtouch.appwake.andriod.Utils.Functions;
import com.notchtouch.appwake.andriod.databinding.ActivityActionOptionsBinding;

public class ActionOptionsActivity extends AppCompatActivity {

    ActivityActionOptionsBinding binding;
    int event = 1;
    int action = 1;
    String action_name;
    LinearLayout selected_action;
    ImageView[] all_radioButtons;
    int user_selected_action = 0;


    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int REQUEST_OPEN_CAMERA_PERMISSION = 102;
    private static final int WRITE_SETTINGS_PERMISSION_REQUEST = 103;
    private static final int ADMIN_PERMISSION_REQUEST = 104;
    private static final int OVERLAY_PERMISSION = 105;
    private static final int ACCESSIBILITY_PERMISSION = 106;
    private static final int NOTIFICATION_PERMISSION_RC = 123;
    private static final int OPEN_SELECTED_APP_RESULT = 107;

    private CameraManager cameraManager;
    private String cameraId;
    private boolean isFlashOn = false;

    // DND Toggle
    private static final String CHANNEL_ID = "my_channel";

    // Sound/Mute & Sound/Vibrate Toggle
    private AudioManager audioManager;

    // Music Play/Pause Toggle
    private MediaSession mediaSession;
    DisplayCutout displayCutout;
    private Rect notchArea;

    // Lock the Screen
    private DevicePolicyManager devicePolicyManager;
    private ComponentName mComponentName;
    private ActivityManager activityManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.lightBackgroundStatusBarDesign(this);
        binding = ActivityActionOptionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        event = getIntent().getIntExtra("event", 1);
        action = getIntent().getIntExtra("action", 1);
        action_name = getIntent().getStringExtra("action_name");
        binding.actionOptionsTitle.setText(action_name != null && !action_name.isEmpty() ? action_name : getString(R.string.app_name));

        LinearLayout[] layoutElements = new LinearLayout[]{binding.aoActionLay, binding.aoAccessLay, binding.aoModesLay,
                binding.aoToolsLay, binding.aoCommunicationLay, binding.aoMediaLay, binding.aoSystemLay};
        for (View element : layoutElements) element.setVisibility(View.GONE);
        selected_action = layoutElements[action - 1];
        selected_action.setVisibility(View.VISIBLE);

        setRadioButtonsArrayDefaultState();

        binding.actionOptionsBackButton.setOnClickListener(v -> onBackPressed());

        binding.aoActionNothingLay.setOnClickListener(v -> updateRadioButtonsArrayState(0));
        binding.aoActionToggleFlashlightLay.setOnClickListener(v -> updateRadioButtonsArrayState(1));
        binding.aoActionTakeScreenshotLay.setOnClickListener(v -> updateRadioButtonsArrayState(2));
        binding.aoActionOpenLongPressPowerMenuLay.setOnClickListener(v -> updateRadioButtonsArrayState(3));
        binding.aoAccessQuickAppAccessLay.setOnClickListener(v -> updateRadioButtonsArrayState(4));
        binding.aoAccessOpenCameraLay.setOnClickListener(v -> updateRadioButtonsArrayState(5));
        binding.aoAccessOpenRecentAppsLay.setOnClickListener(v -> updateRadioButtonsArrayState(6));
        binding.aoOpenSelectedAppsLay.setOnClickListener(v -> updateRadioButtonsArrayState(7));
        binding.aoModesToggleOrientationLay.setOnClickListener(v -> updateRadioButtonsArrayState(8));
        binding.aoModesToggleDndLay.setOnClickListener(v -> updateRadioButtonsArrayState(9));
        binding.aoToolsScanQrCodesLay.setOnClickListener(v -> updateRadioButtonsArrayState(10));
        binding.aoToolsOpenWebsitesLay.setOnClickListener(v -> updateRadioButtonsArrayState(11));
        binding.aoCommunicationQuickDialLay.setOnClickListener(v -> updateRadioButtonsArrayState(12));
        binding.aoMediaPlayPauseMusicLay.setOnClickListener(v -> updateRadioButtonsArrayState(13));
        binding.aoMediaPlayNextMusicLay.setOnClickListener(v -> updateRadioButtonsArrayState(14));
        binding.aoMediaPlayPrevMusicLay.setOnClickListener(v -> updateRadioButtonsArrayState(15));
        binding.aoSystemChangeBrightnessLay.setOnClickListener(v -> updateRadioButtonsArrayState(16));
        binding.aoSystemToggleSoundMuteLay.setOnClickListener(v -> updateRadioButtonsArrayState(17));
        binding.aoSystemToggleSoundVibrateLay.setOnClickListener(v -> updateRadioButtonsArrayState(18));
        binding.aoSystemPowerOffLay.setOnClickListener(v -> updateRadioButtonsArrayState(19));

        binding.aoToolsOpenWebsitesLink.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = binding.aoToolsOpenWebsitesLink.getText().toString();
                if (!txt.isEmpty())
                    Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.OPEN_WEBSITE_LINK, "string", txt);
                else
                    Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.OPEN_WEBSITE_LINK, "string", "https://www.google.com");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.aoSystemChangeBrightnessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.BRIGHTNESS_VALUE, "int", progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setRadioButtonsArrayDefaultState() {
        binding.aoToolsOpenWebsitesLink.setVisibility(View.GONE);
        binding.aoCommunicationQuickDialNumber.setVisibility(View.GONE);
        binding.aoSystemChangeBrightnessSeekbar.setVisibility(View.GONE);
        all_radioButtons = new ImageView[]{
                binding.aoActionNothingImage                   // 0 pos
                , binding.aoActionToggleFlashlightImage        // 1 pos
                , binding.aoActionTakeScreenshotImage          // 2 pos
                , binding.aoActionOpenLongPressPowerMenuImage  // 3 pos
                , binding.aoAccessQuickAppAccessImage          // 4 pos
                , binding.aoAccessOpenCameraImage              // 5 pos
                , binding.aoAccessOpenRecentAppsImage          // 6 pos
                , binding.aoOpenSelectedAppsImage              // 7 pos
                , binding.aoModesToggleOrientationImage        // 8 pos
                , binding.aoModesToggleDndImage                // 9 pos
                , binding.aoToolsScanQrCodesImage              // 10 pos
                , binding.aoToolsOpenWebsitesImage             // 11 pos
                , binding.aoCommunicationQuickDialImage        // 12 pos
                , binding.aoMediaPlayPauseMusicImage           // 13 pos
                , binding.aoMediaPlayNextMusicImage            // 14 pos
                , binding.aoMediaPlayPrevMusicImage            // 15 pos
                , binding.aoSystemChangeBrightnessImage        // 16 pos
                , binding.aoSystemToggleSoundMuteImage         // 17 pos
                , binding.aoSystemToggleSoundVibrateImage      // 18 pos
                , binding.aoSystemPowerOffImage                // 19 pos
        };

        clearAllRadioButtons();
        int selected_option = Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.OPTION_SELECTED, "int", 0);
        int selected_event = Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.EVENT_SELECTED, "int", 1);
        int color = selected_option < 4 ? R.color.singleTouchColor :
                (selected_option < 8 ? R.color.longTouchColor : (selected_option < 10 ? R.color.doubleClickColor :
                        (selected_option < 12 ? R.color.swipeLeftColor : (selected_option < 13 ? R.color.swipeRightColor :
                                (selected_option < 16 ? R.color.singleTouchColor : R.color.longTouchColor)))));

        if (selected_event == event && selected_option == 11) {
            binding.aoToolsOpenWebsitesLink.setVisibility(View.VISIBLE);
            String weblink = Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.OPEN_WEBSITE_LINK, "string", "https://www.google.com");
            binding.aoToolsOpenWebsitesLink.setText(weblink);
        }
        if (selected_event == event && selected_option == 12) {
            binding.aoCommunicationQuickDialNumber.setVisibility(View.VISIBLE);
            String number = Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.OPEN_WEBSITE_LINK, "string", "1234567890");
            binding.aoToolsOpenWebsitesLink.setText(number);
        }
        if (selected_event == event && selected_option == 16) {
            binding.aoSystemChangeBrightnessSeekbar.setVisibility(View.VISIBLE);
            int brightness_val = Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.BRIGHTNESS_VALUE, "int", 50);
            binding.aoSystemChangeBrightnessSeekbar.setProgress(brightness_val);
        }
        if (selected_event == event) {
            all_radioButtons[selected_option].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check_circle));
            all_radioButtons[selected_option].setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), color)));
        }
    }

    private void clearAllRadioButtons() {
        for (int i = 0; i < 20; i++) {
            ImageView imageView = all_radioButtons[i];
            int drawable_id = i < 4 ? R.drawable.te_singletouch_border :
                    (i < 8 ? R.drawable.te_longtouch_border : (i < 10 ? R.drawable.te_doubleclick_border :
                            (i < 12 ? R.drawable.te_swipeleft_border : (i < 13 ? R.drawable.te_swiperight_border :
                                    (i < 16 ? R.drawable.te_singletouch_border : R.drawable.te_longtouch_border)))));

            imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), drawable_id));
            imageView.setImageTintList(null);
        }
    }

    private void updateRadioButtonsArrayState(int user_selected) {
        clearAllRadioButtons();
        binding.aoToolsOpenWebsitesLink.setVisibility(View.GONE);
        binding.aoCommunicationQuickDialNumber.setVisibility(View.GONE);
        binding.aoSystemChangeBrightnessSeekbar.setVisibility(View.GONE);
        user_selected_action = user_selected;
        for (int i = 0; i < 20; i++) {
            ImageView imageView = all_radioButtons[i];
            int drawable_id = i < 4 ? R.drawable.te_singletouch_border :
                    (i < 8 ? R.drawable.te_longtouch_border : (i < 10 ? R.drawable.te_doubleclick_border :
                            (i < 12 ? R.drawable.te_swipeleft_border : (i < 13 ? R.drawable.te_swiperight_border :
                                    (i < 16 ? R.drawable.te_singletouch_border : R.drawable.te_longtouch_border)))));

            Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.EVENT_SELECTED, "int", 1);
            Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.ACTION_SELECTED, "int", 1);
            Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.OPTION_SELECTED, "int", 0);

            imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), drawable_id));
            imageView.setImageTintList(null);
        }

        if (user_selected >= 0) {
            if (user_selected == 0 || user_selected == 5 || user_selected == 10 ||
                    user_selected == 13 || user_selected == 14 || user_selected == 15 ||
                    user_selected == 18)
                updateRadioButtonsUI(user_selected);
            else if (user_selected == 1) {
                if (ContextCompat.checkSelfPermission(ActionOptionsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ActionOptionsActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else updateRadioButtonsUI(user_selected);
            } else if (user_selected == 2) {
                // Take Screenshots (Disabled for now !!!)
            } else if (user_selected == 3) {
                // Open the power long-press menu
            } else if (user_selected == 4) {
                // Quick App Access (Disabled for now !!!)
            } else if (user_selected == 6) {
                // Open Recent App menu
            } else if (user_selected == 7) {
                Intent intent = new Intent(getApplicationContext(), QuickAppAccessActivity.class);
                intent.putExtra("quickAccessTitle", "Open selected app");
                startActivityForResult(intent, OPEN_SELECTED_APP_RESULT);
            }
            else if (user_selected == 8) {
                if (!Settings.System.canWrite(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, WRITE_SETTINGS_PERMISSION_REQUEST);
                } else updateRadioButtonsUI(user_selected);
            }
            else if (user_selected == 9 || user_selected == 17) {
                createNotificationChannel();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (notificationManager != null && !notificationManager.isNotificationPolicyAccessGranted()) {
                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    startActivityForResult(intent, NOTIFICATION_PERMISSION_RC);
                } else updateRadioButtonsUI(user_selected);
            }
            else if (user_selected == 11) {
                String weblink = Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.OPEN_WEBSITE_LINK, "string", "https://www.google.com");
                binding.aoToolsOpenWebsitesLink.setVisibility(View.VISIBLE);
                binding.aoToolsOpenWebsitesLink.setText(weblink);
                updateRadioButtonsUI(user_selected);
            }
            else if (user_selected == 12) {
                String number = Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.OPEN_WEBSITE_LINK, "string", "1234567890");
                binding.aoCommunicationQuickDialNumber.setVisibility(View.VISIBLE);
                binding.aoToolsOpenWebsitesLink.setText(number);
                updateRadioButtonsUI(user_selected);
            }
            else if (user_selected == 16) {
                if (!Settings.System.canWrite(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, WRITE_SETTINGS_PERMISSION_REQUEST);
                } else {
                    int brightness_val = Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.BRIGHTNESS_VALUE, "int", 50);
                    binding.aoSystemChangeBrightnessSeekbar.setVisibility(View.VISIBLE);
                    binding.aoSystemChangeBrightnessSeekbar.setProgress(brightness_val);
                    updateRadioButtonsUI(user_selected);
                }
            }
            else if (user_selected == 19) {
                // Power Off Display
            }
        }
    }

    private void updateRadioButtonsUI(int user_selected) {
        int color = user_selected < 4 ? R.color.singleTouchColor :
                (user_selected < 8 ? R.color.longTouchColor : (user_selected < 10 ? R.color.doubleClickColor :
                        (user_selected < 12 ? R.color.swipeLeftColor : (user_selected < 13 ? R.color.swipeRightColor :
                                (user_selected < 16 ? R.color.singleTouchColor : R.color.longTouchColor)))));

        all_radioButtons[user_selected].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check_circle));
        all_radioButtons[user_selected].setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), color)));

        Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.EVENT_SELECTED, "int", event);
        Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.ACTION_SELECTED, "int", action);
        Functions.putSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.OPTION_SELECTED, "int", user_selected);

        /*if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION);
        } else {
            Intent NotchServiceIntent = new Intent(this, NotchService.class);
            startService(NotchServiceIntent);
        }*/

        boolean accessibilityEnabled = Settings.Secure.getInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0) == 1;
        if (!accessibilityEnabled) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivityForResult(intent, ACCESSIBILITY_PERMISSION);
        }
        else{
            if(!Functions.isServiceRunning(ActionOptionsActivity.this, MyAccessibilityService.class)){
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.notchtouch.appwake.andriod", "com.notchtouch.appwake.andriod.Services.MyAccessibilityService"));
                startService(intent);
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "My Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_SELECTED_APP_RESULT && resultCode == RESULT_OK) {
            updateRadioButtonsUI(7);
        } else if (requestCode == OVERLAY_PERMISSION && Settings.canDrawOverlays(this)) {
            Intent NotchServiceIntent = new Intent(this, NotchService.class);
            startService(NotchServiceIntent);
        }
        else if (requestCode == ACCESSIBILITY_PERMISSION) {
            boolean accessibilityEnabled = Settings.Secure.getInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0) == 1;
            if(accessibilityEnabled && !Functions.isServiceRunning(ActionOptionsActivity.this, MyAccessibilityService.class)){
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.notchtouch.appwake.andriod", "com.notchtouch.appwake.andriod.Services.MyAccessibilityService"));
                startService(intent);
            }
        }
    }
}