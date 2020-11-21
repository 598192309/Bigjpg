package com.bigjpg.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.StringRes;
import android.text.InputType;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/***
 * TextView Spannale
 * ForegroundColorSpannale
 * RelativeSizeSpan
 * StyleSpan
 * ClickableSpan
 */
public class TextViewUtil {

    public static void setUnderline(TextView tv, boolean underlineText){
        TextPaint textPaint = tv.getPaint();
        textPaint.setUnderlineText(underlineText);
    }

    public static String getText(TextView tv) {
        if(tv == null){
            return "";
        }else{
            return tv.getText().toString().trim();
        }
    }

    /**
     * 设置文本框内容,过滤"null"值
     *
     * @param textView
     * @param text
     */
    public static void setText(TextView textView, CharSequence text) {
        setText(textView, text, null);
    }

    public static void setText(TextView textView, @StringRes int resId) {
        setText(textView, textView.getContext().getString(resId), null);
    }

    /**
     * 设置文本框内容，若文本内容为null或为"null"，则显示提示
     *
     * @param textView
     * @param text
     * @param hintText
     */
    public static void setText(TextView textView, CharSequence text, CharSequence hintText) {
        if (textView != null) {
            if (text == null || StringUtil.isEmpty(text.toString())) {
                textView.setText("");
            } else {
                textView.setText(text);
            }

            if (hintText != null) {
                textView.setHint(hintText);
            }
        }

    }

    /**
     * 设置文本框内容，若文本内容为null，则显示提示
     *
     * @param textView
     * @param text
     * @param hintResId
     */
    public static void setText(TextView textView, CharSequence text, int hintResId) {
        if (textView != null) {
            if (text == null || StringUtil.isEmpty(text.toString())) {
                textView.setText("");
            } else {
                textView.setText(text);
            }

            if (hintResId > 0) {
                textView.setHint(hintResId);
            }
        }
    }


    /**
     * 禁止输入框的回车键换行 (通过将换行键设置为GO键实现,会跳往下一个输入焦点框)
     *
     * @param editText
     */
    public static void disableEditTextEnterKeyEvent(EditText editText) {
        try {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setImeOptions(EditorInfo.IME_ACTION_GO);
        } catch (Exception e) {
        }
    }

    /**
     * 设置输入框光标位于文本末尾
     *
     * @param editText
     */
    public static void setCursorAfterText(EditText editText) {
        Spannable spanText = editText.getText();
        if (spanText != null) {
            Selection.setSelection(spanText, spanText.length());
        }
    }

    private static Rect sTextBounds;

    public static void drawTextCenterVertical(Canvas canvas, Paint paint, String text, float cx, float cy) {
        if (sTextBounds == null) {
            sTextBounds = new Rect();
        }
        paint.getTextBounds(text, 0, text.length(), sTextBounds);
        canvas.drawText(text, cx, cy - sTextBounds.exactCenterY(), paint);
    }

    /**
     * 适应文章长度过长的textView
     *
     * @param textView
     * @param maxTextSize
     */
    public static void setTextViewFitText(final TextView textView, final float maxTextSize) {
        if (textView == null) {
            return;
        }
        if (textView.getWidth() == 0) {
            final String text = String.valueOf(textView.getTag());
            textView.setText(text); // 先设上值
            textView.post(new Runnable() {

                @Override
                public void run() {
                    refitTextSize(textView, maxTextSize);
                }
            });
        } else {
            refitTextSize(textView, maxTextSize);
        }
    }

    /**
     * 设置TextView新的字体大小
     *
     * @param textView
     * @param maxTextSize
     */
    public static void refitTextSize(TextView textView, float maxTextSize) {
        final String text = String.valueOf(textView.getTag());
        int textWidth = textView.getWidth();
        int paddingLeft = textView.getPaddingLeft();
        int paddingRight = textView.getPaddingRight();
        TextPaint textPaint = textView.getPaint();
        float minTextSize = 10f;
        if (textWidth <= 0 || text == null || text.length() == 0) {
            return;
        }

        int targetWidth = textWidth - paddingLeft - paddingRight;
        final float threshold = 0.5f; // How close we have to be

        while ((maxTextSize - minTextSize) > threshold) {
            float size = (maxTextSize + minTextSize) / 2;
            textPaint.setTextSize(size);
            if (textPaint.measureText(text) >= targetWidth)
                maxTextSize = size; // too big
            else
                minTextSize = size; // too small
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, minTextSize);
        textView.setText(text);
    }

    /**
     * 获取字符串在控件上显示的行数
     *
     * @param textView
     * @param width    每一行的宽度
     * @param text     字符串
     * @return
     */
    public static int getLineCount(TextView textView, int width, CharSequence text) {
        StaticLayout staticLayout = new StaticLayout(text, textView.getPaint(), width, Layout.Alignment.ALIGN_NORMAL,
                1f, 0f, true);
        return staticLayout.getLineCount();
    }



}
