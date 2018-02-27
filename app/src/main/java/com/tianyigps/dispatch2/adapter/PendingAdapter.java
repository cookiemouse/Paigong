package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
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

            viewHolder.tvOrderNo = view.findViewById(R.id.tv_item_pending_id);
            viewHolder.tvName = view.findViewById(R.id.tv_item_pending_name);
            viewHolder.tvTime = view.findViewById(R.id.tv_item_pending_time);
            viewHolder.tvAddress = view.findViewById(R.id.tv_item_pending_address);
            viewHolder.tvStatus = view.findViewById(R.id.tv_item_pending_order_status);
            viewHolder.tvContact = view.findViewById(R.id.tv_item_pending_phone_name);
            viewHolder.tvMap = view.findViewById(R.id.tv_item_pending_map);
            viewHolder.tvPend = view.findViewById(R.id.tv_item_pending_pend);

            viewHolder.tvOrderInfo = view.findViewById(R.id.tv_item_pending_order);
            viewHolder.tvPart = view.findViewById(R.id.tv_item_pending_part_finish);
            viewHolder.tvEngineer = view.findViewById(R.id.tv_item_pending_engineer);

            viewHolder.trRefuse = view.findViewById(R.id.tr_item_pending_refuse);
            viewHolder.trEngineer = view.findViewById(R.id.tr_item_pending_engineer);
            viewHolder.trModify = view.findViewById(R.id.tr_item_pending_modify);
            viewHolder.trPart = view.findViewById(R.id.tr_item_pending_part_finish);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvOrderNo.setText(data.getOrder());
        viewHolder.tvName.setText(data.getName());
        viewHolder.tvTime.setText(new TimeFormatU().millisToDate2(data.getTime()));
        viewHolder.tvAddress.setText(data.getAddress());
//        viewHolder.tvWire.setText("" + data.getLineNumber());
//        viewHolder.tvWireless.setText("" + data.getLinelessNumber());
        viewHolder.tvPart.setText(data.getModifyReason());

        if (data.getReviseFlag() == 1) {
            viewHolder.trModify.setVisibility(View.VISIBLE);
        } else {
            viewHolder.trModify.setVisibility(View.GONE);
        }

        if (data.getReviseStatus() == 1) {
            viewHolder.trRefuse.setVisibility(View.VISIBLE);
            viewHolder.trModify.setVisibility(View.GONE);
        } else {
            viewHolder.trRefuse.setVisibility(View.GONE);
        }

        String orderInfo;
        switch (data.getOrderType()) {
            case 1: {
                orderInfo = "安装订单：有线 <font color='#3cabfa'>"
                        + data.getLineNumber() + "</font> 个，无线 <font color='#3cabfa'>"
                        + data.getLinelessNumber() + "</font> 个";
                break;
            }
            case 2: {
                orderInfo = "维修订单：有线 <font color='#3cabfa'>"
                        + data.getLineNumber() + "</font> 个，无线 <font color='#3cabfa'>"
                        + data.getLinelessNumber() + "</font> 个";
                break;
            }
            case 3: {
                orderInfo = "拆除订单：有线 <font color='#3cabfa'>"
                        + data.getWireRemove() + "</font> 个，无线 <font color='#3cabfa'>"
                        + data.getWirelessRemove() + "</font> 个"
                        + "<br>"
                        + "安装订单：有线 <font color='#3cabfa'>"
                        + data.getLineNumber() + "</font> 个，无线 <font color='#3cabfa'>"
                        + data.getLinelessNumber() + "</font> 个";
                break;
            }
            default: {
                orderInfo = "安装订单：有线 <font color='#3cabfa'>"
                        + data.getLineNumber() + "</font> 个，无线 <font color='#3cabfa'>"
                        + data.getLinelessNumber() + "</font> 个";
                Log.i(TAG, "onSuccess: orderType.default-->" + data.getOrderType());
            }
        }

        CharSequence charSequence= Html.fromHtml(orderInfo);
        viewHolder.tvOrderInfo.setText(charSequence);

