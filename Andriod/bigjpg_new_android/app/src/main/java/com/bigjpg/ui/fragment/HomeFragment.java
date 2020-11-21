package com.bigjpg.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigjpg.R;
import com.bigjpg.application.BigJPGApplication;
import com.bigjpg.model.constant.AppIntent;
import com.bigjpg.model.constant.EnlargeKey;
import com.bigjpg.model.constant.EventType;
import com.bigjpg.model.entity.EnlargeConfig;
import com.bigjpg.model.entity.EnlargeLog;
import com.bigjpg.model.entity.EnlargeStatus;
import com.bigjpg.model.entity.User;
import com.bigjpg.model.response.EnlargeResponse;
import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.mvp.presenter.HomePresenter;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.mvp.view.HomeView;
import com.bigjpg.ui.activity.EnlargeConfigActivity;
import com.bigjpg.ui.activity.photo.chooser.ImageChooserActivity;
import com.bigjpg.ui.activity.photo.chooser.ImageConstants;
import com.bigjpg.ui.adapter.EnlargeTaskListAdapter;
import com.bigjpg.ui.base.BaseFragment;
import com.bigjpg.ui.viewholder.EnlargeTaskViewHolder;
import com.bigjpg.ui.viewholder.HeaderTaskViewHolder;
import com.bigjpg.util.AppUtil;
import com.bigjpg.util.DialogUtil;
import com.bigjpg.util.EnlargeTaskManager;
import com.bigjpg.util.FileUtil;
import com.bigjpg.util.ImageUtil;
import com.bigjpg.util.LocalBroadcastUtil;
import com.bigjpg.util.ResourcesUtil;
import com.bigjpg.util.StringUtil;
import com.bigjpg.util.UserManager;

import java.lang.ref.WeakReference;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页-放大
 *
 * @author Momo
 * @date 2019-04-09 18:21
 */
public class HomeFragment extends BaseFragment implements HomeView, EnlargeTaskViewHolder.OnEnlargeTaskButtonClickListener, HeaderTaskViewHolder.OnChooseImageClickListener {

    @BindView(R.id.task_list)
    RecyclerView mRecyclerView;

    private HomePresenter mPresenter;
    private List<EnlargeConfig> mData = new ArrayList<>();
    private EnlargeTaskListAdapter mAdapter;
    private HeaderTaskViewHolder mHeaderViewHolder;

    //10MB
    private static final int MAX_FILE_SIZE_IN_BYTE = 10485760;
    private static final int MAX_IMAGE_WIDTH = 3000;
    private static final int MAX_IMAGE_HEIGHT = 3000;
    private static final int RC_CONFIG = 0x33;

    private Handler mCheckTimeoutHandler = new CheckTimeoutHandler(this);
    private static final int MSG_CHECK_TIMEOUT = 0x44;
    private Dialog mTaskFailedDialog;

    static class CheckTimeoutHandler extends Handler {

        WeakReference<HomeFragment> homeFragmentWeakReference;

        public CheckTimeoutHandler(HomeFragment homeFragment) {
            homeFragmentWeakReference = new WeakReference<>(homeFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_CHECK_TIMEOUT && homeFragmentWeakReference.get() != null) {
                String fid = (String) msg.obj;
                HomeFragment homeFragment = homeFragmentWeakReference.get();
                if (homeFragment.isAdded()) {
                    homeFragment.onReceiveCheckTaskTimeout(fid);
                }
            }
        }
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected Presenter onCreatePresenter() {
        mPresenter = new HomePresenter();
        return mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerViewAdapter();
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (mIsCreated) {
            findAndCheckNotFinishTask();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isShown()) {
            findAndCheckNotFinishTask();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EnlargeTaskManager.getInstance().stopCheck();
    }

    /**
     * 检查列表中是否有还未完成的任务
     */
    private void findAndCheckNotFinishTask() {
        for (EnlargeConfig item : mData) {
            if (!TextUtils.isEmpty(item.fid) && !EnlargeStatus.SUCCESS.equals(item.status)) {
                EnlargeTaskManager.getInstance().addTaskFid(item.fid);
            }
        }
        EnlargeTaskManager.getInstance().startCheck();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && mIsCreated) {
            if (requestCode == RC_CONFIG) {
                if (data != null) {
                    onEnlargeConfirmResult(data);
                }
            } else if (requestCode == ImageUtil.REQUEST_CODE_GALLERY) {
                if (data != null) {
                    onChooseFileResult(data);
                }
            }
        }
    }

