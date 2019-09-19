package com.test.mylibrary;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Joe
 * @date 2019/9/18.
 * description：
 */
public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, CheckExitService.class);
        getApplicationContext().startService(intent);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.d("MyApplication: ", "onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d("MyApplication: ", "onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d("MyApplication: ", "onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d("MyApplication: ", "onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d("MyApplication: ", "onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.d("MyApplication: ", "onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d("MyApplication: ", "onActivityDestroyed");
    }
}

