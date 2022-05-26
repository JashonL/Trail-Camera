package com.shuoxd.camera.module.me;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.shuoxd.camera.ActivityAbout;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.HomeDeviceSmallAdapter;
import com.shuoxd.camera.adapter.MySetAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.bean.SetBean;
import com.shuoxd.camera.customview.GridDivider;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.module.account.ChangePassWordActivity;
import com.shuoxd.camera.module.account.UserCenterActivity;
import com.shuoxd.camera.module.bill.BillingHistoryActivity;
import com.shuoxd.camera.module.gallery.PhotoPresenter;
import com.shuoxd.camera.module.gallery.PhotoView;
import com.shuoxd.camera.module.login.LoginActivity;
import com.shuoxd.camera.module.message.MessageActivity;
import com.shuoxd.camera.module.message.MessageListActivity;
import com.shuoxd.camera.module.plans.PlansActivity;
import com.shuoxd.camera.module.support.SupportActivity;
import com.shuoxd.camera.utils.AppUtils;
import com.shuoxd.camera.utils.MyToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MeFragment extends BaseFragment<MePresenter> implements MeView, BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.v_background)
    View vBackground;
    @BindView(R.id.guideline_begin)
    Guideline guidelineBegin;
    @BindView(R.id.guideline_end)
    Guideline guidelineEnd;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_username)
    AppCompatTextView tvUsername;
    @BindView(R.id.tv_certified)
    AppCompatTextView tvCertified;
    @BindView(R.id.tv_email)
    AppCompatTextView tvEmail;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.v_top_bottom)
    View vTopBottom;
    @BindView(R.id.tv_photo_count)
    AppCompatTextView tvPhotoCount;
    @BindView(R.id.v_line1)
    View vLine1;
    @BindView(R.id.tv_camera_count)
    AppCompatTextView tvCameraCount;
    @BindView(R.id.v_line2)
    View vLine2;
    @BindView(R.id.tv_video)
    AppCompatTextView tvVideo;
    @BindView(R.id.cl_num)
    ConstraintLayout clNum;
    @BindView(R.id.iv_ad)
    ImageView ivAd;
    @BindView(R.id.tv_service)
    AppCompatTextView tvService;
    @BindView(R.id.v_service_line)
    View vServiceLine;
    @BindView(R.id.rv_set)
    RecyclerView rvSet;
    @BindView(R.id.tv_date)
    TextView tvDate;


    private MySetAdapter mAdapter;


    @Override
    protected MePresenter createPresenter() {
        return new MePresenter(getContext(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initView() {
        //设置toolbar
/*        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.nocolor));
        tvTitle.setText("");*/

        //初始化RecyclerView

        tvDate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        setAdapter();


    }


    //小图片布局
    private void setAdapter() {
        rvSet.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter = new MySetAdapter(R.layout.item_app_set, new ArrayList<>());
        rvSet.setAdapter(mAdapter);
        rvSet.addItemDecoration(new GridDivider(ContextCompat.getColor(getActivity(), R.color.nocolor), 30, 30));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_empty_view, null);
        mAdapter.setEmptyView(view);
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.setOnItemClickListener(this);
    }


    @Override
    protected void initData() {
        //设置用户信息
        String accountName = App.getUserBean().getAccountName();
        tvUsername.setText(accountName);
        tvEmail.setText(accountName);


        String[] titles = {getString(R.string.m79_plans), getString(R.string.m80_billing_history), getString(R.string.m81_Message),
                getString(R.string.m82_account), getString(R.string.m83_Support), getString(R.string.m84_sign_out)
        };


        int[] res = {R.drawable.plans, R.drawable.billing_hisory, R.drawable.messages,
                R.drawable.account, R.drawable.support, R.drawable.sign_out
        };


        List<SetBean> list = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            SetBean setBean = new SetBean();
            setBean.setIconRes(res[i]);
            setBean.setTitle(titles[i]);
            list.add(setBean);
        }
        mAdapter.replaceData(list);


        try {
            String name=getContext().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            name=getString(R.string.m224_version)+":"+name;
            tvDate.setText(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        presenter.userCenter(accountName);

    }


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarDarkFont(false, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.color_app_main)//这里的颜色，你可以自定义。
                .init();
    }


    @OnClick({R.id.iv_edit,R.id.tv_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_edit:
                startActivity(new Intent(getContext(), ChangePassWordActivity.class));
                break;
            case R.id.tv_date:
                presenter.getSystemConfig();
                AppUtils.isUpgradeApp(getContext(),true);
                break;
        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(getContext(), PlansActivity.class));
                break;
            case 1:
                startActivity(new Intent(getContext(), BillingHistoryActivity.class));
                break;
            case 4:
                startActivity(new Intent(getContext(), SupportActivity.class));
                break;
            case 2:
                startActivity(new Intent(getContext(), MessageListActivity.class));
                break;
            case 3:
                startActivity(new Intent(getContext(), UserCenterActivity.class));
                break;
            case 5:
                //
                String accountName = App.getUserBean().accountName;
                presenter.userLogout(accountName);
                break;
        }
    }


    @Override
    public void showLoginError(String errorMsg) {
        ToastUtils.show(errorMsg);
    }

    @Override
    public void logout() {
        //跳转到登录界面
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void photoCount(String photos) {
        if (!TextUtils.isEmpty(photos)){
            tvPhotoCount.setText(photos);
        }
    }

    @Override
    public void cameraCount(String cameras) {
        if (!TextUtils.isEmpty(cameras)){
            tvCameraCount.setText(cameras);
        }
    }

    @Override
    public void videoCount(String videos) {
        if (!TextUtils.isEmpty(videos)){
            tvVideo.setText(videos);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
