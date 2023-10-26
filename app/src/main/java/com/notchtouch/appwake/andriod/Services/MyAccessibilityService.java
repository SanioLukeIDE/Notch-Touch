package com.notchtouch.appwake.andriod.Services;

import static com.notchtouch.appwake.andriod.Utils.Functions.getStoredNotchLeft;
import static com.notchtouch.appwake.andriod.Utils.Functions.getStoredNotchTop;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import androidx.core.app.NotificationCompat;

import com.notchtouch.appwake.andriod.R;
import com.notchtouch.appwake.andriod.Utils.Functions;

public class MyAccessibilityService extends AccessibilityService {

    private static final String CHANNEL_ID = "MyForegroundServiceChannel";
    private static final int NOTIFICATION_ID = 1;

    private View overlay;
    private WindowManager windowManager;
    GestureDetector gestureDetector;

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.e("accessibilityservice_check", "Accessibility Service connected....");
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("My Foreground Service")
                .setContentText("Running in the foreground")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
        startForeground(NOTIFICATION_ID, notification);

        // performGlobalAction(GLOBAL_ACTION_POWER_DIALOG);
        overlay = View.inflate(getApplicationContext(), R.layout.overlay_service_layout, null);

        /*CustomGestureListener customGestureListener = new CustomGestureListener(this, 200);
        customGestureListener.setGestureListener(new CustomGestureListener.GestureListener() {
            @Override
            public void onDoubleTap() {
                Log.e("notchservice_check", "Notch Service - Button on double tapped.....");
            }

            @Override
            public void onLongPress() {
                Log.e("notchservice_check", "Notch Service - Button on long pressed.....");
            }

            @Override
            public void onSingleTap() {
                Log.e("notchservice_check", "Notch Service - Button Clicked.....");
            }

            @Override
            public void onSwipeLeft() {
                Log.e("notchservice_check", "Notch Service - Button left swiped.....");
            }

            @Override
            public void onSwipeRight() {
                Log.e("notchservice_check", "Notch Service - Button right swiped.....");
            }
        });
        overlay.setOnTouchListener((v, event) -> {
            customGestureListener.onTouch(v, event);
            return true;
        });*/

        overlay.setOnClickListener(v->{
            Log.e("notchservice_check", "Notch Service - Button clicked.....");
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_TAKE_SCREENSHOT);
        });

        Log.e("notch_width", "The notch width is : "+Functions.getStoredNotchWidth(this));
        Log.e("notch_width", "The notch height is : "+Functions.getStoredNotchHeight(this));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                Functions.getStoredNotchWidth(this),
                Functions.getStoredNotchHeight(this),
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
        layoutParams.x= getStoredNotchLeft(this);
        layoutParams.y= getStoredNotchTop(this);
        overlay.setBackgroundColor(Color.GREEN);
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, layoutParams);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlay != null && windowManager != null) windowManager.removeView(overlay);
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