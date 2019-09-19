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

        //App要退出了
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        SharedPreferences sharedPreferences = getSharedPreferences("ExitTime", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("appExitTime", simpleDateFormat.format(date));
        editor.apply();

        Log.d("onCreate: ", simpleDateFormat.format(date));

//        Intent intent = new Intent(this, CheckExitService.class);
//        getApplicationContext().startService(intent);
    }
}

