package me.hwang.picprocessing.ui.activity;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;

import me.hwang.picprocessing.R;
import me.hwang.picprocessing.base.BaseActivity;
import me.hwang.picprocessing.manager.PopupWindowManager;
import me.hwang.picprocessing.manager.WindowManager;
import me.hwang.picprocessing.util.BitmapHelper;
import me.hwang.picprocessing.util.FileUtil;

public class SingleSelectionActivity extends BaseActivity {

    /*
     * startActivityForResult - requestCode
     */
    private final int TAKE_PICTURE = 0; // 拍摄
    private final int CHOOSE_FROM_ALBUM = 1; // 从相册选取

    // 设置头像按钮
    private Button btnSetProfilePhoto;
    // 显示获取到的图像的ImageView
    private ImageView ivProfilePhoto;

    // 底部弹出菜单
    private PopupWindow mPopupWindow;
    // 底部弹出菜单选项 (拍摄)
    private TextView tvItemTakePicture;
    // 底部弹出菜单选项 (从相册选择)
    private TextView tvItemChooseFromAlbum;

    // 当前拍摄的照片的文件路径
    private String mCurrentImagePath;
    // 当前拍摄的照片的Uri
    private Uri mCurrentImageUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    handleWithTakePicture();
                    break;
                case CHOOSE_FROM_ALBUM:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        handleWithChooseFromAlbumAPI19(data);
                    } else {
                        handleWithChooseFromAlbum(data);
                    }
                    break;
            }
        }
    }

    private void displayImage(String path) {
        // 根据要显示图片的ImageView的大小对Bitmap进行裁剪/压缩，以减小内存开销
        Bitmap bitmap = BitmapHelper.decodeSampledBitmapFromFile(path, ivProfilePhoto.getMeasuredWidth(), ivProfilePhoto.getMeasuredHeight());
        // 如果图片的旋转角度不为0，则需要将bitmap进行对应角度的旋转确保其正确显示
        bitmap = BitmapHelper.rotateBitmapByDegree(bitmap, BitmapHelper.getBitmapDegree(path));
        // 将bitmap显示到ImageView上
        ivProfilePhoto.setImageBitmap(bitmap);
    }

    private void handleWithTakePicture() {
        // 发送广播，通知媒体扫描最新拍摄的图片(否则此时系统相册无法查看到本次拍摄的照片)
        BitmapHelper.informMediaScanner(this, mCurrentImageUri);
        // 执行的显示工作
        displayImage(mCurrentImagePath);
        // 对图像进行质量压缩并重新存储(可以针对图片体积过大进行优化)
        BitmapHelper.bitmapCompressAndStorage(mCurrentImagePath);
    }

    private void handleWithChooseFromAlbum(Intent data) {
        // 获取Uri
        Uri imageUri = data.getData();

        // 根据Uri获取文件路径
        String imagePath = null;
        if (imageUri.getScheme().equalsIgnoreCase("content")) {
            imagePath = BitmapHelper.getImagePath(this,imageUri, null);
        } else if (imageUri.getScheme().equalsIgnoreCase("file")) {
            imagePath = imageUri.getPath();
        }

        displayImage(imagePath);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleWithChooseFromAlbumAPI19(Intent data) {
        Uri imageUri = data.getData();
        String imagePath = null;

        if (DocumentsContract.isDocumentUri(this, imageUri)) {
            String docID = DocumentsContract.getDocumentId(imageUri);

            if (imageUri.getAuthority().equals("com.android.providers.media.documents")) {
                // 解析出数字格式的ID
                String id = docID.split(":")[1];
                // id用于执行query的selection当中
                String selection = MediaStore.Images.Media._ID + " = " + id;
                // 查询path
                imagePath = BitmapHelper.getImagePath(this,MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if (imageUri.getAuthority().equals("com.android.providers.downloads.documents")) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads/"), Long.valueOf(docID));
                imagePath = BitmapHelper.getImagePath(this,contentUri, null);
            }

            displayImage(imagePath);
        } else {
            handleWithChooseFromAlbum(data);
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_single_selection;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {
        btnSetProfilePhoto = (Button) findViewById(R.id.btn_set_profile_photo);
        btnSetProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu();
            }
        });

        ivProfilePhoto = (ImageView) findViewById(R.id.iv_profile_photo);

        mPopupWindow = new PopupWindowManager.Builder(this)
                .setContentView(R.layout.popup_single_selection)
                .setBackgroundDrawable(new ColorDrawable(Color.WHITE))
                .setFocusable(true)
                .setTouchable(true)
                .setOutsideTouchable(true)
                .build();

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 恢复Activity窗口透明度
                WindowManager.setBackgroundAlpha(SingleSelectionActivity.this,1f);
            }
        });

        // 拍摄
        tvItemTakePicture = (TextView) mPopupWindow.getContentView().findViewById(R.id.item_take_picture);
        tvItemTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                mPopupWindow.dismiss();
            }
        });
        // 从相册选择
        tvItemChooseFromAlbum = (TextView)  mPopupWindow.getContentView().findViewById(R.id.item_choose_from_album);
        tvItemChooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFromAlbum();
                mPopupWindow.dismiss();
            }
        });
    }

    @Override
    protected void loadData() {

    }

    private void showPopMenu() {
        // 设置Activity窗口半透明
        WindowManager.setBackgroundAlpha(this, 0.5f);
        // 从底部弹出popupWindow
        mPopupWindow.showAtLocation(mContentView, Gravity.BOTTOM, 0, 0);
    }

    private void takePicture() {
        // 生成图片名
        File image = new File(FileUtil.getImageStorageDirectory(), FileUtil.generateFileName(FileUtil.Format.IMAGE_JPEG));
        // 赋值图像文件路径
        mCurrentImagePath = image.getAbsolutePath();
        // 赋值图像文件的Uri
        mCurrentImageUri = Uri.fromFile(image);

        // Action : android.media.action.IMAGE_CAPTURE
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 如果有能够响应该Action的组件
        if (intent.resolveActivity(getPackageManager()) != null) {
            // 设置图像输出的Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentImageUri);
            // 隐式Intent启动
            startActivityForResult(intent, TAKE_PICTURE);
        }
    }

    private void chooseFromAlbum() {
        // Action : android.intent.action.GET_CONTENT
        // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // intent.setType("image/*");
        // startActivityForResult(intent, CHOOSE_FROM_ALBUM);

        // or

        // Action : android.intent.action.PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CHOOSE_FROM_ALBUM);
    }
}
