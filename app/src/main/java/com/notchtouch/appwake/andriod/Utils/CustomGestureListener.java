package com.notchtouch.appwake.andriod.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;

public class CustomGestureListener implements View.OnTouchListener {
    private final GestureDetector gestureDetector;
    private GestureListener gestureListener;

    public static int FLING_DISTANCE_THRESHOLD = 100;
    public static int SWIPE_DISTANCE_THRESHOLD = 30;
    public static int VELOCITY_THRESHOLD = 1;

    public interface GestureListener {
        void onDoubleTap();
        void onLongPress();
        void onSingleTap();
        void onSwipeLeft();
        void onSwipeRight();
    }

    public CustomGestureListener(Context context, int swipeDistanceThreshold) {
        this.gestureDetector = new GestureDetector(context, new CustomGestureDetector());
        FLING_DISTANCE_THRESHOLD = swipeDistanceThreshold / 2;
        SWIPE_DISTANCE_THRESHOLD = swipeDistanceThreshold / 3;
    }

    public void setGestureListener(GestureListener listener) {
        this.gestureListener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            return this.gestureDetector.onTouchEvent(motionEvent);
        }
        return false;
    }

    private class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(@NonNull MotionEvent motionEvent) {
            if (gestureListener != null) {
                gestureListener.onDoubleTap();
            }
            return true;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent motionEvent) {
            if (gestureListener != null) {
                gestureListener.onLongPress();
            }
        }

        @Override
        public boolean onSingleTapConfirmed(@NonNull MotionEvent motionEvent) {
            if (gestureListener != null) {
                gestureListener.onSingleTap();
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            try {
                float deltaX = e2.getX() - e1.getX();
                float deltaY = e2.getY() - e1.getY();
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (Math.abs(deltaX) <= FLING_DISTANCE_THRESHOLD || Math.abs(velocityX) <= VELOCITY_THRESHOLD) {
                        return false;
                    }

                    if (deltaX > 0.0f) {
                        if (gestureListener != null) {
                            gestureListener.onSwipeRight();
                            return true;
                        } else return false;
                    } else {
                        if (gestureListener != null) {
                            gestureListener.onSwipeLeft();
                            return true;
                        } else return false;
                    }
                }
                else return false;
            } catch (Exception e4) {
                e4.printStackTrace();
                return false;
            }
        }
    }
}
