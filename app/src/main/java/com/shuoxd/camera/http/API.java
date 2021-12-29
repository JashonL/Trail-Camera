package com.shuoxd.camera.http;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Description : API
 * 接口的管理类
 *
 * @author XuCanyou666
 * @date 2020/2/7
 */


public class API {

    static final String BASE_URL = "http://47.75.113.249:60011/";
//    static final String BASE_URL = "https://www.wanandroid.com/";

    private static final String OSS_URL = "http://oss1.growatt.com/";

    public static final String USER_URL = "http://server-api.growatt.com/";



    public interface WAZApi {

        //-----------------------【首页相关】----------------------


        //首页文章列表 这里的{}是填入页数
        @GET("article/list/{page}/json")
        Observable<String> getArticleList(@Path("page") Integer page);


        //-----------------------【登录注册】----------------------

        //登录
        @FormUrlEncoded
        @POST("v1/user/login")
        Observable<String> login(@Field("email") String username, @Field("password") String password);


        //注册
        @FormUrlEncoded
        @POST("/v1/user/register")
        Observable<String> register(@Field("email") String email, @Field("password") String password);

        //添加相机
        @FormUrlEncoded
        @POST("/v1/camera/addCamera")
        Observable<String> addCamera(@Field("imei") String imei);

        //相机详情
        @POST("/v1/camera/cameraInfo/{imei}")
        Observable<String> cameraInfo(@Path("imei") String imei,@Field("email")String email);



        //获取国家
        @GET(USER_URL + "newCountryCityAPI.do?op=getCountryList")
        Observable<String> getCountry();

        //根据国家获取服务器
        @GET(USER_URL + "newLoginAPI.do?op=getServerUrl")
        Observable<String> getServerCountry(@Query("country") String country);

        //获取邮箱验证码
        @FormUrlEncoded
        @POST
        Observable<String> getVerificationCode(@Url String url, @Field("accountName") String accountName, @Field("email") String email, @Field("language") String language);

        @FormUrlEncoded
        @POST
        Observable<String> groHomeRegister(@Url String url, @Field("regUserName") String regUserName, @Field("regPassword") String regPassword, @Field("regTimeZone") String regTimeZone, @Field("regEmail") String regEmail, @Field("regCountry") String regCountry);


        @FormUrlEncoded
        @POST
        Observable<String> updateUserPassword(@Url String url, @Field("accountName") String accountName, @Field("passwordOld") String passwordOld, @Field("passwordNew") String passwordNew);

        @FormUrlEncoded
        @POST
        Observable<String> validate(@Url String url, @Field("type") String type, @Field("content") String content);


        @FormUrlEncoded
        @POST
        Observable<String> updateValidate(@Url String url, @Field("type") String type, @Field("userName") String userName, @Field("content") String content);


        @FormUrlEncoded
        @POST
        Observable<String> updateUser(@Url String url, @Field("accountName") String accountName, @Field("email") String email);

        //忘记密码
        @FormUrlEncoded
        @POST
        Observable<String> resetPassWord(@Url String url, @Field("type") String type, @Field("param") String param, @Field("param2") String param2);

        //邮箱找回
        @FormUrlEncoded
        @POST
        Observable<String> sendResetEmailByAccount(@Url String url, @Field("accountName") String accountName);


        //根据国家获取或加码
        @POST("appService/getCountryCode")
        Observable<String> getCodeByCountry(@Body RequestBody body);

        //---------------------------【   收藏   】----------------------------------

        //收藏站内文章
        @POST("lg/collect/{id}/json")
        Observable<String> collectIn(@Path("id") Integer id);

        //取消收藏---文章列表
        @POST("lg/uncollect_originId/{id}/json")
        Observable<String> uncollect(@Path("id") Integer id);


        //---------------------------【   首页   】----------------------------------
        //获取设备列表
        @POST("room/")
        Observable<String> getAllDevice(@Body RequestBody body);


        //获取设备数据
        @FormUrlEncoded
        @POST("/v1/camera/cameraList/1")
        Observable<String> cameraList(@Field("email") String email);

        //---------------------------【   设备   】-----------------------------------
        @POST("tuya/addDevice")
        Observable<String> addDevice(@Body RequestBody body);


        @POST("tuya/addDeviceNew")
        Observable<String> addDeviceNew(@Body RequestBody body);

        @POST("tuya/switchInfo")
        Observable<String> getSwitchDetail(@Body RequestBody body);

        @POST("tuya/editBulbInfo")
        Observable<String> editBulbInfo(@Body RequestBody body);

        @POST("tuya/getBulbInfo")
        Observable<String> getBulbInfo(@Body RequestBody body);

        @POST("tuya/removeDevice")
        Observable<String> deleteDevice(@Body RequestBody body);

        @POST("tuya/editDevName")
        Observable<String> editDevName(@Body RequestBody body);

        @POST("tuya/updateSwitchName")
        Observable<String> updateSwitchName(@Body RequestBody body);

        @POST("/tuya/updateOnoffName")
        Observable<String> updateOnoffName(@Body RequestBody body);
        //---------------------------【   房间   】-----------------------------------

        //获取房间列表
        @POST("room/")
        Observable<String> roomRequest(@Body RequestBody body);

        @POST("room/addRoom")
        Observable<String> createRoom(@Body RequestBody body);

        @POST("room/")
        Observable<String> editRoomName(@Body RequestBody body);

        @POST("room/updateImage")
        Observable<String> updateImage(@Body RequestBody body);

        @POST("tuya/removeDevice")
        Observable<String> removeDevice(@Body RequestBody body);


        //---------------------------【   场景   】-----------------------------------
        @POST("smartHome/")
        Observable<String> smartHomeRequest(@Body RequestBody body);

        //---------------------------【    服务页面      】------------------------------------------
        //首页文章列表 这里的{}是填入页数
        @GET
        Observable<String> getAdvertisingList(@Url String url);

        @POST("appService/manualList")
        Observable<String> manualList(@Body RequestBody body);

        @POST("appService/methodFile ")
        Observable<String> methodFile(@Body RequestBody body);


        //---------------------------【    关于      】------------------------------------------
        @GET
        Observable<String> getServicePhoneNum(@Url String url);

        //----------------------------【  隐私政策&用户协议  】-----------------------------------------
        @POST("appService/privacyPolicy")
        Observable<String> privacyPolicy(@Body RequestBody body);

        @POST("appService/userAgreement ")
        Observable<String> userAgreement(@Body RequestBody body);

        //-----------------------------【         消息中心           】-----------------------------------------
        @POST("appService/getLoginRecord")
        Observable<String> getLoginRecord(@Body RequestBody body);

    }

}
