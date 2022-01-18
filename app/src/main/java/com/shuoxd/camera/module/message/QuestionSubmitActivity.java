package com.shuoxd.camera.module.message;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.AddImageAdapter;
import com.shuoxd.camera.adapter.CameraMulFiterAdapter;
import com.shuoxd.camera.adapter.CameraNameAdapter;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.AddImageBean;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.constants.AllPermissionRequestCode;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.eventbus.FreshQuestion;
import com.shuoxd.camera.module.pictrue.BigImageActivty;
import com.shuoxd.camera.utils.ImagePathUtil;
import com.shuoxd.camera.utils.MyToastUtils;
import com.shuoxd.camera.utils.PhotoUtil;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class QuestionSubmitActivity extends BaseActivity<QuestionSubmitPresenter> implements QuestionView,
        Toolbar.OnMenuItemClickListener, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_descript)
    EditText etDescript;
    @BindView(R.id.et_next)
    TextView etNext;
    @BindView(R.id.ll_pic)
    ConstraintLayout llPic;
    @BindView(R.id.btn_submit)
    AppCompatButton btnSubmit;
    @BindView(R.id.rlv_reply_pic)
    RecyclerView rlvReplyPic;

    private AddImageAdapter addAdapter;

    @Override
    protected QuestionSubmitPresenter createPresenter() {
        return new QuestionSubmitPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_question_submit;
    }

    @Override
    protected void initViews() {
        //toolbar
        initToobar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m81_Message);
        //图片列表

        setAddAdapter();
    }


    //添加的图片
    private void setAddAdapter() {
        rlvReplyPic.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        addAdapter = new AddImageAdapter(new ArrayList<>());
        rlvReplyPic.setAdapter(addAdapter);
        rlvReplyPic.addItemDecoration(new LinearDivider(this, LinearLayoutManager.HORIZONTAL, 20, ContextCompat.getColor(this, R.color.nocolor)));
        addAdapter.setOnItemClickListener(this);
        addAdapter.setOnItemChildClickListener(this);
    }


    @Override
    protected void initData() {
        //获取所有相机
        presenter.getAlldevice();


        //添加一个添加按钮
        ArrayList<AddImageBean> newList = new ArrayList<>();
        AddImageBean bean = new AddImageBean();
        bean.setType(1);
        newList.add(bean);
        addAdapter.replaceData(newList);
    }


    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_delete:
                List<AddImageBean> data = addAdapter.getData();
                List<AddImageBean> newList = new ArrayList<>(data);
                //删除当前这个
                newList.remove(position);
                AddImageBean bean1 = newList.get(newList.size() - 1);
                int type = bean1.getType();
                if (type != 1) {
                    //最后一个是添加按钮
                    AddImageBean bean = new AddImageBean();
                    bean.setType(1);
                    newList.add(bean);
                }
                addAdapter.replaceData(newList);
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == addAdapter) {
            AddImageBean bean = addAdapter.getData().get(position);
            int itemType = bean.getItemType();
            String path = bean.getPath();
            if (itemType == 1) {
                presenter.changeImgDialog();
            } else {//放大图片
                Intent intent = new Intent(this, BigImageActivty.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == QuestionDetailPresenter.CODE_GALLERY_REQUEST) {
                if (data != null) {
                    try {
                        PhotoUtil.startCropImageAct(this, data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == QuestionDetailPresenter.CODE_CAMERA_REQUEST) {
                try {
                    presenter.startCrop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
    }


    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            setImageToHeadView(resultUri);
        } else {
            MyToastUtils.toast(R.string.m86_fail);
        }
    }

    //添加图片
    private void setImageToHeadView(Uri uri) {
        try {
            String path = ImagePathUtil.getRealPathFromUri(this, uri);
            File file = new File(path);
            if (file.length() > 10 * 1024 * 1024) {
                MyToastUtils.toast(R.string.m146_must_smaller);
                return;
            }
            ;

            //更新图片
//            GlideUtils.showImageAct(this, R.drawable.kaola, R.drawable.kaola, plantPath, ivPhoto);
            List<AddImageBean> data = addAdapter.getData();
            //最后一个是添加按钮
            AddImageBean bean = new AddImageBean();
            bean.setType(0);
            bean.setPath(path);
            //插入倒数第二个
            data.add(data.size() - 1, bean);
            if (data.size() > 3) {
                data.remove(data.size() - 1);
            }
            addAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case AllPermissionRequestCode.PERMISSION_CAMERA_CODE:
                if (EasyPermissions.hasPermissions(this, AllPermissionRequestCode.PERMISSION_CAMERA)) {
                    try {
                        presenter.takePicture();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case AllPermissionRequestCode.PERMISSION_EXTERNAL_STORAGE_CODE:
                if (EasyPermissions.hasPermissions(this, AllPermissionRequestCode.PERMISSION_EXTERNAL_STORAGE)) {
                    presenter.selectPicture();
                }
                break;
        }
    }



    @OnClick({R.id.et_next, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_next:
                View contentView = LayoutInflater.from(this).inflate(
                        R.layout.pop_layout, null);

                List<CameraBean> cameraList = presenter.getCameraList();


                RecyclerView rvCamera = contentView.findViewById(R.id.ry_camera);
                rvCamera.setLayoutManager(new LinearLayoutManager(this));
                CameraNameAdapter camerAdapter=new CameraNameAdapter(R.layout.item_text,cameraList);
                rvCamera.setAdapter(camerAdapter);



                int width = getResources().getDimensionPixelSize(R.dimen.dp_225);
                int hight = getResources().getDimensionPixelSize(R.dimen.dp_248);
                int itemHight = getResources().getDimensionPixelOffset(R.dimen.dp_40);


                if (itemHight * cameraList.size() > hight) {
                    hight = getResources().getDimensionPixelSize(R.dimen.dp_248);
                } else {
                    hight = LinearLayout.LayoutParams.WRAP_CONTENT;
                }

                final PopupWindow popupWindow = new PopupWindow(contentView,etNext.getWidth(), hight, true);


                camerAdapter.setOnItemClickListener((adapter, view1, position) -> {
                    String imei = cameraList.get(position).getCamera().getImei();
                    presenter.setImei(imei);
                    if (!TextUtils.isEmpty(imei)){
                        etNext.setText(imei);
                    }
                    popupWindow.dismiss();
                });




                popupWindow.setTouchable(true);
                popupWindow.setTouchInterceptor((v, event) -> false);
                WindowManager.LayoutParams lp = this.getWindow().getAttributes();
                popupWindow.setBackgroundDrawable(new ColorDrawable(0));
                popupWindow.setAnimationStyle(R.style.Popup_Anim);
                popupWindow.showAsDropDown(etNext);
                break;
            case R.id.btn_submit:

                String title = etTitle.getText().toString().trim();
                String desscript = etDescript.getText().toString().trim();
                if (TextUtils.isEmpty(desscript)||TextUtils.isEmpty(title)) {
                    MyToastUtils.toast(R.string.m145_content_cannot_empty);
                    return;
                }

                List<AddImageBean> data = addAdapter.getData();
                List<File> files = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    AddImageBean bean = data.get(i);
                    String path = bean.getPath();
                    if (!TextUtils.isEmpty(path)){
                        File file = new File(path);
                        files.add(file);
                    }
                }
                presenter.replyQuestion(title,desscript, files);
                break;
        }
    }

    @Override
    public void submitSuccess() {
        EventBus.getDefault().post(new FreshQuestion());
        MyToastUtils.toast(R.string.m148_success);
        finish();
    }
}
