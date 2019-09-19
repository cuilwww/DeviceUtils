package com.test.mylibrary;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.test.httputils.HttpHeper;
import com.test.httputils.callback.CommonCallBack;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author Joe
 * @date 2019/9/18.
 * description：
 */
public class MyApplication extends Application {
    HashMap<String, Object> data = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

        getData();
//        upDeviceInfo();


    }

    private void getData() {

        data.put("createTime", "");//
        data.put("delFlag", false);//
        data.put("updateTime", "");
        data.put("bindingFlag", false);//
        data.put("deviceId", DeviceUtils.getInstance(this).getAndroidId());
        data.put("deviceMark", DeviceUtils.getInstance(getApplicationContext()).getDeviceId());//设备标识
        data.put("deviceModel", DeviceUtils.getModel());//设备型号
        data.put("deviceName", DeviceUtils.getInstance(this).getPhoneName());//设备名称
        data.put("deviceType", DeviceUtils.getPlatform());
        data.put("identification", DeviceUtils.getInstance(this).getAndroidId());
        data.put("landscapeFlag", DeviceUtils.getInstance(this).getIsLANDSCAPE());
        data.put("lastLoginAddr", DeviceUtils.getInstance(this).getLoginIP());
        data.put("lastLoginTime", DeviceUtils.getInstance(this).getLoginTime());
        data.put("lastUseTime", DeviceUtils.getInstance(this).getExitTime());
        data.put("macAddr", DeviceUtils.getInstance(this).getMacAddress());
        data.put("manufacture", DeviceUtils.getManufacturer());
        data.put("mobileNumber", DeviceUtils.getInstance(this).getPhoneNumber());
        data.put("operatorCode", DeviceUtils.getInstance(this).getIMSI());
        data.put("operatorName", DeviceUtils.getInstance(this).getOperator());
        data.put("safeType", DeviceUtils.getSafeType());
        data.put("scrnDpi", DeviceUtils.getInstance(this).getScreenDPI());
        data.put("scrnHeight", DeviceUtils.getInstance(this).getScreenHeight());
        data.put("scrnWidth", DeviceUtils.getInstance(this).getScreenWidth());
        data.put("scrnRate", DeviceUtils.getInstance(this).getScreenFraction());
        data.put("scrnResolution", DeviceUtils.getInstance(this).getScreenRatio());
        data.put("serial", DeviceUtils.getSERIAL());
        data.put("setFacePrintFlag", DeviceUtils.getInstance(this).getIsFaced());
        data.put("setFingerPrintFlag", false);
        data.put("setPinNumberFlag", DeviceUtils.getInstance(this).getIsPasswordLock());
        data.put("simulatorFlag", DeviceUtils.getInstance(this).getIsEmulator());
        data.put("systemAgent", DeviceUtils.getInstance(this).getUserAgent());
        data.put("systemName", DeviceUtils.getInstance(this).getSysName());
        data.put("systemVersion", DeviceUtils.getSysVersion());
        data.put("tabletFlag", DeviceUtils.getInstance(this).getIsTablet());


        Log.d("MyApplication", "getData: "+data);
    }

    private void upDeviceInfo() {
        HttpHeper.get().setBaseUrl("https://dev.swagger.madp.xyz/admin");
        HttpHeper.get().create(UpService.class).deviceCollect(data)
                .compose(upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()))
                .compose(RxLifecycleAndroid.bindActivity(BehaviorSubject.create()))
                .subscribe(new CommonCallBack<Object>(true, this) {
                    @Override
                    public void onCallBackSuccess(Object data) {
                    }
                });
    }


}

