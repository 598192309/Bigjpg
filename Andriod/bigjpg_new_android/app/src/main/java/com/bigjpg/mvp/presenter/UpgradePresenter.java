package com.bigjpg.mvp.presenter;

import com.bigjpg.R;
import com.bigjpg.model.response.PaypalResponse;
import com.bigjpg.model.response.UpgradeResponse;
import com.bigjpg.model.rest.RetrofitUtil;
import com.bigjpg.mvp.view.UpgradeView;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Momo
 * @date 2019-04-19 17:31
 */
public class UpgradePresenter extends LoadingPresenter<UpgradeResponse, UpgradeView> {

    @Override
    protected Call<UpgradeResponse> onRequest() {
        return RetrofitUtil.getAPI().getUpradeConfig();
    }

    @Override
    public boolean isDataEmpty(UpgradeResponse data) {
        return data == null || data.getList() == null || data.getList().isEmpty();
    }

    @Override
    public void bindViewData(UpgradeResponse data) {
        getView().onGetUpgradeConfigSuccess(data.getList());
    }

    public void getPaypalOrder(String type){
        getView().showLoadingDialog(R.string.loading);

        Callback<PaypalResponse> callback = new Callback<PaypalResponse>() {
            @Override
            public void onResponse(Call<PaypalResponse> call, Response<PaypalResponse> response) {
                if(isViewAttached()){
                    getView().hideLoadingDialog();
                    int code = response.code();
                    if(code == 302){
                        Headers headers = response.headers();
                        String location = headers.get("Location");
                        PaypalResponse paypalResponse = new PaypalResponse();
                        paypalResponse.setStatus(PaypalResponse.Status.OK);
                        paypalResponse.setUrl(location);
                        getView().onGetPaypalOrderSuccess(paypalResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<PaypalResponse> call, Throwable t) {
                if(isViewAttached()){
                    getView().hideLoadingDialog();
                }
            }
        };

        RetrofitUtil.getAPI().getPaypalOrderCall(type).enqueue(callback);

    }
}
