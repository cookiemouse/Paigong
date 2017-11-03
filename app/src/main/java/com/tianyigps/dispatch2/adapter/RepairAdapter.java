package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterRepairData;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by cookiemouse on 2017/7/25.
 */

public class RepairAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterRepairData> mDataList;

    public RepairAdapter(Context context, List<AdapterRepairData> mDataList) {
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

        AdapterRepairData data = mDataList.get(position);
        ViewHolder viewHolder = null;

        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_repair, null);
            viewHolder = new ViewHolder();

            viewHolder.textViewType = contentView.findViewById(R.id.tv_item_repair_device_type);
            viewHolder.textViewId = contentView.findViewById(R.id.tv_item_repair_device_id);
            viewHolder.textViewName = contentView.findViewById(R.id.tv_item_repair_device_name);
            viewHolder.textViewCarNo = contentView.findViewById(R.id.tv_item_repair_car_no);
            viewHolder.textViewFrameNo = contentView.findViewById(R.id.tv_item_repair_frame_no);
            viewHolder.frameLayout = contentView.findViewById(R.id.fl_item_repair);
            viewHolder.ivGo = contentView.findViewById(R.id.iv_item_repair_go);
            viewHolder.tvGo = contentView.findViewById(R.id.tv_item_repair_go);
            viewHolder.tvPosition = contentView.findViewById(R.id.tv_item_repair_position);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        String terminalType;
        switch (data.getType()) {
            case 1: {
                terminalType = "有线设备";
                break;
            }
            case 2: {
                terminalType = "无线设备";
                break;
            }
            default: {
                terminalType = "";
                Log.i(TAG, "onSuccess: default");
            }
        }

        viewHolder.textViewType.setText(terminalType);
        viewHolder.tvPosition.setText("" + (position + 1));
        viewHolder.textViewId.setText(data.getId());
        viewHolder.textViewName.setText(data.getName());
        viewHolder.textViewCarNo.setText(data.getCarNo());
        viewHolder.textViewFrameNo.setText(data.getFrameNo());

        switch (data.getType()) {
            case 1: {
                viewHolder.frameLayout.setBackgroundResource(R.drawable.bg_item_installing_blue);
                break;
            }
            case 2: {
                viewHolder.frameLayout.setBackgroundResource(R.drawable.bg_item_installing_green);
                break;
            }
            default: {
                viewHolder.frameLayout.setBackgroundResource(R.drawable.bg_item_installing_blue);

            }
        }
        switch (data.getComplete()) {
            case 0: {
                viewHolder.ivGo.setVisibility(View.VISIBLE);
                viewHolder.tvGo.setVisibility(View.GONE);
                break;
            }
            case 1: {
                viewHolder.ivGo.setVisibility(View.GONE);
                viewHolder.tvGo.setVisibility(View.VISIBLE);
                break;
            }
            case 2: {
                viewHolder.ivGo.setVisibility(View.VISIBLE);
                viewHolder.tvGo.setVisibility(View.GONE);
                viewHolder.frameLayout.setBackgroundResource(R.drawable.bg_item_installing_orange);
                break;
            }
            default: {
                Log.i(TAG, "getView: default.complete-->" + data.getComplete());
            }
        }

        return contentView;
    }

    private class ViewHolder {
        private TextView textViewType, textViewId, textViewName, textViewCarNo, textViewFrameNo;
        private FrameLayout frameLayout;
        private ImageView ivGo;
        private TextView tvGo;
        private TextView tvPosition;
    }
}
