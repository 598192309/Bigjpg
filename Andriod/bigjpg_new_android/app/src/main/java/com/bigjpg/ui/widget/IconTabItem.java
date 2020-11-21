package com.bigjpg.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

import com.bigjpg.R;
import com.bigjpg.util.AppUtil;
import com.bigjpg.util.ViewUtil;

/**
 * 图片居中，并可以设置图片大小, 带红点和数字提示的TabItem
 */
public class IconTabItem extends AppCompatRadioButton {

    private Drawable buttonDrawable;

    private int mDrawableWidth;
    private int mDrawableHeight;

    private int mTipBackgroundColor;
    private int mTipTextColor;
    private int mTipTextSize;

    private int mTipDotRadius;
    private int mTextDotRadius;

    private TextPaint mTipTextPaint;
    private Paint mTipBgPaint;

    private boolean mHasTip;
    private String mTipText;

    private int mDrawableLabelGap;

    private RectF mOvalRectF = new RectF();

    private int mTipTextDotTopMargin;
    private int mTipTextDotRightMargin;
    private int mTipDotTopMargin;
    private int mTipDotRightMargin;
    private int mDotOvalPadding;
    private boolean mCheckable;
    //两个字符最少的宽度
    private int mMinText2Width;

    public IconTabItem(Context context) {
        this(context, null);
    }

