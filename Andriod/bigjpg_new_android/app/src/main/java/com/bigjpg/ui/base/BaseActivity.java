package com.bigjpg.ui.base;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bigjpg.R;
import com.bigjpg.application.BigJPGApplication;
import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.mvp.view.IView;
import com.bigjpg.ui.dialog.LoadingDialog;
import com.bigjpg.util.AppLanguageUtils;
import com.bigjpg.util.AppManager;
import com.bigjpg.util.AppToast;
import com.bigjpg.util.DialogUtil;

import butterknife.ButterKnife;

/**
 * BaseActivity
 *
 * @author Momo
 * @date 2019-04-08 16:20
 */
public abstract class BaseActivity extends AppCompatActivity implements IView {

    private Presenter mPresenter;
    private LoadingDialog mLoadingDialog;
    private ViewGroup mRootView;
    private LinearLayout mTitleContainer;
    private Toolbar mToolbar;
    private boolean mIsRecreated;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsRecreated = (savedInstanceState != null);
        AppManager.getInstance().addActivity(this);
        createViewFrame(savedInstanceState);
        ButterKnife.bind(this);
        initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
    }

    private void initPresenter() {
        if (mPresenter == null) {
            mPresenter = onCreatePresenter();
        }
    }

    protected boolean isRecreated() {
        return mIsRecreated;
    }

    private void createViewFrame(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(this);
        mRootView = onCreateRootView(inflater, savedInstanceState);
        mTitleContainer = findViewById(mRootView, R.id.header_title);

        //1.Title
        View titleView = onCreateTitleView(inflater, mTitleContainer, savedInstanceState);
        if (titleView != null && mTitleContainer != null) {
            if (mTitleContainer.getChildCount() != 0) {
                mTitleContainer.removeAllViews();
            }
            addTitleView(titleView);
        }

        //2.Content
        View contentView = onCreateContentView(inflater, mRootView, savedInstanceState);
        if (contentView != null) {
            addContentView(contentView);
        }

        if (mTitleContainer == null) {
            mToolbar = findViewById(mRootView, R.id.tool_bar);
        } else {
            mToolbar = findViewById(mTitleContainer, R.id.tool_bar);
        }

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            initToolbar(mToolbar);
        }

        if (mRootView.getParent() == null) {
            setContentView(mRootView);
        }
    }


    protected void initToolbar(Toolbar toolbar) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static <T extends View> T findViewById(View view, int id) {
        return (T) view.findViewById(id);
    }

    protected ViewGroup getRootView() {
        return mRootView;
    }

    protected ViewGroup onCreateRootView(LayoutInflater inflater, Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.activity_frame, null);
    }

    /**
     * 创建标题
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected View onCreateTitleView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    protected void addTitleView(View titleView) {
        mTitleContainer.addView(titleView);
    }

    protected void addContentView(View contentView) {
        if (contentView.getParent() == null && contentView != mRootView) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mRootView.addView(contentView, params);
        }
    }

    protected View getTitleBar() {
        return mTitleContainer;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(int tipsId) {
        AppToast.showToast(this, tipsId, Toast.LENGTH_SHORT);
    }

    @Override
    public void showToast(String tips) {
        AppToast.showToast(this, tips, Toast.LENGTH_SHORT);
    }

    @Override
    public void showToast(String msg, int duration) {
        AppToast.showToast(this, msg, duration);
    }

    @Override
    public void showToast(int msgResId, int duration) {
        AppToast.showToast(this, msgResId, duration);
    }

    @Override
    public void showLoadingDialog(String resId) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }

        mLoadingDialog.setText(resId);
        mLoadingDialog.show();
    }

    @Override
    public void showLoadingDialog(int resId) {
        showLoadingDialog(getString(resId));
    }

    @Override
    public void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.cancel();
        }
    }

    @Override
    public void showErrorView(Throwable e) {
        if (e != null && e.getMessage() != null) {
            showToast(e.getMessage());
        }
    }

    @Override
    public void showMessageDialog(String message) {
        if (!isFinishing() && message != null) {
            DialogUtil.showTipDialog(this, null, message, null, false);
        }
    }

    @Override
    public void showMessageDialog(@StringRes int msgResId) {
        if (!isFinishing()) {
            DialogUtil.showTipDialog(this, null, getString(msgResId), null, false);
        }
    }

    @Override
    public void showMessageDialog(HttpResponse response, int msgResId) {
        if (response != null) {
            if (TextUtils.isEmpty(response.getStatus())) {
                showMessageDialog(msgResId);
            } else {
                showMessageDialog(response.getStatus());
            }
        } else {
            showMessageDialog(msgResId);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = AppLanguageUtils.attachBaseContext(newBase, BigJPGApplication.getInstance().getAppLanguage(newBase));
        super.attachBaseContext(context);
    }

    protected abstract View onCreateContentView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState);

    protected abstract Presenter onCreatePresenter();
}
