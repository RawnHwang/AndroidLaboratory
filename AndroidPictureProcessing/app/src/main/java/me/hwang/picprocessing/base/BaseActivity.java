package me.hwang.picprocessing.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    // log tag
    protected final String TAG = this.getClass().getSimpleName();
    // activity content view
    protected View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());

        initVariables();
        initViews();
        loadData();
    }

    @Override
    public void setContentView(int layoutResID) {
        mContentView = LayoutInflater.from(this).inflate(layoutResID,null);
        super.setContentView(layoutResID);
    }

    protected abstract int getLayoutID();

    protected abstract void initVariables();

    protected abstract void initViews();

    protected abstract void loadData();

}

