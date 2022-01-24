package com.shuoxd.camera.module.account;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.SceneViewPagerAdapter;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.ProvinceCityBean;
import com.shuoxd.camera.utils.CountryDataUtils;
import com.shuoxd.camera.utils.MyToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserCenterActivity extends BaseActivity<UserCenterPresenter> implements UserCenterView, Toolbar.OnMenuItemClickListener, View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.OnItemClickListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_title)
    TabLayout tabTitle;


    private LinearLayout llAddLaunchView;
    private LinearLayout llAddLinkageView;

    private SceneViewPagerAdapter mSceneViewPagerAdapter;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etAddress;
    private EditText etAddress2;
    private EditText etCity;
    private TextView tvStateValue;
    private TextView tvCountry;
    private EditText etZip;
    private EditText etMobileNumber;


    private CheckBox cbSame;
    private EditText creditAddress;
    private EditText creditAddress2;
    private EditText creditCity;
    private TextView creditStateValue;
    private TextView creditCountryValue;
    private EditText creditContry;
    private EditText creditMobileNumber;
    private TextView creditMonthValue;
    private TextView creditYearValue;


    @Override
    protected UserCenterPresenter createPresenter() {
        return new UserCenterPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_center;
    }

    @Override
    protected void initViews() {
        //头部toolBar
        initToobar(toolbar);
        tvTitle.setText(R.string.m82_account);

    }


    @Override
    protected void initData() {
        //viewpager
        List<View> pagers = new ArrayList<>();
        View malinginformation = LayoutInflater.from(this).inflate(R.layout.view_maling_information, null, false);
        View creditcardinformation = LayoutInflater.from(this).inflate(R.layout.credit_card_information, null, false);
        etFirstName = malinginformation.findViewById(R.id.et_first_name);
        etLastName = malinginformation.findViewById(R.id.et_last_name);
        etAddress = malinginformation.findViewById(R.id.et_address);
        etAddress2 = malinginformation.findViewById(R.id.et_address2);
        etCity = malinginformation.findViewById(R.id.et_city);
        tvStateValue = malinginformation.findViewById(R.id.tv_state_value);
        ImageView ivDrop = malinginformation.findViewById(R.id.iv_drop);
        tvStateValue.setOnClickListener(view -> {
            showCityPickView();
        });
        ivDrop.setOnClickListener(view -> showCityPickView());


        tvCountry = malinginformation.findViewById(R.id.tv_country_value);
        ImageView ivconutryDrop = malinginformation.findViewById(R.id.iv_country_drop);


        tvCountry.setOnClickListener(view -> {
            showCityPickView();
        });
        ivconutryDrop.setOnClickListener(view -> showCityPickView());

        etZip = malinginformation.findViewById(R.id.et_zip);
        etMobileNumber = malinginformation.findViewById(R.id.et_mobile_number);


        cbSame = creditcardinformation.findViewById(R.id.cb_same);
        creditAddress = creditcardinformation.findViewById(R.id.et_address);
        creditAddress2 = creditcardinformation.findViewById(R.id.et_address2);
        creditCity = creditcardinformation.findViewById(R.id.et_city);
        creditStateValue = creditcardinformation.findViewById(R.id.tv_state_value);
        creditCountryValue = creditcardinformation.findViewById(R.id.tv_country_value);
        creditContry = creditcardinformation.findViewById(R.id.et_country);
        creditMobileNumber = creditcardinformation.findViewById(R.id.et_mobile_number);
        creditMonthValue = creditcardinformation.findViewById(R.id.tv_month_value);
        creditYearValue = creditcardinformation.findViewById(R.id.tv_year_value);


        pagers.add(malinginformation);
        pagers.add(creditcardinformation);
        mSceneViewPagerAdapter = new SceneViewPagerAdapter(pagers);
        viewPager.setAdapter(mSceneViewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        //将tablayout和Viewpager绑定
        tabTitle.setupWithViewPager(viewPager);
        //tablayout设置标题
        String[] titles = new String[]{getString(R.string.m183_mailing_information),
                getString(R.string.m184_credti_card_information)};
        tabTitle.removeAllTabs();
        for (String title : titles) {
            TabLayout.Tab tab = tabTitle.newTab();
            tab.setText(title);
            tabTitle.addTab(tab);
        }


    }

/*
    @OnClick({R.id.tv_state_value, R.id.iv_drop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_state_value:
            case R.id.iv_drop:
                showCityPickView();
                break;
        }
    }*/


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    /**
     * 选择省份和城市
     */
    public void showCityPickView() {
        List<String> country = CountryDataUtils.getCountry();
        List<List<String>> state = CountryDataUtils.getState();
        List<ProvinceCityBean> provinceList = new ArrayList<>();

        for (int i = 0; i < country.size(); i++) {
            ProvinceCityBean bean = new ProvinceCityBean();
            bean.setName(country.get(i));
            List<ProvinceCityBean.CityBean> cityList = new ArrayList<>();
            List<String> citys = state.get(i);
            for (int j = 0; j < citys.size(); j++) {
                ProvinceCityBean.CityBean cityBean = new ProvinceCityBean.CityBean();
                cityBean.setName(citys.get(j));
                cityList.add(cityBean);
            }

            provinceList.add(bean);
        }


        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {

            //返回的分别是三个级别的选中位置
            if (provinceList != null && state != null && provinceList.size() > 0 && state.size() > 0) {
                String tx1 = provinceList.get(options1).getPickerViewText();
                String tx2 = state.get(options1).get(options2);
                tvCountry.setText(tx1);
                tvStateValue.setText(tx2);
            }


        })

                .setCancelText(getString(R.string.m127_cancel))
                .setSubmitText(getString(R.string.m152_ok))
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(provinceList, state);
        pvOptions.show();
    }


    @OnClick({R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                int currentItem = viewPager.getCurrentItem();
                if (currentItem == 0) {
                    String firstName = etFirstName.getText().toString();
                    String lastName = etLastName.getText().toString();
                    String address = etAddress.getText().toString();
                    String addressDetail = etAddress2.getText().toString();
                    String country = tvCountry.getText().toString();
                    String state = tvStateValue.getText().toString();
                    String city = etCity.getText().toString();
                    String zipCode = etZip.getText().toString();
                    String mobileNum = etMobileNumber.getText().toString();
                    if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(address) || TextUtils.isEmpty(addressDetail) ||
                            TextUtils.isEmpty(country) || TextUtils.isEmpty(state) || TextUtils.isEmpty(city) || TextUtils.isEmpty(zipCode) || TextUtils.isEmpty(mobileNum)) {
                        MyToastUtils.toast(R.string.m145_content_cannot_empty);
                        return;
                    }
                    presenter.modifyUserInfo(firstName,lastName,address,addressDetail,country,state,city,zipCode,mobileNum);
                } else {

                }
                break;
        }
    }


}
