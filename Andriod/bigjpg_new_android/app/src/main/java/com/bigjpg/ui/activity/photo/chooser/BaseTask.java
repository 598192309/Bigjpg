package com.bigjpg.ui.activity.photo.chooser;

import android.os.AsyncTask;

/**
 * 异步任务基类
 */
public abstract class BaseTask extends AsyncTask<Void, Void, Boolean> {

    /**
     * 失败的时候的错误提示
     */
    protected String error = "";

    /**
     * 是否被终止
     */
    protected boolean interrupt = false;

    /**
     * 结果
     */
    protected Object result = null;

    /**
     * 异步任务执行完后的回调接口
     */
    protected OnTaskResultListener resultListener = null;

    @Override
    protected void onPostExecute(Boolean success) {
        if (!interrupt && resultListener != null) {
            resultListener.onResult(success, error, result);
        }
    }

    /**
     * 中断异步任务
     */
    public void cancel() {
        super.cancel(true);
        interrupt = true;
    }

    public void setOnResultListener(OnTaskResultListener listener) {
        resultListener = listener;
    }

}
