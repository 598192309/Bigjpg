package com.bigjpg.mvp.presenter;


import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.mvp.view.IView;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Presenter
 *
 * @author Momo
 * @date 2016-03-23 17:30
 */
public abstract class Presenter<T extends HttpResponse, V extends IView> {

    private V mView;
    private CompositeDisposable mDisposables;

    public void attachView(V view) {
        mView = view;
    }

    public void detachView(boolean retainInstance) {
        mView = null;
    }

    public V getView() {
        return mView;
    }

    public void unsubscribe() {
        clearDisposable();
    }

    public abstract void start();

    public abstract void stop();

    public abstract void bindViewData(T data);

    public boolean isViewAttached() {
        return getView() != null;
    }

    public void addDisposable(Disposable disposable){
        if(mDisposables == null){
            mDisposables = new CompositeDisposable();
        }
        mDisposables.add(disposable);
    }

    public void clearDisposable(){
        if(mDisposables != null){
            mDisposables.clear();
        }
    }

    protected Scheduler threadIO(){
        return Schedulers.io();
    }

    protected Scheduler threadMain(){
        return AndroidSchedulers.mainThread();
    }

    protected <T> Observable<T> rxSwitchThread(Observable<T> observable) {
        return observable.subscribeOn(threadIO()).observeOn(threadMain());
    }
}
