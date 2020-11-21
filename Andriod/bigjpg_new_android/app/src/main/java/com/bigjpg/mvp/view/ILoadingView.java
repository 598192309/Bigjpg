package com.bigjpg.mvp.view;

/**
 * ILoadingView
 * @author Momo
 * @date 2016-03-24 11:57
 */
public interface ILoadingView extends IView {

    void showLoadingView();

    void showReloadView();

    void showContentView();

    void showEmptyView(String text);

    String getEmptyText();

}


