package com.tianyigps.xiepeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.data.AdapterOperateInstallListData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/7/24.
 */

public class OperateInstallListAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterOperateInstallListData> mListDatas;

    public OperateInstallListAdapter(Context context, List<AdapterOperateInstallListData> mListDatas) {
        this.context = context;
        this.mListDatas = mListDatas;
    }

    @Override
    public int getCount() {
        return mListDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mListDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        AdapterOperateInstallListData data = mListDatas.get(position);
        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_operate_install_listview, null);
            viewHolder = new ViewHolder();

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        return contentView;
    }

    private class ViewHolder {
    }
}
