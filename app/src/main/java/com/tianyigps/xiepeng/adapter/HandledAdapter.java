package com.tianyigps.xiepeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.data.AdapterHandledData;

import java.util.List;

/**
 * Created by djc on 2017/7/13.
 */

public class HandledAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterHandledData> mHandledDataList;

    public HandledAdapter(Context context, List<AdapterHandledData> mHandledDataList) {
        this.context = context;
        this.mHandledDataList = mHandledDataList;
    }

    @Override
    public int getCount() {
        return mHandledDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHandledDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        AdapterHandledData data = mHandledDataList.get(position);

        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_handled, null);

            viewHolder = new ViewHolder();
            viewHolder.textViewName = contentView.findViewById(R.id.tv_item_handled_name);
            viewHolder.textViewTime = contentView.findViewById(R.id.tv_item_handled_time);
            viewHolder.textViewAddress = contentView.findViewById(R.id.tv_item_handled_address);
            viewHolder.textViewId = contentView.findViewById(R.id.tv_item_handled_id);
            viewHolder.textViewNumber = contentView.findViewById(R.id.tv_item_handled_number);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.textViewName.setText(data.getName());
        viewHolder.textViewTime.setText(data.getTime());
        viewHolder.textViewAddress.setText(data.getAddress());
        viewHolder.textViewId.setText(data.getId());
        viewHolder.textViewNumber.setText("安装：有线" + data.getOnline() + "个，无线" + data.getLineLess());

        return contentView;
    }

    private class ViewHolder {
        private TextView textViewName, textViewTime, textViewAddress, textViewId, textViewNumber;
    }
}
