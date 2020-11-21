package com.bigjpg.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.bigjpg.R;


/**
 * 描述:AppToast
 *
 * @author mofx
 * @date 2015-10-26 下午5:15:37
 */
public class AppToast {

    private AppToast() {
    }

    /**
     * Duration: Toast.LENGTH_SHORT(Default)
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, int msgId) {
        showToast(context, msgId, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String msg, int duration) {
        try {
            showSystemToast(context, msg, duration);
        } catch (Exception e) {
        }
    }

    public static void showToast(Context context, int msgId, int duration) {
        try {
            showSystemToast(context, context.getString(msgId), duration);
        } catch (Exception e) {
        }
    }

    private static void showSystemToast(Context context, String msg, int duration) {
        Context applicationContext = context.getApplicationContext();
        android.widget.Toast toast = ToastCompat.makeText(applicationContext, msg, duration);
        toast.setGravity(Gravity.BOTTOM, 0, ResourcesUtil.getDimensionPixelSize(context, R.dimen.toast_y_offset));
        View view = ResourcesUtil.inflate(applicationContext, R.layout.layout_toast);
        toast.setView(view);
        toast.setText(msg);
        toast.show();
    }

}