    /**
     * 放大配置页返回
     *
     * @param data
     */
    private void onEnlargeConfirmResult(Intent data) {
        EnlargeConfig enlargeConfig = (EnlargeConfig) data.getSerializableExtra(EnlargeKey.ENLARGE_CONFIG);
        boolean isEnlargeAfterAll = data.getBooleanExtra(EnlargeKey.ENLARGE_AFTER_ALL, false);

        if (enlargeConfig == null) {
            return;
        }

        if (mPresenter != null) {
            if (isEnlargeAfterAll) {
                boolean isCurrentItemFound = false;
                List<EnlargeConfig> list = new ArrayList<>();
                for (EnlargeConfig item : mData) {
                    if (TextUtils.equals(item.tid, enlargeConfig.tid)) {
                        isCurrentItemFound = true;
                    }
                    if (!isCurrentItemFound) {
                        continue;
                    }
                    if (TextUtils.isEmpty(item.fid) && EnlargeStatus.NEW.equals(item.status)) {
                        item.status = EnlargeStatus.PROCESS;
                        EnlargeConfig.copyEnlargeConfigParam(item, enlargeConfig);
                        list.add(item);
                    }
                }

                mAdapter.notifyDataSetChanged();
                mPresenter.uploadImage(list);
            } else {
                enlargeConfig.status = EnlargeStatus.PROCESS;
                for (EnlargeConfig item : mData) {
                    if (TextUtils.equals(item.tid, enlargeConfig.tid)) {
                        item.status = EnlargeStatus.PROCESS;
                        break;
                    }
                }
                mAdapter.notifyDataSetChanged();
                List<EnlargeConfig> list = new ArrayList<>();
                list.add(enlargeConfig);
                mPresenter.uploadImage(list);
            }
        }
    }

    /**
     * 选择文件返回
     *
     * @param data
     */
    private void onChooseFileResult(Intent data) {
        String path = data.getStringExtra(ImageConstants.EXTRA_PATH);
        if (path == null) {
            ArrayList<String> list = data.getStringArrayListExtra(ImageConstants.EXTRA_PATH_LIST);
            if (list != null && !list.isEmpty()) {
                addImages(list);
            }
        } else {
            addImage(path);
        }
    }

    private EnlargeConfig createEnlargeConfig(String path, int position) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        EnlargeConfig enlargeConfig = new EnlargeConfig();
        enlargeConfig.file_name = FileUtil.getFileName(path);
        enlargeConfig.file_path = path;
        enlargeConfig.file_height = options.outHeight;
        enlargeConfig.file_width = options.outWidth;
        enlargeConfig.files_size = FileUtil.getFileSize(path);
        enlargeConfig.tid = String.valueOf(System.currentTimeMillis()) + position;
        if (enlargeConfig.file_height > MAX_IMAGE_HEIGHT || enlargeConfig.file_width > MAX_IMAGE_WIDTH) {
            enlargeConfig.isOverLimit = true;
        } else if (enlargeConfig.files_size > MAX_FILE_SIZE_IN_BYTE) {
            enlargeConfig.isOverLimit = true;
        }
        return enlargeConfig;
    }

    private void addImage(String path) {
        EnlargeConfig enlargeConfig;
        enlargeConfig = createEnlargeConfig(path, 0);
        mData.add(0, enlargeConfig);
        if (mAdapter == null) {
            initRecyclerViewAdapter();
        } else {
            mAdapter.notifyDataSetChanged();
        }

        if (mHeaderViewHolder != null) {
            if (enlargeConfig.isOverLimit && UserManager.getInstance().isNeedUpgradeVersion()) {
                mHeaderViewHolder.showOverLimitTipsView();
            } else {
                mHeaderViewHolder.hideOverLimitTipsView();
            }
        }
    }

