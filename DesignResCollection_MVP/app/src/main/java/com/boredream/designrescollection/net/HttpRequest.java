package com.boredream.designrescollection.net;


import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import com.boredream.bdcodehelper.entity.AppUpdateInfo;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.designrescollection.base.BaseApplication;
import com.boredream.designrescollection.base.BaseEntity;
import com.boredream.designrescollection.constants.CommonConstants;
import com.boredream.designrescollection.entity.DesignRes;
import com.boredream.designrescollection.entity.FeedBack;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.utils.UserInfoKeeper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class HttpRequest {
    // LeanCloud
    public static final String HOST = "https://api.leancloud.cn";
    public static final String FILE_HOST = "";

    private static final String APP_ID_NAME = "X-LC-Id";
    private static final String API_KEY_NAME = "X-LC-Key";
    public static final String SESSION_TOKEN_KEY = "X-LC-Session";

    private static final String APP_ID_VALUE = "iaEH7ObIA4sPY8RSs3VCVXBg-gzGzoHsz";
    private static final String API_KEY_VALUE = "dXfhXIVyeWMN2czJkd4ehwzs";

    private static Retrofit retrofit;
    private static OkHttpClient httpClient;

    public static OkHttpClient getHttpClient() {
        return httpClient;
    }

    static {
        // OkHttpClient
        httpClient = new OkHttpClient();

        // 统一添加的Header
        httpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader(APP_ID_NAME, APP_ID_VALUE)
                        .addHeader(API_KEY_NAME, API_KEY_VALUE)
                        .addHeader(SESSION_TOKEN_KEY, UserInfoKeeper.getToken())
                        .build();
                return chain.proceed(request);
            }
        });

        // log
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(interceptor);

        // Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create()) // gson
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // rxjava
                .client(httpClient)
                //.callbackExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .build();
    }

    public interface ApiService {
        ////////////////////////////// 通用接口 //////////////////////////////
        // 登录用户
        @GET("/1/login")
        Observable<User> login(
                @Query("username") String username,
                @Query("password") String password);

        // 注册用户
        @POST("/1/users")
        Observable<User> register(
                @Body User user);

        // 修改用户详情(注意, 提交什么参数修改什么参数)
        @PUT("/1/users/{objectId}")
        Observable<BaseEntity> updateUserById(
                @Path("objectId") String userId,
                @Body Map<String, Object> updateInfo);

        // 上传图片接口
        @POST("/1/files/{fileName}")
        Observable<FileUploadResponse> fileUpload(
                @Path("fileName") String fileName,
                @Body RequestBody image);

        // 查询app更新信息
        @GET("/1/classes/AppUpdateInfo")
        Observable<ListResponse<AppUpdateInfo>> getAppUpdateInfo();

        // 提交意见反馈
        @POST("/1/classes/FeedBack")
        Observable<BaseEntity> addFeedBack(
                @Body FeedBack feedBack);


        ////////////////////////////// 业务接口 //////////////////////////////

        // 查询设计资源
        @GET("/1/classes/DesignRes")
        Observable<ListResponse<DesignRes>> getDesignRes(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where,
                @Query("include") String include);
    }

    public static ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }

    ////////////////////////////// 业务接口方法 //////////////////////////////

    /**
     * 查询设计资源
     */
    public static Observable<ListResponse<DesignRes>> getDesignRes(int page) {
        ApiService service = getApiService();
        String where = "{}";
        return service.getDesignRes(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where, null);
    }

    /**
     * 查询设计资源
     *
     * @param page
     * @param name 搜索名称
     */
    public static Observable<ListResponse<DesignRes>> getDesignRes(int page, String name) {
        ApiService service = getApiService();
        String whereName = "{}";
        if (!TextUtils.isEmpty(name)) {
            whereName = "{\"name\":{\"$regex\":\".*" + name + ".*\"}}";
        }
        String where = whereName;
        return service.getDesignRes(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where, null);
    }


    ////////////////////////////// 通用接口方法 //////////////////////////////

    /**
     * 登录用户
     *
     * @param username 用户名
     * @param password 密码
     */
    public static Observable<User> login(String username, String password) {
        ApiService service = getApiService();
        return service.login(username, password)
                .doOnNext(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        // 保存登录用户数据以及token信息
                        UserInfoKeeper.setCurrentUser(user);
                    }
                });
    }

    /**
     * 上传图片
     *
     * @param call    上传成功回调
     * @param uri     图片uri
     * @param reqW    上传图片需要压缩的宽度
     * @param reqH    上传图片需要压缩的高度
     * @param call
     */
    public static void fileUpload(Uri uri, int reqW, int reqH, final Subscriber<FileUploadResponse> call) {
        final ApiService service = getApiService();
        final String filename = "avatar_" + System.currentTimeMillis() + ".jpg";

        // 先从本地获取图片,利用Glide压缩图片后获取byte[]
        Glide.with(BaseApplication.getInstance()).load(uri).asBitmap().toBytes().into(
                new SimpleTarget<byte[]>(reqW, reqH) {
                    @Override
                    public void onResourceReady(final byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                        // 上传图片
                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), resource);

                        Observable<FileUploadResponse> observable = service.fileUpload(filename, requestBody);
                        ObservableDecorator.decorate(observable)
                                .subscribe(call);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        call.onError(new Throwable("图片解析失败"));
                    }
                });
    }

}

