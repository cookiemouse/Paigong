package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterInstallingData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/7/24.
 */

public class InstallingAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterInstallingData> mAdapterInstallingDatas;

    public InstallingAdapter(Context context, List<AdapterInstallingData> mAdapterInstallingDatas) {
        this.context = context;
        this.mAdapterInstallingDatas = mAdapterInstallingDatas;
    }

    @Override
    public int getCount() {
        return mAdapterInstallingDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mAdapterInstallingDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        if (position % 2 == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
//        return super.getViewTypeCount();
        return 2;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {

        AdapterInstallingData data = mAdapterInstallingDatas.get(position);

        ViewHolder viewHolder = null;
        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_installing, null);
            viewHolder = new ViewHolder();

            viewHolder.frameLayout = contentView.findViewById(R.id.fl_item_installing);
            viewHolder.textViewFrameNo = contentView.findViewById(R.id.tv_item_installing_frame_no);
            viewHolder.textViewPendingOnline = contentView.findViewById(R.id.tv_item_installing_pending_online);
            viewHolder.textViewPendingOffline = contentView.findViewById(R.id.tv_item_installing_pending_offline);
            viewHolder.textViewCompleteOnline = contentView.findViewById(R.id.tv_item_installing_complete_online);
            viewHolder.textViewCompleteOffline = contentView.findViewById(R.id.tv_item_installing_complete_offline);
            viewHolder.ivGo = contentView.findViewById(R.id.iv_item_installing_go);
            viewHolder.tvGo = contentView.findViewById(R.id.tv_item_installing_go);
            viewHolder.tvPosition = contentView.findViewById(R.id.tv_item_installing_position);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        if (0 == getItemViewType(position)) {
            viewHolder.frameLayout.setBackgroundResource(R.drawable.bg_item_installing_blue);
        } else {
            viewHolder.frameLayout.setBackgroundResource(R.drawable.bg_item_installing_green);
        }

        viewHolder.textViewFrameNo.setText(data.getFrameNo());
        viewHolder.tvPosition.setText("" + (position + 1));
        viewHolder.textViewPendingOnline.setText("" + data.getOrderLine());
        viewHolder.textViewPendingOffline.setText("" + data.getOrderOffline());
        viewHolder.textViewCompleteOnline.setText("" + data.getCompleteLine());
        viewHolder.textViewCompleteOffline.setText("" + data.getCompleteOffline());

        if (data.isComplete()) {
            if (data.getCompleteLine() == data.getOrderLine() && data.getCompleteOffline() == data.getOrderOffline()) {
                viewHolder.ivGo.setVisibility(View.GONE);
                viewHolder.tvGo.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivGo.setVisibility(View.VISIBLE);
                viewHolder.tvGo.setVisibility(View.GONE);
            }
        } else {
            viewHolder.frameLayout.setBackgroundResource(R.drawable.bg_item_installing_orange);
            viewHolder.ivGo.setVisibility(View.VISIBLE);
            viewHolder.tvGo.setVisibility(View.GONE);
        }

        return contentView;
    }

    private class ViewHolder {
        private FrameLayout frameLayout;
        private TextView textViewFrameNo, textViewPendingOnline, textViewPendingOffline, textViewCompleteOnline, textViewCompleteOffline;
        private ImageView ivGo;
        private TextView tvGo;
        private TextView tvPosition;
    }
}
