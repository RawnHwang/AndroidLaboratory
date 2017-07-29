package me.hwang.picprocessing.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;

import me.hwang.picprocessing.R;
import me.hwang.picprocessing.base.BaseActivity;
import me.hwang.picprocessing.util.BitmapHelper;
import me.hwang.picprocessing.util.FileUtil;

public class PictureCompressionActivity extends BaseActivity {
    private String filePath;
    private String CompressPath;
    private TextView tvBeforeCompress, tvAfterCompress;
    private ImageView ivBeforeCompress, ivAfterCompress;
    private Button btnQualityCompress, btnPixelCompress;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_picture_compression;
    }

    @Override
    protected void initVariables() {
        filePath = FileUtil.getImageStoragePath() + "/" + "test.jpg";
        CompressPath = FileUtil.getImageStoragePath() + "/" + "compress.jpg";
    }

    @Override
    protected void initViews() {
        tvBeforeCompress = (TextView) findViewById(R.id.tv_before_compress);
        tvAfterCompress = (TextView) findViewById(R.id.tv_after_compress);
        ivBeforeCompress = (ImageView) findViewById(R.id.iv_before_compress);
        ivAfterCompress = (ImageView) findViewById(R.id.iv_after_compress);
        btnPixelCompress = (Button) findViewById(R.id.btn_pixel_compress);
        btnQualityCompress = (Button) findViewById(R.id.btn_quality_compress);
        btnPixelCompress.setVisibility(View.GONE);
        btnQualityCompress.setVisibility(View.GONE);

        btnPixelCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPixelCompress();
            }
        });

        btnQualityCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doQualityCompress();
            }
        });
    }

    @Override
    protected void loadData() {

    }

    private void doPixelCompress() {
        Bitmap rawBitmap = BitmapFactory.decodeFile(filePath);
        float rawCount = (float) rawBitmap.getByteCount() / 1024 / 1024;
        ivBeforeCompress.setImageBitmap(rawBitmap);
        tvBeforeCompress.setText("像素压缩前所占的内存空间为:" + (float) (Math.round(rawCount * 100)) / 100 + "MB"
                + "\n bitmap width pixel is :\t" + rawBitmap.getWidth() + "\n bitmap height pixel is :\t" + rawBitmap.getHeight());


        Bitmap sampledBitmap = BitmapHelper.decodeSampledBitmapFromFile(filePath,ivAfterCompress.getMeasuredWidth(),ivAfterCompress.getMeasuredHeight());
        ivAfterCompress.setImageBitmap(sampledBitmap);
        float compressedCount = (float) sampledBitmap.getByteCount() / 1024 / 1024;
        tvAfterCompress.setText("像素压缩后所占的内存空间为:" + (float) (Math.round(compressedCount * 100)) / 100 + "MB"
                + "\n bitmap width pixel is :\t" + sampledBitmap.getWidth() + "\n bitmap height pixel is :\t" + sampledBitmap.getHeight()
                + "\n imageview width pixel is :\t" + ivAfterCompress.getMeasuredWidth() + "\n imageview height pixel is :\t" + ivAfterCompress.getMeasuredHeight());
    }

    private void doQualityCompress() {
        Bitmap rawBitmap = BitmapFactory.decodeFile(filePath);
        float rawCount = (float) rawBitmap.getByteCount() / 1024 / 1024;
        ivBeforeCompress.setImageBitmap(rawBitmap);
        tvBeforeCompress.setText("像素压缩前所占的内存空间为:" + (float) (Math.round(rawCount * 100)) / 100 + "MB"
                + "\n bitmap width pixel is :\t" + rawBitmap.getWidth() + "\n bitmap height pixel is :\t" + rawBitmap.getHeight());


        Bitmap compressedBitmap = BitmapHelper.bitmapCompressAndStorage(CompressPath, rawBitmap, 25, false);
        float compressedCount = (float) compressedBitmap.getByteCount() / 1024 / 1024;
        ivAfterCompress.setImageBitmap(compressedBitmap);
        tvAfterCompress.setText("像素压缩前所占的内存空间为:" + (float) (Math.round(compressedCount * 100)) / 100 + "MB"
                + "\n bitmap width pixel is :\t" + rawBitmap.getWidth() + "\n bitmap height pixel is :\t" + rawBitmap.getHeight());
    }

}
