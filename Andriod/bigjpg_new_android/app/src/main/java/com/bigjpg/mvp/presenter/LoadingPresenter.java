package com.bigjpg.mvp.presenter;

import android.text.TextUtils;

import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.model.rest.AppApi;
import com.bigjpg.model.rest.RetrofitUtil;
import com.bigjpg.mvp.view.ILoadingView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * LoadingPresenter
 *
 * @author Momo
 * @date 2016-03-24 16:54
 */
public abstract class LoadingPresenter<T extends HttpResponse, V extends ILoadingView> extends Presenter<T, V> {

    private boolean mLoading = false;
    private boolean mLoaded = false;
    private boolean mEmpty = false;
    private Call<T> mRequestCall;

    @Override
    public void start() {
        if (!isLoaded() && !isLoading() && getView() != null) {
            getView().showLoadingView();
            request();
        }
    }

    @Override
    public void stop() {
    }

    public void request() {
        if (!isLoading()) {
            cancelRequest();
            mRequestCall = onRequest();
            if (mRequestCall != null) {
                setLoading(true);
                mRequestCall.enqueue(mCallback);
            } else {
                getView().showReloadView();
            }
        }
    }

    public void cancelRequest() {
        if (mRequestCall != null) {
            mRequestCall.cancel();
            mRequestCall = null;
        }
        setLoading(false);
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        cancelRequest();
    }

    public void onRequestFailed(Throwable t) {
        getView().showReloadView();
    }

    public void onResponse(T data) {
        if (isDataEmpty(data)) {
            onRequestEmpty(data);
            setEmpty(true);
        } else {
            getView().showContentView();
            bindViewData(data);
            setEmpty(false);
        }
    }

    public void setEmpty(boolean isEmpty){
        mEmpty = isEmpty;
    }

    public boolean isEmpty(){
        return mEmpty;
    }

    public void onRequestEmpty(HttpResponse data) {
        if (data != null && !TextUtils.isEmpty(data.getStatus())) {
            getView().showEmptyView(data.getStatus());
        } else {
            getView().showEmptyView(getView().getEmptyText());
        }
    }

    public boolean isDataEmpty(T data) {
        return data == null || !data.isOk();
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
    }

    public boolean isLoaded() {
        return mLoaded;
    }

    public void setLoaded(boolean loaded) {
        mLoaded = loaded;
    }

    protected abstract Call<T> onRequest();

    private Callback<T> mCallback = new Callback<T>() {

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            T data = response.body();
            if (isViewAttached()) {
                if(data != null){
                    LoadingPresenter.this.onResponse(data);
                }else{
                    onRequestEmpty(data);
                }
            }
            setLoaded(true);
            setLoading(false);
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            t.printStackTrace();
            if (isViewAttached()) {
                LoadingPresenter.this.onRequestFailed(t);
            }
            setLoaded(false);
            setLoading(false);
            setEmpty(true);
        }

    };

    protected AppApi appApi() {
        return RetrofitUtil.getAPI();
    }
}
