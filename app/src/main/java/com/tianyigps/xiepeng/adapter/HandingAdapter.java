package com.tianyigps.xiepeng.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.data.AdapterHandingData;

import java.util.List;

/**
 * Created by djc on 2017/7/13.
 */

public class HandingAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterHandingData> mHandingDataList;

    public HandingAdapter(Context context, List<AdapterHandingData> mHandingDataList) {
        this.context = context;
        this.mHandingDataList = mHandingDataList;
    }

    @Override
    public int getCount() {
        return mHandingDataList.size();
    }

    @Override
    public AdapterHandingData getItem(int i) {
        return mHandingDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View contentView, ViewGroup viewGroup) {

        final AdapterHandingData data = mHandingDataList.get(i);

        ViewHolder viewHolder = null;
        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_handing, null);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = contentView.findViewById(R.id.tv_item_handing_name);
            viewHolder.textViewTime = contentView.findViewById(R.id.tv_item_handing_time);
            viewHolder.textViewAddress = contentView.findViewById(R.id.tv_item_handing_address);
            viewHolder.textViewId = contentView.findViewById(R.id.tv_item_handing_id);
            viewHolder.textViewNumber = contentView.findViewById(R.id.tv_item_handing_number);
            viewHolder.textViewPhoneName = contentView.findViewById(R.id.tv_item_handing_phone_name);

            viewHolder.imageViewCall = contentView.findViewById(R.id.iv_item_handing_phone);
            viewHolder.imageViewStart = contentView.findViewById(R.id.iv_item_handing_start);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.textViewName.setText(data.getName());
        viewHolder.textViewTime.setText(data.getTime());
        viewHolder.textViewAddress.setText(data.getAddress());
        viewHolder.textViewId.setText(data.getId());
        viewHolder.textViewNumber.setText("安装：有线" + data.getOnline() + "个，无线" + data.getLineless() + "个");
        viewHolder.textViewPhoneName.setText(data.getCallName());

        viewHolder.imageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                data.getCallNumber();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "123456789"));
                context.startActivity(intent);
            }
        });

        viewHolder.imageViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/13 开始
            }
        });

        return contentView;
    }

    private class ViewHolder {
        private TextView textViewName, textViewTime, textViewAddress, textViewId, textViewNumber, textViewPhoneName;
        private ImageView imageViewCall, imageViewStart;
    }
}
