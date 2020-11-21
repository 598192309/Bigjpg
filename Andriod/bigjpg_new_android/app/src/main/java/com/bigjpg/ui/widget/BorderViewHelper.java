package com.bigjpg.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


import com.bigjpg.R;
import com.bigjpg.util.ResourcesUtil;

import java.lang.ref.WeakReference;

/**
 * BorderViewHelper
 * @author Momo
 * @date 2018-12-12 12:13
 */
public class BorderViewHelper {

    private WeakReference<View> mTarget;

    private final int DEFAULT_COLOR = 0xffdddddd;
    private int mLineColor = DEFAULT_COLOR;
    private boolean mIsAllLineVisible = true;
    private boolean mIsConsiderPaddingLeft = true;
    private boolean mIsConsiderPaddingRight = true;
    private boolean mIsConsiderPaddingBottom = true;
    private boolean mIsConsiderPaddingTop = true;
    private boolean mIsTopLineVisible = false;
    private boolean mIsBottomLineVisible = true;
    private boolean mIsLeftLineVisible = false;
    private boolean mIsRightLineVisible = false;
    private float mLineWidth = 1f;
    private Paint mPaint;
    private int paddingLeft;
    private int paddingBottom;
    private int paddingRight;
    private int paddingTop;
    private int mHeight;
    private int mWidth;
    private boolean mIsFirstTime = true;

    private int mLineMarginLeft;
    private int mLineMarginRight;
    private int mTopLineMarginLeft;
    private int mBottomLineMarginLeft;
    private int mTopLineMarginRight;
    private int mBottomLineMarginRight;
    private float mHalfLineWidth;


    public BorderViewHelper(View target){
        mTarget = new WeakReference<>(target);
    }

    public void init(Context context, AttributeSet attrs){
        mLineWidth = context.getResources().getDimensionPixelSize(R.dimen.line_width);
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LineLayout);
            mIsConsiderPaddingLeft = a.getBoolean(R.styleable.LineLayout_isConsiderPaddingLeft, mIsConsiderPaddingLeft);
            mIsConsiderPaddingRight = a.getBoolean(R.styleable.LineLayout_isConsiderPaddingRight, mIsConsiderPaddingRight);
            mIsConsiderPaddingBottom = a.getBoolean(R.styleable.LineLayout_isConsiderPaddingBottom, mIsConsiderPaddingBottom);
            mIsConsiderPaddingTop = a.getBoolean(R.styleable.LineLayout_isConsiderPaddingTop, mIsConsiderPaddingTop);
            mLineColor = a.getColor(R.styleable.LineLayout_lineColor, ResourcesUtil.getColor(context, R.color.line0_color));
            mIsTopLineVisible = a.getBoolean(R.styleable.LineLayout_isTopLineVisible, mIsTopLineVisible);
            mIsBottomLineVisible = a.getBoolean(R.styleable.LineLayout_isBottomLineVisible, mIsBottomLineVisible);
            mIsLeftLineVisible = a.getBoolean(R.styleable.LineLayout_isLeftLineVisible, false);
            mIsRightLineVisible = a.getBoolean(R.styleable.LineLayout_isRightLineVisible, false);
            mLineWidth = a.getDimensionPixelSize(R.styleable.LineLayout_lineWidth, (int) mLineWidth);
            mLineMarginLeft = a.getDimensionPixelSize(R.styleable.LineLayout_lineMarginLeft, mLineMarginLeft);
            mLineMarginRight = a.getDimensionPixelSize(R.styleable.LineLayout_lineMarginRight, mLineMarginRight);
            mTopLineMarginLeft =  a.getDimensionPixelSize(R.styleable.LineLayout_topLineMarginLeft, mLineMarginRight);
            mBottomLineMarginLeft =  a.getDimensionPixelSize(R.styleable.LineLayout_bottomLineMarginLeft, mLineMarginRight);
            mTopLineMarginRight =  a.getDimensionPixelSize(R.styleable.LineLayout_topLineMarginRight, mLineMarginRight);
            mBottomLineMarginRight =  a.getDimensionPixelSize(R.styleable.LineLayout_bottomLineMarginRight, mLineMarginRight);
            a.recycle();
        }

        mHalfLineWidth = mLineWidth / 2f;
        setupPaint();
    }

    private void setupPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setColor(mLineColor);
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh){
        if (w > 0 && h > 0) {
            mHeight = h;
            mWidth = w;
        }
    }

    public void onDraw(Canvas canvas){
        if(mTarget.get() == null){
            return;
        }

        View target = mTarget.get();

        if (mIsFirstTime) {
            mIsFirstTime = false;
            paddingLeft = target.getPaddingLeft();
            paddingRight = target.getPaddingRight();
            paddingBottom = target.getPaddingBottom();
            paddingTop = target.getPaddingTop();
            mHeight = target.getHeight();
            mWidth = target.getWidth();
        }

        if (mIsAllLineVisible) {
            mPaint.setColor(mLineColor);
            float leftBottomX, leftBottomY, rightBottomX, rightBottomY;
            float leftTopX, leftTopY, rightTopX, rightTopY;
            if (mIsConsiderPaddingLeft) {
                leftTopX = mHalfLineWidth + paddingLeft + mLineMarginLeft;
            } else {
                leftTopX = mHalfLineWidth + mLineMarginLeft;
            }
            leftBottomX = leftTopX;

            if (mIsConsiderPaddingRight) {
                rightTopX = mWidth - paddingRight - mLineMarginRight - mHalfLineWidth;
            } else {
                rightTopX = mWidth - mLineMarginRight - mHalfLineWidth;
            }
            rightBottomX = rightTopX;

            if (mIsConsiderPaddingBottom) {
                leftBottomY = mHeight - paddingBottom - mHalfLineWidth;
            } else {
                leftBottomY = mHeight - mHalfLineWidth;
            }
            rightBottomY = leftBottomY;

            if (mIsConsiderPaddingTop) {
                leftTopY = mHalfLineWidth + paddingTop;
            } else {
                leftTopY = mHalfLineWidth;
            }
            rightTopY = leftTopY;

            if (mIsBottomLineVisible) {
                canvas.drawLine(leftBottomX + mBottomLineMarginLeft, leftBottomY, rightBottomX - mBottomLineMarginRight, rightBottomY, mPaint);
            }

            if (mIsTopLineVisible) {
                canvas.drawLine(leftTopX + mTopLineMarginLeft, leftTopY, rightTopX - mTopLineMarginRight, rightTopY, mPaint);
            }

            if (mIsLeftLineVisible) {
                canvas.drawLine(leftTopX, leftTopY, leftBottomX, leftBottomY, mPaint);
            }

            if (mIsRightLineVisible) {
                canvas.drawLine(rightTopX, rightTopY, rightBottomX, rightBottomY, mPaint);
            }
        }
    }

    public void setBorderVisible(boolean isVisible) {
        if(mTarget.get() == null){
            return;
        }

        View target = mTarget.get();
        if (mIsAllLineVisible != isVisible) {
            mIsAllLineVisible = isVisible;
            target.invalidate();
        }
    }

    public void setBorderColor(int color) {
        if(mTarget.get() == null){
            return;
        }

        View target = mTarget.get();
        if (mLineColor != color) {
            mLineColor = color;
            target.invalidate();
        }
    }
}
