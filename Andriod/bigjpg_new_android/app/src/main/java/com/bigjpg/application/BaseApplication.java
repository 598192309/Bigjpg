package com.bigjpg.application;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author Momo
 * @date 2019-04-08 16:37
 */
public abstract class BaseApplication extends Application {

    private static BaseApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static BaseApplication getInstance() {
        if (sApplication == null) {
            throw new IllegalStateException("Application is not created");
        }
        return sApplication;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info == null) {
            info = new PackageInfo();
        }
        return info;
    }

}
