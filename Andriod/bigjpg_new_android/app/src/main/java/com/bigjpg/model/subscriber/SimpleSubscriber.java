package com.bigjpg.model.subscriber;

import androidx.annotation.NonNull;

import com.bigjpg.mvp.view.IView;

import io.reactivex.observers.DisposableObserver;

/**
 * 自定义Subscriber
 *
 */
public abstract class SimpleSubscriber<T> extends DisposableObserver<T> {

    private IView mIView;
    private boolean mHideLoadingWhenCompleted = true;
    private boolean mIsShowErrorMsg = true;

    public SimpleSubscriber(@NonNull IView iview) {
        mIView = iview;
    }

    public SimpleSubscriber(@NonNull IView iview, boolean hideLoadingWhenCompleted) {
        this(iview, hideLoadingWhenCompleted, true);
    }

    /**
     * @param view
     * @param hideLoadingWhenCompleted 是否在完成时隐藏dialog
     * @param showErrorMsg             是否显示错误信息
     */
    public SimpleSubscriber(@NonNull IView view, boolean hideLoadingWhenCompleted, boolean showErrorMsg) {
        this(view);
        this.mHideLoadingWhenCompleted = hideLoadingWhenCompleted;
        mIsShowErrorMsg = showErrorMsg;
    }

    @Override
    public void onComplete() {
        if (mIView == null) {
            return;
        }

        if (mHideLoadingWhenCompleted) {
            mIView.hideLoadingDialog();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mIView == null) {
            return;
        }
        mIView.hideLoadingDialog();
        if (mIsShowErrorMsg) {
            mIView.showErrorView(e);
        }
    }

    @Override
    public void onNext(T t) {
        if (mIView == null) {
            return;
        }
        try{
            onNextAction(t);
        }catch (Exception e){
            onActionException(e);
        }
    }

    public void onActionException(Exception e){
    }

    public abstract void onNextAction(T t);

}
