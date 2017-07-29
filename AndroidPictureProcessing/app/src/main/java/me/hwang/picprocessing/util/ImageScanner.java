package me.hwang.picprocessing.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hwang.picprocessing.Application;
import me.hwang.picprocessing.ui.activity.AppAlbumActivity;

/**
 * 全局图片扫描器
 */
public class ImageScanner {

    public static final String ALL_IMAGES = "所有图片";
    private static final Uri EXTERNAL_IMAGE_PATH = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final String IMAGE_SELECTION = MediaStore.Images.Media.MIME_TYPE + " =? or " + MediaStore.Images.Media.MIME_TYPE + " =?";
    private static final String[] IMAGE_SELECTION_ARGS = new String[] {"image/jpeg","image/png"};

    public interface OnScanListener{
        void onScanCompleted(Map<String, List<String>> imageMap);
    }

    public static void scanImage(OnScanListener listener){
        AsyncScanningTask asyncTask =  new AsyncScanningTask(listener);
        asyncTask.execute();
    }

    private static class AsyncScanningTask extends AsyncTask<Void,Integer,Map<String,List<String>>>{

        private OnScanListener onScanListener;

        public AsyncScanningTask(OnScanListener listener){
            onScanListener = listener;
        }

        @Override
        protected Map<String, List<String>> doInBackground(Void... voids) {
            // 存放所有照片的list
            ArrayList<String> allList = new ArrayList<>();
            // Map根据目录分别存放
            Map<String, List<String>> directoryMap = new HashMap<>();
            // 获取ContentResolver
            ContentResolver resolver = Application.getContext().getContentResolver();
            // Log.d("ImageScanner==>",EXTERNAL_IMAGE_PATH.toString());
            // 执行查询
            Cursor cursor = resolver.query(EXTERNAL_IMAGE_PATH,null,IMAGE_SELECTION,IMAGE_SELECTION_ARGS, MediaStore.Images.Media.DATE_MODIFIED+" desc");

            if(cursor == null)
                return null;

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
                allList.add(path);
            }
            directoryMap.put(ALL_IMAGES,allList);

            return directoryMap;
        }

        @Override
        protected void onPostExecute(Map<String, List<String>> imageMap) {
            onScanListener.onScanCompleted(imageMap);
        }
    }
}
