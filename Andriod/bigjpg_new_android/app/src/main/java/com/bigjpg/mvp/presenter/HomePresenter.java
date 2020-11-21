package com.bigjpg.mvp.presenter;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.bigjpg.R;
import com.bigjpg.application.BigJPGApplication;
import com.bigjpg.model.entity.EnlargeConfig;
import com.bigjpg.model.response.AppConfigResponse;
import com.bigjpg.model.response.EnlargeResponse;
import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.model.rest.ResponseUtil;
import com.bigjpg.model.rest.RetrofitUtil;
import com.bigjpg.model.subscriber.SimpleSubscriber;
import com.bigjpg.mvp.view.HomeView;
import com.bigjpg.util.AppUtil;
import com.bigjpg.util.EnlargeTaskManager;
import com.bigjpg.util.FileUtil;
import com.bigjpg.util.OSSUtil;
import com.bigjpg.util.ResourcesUtil;

import org.json.JSONArray;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 放大任务：
 * 1.图片选择
 * 2.图片上传到阿里云OSS
 * 3.创建放大任务
 * 4.查询放大任务
 * 5.任务结束状态
 */
public class HomePresenter extends SimplePresenter<HomeView> {

    public void startEnlarge(final EnlargeConfig config) {
        DisposableObserver disposableObserver = new SimpleSubscriber<EnlargeResponse>(getView()) {
            @Override
            public void onNextAction(EnlargeResponse response) {
                if (isViewAttached()) {
                    if (HttpResponse.isResponseOk(response)) {
                        String fid = response.getInfo();
                        getView().onStartEnlargeTaskSuccess(config, fid);
                        EnlargeTaskManager.getInstance().addTaskFid(fid);
                        EnlargeTaskManager.getInstance().startCheck();
                    } else {
                        getView().onStartEnlargeTaskFailed(config, response);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if(isViewAttached()){
                    getView().onStartEnlargeTaskFailed(config, null);
                }
            }
        };

        String params = config.getEnlargeParam();
        RetrofitUtil.getAPI().postEnlargeTask(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
        addDisposable(disposableObserver);
    }


    public void retryEnlargeTask(final String fid) {
        getView().showLoadingDialog(R.string.loading);
        DisposableObserver disposableObserver = new SimpleSubscriber<HttpResponse>(getView(), true) {
            @Override
            public void onNextAction(HttpResponse response) {
                if (isViewAttached()) {
                    if (HttpResponse.isResponseOk(response)) {
                        EnlargeTaskManager.getInstance().addTaskFid(fid);
                        EnlargeTaskManager.getInstance().startCheck();
                        getView().onRetryEnlargeTaskSuccess(fid);
                    } else {
                        getView().onRetryEnlargeTaskFailed(response);
                    }
                }
            }
        };
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(fid);
        RetrofitUtil.getAPI().retryEnlargeTasks(jsonArray.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
        addDisposable(disposableObserver);
    }


    public void uploadImage(final List<EnlargeConfig> configs) {
        BigJPGApplication application = BigJPGApplication.getInstance();
        final AppConfigResponse appConfigResponse = application.getAppConfig();
        if (appConfigResponse == null || appConfigResponse.getApp_oss_conf() == null) {
            // OSS not ready
            return;
        }

        Disposable disposable = Observable.create(new ObservableOnSubscribe<EnlargeConfig>() {

            @Override
            public void subscribe(ObservableEmitter<EnlargeConfig> emitter) throws Exception {
                try {
                    for (EnlargeConfig config : configs) {
                        try {
                            config.input = OSSUtil.uploadFileSync(config.file_path, appConfigResponse.getApp_oss_conf());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        emitter.onNext(config);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EnlargeConfig>() {
                    @Override
                    public void accept(EnlargeConfig result) throws Exception {
                        if (isViewAttached()) {
                            if (TextUtils.isEmpty(result.input)) {
                                getView().onUploadImageFailed(result);
                            } else {
                                getView().onUploadImageSuccess(result);
                            }
                        }
                    }
                });

        addDisposable(disposable);
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
                    save(filePath, simplePath, response.body());
                } else {
                    if (isViewAttached()) {
                        getView().hideLoadingDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (isViewAttached()) {
                    getView().hideLoadingDialog();
                    getView().onDownloadFailed(t);
                }
            }
        });
    }

    private void save(final String fileFullPath, final String simplePath, final ResponseBody body) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Boolean>() {

            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                Callable<Boolean> saveCall = new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        boolean saved = ResponseUtil.writeResponseBodyToDisk(fileFullPath, body);
                        if (saved) {
                            BigJPGApplication.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileFullPath)));
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
                        if (isViewAttached()) {
                            getView().hideLoadingDialog();
                            if (saved) {
                                getView().showToast(getView().getContext().getString(R.string.save_to) + simplePath);
                            }else{
                                getView().onDownloadFailed(null);
                            }
                        }
                    }
                });
        addDisposable(disposable);
    }


    public void deleteTask(final EnlargeConfig config) {
        getView().showLoadingDialog(R.string.loading);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(config.fid);

        DisposableObserver disposableObserver = new SimpleSubscriber<HttpResponse>(getView(), true) {
            @Override
            public void onNextAction(HttpResponse response) {
                if (isViewAttached()) {
                    getView().hideLoadingDialog();
                    if (HttpResponse.isResponseOk(response)) {
                        getView().onDeleteTaskSuccess(config);
                    } else {
                        getView().onDeleteTaskFailed(config);
                    }
                }
            }
        };
        RetrofitUtil.getAPI().deleteEnlargeTask(jsonArray.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
        addDisposable(disposableObserver);
    }

}
