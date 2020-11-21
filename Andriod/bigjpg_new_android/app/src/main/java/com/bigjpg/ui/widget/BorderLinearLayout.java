package com.bigjpg.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * BorderLinearLayout
 * @author mfx
 * @date 2014年7月30日 下午7:03:39
 */
public class BorderLinearLayout extends LinearLayout implements IBorderView {

	private BorderViewHelper mBorderViewHelper;

	public BorderLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		getBorderViewHelper().init(context, attrs);
		setWillNotDraw(false);
	}

	private BorderViewHelper getBorderViewHelper() {
		if (mBorderViewHelper == null) {
			mBorderViewHelper = new BorderViewHelper(this);
		}
		return mBorderViewHelper;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		getBorderViewHelper().onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		getBorderViewHelper().onDraw(canvas);
	}

	@Override
	public void setBorderVisible(boolean isVisible) {
		getBorderViewHelper().setBorderVisible(isVisible);
	}

	@Override
	public void setBorderColor(int color) {
		getBorderViewHelper().setBorderColor(color);
	}
}