    private void addImages(List<String> paths) {
        List<EnlargeConfig> enlargeConfigs = new ArrayList<>();
        boolean hasOverLimitItem = false;
        for(int i = 0, size = paths.size(); i < size; i++){
            String path = paths.get(i);
            EnlargeConfig enlargeConfig = createEnlargeConfig(path, i);
            enlargeConfigs.add(enlargeConfig);
            if (enlargeConfig.isOverLimit) {
                hasOverLimitItem = true;
            }
        }

        mData.addAll(0, enlargeConfigs);
        if (mAdapter == null) {
            initRecyclerViewAdapter();
        } else {
            mAdapter.notifyDataSetChanged();
        }

        if (mHeaderViewHolder != null) {
            if (hasOverLimitItem && UserManager.getInstance().isNeedUpgradeVersion()) {
                mHeaderViewHolder.showOverLimitTipsView();
            } else {
                mHeaderViewHolder.hideOverLimitTipsView();
            }
        }
    }

    private void initRecyclerViewAdapter() {
        mHeaderViewHolder = new HeaderTaskViewHolder(ResourcesUtil.inflate(getActivity(), R.layout.layout_task_header));
        mHeaderViewHolder.setOnChooseImageClickListener(this);
        mAdapter = new EnlargeTaskListAdapter(getContext(), mData, mHeaderViewHolder, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDownloadClick(EnlargeConfig enlargeConfig, int position) {
        if (!AppUtil.hasRequiredPermission()) {
            LocalBroadcastUtil.sendBroadcast(getContext(), AppIntent.ACTION_PERMISSION);
            return;
        }
        if (!StringUtil.isEmpty(enlargeConfig.image_url)) {
            showLoadingDialog(R.string.begin_download);
            mPresenter.download(enlargeConfig.image_url);
        }
    }

    @Override
    public void onEnlargeClick(EnlargeConfig enlargeConfig, int position) {
        Intent intent = new Intent(getActivity(), EnlargeConfigActivity.class);
        intent.putExtra(EnlargeKey.ENLARGE_CONFIG, enlargeConfig);
        startActivityForResult(intent, RC_CONFIG);
    }

    @Override
    public void onDeleteClick(final EnlargeConfig enlargeConfig, final int position) {
        DialogUtil.showTwoButtonTipDialog(getActivity(), getString(R.string.sure), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemCount = mAdapter.getItemCount();
                int dataPosition = position - (mAdapter.useHeader() ? 1 : 0);
                if (dataPosition >= 0 && position >= 0 && position < itemCount) {
                    mData.remove(dataPosition);
                    mAdapter.notifyItemRemoved(position);
                    mAdapter.notifyItemRangeChanged(position, itemCount);
                }

                if (!TextUtils.isEmpty(enlargeConfig.fid)) {
                    showLoadingDialog(R.string.loading);
                    mPresenter.deleteTask(enlargeConfig);
                }
            }
        });
    }

    @Override
    public void onRetryClick(EnlargeConfig enlargeConfig, int position) {
        if (StringUtil.isEmpty(enlargeConfig.fid)) {
            // 放大任务未创建成功时的重试
            if (StringUtil.isEmpty(enlargeConfig.input)) {
                //未上传阿里云|oss时的重试
                List<EnlargeConfig> list = new ArrayList<>();
                list.add(enlargeConfig);
                enlargeConfig.progress = 0;
                enlargeConfig.status = EnlargeStatus.PROCESS;
                mAdapter.notifyDataSetChanged();
                mPresenter.uploadImage(list);
            } else {
                //已上传阿里云oss的重试
                enlargeConfig.progress = 0;
                enlargeConfig.status = EnlargeStatus.PROCESS;
                mAdapter.notifyDataSetChanged();
                mPresenter.startEnlarge(enlargeConfig);
            }
        } else {
            mPresenter.retryEnlargeTask(enlargeConfig.fid);
        }
    }

