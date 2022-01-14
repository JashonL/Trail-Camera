package com.shuoxd.camera;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAbout extends AppCompatActivity {

    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        try {
            String name=getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvVersion.setText("版本："+name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
