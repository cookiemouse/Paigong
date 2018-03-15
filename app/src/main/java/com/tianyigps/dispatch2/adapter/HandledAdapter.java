package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterHandledData;
import com.tianyigps.dispatch2.utils.ClipU;
import com.tianyigps.dispatch2.utils.ToastU;

import java.util.List;

/**
 * Created by djc on 2017/7/13.
 */

public class HandledAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterHandledData> mHandledDataList;

    private ToastU mToastU;

    public HandledAdapter(Context context, List<AdapterHandledData> mHandledDataList) {
        this.context = context;
        this.mHandledDataList = mHandledDataList;
        mToastU = new ToastU(context);
    }

    @Override
    public int getCount() {
        return mHandledDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHandledDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        final AdapterHandledData data = mHandledDataList.get(position);

        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_handled, null);

            viewHolder = new ViewHolder();
            viewHolder.textViewName = contentView.findViewById(R.id.tv_item_handled_name);
            viewHolder.textViewTime = contentView.findViewById(R.id.tv_item_handled_time);
            viewHolder.textViewAddress = contentView.findViewById(R.id.tv_item_handled_address);
            viewHolder.textViewId = contentView.findViewById(R.id.tv_item_handled_id);
            viewHolder.textViewTitle = contentView.findViewById(R.id.tv_item_handled_content_title);
            viewHolder.textViewWire = contentView.findViewById(R.id.tv_item_handled_content_wire);
            viewHolder.textViewWireless = contentView.findViewById(R.id.tv_item_handled_content_wireless);
            viewHolder.tvWireRemove = contentView.findViewById(R.id.tv_item_handled_content_wire_remove);
            viewHolder.tvWirelessRemove = contentView.findViewById(R.id.tv_item_handled_content_wireless_remove);
            viewHolder.llRemove = contentView.findViewById(R.id.ll_item_handled_remove);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.textViewName.setText(data.getName());
        viewHolder.textViewTime.setText(data.getTime());
        viewHolder.textViewAddress.setText(data.getAddress());
        viewHolder.textViewId.setText(data.getId());
        viewHolder.textViewWire.setText("" + data.getOnline());
        viewHolder.textViewWireless.setText("" + data.getLineLess());
        viewHolder.tvWireRemove.setText("" + data.getWireRemove());
        viewHolder.tvWirelessRemove.setText("" + data.getWirelessRemove());

        String orderType;

        switch (data.getOrderType()) {
            case 1: {
                orderType = "安装订单：";
                viewHolder.llRemove.setVisibility(View.GONE);
                break;
            }
            case 2: {
                orderType = "维修订单：";
                viewHolder.llRemove.setVisibility(View.GONE);
                break;
            }
            case 3: {
                orderType = "安装订单：";
                viewHolder.llRemove.setVisibility(View.VISIBLE);
                break;
            }
            default: {
                orderType = "安装订单：";
                viewHolder.llRemove.setVisibility(View.GONE);
            }
        }
        viewHolder.textViewTitle.setText(orderType);

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
                // TODO: 2017/7/11 Item点击事件
//                Intent intent = new Intent(context, OrderDetailsActivity.class);
//                context.startActivity(intent);
            }
        });

        return contentView;
    }

    private class ViewHolder {
        private TextView textViewName, textViewTime, textViewAddress, textViewId, textViewTitle, textViewWire, textViewWireless;
        private TextView tvWireRemove, tvWirelessRemove;
        private LinearLayout llRemove;
    }
}