//        if (Data.STATUS_99 == data.getOrderStatus()) {
//            viewHolder.tvStatus.setText("待审核");
//            viewHolder.llContact.setVisibility(View.GONE);
//        } else {
//            viewHolder.llContact.setVisibility(View.VISIBLE);
//        }
        viewHolder.tvStatus.setText(getOrderStatus(data.getOrderStatus()));
        if (Data.STATUS_6 == data.getOrderStatus()) {
            viewHolder.trEngineer.setVisibility(View.VISIBLE);
            String reason = "退回原因：" + data.getBackReason();
            viewHolder.tvPart.setText(reason);

            viewHolder.trPart.setVisibility(View.VISIBLE);
            viewHolder.tvContact.setText(data.getContactName());
            CharSequence charSequence_call= Html.fromHtml(data.getJobNo() + "&ensp;" + data.geteName()
                    + "&ensp;|&ensp;<font color='#3cabfa'>打电话</font>");
            viewHolder.tvEngineer.setText(charSequence_call);
        } else {
            viewHolder.trEngineer.setVisibility(View.GONE);
            viewHolder.trPart.setVisibility(View.GONE);
        }

        if (Data.NODE_2 == data.getNode()) {
            viewHolder.tvStatus.setText("待审核");
            viewHolder.tvPend.setVisibility(View.GONE);
            viewHolder.trPart.setVisibility(View.GONE);
//            viewHolder.llModify.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvPend.setVisibility(View.VISIBLE);
//            viewHolder.llModify.setVisibility(View.GONE);
        }

        if (Data.NODE_12 == data.getNode()) {
            viewHolder.tvPend.setVisibility(View.GONE);
            viewHolder.tvStatus.setText("待审核");
            viewHolder.trPart.setVisibility(View.GONE);
            String reason = "部分完成：" + data.getModifyReason();
            viewHolder.tvPart.setText(reason);
            viewHolder.trPart.setVisibility(View.VISIBLE);
            viewHolder.tvContact.setText(data.getContactName());
//            <a href="tel:021-60297588">021-60297588</a>
            CharSequence charSequence_call= Html.fromHtml(data.getJobNo() + "&ensp;" + data.geteName()
                    + "&ensp;|&ensp;<font color='#3cabfa'>打电话</font>");
            viewHolder.tvEngineer.setText(charSequence_call);
            viewHolder.trEngineer.setVisibility(View.VISIBLE);

            switch (data.getOrderType()) {
                case 1: {
                    orderInfo = "安装&ensp;派单：有线 <font color='#3cabfa'>"
                            + data.getLineNumber() + "</font> 个，无线 <font color='#3cabfa'>"
                            + data.getLinelessNumber() + "</font> 个"
                            + "<br>"
                            + "&emsp;&emsp;&ensp;"
                            + "完成：有线 <font color='#3cabfa'>"
                            + data.getFinishWiredNum() + "</font> 个，无线 <font color='#3cabfa'>"
                            + data.getFinishWirelessNum() + "</font> 个";
                    break;
                }
                case 2: {
                    orderInfo = "维修&ensp;派单：有线 <font color='#3cabfa'>"
                            + data.getLineNumber() + "</font> 个，无线 <font color='#3cabfa'>"
                            + data.getLinelessNumber() + "</font> 个"
                            + "<br>"
                            + "&emsp;&emsp;&ensp;"
                            + "完成：有线 <font color='#3cabfa'>"
                            + data.getFinishWiredNum() + "</font> 个，无线 <font color='#3cabfa'>"
                            + data.getFinishWirelessNum() + "</font> 个";
                    break;
                }
                case 3: {
                    orderInfo = "拆除&ensp;派单：有线 <font color='#3cabfa'>"
                            + data.getWireRemove() + "</font> 个，无线 <font color='#3cabfa'>"
                            + data.getWirelessRemove() + "</font> 个"
                            + "<br>"
                            + "&emsp;&emsp;&ensp;"
                            + "完成：有线 <font color='#3cabfa'>"
                            + data.getRemoFinWiredNum() + "</font> 个，无线 <font color='#3cabfa'>"
                            + data.getRemoFinWirelessNum() + "</font> 个"
                            + "<br>"
                            + "安装&ensp;派单：有线 <font color='#3cabfa'>"
                            + data.getLineNumber() + "</font> 个，无线 <font color='#3cabfa'>"
                            + data.getLinelessNumber() + "</font> 个"
                            + "<br>"
                            + "&emsp;&emsp;&ensp;"
                            + "完成：有线 <font color='#3cabfa'>"
                            + data.getFinishWiredNum() + "</font> 个，无线 <font color='#3cabfa'>"
                            + data.getFinishWirelessNum() + "</font> 个";
                    break;
                }
                default: {
                    orderInfo = "安装 派单：";
                    Log.i(TAG, "onSuccess: orderType.default-->" + data.getOrderType());
                }
            }
        } else {
            viewHolder.trPart.setVisibility(View.VISIBLE);
        }

        charSequence= Html.fromHtml(orderInfo);
        viewHolder.tvOrderInfo.setText(charSequence);

        viewHolder.tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onCall(position);
            }
        });

        viewHolder.tvEngineer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onContactCall(position);
            }
        });

        viewHolder.tvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onMap(position);
            }
        });

        viewHolder.tvPend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onPend(position);
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
        private TextView tvOrderNo, tvName, tvTime, tvAddress, tvStatus;
        private TextView tvContact, tvEngineer, tvMap, tvPend;
        private TextView tvOrderInfo;
        private TextView tvPart;
        private TableRow trRefuse, trEngineer, trModify, trPart;
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