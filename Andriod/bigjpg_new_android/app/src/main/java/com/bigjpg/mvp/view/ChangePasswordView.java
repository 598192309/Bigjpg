package com.bigjpg.mvp.view;

import com.bigjpg.model.response.HttpResponse;

/**
 * @author Momo
 * @date 2019-04-19 11:45
 */
public interface ChangePasswordView extends IView{
    void onChangePasswordSuccess(HttpResponse response);
    void onChangePasswordFailed(HttpResponse response);
}
