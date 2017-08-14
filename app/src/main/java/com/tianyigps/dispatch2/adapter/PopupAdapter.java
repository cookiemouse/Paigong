package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterPopupData;

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

        String type;
        switch (data.getOrderStatus()) {
            case 1: {
                type = "待派单";
                break;
            }
            case 2: {
                type = "空单";
                break;
            }
            case 3: {
                type = "已派单";
                break;
            }
            case 4: {
                type = "退回客户";
                break;
            }
            case 5: {
                type = "已取消";
                break;
            }
            case 6: {
                type = "安装退回";
                break;
            }
            case 7: {
                type = "已完成";
                break;
            }
            case 98: {
                type = "改约不通过";
                break;
            }
            case 99: {
                type = "待审核";
                break;
            }
            default: {
                type = "未知";
            }
        }

        viewHolder.tvTitle.setText(type);
        String count = "" + data.getCount();
        viewHolder.tvCount.setText(count);

        return contentView;
    }

    private class ViewHolder {
        TextView tvTitle;
        TextView tvCount;
    }
}
