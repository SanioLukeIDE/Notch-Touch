package com.notchtouch.appwake.andriod.SingletonClasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.adsmodule.api.adsModule.AdUtils;
import com.adsmodule.api.adsModule.interfaces.AppInterfaces;
import com.adsmodule.api.adsModule.retrofit.APICallHandler;
import com.adsmodule.api.adsModule.retrofit.AdsDataRequestModel;
import com.adsmodule.api.adsModule.retrofit.AdsResponseModel;
import com.adsmodule.api.adsModule.utils.Constants;
import com.notchtouch.appwake.andriod.Activities.SplashScreenActivity;


public class AppOpenAds implements LifecycleObserver, Application.ActivityLifecycleCallbacks {


    @SuppressLint("StaticFieldLeak")
    public static Activity activity;
    MyApplication application;
    boolean isAdShowing;
    Bundle bundle;


    public AppOpenAds(MyApplication application) {
        this.application = application;
        application.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        isAdShowing = false;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        AppOpenAds.activity = activity;
        bundle = savedInstanceState;

    }

    @Override
    public void onActivityStarted(Activity activity) {
        AppOpenAds.activity = activity;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        if (Constants.adsResponseModel != null && Constants.adsResponseModel.isShow_ads()) {
            if (!isAdShowing && AppOpenAds.activity != null &&
                    !AppOpenAds.activity.getClass().getName().equals(SplashScreenActivity.class.getName())) {
                isAdShowing = true;

                AdUtils.showAppOpenAds(Constants.adsResponseModel.getApp_open_ads().getAdx(), AppOpenAds.activity, state_load -> {
                    isAdShowing = false;
                });

            } else {
                isAdShowing = false;
            }
        }

    }

    @Override
    public void onActivityResumed(Activity activity) {

        AppOpenAds.activity = activity;
        if (!activity.getComponentName().getClassName().equals("com.google.android.gms.ads.AdActivity")) {
            isConnected().observe((LifecycleOwner) activity, isConnected -> {
                if (Constants.adsResponseModel.getApp_name().isEmpty() && isConnected) {

                    APICallHandler.callAdsApi(activity, Constants.MAIN_BASE_URL, new AdsDataRequestModel(activity.getPackageName(), ""), new AppInterfaces.AdDataInterface() {
                        @Override
                        public void getAdData(AdsResponseModel adsResponseModel) {
                            if (adsResponseModel != null) {
                                Constants.adsResponseModel = adsResponseModel;
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    public static LiveData<Boolean> isConnected() {
        MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isConnected.postValue(MyApplication.getConnectionStatus().isConnectingToInternet());
                handler.postDelayed(this, 1000);
            }
        }, 1000);
        return isConnected;
    }

}