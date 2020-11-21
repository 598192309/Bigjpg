package com.bigjpg.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * LocalBroadcastUtil
 * @author Momo
 * @date 2016-09-18 15:19
 */
public class LocalBroadcastUtil {

    public static void sendBroadcast(Context context, String action){
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(action));
    }

    public static void sendBroadcast(Context context, Intent intent){
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void registerReceiver(Context context, BroadcastReceiver receiver, IntentFilter intentFilter){
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);
    }

    public static void unregisterReceiver(Context context, BroadcastReceiver receiver){
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

}
