package com.notchtouch.appwake.andriod.Services;

import static com.notchtouch.appwake.andriod.Utils.Functions.getStoredNotchLeft;
import static com.notchtouch.appwake.andriod.Utils.Functions.getStoredNotchTop;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Utils.CustomGestureListener;
import com.notchtouch.appwake.andriod.Utils.Functions;

public class MyAccessibilityService extends AccessibilityService {

    private static final String CHANNEL_ID = "MyForegroundServiceChannel";
    private static final int NOTIFICATION_ID = 1;

    private View overlay;
    private WindowManager windowManager;
    private GestureDetector gd;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    @SuppressLint({"ClickableViewAccessibility", "UnspecifiedRegisterReceiverFlag"})
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("My Foreground Service")
                .setContentText("Running in the foreground")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
        startForeground(NOTIFICATION_ID, notification);

        overlay = View.inflate(getApplicationContext(), R.layout.overlay_service_layout, null);

        CustomGestureListener customGestureListener = new CustomGestureListener(getApplicationContext(), this);
        GestureDetector gd = new GestureDetector(this, customGestureListener);
        gd.setOnDoubleTapListener(customGestureListener);
        overlay.setOnTouchListener((view, motionEvent) -> gd.onTouchEvent(motionEvent));

        boolean isStatusBar = Boolean.TRUE.equals(Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.IS_TOUCH_STATUS_BAR, "boolean", false));
        int width= isStatusBar ? WindowManager.LayoutParams.MATCH_PARENT : Functions.getStoredNotchWidth(this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                width, Functions.getStoredNotchHeight(this),
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
        );
        layoutParams.gravity= Gravity.TOP | Gravity.START;
        if(!isStatusBar) layoutParams.x= getStoredNotchLeft(this);
        layoutParams.y= getStoredNotchTop(this);
        overlay.setBackgroundColor(Color.GREEN);
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, layoutParams);

        IntentFilter intentFilter = new IntentFilter("com.notchtouch.appwake.andriod.ACTION_SETTINGS_CHANGED");
        registerReceiver(settingsChangedReceiver, intentFilter);
    }

    private BroadcastReceiver settingsChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateWindowManagerWidth();
        }
    };

    private void updateWindowManagerWidth() {
        boolean isStatusBar = Boolean.TRUE.equals(Functions.getSharedPref(getApplicationContext(), Functions.APP_SETTINGS_PREF_NAME, Functions.IS_TOUCH_STATUS_BAR, "boolean", false));
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) overlay.getLayoutParams();
        layoutParams.width = isStatusBar ? WindowManager.LayoutParams.MATCH_PARENT : Functions.getStoredNotchWidth(this);
        layoutParams.x= (isStatusBar) ? 0 : getStoredNotchLeft(this);
        windowManager.updateViewLayout(overlay, layoutParams);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlay != null && windowManager != null) windowManager.removeView(overlay);
        if (settingsChangedReceiver != null) {
            unregisterReceiver(settingsChangedReceiver);
            settingsChangedReceiver = null;
        }
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "My Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }
}