package com.bigjpg.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.bigjpg.application.BigJPGApplication;
import com.bigjpg.model.constant.AppConstants;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 描述:应用工具类
 *
 * @author mfx
 */
public class AppUtil {

    /**
     * 获取当前版本号
     *
     * @param context
     * @return versionCode
     */
    public static int getCurVersion(Context context) {
        try {
            PackageInfo pinfo;
            pinfo = context.getPackageManager().getPackageInfo(AppConstants.PACKAGE_NAME, PackageManager.GET_CONFIGURATIONS);
            int versionCode = pinfo.versionCode;
            return versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 获取当前版本号
     *
     * @param context
     * @return versionCode
     */
    public static String getCurVersionName(Context context) {
        try {
            PackageInfo pinfo;
            pinfo = context.getPackageManager().getPackageInfo(AppConstants.PACKAGE_NAME, PackageManager.GET_CONFIGURATIONS);
            String versionName = pinfo.versionName;
            return versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return "1.0";
    }

    /**
     * 获取版本Tag
     *
     * @param context
     * @return
     */
    public static String getCurVersionTagName(Context context) {
        String result = getCurVersionName(context);
        result = result.replace(".", "_");
        return result;
    }


    /**
     * 获取AndroidManifest.xml中的meta_date值 ,规定是string类型
     *
     * @param context
     * @param key
     * @return
     */
    public static String getMetaData(Context context, String key) {
        String metaData = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            metaData = bundle.getString(key);
        } catch (NameNotFoundException e) {
        }
        return metaData;
    }

    /**
     * 获取应用名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        String appName = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            appName = context.getString(ai.labelRes);
        } catch (NameNotFoundException e) {
        }
        return appName;
    }

    /**
     * 获取App安装包信息
     *
     * @param context
     * @return
     */
    public PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 应用是否被隐藏<在后台运行>
     *
     * @param context
     * @return
     */
    public static boolean isRunOnBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        try {
            if (context.getPackageName().equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return isRunOnBackground2(context);
            }
        } catch (Exception e) {
        }
        return true;
    }

    /**
     * 兼容getRunningTasks失效的方案
     *
     * @param context
     * @return
     */
    private static boolean isRunOnBackground2(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.processName != null && processInfo.processName.startsWith(AppConstants.PACKAGE_NAME)) {
                if (processInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<ActivityManager.RunningAppProcessInfo> getRunningProcessName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = activityManager.getRunningAppProcesses();
        return runningAppProcessInfo;
    }

    public static boolean isServiceRunning(Context context, String serviceClassName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        try {
            if (services != null) {
                for (ActivityManager.RunningServiceInfo serviceInfo : services) {
                    if (serviceClassName.equals(serviceInfo.service.getClassName())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断是否锁屏
     *
     * @return true 锁屏状态， false 非锁屏状态
     */
    public static boolean isScreenOff(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Activity.KEYGUARD_SERVICE);
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }

    private static DisplayMetrics sDisplayMetrics;

    /**
     * dip转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dipToPx(Context context, int dipValue) {
        if (sDisplayMetrics == null) {
            sDisplayMetrics = context.getResources().getDisplayMetrics();
        }
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, sDisplayMetrics);
    }

    /**
     * 转换px为dip
     *
     * @param context
     * @param px
     * @return
     */
    public static int pxToDip(Context context, int px) {
        if (sDisplayMetrics == null) {
            sDisplayMetrics = context.getResources().getDisplayMetrics();
        }
        float scale = sDisplayMetrics.density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        if (sDisplayMetrics == null) {
            sDisplayMetrics = context.getResources().getDisplayMetrics();
        }
        float scale = sDisplayMetrics.density;
        return (int) (spValue * scale + 0.5f);
    }


    /**
     * 调用外部浏览器
     *
     * @param url
     */
    public static void callOuterBrowser(Context context, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 复制到剪贴板
     *
     * @param context
     * @param clipLabel
     * @param copyText
     */
    public static void copyToClip(Context context, String clipLabel, CharSequence copyText) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboardService = (android.text.ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardService.setText(copyText);
            } else {
                android.content.ClipboardManager clipboardService = (android.content.ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(clipLabel, copyText);
                clipboardService.setPrimaryClip(clipData);
            }
        } catch (Exception e) {
        }
    }


    private static int statusBarHeight = -1;

    /**
     * 获取状态栏高度
     *
     * @param conext
     * @return 成功返回状态栏高度，失败返回-1
     */
    public static int getScreenStatusBarHeight(Context conext) {
        if (statusBarHeight != -1)
            return statusBarHeight;
        Class<?> c;
        Object obj;
        Field field;
        int x, sbar;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = conext.getResources().getDimensionPixelSize(x);
            statusBarHeight = sbar;
            return sbar;
        } catch (Exception e) {
            LogUtil.w(" get status bar height fail");
            e.printStackTrace();
            return -1;
        }
    }

    public static void shareText(Context context, String title , String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chooserIntent = Intent.createChooser(intent, title);
        if (ActivityUtil.isActivityExist(context, chooserIntent)) {
            context.startActivity(chooserIntent);
        }
    }

    public static String getBigjpgFilePath() {
        String result = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + File.separator + "bigjpg";
        FileUtil.createFolder(result, FileUtil.MODE_UNCOVER);
        return result;
    }

    public static String getBijpgSimpleFilePath(){
        return "bigjpg";
    }

    public static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static boolean hasRequiredPermission(){
        return EasyPermissions.hasPermissions(BigJPGApplication.getInstance(), AppUtil.REQUIRED_PERMISSIONS);
    }

    /**
     * 获取屏幕大小
     *
     * @param context
     * @return 竖屏情况下 [0] width, [1] height ，横屏则相反
     */
    public static int[] getScreenSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        return new int[]{
                display.getWidth(), display.getHeight()
        };
    }

}
