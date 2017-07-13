package com.tianyigps.xiepeng.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.activity.OrderDetailsActivity;
import com.tianyigps.xiepeng.data.AdapterPendingData;

import java.util.List;

/**
 * Created by djc on 2017/7/11.
 */

public class PendingAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterPendingData> mPendingDataList;

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

        ViewHolder viewHolder = null;

        if (null == view) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_pending, null);

            viewHolder.imageViewCall = view.findViewById(R.id.iv_adapter_pending_call);
            viewHolder.imageViewLocate = view.findViewById(R.id.iv_adapter_pending_locate);
            viewHolder.textViewOrder = view.findViewById(R.id.tv_adapter_pending_order);
            viewHolder.textViewName = view.findViewById(R.id.tv_adapter_pending_name);
            viewHolder.textViewTime = view.findViewById(R.id.tv_adapter_pending_time);
            viewHolder.textViewAddress = view.findViewById(R.id.tv_adapter_pending_address);
            viewHolder.textViewOrderType = view.findViewById(R.id.tv_adapter_pending_order_type);
            viewHolder.textViewOrderContent = view.findViewById(R.id.tv_adapter_pending_setting_content);
            viewHolder.buttonSign = view.findViewById(R.id.btn_adapter_pending_sign);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textViewOrder.setText(data.getOrder());
        viewHolder.textViewName.setText(data.getName());
        viewHolder.textViewTime.setText(data.getTime());
        viewHolder.textViewAddress.setText(data.getAddress());
        viewHolder.textViewOrderType.setText(data.getOrderType());
        viewHolder.textViewOrderContent.setText(data.getLineNumber() + "订单" + data.getLinelessNumber());

        viewHolder.imageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/11 拨打电话
//                data.getPhoneNumber();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "123456789"));
                context.startActivity(intent);
            }
        });

        viewHolder.imageViewLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/11 定位
            }
        });

        viewHolder.buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignDialog();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/11 Item点击事件
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                context.startActivity(intent);
            }
        });

        return view;
    }

    private class ViewHolder {
        private ImageView imageViewCall, imageViewLocate;
        private TextView textViewOrder, textViewName, textViewTime, textViewAddress, textViewOrderType, textViewOrderContent;
        private TextView buttonSign;
    }

    //  确认签到对话框
    private void showSignDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View viewDialog = LayoutInflater.from(context).inflate(R.layout.layout_dialog_sign, null);
        TextView textViewEnsure = viewDialog.findViewById(R.id.tv_layout_dialog_sign_ensure);
        TextView textViewCancel = viewDialog.findViewById(R.id.tv_layout_dialog_sign_cancel);
        builder.setView(viewDialog);
        final Dialog dialog = builder.create();
        textViewEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/11 确认 [
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/11 取消
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}