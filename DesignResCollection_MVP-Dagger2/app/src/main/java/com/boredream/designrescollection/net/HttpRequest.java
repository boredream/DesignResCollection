package com.boredream.designrescollection.net;


import android.text.TextUtils;

import com.boredream.bdcodehelper.entity.AppUpdateInfo;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.designrescollection.base.BaseEntity;
import com.boredream.designrescollection.constants.CommonConstants;
import com.boredream.designrescollection.entity.DesignRes;
import com.boredream.designrescollection.entity.FeedBack;
import com.boredream.designrescollection.entity.User;
import com.boredream.designrescollection.utils.UserInfoKeeper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

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
import rx.functions.Action1;

public class HttpRequest {
    // LeanCloud
    public static final String HOST = "https://api.leancloud.cn";

    public ApiService service;

    @Inject OkHttpClient httpClient;

    private static HttpRequest ourInstance = new HttpRequest();
    public static HttpRequest getInstance() {
        return ourInstance;
    }
    private HttpRequest() {
        DaggerHttpComponent.builder()
                .build()
                .inject(this);

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create()) // gson
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // rxjava
                .client(httpClient)
                //.callbackExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .build();

        service = retrofit.create(ApiService.class);
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

    ////////////////////////////// 业务接口方法 //////////////////////////////

    /**
     * 查询设计资源
     */
    public Observable<ListResponse<DesignRes>> getDesignRes(int page) {
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
    public Observable<ListResponse<DesignRes>> getDesignRes(int page, String name) {
        String whereName = "{}";
        if (!TextUtils.isEmpty(name)) {
            whereName = "{\"name\":{\"$regex\":\".*" + name + ".*\"}}";
        }
        String where = whereName;
        return service.getDesignRes(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where, null);
    }

    public Observable<BaseEntity> updateNickname(String userid, String nickname) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("nickname", nickname);

        return service.updateUserById(userid, updateMap);
    }


    ////////////////////////////// 通用接口方法 //////////////////////////////

    /**
     * 登录用户
     *
     * @param username 用户名
     * @param password 密码
     */
    public Observable<User> login(String username, String password) {
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
     * 上传文件
     *
     * @param bytes
     */
    public Observable<FileUploadResponse> fileUpload(byte[] bytes, String filename, MediaType type) {
        RequestBody requestBody = RequestBody.create(type, bytes);
        return service.fileUpload(filename, requestBody);
    }

}

