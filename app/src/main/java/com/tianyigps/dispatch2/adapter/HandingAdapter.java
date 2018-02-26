package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterHandingData;
import com.tianyigps.dispatch2.utils.ClipU;
import com.tianyigps.dispatch2.utils.TimeFormatU;
import com.tianyigps.dispatch2.utils.ToastU;

import java.util.List;

/**
 * Created by djc on 2017/7/13.
 */

public class HandingAdapter extends BaseAdapter {

    private static final String TAG = "HandingAdapter";

    private Context context;
    private List<AdapterHandingData> mHandingDataList;
    private ToastU mToastU;

    private OnItemListener mOnItemListener;

    public HandingAdapter(Context context, List<AdapterHandingData> mHandingDataList) {
        this.context = context;
        this.mHandingDataList = mHandingDataList;
        mToastU = new ToastU(context);
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
            viewHolder.tvStart = contentView.findViewById(R.id.tv_item_handing_start);

            viewHolder.llRemove = contentView.findViewById(R.id.ll_item_handing_remove);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.textViewName.setText(data.getName());
        viewHolder.textViewTime.setText(new TimeFormatU().millisToDate2(data.getTime()));
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
                viewHolder.textViewPhoneName.setEnabled(false);
                viewHolder.tvStart.setEnabled(false);
                viewHolder.tvStart.setTextColor(context.getResources().getColor(R.color.colorGray));
                break;
            }
            case 2: {
                viewHolder.textViewStatue.setVisibility(View.VISIBLE);
                viewHolder.textViewStatue.setText(R.string.audit_fault);
                viewHolder.textViewPhoneName.setEnabled(true);
                viewHolder.tvStart.setEnabled(true);
                viewHolder.tvStart.setTextColor(context.getResources().getColor(R.color.colorBlueTheme));
                break;
            }
            case 3: {
                viewHolder.textViewStatue.setVisibility(View.GONE);
                viewHolder.textViewStatue.setText("");
                viewHolder.textViewPhoneName.setEnabled(true);
                viewHolder.tvStart.setEnabled(true);
                viewHolder.tvStart.setTextColor(context.getResources().getColor(R.color.colorBlueTheme));
                break;
            }
            default: {
                viewHolder.textViewStatue.setVisibility(View.GONE);
                viewHolder.textViewStatue.setText("");
                viewHolder.textViewPhoneName.setEnabled(true);
                viewHolder.tvStart.setEnabled(true);
                viewHolder.tvStart.setTextColor(context.getResources().getColor(R.color.colorBlueTheme));
                Log.i(TAG, "getView: default" + data.getCheckStatus());
            }
        }
        viewHolder.textViewTitle.setText(orderType);
        viewHolder.textViewWire.setText("" + data.getOnline());
        viewHolder.textViewWireless.setText("" + data.getLineless());
        viewHolder.tvRemoveWire.setText("" + data.getRemoveWireNum());
        viewHolder.tvRemoveWireless.setText("" + data.getRemoveWirelessNum());

        if (data.isModify()) {
            viewHolder.textViewModify.setText("（改）");
            viewHolder.textViewModify.setVisibility(View.VISIBLE);
        } else {
            viewHolder.textViewModify.setVisibility(View.GONE);
        }

        viewHolder.textViewPhoneName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnStartClickListener is null");
                }
                mOnItemListener.onCall(position);
            }
        });

        viewHolder.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/13 开始
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnStartClickListener is null");
                }
                mOnItemListener.onStart(position);
            }
        });

        viewHolder.textViewName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipU.clip(context, data.getName());
                mToastU.showToast("客户名称已成功复制");
                return true;
            }
        });

        viewHolder.textViewAddress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipU.clip(context, data.getAddress());
                mToastU.showToast("地址已成功复制");
                return true;
            }
        });

        viewHolder.textViewId.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipU.clip(context, data.getId());
                mToastU.showToast("订单编号已成功复制");
                return true;
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
        private TextView textViewName, textViewTime, textViewAddress, textViewId, textViewTitle, textViewPhoneName
                , textViewModify, textViewWire, textViewWireless, textViewStatue, tvStart;
        private TextView tvRemoveWire, tvRemoveWireless;
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
