package com.notchtouch.appwake.andriod.Utils;

import static android.content.Context.CAMERA_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.notchtouch.appwake.andriod.Services.MyAccessibilityService;

import java.net.URLEncoder;

public class CustomGestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private static final String CHANNEL_ID = "my_channel";
    private CameraManager cameraManager;
    private String cameraId;
    private MediaSession mediaSession;
    private AudioManager audioManager;
    private boolean isFlashOn= false;

    Context context;
    MyAccessibilityService accessibilityService;

    public CustomGestureListener(Context context, MyAccessibilityService accessibilityService) {
        this.context= context;
        this.accessibilityService= accessibilityService;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mediaSession = new MediaSession(context, "MyMediaSession");
    }

    @Override
    public boolean onDown(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {
        if(checkScreenOrientationOption() && checkEventTouched(2)){
            runSelectedActionOption();
            Log.e("listener_check", "Long Press Worked....");
        }
    }

    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        float deltaX = e2.getX() - (e1 != null ? e1.getX() : 0);
        float deltaY = e2.getY() - (e1 != null ? e1.getY() : 0);
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (Math.abs(deltaX) > 100 && Math.abs(velocityX) > 30) {
                if (deltaX > 0.0f) {
                    if(checkScreenOrientationOption() && checkEventTouched(5)){
                        runSelectedActionOption();
                        Log.e("listener_check", "Swipe Right Worked....");
                        return true;
                    }
                } else {
                    if(checkScreenOrientationOption() && checkEventTouched(4)){
                        runSelectedActionOption();
                        Log.e("listener_check", "Swipe Left Worked....");
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(@NonNull MotionEvent motionEvent) {
        if(checkScreenOrientationOption() && checkEventTouched(1)){
            runSelectedActionOption();
            Log.e("listener_check", "Single Tap Worked....");
            return true;
        }
        return false;
    }

    @Override
    public boolean onDoubleTap(@NonNull MotionEvent motionEvent) {
        if(checkScreenOrientationOption() && checkEventTouched(3)){
            runSelectedActionOption();
            Log.e("listener_check", "Double Tap Worked....");
            return true;
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(@NonNull MotionEvent motionEvent) {
        return true;
    }

    private void runSelectedActionOption(){
        checkVibrationModeAndPerform();
        int event_option= Functions.getSharedPref(context, Functions.APP_SETTINGS_PREF_NAME, Functions.OPTION_SELECTED, "int", 0);
        if(event_option > 0){
            if(event_option == 1){
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    cameraManager = (CameraManager) context.getSystemService(CAMERA_SERVICE);
                    createNotificationChannel();
                    try {
                        cameraId = cameraManager.getCameraIdList()[0];
                        if (cameraId != null) {
                            try {
                                cameraManager.setTorchMode(cameraId, isFlashOn);
                                isFlashOn = !isFlashOn;
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(event_option == 2){
                accessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_TAKE_SCREENSHOT);
            }
            else if(event_option == 3){
                accessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_POWER_DIALOG);
            }
            else if(event_option == 5){
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    openCameraIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(openCameraIntent);
                }
            }
            else if(event_option == 6){
                accessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
            }
            else if(event_option == 7){
                String selectedAppPackageName= Functions.getSharedPref(context, Functions.APP_SETTINGS_PREF_NAME, Functions.OPEN_SELECTED_APP, "string", context.getPackageName());
                if(Functions.isAppInstalled(context, selectedAppPackageName)) {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(selectedAppPackageName != null ? selectedAppPackageName : context.getPackageName());
                    if (intent != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            }
            else if(event_option == 8){
                if (Settings.System.canWrite(context)) {
                    ContentResolver contentResolver = context.getContentResolver();
                    boolean autoRotateEnabled = Settings.System.getInt(
                            contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
                    Settings.System.putInt(
                            contentResolver, Settings.System.ACCELEROMETER_ROTATION, autoRotateEnabled ? 0 : 1);
                }
            }
            else if(event_option == 9){
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                if (notificationManager != null && notificationManager.isNotificationPolicyAccessGranted()) {
                    if (notificationManager.getCurrentInterruptionFilter() == NotificationManager.INTERRUPTION_FILTER_NONE) {
                        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                    } else {
                        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
                    }
                }
            }
            else if(event_option == 10){
                PackageManager packageManager = context.getPackageManager();
                Intent lensIntent = packageManager.getLaunchIntentForPackage("com.google.ar.lens");
                if (lensIntent != null) {
                    lensIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(lensIntent);
                } else {
                    Uri playStoreUri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.ar.lens");
                    Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, playStoreUri);
                    playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(playStoreIntent);
                }
            }
            else if(event_option == 11){
                String web_link= Functions.getSharedPref(context, Functions.APP_SETTINGS_PREF_NAME, Functions.OPEN_WEBSITE_LINK, "string", "https://www.google.com");
                if(web_link!=null && !web_link.isEmpty()) {
                    String encodedQuery = null;
                    try {
                        encodedQuery = URLEncoder.encode(web_link, "UTF-8");
                        String searchUrl = (web_link.startsWith("https://") || web_link.startsWith("http://") || web_link.startsWith("www."))
                                ? web_link : "https://www.google.com/search?q=" + encodedQuery;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage("com.android.chrome");
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Uri webpage = Uri.parse("https://www.google.com/search?q="+web_link);
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, webpage);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (intent1.resolveActivity(context.getPackageManager()) != null)
                            context.startActivity(intent1);
                    }
                }
            }
            else if(event_option == 12){
                String dial_number= Functions.getSharedPref(context, Functions.APP_SETTINGS_PREF_NAME, Functions.OPEN_DIAL_NUMBER, "string", "+919876543210");
                if(dial_number!=null && !dial_number.isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("tel:" + dial_number));
                    context.startActivity(intent);
                }
            }
            else if(event_option == 13){
                long eventtime = SystemClock.uptimeMillis();
                if (audioManager.isMusicActive()) {
                    KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE, 0);
                    audioManager.dispatchMediaKeyEvent(downEvent);

                    KeyEvent upEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PAUSE, 0);
                    audioManager.dispatchMediaKeyEvent(upEvent);
                } else {
                    KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY, 0);
                    audioManager.dispatchMediaKeyEvent(downEvent);

                    KeyEvent upEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY, 0);
                    audioManager.dispatchMediaKeyEvent(upEvent);
                }
            }
            else if(event_option == 14){
                long eventTime = SystemClock.uptimeMillis();
                if (audioManager.isMusicActive()) {
                    KeyEvent upEvent = new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT, 0);
                    audioManager.dispatchMediaKeyEvent(upEvent);
                }
            }
            else if(event_option == 15){
                long eventTime = SystemClock.uptimeMillis();
                if (audioManager.isMusicActive()) {
                    KeyEvent upEvent = new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
                    audioManager.dispatchMediaKeyEvent(upEvent);
                }
            }
            else if (event_option == 16) {
                if (Settings.System.canWrite(context)) {
                    Log.e("BRIGHTNESS_VALUE_CHECK", "System permission working...");
                    int brightness_val= Functions.getSharedPref(context, Functions.APP_SETTINGS_PREF_NAME, Functions.BRIGHTNESS_VALUE, "int", 127);
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness_val);
                    /*WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
                    layoutParams.screenBrightness = brightnessValue / 255f;
                    getWindow().setAttributes(layoutParams);*/
                }
            }
            else if (event_option == 17) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                if (notificationManager != null && notificationManager.isNotificationPolicyAccessGranted()) {
                    if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    } else {
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    }
                }
            }
            else if(event_option == 18){
                if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                } else {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                }
            } else if (event_option == 19) {
                accessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN);
            }
        }
    }

    private boolean checkScreenOrientationOption() {
        boolean isLandscapeRestrictModeEnabled = Boolean.TRUE.equals(Functions.getSharedPref(context, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_LANDSCAPE_RESTRICT_MODE_ENABLED, "boolean", false));
        if (isLandscapeRestrictModeEnabled) {
            return !(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        } else return true;
    }

    private void checkVibrationModeAndPerform() {
        if (Boolean.TRUE.equals(Functions.getSharedPref(context, Functions.APP_SETTINGS_PREF_NAME, Functions.IS_VIBRATION_MODE_ENABLED, "boolean", false))) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
        }
    }

    private boolean checkEventTouched(int val) {
        return Functions.getSharedPref(context, Functions.APP_SETTINGS_PREF_NAME, Functions.EVENT_SELECTED, "int", 1) == val;
    }

    private void createNotificationChannel() {
        CharSequence name = "My Channel";
        String description = "My Channel Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }
}
