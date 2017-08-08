package com.tianyigps.xiepeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.data.AdapterPendingData;

import java.util.List;

/**
 * Created by djc on 2017/7/11.
 */

public class PendingAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterPendingData> mPendingDataList;

    private OnItemListener mOnItemListener;

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
            viewHolder.tvContactName = view.findViewById(R.id.tv_item_pending_contact);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvOrderNo.setText(data.getOrder());
        viewHolder.tvName.setText(data.getName());
        viewHolder.tvTime.setText(data.getTime());
        viewHolder.tvAddress.setText(data.getAddress());
        viewHolder.tvOrderType.setText(data.getOrderType());
        viewHolder.tvWire.setText("" + data.getLineNumber());
        viewHolder.tvWireless.setText("" + data.getLinelessNumber());
        viewHolder.tvContactName.setText(data.getContactName());

        viewHolder.imageViewCall.setOnClickListener(new View.OnClickListener() {
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
        private TextView tvOrderNo, tvName, tvTime, tvAddress, tvOrderType, tvWire, tvWireless, tvContactName;
    }

    //  确认签到对话框
    private void showSignDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View viewDialog = LayoutInflater.from(context).inflate(R.layout.layout_dialog_sign, null);
//        TextView textViewEnsure = viewDialog.findViewById(R.id.tv_layout_dialog_sign_ensure);
//        TextView textViewCancel = viewDialog.findViewById(R.id.tv_layout_dialog_sign_cancel);
//        builder.setView(viewDialog);
//        final Dialog dialog = builder.create();
//        textViewEnsure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO: 2017/7/11 确认 [
//            }
//        });
//        textViewCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO: 2017/7/11 取消
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
    }

    public interface OnItemListener {
        void onMap(int position);

        void onCall(int position);

        void onPend(int position);

        void onItem(int position);
    }

    public void setOnItemListener(OnItemListener listener) {
        this.mOnItemListener = listener;
    }
}