package com.bigjpg.mvp.view;

import com.bigjpg.model.response.PayResponse;

/**
 * @author Momo
 * @date 2019-04-22 11:13
 */
public interface PayView extends ILoadingView{
    void getGetPayQrSuccess(PayResponse response);
}
