package com.bigjpg.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bigjpg.R;
import com.bigjpg.application.BigJPGApplication;
import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.mvp.view.IView;
import com.bigjpg.ui.dialog.LoadingDialog;
import com.bigjpg.util.AppToast;
import com.bigjpg.util.DialogUtil;
import com.squareup.leakcanary.RefWatcher;

import java.net.UnknownHostException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 描述:Fragment基类，提供UI刷新
 *
 * @author mfx
 * @date 2014年4月19日 下午7:16:20
 */
public abstract class BaseFragment extends Fragment implements IView {

    protected Activity mActivity;
    protected Handler mUiHandler;
    protected boolean mIsVisibleToUser = false;
    protected boolean mIsCreated = false;
    private boolean mIsPause = true;

    private Presenter mPresenter;
    private LoadingDialog mLoadingDialog;
    private Unbinder mUnbinder;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            mUnbinder = ButterKnife.bind(this, view);
        }

        initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mUiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (isAdded()) {
                    handleUiMessage(msg);
                }
            }
        };
        mIsCreated = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.unsubscribe();
            mPresenter.detachView(getRetainInstance());
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    private void initPresenter() {
        if (mPresenter == null) {
            mPresenter = onCreatePresenter();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsPause = true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = BigJPGApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    public boolean isPause() {
        return mIsPause;
    }

    protected void handleUiMessage(Message msg) {
    }

    protected void sendUiMessage(Message msg) {
        if (mUiHandler != null) {
            mUiHandler.sendMessage(msg);
        }
    }

    protected void sendUiMessage(int what, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        sendUiMessage(msg);
    }

    protected void sendEmptyUiMessage(int what) {
        if (mUiHandler != null) {
            mUiHandler.sendEmptyMessage(what);
        }
    }

    protected void removeUiMessages(int what) {
        if (mUiHandler != null) {
            mUiHandler.removeMessages(what);
        }
    }

    protected void sendUiMessageDelayed(int what, Object obj, long delayMillis) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        sendUiMessageDelayed(msg, delayMillis);
    }

    protected void sendUiMessageDelayed(Message msg, long delayMillis) {
        if (mUiHandler != null) {
            mUiHandler.sendMessageDelayed(msg, delayMillis);
        }
    }

    protected void sendEmptyUiMessageDelayed(int what, long delayMillis) {
        if (mUiHandler != null) {
            mUiHandler.sendEmptyMessageDelayed(what, delayMillis);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            onUserVisible();
        } else {
            onUserInvisible();
        }
    }

    public boolean isVisibleToUser() {
        return mIsVisibleToUser;
    }

    public void onUserVisible() {
    }

    public void onUserInvisible() {
    }

    public void onUserFastVisible() {
    }

    public void finishAttachedActivity() {
        if (mActivity != null && !mActivity.isFinishing()) {
            mActivity.finish();
        }
    }

    /**
     * 监听来自宿主Activity的事件
     *
     * @param eventType 事件类型
     * @param bundle    传递的数据
     */
    public void onActivityEvent(int eventType, Object bundle) {
        if (!mIsCreated || !isAdded()) {
            return;
        }

        dispatchActivityEvent(eventType, bundle);
    }

    private void dispatchActivityEvent(int eventType, Object bundle) {
        FragmentManager fragmentManager = getChildFragmentManager();
        if (fragmentManager != null) {
            List<Fragment> childFragments = fragmentManager.getFragments();
            if (childFragments != null && !childFragments.isEmpty()) {
                for (Fragment f : childFragments) {
                    if (f instanceof BaseFragment) {
                        ((BaseFragment) f).onActivityEvent(eventType, bundle);
                    }
                }
            }
        }
    }

    /**
     * Fragment内嵌在Fragment中时使用该方法判断是否被可见展示
     *
     * @return
     */
    protected boolean isShown() {
        BaseFragment current = this;
        do {
            if (!current.mIsVisibleToUser) {
                return false;
            }
            Fragment parent = current.getParentFragment();
            current = (BaseFragment) parent;
        } while (current != null);
        return true;
    }

    protected void startActivity(Class<?> activityClass) {
        Intent intent = new Intent(mActivity, activityClass);
        startActivity(intent);
    }

    protected void startFragment(int containerViewId, BaseFragment fragment) {
        startFragment(containerViewId, fragment, null);
    }

    protected void startFragment(int containerViewId, BaseFragment fragment, String tag) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(containerViewId, fragment, tag);
        ft.commit();
    }

    @Override
    public void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void showToast(String msg, int duration) {
        if (mActivity != null) {
            AppToast.showToast(mActivity, msg, duration);
        }
    }

    @Override
    public void showToast(@StringRes int msgResId) {
        showToast(msgResId, Toast.LENGTH_SHORT);
    }

    @Override
    public void showToast(@StringRes int msgResId, int duration) {
        if (mActivity != null) {
            AppToast.showToast(mActivity, msgResId, duration);
        }
    }

    @Override
    public void showLoadingDialog(int resId) {
        showLoadingDialog(getString(resId));
    }

    @Override
    public void showLoadingDialog(String resId) {
        if (mActivity == null) {
            return;
        }

        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mActivity);
        }

        mLoadingDialog.setText(resId);
        mLoadingDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.cancel();
        }
    }

    @Override
    public void showErrorView(Throwable e) {
        if(e instanceof UnknownHostException){
            showToast(R.string.network_error);
        }else if (e != null && e.getMessage() != null) {
            showToast(e.getMessage());
        }
    }

    @Override
    public void showMessageDialog(String message) {
        if (mActivity != null && message != null) {
            DialogUtil.showTipDialog(mActivity, null, message, null, false);
        }
    }

    @Override
    public void showMessageDialog(@StringRes int msgResId) {
        if (mActivity != null) {
            DialogUtil.showTipDialog(mActivity, null, getString(msgResId), null, false);
        }
    }

    @Override
    public void showMessageDialog(HttpResponse response, @StringRes int msgResId) {
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

    public boolean onBackPressed(){
        return false;
    }

    protected <T extends View> T findViewById(View view, int id) {
        return (T) view.findViewById(id);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    protected Presenter getPresenter() {
        return mPresenter;
    }

    protected abstract Presenter onCreatePresenter();
}
