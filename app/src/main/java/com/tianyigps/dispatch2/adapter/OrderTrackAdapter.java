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
import com.tianyigps.dispatch2.utils.TimeFormatU;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/8.
 */

public class OrderTrackAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterOrderTrackData> mDataList;

    public OrderTrackAdapter(Context context, List<AdapterOrderTrackData> mDataList) {
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

        AdapterOrderTrackData data = mDataList.get(position);
        ViewHolder viewHolder = null;
        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_order_track, null);
            viewHolder = new ViewHolder();

            viewHolder.ivLeft = contentView.findViewById(R.id.iv_item_order_track);
            viewHolder.tvTime = contentView.findViewById(R.id.tv_item_order_track_time);
            viewHolder.tvInfo = contentView.findViewById(R.id.tv_item_order_track_info);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        String time = new TimeFormatU().millisToDate2(data.getTime());
        viewHolder.tvTime.setText(time);
        viewHolder.tvInfo.setText(data.getInfo());

        if (position == 0) {
            int green = context.getResources().getColor(R.color.colorBlueTheme);
            viewHolder.ivLeft.setImageResource(R.drawable.ic_cycle_green);
            viewHolder.tvTime.setTextColor(green);
            viewHolder.tvInfo.setTextColor(green);
        } else {
            int gray = context.getResources().getColor(R.color.colorGray);
            viewHolder.ivLeft.setImageResource(R.drawable.ic_cycle_gray);
            viewHolder.tvTime.setTextColor(gray);
            viewHolder.tvInfo.setTextColor(gray);
        }
        return contentView;
    }

    private class ViewHolder {
        ImageView ivLeft;
        TextView tvTime, tvInfo;
    }
}
