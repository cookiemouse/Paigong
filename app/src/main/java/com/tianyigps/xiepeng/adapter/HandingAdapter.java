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
import com.tianyigps.xiepeng.activity.OrderDetailsActivity;
import com.tianyigps.xiepeng.data.AdapterHandingData;
import com.tianyigps.xiepeng.data.Data;

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

        final String orderNo = data.getId();

        ViewHolder viewHolder = null;
        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_handing, null);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = contentView.findViewById(R.id.tv_item_handing_name);
            viewHolder.textViewTime = contentView.findViewById(R.id.tv_item_handing_time);
            viewHolder.textViewAddress = contentView.findViewById(R.id.tv_item_handing_address);
            viewHolder.textViewId = contentView.findViewById(R.id.tv_item_handing_id);
            viewHolder.textViewPhoneName = contentView.findViewById(R.id.tv_item_handing_phone_name);
            viewHolder.textViewModify = contentView.findViewById(R.id.tv_item_handing_modify);
            viewHolder.textViewTitle = contentView.findViewById(R.id.tv_item_handing_content_title);
            viewHolder.textViewWire = contentView.findViewById(R.id.tv_item_handing_content_wire);
            viewHolder.textViewWireless = contentView.findViewById(R.id.tv_item_handing_content_wireless);

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
        viewHolder.textViewPhoneName.setText(data.getCallName());
        viewHolder.textViewTitle.setText(data.getOrderType());
        viewHolder.textViewWire.setText("" + data.getOnline());
        viewHolder.textViewWireless.setText("" + data.getLineless());

        if (data.isModify()) {
            viewHolder.textViewModify.setVisibility(View.VISIBLE);
        } else {
            viewHolder.textViewModify.setVisibility(View.GONE);
        }

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

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/11 Item点击事件
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra(Data.DATA_INTENT_ORDER_NO, orderNo);
                context.startActivity(intent);
            }
        });

        return contentView;
    }

    private class ViewHolder {
        private TextView textViewName, textViewTime, textViewAddress
                , textViewId, textViewTitle, textViewPhoneName
                , textViewModify, textViewWire, textViewWireless;
        private ImageView imageViewCall, imageViewStart;
    }
}
