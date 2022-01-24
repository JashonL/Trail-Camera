package com.shuoxd.camera.http;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    static final String BASE_URL = "http://camera.shuoxd.com:50010/";
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

        //登出
        @FormUrlEncoded
        @POST("v1/user/logOut")
        Observable<String> loginOut(@Field("email") String username);




        //注册
        @FormUrlEncoded
        @POST("/v1/user/register")
        Observable<String> register(@Field("email") String email, @Field("password") String password);



        //登录
        @FormUrlEncoded
        @POST("/v1/user/findPassword")
        Observable<String> findPassword(@Field("type") String type, @Field("value") String value);


        //添加相机
        @FormUrlEncoded
        @POST("/v1/camera/addCamera")
        Observable<String> addCamera(@Field("imei") String imei,@Field("careamName")String careamName);



        //----------------------------【获取相机相册】--------------------------------

        //修改用户信息
        @FormUrlEncoded
        @POST("/v1/user/modifyUserInfo")
        Observable<String> modifyUserInfo(@Field("firstName") String firstName,@Field("lastName")String lastName,
                                          @Field("address") String address,@Field("careamName")String addressDetail,
                                          @Field("country") String country,@Field("state")String state,
                                          @Field("city") String city,@Field("zipCode")String zipCode,@Field("mobileNum")String mobileNum);




        //修改银行卡信息
        @FormUrlEncoded
        @POST("/v1/user/modifyCreditCard")
        Observable<String> modifyCreditCard(@Field("cardName") String cardName,@Field("cardAddr")String cardAddr,
                                          @Field("cardCity") String cardCity,@Field("cardCountry")String cardCountry,
                                          @Field("cardState") String cardState,@Field("cardZip")String cardZip,
                                          @Field("cardNum") String cardNum,@Field("cardYear")String cardYear,@Field("cardMonth")String cardMonth);





        //修改密码
        @FormUrlEncoded
        @POST("/v1/user/modifyPassword")
        Observable<String> modifyPassword(@Field("oldPassword") String oldPassword,@Field("newPassword")String newPassword,@Field("confirmPassword")String confirmPassword);



        //相机详情
        @FormUrlEncoded
        @POST("/v1/camera/cameraInfo/{imei}/")
        Observable<String> cameraInfo(@Path("imei") String imei,@Field("email")String email);


        //相机设置获取
        @FormUrlEncoded
        @POST("/v1/camera/cameraParamter/{imei}/")
        Observable<String> cameraParamter(@Path("imei") String imei,@Field("email")String email);


        //相机设置
        @FormUrlEncoded
        @POST("/v1/camera/control/{imei}/")
        Observable<String> control(@Path("imei") String imei,@Field("operationType")String operationType,@Field("operationValue")String operationValue);


        @FormUrlEncoded
        @POST("/v1/camera/operation/{imei}")
        Observable<String> operation_camera(@Path("imei") String imei,@Field("operationType")String operationType,@Field("operationValue")String operationValue);



        //相机图片
        @FormUrlEncoded
        @POST("/v1/camera/photoList/{toPageNum}")
        Observable<String> photoList(@Path("toPageNum")int toPageNum,
                @Field("imeis")String imeis,@Field("isAllCamera")String isAllCamera,
                                     @Field("startDate")String startDate,@Field("endDate")String endDate,
                                     @Field("amPm")String amPm,@Field("photoType")String photoType,
                                     @Field("favorites")String favorites,@Field("moonPhase")String moonPhase,
                                     @Field("startTemperature")String startTemperature,@Field("endTemperature")String endTemperature,
                                     @Field("temperatureUnit")String temperatureUnit

        );




        //获取设备数据
        @FormUrlEncoded
        @POST("/v1/camera/allCameraList")
        Observable<String> allCameraList(@Field("email") String email);



        //获取设备数据
        @FormUrlEncoded
        @POST("/v1/camera/photo/operation/{photoId}")
        Observable<String> operation(@Path("photoId") String photoId,@Field("operationType")String operationType,@Field("operationValue")String operationValue);

        //----------------------------【获取图表消息】--------------------------------
        //获取图表数据
        @FormUrlEncoded
        @POST("/v1/camera/photo/chart/{dateType}")
        Observable<String> chart(@Path("dateType") String dateType,@Field("isAllCamera")String isAllCamera,@Field("imeis")String imeis,@Field("date")String date);



        //----------------------------【获取消息】--------------------------------

        @FormUrlEncoded
        @POST("/v1/message/messageList/{toPageNum}")
        Observable<String> messageList(@Path("toPageNum")int toPageNum,@Field("email")String email);


        @FormUrlEncoded
        @POST("/v1/message/questionList/{toPageNum}")
        Observable<String> questionList(@Path("toPageNum")int toPageNum,@Field("email")String email);



        @FormUrlEncoded
        @POST("/v1/message/question/{questionId}")
        Observable<String> question(@Path("questionId")String questionId,@Field("email")String email);


        @Multipart
        @POST("/v1/message/replyQuestion/{questionId}")
        Observable<String> replyQuestion(@Path("questionId")String questionId,
                                         @PartMap Map<String, RequestBody> map,
                                         @Part List<MultipartBody.Part> parts);



        @Multipart
        @POST("/v1/message/addQuestion")
        Observable<String> addQuestion(@PartMap Map<String, RequestBody> map,
                                         @Part List<MultipartBody.Part> parts);


        @FormUrlEncoded
        @POST("/v1/message/question/operation/{questionId}")
        Observable<String> operation(@Path("questionId")String questionId,@Field("operationType")String operationType);




        @FormUrlEncoded
        @POST("/v1/message/operation/{msgId}")
        Observable<String> msgOperation(@Path("questionId")String questionId,@Field("operationType")String operationType);








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
        @POST("/v1/camera/cameraList/{toPageNum}/")
        Observable<String> cameraList(@Path("toPageNum")int toPageNum,@Field("email") String email);

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
