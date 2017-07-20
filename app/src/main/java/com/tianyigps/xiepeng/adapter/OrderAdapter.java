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
import com.tianyigps.xiepeng.data.AdapterOrderData;
import com.tianyigps.xiepeng.data.Data;

import java.util.List;

/**
 * Created by djc on 2017/7/13.
 */

public class OrderAdapter extends BaseAdapter {

    private static final String TAG = "OrderAdapter";

    private Context context;
    private List<AdapterOrderData> mAdapterOrderDataList;

    private OnMapClickListener mOnMapClickListener;
    private OnSignClickListener mOnSignClickListener;

    public OrderAdapter(Context context, List<AdapterOrderData> mAdapterOrderDataList) {
        this.context = context;
        this.mAdapterOrderDataList = mAdapterOrderDataList;
    }

    @Override
    public int getCount() {
        return mAdapterOrderDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAdapterOrderDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View contentView, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        final AdapterOrderData data = mAdapterOrderDataList.get(position);

        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_order, null);

            viewHolder = new ViewHolder();
            viewHolder.textViewName = contentView.findViewById(R.id.tv_item_order_name);
            viewHolder.textViewTime = contentView.findViewById(R.id.tv_item_order_time);
            viewHolder.textViewAddress = contentView.findViewById(R.id.tv_item_order_address);
            viewHolder.textViewId = contentView.findViewById(R.id.tv_item_order_id);
            viewHolder.textViewPhoneName = contentView.findViewById(R.id.tv_item_order_phone_name);
            viewHolder.textViewLine = contentView.findViewById(R.id.tv_item_order_content_wire);
            viewHolder.textViewLineless = contentView.findViewById(R.id.tv_item_order_content_wireless);
            viewHolder.textViewContentTitle = contentView.findViewById(R.id.tv_item_order_content_title);

            viewHolder.imageViewMap = contentView.findViewById(R.id.iv_item_order_map);
            viewHolder.imageViewCall = contentView.findViewById(R.id.iv_item_order_phone);
            viewHolder.imageViewSign = contentView.findViewById(R.id.iv_item_order_sign);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.textViewName.setText(data.getName());
        viewHolder.textViewTime.setText(data.getTime());
        viewHolder.textViewAddress.setText(data.getAddress());
        viewHolder.textViewId.setText(data.getId());
        viewHolder.textViewPhoneName.setText(data.getPhoneName());
        viewHolder.textViewLine.setText(data.getOrderType());
        viewHolder.textViewLine.setText("" + data.getLineNumber());
        viewHolder.textViewLineless.setText("" + data.getLinelessNumber());

        viewHolder.imageViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/13 地图页
                if (null == mOnMapClickListener) {
                    throw new NullPointerException("OnMapClickListener is null");
                }
                mOnMapClickListener.onClick(position);
            }
        });

        viewHolder.imageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + data.getPhoneNumber()));
                context.startActivity(intent);
            }
        });

        viewHolder.imageViewSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  2017/7/13 签到
                if (null == mOnSignClickListener) {
                    throw new NullPointerException("OnSignClickListener is null");
                }
                mOnSignClickListener.onClick(position);
            }
        });

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/11 Item点击事件
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra(Data.DATA_INTENT_ORDER_NO, data.getId());
                context.startActivity(intent);
            }
        });

        return contentView;
    }

    private class ViewHolder {
        private TextView textViewName, textViewTime, textViewAddress, textViewId, textViewPhoneName, textViewLine, textViewLineless, textViewContentTitle;

        private ImageView imageViewMap, imageViewCall, imageViewSign;
    }

    public interface OnMapClickListener {
        void onClick(int position);
    }

    public void setOnMapClickListener(OnMapClickListener listener) {
        this.mOnMapClickListener = listener;
    }

    public interface OnSignClickListener {
        void onClick(int position);
    }

    public void setOnSignClickListener(OnSignClickListener listener) {
        this.mOnSignClickListener = listener;
    }
}
