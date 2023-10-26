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
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.notchtouch.appwake.andriod.Activities.ActionOptionsActivity;
import com.notchtouch.appwake.andriod.Services.MyAccessibilityService;

public class CustomGestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private static final String CHANNEL_ID = "my_channel";
    private CameraManager cameraManager;
    private String cameraId;
    private boolean isFlashOn = false;

    Context context;
    MyAccessibilityService accessibilityService;

    public CustomGestureListener(Context context, MyAccessibilityService accessibilityService) {
        this.context= context;
        this.accessibilityService= accessibilityService;
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
        if(checkEventTouched(2)){
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
                    if(checkEventTouched(5)){
                        runSelectedActionOption();
                        Log.e("listener_check", "Swipe Right Worked....");
                        return true;
                    }
                } else {
                    if(checkEventTouched(4)){
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
        if(checkEventTouched(1)){
            runSelectedActionOption();
            Log.e("listener_check", "Single Tap Worked....");
            return true;
        }
        return false;
    }

    @Override
    public boolean onDoubleTap(@NonNull MotionEvent motionEvent) {
        if(checkEventTouched(3)){
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
                                boolean currentFlashStatus= Functions.isFlashLightOn(context, cameraId);
                                cameraManager.setTorchMode(cameraId, !currentFlashStatus);
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
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (intent != null) {
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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("https://lens.google/#mode/search"));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "No QR Code Reader installed in your device !! Please install a qr code reader to use this feature. ", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean checkEventTouched(int val){
        return Functions.getSharedPref(context, Functions.APP_SETTINGS_PREF_NAME, Functions.EVENT_SELECTED, "int", 1)==val;
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