    public IconTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources res = context.getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CompoundButton, 0, 0);
        buttonDrawable = a.getDrawable(R.styleable.CompoundButton_android_button);
        mDrawableWidth = a.getDimensionPixelSize(R.styleable.CompoundButton_drawableWidth, 0);
        mDrawableHeight = a.getDimensionPixelSize(R.styleable.CompoundButton_drawableHeight, 0);
        a.recycle();

        TypedArray iconTypedArray = context.obtainStyledAttributes(attrs, R.styleable.IconTabItem, 0, 0);
        mTipBackgroundColor = iconTypedArray.getColor(R.styleable.IconTabItem_tipBackgroundColor, 0xFFf14949);
        mTipTextColor = iconTypedArray.getColor(R.styleable.IconTabItem_tipTextColor, 0xFFFFFFFF);
        mTipTextSize = iconTypedArray.getDimensionPixelSize(R.styleable.IconTabItem_tipTextSize, res.getDimensionPixelSize(R.dimen.font_30));
        mTipDotRadius = iconTypedArray.getDimensionPixelSize(R.styleable.IconTabItem_tipDotRadius, AppUtil.dipToPx(context, 4));
        mTextDotRadius = iconTypedArray.getDimensionPixelSize(R.styleable.IconTabItem_textDotRadius, AppUtil.dipToPx(context, 8));
        mDrawableLabelGap = iconTypedArray.getDimensionPixelSize(R.styleable.IconTabItem_drawableLabelGap, AppUtil.dipToPx(context, 1));

        mTipTextDotTopMargin = iconTypedArray.getDimensionPixelSize(R.styleable.IconTabItem_tipTextDotTopMargin, AppUtil.dipToPx(context, 5));
        mTipTextDotRightMargin = iconTypedArray.getDimensionPixelSize(R.styleable.IconTabItem_tipTextDotRightMargin, AppUtil.dipToPx(context, 5));
        mTipDotTopMargin = iconTypedArray.getDimensionPixelSize(R.styleable.IconTabItem_tipDotTopMargin, AppUtil.dipToPx(context, 8));
        mTipDotRightMargin = iconTypedArray.getDimensionPixelSize(R.styleable.IconTabItem_tipDotRightMargin, AppUtil.dipToPx(context, 8));
        mDotOvalPadding = iconTypedArray.getDimensionPixelSize(R.styleable.IconTabItem_dotOvalPadding, AppUtil.dipToPx(getContext(), 2));
        mCheckable = iconTypedArray.getBoolean(R.styleable.IconTabItem_checkable, true);
        iconTypedArray.recycle();

        setButtonDrawable(R.drawable.empty_drawable);

        mTipBgPaint = new Paint();
        mTipBgPaint.setColor(mTipBackgroundColor);
        mTipBgPaint.setAntiAlias(true);

        mTipTextPaint = new TextPaint();
        mTipTextPaint.setColor(mTipTextColor);
        mTipTextPaint.setTextSize(mTipTextSize);
        mTipTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTipTextPaint.setAntiAlias(true);

        mMinText2Width = (int)mTipTextPaint.measureText("11");
    }

    public void setDrawableWidth(int drawableWidth) {
        this.mDrawableWidth = drawableWidth;
    }

    public void setDrawableHeight(int drawableHeight) {
        this.mDrawableHeight = drawableHeight;
    }

    public void setIconDrawable(int drawableId) {
        this.buttonDrawable = getResources().getDrawable(drawableId);
        invalidate();
    }

    /**
     * 设置有提示红点 ,当有文字和红点提示时，文字优先
     *
     * @param hasTip
     * @see {@link #setTipText(String tipText)}
     */
    public void setHasTip(boolean hasTip) {
        if (mHasTip == hasTip){
            return;
        }
        mHasTip = hasTip;
        invalidate();
    }

    /**
     * 设置有提示文字,当有文字和红点提示时，文字优先
     *
     * @param tipText
     * @see {@link #setHasTip(boolean hasTip)}
     */
    public void setTipText(String tipText) {
        if ((mTipText == null && tipText == null) || mTipText != null && mTipText.equals(tipText)) {
            return;
        }

        mTipText = tipText;
        invalidate();
    }

    @Override
    public void setChecked(boolean checked) {
        if (mCheckable) {
            super.setChecked(checked);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int width = getWidth();
        final int height = getHeight();

        final CharSequence labelText = getText();
        final TextPaint labelPaint = getPaint();
        final float labelFontHeight = ViewUtil.calculateFontHeight(labelPaint);

        boolean noLabel = false;
        if (labelText == null || labelText.length() == 0) {
            noLabel = true;
        }

        final int[] drawableState = getDrawableState();
        int buttonDrawableBottom = 0;

        if (buttonDrawable != null) {
            buttonDrawable.setState(drawableState);
            final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
            final int buttonHeight = mDrawableWidth == 0 ? buttonDrawable.getIntrinsicHeight() : mDrawableWidth;

            int y = 0;

            switch (verticalGravity) {
                case Gravity.BOTTOM:
                    if (noLabel) {
                        y = height - buttonHeight;
                    } else {
                        y = (int) (height - buttonHeight - labelFontHeight - mDrawableLabelGap);
                    }
                    break;
                case Gravity.CENTER_VERTICAL:
                    if (noLabel) {
                        y = (height - buttonHeight) / 2;
                    } else {
                        y = (int) ((height - buttonHeight - labelFontHeight - mDrawableLabelGap) / 2);
                    }
                    break;
            }

            int buttonWidth = mDrawableHeight == 0 ? buttonDrawable.getIntrinsicWidth() : mDrawableHeight;
            int buttonLeft = (width - buttonWidth) / 2;
            buttonDrawableBottom = y + buttonHeight;
            buttonDrawable.setBounds(buttonLeft, y, buttonLeft + buttonWidth, buttonDrawableBottom);
            buttonDrawable.draw(canvas);
        }

        //draw label
        if (!noLabel) {
            ColorStateList labelCSList = getTextColors();
            int curColor = labelCSList.getColorForState(drawableState, labelCSList.getDefaultColor());
            labelPaint.setColor(curColor);
            String drawLabelText = labelText.toString();
            int labelTextWidth = ViewUtil.calculateStringWidth(labelPaint, drawLabelText);
            int labelX = (width - labelTextWidth) / 2;
            int labelY = (int) (buttonDrawableBottom + mDrawableLabelGap - labelPaint.getFontMetrics().top);
            canvas.drawText(drawLabelText, labelX, labelY, labelPaint);
        }

        if (!TextUtils.isEmpty(mTipText)) {
            float tipTextWidth = mTipTextPaint.measureText(mTipText);
            final int tipTextRightMargin = mTipTextDotRightMargin;
            final int tipTextTopMargin = mTipTextDotTopMargin;
            float cx, cy;
            float textX, textY;
            final int dotDiameter = mTextDotRadius * 2;
            final FontMetricsInt fontMetrics = mTipTextPaint.getFontMetricsInt();
            final int fontHeight = fontMetrics.bottom - fontMetrics.top;
            textY = (int) (tipTextTopMargin + (dotDiameter - fontHeight) * 0.5 - fontMetrics.top);

            if (mTipText.length() < 2 && tipTextWidth < dotDiameter && !"⋯".equals(mTipText)) {
                cx = (width + mDrawableWidth) * 0.5f + tipTextRightMargin + mTipDotRadius;
                cy = tipTextTopMargin + mTextDotRadius;
                canvas.drawCircle(cx, cy, mTextDotRadius, mTipBgPaint);
                textX = cx - tipTextWidth * 0.5f;
                canvas.drawText(mTipText, textX, textY, mTipTextPaint);
            } else {
                final int ovalPadding = mDotOvalPadding;
                int ovalWidth;
                if(tipTextWidth < mMinText2Width){
                    ovalWidth = (mMinText2Width + 2 * ovalPadding);
                }else{
                    ovalWidth = (int) (tipTextWidth + 2 * ovalPadding);
                }
                final RectF ovalRect = mOvalRectF;
                ovalRect.left = (width + tipTextWidth) * 0.5f + tipTextRightMargin;
                ovalRect.right = ovalRect.left + ovalWidth;
                ovalRect.top = tipTextTopMargin;
                ovalRect.bottom = ovalRect.top + dotDiameter;
                canvas.drawRoundRect(ovalRect, mTextDotRadius, mTextDotRadius, mTipBgPaint);
                textX = ovalRect.left + (ovalWidth - tipTextWidth) * 0.5f;
                canvas.drawText(mTipText, textX, textY, mTipTextPaint);
            }
        } else if (mHasTip) {
            float cx, cy;
            final int tipDotTopMargin = mTipDotTopMargin;
            final int tipDotRightMargin = mTipDotRightMargin;
            cy = tipDotTopMargin + mTipDotRadius;
            cx = (width + mDrawableWidth) * 0.5f + tipDotRightMargin + mTipDotRadius;
            canvas.drawCircle(cx, cy, mTipDotRadius, mTipBgPaint);
        }
    }
}
