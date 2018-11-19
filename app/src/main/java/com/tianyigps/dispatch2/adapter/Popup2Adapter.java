package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.Adapter2PopupData;
import com.tianyigps.dispatch2.data.AdapterPopupData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public class Popup2Adapter extends BaseAdapter {

    private Context context;
    private List<Adapter2PopupData> mDataList;

    public Popup2Adapter(Context context, List<Adapter2PopupData> mDataList) {
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

        Adapter2PopupData data = mDataList.get(position);

        ViewHolder viewHolder = null;
        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_popup, null);
            viewHolder = new ViewHolder();

            viewHolder.tvTitle = contentView.findViewById(R.id.tv_item_popup_title);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.tvTitle.setText(data.getName());

        return contentView;
    }

    private class ViewHolder {
        TextView tvTitle;
    }
}
