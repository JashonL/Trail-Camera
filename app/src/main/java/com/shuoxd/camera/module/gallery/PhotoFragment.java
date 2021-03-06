package com.shuoxd.camera.module.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.CameraMulFiterAdapter;
import com.shuoxd.camera.adapter.CameraPicVedeoAdapter;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.constants.SharePreferenConstants;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.GridDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;
import com.shuoxd.camera.eventbus.FreshPhoto;
import com.shuoxd.camera.module.camera.CameraDetailActivity;
import com.shuoxd.camera.module.camera.CameraShowListManerge;
import com.shuoxd.camera.module.leftmenu.HomeNavigationViewFragment;
import com.shuoxd.camera.module.video.VideoPlayActivity;
import com.shuoxd.camera.utils.MyToastUtils;
import com.shuoxd.camera.utils.SharedPreferencesUnit;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PhotoFragment extends BaseFragment<PhotoPresenter> implements PhotoView,
        BaseQuickAdapter.OnItemClickListener, Toolbar.OnMenuItemClickListener,
        HomeNavigationViewFragment.IMenuListeners,
        CameraPicVedeoAdapter.SelectedListener, BaseQuickAdapter.OnItemLongClickListener {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.iv_style)
    ImageView ivStyle;
    @BindView(R.id.tv_pic_num)
    TextView tvPicNum;
    @BindView(R.id.iv_switch)
    ImageView ivSwitch;
    @BindView(R.id.rlv_device)
    RecyclerView rlvDevice;
    @BindView(R.id.srl_pull)
    MySwipeRefreshLayout srlPull;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.v_pop)
    View vPop;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;


    /*????????????*/
//    private CameraPicAdapter mAdapter;
    private CameraPicVedeoAdapter mPicVideoAdapter;

    private List<PictureBean> picList = new ArrayList<>();


    private int spanCount = 1;


    @Override
    protected PhotoPresenter createPresenter() {
        return new PhotoPresenter(getContext(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.gallery_fragment;
    }

    @Override
    protected void initView() {
        srlPull.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.color_theme_green));

        //toolbar
        //??????toolBar
        toolbar.inflateMenu(R.menu.menu_camera);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m77_all_camera);

        ivStyle.setImageResource(R.drawable.list_pic_row);
        ivSwitch.setImageResource(R.drawable.list_style_menu);

        //??????????????????
        spanCount = SharedPreferencesUnit.getInstance(getContext()).getInt(SharePreferenConstants.SP_PHOTO_SHOW_STYLE);
        if (spanCount == 0) {
            spanCount = 1;
        }

        //?????????????????????
        if (spanCount == 1) {
            ivSwitch.setImageResource(R.drawable.camera_arrang);
        } else if (spanCount == 2) {
            ivSwitch.setImageResource(R.drawable.spancount);
        } else {
            ivSwitch.setImageResource(R.drawable.list_style_menu);
        }
        setAdapter(spanCount);


        ivSwitch.setOnClickListener(view12 -> {
            int itemDecorationCount = rlvDevice.getItemDecorationCount();
            for (int i = 0; i < itemDecorationCount; i++) {
                rlvDevice.removeItemDecorationAt(i);
            }
            if (spanCount == 1) {
                spanCount = 2;
                ivSwitch.setImageResource(R.drawable.camera_arrang);
            } else if (spanCount == 2) {
                spanCount = 3;
                ivSwitch.setImageResource(R.drawable.spancount);
            } else {
                spanCount = 1;
                ivSwitch.setImageResource(R.drawable.list_style_menu);
            }
            setAdapter(spanCount);


            //???????????????
            SharedPreferencesUnit.getInstance(getContext()).putInt(SharePreferenConstants.SP_PHOTO_SHOW_STYLE, spanCount);
        });


        ivStyle.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        /*---------------------------????????????????????????-----------------------------*/
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.navigationview, new HomeNavigationViewFragment(this)).commit();
    }


    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
