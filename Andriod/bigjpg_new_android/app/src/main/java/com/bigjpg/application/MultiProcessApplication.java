package com.bigjpg.application;

import android.app.ActivityManager;
import android.content.Context;

import com.bigjpg.model.constant.AppConstants;

import java.util.List;

/**
 * @author Momo
 * @date 2019-04-08 16:36
 */
public class MultiProcessApplication extends BaseApplication{

    /**
     * 本应用本地进程名字
     */
    protected static final String LOCAL_PROCESS_NAME = AppConstants.PACKAGE_NAME;


    /**
     * 是否是本应用本地进程
     *
     * @return
     */
    public boolean isLocalProcess() {
        String name = getCurrentProcessName();
        return LOCAL_PROCESS_NAME.equals(name);
    }

    /**
     * 获取进程名称
     *
     * @return
     */
    public String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        if (infos != null && infos.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo appProcessInfo : infos) {
                if (appProcessInfo.pid == pid) {
                    return appProcessInfo.processName;
                }
            }
        }
        return "";
    }
}
