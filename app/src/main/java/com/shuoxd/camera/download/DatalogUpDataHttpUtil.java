package com.shuoxd.camera.download;

import androidx.annotation.NonNull;


import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.okhttp.OkHttpUtils;
import com.shuoxd.camera.okhttp.callback.FileCallBack;
import com.shuoxd.camera.utils.MyToastUtils;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class DatalogUpDataHttpUtil implements HttpManager {
    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {

    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {

    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull FileCallback callback) {
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(path, fileName) {
                             @Override
                             public void onError(Call call, Exception e, int id) {

                                 String errorMsg = App.getInstance().getString(R.string.m3_bad_network_msg);
                                 if (e != null) {
                                     if (e instanceof SocketTimeoutException) {
                                         errorMsg = App.getInstance().getString(R.string.m5_connect_timeout_msg);
                                     }
                                     if (e instanceof ConnectException) {
                                         errorMsg = App.getInstance().getString(R.string.m4_connect_error_msg);
                                     }
                                 }
                                 MyToastUtils.toast(errorMsg);
                             }

                             @Override
                             public void onResponse(File response, int id) {
                                 callback.onResponse(response);
                             }

                             @Override
                             public void onBefore(Request request, int id) {
                                 super.onBefore(request, id);
                                 callback.onBefore();
                             }

                             @Override
                             public void inProgress(float progress, long total, int id) {
                                 callback.onProgress(progress, total);
                             }


                         }


                );
    }
}
