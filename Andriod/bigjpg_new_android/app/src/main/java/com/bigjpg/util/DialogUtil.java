package com.bigjpg.util;

import android.app.Activity;
import android.app.Dialog;
import androidx.annotation.StringRes;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import com.bigjpg.R;
import com.bigjpg.ui.dialog.ListDialog;
import com.bigjpg.ui.dialog.NormalAlertDialog;
import com.bigjpg.ui.simpleback.SimpleBackPage;
import com.bigjpg.ui.simpleback.SimpleBackUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:Dialogs
 *
 * @author mfx
 * @date 2014年4月26日 下午11:49:31
 */
public class DialogUtil {

    private DialogUtil() {
    }

    public static class ListContent {
        private String text;
        private OnClickListener listener;

        public ListContent(String text, OnClickListener listener) {
            this.text = text;
            this.listener = listener;
        }
    }

    public static Dialog showListDialog(Activity activity, List<String> data,
                                        final OnListDialogItemClickListener listener) {
        final ListDialog dialog = new ListDialog(activity);
        dialog.setData(data);

        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemClick(position);
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }

    public static Dialog showListDialog(Activity activity, final List<ListContent> data) {
        final ListDialog dialog = new ListDialog(activity);
        List<String> texts = new ArrayList<>();
        for (ListContent content : data) {
            texts.add(content.text);
        }
        dialog.setData(texts);
        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListContent content = data.get(position);
                content.listener.onClick(view);
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }


    public interface OnListDialogItemClickListener {
        void onItemClick(int position);
    }


    public static Dialog showTipDialog(Activity activity, CharSequence text) {
        return showTipDialog(activity, null, text, null, true);
    }

    public static Dialog showTipDialog(Activity activity, @StringRes int textId) {
        return showTipDialog(activity, null, activity.getString(textId), null, true);
    }


    public static Dialog showTipDialog(Activity activity, String title, CharSequence tip, final OnClickListener onOkClickListener, boolean cancelable) {
        if (activity.isFinishing()) {
            return null;
        }
        try {
            NormalAlertDialog.Builder builder = new NormalAlertDialog.Builder(activity);
            builder.setTitle(title);
            builder.setText(tip);
            builder.setRightListener(onOkClickListener);
            builder.setLeftButtonVisible(false);
            NormalAlertDialog dialog = builder.create();
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(cancelable);
            dialog.show();
            return dialog;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 一个按钮的无标题对话框提示
     */
    public static Dialog showOneButtonTipDialog(Activity activity, final CharSequence tip) {
        return showOneButtonTipDialog(activity, tip, false);
    }

    public static Dialog showOneButtonTipDialog(Activity activity, @StringRes final int tipsId) {
        return showOneButtonTipDialog(activity, ResourcesUtil.getString(activity, tipsId), false);
    }

    public static Dialog showOneButtonTipDialog(Activity activity, final CharSequence tip, boolean cancelable) {
        return showOneButtonTipDialog(activity, tip, null, cancelable);
    }

    public static Dialog showOneButtonTipDialog(Activity activity, final CharSequence tip, final OnClickListener listener, boolean cancelable) {
        return showOneButtonTipDialog(activity, null, tip, listener, cancelable);
    }

    /**
     * 一个按钮的对话框提示
     */
    public static Dialog showOneButtonTipDialog(Activity activity, String title, CharSequence text, final OnClickListener listener, boolean cancelable) {
        if (activity.isFinishing()) {
            return null;
        }

        try {
            NormalAlertDialog.Builder builder = new NormalAlertDialog.Builder(activity);
            builder.setTitle(title);
            builder.setText(text);
            builder.setRightListener(listener);
            builder.setLeftButtonVisible(false);
            NormalAlertDialog dialog = builder.create();
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(cancelable);
            dialog.show();
            return dialog;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 显示确认和取消按钮,只带标题的对话框提示
     */
    public static Dialog showTwoButtonTipDialog(Activity activity, String title, OnClickListener confirmListener) {
        return showTwoButtonTipDialog(activity, null, title, null, confirmListener);
    }

    public static Dialog showTwoButtonTipDialog(Activity activity, String title, OnClickListener confirmListener, boolean canceledOnTouchOutside) {
        return showTwoButtonTipDialog(activity, null, title, null, confirmListener, canceledOnTouchOutside);
    }

    /**
     * 两个按钮的带标题对话框提示
     */
    public static Dialog showTwoButtonTipDialog(Activity activity, final String title, final CharSequence text,
                                                final String rightBtnText, final OnClickListener rightBtnListener) {
        return showTwoButtonTipDialog(activity, title, text, activity.getString(R.string.cancel), rightBtnText, null, rightBtnListener);
    }

    public static Dialog showTwoButtonTipDialog(Activity activity, final String title, final CharSequence text,
                                                final String rightBtnText, final OnClickListener rightBtnListener, boolean canceledOnTouchOutside) {
        return showTwoButtonTipDialog(activity, title, text, activity.getString(R.string.cancel), rightBtnText, null,
                rightBtnListener, canceledOnTouchOutside);
    }

    public static Dialog showTwoButtonTipDialog(Activity activity, final CharSequence text,
                                                final String leftBtnText, final String rightBtnText, final OnClickListener leftBtnListener,
                                                final OnClickListener rightBtnListener) {
        return showTwoButtonTipDialog(activity, null, text, leftBtnText, rightBtnText, leftBtnListener, rightBtnListener, false);
    }

    public static Dialog showTwoButtonTipDialog(Activity activity, final String title, final CharSequence text,
                                                final String leftBtnText, final String rightBtnText, final OnClickListener leftBtnListener,
                                                final OnClickListener rightBtnListener) {
        return showTwoButtonTipDialog(activity, title, text, leftBtnText, rightBtnText, leftBtnListener, rightBtnListener, false);
    }

    public static Dialog showTwoButtonTipDialog(Activity activity, final String title, final CharSequence text,
                                                final String leftBtnText, final String rightBtnText, final OnClickListener leftBtnListener,
                                                final OnClickListener rightBtnListener, boolean canceledOnTouchOutside) {
        return showTwoButtonTipDialog(activity, title, text, leftBtnText, rightBtnText, leftBtnListener, rightBtnListener, canceledOnTouchOutside, true);
    }

    public static Dialog showTwoButtonTipDialog(Activity activity, final String title, final CharSequence text,
                                                final String leftBtnText, final String rightBtnText, final OnClickListener leftBtnListener,
                                                final OnClickListener rightBtnListener, boolean canceledOnTouchOutside, boolean cancelable) {
        if (activity.isFinishing()) {
            return null;
        }

        try {
            NormalAlertDialog.Builder builder = new NormalAlertDialog.Builder(activity);
            builder.setTitle(title);
            builder.setText(text);
            builder.setLeftText(leftBtnText);
            builder.setRightText(rightBtnText);
            builder.setLeftListener(leftBtnListener);
            builder.setRightListener(rightBtnListener);
            NormalAlertDialog dialog = builder.create();
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            dialog.show();
            return dialog;
        } catch (Exception e) {
            return null;
        }
    }

    public static Dialog showUpgradeDialog(final Activity activity, final String text) {
        Dialog dialog = DialogUtil.showTwoButtonTipDialog(activity, null, text, activity.getString(R.string.upgrade), new OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleBackUtil.show(activity, SimpleBackPage.UpgradeFragment);
            }
        });
        return dialog;
    }

}
