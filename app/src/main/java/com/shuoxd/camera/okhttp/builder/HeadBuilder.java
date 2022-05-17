package com.shuoxd.camera.okhttp.builder;


import com.shuoxd.camera.okhttp.OkHttpUtils;
import com.shuoxd.camera.okhttp.request.OtherRequest;
import com.shuoxd.camera.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
