package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterPendedData;
import com.tianyigps.dispatch2.utils.TimeFormatU;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public class PendedAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterPendedData> mDataList;

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
    public View getView(int position, View contentView, ViewGroup viewGroup) {

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
            viewHolder.ivPhone = contentView.findViewById(R.id.iv_item_pended_phone);
            viewHolder.ivContact = contentView.findViewById(R.id.iv_item_pended_contact);
            viewHolder.llWorker = contentView.findViewById(R.id.ll_item_pended_worker);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.tvTitle.setText(data.getTitle());
        viewHolder.tvTime.setText(new TimeFormatU().millisToDate(data.getTime()));
        viewHolder.tvAddress.setText(data.getAddress());
        viewHolder.tvWorker.setText(data.getWorker());
        viewHolder.tvContactName.setText(data.getContact());

        viewHolder.ivPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + data.getWorkerPhone()));
                context.startActivity(intent);
            }
        });

        viewHolder.ivContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + data.getContactPhone()));
                context.startActivity(intent);
            }
        });

        return contentView;
    }

    private class ViewHolder {
        TextView tvTitle, tvTime, tvAddress, tvWorker, tvContactName;
        ImageView ivPhone, ivContact;
        LinearLayout llWorker;
    }
}
