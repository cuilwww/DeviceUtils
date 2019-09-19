package com.test.mylibrary;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author Joe
 * @date 2019/9/19.
 * description：
 */
public class PermissionsLogUtils {
    private static StringBuffer logStringBuffer = new StringBuffer();
    // 查看权限是否已申请
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String checkPermissions(Context context, String... permissions) {
        logStringBuffer.delete(0,logStringBuffer.length());
        for (String permission : permissions) {
            logStringBuffer.append(permission);
            logStringBuffer.append(" is applied? \n     ");
            logStringBuffer.append(isAppliedPermission(context,permission));
            logStringBuffer.append("\n\n");
        }
        return logStringBuffer.toString();
    }


    //使用EasyPermissions查看权限是否已申请
    public static String easyCheckPermissions(Context context,String ... permissions) {
        logStringBuffer.delete(0,logStringBuffer.length());
        for (String permission : permissions) {

            logStringBuffer.append(permission);
            logStringBuffer.append(" is applied? \n     ");
            logStringBuffer.append(EasyPermissions.hasPermissions(context,permission));
            logStringBuffer.append("\n\n");
        }
        return logStringBuffer.toString();
    }


    // 查看权限是否已申请
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean isAppliedPermission(Context context, String permission) {
        return context.checkSelfPermission(permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

}
