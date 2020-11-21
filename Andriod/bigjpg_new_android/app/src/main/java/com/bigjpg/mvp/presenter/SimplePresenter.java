package com.bigjpg.mvp.presenter;


import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.mvp.view.IView;

/**
 * 描述:SimplePresenter
 *
 */
public abstract class SimplePresenter<V extends IView> extends Presenter<HttpResponse, V> {

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void bindViewData(HttpResponse data) {

    }
}
