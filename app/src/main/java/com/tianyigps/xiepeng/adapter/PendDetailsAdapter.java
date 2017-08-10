package com.tianyigps.xiepeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.data.AdapterPendDetailsData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/10.
 */

public class PendDetailsAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterPendDetailsData> mDataList;

    public PendDetailsAdapter(Context context, List<AdapterPendDetailsData> mDataList) {
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
    public boolean isEnabled(int position) {
//        return super.isEnabled(position);
        return false;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {

        AdapterPendDetailsData data = mDataList.get(position);
        ViewHolder viewHolder = null;
        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_pend_details, null);
            viewHolder = new ViewHolder();

            viewHolder.ivIcon = contentView.findViewById(R.id.iv_item_pend_details);
            viewHolder.tvTitle = contentView.findViewById(R.id.tv_item_pend_details_title);
            viewHolder.tvContent = contentView.findViewById(R.id.tv_item_pend_details_content);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.ivIcon.setImageResource(data.getImageResources());
        viewHolder.tvTitle.setText(data.getTitle());
        viewHolder.tvContent.setText(data.getContent());

        if (data.isRed()) {
            viewHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.colorRed));
        } else {
            viewHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.colorBlueTheme));
        }
        
        return contentView;
    }

    private class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle, tvContent;
    }
}
