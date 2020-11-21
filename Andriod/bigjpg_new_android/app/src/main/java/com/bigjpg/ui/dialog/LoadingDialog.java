package com.bigjpg.ui.dialog;

import android.app.Activity;
import android.view.WindowManager;
import android.widget.TextView;

import com.bigjpg.R;
import com.bigjpg.ui.base.BaseDialog;
import com.bigjpg.util.AppUtil;


public class LoadingDialog extends BaseDialog {

	private TextView mTvText;

	public LoadingDialog(Activity activity) {
		this(activity, false, false);
	}

	public LoadingDialog(Activity activity, boolean cancelable, boolean canceledOnTouchOutside) {
		super(activity, R.style.RoundCornerDialog);
		setContentView(R.layout.dialog_loading);
		mTvText  = findViewById(R.id.dialog_loading_text);
		setCancelable(cancelable);
		setCanceledOnTouchOutside(canceledOnTouchOutside);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.x = 0;
		lp.y = 0;
		lp.width = AppUtil.dipToPx(activity, 150);
		getWindow().setAttributes(lp);
	}

	public void setText(String text) {
		if (mTvText != null) {
			mTvText.setText(text);
		}
	}

	public void setText(int textId) {
		if (mTvText != null) {
			mTvText.setText(textId);
		}
	}

}
