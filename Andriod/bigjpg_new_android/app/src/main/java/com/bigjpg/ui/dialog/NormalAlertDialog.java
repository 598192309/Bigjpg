package com.bigjpg.ui.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigjpg.R;
import com.bigjpg.ui.base.BaseDialog;
import com.bigjpg.util.TextViewUtil;
import com.bigjpg.util.ViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class NormalAlertDialog extends BaseDialog {

    @BindView(R.id.dlg_title_tv)
    TextView mTvTitle;
    @BindView(R.id.dlg_content_tv)
    TextView mTvContent;
    @BindView(R.id.dlg_md_confirm_tv)
    TextView mTvConfirm;
    @BindView(R.id.dlg_md_cancel_tv)
    TextView mTvCancel;
    @BindView(R.id.dlg_content_small)
    TextView mTvSmallContent;
    @BindView(R.id.divider)
    View mDivider;

    private String mTitle;
    private String mLeftText;
    private String mRightText;
    private CharSequence mText;
    private View.OnClickListener mLeftListener;
    private View.OnClickListener mRightListener;
    private boolean mLeftButtonVisible;
    private boolean mDismissAfterClick;

    public NormalAlertDialog(Activity activity, Builder builder){
        super(activity);
        setContentView(R.layout.dialog_material_design_alert);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        resetWindowSize();
        mTitle = builder.title;
        mText = builder.text;
        mLeftText = builder.leftText;
        mRightText = builder.rightText;
        mLeftListener = builder.leftListener;
        mRightListener = builder.rightListener;
        mLeftButtonVisible = builder.leftButtonVisible;
        mDismissAfterClick = builder.dismissAfterClick;
        init();
    }

    private void init(){
        if(TextUtils.isEmpty(mTitle)){
            ViewUtil.setViewVisible(mTvTitle, false);
            ViewUtil.setViewVisible(mTvContent, false);
            ViewUtil.setViewVisible(mTvSmallContent, true);
            TextViewUtil.setText(mTvSmallContent, mText);
        }else{
            ViewUtil.setViewVisible(mTvTitle, true);
            ViewUtil.setViewVisible(mTvContent, true);
            ViewUtil.setViewVisible(mTvSmallContent, false);
            TextViewUtil.setText(mTvTitle, mTitle);
            TextViewUtil.setText(mTvContent, mText);
        }


        if(mLeftButtonVisible){
            ViewUtil.setViewVisible(mTvCancel, true);
            if(!TextUtils.isEmpty(mLeftText)){
                TextViewUtil.setText(mTvCancel, mLeftText);
            }
        }else{
            ViewUtil.setViewVisible(mTvCancel, false);
            ViewUtil.setViewVisible(mDivider, false);
            mTvConfirm.setBackgroundResource(R.drawable.btn_dialog_single);
        }

        if(!TextUtils.isEmpty(mRightText)){
            TextViewUtil.setText(mTvConfirm, mRightText);
        }
    }

    @OnClick(R.id.dlg_md_cancel_tv)
    public void onCancelClick(View v){
        if(mDismissAfterClick){
            dismiss();
        }
        if(mLeftListener != null){
            mLeftListener.onClick(v);
        }
    }

    @OnClick(R.id.dlg_md_confirm_tv)
    public void onConfirmClick(View v){
        if(mDismissAfterClick){
            dismiss();
        }
        if(mRightListener != null){
            mRightListener.onClick(v);
        }
    }

    public static class Builder{
        Activity activity;
        String title;
        String leftText;
        String rightText;
        CharSequence text;
        View.OnClickListener leftListener;
        View.OnClickListener rightListener;
        boolean leftButtonVisible = true;
        boolean dismissAfterClick = true;

        public Builder(Activity activity){
            this.activity = activity;
        }

        public Builder setTitle(String title){
            this.title = title;
            return this;
        }

        public Builder setText(CharSequence text){
            this.text = text;
            return this;
        }

        public Builder setLeftText(String leftText){
            this.leftText = leftText;
            return this;
        }

        public Builder setRightText(String rightText){
            this.rightText = rightText;
            return this;
        }

        public Builder setLeftListener(View.OnClickListener leftListener){
            this.leftListener = leftListener;
            return this;
        }


        public Builder setRightListener(View.OnClickListener rightListener){
            this.rightListener = rightListener;
            return this;
        }

        public Builder setLeftButtonVisible(boolean visible){
            this.leftButtonVisible = visible;
            return this;
        }

        public Builder setDismissAfterClick(boolean dismiss){
            this.dismissAfterClick = dismiss;
            return this;
        }

        public NormalAlertDialog create(){
            NormalAlertDialog dialog = new NormalAlertDialog(activity, this);
            return dialog;
        }

        public void show(){
            NormalAlertDialog dialog = create();
            dialog.show();
        }
    }


}
