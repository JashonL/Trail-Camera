package com.shuoxd.camera.module.account;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.CameraFiterAdapter;
import com.shuoxd.camera.adapter.PopAdapter;
import com.shuoxd.camera.adapter.SceneViewPagerAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.ProvinceCityBean;
import com.shuoxd.camera.module.login.User;
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
        BaseQuickAdapter.OnItemClickListener, ViewPager.OnPageChangeListener, CompoundButton.OnCheckedChangeListener {
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
    private EditText creditZip;
    private EditText creditMobileNumber;
    private TextView creditMonthValue;
    private TextView creditYearValue;
    private View vExpires;

    private TextView tvMonth;
    private TextView tvYear;



    private List<String> months = new ArrayList<>();
    private List<String> years = new ArrayList<>();


    private User userBean;

    private String firstName;
    private String lastName;

    private String address;
    private String addressDetail;
    private String country;
    private String state;
    private String city;
    private String zipCode;
    private String mobileNum;



    private View tvSameas;
    private String cardNum;
    private String cardYear;
    private String cardMonth;
    private View monthDrop;
    private View yearDrop;

    private View v_monthDrop;
    private View v_yearDrop;

    private String cardAddr;

    private String cardName;
    private String cardCity;
    private String cardCountry;
    private String cardState;
    private String cardZip;





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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //头部toolBar
        initToobar(toolbar);
        tvTitle.setText(R.string.m82_account);

    }


    @Override
    protected void initImmersionBar() {
        ImmersionBar.with(this)
        .statusBarDarkFont(true, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.color_app_main)//这里的颜色，你可以自定义。
                .init();
    }

    @Override
    protected void initData() {
        userBean = App.getUserBean();

        firstName=userBean.getFirstName();
        lastName=userBean.getLastName();
        address = userBean.getAddress();
        addressDetail = userBean.getAddressDetail();
        country = userBean.getCountry();
        state = userBean.getState();
        city = userBean.getCity();
        zipCode = userBean.getZipCode();
        mobileNum = userBean.getMobileNum();

        cardNum = userBean.getCardNum();
        cardYear = userBean.getCardYear();
        cardMonth = userBean.getCardMonth();
        cardAddr = userBean.getCardAddr();


        cardName = userBean.getCardName();
        cardCity = userBean.getCardCity();
        cardCountry = userBean.getCardCountry();
        cardState = userBean.getCardState();
        cardZip = userBean.getCardZip();







        for (int i = 0; i < 12; i++) {
            int month = i + 1;

            if (month<10){
                months.add("0"+ month);

            }else {
                months.add(month+ "");
            }
        }

        for (int i = 2022; i < 2050; i++) {
            years.add(i + "");
        }


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
//        cbSame.setChecked(true);
        tvSameas = creditcardinformation.findViewById(R.id.tv_sameas);
        tvSameas.setOnClickListener(view -> {
            cbSame.setChecked(!cbSame.isChecked());
        });
        creditAddress = creditcardinformation.findViewById(R.id.et_address);
        creditAddress2 = creditcardinformation.findViewById(R.id.et_address2);
        creditCity = creditcardinformation.findViewById(R.id.et_city);
        creditStateValue = creditcardinformation.findViewById(R.id.tv_state_value);
        creditCountryValue = creditcardinformation.findViewById(R.id.tv_country_value);
        creditZip = creditcardinformation.findViewById(R.id.et_zip);
        creditMobileNumber = creditcardinformation.findViewById(R.id.et_mobile_number);
        creditMonthValue = creditcardinformation.findViewById(R.id.tv_month_value);
        creditYearValue = creditcardinformation.findViewById(R.id.tv_year_value);
        vExpires=creditcardinformation.findViewById(R.id.v_expires);
        tvMonth=creditcardinformation.findViewById(R.id.tv_month);
        tvYear=creditcardinformation.findViewById(R.id.tv_year);
        ImageView creditIvconutryDrop = creditcardinformation.findViewById(R.id.iv_country_drop);


        if (!TextUtils.isEmpty(cardAddr)){
            creditAddress2.setText(cardAddr);
        }


        if (!TextUtils.isEmpty(cardNum)){
            creditMobileNumber.setText(cardNum);
        }


        if (!TextUtils.isEmpty(cardMonth)){
            int value = Integer.parseInt(cardMonth);
            String v;
            if (value<10){
                v="0"+value;
            }else {
                v= String.valueOf(value);
            }

            creditMonthValue.setText(v);
        }

        if (!TextUtils.isEmpty(cardYear)){
            creditYearValue.setText(cardYear);
        }


        if (!TextUtils.isEmpty(address)) {
            etAddress.setText(address);
        }

        if (!TextUtils.isEmpty(firstName)){
            etFirstName.setText(firstName);
        }

        if (!TextUtils.isEmpty(lastName)){
            etLastName.setText(lastName);
        }


        if (!TextUtils.isEmpty(addressDetail)) {
            etAddress2.setText(addressDetail);
        }

        if (!TextUtils.isEmpty(country)) {
            tvCountry.setText(country);
        }
        if (!TextUtils.isEmpty(state)) {
            tvStateValue.setText(state);
        }
        if (!TextUtils.isEmpty(city)) {
            etCity.setText(city);
        }

        if (!TextUtils.isEmpty(zipCode)) {
            etZip.setText(zipCode);
        }

        if (!TextUtils.isEmpty(mobileNum)) {
            etMobileNumber.setText(mobileNum);
        }



/////////////////////


        if (!TextUtils.isEmpty(cardName)) {
            creditAddress.setText(cardName);
        }
        if (!TextUtils.isEmpty(cardCity)) {
            creditCity.setText(cardCity);
        }
        if (!TextUtils.isEmpty(cardCountry)) {
            creditCountryValue.setText(cardCountry);
        }

        if (!TextUtils.isEmpty(cardState)) {
            creditStateValue.setText(cardState);
        }

        if (!TextUtils.isEmpty(cardZip)) {
            creditZip.setText(cardZip);
        }





        ImageView creditIvDrop = creditcardinformation.findViewById(R.id.iv_drop);
        creditIvDrop.setOnClickListener(view -> {
            showCityPickView();
        });
        creditStateValue.setOnClickListener(view -> {
            showCityPickView();
        });

        creditIvconutryDrop.setOnClickListener(view -> {
            showCityPickView();
        });
        creditCountryValue.setOnClickListener(view -> {
            showCityPickView();
        });


        monthDrop = creditcardinformation.findViewById(R.id.iv_month_drop);
        yearDrop = creditcardinformation.findViewById(R.id.iv_year_drop);

        v_monthDrop=creditcardinformation.findViewById(R.id.v_monthdrop);
        v_yearDrop=creditcardinformation.findViewById(R.id.v_yeardrop);


        monthDrop.setOnClickListener(view -> {
            showSelect(v_monthDrop, months);
        });
        creditMonthValue.setOnClickListener(view -> {
            showSelect(v_monthDrop, months);
        });



        yearDrop.setOnClickListener(view -> {
            showSelect(v_yearDrop, years);
        });
        creditYearValue.setOnClickListener(view -> {
            showSelect(v_yearDrop, years);
        });


        cbSame.setOnCheckedChangeListener(this);


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

//        presenter.getUserInfo();

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

        if (position == 1) {
            boolean checked = cbSame.isChecked();
            if (checked) {

                String address = etAddress.getText().toString();
                String address_detail = etAddress2.getText().toString();
                String city = etCity.getText().toString();
                String value = tvStateValue.getText().toString();
                String country = tvCountry.getText().toString();
                String zip = etZip.getText().toString();

                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();


                if (!TextUtils.isEmpty(lastName)&&!TextUtils.isEmpty(firstName)) {
                    String name=lastName+" "+firstName;
                    creditAddress.setText(name);
                }
                if (!TextUtils.isEmpty(address)) {
                    creditAddress2.setText(address);
                }
                if (!TextUtils.isEmpty(city)) {
                    creditCity.setText(city);
                }

                if (!TextUtils.isEmpty(value)) {
                    creditStateValue.setText(city);
                }

                if (!TextUtils.isEmpty(country)) {
                    creditCountryValue.setText(country);
                }


                if (!TextUtils.isEmpty(zip)) {
                    creditZip.setText(zip);
                }


            }

        }

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

                int currentItem = viewPager.getCurrentItem();
                if (currentItem == 0) {
                    tvCountry.setText(tx1);
                    tvStateValue.setText(tx2);
                } else {
                    creditCountryValue.setText(tx1);
                    creditStateValue.setText(tx2);
                }

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
                    presenter.modifyUserInfo(firstName, lastName, address, addressDetail, country, state, city, zipCode, mobileNum);
                } else {


                    String carName = creditAddress.getText().toString();
                    String cardAddr = creditAddress2.getText().toString();
                    String cardCity = creditCity.getText().toString();
                    String cardCountry = creditCountryValue.getText().toString();
                    String cardState = creditStateValue.getText().toString();
                    String cardZip = creditZip.getText().toString();
                    String cardNumber = creditMobileNumber.getText().toString();
                    String cardYear = creditYearValue.getText().toString();
                    String cardMonth = creditMonthValue.getText().toString();
                    if (TextUtils.isEmpty(carName) || TextUtils.isEmpty(cardAddr) || TextUtils.isEmpty(cardCity) || TextUtils.isEmpty(cardCountry) ||
                            TextUtils.isEmpty(cardState) || TextUtils.isEmpty(cardZip) || TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(cardYear) || TextUtils.isEmpty(cardMonth)) {
                        MyToastUtils.toast(R.string.m145_content_cannot_empty);
                        return;
                    }
                    presenter.modifyCreditCard(carName, cardAddr, cardCity, cardCountry, cardState, cardZip, cardNumber, cardYear, cardMonth);

                }
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            String address = etAddress.getText().toString();
            String address_detail = etAddress2.getText().toString();
            String city = etCity.getText().toString();
            String value = tvStateValue.getText().toString();
            String country = tvCountry.getText().toString();
            String zip = etZip.getText().toString();

            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();


            if (!TextUtils.isEmpty(lastName)&&!TextUtils.isEmpty(firstName)) {
                String name=lastName+" "+firstName;
                creditAddress.setText(name);
            }
            if (!TextUtils.isEmpty(address)) {
                creditAddress2.setText(address);
            }
            if (!TextUtils.isEmpty(city)) {
                creditCity.setText(city);
            }

            if (!TextUtils.isEmpty(value)) {
                creditStateValue.setText(city);
            }

            if (!TextUtils.isEmpty(country)) {
                creditCountryValue.setText(country);
            }


            if (!TextUtils.isEmpty(zip)) {
                creditZip.setText(zip);
            }

        } else {
            creditAddress.setText("");
            creditAddress2.setText("");
            creditCity.setText("");
            creditStateValue.setText("");
            creditCountryValue.setText("");
            creditZip.setText("");
        }
    }


    private void showSelect(View dropView, List<String> list) {

        View contentView = LayoutInflater.from(this).inflate(
                R.layout.pop_layout, null);

        RecyclerView rvCamera = contentView.findViewById(R.id.ry_camera);
        rvCamera.setLayoutManager(new LinearLayoutManager(this));



        int height = getWindow().getDecorView().getHeight();
        int[] location = new int[2];
        dropView.getLocationInWindow(location);
        int y = location[1]; // view距离window 顶边的距离（即y轴方向）
        int width = dropView.getWidth();
        int hight = height - y - dropView.getHeight()-100;




        final PopupWindow popupWindow = new PopupWindow(contentView, width, hight, true);
        popupWindow.setTouchable(true);
        PopAdapter camerAdapter = new PopAdapter(R.layout.item_string, list);

        rvCamera.setAdapter(camerAdapter);
        camerAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (v_monthDrop == dropView) {
                creditMonthValue.setText(list.get(position));
            } else {
                creditYearValue.setText(list.get(position));
            }
            popupWindow.dismiss();

        });

        popupWindow.setTouchInterceptor((v, event) -> false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.setAnimationStyle(R.style.Popup_Anim);
        popupWindow.showAsDropDown(dropView);
    }


    @Override
    public void modifyUserInfoSuccess(String firstName, String lastName, String address, String addressDetail,
                                      String country, String state, String city, String zipCode, String mobileNum) {
        App.getUserBean().setAddress(address);
        App.getUserBean().setAddressDetail(addressDetail);
        App.getUserBean().setCountry(country);
        App.getUserBean().setState(state);
        App.getUserBean().setCity(city);
        App.getUserBean().setZipCode(zipCode);
        App.getUserBean().setMobileNum(mobileNum);

        App.getUserBean().setFirstName(firstName);
        App.getUserBean().setLastName(lastName);
    }

    @Override
    public void modifyCardSuccess(String cardName, String cardAddr, String cardCity,
                                  String cardCountry, String cardState, String cardZip, String cardNum,
                                  String cardYear, String cardMonth) {


        App.getUserBean().setCardName(cardName);
        App.getUserBean().setCardAddr(cardAddr);
        App.getUserBean().setCardCity(cardCity);
        App.getUserBean().setCardCountry(cardCountry);
        App.getUserBean().setCardState(cardState);
        App.getUserBean().setCardZip(cardZip);
        App.getUserBean().setCardNum(cardNum);
        App.getUserBean().setCardYear(cardYear);
        App.getUserBean().setCardNum(cardMonth);

    }

    @Override
    public void updataUser() {

        userBean = App.getUserBean();

        firstName=userBean.getFirstName();
        lastName=userBean.getLastName();
        address = userBean.getAddress();
        addressDetail = userBean.getAddressDetail();
        country = userBean.getCountry();
        state = userBean.getState();
        city = userBean.getCity();
        zipCode = userBean.getZipCode();
        mobileNum = userBean.getMobileNum();

        cardNum = userBean.getCardNum();
        cardYear = userBean.getCardYear();
        cardMonth = userBean.getCardMonth();
        cardAddr = userBean.getCardAddr();



        if (!TextUtils.isEmpty(cardAddr)){
            creditAddress2.setText(cardAddr);
        }


        if (!TextUtils.isEmpty(cardNum)){
            creditMobileNumber.setText(cardNum);
        }


        if (!TextUtils.isEmpty(cardMonth)){
            int value = Integer.parseInt(cardMonth);
            String v;
            if (value<10){
                v="0"+value;
            }else {
                v= String.valueOf(value);
            }
            creditMonthValue.setText(v);
        }




        if (!TextUtils.isEmpty(cardYear)){
            creditYearValue.setText(cardYear);
        }


        if (!TextUtils.isEmpty(address)) {
            etAddress.setText(address);
        }

        if (!TextUtils.isEmpty(firstName)){
            etFirstName.setText(firstName);
        }

        if (!TextUtils.isEmpty(lastName)){
            etLastName.setText(lastName);
        }


        if (!TextUtils.isEmpty(addressDetail)) {
            etAddress2.setText(addressDetail);
        }

        if (!TextUtils.isEmpty(country)) {
            tvCountry.setText(country);
        }
        if (!TextUtils.isEmpty(state)) {
            tvStateValue.setText(state);
        }
        if (!TextUtils.isEmpty(city)) {
            etCity.setText(city);
        }

        if (!TextUtils.isEmpty(zipCode)) {
            etZip.setText(zipCode);
        }

        if (!TextUtils.isEmpty(mobileNum)) {
            etMobileNumber.setText(mobileNum);
        }

    }
}
