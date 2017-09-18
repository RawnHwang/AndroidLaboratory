package me.hwang.multiimageselector;

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

/**
 * 全局图片扫描器
 */
public class ImageScanner {

    private static final Uri EXTERNAL_IMAGE_PATH = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final String IMAGE_SELECTION = MediaStore.Images.Media.MIME_TYPE + " =? or " + MediaStore.Images.Media.MIME_TYPE + " =?";
    private static final String[] IMAGE_SELECTION_ARGS = new String[] {"image/jpeg","image/png"};

    public interface OnScanListener{
        void onScanCompleted(Map<String,List<String>> imageMap);
    }

    public static void scanningImage(OnScanListener listener){
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
            Map<String, List<String>> imageMap = new HashMap<>();

            Uri uri = Uri.parse("http://www.baidu.com/a/b/c/d?x=1&y=2");
            uri = Uri.parse("content://media1/external/images/media");
            Log.i("ImageScanner",EXTERNAL_IMAGE_PATH.toString()+"\n"
                    +EXTERNAL_IMAGE_PATH.getAuthority()+"\n"
                    +EXTERNAL_IMAGE_PATH.getHost()+"\n"
                    +EXTERNAL_IMAGE_PATH.getPath()+"\n"
                    +EXTERNAL_IMAGE_PATH.getQuery()+"\n"
                    +EXTERNAL_IMAGE_PATH.getScheme());
            Log.i("ImageScanner",uri.toString()+"\n"
                    +uri.getAuthority()+"\n"
                    +uri.getHost()+"\n"
                    +uri.getPath()+"\n"
                    +uri.getQuery()+"\n"
                    +uri.getScheme()+"\n");
            ContentResolver resolver = MyApplication.getContext().getContentResolver();
            Cursor cursor = resolver.query(EXTERNAL_IMAGE_PATH,null,IMAGE_SELECTION,IMAGE_SELECTION_ARGS, MediaStore.Images.Media.DATE_MODIFIED);
            // Cursor cursor = resolver.query(EXTERNAL_IMAGE_PATH,null,null,null,null);

            if(cursor == null)
                return null;

            while(cursor.moveToNext()){
                String path = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));

                //获取该图片的父路径名
                String parentFileName = new File(path).getParentFile().getName();

                if (!imageMap.containsKey(parentFileName)) {
                    List<String> pathList = new ArrayList<>();
                    pathList.add(path);
                    imageMap.put(parentFileName, pathList);
                } else {
                    imageMap.get(parentFileName).add(path);
                }
            }
            return imageMap;
        }

        @Override
        protected void onPostExecute(Map<String, List<String>> imageMap) {
            onScanListener.onScanCompleted(imageMap);
        }
    }
}
