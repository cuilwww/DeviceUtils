package com.test.mylibrary;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
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
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebSettings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

import static android.content.Context.SENSOR_SERVICE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;

/**
 * @author Joe
 * @date 2019/9/18.
 * description：
 */
public class DeviceUtils {


    private static DeviceUtils instance;
    private TelephonyManager tm;
    private Activity act;
    DisplayMetrics metrics = new DisplayMetrics();

    private DeviceUtils(Activity act) {
        tm = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
        this.act = act;

        WindowManager manager = (WindowManager) act.getSystemService(Service.WINDOW_SERVICE);
        if (manager != null) {
            manager.getDefaultDisplay().getMetrics(metrics);
        }
    }

    public static DeviceUtils getInstance(Activity act) {
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
     */
    public String getScreenFraction() {
        float density = metrics.density;      // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        int screenWidth = (int) (metrics.widthPixels * density + 0.5f);      // 屏幕宽（px，如：480 px）
        int screenHeight = (int) (metrics.heightPixels * density + 0.5f);     // 屏幕高（px，如：800px）
        return (screenWidth + ":" + screenHeight);
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
    private  String getUserAgent() {
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
        WifiManager wifiManager = (WifiManager) act.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        result = wifiInfo.getMacAddress();
        return result;
    }


    /**
     * 获取唯一标识Android_id
     */
    private String getAndroidId() {
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
    private static String getSERIAL() {
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
    public String getPhoneNumber() {

        return tm == null ? null : tm.getLine1Number();
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
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                providersName = "中国移动";
            } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
                providersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
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
        return tm == null ? null : tm.getSubscriberId();
    }


    /**
     * 判断是否存在光传感器来判断是否为模拟器
     * 部分真机也不存在温度和压力传感器。其余传感器模拟器也存在。
     *
     * @return true 为模拟器
     */
    public boolean notHasLightSensorManager() {
        SensorManager sensorManager = (SensorManager) act.getSystemService(SENSOR_SERVICE);
        Sensor sensor8 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //光
        if (null == sensor8) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否模拟器
     *
     * @return true 为模拟器
     */
    public boolean isEmulator() {
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
        return isEmulator || notHasLightSensorManager();
    }


    /**
     * 获取是否竖屏
     */
    public boolean getIsPortrait() {

        Configuration mConfiguration = act.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        return (ori == mConfiguration.ORIENTATION_PORTRAIT);
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
    public boolean getIsSecured() {
        boolean isSecured = false;
        String classPath = getPackageName() + ".DeviceUtils";
        try {
            Class<?> lockPatternClass = Class.forName(classPath);
            Object lockPatternObject = lockPatternClass.getConstructor(Context.class).newInstance(act);
            Method method = lockPatternClass.getMethod("isSecure");
            isSecured = (Boolean) method.invoke(lockPatternObject);
        } catch (Exception e) {
            isSecured = false;
        }
        return isSecured;
    }


    /**
     * 判断是否设置面容标识
     */
    public boolean getIsFaced() {
        return false;
    }

}

