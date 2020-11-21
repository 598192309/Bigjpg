package com.bigjpg.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import androidx.annotation.DrawableRes;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigjpg.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

public class ViewUtil {

    private static final float BANNER_ADJUST_PARAMS_MIDDLE = 3.05f;
    private static final float BANNER_ADJUST_PARAMS_LARGE = 1.93f;

    /**
     * 计算字体高度
     */
    public static int calculateFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
        return (int) (Math.ceil(fm.descent - fm.ascent) * 0.8f);
    }

    /**
     * 计算字体高度
     *
     * @param textPaint
     * @return
     */
    public static float calculateFontHeight(Paint textPaint) {
        FontMetrics fontMetrics = textPaint.getFontMetrics();
        return fontMetrics.bottom - fontMetrics.top;
    }

    /**
     * 计算文字baseline
     *
     * @param rectHeight 要显示的区域高度
     * @param textPaint
     * @return
     */
    public static float getTextBaseLine(float rectHeight, Paint textPaint) {
        float fontHeight = calculateFontHeight(textPaint);
        float textBaseY;
        if (fontHeight <= rectHeight) {
            textBaseY = rectHeight - (rectHeight - fontHeight) / 2 - textPaint.getFontMetrics().bottom;
        } else {
            textBaseY = rectHeight;
        }
        return textBaseY;
    }


    /**
     * 计算字体高度
     *
     * @param textPaint
     * @return
     */
    public static float calculateFontHeight(TextPaint textPaint) {
        FontMetrics fontMetrics = textPaint.getFontMetrics();
        return fontMetrics.bottom - fontMetrics.top;
    }

    /**
     * 计算字符串长度
     */
    public static int calculateStringWidth(Paint paint, String s) {
        int iRet = 0;
        if (s != null && s.length() > 0) {
            int len = s.length();
            float[] widths = new float[len];
            paint.getTextWidths(s, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
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
     * Returns the current View.OnClickListener for the given View
     *
     * @param view the View whose click listener to retrieve
     * @return the View.OnClickListener attached to the view; null if it could
     * not be retrieved
     */
    public static View.OnClickListener getOnClickListener(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getOnClickListenerV14(view);
        } else {
            return getOnClickListenerV(view);
        }
    }

    // Used for APIs lower than ICS (API 14)
    private static View.OnClickListener getOnClickListenerV(View view) {
        View.OnClickListener retrievedListener = null;
        String viewStr = "android.view.View";
        Field field;

        try {
            field = Class.forName(viewStr).getDeclaredField("mOnClickListener");
            retrievedListener = (View.OnClickListener) field.get(view);
        } catch (NoSuchFieldException ex) {
            Log.e("Reflection", "No Such Field.");
        } catch (IllegalAccessException ex) {
            Log.e("Reflection", "Illegal Access.");
        } catch (ClassNotFoundException ex) {
            Log.e("Reflection", "Class Not Found.");
        }

        return retrievedListener;
    }

    // Used for new ListenerInfo class structure used beginning with API 14
    // (ICS)
    private static View.OnClickListener getOnClickListenerV14(View view) {
        View.OnClickListener retrievedListener = null;
        String viewStr = "android.view.View";
        String lInfoStr = "android.view.View$ListenerInfo";

        try {
            Field listenerField = Class.forName(viewStr).getDeclaredField("mListenerInfo");
            Object listenerInfo = null;

            if (listenerField != null) {
                listenerField.setAccessible(true);
                listenerInfo = listenerField.get(view);
            }

            Field clickListenerField = Class.forName(lInfoStr).getDeclaredField("mOnClickListener");

            if (clickListenerField != null && listenerInfo != null) {
                retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);
            }
        } catch (NoSuchFieldException ex) {
            Log.e("Reflection", "No Such Field.");
        } catch (IllegalAccessException ex) {
            Log.e("Reflection", "Illegal Access.");
        } catch (ClassNotFoundException ex) {
            Log.e("Reflection", "Class Not Found.");
        }

        return retrievedListener;
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
     * 设置文本框内容，若文本内容为null，则显示提示
     *
     * @param textView
     * @param text
     * @param hintResId
     */
    public static void setText(TextView textView, String text, int hintResId) {
        if (text == null && hintResId > 0) {
            textView.setHint(hintResId);
        } else {
            textView.setText(text);
        }
    }

    /**
     * 设置文本框内容，若文本内容为null或为"null"，则显示提示
     *
     * @param textView
     * @param text
     * @param hintText
     */
    public static void setText(TextView textView, String text, String hintText) {
        if (text == null || "null".equals(text)) {
            if (hintText != null) {
                textView.setHint(hintText);
            }
        } else {
            textView.setText(text);
        }
    }

    /**
     * 设置文本框内容,过滤"null"值
     *
     * @param textView
     * @param text
     */
    public static void setText(TextView textView, String text) {
        setText(textView, text, null);
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

    /**
     * 获取字符串在控件上显示的行数
     *
     * @param textView
     * @param width    每一行的宽度
     * @param text     字符串
     * @return
     */
    public static int getLineCount(TextView textView, int width, String text) {
        StaticLayout staticLayout = new StaticLayout(text, textView.getPaint(), width, Layout.Alignment.ALIGN_NORMAL,
                1f, 0f, true);
        return staticLayout.getLineCount();
    }

    /**
     * 隐藏软键盘
     *
     * @param et
     */
    public static void hideSoftKeyboard(EditText et) {
        Context context = et.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     *
     * @param et
     */
    public static void showSoftKeyboard(EditText et) {
        Context context = et.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
    }

    public static void showKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static Activity scanForActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * 获取应用程序的所占用的屏幕
     *
     * @param activity
     * @return
     */
    @SuppressLint("NewApi")
    public static int getAppScreenWidth(Activity activity) {
        Display dis = activity.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 13) {
            Point p = new Point();
            dis.getSize(p);
            return p.x;
        } else {
            return dis.getWidth();
        }
    }



    public static void setBoldStyleText(TextView view, String text, String keyword) {
        view.setText(getBoldStyleText(text, keyword));
    }

    public static CharSequence getBoldStyleText(String text, String keyword) {
        int start = text.toLowerCase(Locale.ENGLISH).indexOf(keyword.toLowerCase(Locale.ENGLISH));
        if (start < 0) {
            return text;
        } else {
            int end = start + keyword.length();
            if (end <= text.length()) {
                SpannableString spanStr = new SpannableString(text);
                spanStr.setSpan(new StyleSpan(Typeface.BOLD), start, start + keyword.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                return spanStr;
            }
            return text;
        }
    }


    /**
     * 设置ViewPager样式使得更加适应应用
     */
    public static void setViewPagerPerfectStyle(ViewPager viewPager) {
        viewPager.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
    }

    public static void setSwipeRefreshLayoutStyle(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent);
    }

    public static void adjustTextPaint(Paint paint, float textDisplayWidth, String text) {
        float textSize = paint.getTextSize();
        int textLength = ViewUtil.calculateStringWidth(paint, text);
        while (textLength > textDisplayWidth && textSize > 0) {
            textSize -= 1;
            paint.setTextSize(textSize);
            textLength = ViewUtil.calculateStringWidth(paint, text);
        }
    }

    /**
     * 获取应用程序的屏幕宽度和高度
     *
     * @param activity
     * @return
     */
    @SuppressLint("NewApi")
    public static int[] getAppScreen(Activity activity) {
        int[] wh = new int[2];
        Display dis = activity.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 13) {
            Point p = new Point();
            dis.getSize(p);
            wh[0] = p.x;
            wh[1] = p.y;
            return wh;
        } else {
            wh[0] = dis.getWidth();
            wh[1] = dis.getHeight();
            return wh;
        }
    }

    /**
     * 获取应用程序的屏幕高度
     *
     * @param activity
     * @return
     */
    public static int getAppScreenHeight(Activity activity) {
        return getAppScreen(activity)[1];
    }

    private static long sLastViewClickTime;
    private static long sGentleLastViewClickTime;
    public static final long CLICK_INTERVAL = 800L;
    public static final long GENTLE_CLICK_INTERVAL = 400L;

    /**
     * 是否是连续快速点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeDiff = time - sLastViewClickTime;
        if (timeDiff < CLICK_INTERVAL) {
            return true;
        }
        sLastViewClickTime = time;
        return false;
    }


    public static boolean isGentleFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeDiff = time - sGentleLastViewClickTime;
        if (timeDiff < GENTLE_CLICK_INTERVAL) {
            return true;
        }
        sGentleLastViewClickTime = time;
        return false;
    }


    /**
     * 滚动ListView到上方
     *
     * @param listView
     */
    public static void scrollListViewToTop(final ListView listView) {
        if (listView != null && listView.getChildCount() > 0) {
            listView.smoothScrollToPositionFromTop(0, 0, 100);
            listView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listView.smoothScrollToPositionFromTop(0, 0, 0);
                    listView.setSelection(0);
                }
            }, 100);
        }
    }

    /**
     * 滚动RecyclerView到上方
     */
    public static void scrollRecyclerViewToTop(final RecyclerView recyclerView) {
        if (recyclerView != null && recyclerView.getChildCount() > 0) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    public static void setListViewTransparentDivider(ListView listView) {
        listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        listView.setDividerHeight(AppUtil.dipToPx(listView.getContext(), 5));
    }

    public static void setViewPagerPageMargin(ViewPager viewPager) {
        viewPager.setPageMargin(AppUtil.dipToPx(viewPager.getContext(), 5));
    }

    public static void setViewVisible(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }


    public static void setViewHidden(View view, boolean hidden) {
        view.setVisibility(hidden ? View.INVISIBLE : View.VISIBLE);
    }

    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static void setViewEnable(View view, boolean enabled) {
        view.setEnabled(enabled);
    }

    public static StateListDrawable createBackgroundColorSelector(Context context, int normalColor, int pressedColor) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(pressedColor));
        states.addState(new int[]{}, new ColorDrawable(normalColor));
        return states;
    }

    public static ColorStateList createTextColorSelector(Context context, int normalColor, int pressedColor) {
        ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{}}, new int[]{pressedColor, normalColor});
        return colorStateList;
    }

    public static StateListDrawable createBackgroundDrawableSelector(Drawable normal, Drawable pressed) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, pressed);
        states.addState(new int[]{}, normal);
        return states;
    }

    public static void setPaddingLeft(View view, int value) {
        view.setPadding(value, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setPaddingTop(View view, int value) {
        view.setPadding(view.getPaddingLeft(), value, view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setPaddingRight(View view, int value) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), value, view.getPaddingBottom());
    }

    public static void setPaddingBottom(View view, int value) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), value);
    }


}
