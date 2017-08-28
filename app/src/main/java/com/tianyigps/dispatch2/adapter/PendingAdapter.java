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
import com.tianyigps.dispatch2.data.AdapterPendingData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.utils.TimeFormatU;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by djc on 2017/7/11.
 */

public class PendingAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterPendingData> mPendingDataList;

    private OnItemListener mOnItemListener;

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

        final int position = i;

        ViewHolder viewHolder = null;

        if (null == view) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_pending, null);

            viewHolder.imageViewCall = view.findViewById(R.id.iv_item_pending_phone);
            viewHolder.imageViewLocate = view.findViewById(R.id.iv_item_pending_map);
            viewHolder.tvOrderNo = view.findViewById(R.id.tv_item_pending_id);
            viewHolder.tvName = view.findViewById(R.id.tv_item_pending_name);
            viewHolder.tvTime = view.findViewById(R.id.tv_item_pending_time);
            viewHolder.tvAddress = view.findViewById(R.id.tv_item_pending_address);
            viewHolder.tvOrderType = view.findViewById(R.id.tv_item_pending_content_title);
            viewHolder.tvWire = view.findViewById(R.id.tv_item_pending_content_wire);
            viewHolder.tvWireless = view.findViewById(R.id.tv_item_pending_content_wireless);
            viewHolder.imageViewPend = view.findViewById(R.id.iv_item_pending_pend);
            viewHolder.tvWorkerName = view.findViewById(R.id.tv_item_pending_phone_name);
            viewHolder.llRemove = view.findViewById(R.id.ll_item_pending_remove);
            viewHolder.llContact = view.findViewById(R.id.ll_item_pending_contact);
            viewHolder.llPend = view.findViewById(R.id.ll_item_pending_pend);
            viewHolder.llModify = view.findViewById(R.id.ll_item_pending_modify);
            viewHolder.tvWireRemove = view.findViewById(R.id.tv_item_pending_remove_content_wire);
            viewHolder.tvWirelessRemove = view.findViewById(R.id.tv_item_pending_remove_content_wireless);
            viewHolder.tvStatus = view.findViewById(R.id.tv_item_pending_order_status);
            viewHolder.tvModifyDate = view.findViewById(R.id.tv_item_pending_modify_date);
            viewHolder.tvModifyReason = view.findViewById(R.id.tv_item_pending_modify_reason);
            viewHolder.tvRed = view.findViewById(R.id.tv_item_pending_red);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvOrderNo.setText(data.getOrder());
        viewHolder.tvName.setText(data.getName());
        viewHolder.tvTime.setText(new TimeFormatU().millisToDate2(data.getTime()));
        viewHolder.tvAddress.setText(data.getAddress());
        viewHolder.tvWire.setText("" + data.getLineNumber());
        viewHolder.tvWireless.setText("" + data.getLinelessNumber());
        viewHolder.tvWorkerName.setText(data.getContactName());
        viewHolder.tvModifyDate.setText(data.getModifyTime());
        viewHolder.tvModifyReason.setText(data.getModifyReason());

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
                viewHolder.tvWireRemove.setText("" + data.getWireRemove());
                viewHolder.tvWirelessRemove.setText("" + data.getWirelessRemove());
                break;
            }
            default: {
                orderType = "安装：";
                Log.i(TAG, "onSuccess: orderType.default-->" + data.getOrderType());
            }
        }

//        if (Data.STATUS_99 == data.getOrderStatus()) {
//            viewHolder.tvStatus.setText("待审核");
//            viewHolder.llContact.setVisibility(View.GONE);
//        } else {
//            viewHolder.llContact.setVisibility(View.VISIBLE);
//        }
        viewHolder.tvStatus.setText(getOrderStatus(data.getOrderStatus()));
        if (Data.NODE_2 == data.getNode() || Data.NODE_12 == data.getNode()) {
            viewHolder.tvStatus.setText("待审核");
            viewHolder.llPend.setVisibility(View.GONE);
            viewHolder.llModify.setVisibility(View.VISIBLE);
        } else {
            viewHolder.llPend.setVisibility(View.VISIBLE);
            viewHolder.llModify.setVisibility(View.GONE);
        }

        viewHolder.tvOrderType.setText(orderType);

        viewHolder.imageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onCall(position);
            }
        });

        viewHolder.llContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onCall(position);
            }
        });

        viewHolder.imageViewLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onMap(position);
            }
        });

        viewHolder.imageViewPend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onPend(position);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/11 Item点击事件
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onItem(position);
            }
        });

        return view;
    }

    private class ViewHolder {
        private ImageView imageViewCall, imageViewLocate, imageViewPend;
        private TextView tvOrderNo, tvName, tvTime, tvAddress, tvOrderType, tvWire, tvWireless, tvWorkerName, tvStatus;
        private TextView tvWireRemove, tvWirelessRemove;
        private TextView tvModifyDate, tvModifyReason;
        private TextView tvRed;
        private LinearLayout llRemove, llContact, llPend, llModify;
    }

    //  订单状态
    private String getOrderStatus(int status) {
        String orderStatus = "";
        switch (status) {
            case 1: {
                orderStatus = "待派单";
                break;
            }
            case 2: {
                orderStatus = "空单";
                break;
            }
            case 3: {
                orderStatus = "已派单";
                break;
            }
            case 4: {
                orderStatus = "退回客户";
                break;
            }
            case 5: {
                orderStatus = "已取消";
                break;
            }
            case 6: {
                orderStatus = "安装退回";
                break;
            }
            case 7: {
                orderStatus = "已完成";
                break;
            }
            case 98: {
                orderStatus = "改约不通过";
                break;
            }
            case 99: {
                orderStatus = "待审核";
                break;
            }
            default: {
                Log.i(TAG, "getOrderStatus: default-->" + orderStatus);
            }
        }
        return orderStatus;
    }

    public interface OnItemListener {
        void onMap(int position);

        void onCall(int position);

        void onPend(int position);

        void onItem(int position);
    }

    public void setOnItemListener(OnItemListener listener) {
        this.mOnItemListener = listener;
    }
}