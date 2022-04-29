package com.shuoxd.camera.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shuoxd.camera.R;


/**
 * Created by dg on 2017/7/25.
 */

public class GlideUtils {
    private static GlideUtils inst;

    public static GlideUtils getInstance() {
        if (inst == null) {
            inst = new GlideUtils();
        }
        return inst;
    }
    public  void showImageContext(Context context, String path, ImageView iv){
        showImageContext(context, R.drawable.default_pic,R.drawable.default_pic,path,iv);
    }
    public  void showImageContext(Context context, int placeholderRes, int errorRes, String path, ImageView iv){
        Glide.with(context).load(path).placeholder(placeholderRes).error(errorRes).dontAnimate().into(iv);
    }
    public  void showImageAct(Activity act, String path , ImageView iv){
        showImageAct(act,R.drawable.default_pic,R.drawable.default_pic,path,iv);
    }
    public  void showImageAct(Activity act, int resId , ImageView iv){
        showImageAct(act,R.drawable.default_pic,R.drawable.default_pic,resId,iv);
    }
    public  void showImageAct(Activity act, int placeholderRes, int errorRes, String path , ImageView iv){
        Glide.with(act).load(path).placeholder(placeholderRes).error(errorRes).dontAnimate().into(iv);
    }
    public  void showImageActNoCache(Activity act, int placeholderRes, int errorRes, String path , ImageView iv){
        Glide.with(act).load(path).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(placeholderRes).error(errorRes).dontAnimate().into(iv);
    }
    public  void showImageAct(Activity act, int placeholderRes, int errorRes, int resId , ImageView iv){
        Glide.with(act).load(resId).placeholder(placeholderRes).error(errorRes).dontAnimate().into(iv);
    }
    public  void showImageContext(Context con, int placeholderRes, int errorRes, int resId , ImageView iv){
        Glide.with(con).load(resId).placeholder(placeholderRes).error(errorRes).dontAnimate().into(iv);
    }


    public  void showImageAct(Context context, int placeholderRes, int errorRes, String path , ImageView iv){
        Glide.with(context).load(path).placeholder(placeholderRes).error(errorRes).dontAnimate().into(iv);
    }


    public void setImageBackground(Context context, int resId , View view){
        Glide.with(context)
                .asBitmap()
                .load(resId)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                        view.setBackground(drawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    /**
     *
     * @param act
     * @param resId
     * @param iv
     * @param type:0:表示没有占位图;1:默认占位图；其他为咱位图资源
     */
    public  void showImageAct(Activity act, int resId , ImageView iv, int type){
        switch (type){
            case 0:
                Glide.with(act).load(resId).error(R.drawable.default_pic).dontAnimate().into(iv);
                break;
            case 1:
                Glide.with(act).load(resId).placeholder(R.drawable.default_pic).error(R.drawable.default_pic).dontAnimate().into(iv);
                break;
            default:
                Glide.with(act).load(resId).placeholder(type).error(R.drawable.default_pic).dontAnimate().into(iv);
                break;
        }
    }


}
