package com.test.mylibrary;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Joe
 * @date 2019/9/18.
 * description：
 */
public class CheckExitService extends Service {

//    private String packageName = DeviceUtils.getInstance(geta()).getPackageName;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        //App要退出了
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());

        SharedPreferences sharedPreferences = getSharedPreferences("ExitTime", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("appExitTime", simpleDateFormat.format(date));
        editor.apply();

    }

    //service异常停止的回调
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ActivityManager activtyManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activtyManager.getRunningAppProcesses();
        for (int i = 0; i < runningAppProcesses.size(); i++) {
//            if (packageName.equals(runningAppProcesses.get(i).processName)) {
//                Toast.makeText(this, "app还在运行中", Toast.LENGTH_LONG).show();
//            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

