package com.bigjpg.ui.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bigjpg.R;
import com.bigjpg.model.entity.EnlargeConfig;
import com.bigjpg.model.entity.EnlargeStatus;
import com.bigjpg.util.FileUtil;
import com.bigjpg.util.FrescoLoader;
import com.bigjpg.util.ResourcesUtil;
import com.bigjpg.util.TextViewUtil;
import com.bigjpg.util.ViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 放大任务
 *
 * @author Momo
 * @date 2019-04-11 16:24
 */
public class EnlargeTaskViewHolder extends BaseTaskViewHolder {

    @BindView(R.id.task_image)
    SimpleDraweeView sdv;
    @BindView(R.id.task_desc)
    TextView tvDesc;
    @BindView(R.id.task_progress)
    ProgressBar progressBar;
    @BindView(R.id.task_status)
    TextView tvStatus;
    @BindView(R.id.task_download)
    TextView tvDownload;
    @BindView(R.id.task_enlarge)
    TextView tvEnlarge;
    @BindView(R.id.task_delete)
    TextView tvDelete;
    @BindView(R.id.task_retry)
    TextView tvRetry;
    private int height;
    private int width;
    private EnlargeConfig enlargeConfig;
    private int position;
    private OnEnlargeTaskButtonClickListener listener;
    private Context mContext;

    public EnlargeTaskViewHolder(View view) {
        super(view);
        mContext = view.getContext();
        height = ResourcesUtil.getDimensionPixelSize(mContext, R.dimen.task_image_size);
        width = height;
    }

    @Override
    public void bindViewData(EnlargeConfig data, int dataPosition) {
        enlargeConfig = data;
        position = dataPosition;
        FrescoLoader.loadImageFromFile(sdv, data.file_path, width, height);
        if(data.isOverLimit){
            progressBar.setProgress(0);
            TextViewUtil.setText(tvStatus, mContext.getString(R.string.over));
            ViewUtil.setViewVisible(tvStatus, true);
            ViewUtil.setViewVisible(tvDownload, false);
            ViewUtil.setViewVisible(tvEnlarge, false);
            ViewUtil.setViewVisible(tvDelete, true);
            ViewUtil.setViewVisible(tvRetry, false);

        }else{
            switch (data.status) {
                case EnlargeStatus.NEW:
                    if (data.progress == 0) {
                        onTaskCreate(data);
                    } else {
                        onTaskProcess(data);
                    }
                    break;
                case EnlargeStatus.PROCESS:
                    onTaskProcess(data);
                    break;
                case EnlargeStatus.SUCCESS:
                    onTaskSuccess(data);
                    break;
                case EnlargeStatus.ERROR:
                    onTaskError(data);
                    break;
                default:
                    onTaskError(data);
                    break;
            }
        }
        tvDesc.setText(String.format("%s，%sx%spx", FileUtil.getReadableFileSize(data.files_size), data.file_width, data.file_height));
    }

    void onTaskCreate(EnlargeConfig data) {
        progressBar.setProgress(data.progress);
        ViewUtil.setViewVisible(tvStatus, false);
        ViewUtil.setViewVisible(tvDownload, false);
        ViewUtil.setViewVisible(tvEnlarge, true);
        ViewUtil.setViewVisible(tvDelete, true);
        ViewUtil.setViewVisible(tvRetry, false);
    }

    void onTaskProcess(EnlargeConfig data) {
        progressBar.setProgress(data.progress);
        TextViewUtil.setText(tvStatus, mContext.getString(R.string.process));
        tvStatus.setTextColor(ResourcesUtil.getColor(mContext, R.color.text_white));
        ViewUtil.setViewVisible(tvStatus, true);
        ViewUtil.setViewVisible(tvDownload, false);
        ViewUtil.setViewVisible(tvEnlarge, false);
        ViewUtil.setViewVisible(tvDelete, true);
        ViewUtil.setViewVisible(tvRetry, false);
    }

    void onTaskSuccess(EnlargeConfig data) {
        progressBar.setProgress(100);
        TextViewUtil.setText(tvStatus, ResourcesUtil.getString(mContext, R.string.succ));
        tvStatus.setTextColor(ResourcesUtil.getColor(mContext, R.color.text_white));
        ViewUtil.setViewVisible(tvStatus, true);
        ViewUtil.setViewVisible(tvDownload, true);
        ViewUtil.setViewVisible(tvEnlarge, false);
        ViewUtil.setViewVisible(tvDelete, false);
        ViewUtil.setViewVisible(tvRetry, false);
    }

    void onTaskError(EnlargeConfig data) {
        progressBar.setProgress(0);
        TextViewUtil.setText(tvStatus, ResourcesUtil.getString(mContext, R.string.fail));
        tvStatus.setTextColor(ResourcesUtil.getColor(mContext, R.color.text_red));
        ViewUtil.setViewVisible(tvStatus, true);
        ViewUtil.setViewVisible(tvDownload, false);
        ViewUtil.setViewVisible(tvEnlarge, false);
        ViewUtil.setViewVisible(tvDelete, true);
        ViewUtil.setViewVisible(tvRetry, true);
    }

    @OnClick(R.id.task_enlarge)
    void onTaskEnlargeClick() {
        if (listener != null) {
            listener.onEnlargeClick(enlargeConfig, position);
        }
    }

    @OnClick(R.id.task_download)
    void onTaskDownloadClick() {
        if (listener != null) {
            listener.onDownloadClick(enlargeConfig, position);
        }
    }

    @OnClick(R.id.task_delete)
    void onTaskDeleteClick() {
        if (listener != null) {
            listener.onDeleteClick(enlargeConfig, position);
        }
    }

    @OnClick(R.id.task_retry)
    void onTaskRetryClick() {
        if (listener != null) {
            listener.onRetryClick(enlargeConfig, position);
        }
    }

    public void setOnEnlargeTaskButtonClick(OnEnlargeTaskButtonClickListener listener) {
        this.listener = listener;
    }

    public interface OnEnlargeTaskButtonClickListener {
        void onDownloadClick(EnlargeConfig enlargeConfig, int position);

        void onEnlargeClick(EnlargeConfig enlargeConfig, int position);

        void onDeleteClick(EnlargeConfig enlargeConfig, int position);

        void onRetryClick(EnlargeConfig enlargeConfig, int position);
    }
}
