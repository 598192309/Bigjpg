package com.bigjpg.mvp.view;

import com.bigjpg.model.entity.EnlargeConfig;
import com.bigjpg.model.response.EnlargeResponse;
import com.bigjpg.model.response.HttpResponse;

import java.util.List;

public interface HomeView extends IView{
    void onGetEnlargeTaskSuccess(List<EnlargeConfig> list);
    void onUploadImageSuccess(EnlargeConfig config);
    void onUploadImageFailed(EnlargeConfig config);
    void onRetryEnlargeTaskSuccess(String fid);
    void onRetryEnlargeTaskFailed(HttpResponse response);
    void onStartEnlargeTaskSuccess(EnlargeConfig config, String fid);
    void onStartEnlargeTaskFailed(EnlargeConfig config, EnlargeResponse response);
    void onDeleteTaskSuccess(EnlargeConfig config);
    void onDeleteTaskFailed(EnlargeConfig config);
    void onDownloadFailed(Throwable t);
}
