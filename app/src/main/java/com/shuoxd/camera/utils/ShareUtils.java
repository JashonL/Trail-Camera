package com.shuoxd.camera.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.mylhyl.circledialog.CircleDialog;
import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareUtils {


    public static final String authority =  CommentUtils.getPackageName(App.getInstance()) + ".fileProvider";

    public static void share(FragmentActivity act, String picName, View headerVeiw, View scrollView, boolean isShare) {
        new CircleDialog.Builder()
                .setTitle(act.getString(R.string.m150_tips))
                .setText(act.getString(R.string.m218_save_album))
                .setNegative(act.getString(R.string.m127_cancel), v -> {

                })
                .setPositive(act.getString(R.string.m152_ok), v -> {

                }).show(act.getSupportFragmentManager());
    }







    // 保存到sdcard
    public static Uri savePic(Activity activity, Bitmap b, File strFileName) {
        Uri fileUri = null;

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileUri = FileProvider.getUriForFile(
                    activity,
                    authority,
                    strFileName);
        } catch (IllegalArgumentException e) {
            Log.e("File Selector",
                    "The selected file can't be shared: " + strFileName.toString());
        }
        return fileUri;
    }

    /**
     * 将Bitmap插入到图库
     *
     * @param activity
     * @param picName
     * @param pictureFile
     */
    public static void insertAlbum(Activity activity, String picName, File pictureFile) throws Exception{
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String insertImage = MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                            pictureFile.getAbsolutePath(), picName, null);
                    //获得插入图库后的图片路径
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = activity.getContentResolver().query(Uri.parse(insertImage), proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String fileStr = cursor.getString(column_index);
                    cursor.close();
                    // 最后通知图库更新
                    File file = new File(fileStr);
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(file);
                    intent.setData(uri);
                    activity.sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }




    public interface RecycleViewRecCallback {
        void onRecFinished(Bitmap bitmap);
    }


    // 获取指定Activity的截屏，保存到png文件
    public static Bitmap takeScreenShot(Activity activity, File picpath) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

    /*    // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;*/

        // 获取屏幕长和高
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        // 去掉标题栏
        Bitmap b = Bitmap.createBitmap(b1, 0, 0, width, height);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 压缩图片
        final int cacheSize = maxMemory / 8;
        b = ImagePathUtil.qualityCompress1(b, cacheSize);
        Canvas canvas = new Canvas(b);
        canvas.drawColor(ContextCompat.getColor(activity, R.color.white));
        view.draw(canvas);
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        savePic(activity,b, picpath);
        return b;
    }


    /**
     * 使用友盟分享图片
     *
     * @param bitmap    分享的bitmap对象
     */

    public static void sharePic(Context context, Uri bitmap) throws Exception{
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        // 比如发送文本形式的数据内容
// 指定发送的内容
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
// 指定发送内容的类型
        sendIntent.setType("text/plain");
        // 比如发送二进制文件数据流内容（比如图片、视频、音频文件等等）
// 指定发送的内容 (EXTRA_STREAM 对于文件 Uri )
        sendIntent.putExtra(Intent.EXTRA_STREAM, bitmap);
// 指定发送内容的类型 (MIME type)
        sendIntent.setType("image/jpeg");

        context.startActivity(Intent.createChooser(sendIntent, "Share To"));
    }



    public static Bitmap convertViewToBitmap(View view){

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }



    /**
     * @param pic1 图一
     * @param pic2 图二
     * @return only_bitmap
     */
    public static Bitmap combineBitmapsIntoOnlyOne(Bitmap pic1, Bitmap pic2, Activity context) throws Exception{
        // 获取屏幕长和高
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int w_total = metrics.widthPixels;
//        int w_total = pic2.getWidth();
        int h_total = pic1.getHeight() + pic2.getHeight();
        int h_pic1 = pic1.getHeight();
        Bitmap only_bitmap = Bitmap.createBitmap(w_total, h_total, Bitmap.Config.ARGB_8888);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        only_bitmap = ImagePathUtil.qualityCompress1(only_bitmap, cacheSize);
        Canvas canvas = new Canvas(only_bitmap);
        canvas.drawColor(ContextCompat.getColor(context, R.color.white));
        canvas.drawBitmap(pic1, 0, 0, null);
        canvas.drawBitmap(pic2, 0, h_pic1, null);
        return only_bitmap;
    }



    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    private static Bitmap cropBitmap(Bitmap bitmap,Bitmap pic1) throws Exception {

        int height = pic1.getHeight();

        int cropWidth =bitmap.getWidth();
        int cropHeight = bitmap.getHeight()-pic1.getHeight();
        return Bitmap.createBitmap(bitmap, 0, height, cropWidth, cropHeight, null, false);
    }


}
