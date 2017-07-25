package com.tianyigps.xiepeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.data.AdapterRepairData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/7/25.
 */

public class RepairAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterRepairData> mDataList;

    public RepairAdapter(Context context, List<AdapterRepairData> mDataList) {
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

        AdapterRepairData data = mDataList.get(position);
        ViewHolder viewHolder = null;

        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_repair, null);
            viewHolder = new ViewHolder();

            viewHolder.textViewType = contentView.findViewById(R.id.tv_item_repair_device_type);
            viewHolder.textViewId = contentView.findViewById(R.id.tv_item_repair_device_id);
            viewHolder.textViewName = contentView.findViewById(R.id.tv_item_repair_device_name);
            viewHolder.textViewCarNo = contentView.findViewById(R.id.tv_item_repair_car_no);
            viewHolder.textViewFrameNo = contentView.findViewById(R.id.tv_item_repair_frame_no);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }
        viewHolder.textViewType.setText(data.getType());
        viewHolder.textViewId.setText(data.getId());
        viewHolder.textViewName.setText(data.getName());
        viewHolder.textViewCarNo.setText(data.getCarNo());
        viewHolder.textViewFrameNo.setText(data.getFrameNo());

        return contentView;
    }

    private class ViewHolder {
        private TextView textViewType, textViewId, textViewName, textViewCarNo, textViewFrameNo;
    }
}
