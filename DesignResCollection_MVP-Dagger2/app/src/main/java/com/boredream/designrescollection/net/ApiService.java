package com.boredream.designrescollection.net;

import com.boredream.bdcodehelper.entity.AppUpdateInfo;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.designrescollection.base.BaseEntity;
import com.boredream.designrescollection.entity.DesignRes;
import com.boredream.designrescollection.entity.FeedBack;
import com.boredream.designrescollection.entity.User;
import com.squareup.okhttp.RequestBody;

import java.util.Map;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

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
            @Query("skip") int page);
}