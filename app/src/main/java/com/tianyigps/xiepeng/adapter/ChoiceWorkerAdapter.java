package com.tianyigps.xiepeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.data.AdapterChoiceWorkerData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public class ChoiceWorkerAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterChoiceWorkerData> mDataList;

    public ChoiceWorkerAdapter(Context context, List<AdapterChoiceWorkerData> mDataList) {
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

        AdapterChoiceWorkerData data = mDataList.get(position);
        ViewHolder viewHolder = null;
        if (null == contentView) {
            viewHolder = new ViewHolder();
            contentView = LayoutInflater.from(context).inflate(R.layout.item_choice_worker, null);

            viewHolder.tvId = contentView.findViewById(R.id.tv_item_choice_worker_id);
            viewHolder.tvName = contentView.findViewById(R.id.tv_item_choice_worker_name);
            viewHolder.tvArea = contentView.findViewById(R.id.tv_item_choice_worker_area);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.tvId.setText(data.getId());
        viewHolder.tvName.setText(data.getName());
        viewHolder.tvArea.setText(data.getArea());

        if (data.isSelect()) {
            viewHolder.tvId.setTextSize(16);
            viewHolder.tvName.setTextSize(16);
            viewHolder.tvArea.setTextSize(16);
            contentView.setBackgroundColor(context.getResources().getColor(R.color.colorBlueDis));
        } else {
            viewHolder.tvId.setTextSize(14);
            viewHolder.tvName.setTextSize(14);
            viewHolder.tvArea.setTextSize(14);
            contentView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

        return contentView;
    }

    private class ViewHolder {
        TextView tvId, tvName, tvArea;
    }
}
