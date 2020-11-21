package com.bigjpg.mvp.view;

import com.bigjpg.model.response.LoginResponse;
import com.bigjpg.model.response.UserResponse;

/**
 * SettingView
 * @author Momo
 * @date 2019-04-15 15:11
 */
public interface SettingView extends IView {
    void onRegisterSuccess(LoginResponse response);
    void onRegisterFailed(LoginResponse response);
    void onLoginSuccess(LoginResponse response);
    void onLoginFailed(LoginResponse response);
    void onLogoutSuccess();
    void onLogoutFailed();
    void onGetUserInfoSuccess(UserResponse response);
    void onGetUserInfoFailed(UserResponse response);
}
