package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterLocateWarnData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/12/8.
 */

public class LocateWarnAdapter extends BaseAdapter {

    private Context mContext;
    private List<AdapterLocateWarnData> mLocateWarnDataList;

    public LocateWarnAdapter(Context context, List<AdapterLocateWarnData> mLocateWarnDataList) {
        this.mContext = context;
        this.mLocateWarnDataList = mLocateWarnDataList;
    }

    @Override
    public int getCount() {
        return mLocateWarnDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mLocateWarnDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        AdapterLocateWarnData data = mLocateWarnDataList.get(i);

        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_locate_warn, null);

            viewHolder = new ViewHolder();
            viewHolder.tvType = view.findViewById(R.id.tv_item_locate_warn_type);
            viewHolder.tvTime = view.findViewById(R.id.tv_item_locate_warn_time);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvType.setText(data.getType());
        viewHolder.tvTime.setText(data.getTime());
        if (0 == i) {
            viewHolder.tvType.setTextSize(18);
            viewHolder.tvTime.setTextSize(18);
//            viewHolder.tvType.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//            viewHolder.tvTime.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            viewHolder.tvType.setTextSize(16);
            viewHolder.tvTime.setTextSize(16);
        }

        return view;
    }

    private class ViewHolder {
        TextView tvType;
        TextView tvTime;
    }
}
