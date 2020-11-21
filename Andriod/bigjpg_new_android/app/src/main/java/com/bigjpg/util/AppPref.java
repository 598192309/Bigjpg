package com.bigjpg.util;

import android.content.Context;

import com.bigjpg.application.BigJPGApplication;
import com.bigjpg.model.constant.AppLanguages;

public class AppPref extends PreferenceOpenHelper {

    private final static String PREFERENCE_NAME = "bigjpg";

    private static class HolderClass {
        private final static AppPref instance = createAppPref(BigJPGApplication.getInstance());
    }

    public static AppPref createAppPref(Context context){
        return new AppPref(context, PREFERENCE_NAME);
    }

    private AppPref(Context context, String prefName) {
        super(context, prefName);
    }

    public static AppPref getInstance() {
        return HolderClass.instance;
    }

    public void putCurUserId(String uid) {
        putString(Keys.CURRENT_USER_ID, uid);
    }

    public String getCurUserId() {
        return getString(Keys.CURRENT_USER_ID, null);
    }

    public String getCookies() {
        return getString(Keys.COOKIES, "");
    }

    public void setUserName(String userName){
        putString(Keys.USER_NAME, userName);
    }

    public String getUserName(){
        return getString(Keys.USER_NAME, "");
    }

    public void setCookies(String cookies) {
        putString(Keys.COOKIES, cookies);
    }

    public void setAutoDownloadImage(boolean auto){
        putBoolean(Keys.AUTO_DOWNLOAD_ENLARGE_IMAGE, auto);
    }

    public boolean isAutoDownloadImage(){
        return getBoolean(Keys.AUTO_DOWNLOAD_ENLARGE_IMAGE, false);
    }

    public long getVersionCodeNot2Update() {
        return getLong(Keys.VERSION_CODE_NOT_2_UPDATE, 0);
    }

    public void putVersionCodeNot2Update(long versionCode) {
        putLong(Keys.VERSION_CODE_NOT_2_UPDATE, versionCode);
    }

    public boolean isNightMode(){
        return getBoolean(Keys.NIGHT_MODE, false);
    }

    public void putNightMode(boolean isNightMode){
        putBoolean(Keys.NIGHT_MODE, isNightMode);
    }

    public String getLanguage(){
        return getString(Keys.APP_LNG, "");
    }

    public void setLanguage(String language){
        putString(Keys.APP_LNG, language);
    }

    private interface Keys {
        String COOKIES = "cookies";
        String CURRENT_USER_ID = "current_user_id";
        String USER_NAME = "user_name";
        String AUTO_DOWNLOAD_ENLARGE_IMAGE  = "auto_download_jpg";
        String VERSION_CODE_NOT_2_UPDATE = "version_code_not_2_update";
        String APP_LNG = "app_lng";
        String NIGHT_MODE = "night_mode";
    }
}