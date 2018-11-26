package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterOrderData;
import com.tianyigps.dispatch2.utils.ClipU;
import com.tianyigps.dispatch2.utils.TimeFormatU;
import com.tianyigps.dispatch2.utils.ToastU;

import java.util.List;

/**
 * Created by djc on 2017/7/13.
 */

public class OrderAdapter extends BaseAdapter {

    private static final String TAG = "OrderAdapter";

    private Context context;
    private List<AdapterOrderData> mAdapterOrderDataList;

    private long currentTime = System.currentTimeMillis();
    private ToastU mToastU;

    private OnItemListener mOnItemListener;

    public OrderAdapter(Context context, List<AdapterOrderData> mAdapterOrderDataList) {
        this.context = context;
        this.mAdapterOrderDataList = mAdapterOrderDataList;
        mToastU = new ToastU(context);
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

            viewHolder.tvEngineer = contentView.findViewById(R.id.tv_item_order_engineer);
            viewHolder.tvMap = contentView.findViewById(R.id.tv_item_order_map);
            viewHolder.tvStart = contentView.findViewById(R.id.tv_item_order_start);

            viewHolder.tvOrder = contentView.findViewById(R.id.tv_item_order_order);

            viewHolder.tvModify = contentView.findViewById(R.id.tv_item_order_modify);
            viewHolder.tvLate = contentView.findViewById(R.id.tv_item_order_late);
            viewHolder.ivNew = contentView.findViewById(R.id.iv_item_order_new_or_modify);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.textViewName.setText(data.getName());
        viewHolder.textViewTime.setText(new TimeFormatU().millisToDate2(data.getTime()));
        viewHolder.textViewAddress.setText(data.getAddress());
        viewHolder.textViewId.setText(data.getId());
        viewHolder.tvEngineer.setText(data.getPhoneName());

        if (data.getReviseFlag() == 1) {
            viewHolder.tvModify.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvModify.setVisibility(View.GONE);
        }

        if (currentTime >= data.getTime()) {
            viewHolder.tvLate.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvLate.setVisibility(View.GONE);
        }

        if (data.isShowNew()) {
            viewHolder.ivNew.setVisibility(View.VISIBLE);
            viewHolder.ivNew.setImageResource(R.drawable.ic_new);
        }else if (data.isShowModify()) {
            viewHolder.ivNew.setVisibility(View.VISIBLE);
            viewHolder.ivNew.setImageResource(R.drawable.ic_modify);
        } else {
            viewHolder.ivNew.setVisibility(View.GONE);
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
                Log.i(TAG, "onResponse: default");
            }
        }

        CharSequence charSequence = Html.fromHtml(orderInfo);
        viewHolder.tvOrder.setText(charSequence);

        viewHolder.tvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/13 地图页
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onMapClick(position);
            }
        });

        viewHolder.tvEngineer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onCallClick(position);
            }
        });

        viewHolder.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  2017/7/13 签到
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onSignClick(position);
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
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onItemClick(position);
            }
        });

        return contentView;
    }

    private class ViewHolder {
        private TextView textViewName, textViewTime, textViewAddress, textViewId;
        private TextView tvModify, tvLate;
        private TextView tvEngineer, tvMap, tvStart;
        private TextView tvOrder;
        private ImageView ivNew;
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
