package com.bigjpg.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bigjpg.R;
import com.bigjpg.exception.InputException;
import com.bigjpg.model.constant.AppIntent;
import com.bigjpg.model.entity.User;
import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.model.response.LoginResponse;
import com.bigjpg.model.response.UserResponse;
import com.bigjpg.model.rest.RetrofitUtil;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.mvp.presenter.SettingPresenter;
import com.bigjpg.mvp.view.SettingView;
import com.bigjpg.ui.base.BaseFragment;
import com.bigjpg.ui.simpleback.SimpleBackPage;
import com.bigjpg.ui.simpleback.SimpleBackUtil;
import com.bigjpg.util.AppPref;
import com.bigjpg.util.AppUtil;
import com.bigjpg.util.DialogUtil;
import com.bigjpg.util.FileUtil;
import com.bigjpg.util.FrescoLoader;
import com.bigjpg.util.LocalBroadcastUtil;
import com.bigjpg.util.ResourcesUtil;
import com.bigjpg.util.StringUtil;
import com.bigjpg.util.UserManager;
import com.bigjpg.util.ViewUtil;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置
 *
 * @author Momo
 * @date 2019-04-09 18:28
 */
public class SettingFragment extends BaseFragment implements SettingView {

    @BindView(R.id.setting_version_bg)
    View mVersionBackground;
    @BindView(R.id.setting_version_status)
    TextView mTvUserVersion;
    @BindView(R.id.setting_expire)
    TextView mTvExpire;
    @BindView(R.id.setting_used)
    TextView mTvUsed;
    @BindView(R.id.setting_email)
    EditText mEdtEmail;
    @BindView(R.id.setting_password)
    EditText mEdtPassword;
    @BindView(R.id.setting_register)
    CheckBox mCbRegister;
    @BindView(R.id.setting_login)
    Button mBtnLogin;
    @BindView(R.id.setting_logout)
    Button mBtnLogout;
    @BindView(R.id.setting_forget_password)
    TextView mTvForgetPassword;
    @BindView(R.id.setting_modify_password)
    TextView mTvModifyPassword;
    @BindView(R.id.setting_qr_layout)
    View mQrCodeLayout;
    @BindView(R.id.setting_version)
    TextView mTvVersion;
    @BindView(R.id.setting_upgrade)
    TextView mTvUpgrade;

