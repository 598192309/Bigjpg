package com.bigjpg.mvp.view;

import com.bigjpg.model.entity.UpgradeItem;
import com.bigjpg.model.response.PaypalResponse;

import java.util.List;

/**
 * @author Momo
 * @date 2019-04-19 17:31
 */
public interface UpgradeView extends ILoadingView {
    void onGetUpgradeConfigSuccess(List<UpgradeItem> list);
    void onGetUpgradeConfigFailed();
    void onGetPaypalOrderSuccess(PaypalResponse response);
    void onGetPaypalOrderFailed(PaypalResponse response);
}
