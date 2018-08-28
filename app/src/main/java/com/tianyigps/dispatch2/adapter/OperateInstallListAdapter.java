package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterOperateInstallListData;
import com.tianyigps.dispatch2.data.Data;

import java.util.List;

/**
 * Created by cookiemouse on 2017/7/24.
 */

public class OperateInstallListAdapter extends BaseAdapter {

    private static final String TAG = "OperateInstallAdapter";

    private static final String IMEI = "imei";
    private static final String POSITION = "position";
    private static final int DELAY = 200;

    private Context context;
    private List<AdapterOperateInstallListData> mListDatas;

    private OnItemOperateListener mOnItemOperateListener;

    private MyHandler myHandler;

    public OperateInstallListAdapter(Context context, List<AdapterOperateInstallListData> mListDatas) {
        this.context = context;
        this.mListDatas = mListDatas;
        myHandler = new MyHandler();
    }

    @Override
    public int getCount() {
        return mListDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mListDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {

        final AdapterOperateInstallListData data = mListDatas.get(position);

        final int positionFinal = position;

        ViewHolder viewHolder = null;
        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_operate_install_listview, null);
            viewHolder = new ViewHolder();

            viewHolder.tvTitle = contentView.findViewById(R.id.tv_item_operate_install_new_device_no_title);
            viewHolder.tvStatus = contentView.findViewById(R.id.tv_item_operate_install_state);
            viewHolder.tvTNoOld = contentView.findViewById(R.id.tv_item_operate_install_old_device_no);
            viewHolder.etTNoNew = contentView.findViewById(R.id.et_item_operate_new_device_no);
            viewHolder.tvTip1 = contentView.findViewById(R.id.tv_item_operate_install_tip_1);
            viewHolder.tvTip2 = contentView.findViewById(R.id.tv_item_operate_install_tip_2);
            viewHolder.tvTip3 = contentView.findViewById(R.id.tv_item_operate_install_tip_3);

            viewHolder.etPosition = contentView.findViewById(R.id.et_item_operate_install_position);

            viewHolder.ivScanner = contentView.findViewById(R.id.iv_item_operate_install_new_scanner);
            viewHolder.ivLocate = contentView.findViewById(R.id.iv_item_operate_install_new_locate);
            viewHolder.ivPositionPic = contentView.findViewById(R.id.iv_item_operate_install_position_pic);
            viewHolder.ivInstallPic = contentView.findViewById(R.id.iv_item_operate_install_install_pic);

            viewHolder.ivPositionDelete = contentView.findViewById(R.id.iv_item_operate_install_position_pic_delete);
            viewHolder.ivInstallDelete = contentView.findViewById(R.id.iv_item_operate_install_install_pic_delete);

            viewHolder.rlOld = contentView.findViewById(R.id.rl_item_operate_install_old);
            viewHolder.rlItem = contentView.findViewById(R.id.rl_item_operate_install_list);
            viewHolder.rlInstall = contentView.findViewById(R.id.rl_item_operate_install_install);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        String tNoOld = data.gettNoOld();
        String tNoNew = data.gettNoNew();

        if (data.isReplaceAble()) {
            viewHolder.tvStatus.setEnabled(true);
        } else {
            viewHolder.tvStatus.setEnabled(false);
        }

        if (null != tNoOld && !tNoOld.equals("")) {
            viewHolder.rlOld.setVisibility(View.VISIBLE);
            viewHolder.tvStatus.setText(R.string.not_replace);

            viewHolder.tvTNoOld.setText(tNoOld);
        } else {
            viewHolder.rlOld.setVisibility(View.GONE);
            viewHolder.tvStatus.setText(R.string.repair_replace);
        }

        viewHolder.etTNoNew.setText(data.gettNoNew());
        viewHolder.etPosition.setText(data.getPosition());

        if (data.getPositionPicUrl() != null) {
            Picasso.get()
                    .load(data.getPositionPic())
                    .fit()
                    .centerInside()
                    .error(R.drawable.ic_camera)
                    .into(viewHolder.ivPositionPic);
            viewHolder.ivPositionDelete.setVisibility(View.VISIBLE);
        } else {
            Picasso.get()
                    .load(R.drawable.ic_camera)
                    .fit()
                    .centerInside()
                    .into(viewHolder.ivPositionPic);
            viewHolder.ivPositionDelete.setVisibility(View.GONE);
        }

        if (data.getInstallPicUrl() != null) {
            Picasso.get()
                    .load(data.getInstallPic())
                    .fit()
                    .centerInside()
                    .error(R.drawable.ic_camera)
                    .into(viewHolder.ivInstallPic);

            viewHolder.ivInstallDelete.setVisibility(View.VISIBLE);
        } else {
            Picasso.get()
                    .load(R.drawable.ic_camera)
                    .fit()
                    .centerInside()
                    .into(viewHolder.ivInstallPic);
            viewHolder.ivInstallDelete.setVisibility(View.GONE);
        }

        if (data.isWire()) {
            viewHolder.tvTitle.setText("新有线设备");
            viewHolder.rlInstall.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvTitle.setText("新无线设备");
            viewHolder.rlInstall.setVisibility(View.GONE);
            viewHolder.ivInstallDelete.setVisibility(View.GONE);
        }

        if (data.isComplete()) {
            viewHolder.rlItem.setBackgroundResource(R.drawable.bg_item);
            viewHolder.ivLocate.setBackgroundResource(R.color.colorNull);
            viewHolder.ivInstallPic.setBackgroundResource(R.color.colorNull);
            viewHolder.ivPositionPic.setBackgroundResource(R.color.colorNull);
            viewHolder.tvTip1.setVisibility(View.GONE);
            viewHolder.tvTip2.setVisibility(View.GONE);
            viewHolder.tvTip3.setVisibility(View.GONE);
        } else {
            viewHolder.rlItem.setBackgroundResource(R.drawable.bg_item_orange);
            //  定位按钮
            if (0 == data.getModel()) {
                viewHolder.ivLocate.setBackgroundResource(R.drawable.bg_edit_orange);
                viewHolder.tvTip1.setVisibility(View.VISIBLE);
                viewHolder.tvTip1.setText(context.getString(R.string.tip_locate));
            } else {
                viewHolder.ivLocate.setBackgroundResource(R.color.colorNull);
            }
            //  设备号
            if (null == tNoNew || "".equals(tNoNew)) {
                viewHolder.tvTip1.setText(context.getString(R.string.tip_imei));
                viewHolder.tvTip1.setVisibility(View.VISIBLE);
            }
            if (null != tNoNew && !"".equals(tNoNew) && 0 != data.getModel()) {
                viewHolder.tvTip1.setVisibility(View.GONE);
            }

            //  安装位置图
            if (null == data.getPositionPicUrl()) {
                viewHolder.ivPositionPic.setBackgroundResource(R.drawable.bg_edit_orange);
                viewHolder.tvTip2.setVisibility(View.VISIBLE);
                viewHolder.tvTip2.setText(context.getString(R.string.tip_pic));
            } else {
                viewHolder.ivPositionPic.setBackgroundResource(R.color.colorNull);
            }
            //  安装位置
            if (null == data.getPosition() || "".equals(data.getPosition())) {
                viewHolder.tvTip2.setVisibility(View.VISIBLE);
                viewHolder.tvTip2.setText(context.getString(R.string.tip_position));
            }
            if (null != data.getPositionPicUrl() && null != data.getPosition() && !"".equals(data.getPosition())) {
                viewHolder.tvTip2.setVisibility(View.GONE);
            }

            //  接线图
            if (null == data.getInstallPicUrl()) {
                viewHolder.ivInstallPic.setBackgroundResource(R.drawable.bg_edit_orange);
                viewHolder.tvTip3.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivInstallPic.setBackgroundResource(R.color.colorNull);
                viewHolder.tvTip3.setVisibility(View.GONE);
            }
        }

        viewHolder.etTNoNew.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (!focus) {
                    data.settNoNew(((EditText) view).getText().toString());
                }
            }
        });

        viewHolder.etPosition.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    data.setPosition(((EditText) view).getText().toString());
                }
            }
        });

        viewHolder.tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  2017/7/31 暴露给外部，存储数据
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onStatusClick(positionFinal);
            }
        });

        viewHolder.ivScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  2017/7/31 扫描条形码，暴露给外部
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onScannerClick(positionFinal);
            }
        });

        viewHolder.ivLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/31 快速定位，暴露给外部
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onLocateClick(positionFinal);
            }
        });

        viewHolder.ivPositionPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onPositionPicClick(positionFinal);
            }
        });

        viewHolder.ivInstallPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onInstallPicClick(positionFinal);
            }
        });

        viewHolder.ivPositionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onPositionPicDelete(positionFinal);
            }
        });

        viewHolder.ivInstallDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onInstallPicDelete(positionFinal);
            }
        });

        return contentView;
    }

    private class ViewHolder {
        private TextView tvTitle, tvStatus, tvTNoOld;
        private TextView tvTip1, tvTip2, tvTip3;
        private EditText etTNoNew, etPosition;
        private ImageView ivScanner, ivLocate, ivPositionPic, ivInstallPic;
        private RelativeLayout rlOld, rlItem, rlInstall;
        private ImageView ivPositionDelete, ivInstallDelete;
    }

    public interface OnItemOperateListener {
        void onScannerClick(int position);

        void onTextChanged(int position, String imei);

        void onStatusClick(int position);

        void onLocateClick(int position);

        void onPositionPicClick(int position);

        void onInstallPicClick(int position);

        void onPositionPicDelete(int position);

        void onInstallPicDelete(int position);
    }

    public void setOnItemOperateListener(OnItemOperateListener listener) {
        this.mOnItemOperateListener = listener;
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_1: {
                    Bundle bundle = (Bundle) msg.obj;
                    String imei = bundle.getString(IMEI);
                    int position = bundle.getInt(POSITION);
                    if (null == mOnItemOperateListener) {
                        throw new NullPointerException("OnItemOperateListener is null");
                    }
                    mOnItemOperateListener.onTextChanged(position, imei);
                    break;
                }
            }
        }
    }
}