    private SettingPresenter mPresenter;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    protected Presenter onCreatePresenter() {
        mPresenter = new SettingPresenter();
        return mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        if (UserManager.getInstance().isLogin()) {
            showLoginStatusView();
            setUserInfoData(UserManager.getInstance().getUser());
        } else {
            showNotLoginStatusView();
            setUserInfoData(new User());
        }

        Configuration configuration = ResourcesUtil.getConfiguration(getActivity());
        Locale locale = configuration.locale;
        String language = locale.getLanguage();
        if (language != null && language.endsWith("zh") && !"TW".equals(locale.getCountry())) {
            ViewUtil.setViewVisible(mQrCodeLayout, true);
        } else {
            ViewUtil.setViewVisible(mQrCodeLayout, false);
        }

        mTvVersion.setText("v"+AppUtil.getCurVersionName(getContext()));
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (mIsCreated) {
            if (UserManager.getInstance().isLogin()) {
                mPresenter.getUserInfo();
            } else {
                showNotLoginStatusView();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsCreated && isShown()) {
            if (UserManager.getInstance().isLogin()) {
                mPresenter.getUserInfo();
            }
        }
    }

    @OnClick(R.id.setting_login)
    void onLoginClick() {
        try {
            String email = mEdtEmail.getText().toString().trim();
            String password = mEdtPassword.getText().toString().trim();
            if (checkInput(email, password)) {
                if (mCbRegister.isChecked()) {
                    mPresenter.register(email, password);
                } else {
                    mPresenter.login(email, password);
                }
            }
        } catch (InputException e) {
            showMessageDialog(e.getMessage());
        }
    }

    @OnClick(R.id.setting_qr)
    void onQrCodeClick() {
        if (!AppUtil.hasRequiredPermission()) {
            LocalBroadcastUtil.sendBroadcast(getContext(), AppIntent.ACTION_PERMISSION);
            return;
        }

        showLoadingDialog(R.string.begin_download);
        saveQRCodeToGallery();
    }

    private void saveQRCodeToGallery() {
        FrescoLoader.getBitmap(getContext(), FrescoLoader.createDrawableImageUri(R.drawable.wechat_qr), new FrescoLoader.OnLoadBitmapListener() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap) {
                final Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoadingDialog();
                            String imagePath = File.separator +  "wechat_qr" + System.currentTimeMillis() + ".jpg";
                            String simplePath = AppUtil.getBijpgSimpleFilePath() + imagePath;
                            String filePath = AppUtil.getBigjpgFilePath() + imagePath;
                            boolean saved = FileUtil.saveBitmap(bitmap, filePath, Bitmap.CompressFormat.JPEG);
                            if (saved) {
                                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
                                showToast(getString(R.string.save_to) + simplePath);
                            } else {
                                showToast(R.string.error);
                            }
                        }
                    });
                }
            }

            @Override
            public void onBitmapLoadFailure() {
                final Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoadingDialog();
                            showToast(R.string.error);
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.setting_share)
    void onShareClick() {
        String title = getString(R.string.title);
        AppUtil.shareText(getActivity(), title, title + RetrofitUtil.HOST_URL);
    }

    @OnClick(R.id.setting_visit)
    void onVisitClick() {
        AppUtil.callOuterBrowser(getActivity(), RetrofitUtil.HOST_URL);
    }

    @OnClick(R.id.setting_feedback)
    void onFeedbackClick() {
        AppUtil.callOuterBrowser(getActivity(), "mailto:i@bigjpg.com&subject=Bigjpg");
    }

    /**
     * 已登录状态
     */
    private void showLoginStatusView() {
        ViewUtil.setViewVisible(mEdtEmail, false);
        ViewUtil.setViewVisible(mEdtPassword, false);
        ViewUtil.setViewVisible(mCbRegister, false);
        ViewUtil.setViewVisible(mBtnLogout, true);
        ViewUtil.setViewVisible(mBtnLogin, false);
        ViewUtil.setViewVisible(mTvForgetPassword, false);
        ViewUtil.setViewVisible(mTvModifyPassword, true);

        String username = AppPref.getInstance().getUserName();
        if (!TextUtils.isEmpty(username)) {
            mBtnLogout.setText(getString(R.string.logout) + " " + username);
        }
    }

    private void setUserInfoData(User user) {
        Activity activity = getActivity();
        if (User.Version.NONE.equals(user.version)) {
            mVersionBackground.setBackground(ResourcesUtil.getDrawable(activity, R.drawable.shape_light_gray));
            mTvUserVersion.setTextColor(ResourcesUtil.getColor(activity, R.color.text_black));
            mTvExpire.setTextColor(ResourcesUtil.getColor(activity, R.color.text_black));
            mTvUsed.setTextColor(ResourcesUtil.getColor(activity, R.color.text_black));
            mTvUserVersion.setText(ResourcesUtil.getString(activity, R.string.version_free));
            mTvUpgrade.setBackgroundResource(R.drawable.btn_green_2);
        } else if (User.Version.BASIC.equals(user.version)) {
            mVersionBackground.setBackground(ResourcesUtil.getDrawable(activity, R.drawable.shape_purple));
            mTvUserVersion.setTextColor(ResourcesUtil.getColor(activity, R.color.white));
            mTvExpire.setTextColor(ResourcesUtil.getColor(activity, R.color.white));
            mTvUsed.setTextColor(ResourcesUtil.getColor(activity, R.color.white));
            mTvUserVersion.setText(ResourcesUtil.getString(activity, R.string.version_basic));
            mTvUpgrade.setBackgroundResource(R.drawable.btn_round_border_light);
        } else if (User.Version.STANDARD.equals(user.version)) {
            mVersionBackground.setBackground(ResourcesUtil.getDrawable(activity, R.drawable.shape_green));
            mTvUserVersion.setTextColor(ResourcesUtil.getColor(activity, R.color.white));
            mTvExpire.setTextColor(ResourcesUtil.getColor(activity, R.color.white));
            mTvUsed.setTextColor(ResourcesUtil.getColor(activity, R.color.white));
            mTvUserVersion.setText(ResourcesUtil.getString(activity, R.string.version_standard));
            mTvUpgrade.setBackgroundResource(R.drawable.btn_round_border_yellow);
        } else if (User.Version.PRO.equals(user.version)) {
            mVersionBackground.setBackground(ResourcesUtil.getDrawable(activity, R.drawable.shape_blue));
            mTvUserVersion.setTextColor(ResourcesUtil.getColor(activity, R.color.white));
            mTvExpire.setTextColor(ResourcesUtil.getColor(activity, R.color.white));
            mTvUsed.setTextColor(ResourcesUtil.getColor(activity, R.color.white));
            mTvUserVersion.setText(ResourcesUtil.getString(activity, R.string.version_premium));
            mTvUpgrade.setBackgroundResource(R.drawable.btn_round_border_light);
        } else {
            mVersionBackground.setBackground(ResourcesUtil.getDrawable(activity, R.drawable.shape_light_gray));
            mTvUserVersion.setTextColor(ResourcesUtil.getColor(activity, R.color.text_black));
            mTvExpire.setTextColor(ResourcesUtil.getColor(activity, R.color.text_black));
            mTvUsed.setTextColor(ResourcesUtil.getColor(activity, R.color.text_black));
            mTvUserVersion.setText(ResourcesUtil.getString(activity, R.string.version_free));
            mTvUpgrade.setBackgroundResource(R.drawable.btn_green_2);
        }

        if(UserManager.getInstance().isLogin()){
            if(StringUtil.isEmpty(UserManager.getInstance().getUser().version)){
                // 免费版
                mTvExpire.setText("");
            }else{
                if (user.is_expire) {
                    // 已过期
                    mTvExpire.setText(R.string.expire);
                } else {
                    // 2020-04-14 00:00:00
                    if (user.expire != null && user.expire.length() >= 10) {
                        mTvExpire.setText(user.expire.substring(0, 10));
                    } else {
                        mTvExpire.setText(user.expire);
                    }
                }
            }
        }else{
            mTvExpire.setText("");
        }

        if (user.used > 0) {
            mTvUsed.setText(getString(R.string.used) + user.used);
        } else {
            mTvUsed.setText("");
        }

    }

    /**
     * 未登录状态
     */
    private void showNotLoginStatusView() {
        ViewUtil.setViewVisible(mEdtEmail, true);
        ViewUtil.setViewVisible(mEdtPassword, true);
        ViewUtil.setViewVisible(mCbRegister, true);
        ViewUtil.setViewVisible(mBtnLogout, false);
        ViewUtil.setViewVisible(mBtnLogin, true);
        ViewUtil.setViewVisible(mTvForgetPassword, true);
        ViewUtil.setViewVisible(mTvModifyPassword, false);

        mEdtPassword.setText("");
        mCbRegister.setChecked(false);
        String username = AppPref.getInstance().getUserName();
        if (!TextUtils.isEmpty(username)) {
            mEdtEmail.setText(username);
            ViewUtil.setCursorAfterText(mEdtEmail);
        }
    }

    private boolean checkInput(String email, String password) throws InputException {
        if (!StringUtil.isEmail(email)) {
            throw new InputException(getString(R.string.username_like));
        }
        if (TextUtils.isEmpty(password)) {
            throw new InputException(getString(R.string.password_error));
        }
        return true;
    }

    @OnClick(R.id.setting_logout)
    void onLogoutClick() {
        DialogUtil.showTwoButtonTipDialog(getActivity(), getString(R.string.logout), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.logout();
            }
        });
    }

    @OnClick(R.id.setting_forget_password)
    void onForgetPasswordClick() {
        SimpleBackUtil.show(getActivity(), SimpleBackPage.ResetPasswordFragment);
    }

    @OnClick(R.id.setting_modify_password)
    void onModifyPasswordClick() {
        SimpleBackUtil.show(getActivity(), SimpleBackPage.ChangePasswordFragment);
    }

    @OnClick(R.id.setting_upgrade)
    void onUpgradeClick() {
        SimpleBackUtil.show(getActivity(), SimpleBackPage.UpgradeFragment);
    }

    @OnClick(R.id.setting_conf)
    void onMoreSettingClick() {
        SimpleBackUtil.show(getActivity(), SimpleBackPage.MoreSettingFragment);
    }

    @Override
    public void onRegisterSuccess(LoginResponse response) {
        onLoginSuccess(response);
        showToast(R.string.succ);
    }

    @Override
    public void onRegisterFailed(LoginResponse response) {
        onLoginFailed(response);
    }

    @Override
    public void onLoginSuccess(LoginResponse response) {
        showLoginStatusView();
        mPresenter.getUserInfo();
    }

    @Override
    public void onLoginFailed(LoginResponse response) {
        if (response != null) {
            if (LoginResponse.Status.ERROR.equals(response.getStatus())) {
                showMessageDialog(R.string.error);
            } else if (LoginResponse.Status.PASSWORD_ERROR.equals(response.getStatus())) {
                showMessageDialog(R.string.password_error);
            } else if (LoginResponse.Status.NOT_EXIST.equals(response.getStatus())) {
                showMessageDialog(R.string.user_not_exist);
            }
        } else {
            showMessageDialog(R.string.error);
        }
    }

    @Override
    public void onLogoutSuccess() {
        showNotLoginStatusView();
        setUserInfoData(new User());
        LocalBroadcastUtil.sendBroadcast(getActivity(), AppIntent.ACTION_LOGOUT);
    }

    @Override
    public void onLogoutFailed() {

    }

    @Override
    public void onGetUserInfoSuccess(UserResponse response) {
        User user = response.getUser();
        setUserInfoData(user);
    }

    @Override
    public void onGetUserInfoFailed(UserResponse response) {
        if (response != null && HttpResponse.Status.NO_LOGIN.equals(response.getStatus())) {
            UserManager.getInstance().logout(getContext(), null);
            showNotLoginStatusView();
        }
    }
}
