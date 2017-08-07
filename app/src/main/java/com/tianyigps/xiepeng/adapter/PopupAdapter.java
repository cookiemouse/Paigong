package com.tianyigps.xiepeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.data.AdapterPopupData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public class PopupAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterPopupData> mDataList;

    public PopupAdapter(Context context, List<AdapterPopupData> mDataList) {
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

        AdapterPopupData data = mDataList.get(position);

        ViewHolder viewHolder = null;
        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_popup, null);
            viewHolder = new ViewHolder();

            viewHolder.tvTitle = contentView.findViewById(R.id.tv_item_popup_title);
            viewHolder.tvCount = contentView.findViewById(R.id.tv_item_popup_count);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.tvTitle.setText(data.getTitle());
        String count = "" + data.getCount();
        viewHolder.tvCount.setText(count);

        return contentView;
    }

    private class ViewHolder {
        TextView tvTitle;
        TextView tvCount;
    }
}
