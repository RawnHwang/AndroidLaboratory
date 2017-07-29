package me.hwang.picprocessing.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.hwang.picprocessing.R;
import me.hwang.picprocessing.adapter.AlbumFolderAdapter;
import me.hwang.picprocessing.adapter.AppAlbumAdapter;
import me.hwang.picprocessing.base.BaseActivity;
import me.hwang.picprocessing.bean.PictureFolder;
import me.hwang.picprocessing.manager.PopupWindowManager;
import me.hwang.picprocessing.util.ImageScanner;
import me.hwang.picprocessing.widget.MaxHeightRecyclerView;

public class AppAlbumActivity extends BaseActivity {
    public static  final String EXTRA_SELECTED_NUM = "SELECTED_NUM";
    private final String DEFAULT_FOLDER = ImageScanner.ALL_IMAGES;
    private final int SELECT_LIMIT = 9;
    private int selectedSurplus;

    // tool bar
    private Toolbar mToolbar;
    private Button btnImgSelectCtrl;

    // content view
    private RecyclerView rvAlbum;
    private AppAlbumAdapter albumAdapter;
    private HashMap<PictureFolder, List<String>> directoryMap;  // 根据目录分别存放的图片数据
    private ArrayList<String> listAlbum; // 当前相簿中的所有图片数据
    private View albumMaskLayer; // 蒙层，用于在popupWindow显示时的遮罩效果

    // bottom bar
    private View mBottomBar;
    private TextView tvSwitchFolder; // 切换目录

    // PopupWindow
    private PopupWindow mPopupWindow;
    private MaxHeightRecyclerView rvAlbumFolder;
    private AlbumFolderAdapter folderAdapter;
    private ArrayList<PictureFolder> folderList;

    // 当前选择的文件夹
    private PictureFolder mCurrentFolder;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_app_album;
    }

    @Override
    protected void initVariables() {
        selectedSurplus = SELECT_LIMIT - getIntent().getIntExtra(EXTRA_SELECTED_NUM,0);

        albumAdapter = new AppAlbumAdapter(this, listAlbum = new ArrayList<>());
        albumAdapter.setOnItemCheckListener(new AppAlbumAdapter.OnItemCheckListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked, int position) {
                if (albumAdapter.getSelected().size() < selectedSurplus) {
                    if (!albumAdapter.getSelected().contains(listAlbum.get(position))) {
                        buttonView.setButtonDrawable(R.drawable.bg_checkbox_selected);
                        albumAdapter.select(listAlbum.get(position));
                        btnImgSelectCtrl.setText("完成("+albumAdapter.getSelected().size() + "/" + selectedSurplus + ")");
                    } else {
                        buttonView.setButtonDrawable(R.drawable.bg_checkbox_unselected);
                        albumAdapter.unSelect(listAlbum.get(position));
                        if(albumAdapter.getSelected().size() > 0) {
                            btnImgSelectCtrl.setText("完成("+albumAdapter.getSelected().size()  + "/" + selectedSurplus +")");
                        }else{
                            btnImgSelectCtrl.setText("完成");
                        }
                    }

                }else{
                    Toast.makeText(AppAlbumActivity.this,"不能再选择更多照片了",Toast.LENGTH_LONG).show();
                }
            }
        });

        folderAdapter = new AlbumFolderAdapter(this, folderList = new ArrayList<>());
        folderAdapter.setOnItemClickListener(new AlbumFolderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCurrentFolder.setChecked(false);

                PictureFolder folder = folderList.get(position);
                folder.setChecked(true);

                mCurrentFolder = folder;

                folderAdapter.notifyDataSetChanged();
                mPopupWindow.dismiss();

                tvSwitchFolder.setText(mCurrentFolder.getFolderName());
            }
        });
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.tb_app_album);
        mToolbar.setTitle("图片");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true); // home键是否可以点击
        getSupportActionBar().setDisplayShowHomeEnabled(true); // 是否允许home键显示

        btnImgSelectCtrl = (Button) findViewById(R.id.btn_img_select_ctrl);
        btnImgSelectCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnWithResult();
            }
        });

        rvAlbum = (RecyclerView) findViewById(R.id.rv_album);
        rvAlbum.setAdapter(albumAdapter);
        rvAlbum.setLayoutManager(new GridLayoutManager(this, 3));

        albumMaskLayer = findViewById(R.id.view_mask_layer);

        mBottomBar = findViewById(R.id.app_album_bottom_bar);

        tvSwitchFolder = (TextView) findViewById(R.id.tv_switch_folder);
        tvSwitchFolder.setText(DEFAULT_FOLDER);
        tvSwitchFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu();
            }
        });

        mPopupWindow = new PopupWindowManager.Builder(this)
                .setContentView(R.layout.popup_album_folder)
                .setBackgroundDrawable(new ColorDrawable(Color.WHITE))
                .setFocusable(true)
                .setTouchable(true)
                .setOutsideTouchable(true)
                .build();
        mPopupWindow.setAnimationStyle(R.style.popup_window_style);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                albumMaskLayer.setVisibility(View.GONE);

                listAlbum.clear();
                listAlbum.addAll(directoryMap.get(mCurrentFolder));
                albumAdapter.notifyDataSetChanged();
            }
        });

        rvAlbumFolder = (MaxHeightRecyclerView) mPopupWindow.getContentView().findViewById(R.id.rv_album_folders);
        rvAlbumFolder.setMaxHeight(getResources().getDisplayMetrics().heightPixels * 7 / 10);
        rvAlbumFolder.setAdapter(folderAdapter);
        rvAlbumFolder.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void loadData() {
        scanImage();
    }

    private void scanImage() {
        ImageScanner.scanImage(new ImageScanner.OnScanListener() {
            @Override
            public void onScanCompleted(Map<String, List<String>> imageMap) {
                directoryMap = resolveRawMap(imageMap);
                listAlbum.addAll(directoryMap.get(mCurrentFolder));
                albumAdapter.notifyDataSetChanged();
            }
        });
    }

    private HashMap<PictureFolder, List<String>> resolveRawMap(Map<String, List<String>> imageMap) {
        HashMap<PictureFolder, List<String>> result = new HashMap<>();

        Set<String> folderSet = imageMap.keySet();

        for (String folderName : folderSet) {
            List<String> listImage = imageMap.get(folderName);
            PictureFolder folder;
            if (folderName.equals(DEFAULT_FOLDER)) {
                mCurrentFolder = folder = new PictureFolder(folderName, listImage.get(0), listImage.size(), true);
                folderList.add(0, folder);
            } else {
                folder = new PictureFolder(folderName, listImage.get(0), listImage.size(), false);
                folderList.add(folder);
            }
            result.put(folder, listImage);
        }

        imageMap.clear();

        return result;
    }

    private void showPopMenu() {
        albumMaskLayer.setVisibility(View.VISIBLE);
        mPopupWindow.showAtLocation(mBottomBar, Gravity.BOTTOM, 0, mBottomBar.getMeasuredHeight());
    }

    private void returnWithResult() {
        // 将本次选取的图像数据存放进结果集合
        ArrayList<String> listResult = new ArrayList<>();
        listResult.addAll(albumAdapter.getSelected());
        // 构造返回的intent
        Intent intent = new Intent();
        intent.putStringArrayListExtra(MultiSelectionActivity.IMAGE_ADDED_EXTRA, listResult);
        setResult(RESULT_OK, intent);
        // 清空listSelected
        albumAdapter.clearSelected();
        // 返回
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
