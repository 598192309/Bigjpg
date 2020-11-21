package com.bigjpg.util;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bigjpg.application.BigJPGApplication;
import com.bigjpg.model.entity.OssConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 上传到阿里云OSS
 */
public class OSSUtil {

    private static OSS sOSS;

    private static String getSuffix(String filePath) {
        return FileUtil.getFileName(filePath);
    }

    public static String generateUploadFileName(String filePath) {
        // "file:///storage/emulated/0/DCIM/Screenshots/Screenshot_20180119-122029.png"
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //upload/2018-1-20/15164503124890.46344085317105055.png
        return "upload/" + sdf.format(date) + "/" + System.currentTimeMillis() + Math.random() + getSuffix(filePath);
    }

    /**
     * 初始化OSS，new OSSClient需要放在后台线程中
     *
     * @param config
     * @return
     */
    private static OSS getOss(OssConfig config) {
        if (sOSS == null) {
            ClientConfiguration conf = new ClientConfiguration();
            // connction time out default 15s
            conf.setConnectionTimeout(15 * 1000);
            // socket timeout，default 15s
            conf.setSocketTimeout(15 * 1000);
            // synchronous request number，default 5
            conf.setMaxConcurrentRequest(5);
            // retry，default 2
            conf.setMaxErrorRetry(2);
            //write local log file ,path is SDCard_path\OSSLog\logs.csv
            OSSLog.enableLog();
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(config.accesskeyid, config.accesskeysecret, "");

            //NetworkOnMainThreadException
            sOSS = new OSSClient(BigJPGApplication.getInstance(), config.endpoint, credentialProvider, conf);
        }
        return sOSS;
    }

    /**
     * 异步上传
     *
     * @param filePath
     * @param config
     */
    public static OSSAsyncTask uploadFileAsync(String filePath, OssConfig config) {
        PutObjectRequest put = new PutObjectRequest(config.bucket, generateUploadFileName(filePath), filePath);
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                LogUtil.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = getOss(config).asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                LogUtil.d("PutObject", "UploadSuccess");
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // Request exception
                if (clientExcepion != null) {
                    // Local exception, such as a network exception
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // Service exception
                    LogUtil.e("ErrorCode", serviceException.getErrorCode());
                    LogUtil.e("RequestId", serviceException.getRequestId());
                    LogUtil.e("HostId", serviceException.getHostId());
                    LogUtil.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

        return task;
    }


    /**
     * 同步上传
     *
     * @param filePath
     * @param config
     * @throws ClientException  本地异常，如网络异常等
     * @throws ServiceException 服务异常
     */
    public static String uploadFileSync(String filePath, OssConfig config) throws ClientException, ServiceException {
        String objectKey = generateUploadFileName(filePath);
        PutObjectRequest put = new PutObjectRequest(config.bucket, objectKey, filePath);
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                LogUtil.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        PutObjectResult putResult = getOss(config).putObject(put);

        LogUtil.d("PutObject", "UploadSuccess");
        LogUtil.d("ETag", putResult.getETag());
        LogUtil.d("RequestId", putResult.getRequestId());

        return config.getOSSUrl() + '/' + objectKey;

    }
}
