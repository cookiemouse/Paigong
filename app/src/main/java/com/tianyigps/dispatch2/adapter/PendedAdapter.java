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
import com.tianyigps.dispatch2.data.AdapterPendedData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.utils.TimeFormatU;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public class PendedAdapter extends BaseAdapter {

    private static final String TAG = "PendedAdapter";

    private Context context;
    private List<AdapterPendedData> mDataList;

    private OnItemListener mOnItemListener;

    public PendedAdapter(Context context, List<AdapterPendedData> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
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
            viewHolder.tvWorker = contentView.findViewById(R.id.tv_item_pended_phone_worker_name);
            viewHolder.tvContactName = contentView.findViewById(R.id.tv_item_pended_contact);
            viewHolder.tvStatus = contentView.findViewById(R.id.tv_item_pended_order_status);
            viewHolder.ivPhone = contentView.findViewById(R.id.iv_item_pended_phone);
            viewHolder.ivContact = contentView.findViewById(R.id.iv_item_pended_contact);
            viewHolder.ivMap = contentView.findViewById(R.id.iv_item_pended_map);
            viewHolder.llWorker = contentView.findViewById(R.id.ll_item_pended_worker);
            viewHolder.llContact = contentView.findViewById(R.id.ll_item_pended_contact);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        int orderStatus = data.getOrderStatus();
        if (Data.STATUS_2 == orderStatus
                || Data.STATUS_4 == orderStatus
                || Data.STATUS_5 == orderStatus) {
            viewHolder.llWorker.setVisibility(View.GONE);
        } else {
            viewHolder.llWorker.setVisibility(View.VISIBLE);
        }
        viewHolder.tvStatus.setText(getOrderStatus(data.getOrderStatus()));

        viewHolder.tvTitle.setText(data.getTitle());
        viewHolder.tvTime.setText(new TimeFormatU().millisToDate(data.getTime()));
        viewHolder.tvAddress.setText(data.getAddress());
        viewHolder.tvWorker.setText(data.getWorker());
        viewHolder.tvContactName.setText(data.getContact());

        viewHolder.ivPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onCall(position);
            }
        });

        viewHolder.ivContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onContact(position);
            }
        });

        viewHolder.llWorker.setOnClickListener(new View.OnClickListener() {
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
                mOnItemListener.onContact(position);
            }
        });

        viewHolder.ivMap.setOnClickListener(new View.OnClickListener() {
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

        return contentView;
    }

    private class ViewHolder {
        TextView tvTitle, tvTime, tvAddress, tvWorker, tvContactName, tvStatus;
        ImageView ivPhone, ivContact, ivMap;
        LinearLayout llWorker, llContact;
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
