package com.shuoxd.camera.module.plans;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.ProgramBean;

import java.util.List;

public interface PlansChangeView extends BaseView {

    void showAnnual(List<ProgramBean> lists);

    void showMonthly(List<ProgramBean> lists);



    void annualSelected(int selected);

    void monthlySelected(int selected);
}
