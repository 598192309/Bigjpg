package com.bigjpg.mvp.view;

import com.bigjpg.model.response.AppConfigResponse;

/**
 * MainView
 * @author Momo
 * @date 2019-04-16 17:22
 */
public interface MainView extends IView{
    void setMainTabPosition(int position);
    void onGetAppConfigSuccess(AppConfigResponse response);
}
