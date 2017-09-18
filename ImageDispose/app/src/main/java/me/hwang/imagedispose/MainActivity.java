package me.hwang.imagedispose;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private Button btnTakePicture, btnGetFromAlbum;
    private ImageView ivResult;

    private Map<String, List<String>> directoryMap;
    private final Uri EXTERNAL_IMAGE_PATH = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private final String IMAGE_SELECTION = MediaStore.Images.Media.MIME_TYPE + " =? or " + MediaStore.Images.Media.MIME_TYPE + " =?";
    private final String[] IMAGE_SELECTION_ARGS = new String[] {"image/jpeg","image/png"};

    public void scanImage(){
        // Map根据目录分别存放
        directoryMap = new HashMap<>();
        // 获取ContentResolver
        ContentResolver resolver = getContentResolver();
        // 执行查询
        Cursor cursor = resolver.query(EXTERNAL_IMAGE_PATH,null,IMAGE_SELECTION,IMAGE_SELECTION_ARGS, MediaStore.Images.Media.DATE_MODIFIED+" desc");

        if(cursor == null)
            return;

        while(cursor.moveToNext()){
            // 获取图片路径
            String path = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));

            // 获取该图片的父路径名
            String parentFileName = new File(path).getParentFile().getName();

            if (!directoryMap.containsKey(parentFileName)) {
                List<String> directoryList = new ArrayList<>();
                directoryList.add(path);
                directoryMap.put(parentFileName, directoryList);
            } else {
                directoryMap.get(parentFileName).add(path);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTakePicture = (Button) findViewById(R.id.btn_take_picture);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        btnGetFromAlbum = (Button) findViewById(R.id.btn_get_from_album);
        btnGetFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImages();
            }
        });

        ivResult = (ImageView) findViewById(R.id.iv_result);
    }

    private void getImages() {
        Log.d(TAG+"==>",MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
        // Action : android.intent.action.GET_CONTENT
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 0x002);
    }

    private void getImage(){
        // Action : android.intent.action.PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 0x002);
    }
    private String mCurrentPath;

    private void takePicture() {
        File image = new File(mkAppImagesDir(), "test.jpg");
        mCurrentPath = image.getAbsolutePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // intent.putExtra("output", Uri.fromFile(image));
        startActivityForResult(intent, 0x001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            // 根据Uri获取文件路径
            String imagePath = null;
            if (imageUri.getScheme().equalsIgnoreCase("content")) {
                imagePath = getImagePath(this,imageUri, null);
            } else if (imageUri.getScheme().equalsIgnoreCase("file")) {
                imagePath = imageUri.getPath();
            }
        }
    }

    public static String getImagePath(Activity activity, Uri imageUri, String selection) {
        String path = null;
        // query projection
        String[] projection = {MediaStore.Images.Media.DATA};
        // 执行查询
        Cursor cursor;
        if (Build.VERSION.SDK_INT < 11) {
            cursor = activity.managedQuery(imageUri, projection, selection, null, null);
        } else {
            CursorLoader cursorLoader = new CursorLoader(activity, imageUri, projection, selection, null, null);
            cursor = cursorLoader.loadInBackground();
        }

        if (cursor != null) {
            // 从查询结果解析path
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

//    Bundle extras = data.getExtras();
//    Bitmap bitmap = (Bitmap) extras.get("data");
//        Log.d(TAG + "==>", bitmap.getWidth() + "//" + bitmap.getHeight());

    //    Log.d(TAG+"==>",data.getData().toString());
//    Cursor cursor = getContentResolver().query(data.getData(),null,null,null,null);
//        cursor.moveToFirst();
//    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        Log.d(TAG+"==>",path);
//    Bitmap bitmap = BitmapFactory.decodeFile(path);
//        Log.d(TAG+"==>",bitmap.getWidth()+"//"+bitmap.getHeight());

    private File mkAppImagesDir() {
        String path = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/" + "Images";

        File file = new File(path);

        if (!file.exists())
            file.mkdirs();

        return file;
    }

    public static void informMediaScanner(Context context, Uri uri) {
        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(localIntent);
    }
}
