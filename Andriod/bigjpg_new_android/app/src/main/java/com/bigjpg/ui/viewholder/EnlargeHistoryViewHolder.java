package com.bigjpg.ui.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bigjpg.R;
import com.bigjpg.model.entity.EnlargeConfig;
import com.bigjpg.model.entity.EnlargeLog;
import com.bigjpg.model.entity.EnlargeStatus;
import com.bigjpg.util.FileUtil;
import com.bigjpg.util.FrescoLoader;
import com.bigjpg.util.ResourcesUtil;
import com.bigjpg.util.TextViewUtil;
import com.bigjpg.util.ViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class EnlargeHistoryViewHolder extends BaseHistoryViewHolder {

    @BindView(R.id.history_image)
    SimpleDraweeView sdv;
    @BindView(R.id.history_config)
    TextView tvParam;
    @BindView(R.id.history_status)
    TextView tvStatus;
    @BindView(R.id.history_download)
    View vDownload;
    @BindView(R.id.history_retry)
    View vRetry;
    @BindView(R.id.history_check)
    CheckBox checkBox;
    @BindView(R.id.history_cb)
    View vCheckBox;
    @BindView(R.id.history_buttons)
    View vButtons;
    private int height;
    private int width;
    private EnlargeLog enlargeLog;
    private int position;
    private OnEnlargeLogButtonClickListener listener;
    private static final String[] X2 = {"", "2", "4", "8", "16"};
    private boolean isCheckable = false;
    private static final Set<String> sCheckList = new HashSet<>();
    private Context mContext;

    public EnlargeHistoryViewHolder(View view) {
        super(view);
        mContext = view.getContext();
        height = ResourcesUtil.getDimensionPixelSize(mContext, R.dimen.history_image_size);
        width = height;
    }

    public void setCheckable(boolean isCheckable) {
        this.isCheckable = isCheckable;
    }

    @Override
    public void bindViewData(EnlargeLog data, int dataPosition) {
        enlargeLog = data;
        position = dataPosition;
        if (!TextUtils.isEmpty(data.enlargeUrl)) {
            FrescoLoader.loadImage(sdv, data.enlargeUrl + String.format("?x-oss-process=image/resize,m_fill,w_%d,h_%d", width, height));
        } else if (data.conf != null && data.conf.input != null) {
            FrescoLoader.loadImage(sdv, data.conf.input, width, height);
        }
        EnlargeConfig conf = data.conf;
        if (conf != null) {
            TextViewUtil.setText(tvParam, getEnlargeParams(conf));
        } else {
            TextViewUtil.setText(tvParam, "");
        }

        if (EnlargeStatus.SUCCESS.equals(data.status)) {
            ViewUtil.setViewVisible(tvStatus, false);
            ViewUtil.setViewVisible(vDownload, true);
            ViewUtil.setViewVisible(vRetry, false);
        } else if (EnlargeStatus.ERROR.equals(data.status)) {
            tvStatus.setTextColor(ResourcesUtil.getColor(mContext, R.color.text_red));
            ViewUtil.setViewVisible(tvStatus, true);
            TextViewUtil.setText(tvStatus, ResourcesUtil.getString(mContext, R.string.fail));
            ViewUtil.setViewVisible(vDownload, false);
            ViewUtil.setViewVisible(vRetry, true);
        } else if (EnlargeStatus.PROCESS.equals(data.status)) {
            tvStatus.setTextColor(ResourcesUtil.getColor(mContext, R.color.text_white));
            ViewUtil.setViewVisible(tvStatus, true);
            TextViewUtil.setText(tvStatus, ResourcesUtil.getString(mContext, R.string.process));
            ViewUtil.setViewVisible(vDownload, false);
            ViewUtil.setViewVisible(vRetry, false);
        } else if (EnlargeStatus.NEW.equals(data.status)) {
            tvStatus.setTextColor(ResourcesUtil.getColor(mContext, R.color.text_white));
            ViewUtil.setViewVisible(tvStatus, true);
            TextViewUtil.setText(tvStatus, ResourcesUtil.getString(mContext, R.string.process));
            ViewUtil.setViewVisible(vDownload, false);
            ViewUtil.setViewVisible(vRetry, false);
        } else {
            ViewUtil.setViewVisible(tvStatus, false);
            ViewUtil.setViewVisible(vDownload, false);
            ViewUtil.setViewVisible(vRetry, false);
        }

        if(sCheckList.contains(data.fid)){
            data.isChecked = true;
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }

        if (isCheckable) {
            if (EnlargeStatus.SUCCESS.equals(data.status)) {
                ViewUtil.setViewVisible(vCheckBox, true);
                ViewUtil.setViewVisible(vButtons, false);
            } else {
                ViewUtil.setViewHidden(vCheckBox, true);
                ViewUtil.setViewVisible(vButtons, false);
            }
        } else {
            ViewUtil.setViewVisible(vCheckBox, false);
            ViewUtil.setViewVisible(vButtons, true);
        }

    }

    @OnCheckedChanged(R.id.history_check)
    void onItemCheckChange() {
        enlargeLog.isChecked = checkBox.isChecked();
        if(enlargeLog.isChecked){
            sCheckList.add(enlargeLog.fid);
        }else{
            sCheckList.remove(enlargeLog.fid);
        }
    }

    private String getEnlargeParams(EnlargeConfig config) {
        StringBuilder result = new StringBuilder();
        if (config.x2 < 5) {
            result.append(X2[config.x2]);
            result.append("x");
        }
        if (config.files_size > 0.0F) {
            result.append(",");
            result.append(FileUtil.getReadableFileSize(config.files_size));
        }
        if (EnlargeConfig.Style.ART.equals(config.style)) {
            result.append(",");
            result.append(ResourcesUtil.getString(mContext, R.string.carton));
        } else if (EnlargeConfig.Style.PHOTO.equals(config.style)) {
            result.append(",");
            result.append(ResourcesUtil.getString(mContext, R.string.photo));
        }
        result.append(",");
        result.append("\n");
        result.append(ResourcesUtil.getString(mContext, R.string.noise));
        result.append(":");
        if (config.noise == EnlargeConfig.Noise.NONE) {
            result.append(ResourcesUtil.getString(mContext, R.string.none));
        } else if (config.noise == EnlargeConfig.Noise.LOW) {
            result.append(ResourcesUtil.getString(mContext, R.string.low));
        } else if (config.noise == EnlargeConfig.Noise.MEDIUM) {
            result.append(ResourcesUtil.getString(mContext, R.string.mid));
        } else if (config.noise == EnlargeConfig.Noise.HIGH) {
            result.append(ResourcesUtil.getString(mContext, R.string.high));
        } else if (config.noise == EnlargeConfig.Noise.HIGHEST) {
            result.append(ResourcesUtil.getString(mContext, R.string.highest));
        } else {
            result.append(ResourcesUtil.getString(mContext, R.string.none));
        }
        return result.toString();
    }

    @OnClick(R.id.history_download)
    void onDownloadClick() {
        if (listener != null) {
            listener.onDownloadClick(enlargeLog, position);
        }
    }

    @OnClick(R.id.history_delete)
    void onDeleteClick() {
        if (listener != null) {
            listener.onDeleteClick(enlargeLog, position);
        }
    }

    @OnClick(R.id.history_retry)
    void onRetryClick() {
        if (listener != null) {
            listener.onRetryClick(enlargeLog, position);
        }
    }

    public void setOnEnlargeLogButtonClickListener(OnEnlargeLogButtonClickListener listener) {
        this.listener = listener;
    }

    public interface OnEnlargeLogButtonClickListener {
        void onDownloadClick(EnlargeLog enlargeLog, int position);

        void onDeleteClick(EnlargeLog enlargeLog, int position);

        void onRetryClick(EnlargeLog enlargeLog, int position);
    }
}