//        cameraId = getArguments().getString("cameraId");


        //??????????????????
        presenter.setImeis("-1");
        presenter.setIsAllCamera("1");

        srlPull.setOnRefreshListener(() -> {
            try {
                presenter.setTotalPage(1);
                presenter.setPageNow(0);
                presenter.getCameraPic();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //????????????????????????
        try {
            presenter.setTotalPage(1);
            presenter.setPageNow(0);
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void refresh() {
        presenter.getAlldevice();
        presenter.getCameraPic();
    }


    //???????????????
    private void setAdapter(int span) {

        List<PictureBean> data = new ArrayList<>();
        if (mPicVideoAdapter != null) {
            data = mPicVideoAdapter.getData();
        }


        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), span);
        rlvDevice.setLayoutManager(layoutManager);
        mPicVideoAdapter = new CameraPicVedeoAdapter(data, this);
        rlvDevice.setAdapter(mPicVideoAdapter);
        rlvDevice.addItemDecoration(new GridDivider(ContextCompat.getColor(getActivity(), R.color.nocolor), 10, 10));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_empty_view, rlvDevice, false);
        mPicVideoAdapter.setEmptyView(view);
        mPicVideoAdapter.setHeaderAndEmpty(true);

        mPicVideoAdapter.setLoadMoreView(new CustomLoadMoreView());
        mPicVideoAdapter.disableLoadMoreIfNotFullPage(rlvDevice);
        mPicVideoAdapter.setOnLoadMoreListener(() -> {
            try {
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, rlvDevice);
        mPicVideoAdapter.setOnItemClickListener(this);
        mPicVideoAdapter.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<PictureBean> data = mPicVideoAdapter.getData();
        PictureBean pictureBean = data.get(position);
        boolean checked = pictureBean.isChecked();
        int itemType = pictureBean.getItemType();
        String id = pictureBean.getId();
        String fullPath = pictureBean.getFullPath();
        String collection = pictureBean.getCollection();

        if (itemType == CameraPicVedeoAdapter.HD_PIC_FLAG_EDIT || itemType == CameraPicVedeoAdapter.HD_PIC_FLAG_VIDEO_EDIT) {
            boolean b = !checked;
            pictureBean.setChecked(b);
            mPicVideoAdapter.toggle(id, b);
            mPicVideoAdapter.notifyDataSetChanged();


            List<String> selectedImeis = mPicVideoAdapter.getSelectedImeis();
            int size = selectedImeis.size();
            tv_selected_num.setText(size + "");

            if (selectedImeis.size() == mPicVideoAdapter.getData().size()) {
                setAllSelected();
            } else {
                setNotAllSelected();
            }

        } else {
          /*  if (itemType==CameraPicVedeoAdapter.HD_PIC_FLAG_VIDEO){
                Intent intent = new Intent(getContext(), VideoPlayActivity.class);
                //???????????????????????????
                intent.putExtra("fullPath", fullPath);
                intent.putExtra("id",id);
                intent.putExtra("collection",collection);
                intent.putExtra("alias", getString(R.string.m77_all_camera));
                startActivity(intent);
            }else {
                CameraShowListManerge.getInstance().setPicList(picList);
                Intent intent = new Intent(getContext(), CameraDetailActivity.class);
                //???????????????????????????
                intent.putExtra("position", position);
                intent.putExtra("alias", getString(R.string.m77_all_camera));
                startActivity(intent);
            }*/



            CameraShowListManerge.getInstance().setPicList(picList);
            Intent intent = new Intent(getContext(), CameraDetailActivity.class);
            //???????????????????????????
            intent.putExtra("position", position);
            intent.putExtra("alias", getString(R.string.m77_all_camera));
            startActivity(intent);

        }


    }

    @Override
    public void initImmersionBar() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.right_action) {
            // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
            View contentView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.pop_layout, null);

            List<CameraBean> cameraList = presenter.getCameraList();
            RecyclerView rvCamera = contentView.findViewById(R.id.ry_camera);
            rvCamera.setLayoutManager(new LinearLayoutManager(getContext()));
            CameraMulFiterAdapter camerAdapter = new CameraMulFiterAdapter(R.layout.item_camera_menu, cameraList);
            rvCamera.setAdapter(camerAdapter);
            camerAdapter.setOnItemClickListener((adapter, view, position) -> {
                camerAdapter.setNowSelectPosition(position);
                StringBuilder imeis = new StringBuilder();
                List<CameraBean> data = camerAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    CameraBean cameraBean = data.get(i);
                    boolean selected = cameraBean.isSelected();
                    if (selected) {
                        if (i == 0) {
                            imeis = new StringBuilder("-1");
                            break;
                        } else {
                            String imei = cameraBean.getCamera().getImei();
                            imeis.append(imei).append("_");
                        }
                    }
                }
                if (imeis.toString().endsWith("_")) {
                    imeis = new StringBuilder(imeis.substring(0, imeis.length() - 1));
                    presenter.setImeis(imeis.toString());
                    presenter.setIsAllCamera("-1");
                } else {
                    presenter.setImeis("-1");
                    presenter.setIsAllCamera("1");
                }
                presenter.setTotalPage(1);
                presenter.setPageNow(0);
                presenter.getCameraPic();
            });


            int width = getResources().getDimensionPixelSize(R.dimen.dp_225);
            int hight = getResources().getDimensionPixelSize(R.dimen.dp_248);
            int itemHight = getResources().getDimensionPixelOffset(R.dimen.dp_40);


            if (itemHight * cameraList.size() > hight) {
                hight = getResources().getDimensionPixelSize(R.dimen.dp_248);
            } else {
                hight = LinearLayout.LayoutParams.WRAP_CONTENT;
            }


            final PopupWindow popupWindow = new PopupWindow(contentView, width, hight, true);
            popupWindow.setTouchable(true);


            popupWindow.setTouchInterceptor((v, event) -> false);
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getActivity().getWindow().setAttributes(lp);
            popupWindow.setOnDismissListener(() -> {
                WindowManager.LayoutParams lp1 = getActivity().getWindow().getAttributes();
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp1);
            });
            popupWindow.setBackgroundDrawable(new ColorDrawable(0));
            popupWindow.setAnimationStyle(R.style.Popup_Anim);
            popupWindow.showAsDropDown(vPop);
        }

        return true;
    }


    @Override
    public void showResultError(String msg) {
        super.showResultError(msg);
        srlPull.setRefreshing(false);
    }


    @Override
    public void showCameraPic(List<PictureBean> beans) {
        srlPull.setRefreshing(false);
        int pageNow = presenter.getPageNow();
        if (pageNow == 1) {
            picList.clear();
            List<PictureBean> adapterList = new ArrayList<>(beans);
            mPicVideoAdapter.setNewData(adapterList);
        } else {
            mPicVideoAdapter.addData(beans);
            mPicVideoAdapter.loadMoreComplete();
        }

        int size = mPicVideoAdapter.getData().size();
        int size1 = mPicVideoAdapter.getSelectedImeis().size();

        if (size != size1) {
            setNotAllSelected();
        }

        picList.addAll(beans);

    }


    @Override
    public void showServerError(String msg) {
        super.showServerError(msg);
        srlPull.setRefreshing(false);
    }

    @Override
    public void onErrorCode(BaseBean bean) {
        super.onErrorCode(bean);

        srlPull.setRefreshing(false);
    }


    @Override
    public void showNoMoreData() {
        //??????????????????
        mPicVideoAdapter.loadMoreEnd();
    }

    @Override
    public void showMoreFail() {
        //??????????????????
        mPicVideoAdapter.loadMoreFail();
    }

    @Override
    public void showTotalNum(int totalNum) {
        String s = totalNum + " " + getString(R.string.m76_photos);
        tvPicNum.setText(s);
    }

    @Override
    public void showCollecMsg() {
        //?????????????????????pop????????????
        cloaseEdit(mPicVideoAdapter.getData());
        try {
            presenter.setTotalPage(1);
            presenter.setPageNow(0);
            presenter.getCameraPic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        //?????????????????????pop????????????
        cloaseEdit(mPicVideoAdapter.getData());
        try {
            presenter.setTotalPage(1);
            presenter.setPageNow(0);
            presenter.getCameraPic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dowload() {
        //?????????????????????pop????????????
        cloaseEdit(mPicVideoAdapter.getData());
        try {
            presenter.setTotalPage(1);
            presenter.setPageNow(0);
            presenter.getCameraPic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reset() {
        presenter.setTotalPage(1);
        presenter.setPageNow(0);
        presenter.defautParams();
        presenter.getCameraPic();
        drawerLayout.closeDrawers();
    }

    @Override
    public void apply(String startDate, String endDate, String amPm,
                      String photoType, String favorites, String moonPhase,
                      String startTemperature, String endTemperature, String temperatureUnit) {

        presenter.setStartDate(startDate);
        presenter.setEndDate(endDate);
        presenter.setAmPm(amPm);
        presenter.setPhotoType(photoType);
        presenter.setFavorites(favorites);
        presenter.setMoonPhase(moonPhase);
        presenter.setStartTemperature(startTemperature);
        presenter.setEndTemperature(endTemperature);
        presenter.setTemperatureUnit(temperatureUnit);

        presenter.setTotalPage(1);
        presenter.setPageNow(0);
        presenter.getCameraPic();
        drawerLayout.closeDrawers();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdata(FreshPhoto bean) {
        //??????????????????
        try {
            presenter.setTotalPage(1);
            presenter.setPageNow(0);
            presenter.getCameraPic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.iv_edit)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_edit:
                //??????????????????
                List<PictureBean> data = mPicVideoAdapter.getData();
                if (presenter.isEditMode()) {
                    cloaseEdit(data);

                } else {

                    showEditPop();


                    presenter.setEditMode(true);
                    for (int i = 0; i < data.size(); i++) {
                        PictureBean pictureBean = data.get(i);
                        String type = pictureBean.getType();
                        //????????????????????????
                        pictureBean.setChecked(false);
                        if ("2".equals(type)) {
                            pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG_VIDEO_EDIT);
                        } else {
                            pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG_EDIT);
                        }
                    }
                }

                mPicVideoAdapter.notifyDataSetChanged();

                break;
        }
    }

    public void cloaseEdit(List<PictureBean> data) {
        presenter.setEditMode(false);
        for (int i = 0; i < data.size(); i++) {
            PictureBean pictureBean = data.get(i);
            String type = pictureBean.getType();
            //????????????????????????
            pictureBean.setChecked(false);
            //????????????
            mPicVideoAdapter.getSelectedImeis().clear();

            if ("2".equals(type)) {
                pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG_VIDEO);
            } else {
                pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG);
            }
        }
        if (editPop != null) {
            editPop.dismiss();
        }
        srlPull.setEnabled(true);
    }


    private PopupWindow editPop;

    private int popOffset=200;

    private TextView tv_selected_num;
    private TextView tv_select_all;
    private TextView tv_download;
    private TextView tv_collection;
    private TextView tv_selected_delete;
    private ImageView iv_camera;


    private void showEditPop() {
        srlPull.setEnabled(false);

        View myView = LayoutInflater.from(getActivity()).inflate(
                R.layout.pop_edit, null);

        iv_camera = myView.findViewById(R.id.iv_camera);
        tv_selected_num = myView.findViewById(R.id.tv_selected_num);
        tv_select_all = myView.findViewById(R.id.tv_select_all);
        tv_download = myView.findViewById(R.id.tv_download);
        tv_collection = myView.findViewById(R.id.tv_collection);
        tv_selected_delete = myView.findViewById(R.id.tv_selected_delete);


        tv_download.setOnClickListener(view -> {
            //????????????
            List<String> selectedImeis = mPicVideoAdapter.getSelectedImeis();
            if (selectedImeis.size()==0){
                MyToastUtils.toast(R.string.m215_not_choose);
                return;
            }


            StringBuilder ids = new StringBuilder();
            for (int i = 0; i < selectedImeis.size(); i++) {
                String id = selectedImeis.get(i);
                ids.append(id).append("_");
            }
            if (ids.toString().endsWith("_")) {
                ids = new StringBuilder(ids.substring(0, ids.length() - 1));
            }
            presenter.operation(ids.toString(),"resolution","1");
        });



        tv_collection.setOnClickListener(view -> {//????????????

            List<String> selectedImeis = mPicVideoAdapter.getSelectedImeis();
            if (selectedImeis.size()==0){
                MyToastUtils.toast(R.string.m215_not_choose);
                return;
            }

            StringBuilder ids = new StringBuilder();
            for (int i = 0; i < selectedImeis.size(); i++) {
                String id = selectedImeis.get(i);
                ids.append(id).append("_");
            }
            if (ids.toString().endsWith("_")) {
                ids = new StringBuilder(ids.substring(0, ids.length() - 1));
            }
            presenter.operation(ids.toString(),"favorites","1");
        });


        //????????????
        tv_selected_delete.setOnClickListener(view -> {
            List<String> selectedImeis = mPicVideoAdapter.getSelectedImeis();
            if (selectedImeis.size()==0){
                MyToastUtils.toast(R.string.m215_not_choose);
                return;
            }

            StringBuilder ids = new StringBuilder();
            for (int i = 0; i < selectedImeis.size(); i++) {
                String id = selectedImeis.get(i);
                ids.append(id).append("_");
            }
            if (ids.toString().endsWith("_")) {
                ids = new StringBuilder(ids.substring(0, ids.length() - 1));
            }
            presenter.operation(ids.toString(),"remove","1");
        });






        iv_camera.setOnClickListener(view -> {
            //????????????
            mPicVideoAdapter.getSelectedImeis().clear();

            List<PictureBean> data = mPicVideoAdapter.getData();
            presenter.setEditMode(false);
            for (int i = 0; i < data.size(); i++) {
                PictureBean pictureBean = data.get(i);
                String type = pictureBean.getType();
                //????????????????????????
                pictureBean.setChecked(false);
                //????????????
                mPicVideoAdapter.getSelectedImeis().clear();

                if ("2".equals(type)) {
                    pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG_VIDEO);
                } else {
                    pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG);
                }
            }

            mPicVideoAdapter.notifyDataSetChanged();
            if (editPop != null) {
                editPop.dismiss();
            }

            srlPull.setEnabled(true);

        });

        tv_select_all.setOnClickListener(view -> {
            if (presenter.isAllSelected()) {

                setNotAllSelected();

                List<PictureBean> data = mPicVideoAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    PictureBean pictureBean = data.get(i);
                    pictureBean.setChecked(false);
                    mPicVideoAdapter.toggle(pictureBean.getId(), false);
                }

                tv_selected_num.setText(0 + "");

                mPicVideoAdapter.notifyDataSetChanged();
            } else {

                setAllSelected();

                List<PictureBean> data = mPicVideoAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    PictureBean pictureBean = data.get(i);
                    pictureBean.setChecked(true);
                    mPicVideoAdapter.toggle(pictureBean.getId(), true);
                }

                tv_selected_num.setText(data.size() + "");

                mPicVideoAdapter.notifyDataSetChanged();
            }
        });


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int hight = LinearLayout.LayoutParams.WRAP_CONTENT;


        editPop = new PopupWindow(myView, width, hight, false);
        editPop.setTouchable(true);
        editPop.setFocusable(false); // ??????PopupWindow???????????????
        editPop.setTouchInterceptor((v, event) -> false);
        editPop.setBackgroundDrawable(new ColorDrawable(0));
        editPop.setAnimationStyle(R.style.Popup_Anim);
        editPop.setOutsideTouchable(false);
        editPop.showAtLocation(srlPull, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, popOffset);



        //????????????popupwindow??? ??????????????????????????????
        //???????????????????????????????????????ViewPager??????????????????popupwindow???????????????????????????????????????????????????MotionEvent.ACTION_MOVE:?????????????????????PopupWindow??????????????????????????????
        //********************?????????popupview????????????************************
        //********************????????????????????????popupwindow?????????????????????????????????************************
        myView.setOnTouchListener(new View.OnTouchListener() {
            int orgX, orgY;
            int offsetX,offsetY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        orgX = (int) event.getRawX();
                        orgY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetX = (int) event.getRawX() - orgX;
                        offsetY = (int) event.getRawY() - orgY;


                        Log.d("liaojinsha","down:"+orgY+"move:"+event.getRawY());
                        editPop.update(0, -offsetY+popOffset, -1, -1, true);
                        break;
                    case MotionEvent.ACTION_UP:

                        popOffset=-offsetY+popOffset;

                        break;
                }
                return true;
            }
        });

    }


    @Override
    public void selected() {
        List<String> selectedImeis = mPicVideoAdapter.getSelectedImeis();
        int size = selectedImeis.size();
        tv_selected_num.setText(size + "");
        if (selectedImeis.size() == mPicVideoAdapter.getData().size()) {
            setAllSelected();
        } else {
            setNotAllSelected();
        }
    }


    private void setAllSelected() {
        if (tv_select_all != null) {
            tv_select_all.setText(R.string.m214_deselect_all);
            tv_select_all.setTextColor(ContextCompat.getColor(getContext(), R.color.color_app_main));
            presenter.setAllSelected(true);
            setTextViewDrawableTop(R.drawable.allselected);
        }

    }


    private void setNotAllSelected() {
        if (tv_select_all != null) {
            tv_select_all.setText(R.string.m212_selected_all);
            tv_select_all.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            presenter.setAllSelected(false);
            setTextViewDrawableTop(R.drawable.selected_all);
        }

    }


    public void setTextViewDrawableTop(int drawId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(drawId, null);
        } else {
            drawable = getResources().getDrawable(drawId);
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_select_all.setCompoundDrawables(null, drawable, null, null);
        }
    }


    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {


        showEditPop();

        List<PictureBean> data = mPicVideoAdapter.getData();
        presenter.setEditMode(true);
        for (int i = 0; i < data.size(); i++) {
            PictureBean pictureBean = data.get(i);
            String type = pictureBean.getType();
            //????????????????????????
            pictureBean.setChecked(false);
            if ("2".equals(type)) {
                pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG_VIDEO_EDIT);
            } else {
                pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG_EDIT);
            }
        }

        mPicVideoAdapter.notifyDataSetChanged();
        return false;
    }





    public void hideEdit() {
        if (presenter!=null){
            presenter.setEditMode(false);
        }
        List<PictureBean> data = mPicVideoAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            PictureBean pictureBean = data.get(i);
            String type = pictureBean.getType();
            //????????????????????????
            pictureBean.setChecked(false);
            //????????????
            mPicVideoAdapter.getSelectedImeis().clear();

            if ("2".equals(type)) {
                pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG_VIDEO);
            } else {
                pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG);
            }
        }
        if (editPop != null) {
            editPop.dismiss();
        }
        srlPull.setEnabled(true);
        mPicVideoAdapter.notifyDataSetChanged();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);


        if (hidden){
            hideEdit();
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
