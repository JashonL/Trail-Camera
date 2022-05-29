package com.shuoxd.camera.module.account;

import com.shuoxd.camera.base.BaseView;

public interface UserCenterView extends BaseView {

    void modifyUserInfoSuccess(String firstName, String lastName,
                               String address, String addressDetail,
                               String country, String state,
                               String city, String zipCode, String mobileNum);

    void modifyCardSuccess(String firstName, String lastName,
                           String address, String addressDetail,
                           String country, String state,
                           String city, String zipCode, String mobileNum);

    void updataUser();
}
