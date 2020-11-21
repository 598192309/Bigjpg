package com.bigjpg.mvp.presenter;

import com.bigjpg.application.BigJPGApplication;
import com.bigjpg.model.response.AppConfigResponse;
import com.bigjpg.model.rest.RetrofitUtil;
import com.bigjpg.model.subscriber.SimpleSubscriber;
import com.bigjpg.mvp.view.MainView;
import com.bigjpg.util.CacheUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * MainPresenter
 *
 * @author Momo
 * @date 2019-04-16 17:22
 */
public class MainPresenter extends SimplePresenter<MainView> {


    public void getAppConfig() {
        DisposableObserver disposableObserver = new SimpleSubscriber<AppConfigResponse>(getView()) {
            @Override
            public void onNextAction(AppConfigResponse response) {
                if(isViewAttached() && response != null){
                    getView().onGetAppConfigSuccess(response);
                }
            }
        };
        RetrofitUtil.getAPI().getAppConfig()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<AppConfigResponse>() {
                    @Override
                    public void accept(AppConfigResponse response) throws Exception {
                        if (response != null && response.getApp_oss_conf() != null) {
                            BigJPGApplication.getInstance().setAppConfig(response);
                            CacheUtil.saveObject(BigJPGApplication.getInstance(), response, CacheUtil.CacheKey.APP_CONFIG);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
        addDisposable(disposableObserver);
    }


}
