package com.bigjpg.model.rest;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * RetrofitUtil
 * @author Momo
 * @date 2016-03-28 11:23
 */
public class RetrofitUtil {

    public static final String HOST_URL =  "https://bigjpg.com/";
    private static AppApi sAPI;

    private static String getServerUrl() {
        return HOST_URL;
    }

    public static AppApi getAPI() {
        if (sAPI == null) {
            synchronized (RetrofitUtil.class) {
                if (sAPI == null) {
                    Retrofit.Builder builder = new Retrofit.Builder();
                    builder.baseUrl(getServerUrl());
                    builder.addConverterFactory(CustomGsonConverterFactory.create(GsonUtil.getGson()));
                    builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
                    builder.client(OkHttpClientUtil.getOkHttpClient());
                    Retrofit retrofit = builder.build();
                    sAPI = retrofit.create(AppApi.class);
                }
            }
        }

        return sAPI;
    }


    public static void resetAPI(){
        sAPI = null;
    }


}
