package com.bigjpg.mvp.presenter;

import com.bigjpg.model.entity.PayType;
import com.bigjpg.model.response.PayResponse;
import com.bigjpg.model.rest.RetrofitUtil;
import com.bigjpg.mvp.view.PayView;
import com.bigjpg.util.UserManager;

import retrofit2.Call;

/**
 * PayPresenter
 * @author Momo
 * @date 2019-04-22 11:14
 */

public class PayPresenter extends LoadingPresenter<PayResponse, PayView> {

    private String mType;
    private int mPayType;

    public PayPresenter(String type, int payType){
        mType = type;
        mPayType = payType;
    }

    @Override
    protected Call<PayResponse> onRequest() {
        if(mPayType == PayType.WECHAT){
            return RetrofitUtil.getAPI().getYouzanOrderCall(mType);
        }else if(mPayType == PayType.ALIPAY){
            return RetrofitUtil.getAPI().getAlipayOrderCall(1, mType, UserManager.getInstance().getUser().username);
        }else{
            return null;
        }
    }

    @Override
    public boolean isDataEmpty(PayResponse data) {
        return super.isDataEmpty(data);
    }

    @Override
    public void bindViewData(PayResponse data) {
        getView().getGetPayQrSuccess(data);
    }
}
