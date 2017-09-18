package me.hwang.multiimageselector;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Button btnOpenAlbum,btnTestService,btnTestHandlerThread;
    private ImageView iv;
    private String imagePath;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOpenAlbum = findViewById(R.id.btn_open_album);
        btnTestService = findViewById(R.id.btn_test_service);
        btnTestHandlerThread = findViewById(R.id.btn_test_handler_thread);
        iv = findViewById(R.id.imageView);

        HandlerThread hThread = new HandlerThread("handler thread");
        hThread.start();

        handler = new Handler(hThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                Log.i("MainActivity",Thread.currentThread().getName());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        btnTestService.setText("worile");
                    }
                });
            }
        };

        btnTestService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MyService.class);
                startService(intent);
            }
        });

        btnOpenAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,111);

//                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//                getAlbum.setType("image/*");
//                startActivityForResult(getAlbum, 111);
            }
        });


        btnTestHandlerThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   handler.sendEmptyMessage(0x00);
            }
        });

//        ImageScanner.scanningImage(new ImageScanner.OnScanListener() {
//            @Override
//            public void onScanCompleted(Map<String, List<String>> imageMap) {
//                StringBuilder print = new StringBuilder();
//
//                Set<String> keySet = imageMap.keySet();
//                for (String key : keySet){
//                    print.append(key).append(":\n");
//
//                    List<String> pathList = imageMap.get(key);
//
//                    for (String path : pathList){
//                        print.append(path).append("\n");
//                        if(imagePath==null)
//                        imagePath = path;
//                    }
//                }
//                //Log.i("MainActivity",print.toString());
//                Glide.with(MainActivity.this).load(imagePath).into(iv);
//            }
//        });

    }


}
