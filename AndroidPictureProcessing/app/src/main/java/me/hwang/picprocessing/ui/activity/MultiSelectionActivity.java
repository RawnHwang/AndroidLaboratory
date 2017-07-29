package me.hwang.picprocessing.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import me.hwang.picprocessing.R;
import me.hwang.picprocessing.adapter.PictureAddedAdapter;
import me.hwang.picprocessing.base.BaseActivity;
import me.hwang.picprocessing.manager.PopupWindowManager;
import me.hwang.picprocessing.manager.WindowManager;

public class MultiSelectionActivity extends BaseActivity {

    public static final String IMAGE_ADDED_EXTRA = "IMAGE_ADDED";
    /*
     * startActivityForResult - requestCode
     */
    public final int PICTURE_MULTI_SELECT = 0;

    // gridView已选择的图片
    private GridView gvPictureAdded;
    // gridView adapter
    private PictureAddedAdapter picAddedAdapter;
    // 已添加的图片数据集合
    private ArrayList<String> listPicAdded;

    // 底部弹出菜单
    private PopupWindow mPopupWindow;
    // 底部弹出菜单选项
    private TextView tvItemAddFromAlbum;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICTURE_MULTI_SELECT:
                    handleMultiSelect(data);
                    break;
            }
        }
    }

    private void handleMultiSelect(Intent data) {
        listPicAdded.addAll(0,data.getStringArrayListExtra(IMAGE_ADDED_EXTRA));
        if(listPicAdded.size() >= 9){
            picAddedAdapter.permitAdd(false);
        }
        // request refresh
        picAddedAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_multi_selection;
    }

    @Override
    protected void initVariables() {
        picAddedAdapter = new PictureAddedAdapter(this,listPicAdded = new ArrayList<>());
    }

    @Override
    protected void initViews() {
        gvPictureAdded = (GridView) findViewById(R.id.gv_picture_added);
        gvPictureAdded.setAdapter(picAddedAdapter);

        gvPictureAdded.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == listPicAdded.size()){
                    showPopMenu();
                }else{
                    //Todo 查看图片
                }
            }
        });

        mPopupWindow = new PopupWindowManager.Builder(this)
                .setContentView(R.layout.popup_multi_selection)
                .setBackgroundDrawable(new ColorDrawable(Color.WHITE))
                .setWidth(getResources().getDisplayMetrics().widthPixels * 4 /5)
                .setFocusable(true)
                .setTouchable(true)
                .setOutsideTouchable(true)
                .build();

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 恢复Activity窗口透明度
                WindowManager.setBackgroundAlpha(MultiSelectionActivity.this,1f);
            }
        });

        // 从相册选择
        tvItemAddFromAlbum = (TextView) mPopupWindow.getContentView().findViewById(R.id.item_add_from_album);
        tvItemAddFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFromAlbum();
                mPopupWindow.dismiss();
            }
        });
    }


    @Override
    protected void loadData() {

    }

    private void showPopMenu() {
        // 设置Activity窗口半透明
        WindowManager.setBackgroundAlpha(this,0.5f);
        // 从底部弹出popupWindow
        mPopupWindow.showAtLocation(mContentView, Gravity.CENTER, 0, 0);
    }

    private void addFromAlbum() {
        Intent intent = new Intent(this,AppAlbumActivity.class);
        intent.putExtra(AppAlbumActivity.EXTRA_SELECTED_NUM,listPicAdded.size());
        startActivityForResult(intent,PICTURE_MULTI_SELECT);
    }
}
