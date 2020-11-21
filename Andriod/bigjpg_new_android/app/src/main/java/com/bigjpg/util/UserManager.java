package com.bigjpg.util;

import android.content.Context;

import com.bigjpg.application.BigJPGApplication;
import com.bigjpg.model.entity.User;
import com.bigjpg.model.response.LoginResponse;
import com.bigjpg.model.rest.OkHttpClientUtil;

/**
 * @author Momo
 * @date 2019-04-16 16:00
 */
public class UserManager {

    private User mUser;

    private static class HolderClass {
        private static final UserManager instance = new UserManager();
    }

    private UserManager() {
    }
    public static UserManager getInstance() {
        return HolderClass.instance;
    }

    public User getUser() {
        if (mUser == null) {
            String currentUid = AppPref.getInstance().getCurUserId();
            if (!StringUtil.isEmpty(currentUid)) {
                mUser = CacheUtil.getObject(BigJPGApplication.getInstance(), CacheUtil.CacheKey.LOGIN_USER);
            }
        }
        if (mUser == null) {
            mUser = new User();
        }
        return mUser;
    }

    public boolean isNeedUpgradeVersion(){
        String version = UserManager.getInstance().getUser().version;
        return (StringUtil.isEmpty(version) || User.Version.NONE.equals(version) || UserManager.getInstance().getUser().is_expire);
    }

    public void refreshUser(User user) {
        if (user != null) {
            mUser = user;
        }
    }

    public boolean isLogin() {
        return !StringUtil.isEmpty(getUser().username);
    }

    public void saveLoginData(Context context, LoginResponse response) {
        User user = new User();
        user.username = response.getUsername();
        user.is_expire = response.isIs_expire();
        refreshUser(user);
        AppPref.getInstance().putCurUserId(user.username);
        saveUser(context, user);
    }

    public void saveUser(Context context, User user){
        CacheUtil.saveObject(context, user, CacheUtil.CacheKey.LOGIN_USER);
    }

    public void logout(Context context, OnAccountLogoutListener listener) {
        AppPref.getInstance().putCurUserId("");
        AppPref.getInstance().setCookies("");
        OkHttpClientUtil.setCookies("");
        mUser = null;
    }


    public interface OnAccountLogoutListener {
        void onLogoutSuccess();

        void onLogoutFailed();
    }

}
