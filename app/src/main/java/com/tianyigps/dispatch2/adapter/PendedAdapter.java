package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterPendedData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.utils.ClipU;
import com.tianyigps.dispatch2.utils.TimeFormatU;
import com.tianyigps.dispatch2.utils.ToastU;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public class PendedAdapter extends BaseAdapter {

    private static final String TAG = "PendedAdapter";

    private Context context;
    private List<AdapterPendedData> mDataList;

    private OnItemListener mOnItemListener;

    private ToastU mToastU;

    public PendedAdapter(Context context, List<AdapterPendedData> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
        mToastU = new ToastU(context);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View contentView, ViewGroup viewGroup) {


        final AdapterPendedData data = mDataList.get(position);
        ViewHolder viewHolder = null;
        if (null == contentView) {
            viewHolder = new ViewHolder();
            contentView = LayoutInflater.from(context).inflate(R.layout.item_pended, null);

            viewHolder.tvTitle = contentView.findViewById(R.id.tv_item_pended_title);
            viewHolder.tvTime = contentView.findViewById(R.id.tv_item_pended_time);
            viewHolder.tvAddress = contentView.findViewById(R.id.tv_item_pended_address);
            viewHolder.tvMap = contentView.findViewById(R.id.tv_item_pended_map);
            viewHolder.tvWorker = contentView.findViewById(R.id.tv_item_pended_phone_worker_name);
            viewHolder.tvContactName = contentView.findViewById(R.id.tv_item_pended_contact);
            viewHolder.tvStatus = contentView.findViewById(R.id.tv_item_pended_order_status);
            viewHolder.tvModify = contentView.findViewById(R.id.tv_item_pended_modify);
            viewHolder.tvOrderNo = contentView.findViewById(R.id.tv_item_pended_id);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        int orderStatus = data.getOrderStatus();
        if (Data.STATUS_2 == orderStatus
                || Data.STATUS_4 == orderStatus
                || Data.STATUS_5 == orderStatus) {
            viewHolder.tvWorker.setVisibility(View.GONE);
        } else {
            viewHolder.tvWorker.setVisibility(View.VISIBLE);
        }
        viewHolder.tvStatus.setText(getOrderStatus(data.getOrderStatus()));

        viewHolder.tvTitle.setText(data.getTitle());
        viewHolder.tvTime.setText(new TimeFormatU().millisToDate2(data.getTime()));
        viewHolder.tvAddress.setText(data.getAddress());
        viewHolder.tvWorker.setText(data.getWorker());
        viewHolder.tvContactName.setText(data.getContact());
        viewHolder.tvOrderNo.setText(data.getOrderNo());
        if (data.getReviseFlag() == 1) {
            viewHolder.tvModify.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvModify.setVisibility(View.GONE);
        }

        viewHolder.tvContactName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onContact(position);
            }
        });

        viewHolder.tvWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onCall(position);
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

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onItem(position);
            }
        });

        viewHolder.tvOrderNo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipU.clip(context, data.getOrderNo());
                mToastU.showToast("订单编号已成功复制");
                return true;
            }
        });

        return contentView;
    }

    private class ViewHolder {
        TextView tvTitle, tvTime, tvAddress, tvWorker, tvContactName, tvStatus, tvModify;
        TextView tvMap;
        TextView tvOrderNo;
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

        void onContact(int position);

        void onCall(int position);

        void onItem(int position);
    }

    public void setOnItemListener(OnItemListener listener) {
        this.mOnItemListener = listener;
    }
}
