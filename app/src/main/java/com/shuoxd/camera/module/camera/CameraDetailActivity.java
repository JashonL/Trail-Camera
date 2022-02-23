package com.shuoxd.camera.module.camera;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.eventbus.FreshPhoto;
import com.shuoxd.camera.module.pictrue.BigImageActivty;
import com.shuoxd.camera.utils.GlideUtils;
import com.shuoxd.camera.utils.MyToastUtils;
import com.shuoxd.camera.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraDetailActivity extends BaseActivity<CameraDetailPresenter> implements CameraDetailView, ViewPager.OnPageChangeListener {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_temp)
    TextView tvTemp;
    @BindView(R.id.tv_collec)
    TextView tvCollec;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.cl_menu)
    ConstraintLayout clMenu;
    @BindView(R.id.btn_download)
    Button btnDownLoad;


    private ViewPagerAdapter mAdapter;

    private int currenPosition = 0;


    //数据源
    private List<PictureBean> picList;


    @Override
    protected CameraDetailPresenter createPresenter() {
        return new CameraDetailPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.acivity_camera_detail;
    }

    @Override
    protected void initViews() {
        //初始化toolbar
        initToobar(toolbar);
        tvTitle.setText(R.string.m65_camera_of_garden);
        //初始化Viewpager
        List<PictureBean> picList1 = CameraShowListManerge.getInstance().getPicList();
        picList = new ArrayList<>(picList1);

        mAdapter = new ViewPagerAdapter(picList);
        vp.setAdapter(mAdapter);
        vp.addOnPageChangeListener(this);
    }

    @Override
    protected void initData() {
        String alias = getIntent().getStringExtra("alias");
        tvTitle.setText(alias);

        currenPosition = getIntent().getIntExtra("position", 0);
        int count = mAdapter.getCount();
        vp.setCurrentItem(currenPosition);

        List<PictureBean> viewLists = mAdapter.getViewLists();
        PictureBean pictureBean = viewLists.get(currenPosition);
        String uploadDate = pictureBean.getUploadDate();
        if (!TextUtils.isEmpty(uploadDate)) {
            tvDate.setText(uploadDate);
        }

        String collection = pictureBean.getCollection();
        if ("1".equals(collection)) {
            ViewUtils.setTextViewDrawableTop(this, tvCollec, R.drawable.collected);
        } else {
            ViewUtils.setTextViewDrawableTop(this, tvCollec, R.drawable.collection);
        }


        String type = pictureBean.getType();

        String wrongPhoto = pictureBean.getWrongPhoto();


        if ("5".equals(type)) {
            btnDownLoad.setText(R.string.m216_cancel_hq);
            btnDownLoad.setEnabled(true);
        } else if ("0".equals(type) && "1".equals(wrongPhoto)) {
            btnDownLoad.setText(R.string.m210_hqphoto_is_not_available);
            btnDownLoad.setEnabled(false);//不可点击
        } else {
            //可点击
            btnDownLoad.setEnabled(!"1".equals(type) && !"2".equals(type));//不可点击
        }


        String num = (currenPosition + 1) + "/" + count;
        tvNum.setText(num);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currenPosition = position;


        List<PictureBean> viewLists = mAdapter.getViewLists();

        String num = (position + 1) + "/" + viewLists.size();
        tvNum.setText(num);


        PictureBean pictureBean = viewLists.get(position);


        String collection = pictureBean.getCollection();
        if ("1".equals(collection)) {
            ViewUtils.setTextViewDrawableTop(this, tvCollec, R.drawable.collected);
        } else {
            ViewUtils.setTextViewDrawableTop(this, tvCollec, R.drawable.collection);
        }

        String uploadDate = pictureBean.getUploadDate();
        if (!TextUtils.isEmpty(uploadDate)) {
            tvDate.setText(uploadDate);
        }

        String type = pictureBean.getType();
        String wrongPhoto = pictureBean.getWrongPhoto();

        if ("5".equals(type)) {
            btnDownLoad.setText(R.string.m209_waiting_synchronization);
            btnDownLoad.setEnabled(true);
        } else if ("0".equals(type) && "1".equals(wrongPhoto)) {
            btnDownLoad.setText(R.string.m210_hqphoto_is_not_available);
            btnDownLoad.setEnabled(false);//不可点击
        } else {
            //可点击
            btnDownLoad.setEnabled(!"1".equals(type) && !"2".equals(type));//不可点击
        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @OnClick({R.id.tv_collec, R.id.tv_delete, R.id.tv_share, R.id.btn_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_collec: {
                List<PictureBean> viewLists = mAdapter.getViewLists();
                int position = vp.getCurrentItem();
                PictureBean pictureBean = viewLists.get(position);
                String id = pictureBean.getId();
                String collection = pictureBean.getCollection();
                String operationValue = "1".equals(collection) ? "0" : "1";
                presenter.operation(id, "favorites", operationValue);
            }
            break;
            case R.id.tv_delete: {
                List<PictureBean> viewLists = mAdapter.getViewLists();
                int position = vp.getCurrentItem();
                PictureBean pictureBean = viewLists.get(position);
                String id = pictureBean.getId();
                presenter.operation(id, "remove", "1");
            }
            break;
            case R.id.tv_share:
                break;

            case R.id.btn_download: {
                List<PictureBean> viewLists = mAdapter.getViewLists();
                int position = vp.getCurrentItem();
                PictureBean pictureBean = viewLists.get(position);
                String id = pictureBean.getId();
                String type = pictureBean.getType();
                if ("5".equals(type)) {
                    presenter.operation(id, "resolution", "0");
                } else {
                    presenter.operation(id, "resolution", "1");
                }

            }
            break;

        }
    }

    @Override
    public void showCollecMsg(String collection) {
        int currentItem = vp.getCurrentItem();
        PictureBean pictureBean = mAdapter.getViewLists().get(currentItem);
        pictureBean.setCollection(collection);
        mAdapter.notifyDataSetChanged();
        if ("1".equals(collection)) {
            ViewUtils.setTextViewDrawableTop(this, tvCollec, R.drawable.collected);
        } else {
            ViewUtils.setTextViewDrawableTop(this, tvCollec, R.drawable.collection);
        }



    }

    @Override
    public void delete(String photoId) {
  /*      int currentItem = vp.getCurrentItem();
        mAdapter.getViewLists().remove(currentItem);
        mAdapter.notifyDataSetChanged();*/
        int index=-1;
        for (int i = 0; i < picList.size(); i++) {
            String id = picList.get(i).getId();
            if (id.equals(photoId)){
                index=i;
                break;
            }
        }

        if (index!=-1){
            picList.remove(index);
            mAdapter = new ViewPagerAdapter(picList);
            vp.setAdapter(mAdapter);


            int count = mAdapter.getCount();
            if (currenPosition>=count-1){
                currenPosition--;
            }
            vp.setCurrentItem(currenPosition);

            String num = (currenPosition + 1) + "/" + mAdapter.getCount();
            tvNum.setText(num);
        }

        //通知其他页面更新
        EventBus.getDefault().post(new FreshPhoto());
//        finish();
    }

    @Override
    public void dowload(String photoId, String msg, String operationValue) {
        MyToastUtils.toast(msg);


        int currentItem = vp.getCurrentItem();
        PictureBean pictureBean = mAdapter.getViewLists().get(currentItem);
        if ("1".equals(operationValue)) {
            pictureBean.setType("5");
            btnDownLoad.setText(R.string.m209_waiting_synchronization);
        } else {
            pictureBean.setType("0");
            btnDownLoad.setText(R.string.m24_download);
        }
        mAdapter.notifyDataSetChanged();
        //通知其他页面更新
        EventBus.getDefault().post(new FreshPhoto());
    }


    class ViewPagerAdapter extends PagerAdapter {

        private List<PictureBean> viewLists;
        private List<View> imageViews;


        public ViewPagerAdapter(List<PictureBean> viewLists) {
            this.viewLists = viewLists;
            imageViews = new ArrayList<>();
            for (int i = 0; i < viewLists.size(); i++) {
                View inflate = LayoutInflater.from(CameraDetailActivity.this).inflate(R.layout.layout_vp_image, vp, false);
                ImageView ivCamera = inflate.findViewById(R.id.iv_camera);
                TextView tvHd = inflate.findViewById(R.id.tv_hd);


                String type = viewLists.get(i).getType();
                //类型 图片类型(0:缩略图;1:高清图;2:视频)
                if (!"1".equals(type)) {
                    tvHd.setVisibility(View.GONE);
                } else {
                    tvHd.setVisibility(View.VISIBLE);
                }


                String url = viewLists.get(i).getFullPath();
                GlideUtils.getInstance().showImageContext(mContext, R.drawable.kaola, R.drawable.kaola, url, ivCamera);
                inflate.setOnClickListener(view -> {
                    Intent intent = new Intent(CameraDetailActivity.this, BigImageActivty.class);
                    int position = vp.getCurrentItem();
                    PictureBean pictureBean = viewLists.get(position);
                    String fullPath = pictureBean.getFullPath();
                    intent.putExtra("path", fullPath);
                    startActivity(intent);
                });

                imageViews.add(inflate);
            }
        }


        @Override
        public int getCount() {
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;//
        }


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(imageViews.get(position));//将image添加到容器中显示
            return imageViews.get(position);//返回当前下标要显示的imageview
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            container.removeView(imageViews.get(position));//销毁的item
        }


        public List<PictureBean> getViewLists() {
            return viewLists;
        }


    }


}
