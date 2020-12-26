package com.example.filetransfer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileListHolder> {

    private List<Map<String, Object>> mList;

    private Context mContext;

    private List<Boolean> select;

    List<String> files;

    public FileListAdapter(List<Map<String, Object>> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        select = new ArrayList<>();
        files = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            select.add(false);
        }
    }

    @NonNull
    @Override
    public FileListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileListHolder(LayoutInflater.from(mContext).inflate(R.layout.file_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FileListHolder holder, final int position) {
        if (select.get(position)) {
            holder.iv_select.setImageResource(R.mipmap.already_select);
        }else {
            holder.iv_select.setImageResource(R.mipmap.no_select);
        }

        final boolean fIsDir = (boolean) mList.get(position).get("fIsDir");
        if (fIsDir) {

            holder.iv_icon.setImageResource(R.mipmap.file_img);
        }else {

            String fileType = (String) mList.get(position).get("fileType");
            if (fileType == null || "".equals(fileType)) {
                holder.iv_icon.setImageResource(R.mipmap.no_name);
            }else if (fileType.equals("mp3")) {
                holder.iv_icon.setImageResource(R.mipmap.music_icon);
            }else if (fileType.equals("mp4")) {
                holder.iv_icon.setImageResource(R.mipmap.video_icon);
            }else {
                holder.iv_icon.setImageResource(R.mipmap.text);
            }

        }
        holder.tv_name.setText(mList.get(position).get("fName")+"");
        holder.tv_content.setText(mList.get(position).get("fInfo") + "");

        holder.iv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select.get(position)) {
                    select.remove(position);
                    select.add(position,false);
                }else {
                    select.remove(position);
                    select.add(position,true);
                }

                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fIsDir) {

                    if (iOnClick != null) {
                        iOnClick.iOnClickJump(mList.get(position).get("fPath") + "");
                    }

                }else {

                    if (select.get(position)) {
                        select.remove(position);
                        select.add(position,false);
                    }else {
                        select.remove(position);
                        select.add(position,true);
                    }

                    notifyDataSetChanged();
                }
            }
        });
    }

    IOnClick iOnClick;

    public void setiOnClick(IOnClick iOnClick) {
        this.iOnClick = iOnClick;
    }

    public interface IOnClick {
        void iOnClickJump(String filePath);
    }

    public List<String> getFiles() {
        files.clear();
        for (int i = 0; i < select.size(); i++) {
            if (select.get(i)) {
                files.add(mList.get(i).get("fPath") + "");
            }
        }
        return files;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class FileListHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_content;
        ImageView iv_icon;
        ImageView iv_select;
        public FileListHolder(@NonNull View itemView) {
            super(itemView);
            iv_select = itemView.findViewById(R.id.iv_select);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }
}
