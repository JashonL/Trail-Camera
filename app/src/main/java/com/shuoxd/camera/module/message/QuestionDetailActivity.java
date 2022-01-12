package com.shuoxd.camera.module.message;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.AddImageAdapter;
import com.shuoxd.camera.adapter.ImageAdapter;
import com.shuoxd.camera.adapter.QuestionReplyAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.AddImageBean;
import com.shuoxd.camera.bean.QuestionBean;
import com.shuoxd.camera.bean.ReplyBean;
import com.shuoxd.camera.constants.AllPermissionRequestCode;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.eventbus.FreshQuestion;
import com.shuoxd.camera.module.pictrue.BigImageActivty;
import com.shuoxd.camera.utils.ImagePathUtil;
import com.shuoxd.camera.utils.MyToastUtils;
import com.shuoxd.camera.utils.PhotoUtil;
import com.shuoxd.camera.utils.SoftHideKeyBoardUtil;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class QuestionDetailActivity extends BaseActivity<QuestionDetailPresenter>
        implements QuestionDetailView, Toolbar.OnMenuItemClickListener, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_question_detail)
    TextView tvQuestionDetail;
    @BindView(R.id.rlv_pic)
    RecyclerView rlvPic;
    @BindView(R.id.rlv_content)
    RecyclerView rlvContent;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.rlv_add_pic)
    RecyclerView rlvAddPic;
    @BindView(R.id.cl_reply)
    ConstraintLayout clReply;
    @BindView(R.id.btn_solved)
    AppCompatButton btnSolved;
    @BindView(R.id.et_content)
    EditText etContent;


    private QuestionReplyAdapter mAdapter;

    private ImageAdapter imageAdapter;


    private AddImageAdapter addAdapter;

    @Override
    protected QuestionDetailPresenter createPresenter() {
        return new QuestionDetailPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_quetion_detail;
    }

    @Override
    protected void initViews() {
        SoftHideKeyBoardUtil.assistActivity(this);
        //toolbar
        initToobar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m139_question);


        setQuestionPic();
        //recyclerview
        setReplyAdapter();

        setAddAdapter();
    }


    //问题的图片
    private void setQuestionPic() {
        rlvPic.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new ImageAdapter(R.layout.item_image, new ArrayList<>());
        rlvPic.setAdapter(imageAdapter);
        rlvPic.addItemDecoration(new LinearDivider(this, LinearLayoutManager.HORIZONTAL, 20, ContextCompat.getColor(this, R.color.nocolor)));
        imageAdapter.setOnItemClickListener(this);
    }


    //回复的列表
    private void setReplyAdapter() {
        rlvContent.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new QuestionReplyAdapter(R.layout.item_question_reply, new ArrayList<>());
        rlvContent.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        LinearDivider linearDivider = new LinearDivider(this, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(this, R.color.gray_aaaaaa));
        rlvContent.addItemDecoration(linearDivider);
    }

    //添加的图片
    private void setAddAdapter() {
        rlvAddPic.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        addAdapter = new AddImageAdapter(new ArrayList<>());
        rlvAddPic.setAdapter(addAdapter);
        rlvAddPic.addItemDecoration(new LinearDivider(this, LinearLayoutManager.HORIZONTAL, 20, ContextCompat.getColor(this, R.color.nocolor)));
        addAdapter.setOnItemClickListener(this);
        addAdapter.setOnItemChildClickListener(this);
    }

    @Override
    protected void initData() {

        //请求单个问题详情
        String id = getIntent().getStringExtra("id");
        presenter.setQuestionId(id);


        String accountName = App.getUserBean().getAccountName();
        presenter.questionDetail(id, accountName);


        //添加一个添加按钮
        ArrayList<AddImageBean> newList = new ArrayList<>();
        AddImageBean bean = new AddImageBean();
        bean.setType(1);
        newList.add(bean);
        addAdapter.replaceData(newList);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == QuestionDetailPresenter.CODE_GALLERY_REQUEST) {
                if (data != null) {
                    try {
                        PhotoUtil.startCropImageAct(this, data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == QuestionDetailPresenter.CODE_CAMERA_REQUEST) {
                try {
                    presenter.startCrop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
    }


    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            setImageToHeadView(resultUri);
        } else {
            MyToastUtils.toast(R.string.m86_fail);
        }
    }

    //添加图片
    private void setImageToHeadView(Uri uri) {
        try {
            String path = ImagePathUtil.getRealPathFromUri(this, uri);
            File file = new File(path);
            if (file.length() > 10 * 1024 * 1024) {
                MyToastUtils.toast(R.string.m146_must_smaller);
                return;
            }
            ;

            //更新图片
//            GlideUtils.showImageAct(this, R.drawable.kaola, R.drawable.kaola, plantPath, ivPhoto);
            List<AddImageBean> data = addAdapter.getData();
            //最后一个是添加按钮
            AddImageBean bean = new AddImageBean();
            bean.setType(0);
            bean.setPath(path);
            //插入倒数第二个
            data.add(data.size() - 1, bean);
            if (data.size() > 3) {
                data.remove(data.size() - 1);
            }
            addAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case AllPermissionRequestCode.PERMISSION_CAMERA_CODE:
                if (EasyPermissions.hasPermissions(this, AllPermissionRequestCode.PERMISSION_CAMERA)) {
                    try {
                        presenter.takePicture();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case AllPermissionRequestCode.PERMISSION_EXTERNAL_STORAGE_CODE:
                if (EasyPermissions.hasPermissions(this, AllPermissionRequestCode.PERMISSION_EXTERNAL_STORAGE)) {
                    presenter.selectPicture();
                }
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == addAdapter) {
            AddImageBean bean = addAdapter.getData().get(position);
            int itemType = bean.getItemType();
            String path = bean.getPath();
            if (itemType == 1) {
                presenter.changeImgDialog();
            } else {//放大图片
                Intent intent = new Intent(this, BigImageActivty.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        }

        if (adapter == imageAdapter) {
            String s = imageAdapter.getData().get(position);
            Intent intent = new Intent(this, BigImageActivty.class);
            intent.putExtra("path", s);
            startActivity(intent);
        }
    }

    @Override
    public void showQuestion(QuestionBean questionBean) {
        String title = questionBean.getTitle();
        if (!TextUtils.isEmpty(title)) {
            tvName.setText(title);
        }

        String content = questionBean.getContent();
        if (!TextUtils.isEmpty(content)) {
            tvQuestionDetail.setText(content);
        }

        String time = questionBean.getCreateTime();
        if (!TextUtils.isEmpty(time)) {
            tvDate.setText(time);
        }


        List<String> paths = new ArrayList<>();
        String path1 = questionBean.getAttachmentOnePath();
        String path2 = questionBean.getAttachmentTwoPath();
        String path3 = questionBean.getAttachmentThreePath();
        if (!TextUtils.isEmpty(path1)) {
            paths.add(path1);
        }
        if (!TextUtils.isEmpty(path2)) {
            paths.add(path2);
        }

        if (!TextUtils.isEmpty(path3)) {
            paths.add(path3);
        }
        imageAdapter.replaceData(paths);

    }

    @Override
    public void showStatus(String status) {
        clReply.setVisibility("2".equals(status)?View.GONE:View.VISIBLE);
        btnSolved.setEnabled("2".equals(status));
    }

    @Override
    public void showReply(List<ReplyBean> beans) {
   /*     ReplyBean bean = new ReplyBean();
        bean.setReplyTime("2022-01-12");
        bean.setReplyUsername("721695214@qq.com");
        bean.setContent("这是一条测试数据");
        bean.setAttachmentOnePath("https://img.shuoxd.com/camera/oudiwei/user/admin@qq.com/16415744832791202.JPG");
        bean.setAttachmentTwoPath("https://img.shuoxd.com/camera/oudiwei/user/admin@qq.com/16415744832791202.JPG");
        bean.setAttachmentThreePath("https://img.shuoxd.com/camera/oudiwei/user/admin@qq.com/16415744832791202.JPG");
        beans.add(bean);
        beans.add(bean);*/
        mAdapter.replaceData(beans);
        //滚动到最后一条
        int positionForSection = mAdapter.getData().size() - 1;
        if (positionForSection != -1) {
            rlvContent.scrollToPosition(positionForSection);
            LinearLayoutManager mLayoutManager = (LinearLayoutManager) rlvContent.getLayoutManager();
            assert mLayoutManager != null;
            mLayoutManager.scrollToPositionWithOffset(positionForSection, 0);
        }

    }

    @Override
    public void solved() {
        EventBus.getDefault().post(new FreshQuestion());
        finish();
    }

    @Override
    public void replySuccess() {
        List<AddImageBean> datas = new ArrayList<>();
        AddImageBean bean = new AddImageBean();
        bean.setType(1);
        datas.add(bean);
        addAdapter.replaceData(datas);
        etContent.setText("");
        //重新请求问题详情
        String accountName = App.getUserBean().getAccountName();
        presenter.questionDetail(presenter.getQuestionId(), accountName);
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_delete:
                List<AddImageBean> data = addAdapter.getData();
                List<AddImageBean> newList = new ArrayList<>(data);
                //删除当前这个
                newList.remove(position);
                AddImageBean bean1 = newList.get(newList.size() - 1);
                int type = bean1.getType();
                if (type != 1) {
                    //最后一个是添加按钮
                    AddImageBean bean = new AddImageBean();
                    bean.setType(1);
                    newList.add(bean);
                }
                addAdapter.replaceData(newList);
                break;
        }
    }


    @OnClick({R.id.tv_send, R.id.btn_solved})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_send:
                String s = etContent.getText().toString().trim();
                if (TextUtils.isEmpty(s)) {
                    MyToastUtils.toast(R.string.m145_content_cannot_empty);
                    return;
                }

                List<AddImageBean> data = addAdapter.getData();
                List<File> files = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    AddImageBean bean = data.get(i);
                    String path = bean.getPath();
                    if (!TextUtils.isEmpty(path)) {
                        File file = new File(path);
                        files.add(file);
                    }
                }

                presenter.replyQuestion(s, files);

                break;
            case R.id.btn_solved:
                presenter.operation("ok");

                break;
        }
    }
}
