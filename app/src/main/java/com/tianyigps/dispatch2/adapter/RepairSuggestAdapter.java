package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterOrderTrackData;
import com.tianyigps.dispatch2.data.AdapterRepairSeggestData;
import com.tianyigps.dispatch2.utils.TimeFormatU;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/8.
 */

public class RepairSuggestAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterRepairSeggestData> mDataList;

    public RepairSuggestAdapter(Context context, List<AdapterRepairSeggestData> mDataList) {
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

        AdapterRepairSeggestData data = mDataList.get(position);
        ViewHolder viewHolder = null;
        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_repair_suggest, null);
            viewHolder = new ViewHolder();

            viewHolder.tvCar = contentView.findViewById(R.id.tv_item_repair_suggest_car);
            viewHolder.tvImei = contentView.findViewById(R.id.tv_item_repair_suggest_imei);
            viewHolder.tvSuggest = contentView.findViewById(R.id.tv_item_repair_suggest_content);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.tvCar.setText(data.getCar());
        viewHolder.tvImei.setText(data.getImei());
        viewHolder.tvSuggest.setText(data.getSuggest());

        return contentView;
    }

    private class ViewHolder {
        TextView tvCar, tvImei, tvSuggest;
    }
}
