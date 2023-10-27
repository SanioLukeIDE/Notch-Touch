package com.notchtouch.appwake.andriod.SingletonClasses;

import static com.adsmodule.api.adsModule.retrofit.APICallHandler.callAppCountApi;

import android.app.Application;

import com.adsmodule.api.adsModule.preferencesManager.AppPreferences;
import com.adsmodule.api.adsModule.retrofit.AdsDataRequestModel;
import com.adsmodule.api.adsModule.utils.ConnectionDetector;
import com.adsmodule.api.adsModule.utils.Constants;
import com.adsmodule.api.adsModule.utils.Global;

public class MyApplication extends Application {

    static AppPreferences preferences;
    private static MyApplication app;
    private static ConnectionDetector cd;

    public static AppPreferences getPreferences() {
        if (preferences == null)
            preferences = new AppPreferences(app);
        return preferences;
    }

    public static ConnectionDetector getConnectionStatus() {
        if (cd == null) {
            cd = new ConnectionDetector(app);
        }
        return cd;
    }

    public static synchronized MyApplication getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        AppPreferences preferences = new AppPreferences(app);
        if (preferences.isFirstRun()) {
            callAppCountApi(Constants.MAIN_BASE_URL, new AdsDataRequestModel(app.getPackageName(), Global.getDeviceId(app)), () -> {
                preferences.setFirstRun(false);
            });
        }

        new AppOpenAds(app);

    }
}

