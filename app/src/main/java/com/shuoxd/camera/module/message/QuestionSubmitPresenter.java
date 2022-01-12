package com.shuoxd.camera.module.message;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.view.listener.OnLvItemClickListener;
import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.constants.AllPermissionRequestCode;
import com.shuoxd.camera.utils.PhotoUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

public class QuestionSubmitPresenter extends BasePresenter<QuestionView> {


    private Uri imageUri;
    //请求码
    public static final int CODE_GALLERY_REQUEST = 101;
    public static final int CODE_CAMERA_REQUEST = 102;

    private List<CameraBean> cameraList = new ArrayList<>();


    private String imei;


    public QuestionSubmitPresenter(Context context, QuestionView baseView) {
        super(context, baseView);
    }


    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void changeImgDialog() {
        List<String> photos = new ArrayList<>();
        photos.add(context.getString(R.string.m141_take_photo));
        photos.add(context.getString(R.string.m142_choose_photos));
        new CircleDialog.Builder()
                .setTitle(context.getString(R.string.m140_selection))
                .setItems(photos, new OnLvItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                //请求拍照权限
                                if (EasyPermissions.hasPermissions(context, AllPermissionRequestCode.PERMISSION_CAMERA)) {
                                    try {
                                        takePicture();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    EasyPermissions.requestPermissions((Activity) context, String.format("%s:%s", context.getString(R.string.m143_request_permission), context.getString(R.string.m144_camera_or_storage)), AllPermissionRequestCode.PERMISSION_CAMERA_CODE, AllPermissionRequestCode.PERMISSION_CAMERA);
                                }
                                break;
                            case 1:
                                if (EasyPermissions.hasPermissions(context, AllPermissionRequestCode.PERMISSION_EXTERNAL_STORAGE)) {
                                    selectPicture();
                                } else {
                                    EasyPermissions.requestPermissions((Activity) context, String.format("%s:%s", context.getString(R.string.m143_request_permission), context.getString(R.string.m144_camera_or_storage)), AllPermissionRequestCode.PERMISSION_EXTERNAL_STORAGE_CODE, AllPermissionRequestCode.PERMISSION_EXTERNAL_STORAGE);
                                }
                                break;
                        }
                        return true;
                    }
                })
                .setGravity(Gravity.BOTTOM)
                .setNegative(context.getString(R.string.m127_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show(((FragmentActivity) context).getSupportFragmentManager());


    }



    public void takePicture() throws IOException {
        imageUri = PhotoUtil.getImageUri((FragmentActivity) context, PhotoUtil.getFile());
        PhotoUtil.takePicture((Activity) context, imageUri, CODE_CAMERA_REQUEST);
    }


    public void selectPicture() {
        PhotoUtil.openPic((Activity) context, CODE_GALLERY_REQUEST);
    }

    public void startCrop() throws IOException {
        Uri cropImageUri = Uri.fromFile(PhotoUtil.getFile());
        PhotoUtil.startCrop((FragmentActivity) context, imageUri, cropImageUri);
    }


    public void getAlldevice() {
        String accountName = App.getUserBean().getAccountName();
        //获取设备
        addDisposable(apiServer.allCameraList(accountName), new BaseObserver<String>(baseView, false) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        JSONArray obj = jsonObject.getJSONArray("obj");
                        //解析相机数据
                        int totalNum=0;
                        cameraList.clear();
                        for (int i = 0; i < obj.length(); i++) {
                            JSONObject jsonObject1 = obj.getJSONObject(i);
                            CameraBean cameraBean = new Gson().fromJson(jsonObject1.toString(), CameraBean.class);
                            String totalPhotoNum = cameraBean.getTotalPhotoNum();
                            totalNum += Integer.parseInt(totalPhotoNum);
                            cameraList.add(cameraBean);
                        }

                   /*     CameraBean cameraBean =new CameraBean();
                        cameraBean.setTotalPhotoNum(String.valueOf(totalNum));
                        CameraBean.CameraInfo info=new CameraBean.CameraInfo();
                        info.setAlias(context.getString(R.string.m77_all_camera));
                        cameraBean.setCamera(info);
                        cameraBean.setSelected(true);
                        cameraList.add(0,cameraBean);*/

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showServerError(msg);
            }
        });


    }




    public void replyQuestion(String title,String content, List<File> fileList) {
        Map<String, RequestBody> params = new HashMap<>();

        params.put("content", convertToRequestBody(content));
        params.put("title", convertToRequestBody(title));
        params.put("imei", convertToRequestBody(imei));


        List<MultipartBody.Part> partList = filesToMultipartBodyParts(fileList);

        //获取设备
        addDisposable(apiServer.addQuestion( params, partList), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        baseView.submitSuccess();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showServerError(msg);
            }
        });
    }



    private RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }

    private List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());


        for (int i = 0; i < files.size(); i++) {
            String key = "";
            switch (i) {
                case 0:
                    key = "attachmentOne";
                    break;
                case 1:
                    key = "attachmentTwo";
                    break;
                case 2:
                    key = "attachmentThree";
                    break;
                default:
                    key = "attachmentThree";
                    break;

            }

            File file = files.get(i);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
            parts.add(part);
        }

        return parts;
    }



    public List<CameraBean> getCameraList() {
        return cameraList;
    }
}
