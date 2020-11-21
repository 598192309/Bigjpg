package com.bigjpg.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bigjpg.R;
import com.bigjpg.model.constant.AppIntent;
import com.bigjpg.model.constant.EnlargeKey;
import com.bigjpg.model.constant.EventType;
import com.bigjpg.model.entity.EnlargeLog;
import com.bigjpg.model.entity.EnlargeStatus;
import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.model.response.UserResponse;
import com.bigjpg.mvp.presenter.HistoryPresenter;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.mvp.view.HistoryView;
import com.bigjpg.mvp.view.MainView;
import com.bigjpg.ui.activity.MainActivity;
import com.bigjpg.ui.adapter.EnlargeHistoryListAdapter;
import com.bigjpg.ui.base.BaseFragment;
import com.bigjpg.ui.viewholder.EnlargeHistoryViewHolder;
import com.bigjpg.ui.viewholder.HeaderHistoryViewHolder;
import com.bigjpg.util.AppUtil;
import com.bigjpg.util.DialogUtil;
import com.bigjpg.util.EnlargeTaskManager;
import com.bigjpg.util.LocalBroadcastUtil;
import com.bigjpg.util.ResourcesUtil;
import com.bigjpg.util.StringUtil;
import com.bigjpg.util.UserManager;
import com.bigjpg.util.ViewUtil;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 放大记录
 *
 * @author Momo
 * @date 2019-04-09 18:28
 */
public class HistoryFragment extends BaseFragment implements HistoryView, EnlargeHistoryViewHolder.OnEnlargeLogButtonClickListener, HeaderHistoryViewHolder.OnBatchDownloadClickListener {


    @BindView(R.id.history_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.history_login)
    Button mBtnLogin;

    private HistoryPresenter mPresenter;
    private List<EnlargeLog> mData = new ArrayList<>();
    private EnlargeHistoryListAdapter mAdapter;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    protected Presenter onCreatePresenter() {
        mPresenter = new HistoryPresenter();
        return mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new EnlargeHistoryListAdapter(getContext(), mData, this, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (mIsCreated) {
            if (UserManager.getInstance().isLogin()) {
                hideLoginView();
                mPresenter.getEnlargeLogs();
            } else {
                showLoginView();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsCreated && isShown()) {
            if (UserManager.getInstance().isLogin()) {
                hideLoginView();
                mPresenter.getEnlargeLogs();
            } else {
                showLoginView();
            }
        }
    }

    @Override
    public void onDownloadClick(EnlargeLog enlargeLog, int position) {
        if (!AppUtil.hasRequiredPermission()) {
            LocalBroadcastUtil.sendBroadcast(getContext(), AppIntent.ACTION_PERMISSION);
            return;
        }

        showLoadingDialog(R.string.loading);
        mPresenter.download(enlargeLog);
    }

    @Override
    public void onDeleteClick(final EnlargeLog enlargeLog, int position) {
        DialogUtil.showTwoButtonTipDialog(getActivity(), getString(R.string.sure), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.deleteTask(enlargeLog);
            }
        });
    }

    @Override
    public void onRetryClick(EnlargeLog enlargeLog, int position) {
        mPresenter.retryTask(enlargeLog);
    }

    @Override
    public void onBatchDownloadClick() {
        if (UserManager.getInstance().isLogin()) {
            if (mAdapter != null) {
                mAdapter.setCheckable(true);
            }
        } else {
            showLoginDialog();
        }
    }

    @Override
    public void onBatchDownloadOkClick() {
        if (!AppUtil.hasRequiredPermission()) {
            LocalBroadcastUtil.sendBroadcast(getContext(), AppIntent.ACTION_PERMISSION);
            return;
        }

        if (mAdapter != null) {
            mAdapter.setCheckable(false);
        }

        ArrayList<EnlargeLog> checkList = new ArrayList<>();
        for (EnlargeLog log : mData) {
            if (log.isChecked) {
                checkList.add(log);
            }
        }
        if (!checkList.isEmpty()) {
            showLoadingDialog(R.string.loading);
            mPresenter.batchDownload(checkList);
        }
    }

    @Override
    public void onBatchDownloadCancelClick() {
        if (mAdapter != null) {
            mAdapter.setCheckable(false);
        }
    }

    @Override
    public void onGetEnlargeLogSuccess(UserResponse response) {
        mData.clear();
        if (response.getLogs() != null) {
            mData.addAll(response.getLogs());
        }
        for (EnlargeLog log : mData) {
            if (!StringUtil.isEmpty(log.fid) && (EnlargeStatus.PROCESS.equals(log.status) || EnlargeStatus.NEW.equals(log.status))) {
                //历史列表中有放大中的任务，放大查询列表中，不用马上执行，否则进入查询--更新历史--查询 的死循环
                EnlargeTaskManager.getInstance().addTaskFid(log.fid);
                EnlargeTaskManager.getInstance().startCheckIfNotStart();
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetEnlargeLogFailed(UserResponse response) {
        if (response != null && HttpResponse.Status.NO_LOGIN.equals(response.getStatus())) {
            UserManager.getInstance().logout(getContext(), null);
            showLoginView();
        }
    }

    @Override
    public void onDeleteTaskSuccess(EnlargeLog log) {
        showToast(R.string.succ);
        mPresenter.getEnlargeLogs();
        if (!TextUtils.isEmpty(log.fid)) {
            EnlargeTaskManager.getInstance().removeTaskFid(log.fid);
            Intent intent = new Intent();
            intent.setAction(AppIntent.ACTION_TASK_DELETE);
            intent.putExtra(EnlargeKey.ENLARGE_LOG, log);
            LocalBroadcastUtil.sendBroadcast(getContext(), intent);
        }
    }

    @Override
    public void onDeleteTaskFailed(EnlargeLog log) {
        showToast(R.string.error);
    }

    @Override
    public void onRetryTaskSuccess(EnlargeLog log) {
        showToast(R.string.succ);
        mPresenter.getEnlargeLogs();
    }

    @Override
    public void onRetryTaskFailed(EnlargeLog log) {
        showToast(R.string.error);
    }

    @Override
    public void onDownloadFailed(Throwable t) {
        if (t instanceof UnknownHostException) {
            showToast(R.string.network_error);
        } else {
            showToast(R.string.error);
        }
    }

    @Override
    public void onActivityEvent(int eventType, Object bundle) {
        super.onActivityEvent(eventType, bundle);
        if (EventType.LOGOUT == eventType) {
            if (mData != null && mAdapter != null) {
                mData.clear();
                mAdapter.notifyDataSetChanged();
            }
        } else if (eventType == EventType.TASK_UPDATE) {
            if (mIsCreated && !isPause() && isShown()) {
                mPresenter.getEnlargeLogs();
            }
        }
    }

    @OnClick(R.id.history_login)
    void onLoginClick() {
        if (getActivity() instanceof MainView) {
            MainView mainView = (MainView) getActivity();
            mainView.setMainTabPosition(MainActivity.TAB_INDEX_SETTING);
        }
    }

    private void showLoginView() {
        ViewUtil.setViewVisible(mBtnLogin, true);
    }

    private void hideLoginView() {
        ViewUtil.setViewVisible(mBtnLogin, false);
    }

    private void showLoginDialog() {
        DialogUtil.showTwoButtonTipDialog(getActivity(), getString(R.string.no_upgrade), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClick();
            }
        });
    }
}
