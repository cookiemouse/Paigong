package com.tianyigps.xiepeng.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tianyigps.xiepeng.R;

import java.util.List;

/**
 * Created by cookiemouse on 2017/7/24.
 */

public class OperateInstallAdapter extends RecyclerView.Adapter<OperateInstallAdapter.MyViewHolder> {

    private Context context;
    private List<String> mPathList;

    public OperateInstallAdapter(Context context, List<String> pathList) {
        this.context = context;
        this.mPathList = pathList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_operate_install_recycler, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.imageView.setBackgroundResource(R.color.colorAccent);
    }

    @Override
    public int getItemCount() {
        return mPathList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_item_operate_install);
        }
    }
}
