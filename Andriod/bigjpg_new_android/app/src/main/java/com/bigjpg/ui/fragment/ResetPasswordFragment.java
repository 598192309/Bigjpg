package com.bigjpg.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bigjpg.R;
import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.mvp.presenter.ResetPasswordPresenter;
import com.bigjpg.mvp.view.ResetPasswordView;
import com.bigjpg.ui.base.BaseFragment;
import com.bigjpg.util.AppPref;
import com.bigjpg.util.DialogUtil;
import com.bigjpg.util.StringUtil;
import com.bigjpg.util.TextViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码
 * @author Momo
 * @date 2019-04-19 11:25
 */
public class ResetPasswordFragment extends BaseFragment implements ResetPasswordView {

    private ResetPasswordPresenter mPresenter;

    @BindView(R.id.email)
    EditText mEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String username = AppPref.getInstance().getUserName();
        if(!TextUtils.isEmpty(username)){
            mEmail.setText(username);
            TextViewUtil.setCursorAfterText(mEmail);
        }
    }

    @Override
    protected Presenter onCreatePresenter() {
        mPresenter = new ResetPasswordPresenter();
        return mPresenter;
    }

    @OnClick(R.id.ok)
    void onOkClick(){
        String email = mEmail.getText().toString();
        if(!StringUtil.isEmail(email)){
            showMessageDialog(R.string.username_like);
            return;
        }
        mPresenter.resetPassword(email);
    }

    @Override
    public void onResetPasswordSuccess(HttpResponse response) {
        DialogUtil.showTipDialog(mActivity, null, getString(R.string.check_email), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAttachedActivity();
            }
        }, false);
    }

    @Override
    public void onResetPasswordFailed(HttpResponse response) {
        if (response != null && !TextUtils.isEmpty(response.getStatus())) {
            showMessageDialog(response.getStatus());
        } else {
            showMessageDialog(R.string.error);
        }
    }
}
