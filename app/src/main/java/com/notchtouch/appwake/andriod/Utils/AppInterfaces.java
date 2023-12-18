package com.notchtouch.appwake.andriod.Utils;

public class AppInterfaces {

    public interface NotchInfoCallback {
        void onNotchInfoAvailable(int notchLeft, int notchTop, int notchRight, int notchBottom);
        void onNoNotch();
    }

    public interface AccessibilityInterface{
        void getResult(boolean result);
    }

}
