package com.bigjpg.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.bigjpg.R;
import com.bigjpg.application.BigJPGApplication;
import com.bigjpg.model.constant.AppIntent;
import com.bigjpg.model.constant.EnlargeKey;
import com.bigjpg.model.entity.EnlargeConfig;
import com.bigjpg.model.entity.EnlargeStatus;
import com.bigjpg.model.response.EnlargeProcessResponse;
import com.bigjpg.model.rest.ResponseUtil;
import com.bigjpg.model.rest.RetrofitUtil;
import com.bigjpg.ui.base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnlargeTaskManager {

    private static final int MSG_CHECK = 0x99;
    private static final long CHECK_INTERVAL_IN_MILLS = 3500L;
    private static EnlargeTaskManager sInstance;

    private Vector<String> mCheckTaskFids = new Vector<>();
    private List<String> mDownloadedImageUrls = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_CHECK) {
                if (!mCheckTaskFids.isEmpty()) {
                    String fidJSON;
                    JSONArray jsonArray = new JSONArray();
                    for (String fid : mCheckTaskFids) {
                        if (fid != null) {
                            jsonArray.put(fid);
                        }
                    }

                    if (jsonArray.length() > 0) {
                        fidJSON = jsonArray.toString();
                    } else {
                        return;
                    }
                    RetrofitUtil.getAPI().getEnlargeTasksCall(fidJSON).enqueue(mCallback);
                    mHandler.removeMessages(MSG_CHECK);
                    mHandler.sendEmptyMessageDelayed(MSG_CHECK, CHECK_INTERVAL_IN_MILLS);
                }
            }
        }
    };

    private Callback<EnlargeProcessResponse> mCallback = new Callback<EnlargeProcessResponse>() {

        @Override
        public void onResponse(Call<EnlargeProcessResponse> call, Response<EnlargeProcessResponse> response) {
            EnlargeProcessResponse data = response.body();
            if (data != null && data.getData() != null) {
                try {
                    String stringData = data.getData();
                    JSONObject jsonObject = new JSONObject(stringData);
                    List<EnlargeConfig> result = new ArrayList<>();
                    Iterator<String> iterator = mCheckTaskFids.iterator();
                    while (iterator.hasNext()) {
                        String fid = iterator.next();
                        if (jsonObject.has(fid)) {
                            EnlargeConfig item = new EnlargeConfig();
                            JSONArray array = jsonObject.getJSONArray(fid);
                            item.status = array.optString(0);
                            // "null"
                            item.image_url = array.optString(1);
                            item.fid = fid;
                            if (EnlargeStatus.SUCCESS.equals(item.status) || EnlargeStatus.ERROR.equals(item.status)) {
                                if (EnlargeStatus.SUCCESS.equals(item.status)) {
                                    handleAutoDownload(item.image_url);
                                }
                                iterator.remove();
                            }
                            result.add(item);
                        }
                    }
                    if (!result.isEmpty()) {
                        Intent intent = new Intent();
                        intent.setAction(AppIntent.ACTION_TASK_UPDATE);
                        intent.putExtra(EnlargeKey.TASK_LIST, (Serializable) result);
                        LocalBroadcastUtil.sendBroadcast(BigJPGApplication.getInstance(), intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<EnlargeProcessResponse> call, Throwable t) {

        }
    };

    private EnlargeTaskManager() {
    }

    public static synchronized EnlargeTaskManager getInstance() {
        if (sInstance == null) {
            sInstance = new EnlargeTaskManager();
        }
        return sInstance;
    }


    private void handleAutoDownload(String imageUrl) {
        if (AppPref.getInstance().isAutoDownloadImage() && !TextUtils.isEmpty(imageUrl)) {
            if (!mDownloadedImageUrls.contains(imageUrl)) {
                mDownloadedImageUrls.add(imageUrl);
                download(imageUrl);
            }
        }
    }

    public void download(final String image_url) {
        RetrofitUtil.getAPI().downloadFile(image_url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String fileName = FileUtil.getFileName(image_url);
                    String imagePath = File.separator + System.currentTimeMillis() + "_" + fileName;
                    String simplePath = AppUtil.getBijpgSimpleFilePath() + imagePath;
                    String filePath = AppUtil.getBigjpgFilePath() + imagePath;
                    save(image_url, filePath, simplePath, response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void save(final String imageUrl, final String fileFullPath, final String simplePath, final ResponseBody body) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {

            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                Callable<Boolean> saveCall = new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        boolean saved = ResponseUtil.writeResponseBodyToDisk(fileFullPath, body);
                        if (saved) {
                            Context context = BigJPGApplication.getInstance();
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileFullPath)));
                        } else {
                            mDownloadedImageUrls.remove(imageUrl);
                        }
                        return saved;
                    }
                };
                try {
                    emitter.onNext(saveCall.call());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean saved) throws Exception {
                        Activity activity = AppManager.getInstance().getCurrentActivity();
                        if (activity != null && !activity.isFinishing()) {
                            ((BaseActivity) activity).showToast(activity.getString(R.string.save_to) + simplePath);
                        }
                    }
                });
    }

    public void startCheck() {
        mHandler.removeMessages(MSG_CHECK);
        mHandler.sendEmptyMessageDelayed(MSG_CHECK, 800L);
    }

    public void startCheckIfNotStart() {
        if (!mHandler.hasMessages(MSG_CHECK)) {
            mHandler.sendEmptyMessageDelayed(MSG_CHECK, CHECK_INTERVAL_IN_MILLS);
        }
    }

    public void stopCheck() {
        mHandler.removeMessages(MSG_CHECK);
    }

    public void addTaskFid(String fid) {
        if (!mCheckTaskFids.contains(fid)) {
            mCheckTaskFids.add(fid);
        }
    }

    //remove
    public void removeTaskFid(String fid) {
        mCheckTaskFids.remove(fid);
    }

    public void clearTaskFid() {
        mCheckTaskFids.clear();
    }
}
