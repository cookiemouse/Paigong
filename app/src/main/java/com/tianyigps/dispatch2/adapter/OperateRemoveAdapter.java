package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.bean.LastInstallerBean;
import com.tianyigps.dispatch2.data.AdapterOperateRemoveData;
import com.tianyigps.dispatch2.interfaces.OnGetLastInstallerListener;

import java.util.List;

/**
 * Created by cookiemouse on 2017/7/25.
 */

public class OperateRemoveAdapter extends BaseAdapter {

    private static final String TAG = "OperateRemoveAdapter";

    private Context context;
    private List<AdapterOperateRemoveData> mDataList;

    private OnItemListener mOnItemListener;

    public OperateRemoveAdapter(Context context, List<AdapterOperateRemoveData> mDataList) {
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
        final AdapterOperateRemoveData data = mDataList.get(position);
        ViewHolder viewHolder = null;
        final int positionFinal = position;

        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_operate_remove, null);
            viewHolder = new ViewHolder();

            viewHolder.carNo = contentView.findViewById(R.id.tv_item_operate_remove_car_no);
            viewHolder.frameNo = contentView.findViewById(R.id.tv_item_operate_remove_frame_no);
            viewHolder.typeAndName = contentView.findViewById(R.id.tv_item_operate_remove_device_type_and_name);
            viewHolder.tNo = contentView.findViewById(R.id.tv_item_operate_remove_device_t_no);
            viewHolder.position = contentView.findViewById(R.id.tv_item_operate_remove_position);
            viewHolder.date = contentView.findViewById(R.id.tv_item_operate_remove_date);
            viewHolder.installName = contentView.findViewById(R.id.tv_item_operate_remove_worker);
            viewHolder.installPhone = contentView.findViewById(R.id.tv_item_operate_remove_worker_phone);
            viewHolder.picPosition = contentView.findViewById(R.id.iv_item_operate_remove_position_pic);
            viewHolder.picInstall = contentView.findViewById(R.id.iv_item_operate_remove_install_pic);
            viewHolder.state = contentView.findViewById(R.id.tv_item_operate_remove_state);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.carNo.setText(data.getCarNo());
        viewHolder.frameNo.setText(data.getFrameNo());
        String typeAndName = data.getTerminalType() + data.getTerminalName();
        viewHolder.typeAndName.setText(typeAndName);
        viewHolder.tNo.setText(data.gettNo());
        viewHolder.position.setText(data.getInstallPosition());

        viewHolder.installName.setText(data.getInstallName());
        viewHolder.installPhone.setText(data.getInstallPhone());
        viewHolder.date.setText(data.getDate());

        String state;
        switch (data.getRemoveState()) {
            case 0: {
                state = "拆除";
                viewHolder.state.setEnabled(true);
                break;
            }
            case 1: {
                viewHolder.state.setEnabled(false);
                state = "已拆除";
                break;
            }
            case 2: {
                viewHolder.state.setEnabled(false);
                state = "拆除已安装";
                break;
            }
            default: {
                state = "拆除";
                viewHolder.state.setEnabled(true);
                Log.i(TAG, "getView: default");
            }
        }
        viewHolder.state.setText(state);

        Picasso.with(context)
                .load(data.getPicPosition())
                .error(R.color.colorNull)
                .fit()
                .centerInside()
                .into(viewHolder.picPosition);
        Picasso.with(context)
                .load(data.getPicPosition())
                .error(R.color.colorNull)
                .fit()
                .centerInside()
                .into(viewHolder.picInstall);

        viewHolder.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("mOnItemListener is null");
                }
                mOnItemListener.onRemove(positionFinal);
            }
        });

        return contentView;
    }

    public interface OnItemListener {
        void onRemove(int position);
    }

    public void setItemListener(OnItemListener listener) {
        this.mOnItemListener = listener;
    }

    private class ViewHolder {
        private TextView carNo, frameNo, typeAndName, tNo, position, date, installName, installPhone, state;
        private ImageView picPosition, picInstall;
    }
}
