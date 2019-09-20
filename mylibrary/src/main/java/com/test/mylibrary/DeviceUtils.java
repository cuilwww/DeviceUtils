package com.test.mylibrary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebSettings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static android.content.Context.SENSOR_SERVICE;

/**
 * @author Joe
 * @date 2019/9/18.
 * description：
 */

public class DeviceUtils implements EasyPermissions.PermissionCallbacks {


    private static DeviceUtils instance;
    private TelephonyManager tm;
    private Context act;
    DisplayMetrics metrics = new DisplayMetrics();

    private DeviceUtils(Context act) {
        tm = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
        this.act = act;

        WindowManager manager = (WindowManager) act.getSystemService(Service.WINDOW_SERVICE);
        if (manager != null) {
            manager.getDefaultDisplay().getMetrics(metrics);
        }
    }

    public static DeviceUtils getInstance(Context act) {
        if (instance == null) {
            instance = new DeviceUtils(act);
        } else if (instance.act != act) {
            instance = new DeviceUtils(act);
        }
        return instance;
    }


    /**
     * 获取设备标识
     */
    public String getDeviceId() {
        return DeviceIdUtil.getDeviceId(act);
    }

    /**
     * 获取设备型号
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 设备平台
     */
    public static String getPlatform() {
        return "Android";
    }