    @Override
    public void onChooseImageClick() {
        if (BigJPGApplication.getInstance().getAppConfig() == null) {
            LocalBroadcastUtil.sendBroadcast(getContext(), AppIntent.ACTION_GET_CONFIG);
            showToast("Please wait...");
            return;
        }

        if (AppUtil.hasRequiredPermission()) {
            Intent intent = new Intent(getActivity(), ImageChooserActivity.class);
            startActivityForResult(intent, ImageUtil.REQUEST_CODE_GALLERY);
        } else {
            LocalBroadcastUtil.sendBroadcast(getContext(), AppIntent.ACTION_PERMISSION);
        }
    }

    @Override
    public void onStartEnlargeTaskSuccess(EnlargeConfig config, String fid) {
        EnlargeConfig startedItem = null;
        for (EnlargeConfig item : mData) {
            // 开始放大任务，根据tid 匹配设置fid
            if (TextUtils.equals(item.tid, config.tid)) {
                item.fid = fid;
                startedItem = item;
                break;
            }
        }
        if (startedItem != null) {
            handleEnlargeProgress(startedItem);
            mAdapter.notifyDataSetChanged();
        }

        //放大任务超出30秒，提示升级(只要不是会员，提示升级)
        if (!UserManager.getInstance().isLogin() || UserManager.getInstance().isNeedUpgradeVersion()) {
            Message msg = Message.obtain();
            msg.what = MSG_CHECK_TIMEOUT;
            msg.obj = fid;
            mCheckTimeoutHandler.sendMessageDelayed(msg, 30000L);
        }
    }

    @Override
    public void onStartEnlargeTaskFailed(EnlargeConfig config, EnlargeResponse response) {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }

        EnlargeConfig startedItem = null;
        for (EnlargeConfig item : mData) {
            // 开始放大任务，根据tid 匹配设置fid
            if (TextUtils.equals(item.tid, config.tid)) {
                startedItem = item;
                break;
            }
        }
        if (startedItem != null) {
            startedItem.status = EnlargeStatus.ERROR;
            startedItem.progress = 0;
            handleEnlargeProgress(startedItem);
            mAdapter.notifyDataSetChanged();
        }


        if((mTaskFailedDialog != null && mTaskFailedDialog.isShowing())){
            return;
        }

