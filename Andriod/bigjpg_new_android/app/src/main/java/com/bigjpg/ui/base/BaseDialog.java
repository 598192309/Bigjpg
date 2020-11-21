package com.bigjpg.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.bigjpg.R;
import com.bigjpg.util.ResourcesUtil;

import butterknife.ButterKnife;

/**
 * BaseDialog
 */
public abstract class BaseDialog extends Dialog {


    public BaseDialog(Activity activity) {
        this(activity, R.style.BaseDialog);
    }

    public BaseDialog(Context context, int dialogFullscreen) {
        super(context, dialogFullscreen);
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }


    public void resetWindowSize() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.x = 0;
            lp.y = 0;
            lp.width = ResourcesUtil.getDimensionPixelSize(getOwnerActivity(), R.dimen.ui_dialog_standard_width);
            getWindow().setAttributes(lp);
        }
    }

}
