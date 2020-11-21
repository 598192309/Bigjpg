package com.bigjpg.mvp.view;

import android.content.Context;
import androidx.annotation.StringRes;

import com.bigjpg.model.response.HttpResponse;


/**
 * IView
 * @author Momo
 * @date 2016-03-23 17:30
 */
public interface IView {

    Context getContext();

    void showToast(@StringRes int tipsId);
    void showToast(String tips);
    void showToast(String msg, int duration);
    void showToast(@StringRes int msgResId, int duration);

    void showLoadingDialog(String resId);
    void showLoadingDialog(@StringRes int resId);

    void hideLoadingDialog();
    void showErrorView(Throwable e);

    void showMessageDialog(String message);
    void showMessageDialog(@StringRes int msgResId);
    void showMessageDialog(HttpResponse response, @StringRes int msgResId);
}