    /**
     * 获取设备制造商
     */
    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }


    /**
     * 获取是否绑定
     */
    public static boolean getIsBind() {
        return true;
    }


    /**
     * 获取安全状态
     */
    public static int getSafeType() {
        return RootCheck.isRoot() ? 2 : 1;

    }

    /**
     * 最后一次使用时间
     */
    public String getExitTime() {
        SharedPreferences sharedPreferences = act.getSharedPreferences("ExitTime", Context.MODE_PRIVATE);
        return sharedPreferences.getString("appExitTime", "");
    }


    /**
     * 最后一次登录时间
     */
    public String getLoginTime() {
        return "";
    }

    /**
     * 最后一次登录IP
     */
    public String getLoginIP() {
        return "";
    }


    /**
     * 屏幕宽度（像素）
     */
    public int getScreenWidth() {
        return metrics.widthPixels;
    }


    /**
     * 屏幕宽度（像素）
     */
    public int getScreenHeight() {
        return metrics.heightPixels;
    }

    /**
     * 屏幕分辨率
     */
    public String getScreenRatio() {
        return (metrics.widthPixels + "*" + metrics.heightPixels);
    }

    /**
     * 屏幕dpi
     */
    public float getScreenDPI() {
        return metrics.densityDpi;
    }

    /**
     * 屏幕比率
     * Ø	Android：
     * n	hdpi：21
     * n	ldpi：22
     * n	mdpi：23
     * n	xhdpi：24
     * n	xxhdpi：25
     * n	xxxhdpi：26
     *
     * mdpi	120dpi~160dpi
     * hdpi	160dpi~240dpi
     * xhdpi	240dpi~320dpi
     * xxhdpi	320dpi~480dpi
     * xxxhdpi	480dpi~640dpi
     */
    public int getScreenFraction() {
        if (getScreenDPI() == 0) {
            return 0;
        }
        if (120 <= getScreenDPI() && 160 > getScreenDPI()) {
            return 22;
        } else if (160 <= getScreenDPI() && 240 > getScreenDPI()) {
            return 23;
        } else if (240 <= getScreenDPI() && 320 > getScreenDPI()) {
            return 24;
        } else if (320 <= getScreenDPI() && 480 > getScreenDPI()) {
            return 25;
        } else if (480 <= getScreenDPI() && 640 > getScreenDPI()) {
            return 26;
        }
        return 0;
    }

    /**
     * 获取系统名称
     */
    public String getSysName() {

        return android.os.Build.DEVICE;


    }


    /**
     * 获取手机系统版本
     */
    public static String getSysVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取系统引擎
     */
    public String getUserAgent() {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(act);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * 获取手机MAC地址 只有手机开启wifi才能获取到mac地址
     */
    public String getMacAddress() {


        String result = "";
        WifiManager wifiManager = (WifiManager) act.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //只有手机开启wifi才能获取到mac地址
        if (null != wifiManager) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            result = wifiInfo.getMacAddress();
        }
        return result;
    }


    /**
     * 获取唯一标识Android_id
     */
    public String getAndroidId() {
        try {
            return Settings.Secure.getString(act.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }


    /**
     * 获取序列号
     */
    public static String getSERIAL() {
        try {
            return Build.SERIAL;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获取设备名称
     */
    public String getPhoneName() {
        return Settings.Global.getString(act.getContentResolver(), Settings.Global.DEVICE_NAME);
    }


    /**
     * 获取手机号码
     */
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getPhoneNumber() {

        boolean isHave = PermissionsLogUtils.easyCheckPermissions(act,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE);

        return !isHave ? "" : (tm == null ? "" : tm.getLine1Number());


    }


    /**
     * 获取运营商
     *
     * @return 运营商名字
     */
    public String getOperator() {


        String providersName = "";
        String IMSI = getIMSI();
        if (IMSI != null) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46004") || IMSI.startsWith("46007") || IMSI.startsWith("46008")) {
                providersName = "中国移动";
            } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006") || IMSI.startsWith("46009")) {
                providersName = "中国联通";
            } else if (IMSI.startsWith("46003") || IMSI.startsWith("46005") || IMSI.startsWith("46011")) {
                providersName = "中国电信";
            }
            return providersName;
        } else {
            return "";
        }
    }

    /**
     * 获取运营商代码（IMSI）
     */
    public String getIMSI() {

        boolean isHave = PermissionsLogUtils.easyCheckPermissions(act,
                Manifest.permission.READ_PHONE_STATE);
        return !isHave ? "" : (tm == null ? "" : tm.getSubscriberId());
    }


    /**
     * 判断是否存在光传感器来判断是否为模拟器
     * 部分真机也不存在温度和压力传感器。其余传感器模拟器也存在。
     *
     * @return true 为模拟器
     */
    public boolean getIsNotHasLightSensorManager() {
        SensorManager sensorManager = (SensorManager) act.getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
        Sensor sensor8 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //光
        return null == sensor8;
    }

    /**
     * 是否模拟器
     *
     * @return true 为模拟器
     */
    public boolean getIsEmulator() {
        String url = "tel:" + "123456";
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_DIAL);
        // 是否可以处理跳转到拨号的 Intent
        boolean canResolveIntent = intent.resolveActivity(act.getPackageManager()) != null;
        boolean isEmulator = Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.SERIAL.equalsIgnoreCase("unknown")
                || Build.SERIAL.equalsIgnoreCase("android")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)
                || ((TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName().toLowerCase().equals("android")
                || !canResolveIntent;
        return (isEmulator || getIsNotHasLightSensorManager());
    }


    /**
     * 获取是否横屏
     */
    public boolean getIsLANDSCAPE() {

        Configuration mConfiguration = act.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        return (ori == mConfiguration.ORIENTATION_LANDSCAPE);
    }


    /**
     * 判断是否是 平板 还是 手机
     *
     * @return true : 平板 ; false : 手机
     */
    public boolean getIsTablet() {
        return (act.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    /**
     * 获取应用包名
     */
    public String getPackageName() {
        return act.getPackageName();
    }


    /**
     * 判断是否设置登录标识
     */
    public boolean getIsPasswordLock() {
        KeyguardManager keyguardManager = (KeyguardManager) act.getSystemService(Context.KEYGUARD_SERVICE);
        assert keyguardManager != null;
        return keyguardManager.isKeyguardSecure();
    }


    /**
     * 判断是否设置面容标识
     */
    public boolean getIsFaced() {
        return false;
    }


    //当权限被成功申请的时候执行回调，requestCode是代表你权限请求的识别码，list里面装着申请的权限的名字：
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }


    //当权限申请失败的时候执行的回调，参数意义同上
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    //在里面调用EasyPermissions.onRequestPermissionsResult()，实现回调。
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}

