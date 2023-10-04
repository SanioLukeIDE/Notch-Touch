package com.notchtouch.appwake.andriod.CustomLayouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.notchtouch.appwake.andriod.Utils.Functions;

public class FullscreenOverlayLayout extends FrameLayout {

    public FullscreenOverlayLayout(Context context) {
        super(context);
    }

    public FullscreenOverlayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullscreenOverlayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        @SuppressLint("DrawAllocation") Point screenSize = new Point();
        windowManager.getDefaultDisplay().getRealSize(screenSize);

        super.onMeasure(Functions.getNotchWidth(getContext()), Functions.getNotchHeight(getContext()));
    }
}