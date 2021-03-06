package com.bigjpg.mvp.presenter;

import com.bigjpg.R;
import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.model.rest.RetrofitUtil;
import com.bigjpg.model.subscriber.SimpleSubscriber;
import com.bigjpg.mvp.view.ResetPasswordView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 重置密码
 * @author Momo
 * @date 2019-04-19 15:23
 */
public class ResetPasswordPresenter extends SimplePresenter<ResetPasswordView> {

    public void resetPassword(String email){
        getView().showLoadingDialog(R.string.loading);
        DisposableObserver disposableObserver = new SimpleSubscriber<HttpResponse>(getView()) {
            @Override
            public void onNextAction(HttpResponse response) {
                if (isViewAttached()) {
                    getView().hideLoadingDialog();
                    if (HttpResponse.isResponseOk(response)) {
                        getView().onResetPasswordSuccess(response);
                    } else {
                        getView().onResetPasswordFailed(response);
                    }
                }
            }
        };
        RetrofitUtil.getAPI().resetPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
        addDisposable(disposableObserver);
    }
}
