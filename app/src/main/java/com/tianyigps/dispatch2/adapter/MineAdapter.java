package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterMineData;

import java.util.List;

/**
 * Created by djc on 2017/7/13.
 */

public class MineAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterMineData> mAdapterMineDataList;

    public MineAdapter(Context context, List<AdapterMineData> mAdapterMineDataList) {
        this.context = context;
        this.mAdapterMineDataList = mAdapterMineDataList;
    }

    @Override
    public int getCount() {
        return mAdapterMineDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAdapterMineDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        AdapterMineData data = mAdapterMineDataList.get(position);

        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_mine, null);
            viewHolder = new ViewHolder();

            viewHolder.imageViewIcon = contentView.findViewById(R.id.iv_item_mine_icon);
            viewHolder.textView = contentView.findViewById(R.id.tv_item_mine_title);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.textView.setText(data.getTitle());
        viewHolder.imageViewIcon.setImageResource(data.getImageid());

        return contentView;
    }

    private class ViewHolder {
        private ImageView imageViewIcon;
        private TextView textView;
    }
}
