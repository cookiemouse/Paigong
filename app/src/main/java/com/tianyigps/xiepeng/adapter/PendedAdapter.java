package com.tianyigps.xiepeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.data.AdapterPendedData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public class PendedAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterPendedData> mDataList;

    public PendedAdapter(Context context, List<AdapterPendedData> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {

        AdapterPendedData data = mDataList.get(position);
        ViewHolder viewHolder = null;
        if (null == contentView) {
            viewHolder = new ViewHolder();
            contentView = LayoutInflater.from(context).inflate(R.layout.item_pended, null);

            viewHolder.tvTitle = contentView.findViewById(R.id.tv_item_pended_title);
            viewHolder.tvTime = contentView.findViewById(R.id.tv_item_pended_time);
            viewHolder.tvAddress = contentView.findViewById(R.id.tv_item_pended_address);
            viewHolder.tvContactName = contentView.findViewById(R.id.tv_item_pended_custom);
            viewHolder.tvWorker = contentView.findViewById(R.id.tv_item_pended_phone_worker_name);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.tvTitle.setText(data.getTitle());
        viewHolder.tvTime.setText(data.getTime());
        viewHolder.tvAddress.setText(data.getAddress());
        viewHolder.tvWorker.setText(data.getWorker());
        viewHolder.tvContactName.setText(data.getContactName());

        return contentView;
    }

    private class ViewHolder {
        TextView tvTitle, tvTime, tvAddress, tvWorker, tvContactName;
    }
}
