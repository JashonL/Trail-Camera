package com.shuoxd.camera.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.hjq.toast.ToastUtils;
import com.mylhyl.circledialog.BaseCircleDialog;
import com.mylhyl.circledialog.CircleDialog;
import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.bean.AppSystemDto;
import com.shuoxd.camera.module.plans.PlansDetailActivity;

public class AppUtils {


    public static void isUpgradeApp(Context context,boolean manual){
        AppSystemDto systemDto = App.getSystemDto();
        if (systemDto!=null){
            //是否有新版本
            String needUpgrade = systemDto.getNeedUpgrade();
            //是否强制升级
            String forcedUpgrade = systemDto.getForcedUpgrade();

            boolean isForce="1".equals(forcedUpgrade);
            //APP最新可升级的版本号
            String lastVersion = systemDto.getLastVersion();
            //App最新可升级版本地址
            String lastVersionUpgradeUrl = systemDto.getLastVersionUpgradeUrl();
            //APP最新版本是否为测试版本
            String lastVersionIsBeta = systemDto.getLastVersionIsBeta();
            //app本地升级内容
            String lastVersionUpgradeDescription = systemDto.getLastVersionUpgradeDescription();

            if ("1".equals(needUpgrade)){//有新版本

                View view = LayoutInflater.from(context).inflate(R.layout.app_updata_dialog, null);
                ImageView ivClose = view.findViewById(R.id.iv_close);
                Button btnUpDate = view.findViewById(R.id.btn_up_date);
                ProgressBar progressBar = view.findViewById(R.id.progressBar);
                TextView tvNewVersion = view.findViewById(R.id.tv_new_version);
                TextView tvLogReal = view.findViewById(R.id.tv_log_real);
                ivClose.setVisibility(isForce ? View.GONE : View.VISIBLE);

                //最新版本
                if (!TextUtils.isEmpty(lastVersion)) {
                    tvNewVersion.setText(lastVersion);
                }

                if (!TextUtils.isEmpty(lastVersionUpgradeDescription)) {
                    String replace = lastVersionUpgradeDescription.replace("\\n", "\n");
                    tvLogReal.setText(replace);
                }

                //显示弹框
                CircleDialog.Builder builder = new CircleDialog.Builder();
                builder.setBodyView(view, view1 -> {
                });
                builder.setGravity(Gravity.CENTER);
                builder.setCancelable(false);
                FragmentManager supportFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                BaseCircleDialog show = builder.show(supportFragmentManager);

                ivClose.setOnClickListener(v -> {
                    show.dialogDismiss();

                    if (isForce){
                        App.getInstance().exit();
                    }

                });


                btnUpDate.setOnClickListener(v -> {
                    //跳转到应用市场
                    show.dialogDismiss();
                    if ("0".equals(lastVersionIsBeta)){//正式版
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(
                                    lastVersionUpgradeUrl));
                            intent.setPackage("com.android.vending");
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Uri uri = Uri.parse(lastVersionUpgradeUrl);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
                        } finally {
                            if (isForce) {    //强制退出
                                App.getInstance().exit();
                            }
                        }
                    }else {//测试版
                        Uri uri = Uri.parse(lastVersionUpgradeUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);

                        if (isForce) {    //强制退出
                            App.getInstance().exit();
                        }
                    }
                });
            }else {
                if (manual){
                    ToastUtils.show(R.string.m280_no_newversion);
                }

            }


        }

    }







}
