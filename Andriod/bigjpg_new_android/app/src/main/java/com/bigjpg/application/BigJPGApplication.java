package com.bigjpg.application;

import android.content.Context;
import android.content.res.Configuration;
import androidx.multidex.MultiDex;
import androidx.appcompat.app.AppCompatDelegate;

import com.bigjpg.model.response.AppConfigResponse;
import com.bigjpg.model.rest.OkHttpClientUtil;
import com.bigjpg.util.AppLanguageUtils;
import com.bigjpg.util.AppPref;
import com.bigjpg.util.CacheUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import okhttp3.OkHttpClient;

/**
 * 入口Application
 *
 * @author Momo
 * @date 2019-04-08 16:40
 */
public class BigJPGApplication extends MultiProcessApplication {

    private AppConfigResponse mAppConfig;
    private RefWatcher mRefWatcher;
    private boolean mIsNightMode;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            mRefWatcher = LeakCanary.install(this);
        }

        onLanguageChange();
        if (isLocalProcess()) {
            init();
        }
    }

    public static BigJPGApplication getInstance() {
        return (BigJPGApplication) BaseApplication.getInstance();
    }

    private void init() {
        initNightMode();
        initFresco(this);
        initAppConfig();
    }

    private void initNightMode(){
        mIsNightMode = AppPref.getInstance().isNightMode();
        if(mIsNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private static void initFresco(Context context) {
        OkHttpClient okHttpClient = OkHttpClientUtil.getOkHttpClient();
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(context, okHttpClient)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(context, config);
    }

    public static RefWatcher getRefWatcher(Context context) {
        BigJPGApplication application = (BigJPGApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(AppLanguageUtils.attachBaseContext(base, getAppLanguage(base)));
        MultiDex.install(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onLanguageChange();
    }

    private void initAppConfig() {
        if (mAppConfig == null) {
            mAppConfig = CacheUtil.getObject(this, CacheUtil.CacheKey.APP_CONFIG);
        }
    }

    public void setAppConfig(AppConfigResponse appConfig) {
        mAppConfig = appConfig;
    }

    public AppConfigResponse getAppConfig() {
        return mAppConfig;
    }

    public boolean isNightMode(){
        return mIsNightMode;
    }

    public void setNightMode(boolean isNightMode){
        mIsNightMode = isNightMode;
        AppPref.getInstance().putNightMode(mIsNightMode);
    }

    private void onLanguageChange() {
        AppLanguageUtils.changeAppLanguage(this, AppLanguageUtils.getDefaultLanguage(this));
    }

    public String getAppLanguage(Context context) {
        return AppPref.createAppPref(context).getLanguage();
    }
}
