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

        //-----------------------【登录注册】----------------------

        //登录
        @FormUrlEncoded
        @POST("v1/user/login")
        Observable<String> login(@Field("email") String username, @Field("password") String password,
                                 @Field("phoneOs") String phoneOs, @Field("phoneModel") String phoneModel,
                                 @Field("appVersion") String appVersion
        );

        //登出
        @FormUrlEncoded
        @POST("v1/user/logOut")
        Observable<String> loginOut(@Field("email") String username);




        //注册
        @FormUrlEncoded
        @POST("/v1/user/register")
        Observable<String> register(@Field("timeZone")String timeZone,@Field("email") String email, @Field("password") String password);



        //找回密码
        @FormUrlEncoded
        @POST("/v1/user/findPassword")
        Observable<String> findPassword(@Field("type") String type, @Field("value") String value,@Field("emailCode")String emailCode);




        //获取验证码
        @FormUrlEncoded
        @POST("/v1/user/sendEmailCode")
        Observable<String> sendEmailCode(@Field("type") String type,@Field("value") String value);




        //添加相机
        @FormUrlEncoded
        @POST("/v1/camera/addCamera")
        Observable<String> addCamera(@Field("imei") String imei,@Field("careamName")String careamName,
                                     @Field("longitude")String lng,@Field("latitude")String latitude);



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
        Observable<String> modifyPassword(@Field("emailCode") String emailCode,@Field("newPassword")String newPassword,@Field("confirmPassword")String confirmPassword);



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


        //批量操作
        @FormUrlEncoded
        @POST("v1/camera/photo/operation/batch/{photoIds}")
        Observable<String> photoIds(@Path("photoIds") String photoId,@Field("operationType")String operationType,@Field("operationValue")String operationValue);


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
        Observable<String> msgOperation(@Path("msgId")String msgId,@Field("operationType")String operationType);








        //---------------------------【   首页   】----------------------------------

        //获取设备数据
        @FormUrlEncoded
        @POST("/v1/camera/cameraList/{toPageNum}/")
        Observable<String> cameraList(@Path("toPageNum")int toPageNum,@Field("email") String email);




        //获取plans详情
        @FormUrlEncoded
        @POST("/v1/plan/getPlan/")
        Observable<String> getPlan(@Field("imei") String imei);


        //获取套餐列表
        @FormUrlEncoded
        @POST("/v1/plan/getPlanTemplateList/")
        Observable<String> getPlanTemplateList(@Field("imei") String imei);


        //修改套餐
        @FormUrlEncoded
        @POST("/v1/plan/modifyCameraPlan/{imei}")
        Observable<String> modifyCameraPlan(@Path("imei") String imei,@Field("planTemplateId")String planTemplateId);


        //启用套餐
        @FormUrlEncoded
        @POST("/v1/plan/changePlanStatus/{imei}")
        Observable<String> changePlanStatus(@Path("imei") String imei);


        //
        @FormUrlEncoded
        @POST("/v1/message/faqList/")
        Observable<String> faqList(@Field("email") String email);



        //账单详情
        @FormUrlEncoded
        @POST("/v1/bill/billLogList/{toPageNum}")
        Observable<String> billLogList(@Path("toPageNum")int toPageNum, @Field("imei")String imei,
                                       @Field("isAllCamera")String isAllCamera);


        //账单详情
        @FormUrlEncoded
        @POST("/v1/bill/billLogChart/")
        Observable<String> billLogChart(@Field("imei")String imei,@Field("isAllCamera")String isAllCamera
        ,@Field("date")String date);

        //账单详情
        @FormUrlEncoded
        @POST("/v1/user/userCenter/")
        Observable<String> userCenter(@Field("imei")String imei);

    }

}
