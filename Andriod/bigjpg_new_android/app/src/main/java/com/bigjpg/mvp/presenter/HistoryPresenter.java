package com.bigjpg.mvp.presenter;

import android.content.Intent;
import android.net.Uri;

import com.bigjpg.R;
import com.bigjpg.application.BigJPGApplication;
import com.bigjpg.model.entity.EnlargeLog;
import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.model.response.UserResponse;
import com.bigjpg.model.rest.ResponseUtil;
import com.bigjpg.model.rest.RetrofitUtil;
import com.bigjpg.model.subscriber.SimpleSubscriber;
import com.bigjpg.mvp.view.HistoryView;
import com.bigjpg.util.AppUtil;
import com.bigjpg.util.FileUtil;
import com.bigjpg.util.ResourcesUtil;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
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

public class HistoryPresenter extends SimplePresenter<HistoryView> {

    public void getEnlargeLogs() {
        DisposableObserver disposableObserver = new SimpleSubscriber<UserResponse>(getView()) {
            @Override
            public void onNextAction(UserResponse response) {
                if (isViewAttached()) {
                    if (HttpResponse.isResponseOk(response)) {
                        getView().onGetEnlargeLogSuccess(response);
                    } else {
                        getView().onGetEnlargeLogFailed(response);
                    }
                }
            }
        };
        RetrofitUtil.getAPI().getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
        addDisposable(disposableObserver);
    }

    public void deleteTask(final EnlargeLog log) {
        getView().showLoadingDialog(R.string.loading);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(log.fid);

        DisposableObserver disposableObserver = new SimpleSubscriber<HttpResponse>(getView()) {
            @Override
            public void onNextAction(HttpResponse response) {
                if (isViewAttached()) {
                    getView().hideLoadingDialog();
                    if (HttpResponse.isResponseOk(response)) {
                        getView().onDeleteTaskSuccess(log);
                    } else {
                        getView().onDeleteTaskFailed(log);
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

    public void retryTask(final EnlargeLog log) {
        getView().showLoadingDialog(R.string.loading);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(log.fid);

        DisposableObserver disposableObserver = new SimpleSubscriber<HttpResponse>(getView()) {
            @Override
            public void onNextAction(HttpResponse response) {
                if (isViewAttached()) {
                    getView().hideLoadingDialog();
                    if (HttpResponse.isResponseOk(response)) {
                        getView().onRetryTaskSuccess(log);
                    } else {
                        getView().onRetryTaskFailed(log);
                    }
                }
            }
        };
        RetrofitUtil.getAPI().retryEnlargeTasks(jsonArray.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
        addDisposable(disposableObserver);
    }

    public void batchDownload(ArrayList<EnlargeLog> logs) {
        getView().showLoadingDialog(R.string.loading);
        for (int i = 0; i < logs.size(); i++) {
            download(logs.get(i));
        }
    }

    public void download(final EnlargeLog log) {
        RetrofitUtil.getAPI().downloadFile(log.enlargeUrl).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String fileName = FileUtil.getFileName(log.enlargeUrl);
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
}
