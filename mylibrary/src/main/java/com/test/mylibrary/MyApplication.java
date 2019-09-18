package com.test.mylibrary;

import android.app.Application;
import android.content.Intent;

/**
 * @author Joe
 * @date 2019/9/18.
 * description：
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent=new Intent(this,CheckExitService.class);
        getApplicationContext().startService(intent);
    }
}

