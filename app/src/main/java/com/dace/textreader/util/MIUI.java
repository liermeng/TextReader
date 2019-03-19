package com.dace.textreader.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * =============================================================================
 * Copyright (c) 2018 Administrator All rights reserved.
 * Packname com.dace.textreader.util
 * Created by Administrator.
 * Created time 2018/8/22 0022 下午 5:44.
 * Version   1.0;
 * Describe :
 * History:
 * 需要清楚：一个MIUI版本对应小米各种机型，基于不同的安卓版本，但是权限设置页跟MIUI版本有关
 * 测试TYPE_TOAST类型
 * 7.0:
 * 小米      5        MIUI8         -------------------- 失败
 * 小米   Note2       MIUI9         -------------------- 失败
 * 6.0.1:
 * 小米   5                         -------------------- 失败
 * 小米   红米note3                 -------------------- 失败
 * 6.0:
 * 小米   5                         -------------------- 成功
 * 小米   红米4A      MIUI8         -------------------- 成功
 * 小米   红米Pro     MIUI7         -------------------- 成功
 * 小米   红米Note4   MIUI8         -------------------- 失败
 * 经过各种横向纵向测试对比，得出一个结论，就是小米对TYPE_TOAST的处理机制毫无规律可言！
 * 跟Android版本无关，跟MIUI版本无关，addView方法也不报错
 * 所以最后对小米6.0以上的适配方法是：不使用 TYPE_TOAST 类型，统一申请权限
 * ==============================================================================
 */
public class MIUI {

    private static final String miui = "ro.miui.ui.version.name";
    private static final String miui5 = "V5";
    private static final String miui6 = "V6";
    private static final String miui7 = "V7";
    private static final String miui8 = "V8";
    private static final String miui9 = "V9";

    public static boolean rom() {
        return Build.MANUFACTURER.equals("Xiaomi");
    }

    private static String getProp() {
        return Rom.getProp(miui);
    }

    /**
     * 跳转请求悬浮窗权限
     */
    public static void req(final Context context) {
        switch (getProp()) {
            case miui5:
                reqForMiui5(context);
                break;
            case miui6:
            case miui7:
                reqForMiui67(context);
                break;
            case miui8:
            case miui9:
                reqForMiui89(context);
                break;
            default:
                reqForMiui89(context);
                break;
        }
    }

    private static void reqForMiui5(Context context) {
        String packageName = context.getPackageName();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Rom.isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        }
    }

    private static void reqForMiui67(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.AppPermissionEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Rom.isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        }
    }

    private static void reqForMiui89(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Rom.isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setPackage("com.miui.securitycenter");
            intent.putExtra("extra_pkgname", context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Rom.isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            }
        }
    }

}