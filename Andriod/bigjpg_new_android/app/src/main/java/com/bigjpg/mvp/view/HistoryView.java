package com.bigjpg.mvp.view;

import com.bigjpg.model.entity.EnlargeLog;
import com.bigjpg.model.response.UserResponse;

public interface HistoryView extends IView {
    void onGetEnlargeLogSuccess(UserResponse response);
    void onGetEnlargeLogFailed(UserResponse response);
    void onDeleteTaskSuccess(EnlargeLog log);
    void onDeleteTaskFailed(EnlargeLog log);
    void onRetryTaskSuccess(EnlargeLog log);
    void onRetryTaskFailed(EnlargeLog log);
    void onDownloadFailed(Throwable t);
}
