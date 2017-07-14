package com.tianyigps.xiepeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.data.AdapterStatisticsManagerData;

import java.util.List;

/**
 * Created by djc on 2017/7/14.
 */

public class StatisticsManagerAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterStatisticsManagerData> mAdapterStatisticsManagerDataList;

    public StatisticsManagerAdapter(Context context, List<AdapterStatisticsManagerData> adapterStatisticsManagerData) {
        this.context = context;
        this.mAdapterStatisticsManagerDataList = adapterStatisticsManagerData;
    }

    @Override
    public int getCount() {
        return mAdapterStatisticsManagerDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAdapterStatisticsManagerDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {

        AdapterStatisticsManagerData data = mAdapterStatisticsManagerDataList.get(position);

        ViewHolder viewHolder = null;

        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_statistics_list_manager, null);

            viewHolder = new ViewHolder();

            viewHolder.textViewName = contentView.findViewById(R.id.tv_item_statistics_name);
            viewHolder.textViewDoor = contentView.findViewById(R.id.tv_item_statistics_door);
            viewHolder.textViewCarNamber = contentView.findViewById(R.id.tv_item_statistics_car_number);
            viewHolder.textViewOnline = contentView.findViewById(R.id.tv_item_statistics_online);
            viewHolder.textViewOffline = contentView.findViewById(R.id.tv_item_statistics_offline);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.textViewName.setText(data.getName());
        viewHolder.textViewDoor.setText("" + data.getDoor());
        viewHolder.textViewCarNamber.setText("" + data.getCarNumber());
        viewHolder.textViewOnline.setText("" + data.getOnline());
        viewHolder.textViewOffline.setText("" + data.getOffline());

        return contentView;
    }

    private class ViewHolder {
        private TextView textViewName, textViewDoor, textViewCarNamber, textViewOnline, textViewOffline;
    }
}
