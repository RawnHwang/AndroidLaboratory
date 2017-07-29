package me.hwang.picprocessing.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import me.hwang.picprocessing.R;
import me.hwang.picprocessing.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private Button btnGotoSingle;
    private Button btnGotoMulti;
    private Button btnGotoCompress;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {
        btnGotoSingle = (Button) findViewById(R.id.btn_goto_single);
        btnGotoSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SingleSelectionActivity.class);
                startActivity(intent);
            }
        });

        btnGotoMulti = (Button) findViewById(R.id.btn_goto_multi);
        btnGotoMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MultiSelectionActivity.class);
                startActivity(intent);
            }
        });

        btnGotoCompress = (Button) findViewById(R.id.btn_goto_compress);
        btnGotoCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PictureCompressionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData() {

    }
}
