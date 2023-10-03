package com.notchtouch.appwake.andriod.CustomLayouts;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class OverlayDrawingView extends View {

    public OverlayDrawingView(Context context) {
        this(context, null);
    }

    public OverlayDrawingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverlayDrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}