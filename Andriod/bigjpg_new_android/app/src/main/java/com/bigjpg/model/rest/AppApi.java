package com.bigjpg.model.rest;

import com.bigjpg.model.response.AppConfigResponse;
import com.bigjpg.model.response.EnlargeProcessResponse;
import com.bigjpg.model.response.EnlargeResponse;
import com.bigjpg.model.response.HttpResponse;
import com.bigjpg.model.response.LoginResponse;
import com.bigjpg.model.response.PayResponse;
import com.bigjpg.model.response.PaypalResponse;
import com.bigjpg.model.response.UpgradeResponse;
import com.bigjpg.model.response.UserResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * AppApi
 */
public interface AppApi {

    String LOGIN_URL = RetrofitUtil.HOST_URL + "login";

    //登陆/创建用户
    @FormUrlEncoded
    @POST("login")
    Observable<LoginResponse> postRegister(@Field("username") String username,
                                           @Field("password") String password);

    //登陆/创建用户
    @FormUrlEncoded
    @POST("login")
    Observable<LoginResponse> postLogin(@Field("username") String username,
                                        @Field("password") String password,
                                        @Field("not_reg") int not_reg);

    //用户登陆密码更新
    @FormUrlEncoded
    @POST("user")
    Observable<HttpResponse> postPassword(@Field("new") String password);

    //重新设置密码
    @FormUrlEncoded
    @POST("reset")
    Observable<HttpResponse> resetPassword(@Field("username") String email);

    //用户信息查询
    @GET("user")
    Observable<UserResponse> getUser();

    //创建放大任务状态
    @FormUrlEncoded
    @POST("task")
    Observable<EnlargeResponse> postEnlargeTask(@Field("conf") String confJSON);

    //查询放大任务状态
    @GET("free")
    Observable<EnlargeProcessResponse> getEnlargeTasks(@Query("fids") String fidsJSON);

    @GET("free")
    Call<EnlargeProcessResponse> getEnlargeTasksCall(@Query("fids") String fidsJSON);

    //重试放大任务
    @GET("retry")
    Observable<HttpResponse> retryEnlargeTasks(@Query("fids") String fidsJSON);

    //删除放大任务
    @DELETE("free")
    Observable<HttpResponse> deleteEnlargeTask(@Query("fids") String fidsJSON);


    //创建订单(youzan支付), 用户查询接口来查询支付状态
    @GET("youzan_order")
    Call<PayResponse> getYouzanOrderCall(@Query("goods") String goods);

    // paypal order
    @GET("paypal_order")
    Call<PaypalResponse> getPaypalOrderCall(@Query("goods") String goods);

    //支付宝
    @GET("bill")
    Call<PayResponse> getAlipayOrderCall(@Query("a") int ali, @Query("g") String g, @Query("u") String u);

    //获取语言配置、oss配置
    @GET("conf")
    Observable<AppConfigResponse> getAppConfig();

    //获取语言配置、oss配置
    @GET("conf?")
    Call<UpgradeResponse> getUpradeConfig();


    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

}

