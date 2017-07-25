package com.tianyigps.xiepeng.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.data.AdapterOperateRemoveData;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;

import java.util.List;

/**
 * Created by cookiemouse on 2017/7/25.
 */

public class OperateRemoveAdapter extends BaseAdapter {

    private static final String TAG = "OperateRemoveAdapter";

    private Context context;
    private List<AdapterOperateRemoveData> mDataList;
    private SharedpreferenceManager mSharedpreferenceManager;
    private String picPathUrl;

    public OperateRemoveAdapter(Context context, List<AdapterOperateRemoveData> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
        mSharedpreferenceManager = new SharedpreferenceManager(context);
        picPathUrl = mSharedpreferenceManager.getImageBaseUrl();
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
        AdapterOperateRemoveData data = mDataList.get(position);
        ViewHolder viewHolder = null;

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
            viewHolder.picIntall = contentView.findViewById(R.id.iv_item_operate_remove_install_pic);

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
        viewHolder.date.setText(data.getDate());
        viewHolder.installName.setText(data.getInstallName());
        viewHolder.installPhone.setText(data.getInstallPhone());

        Log.i(TAG, "getView: url-->" + picPathUrl + data.getPicPosition());
        Picasso.with(context).load(picPathUrl + data.getPicPosition()).resize(320, 160).into(viewHolder.picPosition);
        Picasso.with(context).load(picPathUrl + data.getPicInstall()).resize(320, 160).into(viewHolder.picIntall);

        return contentView;
    }

    private class ViewHolder {
        private TextView carNo, frameNo, typeAndName, tNo, position, date, installName, installPhone;
        private ImageView picPosition, picIntall;
    }
}
