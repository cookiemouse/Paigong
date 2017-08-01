package com.tianyigps.xiepeng.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.data.AdapterOperateInstallRecyclerData;

import java.io.File;
import java.util.List;

/**
 * Created by cookiemouse on 2017/7/24.
 */

public class OperateInstallAdapter extends RecyclerView.Adapter<OperateInstallAdapter.MyViewHolder> {

    private Context context;
    private List<AdapterOperateInstallRecyclerData> mDataList;

    private OnItemOperateListener mOnItemOperateListener;

    public OperateInstallAdapter(Context context, List<AdapterOperateInstallRecyclerData> dataList) {
        this.context = context;
        this.mDataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_operate_install_recycler, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int positionFinal = position;

        String path = mDataList.get(position).getPath();
        if (null == path) {
            holder.imageViewDelete.setVisibility(View.GONE);
            Picasso.with(context)
                    .load(R.drawable.ic_add_pic)
                    .fit()
                    .centerInside()
                    .into(holder.imageViewPic);
        } else {
            Picasso.with(context)
                    .load(new File(path))
                    .fit()
                    .centerInside()
                    .error(R.drawable.ic_camera)
                    .into(holder.imageViewPic);
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        }

        holder.imageViewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onPicClick(positionFinal);
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onDeleteClick(positionFinal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewPic, imageViewDelete;

        MyViewHolder(View itemView) {
            super(itemView);
            imageViewPic = itemView.findViewById(R.id.iv_item_operate_install);
            imageViewDelete = itemView.findViewById(R.id.iv_item_operate_install_delete);
        }
    }

    public interface OnItemOperateListener {
        void onDeleteClick(int position);

        void onPicClick(int position);
    }

    public void setOnItemOperateListener(OnItemOperateListener listener) {
        this.mOnItemOperateListener = listener;
    }
}
