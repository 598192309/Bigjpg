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
import com.bigjpg.mvp.presenter.ChangePasswordPresenter;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.mvp.view.ChangePasswordView;
import com.bigjpg.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码
 *
 * @author Momo
 * @date 2019-04-19 11:25
 */
public class ChangePasswordFragment extends BaseFragment implements ChangePasswordView {

    private ChangePasswordPresenter mPresenter;

    @BindView(R.id.new_password)
    EditText mEdtPassword;
    @BindView(R.id.password_again)
    EditText mEdtAgain;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    protected Presenter onCreatePresenter() {
        mPresenter = new ChangePasswordPresenter();
        return mPresenter;
    }

    @Override
    public void onChangePasswordSuccess(HttpResponse response) {
        showToast(R.string.reset_success);
        finishAttachedActivity();
    }

    @Override
    public void onChangePasswordFailed(HttpResponse response) {
        if (response != null && !TextUtils.isEmpty(response.getStatus())) {
            showMessageDialog(response.getStatus());
        } else {
            showMessageDialog(R.string.error);
        }
    }

    @OnClick(R.id.ok)
    void onOkClick() {
        String password1 = mEdtPassword.getText().toString();
        String password2 = mEdtAgain.getText().toString();
        if (TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
            showMessageDialog(R.string.input_new_password);
            return;
        }

        if (!TextUtils.equals(password1, password2)) {
            showMessageDialog(R.string.password_not_same);
            return;
        }

        mPresenter.changePassword(password1);
    }
}
