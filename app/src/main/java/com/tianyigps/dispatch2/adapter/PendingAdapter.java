package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterPendingData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.utils.ClipU;
import com.tianyigps.dispatch2.utils.TimeFormatU;
import com.tianyigps.dispatch2.utils.ToastU;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by djc on 2017/7/11.
 */

public class PendingAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterPendingData> mPendingDataList;

    private ToastU mToastU;

    private OnItemListener mOnItemListener;

    public PendingAdapter(Context context, List<AdapterPendingData> mPendingDataList) {
        this.context = context;
        this.mPendingDataList = mPendingDataList;
        mToastU = new ToastU(context);
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
            viewHolder.llInstallComplete = view.findViewById(R.id.ll_item_pending_install_complete);
            viewHolder.llRemoveComplete = view.findViewById(R.id.ll_item_pending_remove_complete);
            viewHolder.tvWireRemove = view.findViewById(R.id.tv_item_pending_remove_content_wire);
            viewHolder.tvWirelessRemove = view.findViewById(R.id.tv_item_pending_remove_content_wireless);
            viewHolder.tvStatus = view.findViewById(R.id.tv_item_pending_order_status);
            viewHolder.tvModifyDate = view.findViewById(R.id.tv_item_pending_modify_date);
            viewHolder.tvModifyReason = view.findViewById(R.id.tv_item_pending_modify_reason);
            viewHolder.tvRed = view.findViewById(R.id.tv_item_pending_red);
            viewHolder.tvBackReason = view.findViewById(R.id.tv_item_pending_back);
            viewHolder.rlContact = view.findViewById(R.id.rl_item_pending_contact);
            viewHolder.tvContactPhone = view.findViewById(R.id.tv_item_pending_rl_contact_phone);
            viewHolder.tvJobNoName = view.findViewById(R.id.tv_item_pending_rl_contact_jobNo_name);
            viewHolder.tvRemoveTitle = view.findViewById(R.id.tv_item_pending_remove_content_title);
            viewHolder.tvWireC = view.findViewById(R.id.tv_item_pending_install_content_wire_complete);
            viewHolder.tvWirelessC = view.findViewById(R.id.tv_item_pending_install_content_wireless_complete);
            viewHolder.tvRemoveWireC = view.findViewById(R.id.tv_item_pending_remove_content_wire_complete);
            viewHolder.tvRemoveWirelessC = view.findViewById(R.id.tv_item_pending_remove_content_wireless_complete);

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
        viewHolder.tvWireC.setText("" + data.getFinishWiredNum());
        viewHolder.tvWirelessC.setText("" + data.getFinishWirelessNum());
        viewHolder.tvRemoveWireC.setText("" + data.getRemoFinWiredNum());
        viewHolder.tvRemoveWirelessC.setText("" + data.getRemoFinWirelessNum());

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
        if (Data.STATUS_6 == data.getOrderStatus()) {
            viewHolder.rlContact.setVisibility(View.VISIBLE);
            String reason = "退回原因：" + data.getBackReason();
            viewHolder.tvBackReason.setText(reason);

            viewHolder.tvBackReason.setVisibility(View.VISIBLE);
            viewHolder.tvContactPhone.setText(data.getPhoneNumber());
            viewHolder.tvJobNoName.setText(data.getJobNo() + " " + data.geteName());
        } else {
            viewHolder.rlContact.setVisibility(View.GONE);
            viewHolder.tvBackReason.setVisibility(View.GONE);
        }

        if (Data.NODE_2 == data.getNode()) {
            viewHolder.tvStatus.setText("待审核");
            viewHolder.llPend.setVisibility(View.GONE);
            viewHolder.tvBackReason.setVisibility(View.GONE);
            viewHolder.llModify.setVisibility(View.VISIBLE);
        } else {
            viewHolder.llPend.setVisibility(View.VISIBLE);
            viewHolder.llModify.setVisibility(View.GONE);
        }

        if (Data.NODE_12 == data.getNode()) {
            viewHolder.llPend.setVisibility(View.GONE);
            viewHolder.tvStatus.setText("待审核");
            viewHolder.tvModifyReason.setVisibility(View.GONE);
            viewHolder.tvModifyDate.setVisibility(View.GONE);
            String reason = "部分完成：" + data.getModifyReason();
            viewHolder.tvBackReason.setText(reason);
            viewHolder.tvBackReason.setVisibility(View.VISIBLE);
            viewHolder.tvContactPhone.setText(data.getPhoneNumber());
            viewHolder.tvJobNoName.setText(data.getJobNo() + " " + data.geteName());
            viewHolder.rlContact.setVisibility(View.VISIBLE);

            viewHolder.llInstallComplete.setVisibility(View.VISIBLE);

            switch (data.getOrderType()) {
                case 1: {
                    orderType = "安装 派单：";
                    viewHolder.llRemoveComplete.setVisibility(View.GONE);
                    break;
                }
                case 2: {
                    orderType = "维修 派单：";
                    viewHolder.llRemoveComplete.setVisibility(View.GONE);
                    break;
                }
                case 3: {
                    orderType = "安装 派单：";
                    viewHolder.tvRemoveTitle.setText("拆除 派单：");
                    viewHolder.llRemoveComplete.setVisibility(View.VISIBLE);
                    break;
                }
                default: {
                    orderType = "安装 派单：";
                    Log.i(TAG, "onSuccess: orderType.default-->" + data.getOrderType());
                }
            }
        }else {
            viewHolder.llPend.setVisibility(View.VISIBLE);
            viewHolder.tvModifyReason.setVisibility(View.VISIBLE);
            viewHolder.tvModifyDate.setVisibility(View.VISIBLE);

            viewHolder.rlContact.setVisibility(View.GONE);
            viewHolder.llInstallComplete.setVisibility(View.GONE);
            viewHolder.llRemoveComplete.setVisibility(View.GONE);
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

        viewHolder.rlContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onContactCall(position);
            }
        });

        viewHolder.tvName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipU.clip(context, data.getName());
                mToastU.showToast("客户名称已成功复制");
                return true;
            }
        });

        viewHolder.tvAddress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipU.clip(context, data.getAddress());
                mToastU.showToast("地址已成功复制");
                return true;
            }
        });

        viewHolder.tvOrderNo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipU.clip(context, data.getOrder());
                mToastU.showToast("订单编号已成功复制");
                return true;
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
        private TextView tvRemoveTitle;
        private TextView tvBackReason;
        private TextView tvWireC, tvWirelessC, tvRemoveWireC, tvRemoveWirelessC;
        private LinearLayout llRemove, llContact, llPend, llModify, llInstallComplete, llRemoveComplete;
        private RelativeLayout rlContact;
        private TextView tvContactPhone, tvJobNoName;
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

        void onContactCall(int position);
    }

    public void setOnItemListener(OnItemListener listener) {
        this.mOnItemListener = listener;
    }
}