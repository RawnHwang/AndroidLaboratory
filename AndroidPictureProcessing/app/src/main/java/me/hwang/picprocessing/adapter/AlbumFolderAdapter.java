package me.hwang.picprocessing.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.hwang.picprocessing.R;
import me.hwang.picprocessing.bean.PictureFolder;

public class AlbumFolderAdapter extends RecyclerView.Adapter<AlbumFolderAdapter.ViewHolder>{

    private Context context;
    private List<PictureFolder> data;

    public AlbumFolderAdapter(Context context, List<PictureFolder> data) {
        this.context = context;
        this.data = data;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemCheckListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemCheckListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_album_folder,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PictureFolder folder = data.get(position);
        Glide.with(context).load(folder.getCoverImagePath()).into(holder.ivFolderCover);
        holder.tvFolderName.setText(folder.getFolderName());
        holder.tvPictureNum.setText(folder.getPictureNum()+"张");
        holder.rbFolderChecked.setChecked(folder.isChecked());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivFolderCover;
        TextView tvFolderName;
        TextView tvPictureNum;
        RadioButton rbFolderChecked;

        public ViewHolder(View itemView) {
            super(itemView);

            ivFolderCover = (ImageView) itemView.findViewById(R.id.iv_folder_cover);
            tvFolderName = (TextView) itemView.findViewById(R.id.tv_folder_name);
            tvPictureNum = (TextView) itemView.findViewById(R.id.tv_folder_picture_num);
            rbFolderChecked = (RadioButton) itemView.findViewById(R.id.rb_folder_checked);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemCheckListener == null)
                        return;

                    onItemCheckListener.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }
}


