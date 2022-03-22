package com.shuoxd.camera.module.pictrue;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ortiz.touchview.TouchImageView;
import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.utils.CircleDialogUtils;
import com.shuoxd.camera.utils.GlideUtils;
import com.shuoxd.camera.utils.MyToastUtils;
import com.shuoxd.camera.utils.ShareUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BigImageActivty extends BaseActivity<BigImagePresenter> implements BigImageView {


    @BindView(R.id.iv_touch)
    TouchImageView ivTouch;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private Bitmap bitmap;
    private String name;

    @Override
    protected BigImagePresenter createPresenter() {
        return new BigImagePresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_big_pictrue;
    }

    @Override
    protected void initViews() {

        ivTouch.setOnLongClickListener(view -> {
            CircleDialogUtils.showCommentDialog(BigImageActivty.this, getString(R.string.m150_tips), getString(R.string.m220_save_pic),
                    getString(R.string.m152_ok), getString(R.string.m127_cancel), Gravity.CENTER, view1 -> {
                        try {
                            if (bitmap!=null){
                                String path = saveImage(bitmap, name);
                                ShareUtils.insertAlbum(this,name,new File(path));
                            }else {
                                MyToastUtils.toast(R.string.m221_save_error);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }, view12 -> {
                    });

            return false;
        });


    }


    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
/*        //设置共同沉浸式样式
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(false, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .statusBarColor(R.color.nocolor)//这里的颜色，你可以自定义。
                .init();*/
    }


    @Override
    protected void initData() {
        String path = getIntent().getStringExtra("path");
         name = getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(name)) {
            tvTitle.setText(name);
        }
        if (!TextUtils.isEmpty(path)) {
            GlideUtils.getInstance().showImageContext(mContext, R.drawable.deer, R.drawable.deer, path, ivTouch);


            Glide.with(mContext)
                    .asBitmap()
                    .load(path)
                    .placeholder(R.drawable.kaola).error(R.drawable.kaola).dontAnimate()
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            bitmap = resource;
                            ivTouch.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    private String saveImage(Bitmap image, String id) {
        String saveImagePath = null;
        String imageFileName = "JPEG_" + "down" + id + ".jpg";
        String parentPath = App.getInstance().getFilesDir().getPath();


        File storageDir = new File(parentPath);

        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            saveImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fout = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
//            galleryAddPic(saveImagePath);
        }
        return saveImagePath;
    }

}
