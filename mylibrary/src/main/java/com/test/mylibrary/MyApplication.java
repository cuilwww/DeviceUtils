package com.test.mylibrary;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Joe
 * @date 2019/9/18.
 * description：
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();




        Intent intent = new Intent(this, CheckExitService.class);
        getApplicationContext().startService(intent);
    }
}

