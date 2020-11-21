package com.bigjpg.model.rest;

import android.content.pm.PackageInfo;
import android.text.TextUtils;

import com.bigjpg.application.BigJPGApplication;
import com.bigjpg.model.constant.AppConstants;
import com.bigjpg.util.AppPref;
import com.bigjpg.util.AppUtil;
import com.bigjpg.util.SdkLevel;

import java.io.IOException;
import java.net.Proxy;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttpClientUtil
 *
 * @author Momo
 * @date 2016-03-28 10:55
 */
public class OkHttpClientUtil {

    private static OkHttpClient sOkHttpClient;
    private static String sUserAgent;
    private static String sVersionName;
    private static String sCookies;
    private static final String CLIENT_TYPE = "Android";

    public static OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (OkHttpClientUtil.class) {
                if (sOkHttpClient == null) {
                    okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
                    builder.addInterceptor(new HeaderInterceptor());
                    builder.connectTimeout(Config.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS);
                    builder.readTimeout(Config.HTTP_READ_TIMEOUT, TimeUnit.SECONDS);
                    builder.proxy(Proxy.NO_PROXY);
                    builder.followRedirects(false);
                    sOkHttpClient = builder.build();
                }
            }
        }
        return sOkHttpClient;
    }

    public static void setSSLOKHttpClient(String appCertFilePath) {
        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
        builder.addInterceptor(new HeaderInterceptor());
        builder.connectTimeout(Config.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(Config.HTTP_READ_TIMEOUT, TimeUnit.SECONDS);
        builder.sslSocketFactory(HttpsFactory.getSSLSocketFactory(new String[]{appCertFilePath}));
        builder.proxy(Proxy.NO_PROXY);
        sOkHttpClient = builder.build();
    }

    static class HeaderInterceptor implements Interceptor {

        public Hashtable<String, String> headers;

        public HeaderInterceptor() {

        }

        public HeaderInterceptor(Hashtable<String, String> headers) {
            this.headers = headers;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("User-Agent", getUserAgent());
            builder.addHeader("Accept", "application/json");
            builder.addHeader("Content-type", "application/json;charset=UTF-8");

            if (sVersionName == null) {
                sVersionName = AppUtil.getCurVersionName(BigJPGApplication.getInstance());
            }

            builder.addHeader("APP-VERSION", sVersionName);
            builder.addHeader("CLIENT-TYPE", CLIENT_TYPE);

            sCookies = getCookie();
            if (sCookies != null) {
                builder.addHeader("Cookie", sCookies);
            }


            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    builder.addHeader(key, headers.get(key));
                }
            }
            Request newRequest = builder.build();
            HttpUrl beforeUrl = newRequest.url();
            Response response = chain.proceed(newRequest);

            if (AppApi.LOGIN_URL.equals(beforeUrl.toString())) {
                String set_cookie = response.header("Set-Cookie");
                if (!TextUtils.isEmpty(set_cookie) && !TextUtils.equals(set_cookie, sCookies)) {
                    sCookies = set_cookie;
                    AppPref.getInstance().setCookies(set_cookie);
                }
            }
            return response;
        }
    }

    private static String getUserAgent() {
        if (TextUtils.isEmpty(sUserAgent)) {
            PackageInfo pi = BigJPGApplication.getInstance().getPackageInfo();
            StringBuffer ua = new StringBuffer();
            ua.append(AppConstants.APP_NAME_EN);
            ua.append("/ver_" + pi.versionCode);
            ua.append("/sdk_" + SdkLevel.getLevel());
            sUserAgent = ua.toString();
        }
        return sUserAgent;
    }

    public static String getCookie() {
        if (sCookies == null) {
            sCookies = AppPref.getInstance().getCookies();
        }
        return sCookies;
    }

    public static void setCookies(String cookies) {
        sCookies = cookies;
    }

}
