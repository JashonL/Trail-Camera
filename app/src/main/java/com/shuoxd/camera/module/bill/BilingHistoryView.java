package com.shuoxd.camera.module.bill;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.HistoryBean;

import java.util.List;

public interface BilingHistoryView extends BaseView {

    void showNoMoreData();

    void showMoreFail();

    void deleteSuccess();

    void showDatalist(List<HistoryBean> list);

}