        if (response != null && response.getStatus() != null) {
            String status = response.getStatus();
            if (HttpResponse.Status.PARALLEL_LIMIT.equals(status)) {
                mTaskFailedDialog = DialogUtil.showUpgradeDialog(mActivity, getString(R.string.num_limit));
            } else if (HttpResponse.Status.MONTH_LIMIT.equals(status)) {
                if (User.Version.PRO.equals(UserManager.getInstance().getUser().version)) {
                    mTaskFailedDialog = DialogUtil.showOneButtonTipDialog(mActivity, R.string.month_limit);
                } else {
                    //非pro会员 还可以继续升级
                    mTaskFailedDialog = DialogUtil.showUpgradeDialog(mActivity, getString(R.string.month_limit));
                }
            } else if (HttpResponse.Status.PARAM_ERROR.equals(status)) {
                mTaskFailedDialog = DialogUtil.showOneButtonTipDialog(mActivity, R.string.params);
            } else if (HttpResponse.Status.SIZE_LIMIT.equals(status)) {
                mTaskFailedDialog = DialogUtil.showOneButtonTipDialog(mActivity, R.string.size_limit);
            } else {
                mTaskFailedDialog = DialogUtil.showOneButtonTipDialog(mActivity, status);
            }
        }

    }

    @Override
    public void onDeleteTaskSuccess(EnlargeConfig config) {
        showToast(R.string.succ);
        if (!TextUtils.isEmpty(config.fid)) {
            EnlargeTaskManager.getInstance().removeTaskFid(config.fid);
        }
    }

    @Override
    public void onDeleteTaskFailed(EnlargeConfig config) {
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
    public void onGetEnlargeTaskSuccess(List<EnlargeConfig> list) {
        if (mAdapter != null && list != null && !list.isEmpty()) {
            for (EnlargeConfig item1 : mData) {
                for (EnlargeConfig item2 : list) {
                    if (TextUtils.equals(item1.fid, item2.fid)) {
                        item1.status = item2.status;
                        item1.image_url = item2.image_url;
                        handleEnlargeProgress(item1);
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void onHistoryTaskDelete(EnlargeLog log) {
        if (mData == null || mAdapter == null) {
            return;
        }
        int dataPosition = -1;
        for (int i = 0, size = mData.size(); i < size; i++) {
            EnlargeConfig item = mData.get(i);
            if (log.fid != null && TextUtils.equals(log.fid, item.fid)) {
                dataPosition = i;
                break;
            }
        }
        if (dataPosition >= 0) {
            int position = dataPosition + (mAdapter.useHeader() ? 1 : 0);
            int itemCount = mAdapter.getItemCount();
            if (position < itemCount) {
                mData.remove(dataPosition);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, itemCount);
            }

        }
    }

    private void handleEnlargeProgress(EnlargeConfig item) {
        if (EnlargeStatus.NEW.equals(item.status) || EnlargeStatus.PROCESS.equals(item.status)) {
            item.progress = item.progress + 2;
            if (item.progress > 80) {
                item.progress = 80;
            }
        } else if (EnlargeStatus.SUCCESS.equals(item.status)) {
            item.progress = 100;
        } else if (EnlargeStatus.ERROR.equals(item.status)) {
            item.progress = 0;
        }
    }

    @Override
    public void onUploadImageSuccess(EnlargeConfig config) {
        config.status = EnlargeStatus.PROCESS;
        mAdapter.notifyDataSetChanged();
        mPresenter.startEnlarge(config);
    }

    @Override
    public void onUploadImageFailed(EnlargeConfig config) {
        boolean isFound = false;
        for (EnlargeConfig item : mData) {
            if (TextUtils.equals(item.tid, config.tid)) {
                item.status = EnlargeStatus.ERROR;
                item.progress = 0;
                isFound = true;
                break;
            }
        }

        if (isFound) {
            mAdapter.notifyDataSetChanged();
        }
        showToast(R.string.upload_error);
    }

    @Override
    public void onRetryEnlargeTaskSuccess(String fid) {
        boolean isFound = false;
        for (EnlargeConfig item : mData) {
            if (TextUtils.equals(item.fid, fid)) {
                item.status = EnlargeStatus.PROCESS;
                item.progress = 5;
                isFound = true;
                break;
            }
        }
        if (isFound) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRetryEnlargeTaskFailed(HttpResponse response) {
        showMessageDialog(R.string.error);
    }

    @Override
    public void onActivityEvent(int eventType, Object bundle) {
        super.onActivityEvent(eventType, bundle);
        if (eventType == EventType.TASK_UPDATE && bundle != null) {
            List<EnlargeConfig> list = (List<EnlargeConfig>) bundle;
            if (mIsCreated && !isPause() && isShown()) {
                onGetEnlargeTaskSuccess(list);
            }
        } else if (eventType == EventType.TASK_DELETE && bundle instanceof EnlargeLog) {
            onHistoryTaskDelete((EnlargeLog) bundle);
        } else if (eventType == EventType.LOGOUT) {
            if (isAdded() && mData != null && mAdapter != null) {
                mData.clear();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void onReceiveCheckTaskTimeout(String fid) {
        boolean isTaskInList = false;
        String taskStatus = null;
        for (EnlargeConfig item : mData) {
            if (fid != null && TextUtils.equals(item.fid, fid)) {
                isTaskInList = true;
                taskStatus = item.status;
                break;
            }
        }

        if (isTaskInList && (EnlargeStatus.PROCESS.equals(taskStatus) || EnlargeStatus.NEW.equals(taskStatus))) {
            showTimeoutUpgradeTips();
        }
    }

    private void showTimeoutUpgradeTips() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }

        if (mHeaderViewHolder != null) {
            if (UserManager.getInstance().isNeedUpgradeVersion()) {
                mHeaderViewHolder.showOverLimitTipsView();
            }
        }
    }

    private void removeCheckTimeoutMessage() {
        if (mCheckTimeoutHandler != null) {
            mCheckTimeoutHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeCheckTimeoutMessage();
        EnlargeTaskManager.getInstance().clearTaskFid();

        if (mTaskFailedDialog != null && mTaskFailedDialog.isShowing()) {
            mTaskFailedDialog.dismiss();
        }
    }

}
