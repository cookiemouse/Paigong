package com.tianyigps.xiepeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.data.AdapterPendingData;

import java.util.List;

/**
 * Created by djc on 2017/7/11.
 */

public class PendingAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterPendingData> mPendingDataList;

    public PendingAdapter(Context context, List<AdapterPendingData> mPendingDataList) {
        this.context = context;
        this.mPendingDataList = mPendingDataList;
    }

    @Override
    public int getCount() {
        return mPendingDataList.size();
    }

    @Override
    public AdapterPendingData getItem(int i) {
        return mPendingDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final AdapterPendingData data = mPendingDataList.get(i);

        ViewHolder viewHolder = null;

        if (null == view){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.adapter_pending, null);

            viewHolder.imageViewCall = view.findViewById(R.id.iv_adapter_pending_call);
            viewHolder.imageViewLocate = view.findViewById(R.id.iv_adapter_pending_locate);
            viewHolder.textViewOrder = view.findViewById(R.id.tv_adapter_pending_order);
            viewHolder.textViewName = view.findViewById(R.id.tv_adapter_pending_name);
            viewHolder.textViewTime = view.findViewById(R.id.tv_adapter_pending_time);
            viewHolder.textViewAddress = view.findViewById(R.id.tv_adapter_pending_address);
            viewHolder.textViewOrderType = view.findViewById(R.id.tv_adapter_pending_order_type);
            viewHolder.textViewOrderContent = view.findViewById(R.id.tv_adapter_pending_setting_content);
            viewHolder.buttonSign = view.findViewById(R.id.btn_adapter_pending_sign);

            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textViewOrder.setText(data.getOrder());
        viewHolder.textViewName.setText(data.getName());
        viewHolder.textViewTime.setText(data.getTime());
        viewHolder.textViewAddress.setText(data.getAddress());
        viewHolder.textViewOrderType.setText(data.getOrderType());
        viewHolder.textViewOrderContent.setText(data.getLineNumber() + "订单" + data.getLinelessNumber());

        viewHolder.imageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/11 拨打电话
//                data.getPhoneNumber();
            }
        });

        viewHolder.imageViewLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/11 定位
            }
        });

        viewHolder.buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/11 签到
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/11 Item点击事件
            }
        });

        return view;
    }

    private class ViewHolder{
        private ImageView imageViewCall, imageViewLocate;
        private TextView textViewOrder, textViewName, textViewTime
                , textViewAddress, textViewOrderType, textViewOrderContent;
        private TextView buttonSign;
    }
}