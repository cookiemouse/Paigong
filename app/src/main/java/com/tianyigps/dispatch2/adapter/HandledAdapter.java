package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterHandledData;

import java.util.List;

/**
 * Created by djc on 2017/7/13.
 */

public class HandledAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterHandledData> mHandledDataList;

    public HandledAdapter(Context context, List<AdapterHandledData> mHandledDataList) {
        this.context = context;
        this.mHandledDataList = mHandledDataList;
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

        AdapterHandledData data = mHandledDataList.get(position);

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

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.textViewName.setText(data.getName());
        viewHolder.textViewTime.setText(data.getTime());
        viewHolder.textViewAddress.setText(data.getAddress());
        viewHolder.textViewId.setText(data.getId());
        viewHolder.textViewTitle.setText(data.getOrderType());
        viewHolder.textViewWire.setText("" + data.getOnline());
        viewHolder.textViewWireless.setText("" + data.getLineLess());

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
        private TextView textViewName, textViewTime, textViewAddress
                , textViewId, textViewTitle, textViewWire, textViewWireless;
    }
}
