package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterHandingData;

import java.util.List;

/**
 * Created by djc on 2017/7/13.
 */

public class HandingAdapter extends BaseAdapter {

    private static final String TAG = "HandingAdapter";

    private Context context;
    private List<AdapterHandingData> mHandingDataList;

    private OnItemListener mOnItemListener;

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

        final int position = i;

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
            viewHolder.tvRemoveWire = contentView.findViewById(R.id.tv_item_handing_remove_content_wire);
            viewHolder.textViewWireless = contentView.findViewById(R.id.tv_item_handing_content_wireless);
            viewHolder.tvRemoveWireless = contentView.findViewById(R.id.tv_item_handing_remove_content_wireless);
            viewHolder.textViewStatue = contentView.findViewById(R.id.tv_item_handing_status);

            viewHolder.imageViewCall = contentView.findViewById(R.id.iv_item_handing_phone);
            viewHolder.imageViewStart = contentView.findViewById(R.id.iv_item_handing_start);
            viewHolder.llRemove = contentView.findViewById(R.id.ll_item_handing_remove);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.textViewName.setText(data.getName());
        viewHolder.textViewTime.setText(data.getTime());
        viewHolder.textViewAddress.setText(data.getAddress());
        viewHolder.textViewId.setText(data.getId());
        viewHolder.textViewPhoneName.setText(data.getCallName());

        String orderType;
        switch (data.getOrderType()) {
            case 1: {
                orderType = "安装：";
                viewHolder.llRemove.setVisibility(View.GONE);
                break;
            }
            case 2: {
                orderType = "维修：";
                viewHolder.llRemove.setVisibility(View.GONE);
                break;
            }
            case 3: {
                orderType = "安装：";
                viewHolder.llRemove.setVisibility(View.VISIBLE);
                break;
            }
            default: {
                orderType = "安装：";
                Log.i(TAG, "onResponse: default" + data.getOrderType());
            }
        }
        switch (data.getCheckStatus()) {
            case 1: {
                viewHolder.textViewStatue.setVisibility(View.VISIBLE);
                viewHolder.textViewStatue.setText(R.string.pending_audit);
                viewHolder.imageViewCall.setEnabled(false);
                viewHolder.imageViewStart.setEnabled(false);
                break;
            }
            case 2: {
                viewHolder.textViewStatue.setVisibility(View.VISIBLE);
                viewHolder.textViewStatue.setText(R.string.audit_fault);
                viewHolder.imageViewCall.setEnabled(true);
                viewHolder.imageViewStart.setEnabled(true);
                break;
            }
            case 3: {
                viewHolder.textViewStatue.setVisibility(View.GONE);
                viewHolder.textViewStatue.setText("");
                viewHolder.imageViewCall.setEnabled(true);
                viewHolder.imageViewStart.setEnabled(true);
                break;
            }
            default: {
                viewHolder.textViewStatue.setVisibility(View.GONE);
                viewHolder.textViewStatue.setText("");
                viewHolder.imageViewCall.setEnabled(true);
                viewHolder.imageViewStart.setEnabled(true);
                Log.i(TAG, "getView: default" + data.getCheckStatus());
            }
        }
        viewHolder.textViewTitle.setText(orderType);
        viewHolder.textViewWire.setText("" + data.getOnline());
        viewHolder.textViewWireless.setText("" + data.getLineless());
        viewHolder.tvRemoveWire.setText("" + data.getRemoveWireNum());
        viewHolder.tvRemoveWireless.setText("" + data.getRemoveWirelessNum());

        if (data.isModify()) {
            viewHolder.textViewModify.setVisibility(View.VISIBLE);
        } else {
            viewHolder.textViewModify.setVisibility(View.GONE);
        }

        viewHolder.imageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnStartClickListener is null");
                }
                mOnItemListener.onCall(position);
            }
        });

        viewHolder.imageViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/13 开始
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnStartClickListener is null");
                }
                mOnItemListener.onStart(position);
            }
        });

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/11 Item点击事件
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnStartClickListener is null");
                }
                mOnItemListener.onItem(position);
            }
        });

        return contentView;
    }

    private class ViewHolder {
        private TextView textViewName, textViewTime, textViewAddress, textViewId, textViewTitle, textViewPhoneName, textViewModify, textViewWire, textViewWireless, textViewStatue;
        private TextView tvRemoveWire, tvRemoveWireless;
        private ImageView imageViewCall, imageViewStart;
        private LinearLayout llRemove;
    }

    public interface OnItemListener {
        void onStart(int position);

        void onCall(int position);

        void onItem(int position);
    }

    public void setItemListener(OnItemListener listener) {
        this.mOnItemListener = listener;
    }
}
