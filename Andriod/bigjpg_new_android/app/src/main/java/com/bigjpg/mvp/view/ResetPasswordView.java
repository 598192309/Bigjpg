package com.bigjpg.mvp.view;

import com.bigjpg.model.response.HttpResponse;

/**
 * @author Momo
 * @date 2019-04-19 15:23
 */
public interface ResetPasswordView extends IView{
    void onResetPasswordSuccess(HttpResponse response);
    void onResetPasswordFailed(HttpResponse response);
}
