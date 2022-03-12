package com.shuoxd.camera.module.leftmenu;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.contrarywind.view.WheelView;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.components.ImmersionFragment;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.PhaseAdapter;
import com.shuoxd.camera.bean.PhaseBean;
import com.shuoxd.camera.customview.GridDivider;
import com.shuoxd.camera.utils.CommentUtils;
import com.shuoxd.camera.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeNavigationViewFragment extends ImmersionFragment implements
        BaseQuickAdapter.OnItemClickListener, CompoundButton.OnCheckedChangeListener {


    protected ImmersionBar mImmersionBar;
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_filter)
    TextView tvFilter;
    @BindView(R.id.tv_photo)
    TextView tvPhoto;
    @BindView(R.id.cb_hd)
    CheckBox cbHd;
    @BindView(R.id.cb_video)
    CheckBox cbVideo;
    @BindView(R.id.ll_photo)
    LinearLayout llPhoto;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.v_date_background)
    View vDateBackground;
    @BindView(R.id.tv_date_start)
    TextView tvDateStart;
    @BindView(R.id.tv_date_center)
    TextView tvDateCenter;
    @BindView(R.id.tv_date_end)
    TextView tvDateEnd;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.cb_am)
    CheckBox cbAm;
    @BindView(R.id.cb_pm)
    CheckBox cbPm;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.tv_favorites)
    TextView tvFavorites;
    @BindView(R.id.cb_favorites)
    CheckBox cbFavorites;
    @BindView(R.id.tv_temp)
    TextView tvTemp;
    @BindView(R.id.cb_f)
    CheckBox cbF;
    @BindView(R.id.cb_c)
    CheckBox cbC;
    @BindView(R.id.wheel_start)
    WheelView wheelStart;
    @BindView(R.id.tv_to)
    TextView tvTo;
    @BindView(R.id.wheel_end)
    WheelView wheelEnd;
    @BindView(R.id.tv_lunar_phase)
    TextView tvLunarPhase;
    @BindView(R.id.rv_phase)
    RecyclerView rvPhase;
    @BindView(R.id.tv_reset)
    TextView tvReset;
    @BindView(R.id.ll_reset)
    LinearLayout llReset;
    @BindView(R.id.tv_apply)
    TextView tvApply;
    @BindView(R.id.ll_apply)
    LinearLayout llApply;
    @BindView(R.id.gp_temp)
    Group gpTemp;


    /*设备部分*/
    private PhaseAdapter mAdapter;


    private IMenuListeners listeners;


    private String startDate = "-1";
    private String endDate = "-1";
    private String amPm = "-1";
    private String photoType = "-1";
    private String favorites = "-1";
    private String moonPhase = "-1";
    private String startTemperature = "0";
    private String endTemperature = "0";
    private String temperatureUnit = "-1";


    public HomeNavigationViewFragment(IMenuListeners listeners) {
        this.listeners = listeners;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leftmenu_layout, null);
        ButterKnife.bind(this, view);

        List<String> tempF = CommentUtils.tempF();
        List<String> tempC = CommentUtils.tempC();
        //初始化时间选择器
        wheelStart.setCyclic(true);
        wheelStart.isCenterLabel(true);
        wheelStart.setAdapter(new ArrayWheelAdapter<>(tempF));
        wheelStart.setCurrentItem(50);
        wheelStart.setTextColorCenter(ContextCompat.getColor(getContext(), R.color.color_text_00));
        wheelStart.setItemsVisibleCount(3);

        wheelEnd.setCyclic(true);
        wheelEnd.isCenterLabel(true);
        wheelEnd.setAdapter(new ArrayWheelAdapter<>(tempC));
        wheelEnd.setCurrentItem(50);
        wheelEnd.setTextColorCenter(ContextCompat.getColor(getContext(), R.color.color_text_00));
        wheelEnd.setItemsVisibleCount(3);


        //初始化月相
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        rvPhase.setLayoutManager(layoutManager);
        mAdapter = new PhaseAdapter(R.layout.item_phase, new ArrayList<>());
        rvPhase.setAdapter(mAdapter);
        rvPhase.addItemDecoration(new GridDivider(ContextCompat.getColor(getActivity(), R.color.nocolor), 10, 10));

        mAdapter.setOnItemClickListener(this);

        int[] res = {R.drawable.phase1, R.drawable.phase2, R.drawable.phase3, R.drawable.phase4,
                R.drawable.phase5, R.drawable.phase6, R.drawable.phase7, R.drawable.phase8,};

        int[] res_selected = {R.drawable.phase1_selected, R.drawable.phase2_selected, R.drawable.phase3_selected, R.drawable.phase4_selected,
                R.drawable.phase5_selected, R.drawable.phase6_selected, R.drawable.phase7_selected, R.drawable.phase8_selected};
        List<PhaseBean> phaseBeans = new ArrayList<>();
        for (int i = 0; i < res.length; i++) {
            PhaseBean bean = new PhaseBean();
            bean.setIndex(i);
            bean.setSelected(false);
            bean.setResNormal(res[i]);
            bean.setResSelected(res_selected[i]);
            phaseBeans.add(bean);
        }
        mAdapter.replaceData(phaseBeans);


        cbHd.setOnCheckedChangeListener(this);
        cbVideo.setOnCheckedChangeListener(this);
        cbAm.setOnCheckedChangeListener(this);
        cbPm.setOnCheckedChangeListener(this);
        cbFavorites.setOnCheckedChangeListener(this);
        cbC.setOnCheckedChangeListener(this);
        cbF.setOnCheckedChangeListener(this);

        gpTemp.setVisibility(View.GONE);


        return view;
    }


    @Override
    public void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(false, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .statusBarView(statusBarView)
                .statusBarColor(R.color.color_app_main)//这里的颜色，你可以自定义。
                .init();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mAdapter.setNowSelectPosition(position);
    }

    @OnClick({R.id.tv_date_start, R.id.tv_date_end, R.id.ll_reset, R.id.ll_apply, R.id.iv_delete, R.id.iv_delete_unit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_date_start:
                try {
                    DateUtils.showTimepickViews(getContext(), null, new DateUtils.ImplSelectTimeListener() {
                        @Override
                        public void seletedListener(String date) {
                            tvDateStart.setText(date);
                        }

                        @Override
                        public void result(int year, int month, int day) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.tv_date_end:
                try {
                    DateUtils.showTimepickViews(getContext(), null, new DateUtils.ImplSelectTimeListener() {
                        @Override
                        public void seletedListener(String date) {
                            tvDateEnd.setText(date);
                        }

                        @Override
                        public void result(int year, int month, int day) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.iv_delete:
                tvDateStart.setText("");
                tvDateEnd.setText("");
                break;

            case R.id.iv_delete_unit:
                cbF.setChecked(false);
                cbC.setChecked(false);
                temperatureUnit = "-1";

                wheelEnd.setCurrentItem(50);
                wheelStart.setCurrentItem(50);

                gpTemp.setVisibility(View.GONE);
                break;

            case R.id.ll_reset:
                reset();
//                listeners.reset();
                break;

            case R.id.ll_apply:

                if (cbVideo.isChecked()) {
                    photoType = "2";
                } else if (cbHd.isChecked()) {
                    photoType = "1";
                } else {
                    photoType = "-1";
                }


                String s = tvDateStart.getText().toString();
                String s1 = tvDateEnd.getText().toString();
                if (TextUtils.isEmpty(s) || TextUtils.isEmpty(s1)) {
                    startDate = "-1";
                    endDate = "-1";
                } else {
                    startDate = s;
                    endDate = s1;
                }


                if (cbAm.isChecked()) {
                    amPm = "0";
                } else if (cbPm.isChecked()) {
                    amPm = "1";
                } else {
                    amPm = "-1";
                }

                favorites = cbFavorites.isChecked() ? "1" : "-1";


                if (cbC.isChecked()) {
                    temperatureUnit = "0";
                }

                if (cbF.isChecked()) {
                    temperatureUnit = "1";
                }

                if (!cbC.isChecked() && !cbF.isChecked()) {
                    temperatureUnit = "-1";
                }

                int temp_start = wheelStart.getCurrentItem();
                int temp_end = wheelEnd.getCurrentItem();

                List<String> tempF = CommentUtils.tempF();
                List<String> tempC = CommentUtils.tempC();

                startTemperature = "" + tempF.get(temp_start);
                endTemperature = "" + tempC.get(temp_end);


                int nowSelectPosition = mAdapter.getNowSelectPosition();
                if (nowSelectPosition == -1) {
                    moonPhase = String.valueOf(-1);
                } else {
                    moonPhase = String.valueOf(nowSelectPosition + 1);
                }


                listeners.apply(
                        startDate, endDate,
                        amPm, photoType, favorites,
                        moonPhase, startTemperature,
                        endTemperature, temperatureUnit);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == cbHd) {
            if (b) cbVideo.setChecked(false);
        } else if (compoundButton == cbVideo) {
            if (b) cbHd.setChecked(false);
        } else if (compoundButton == cbAm) {
            if (b) cbPm.setChecked(false);
        } else if (compoundButton == cbPm) {
            if (b) cbAm.setChecked(false);
        } else if (compoundButton == cbF) {
            if (b) cbC.setChecked(false);
            if (compoundButton.isPressed() && b) {
                gpTemp.setVisibility(View.VISIBLE);
            } else if (!b && !cbC.isChecked()) {
                temperatureUnit = "-1";

                wheelEnd.setCurrentItem(50);
                wheelStart.setCurrentItem(50);

                gpTemp.setVisibility(View.GONE);
            }

        } else if (compoundButton == cbC) {
            if (b) cbF.setChecked(false);
            if (compoundButton.isPressed() && b) {
                gpTemp.setVisibility(View.VISIBLE);
            } else if (!b && !cbF.isChecked()) {
                temperatureUnit = "-1";

                wheelEnd.setCurrentItem(50);
                wheelStart.setCurrentItem(50);

                gpTemp.setVisibility(View.GONE);
            }
        }
    }


    private void reset() {
        startDate = "-1";
        endDate = "-1";
        amPm = "-1";
        photoType = "-1";
        favorites = "-1";
        moonPhase = "-1";
        startTemperature = "0";
        endTemperature = "0";
        temperatureUnit = "-1";

        cbHd.setChecked(false);
        cbVideo.setChecked(false);

        tvDateStart.setText("");
        tvDateEnd.setText("");

        cbAm.setChecked(false);
        cbPm.setChecked(false);

        cbF.setChecked(false);
        cbC.setChecked(false);

        gpTemp.setVisibility(View.GONE);


        cbFavorites.setChecked(false);

        wheelEnd.setCurrentItem(50);
        wheelStart.setCurrentItem(50);

        List<PhaseBean> data = mAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setSelected(false);
        }

        mAdapter.notifyDataSetChanged();
    }


    public interface IMenuListeners {

        void reset();

        void apply(
                String startDate, String endDate,
                String amPm, String photoType,
                String favorites, String moonPhase,
                String startTemperature, String endTemperature,
                String temperatureUnit);

    }


}
