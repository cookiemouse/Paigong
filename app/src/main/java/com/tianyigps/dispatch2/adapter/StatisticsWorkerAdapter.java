package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterStatisticsWorkderData;

import java.util.List;

/**
 * Created by djc on 2017/7/14.
 */

public class StatisticsWorkerAdapter extends BaseAdapter {

    private static final String TAG = "StatisticsWorkerAdapter";

    private Context context;
    private List<AdapterStatisticsWorkderData> mStatisticsWorkderDataList;

    public StatisticsWorkerAdapter(Context context, List<AdapterStatisticsWorkderData> mStatisticsWorkderDataList) {
        this.context = context;
        this.mStatisticsWorkderDataList = mStatisticsWorkderDataList;
    }

    @Override
    public int getCount() {
        return mStatisticsWorkderDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStatisticsWorkderDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {

        AdapterStatisticsWorkderData data = mStatisticsWorkderDataList.get(position);
        ViewHolder viewHolder = null;

        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_statistics_list_worker, null);
            viewHolder = new ViewHolder();
            viewHolder.textViewTitle = contentView.findViewById(R.id.tv_item_statistics_type);
            viewHolder.textViewNumber = contentView.findViewById(R.id.tv_item_statistics_number);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.textViewTitle.setText(data.getType());
        viewHolder.textViewNumber.setText("" + data.getNumber());

        return contentView;
    }

    private class ViewHolder {
        private TextView textViewTitle, textViewNumber;
    }
}
