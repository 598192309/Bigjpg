package com.bigjpg.mvp.presenter;

import com.bigjpg.R;
import com.bigjpg.application.BigJPGApplication;
import com.bigjpg.model.entity.User;
import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.model.response.LoginResponse;
import com.bigjpg.model.response.UserResponse;
import com.bigjpg.model.rest.RetrofitUtil;
import com.bigjpg.model.subscriber.SimpleSubscriber;
import com.bigjpg.mvp.view.SettingView;
import com.bigjpg.util.AppPref;
import com.bigjpg.util.UserManager;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * SettingPresenter
 *
 * @author Momo
 * @date 2019-04-15 15:12
 */
public class SettingPresenter extends SimplePresenter<SettingView> {

    public void register(String email, String password) {
        getView().showLoadingDialog(R.string.loading);
        DisposableObserver disposableObserver = new SimpleSubscriber<LoginResponse>(getView(), true) {
            @Override
            public void onNextAction(LoginResponse response) {
                if (isViewAttached()) {
                    if (HttpResponse.isResponseOk(response)) {
                        getView().onRegisterSuccess(response);
                    } else {
                        getView().onRegisterFailed(response);
                    }
                }
            }
        };
        RetrofitUtil.getAPI().postRegister(email, password)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse response) throws Exception {
                        if (HttpResponse.isResponseOk(response)) {
                            UserManager.getInstance().saveLoginData(BigJPGApplication.getInstance(), response);
                            AppPref.getInstance().setUserName(response.getUsername());
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
        addDisposable(disposableObserver);
    }

    public void login(String email, String password) {
        getView().showLoadingDialog(R.string.loading);
        DisposableObserver disposableObserver = new SimpleSubscriber<LoginResponse>(getView(), true) {
            @Override
            public void onNextAction(LoginResponse response) {
                if (isViewAttached()) {
                    if (HttpResponse.isResponseOk(response)) {
                        getView().onLoginSuccess(response);
                    } else {
                        getView().onLoginFailed(response);
                    }
                }
            }
        };
        RetrofitUtil.getAPI().postLogin(email, password, 1)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse response) throws Exception {
                        if (HttpResponse.isResponseOk(response)) {
                            UserManager.getInstance().saveLoginData(BigJPGApplication.getInstance(), response);
                            AppPref.getInstance().setUserName(response.getUsername());
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
        addDisposable(disposableObserver);
    }

    public void getUserInfo() {
        DisposableObserver disposableObserver = new SimpleSubscriber<UserResponse>(getView()) {
            @Override
            public void onNextAction(UserResponse response) {
                if (isViewAttached()) {
                    if (HttpResponse.isResponseOk(response)) {
                        getView().onGetUserInfoSuccess(response);
                    } else {
                        getView().onGetUserInfoFailed(response);
                    }
                }
            }
        };
        RetrofitUtil.getAPI().getUser()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<UserResponse>() {
                    @Override
                    public void accept(UserResponse response) throws Exception {
                        if (HttpResponse.isResponseOk(response)) {
                            User user = response.getUser();
                            UserManager.getInstance().refreshUser(user);
                            UserManager.getInstance().saveUser(BigJPGApplication.getInstance(), user);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
        addDisposable(disposableObserver);
    }

    public void logout() {
        getView().showLoadingDialog(R.string.loading);

        Disposable disposable = Observable.create(new ObservableOnSubscribe<Long>() {

            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                Callable<Long> calcCall = new Callable<Long>() {
                    @Override
                    public Long call() throws Exception {
                        try {
                            long start = System.currentTimeMillis();
                            UserManager.getInstance().logout(BigJPGApplication.getInstance(), null);
                            long end = System.currentTimeMillis();
                            return end - start;
                        } catch (Exception e) {
                            return 0L;
                        }
                    }
                };
                try {
                    emitter.onNext(calcCall.call());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).subscribeOn(Schedulers.computation())
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long size) throws Exception {
                        if (isViewAttached()) {
                            getView().hideLoadingDialog();
                            getView().onLogoutSuccess();
                        }
                    }
                });

        addDisposable(disposable);
    }

}
