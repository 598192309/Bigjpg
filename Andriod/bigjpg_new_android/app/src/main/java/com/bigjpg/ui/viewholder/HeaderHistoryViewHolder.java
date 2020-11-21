package com.bigjpg.ui.viewholder;

import android.view.View;

import com.bigjpg.R;
import com.bigjpg.model.entity.EnlargeLog;
import com.bigjpg.util.ViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class HeaderHistoryViewHolder extends BaseHistoryViewHolder {

    private OnBatchDownloadClickListener listener;
    private boolean showOkLayout = false;
    @BindView(R.id.history_batch_download)
    View btnDownload;
    @BindView(R.id.history_batch_download_sure)
    View btnOk;
    @BindView(R.id.history_batch_download_cancel)
    View btnCancel;

    public HeaderHistoryViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindViewData(EnlargeLog data, int dataPosition) {

    }

    @OnClick(R.id.history_batch_download)
    void onBatchDownload() {
        showOkLayout = false;
        ViewUtil.setViewVisible(btnDownload, false);
        ViewUtil.setViewVisible(btnOk, true);
        ViewUtil.setViewVisible(btnCancel, true);
        if (listener != null) {
            listener.onBatchDownloadClick();
        }
    }

    @OnClick(R.id.history_batch_download_sure)
    void onBatchDownloadOkClick() {
        ViewUtil.setViewVisible(btnDownload, true);
        ViewUtil.setViewVisible(btnOk, false);
        ViewUtil.setViewVisible(btnCancel, false);
        if (listener != null) {
            listener.onBatchDownloadOkClick();
        }
    }

    @OnClick(R.id.history_batch_download_cancel)
    void onBatchDownloadCancelClick() {
        ViewUtil.setViewVisible(btnDownload, true);
        ViewUtil.setViewVisible(btnOk, false);
        ViewUtil.setViewVisible(btnCancel, false);
        if (listener != null) {
            listener.onBatchDownloadCancelClick();
        }
    }

    public void setOnBatchDownloadClickListener(OnBatchDownloadClickListener listener) {
        this.listener = listener;
    }

    public interface OnBatchDownloadClickListener {
        void onBatchDownloadClick();

        void onBatchDownloadOkClick();

        void onBatchDownloadCancelClick();
    }
}
