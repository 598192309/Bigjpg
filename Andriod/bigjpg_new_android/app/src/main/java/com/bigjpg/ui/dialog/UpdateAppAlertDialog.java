package com.bigjpg.ui.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.bigjpg.R;
import com.bigjpg.model.response.AppConfigResponse;
import com.bigjpg.ui.base.BaseDialog;
import com.bigjpg.util.AppPref;
import com.bigjpg.util.AppUtil;

import butterknife.OnClick;

/**
 * @author Momo
 * @date 2018-11-07 11:51
 */
public class UpdateAppAlertDialog extends BaseDialog {

    private AppConfigResponse mResponse;

    public UpdateAppAlertDialog(Activity activity, AppConfigResponse response) {
        super(activity);
        mResponse = response;
        setContentView(R.layout.dialog_update_app_alert);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.x = 0;
            lp.y = 0;
            lp.width = AppUtil.dipToPx(activity, 280);
            getWindow().setAttributes(lp);
        }

    }


    @OnClick(R.id.cancel)
    void onCancelClick() {
        dismiss();
    }

    @OnClick(R.id.confirm)
    void onConfirmClick() {
        if (!TextUtils.isEmpty(mResponse.getNewest_app())) {
            AppUtil.callOuterBrowser(getOwnerActivity(), mResponse.getNewest_app());
        }
        dismiss();
    }

    @OnClick(R.id.not_notify)
    void onNotNotifyClick() {
        AppPref.getInstance().putVersionCodeNot2Update(mResponse.getVersion());
        dismiss();
    }
}
