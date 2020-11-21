
package com.bigjpg.util;

import android.util.Log;

/** 
 *  描述:调试工具类
 *
 * @author mfx
 */
public class LogUtil {

    public static final String TAG = "custom_log";

    private static boolean isDebug = false;
    
    private static boolean isStrictMode = false;

    /**
     * 是否处于调试模式
     */
    public static boolean isDebug() {
        return isDebug;
    }

    public static boolean isStrictMode(){
        return isStrictMode;
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void v(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void w(String msg) {
        if (isDebug) {
            Log.w(TAG, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }
}
