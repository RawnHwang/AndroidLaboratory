package me.hwang.picprocessing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import me.hwang.picprocessing.R;


public class PictureAddedAdapter extends BaseAdapter {

    private Context context;
    // 图像文件路径数据
    private List<String> data;
    // 用于存放复用的convertView的map,避免因position = 0的重复加载问题导致图片错乱
    private HashMap<Integer, View> viewMap;
    // 是否显示添加按钮
    private boolean isPermitAdd = true;

    public PictureAddedAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        this.viewMap = new HashMap<>();
    }

    public void permitAdd(boolean yesNo){
        isPermitAdd = yesNo;
    }
    // size + 1 是因为在末尾固定存在一个添加按钮
    @Override
    public int getCount() {
        if (isPermitAdd)
            return data.size() + 1;
        else
            return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView ivPictureAdded;

        if (!viewMap.containsKey(position)) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_picture_added, null);
            ivPictureAdded = (ImageView) convertView.findViewById(R.id.iv_picture_added);
            convertView.setTag(ivPictureAdded);
            viewMap.put(position, convertView);
        } else {
            convertView = viewMap.get(position);
            ivPictureAdded = (ImageView) convertView.getTag();
        }
        if (position == data.size()) {
            // 末尾的添加按钮
            ivPictureAdded.setBackgroundResource(R.drawable.ic_add_more_picture);
        } else {
            Glide.with(context).load(data.get(position)).into(ivPictureAdded);
        }

        return convertView;
    }
}
