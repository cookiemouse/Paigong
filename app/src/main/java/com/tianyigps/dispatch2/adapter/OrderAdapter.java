package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterOrderData;

import java.util.List;

/**
 * Created by djc on 2017/7/13.
 */

public class OrderAdapter extends BaseAdapter {

    private static final String TAG = "OrderAdapter";

    private Context context;
    private List<AdapterOrderData> mAdapterOrderDataList;

    private OnItemListener mOnItemListener;

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
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onMapClick(position);
            }
        });

        viewHolder.imageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onCallClick(position);
            }
        });

        viewHolder.imageViewSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  2017/7/13 签到
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onSignClick(position);
            }
        });

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/11 Item点击事件
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onItemClick(position);
            }
        });

        return contentView;
    }

    private class ViewHolder {
        private TextView textViewName, textViewTime, textViewAddress, textViewId, textViewPhoneName, textViewLine, textViewLineless, textViewContentTitle;

        private ImageView imageViewMap, imageViewCall, imageViewSign;
    }

    public interface OnItemListener {
        void onSignClick(int position);

        void onItemClick(int position);

        void onCallClick(int position);

        void onMapClick(int position);
    }

    public void setOnItemListener(OnItemListener listener) {
        this.mOnItemListener = listener;
    }
}
