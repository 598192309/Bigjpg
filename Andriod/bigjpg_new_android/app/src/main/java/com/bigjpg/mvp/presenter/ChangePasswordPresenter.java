package com.bigjpg.mvp.presenter;

import com.bigjpg.R;
import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.model.response.UserResponse;
import com.bigjpg.model.rest.RetrofitUtil;
import com.bigjpg.model.subscriber.SimpleSubscriber;
import com.bigjpg.mvp.view.ChangePasswordView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * ChangePasswordPresenter
 * @author Momo
 * @date 2019-04-19 11:44
 */
public class ChangePasswordPresenter extends SimplePresenter<ChangePasswordView> {
    public void changePassword(String newPassword) {
        getView().showLoadingDialog(R.string.loading);
        DisposableObserver disposableObserver = new SimpleSubscriber<HttpResponse>(getView()) {
            @Override
            public void onNextAction(HttpResponse response) {
                if (isViewAttached()) {
                    getView().hideLoadingDialog();
                    if (HttpResponse.isResponseOk(response)) {
                        getView().onChangePasswordSuccess(response);
                    } else {
                        getView().onChangePasswordFailed(response);
                    }
                }
            }
        };
        RetrofitUtil.getAPI().postPassword(newPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
        addDisposable(disposableObserver);
    }
}
