package me.hwang.picprocessing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.hwang.picprocessing.R;

public class AppAlbumAdapter extends RecyclerView.Adapter<AppAlbumAdapter.ViewHolder> {

    private Context context;
    private List<String> data;
    private List<String> selected;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemCheckListener onItemCheckListener;

    public interface OnItemCheckListener {
        void onCheckedChanged(CompoundButton buttonView, boolean isChecked, int position);
    }

    private OnItemClickListener onItemClickListener;

    public AppAlbumAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        this.selected = new ArrayList<>();
    }

    public void select(String path) {
        selected.add(path);
    }

    public void unSelect(String path) {
        selected.remove(path);
    }

    public List<String> getSelected() {
        return selected;
    }

    public void clearSelected() {
        selected.clear();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_album, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String path = data.get(position);

        boolean isSelected = selected.contains(path);

        Glide.with(context).load(path).into(holder.ivAlbumPic);

        if (isSelected)
            holder.isSelected.setButtonDrawable(R.drawable.bg_checkbox_selected);
        else
            holder.isSelected.setButtonDrawable(R.drawable.bg_checkbox_unselected);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAlbumPic;
        CheckBox isSelected;

        public ViewHolder(View itemView) {
            super(itemView);

            ivAlbumPic = (ImageView) itemView.findViewById(R.id.iv_album_pic);
            ivAlbumPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener == null)
                        return;

                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            });

            isSelected = (CheckBox) itemView.findViewById(R.id.cb_pic_selected);
            isSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (onItemCheckListener == null)
                        return;

                    onItemCheckListener.onCheckedChanged(buttonView, isChecked, getAdapterPosition());
                }
            });
        }
    }
}
