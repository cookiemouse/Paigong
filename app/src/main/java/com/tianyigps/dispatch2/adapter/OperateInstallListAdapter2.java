package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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

public class OperateInstallListAdapter2 extends BaseAdapter {

    private static final String TAG = "OperateInstallAdapter";

    private static final String IMEI = "imei";
    private static final String POSITION = "position";
    private static final String FOCUS = "focus";
    private static final int DELAY = 200;

    private Context context;
    private List<AdapterOperateInstallListData> mListDatas;

    private OnItemOperateListener mOnItemOperateListener;

    private MyHandler myHandler;

    public OperateInstallListAdapter2(Context context, List<AdapterOperateInstallListData> mListDatas) {
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

        TextView tvTitle, tvStatus, tvTNoOld;
        TextView tvTip1, tvTip2, tvTip3;
        final EditText etTNoNew;
        EditText etPosition;
        ImageView ivScanner, ivLocate, ivPositionPic, ivInstallPic;
        RelativeLayout rlOld, rlItem, rlInstall;
        ImageView ivPositionDelete, ivInstallDelete;

        contentView = LayoutInflater.from(context).inflate(R.layout.item_operate_install_listview, null);

        tvTitle = contentView.findViewById(R.id.tv_item_operate_install_new_device_no_title);
        tvStatus = contentView.findViewById(R.id.tv_item_operate_install_state);
        tvTNoOld = contentView.findViewById(R.id.tv_item_operate_install_old_device_no);
        etTNoNew = contentView.findViewById(R.id.et_item_operate_new_device_no);
        tvTip1 = contentView.findViewById(R.id.tv_item_operate_install_tip_1);
        tvTip2 = contentView.findViewById(R.id.tv_item_operate_install_tip_2);
        tvTip3 = contentView.findViewById(R.id.tv_item_operate_install_tip_3);

        etPosition = contentView.findViewById(R.id.et_item_operate_install_position);

        ivScanner = contentView.findViewById(R.id.iv_item_operate_install_new_scanner);
        ivLocate = contentView.findViewById(R.id.iv_item_operate_install_new_locate);
        ivPositionPic = contentView.findViewById(R.id.iv_item_operate_install_position_pic);
        ivInstallPic = contentView.findViewById(R.id.iv_item_operate_install_install_pic);

        ivPositionDelete = contentView.findViewById(R.id.iv_item_operate_install_position_pic_delete);
        ivInstallDelete = contentView.findViewById(R.id.iv_item_operate_install_install_pic_delete);

        rlOld = contentView.findViewById(R.id.rl_item_operate_install_old);
        rlItem = contentView.findViewById(R.id.rl_item_operate_install_list);
        rlInstall = contentView.findViewById(R.id.rl_item_operate_install_install);

        String tNoOld = data.gettNoOld();
        String tNoNew = data.gettNoNew();

        if (data.isReplaceAble()) {
            tvStatus.setEnabled(true);
        } else {
            tvStatus.setEnabled(false);
        }

        if (null != tNoOld && !tNoOld.equals("")) {
            rlOld.setVisibility(View.VISIBLE);
            tvStatus.setText(R.string.not_replace);

            tvTNoOld.setText(tNoOld);
        } else {
            rlOld.setVisibility(View.GONE);
            tvStatus.setText(R.string.repair_replace);
        }

        etTNoNew.setText(data.gettNoNew());
        etPosition.setText(data.getPosition());

        if (data.getPositionPicUrl() != null) {
            Picasso.with(context)
                    .load(data.getPositionPic())
                    .fit()
                    .centerInside()
                    .error(R.drawable.ic_camera)
                    .into(ivPositionPic);
            ivPositionDelete.setVisibility(View.VISIBLE);
        } else {
            Picasso.with(context)
                    .load(R.drawable.ic_camera)
                    .fit()
                    .centerInside()
                    .into(ivPositionPic);
            ivPositionDelete.setVisibility(View.GONE);
        }

        if (data.getInstallPicUrl() != null) {
            Picasso.with(context)
                    .load(data.getInstallPic())
                    .fit()
                    .centerInside()
                    .error(R.drawable.ic_camera)
                    .into(ivInstallPic);

            ivInstallDelete.setVisibility(View.VISIBLE);
        } else {
            Picasso.with(context)
                    .load(R.drawable.ic_camera)
                    .fit()
                    .centerInside()
                    .into(ivInstallPic);
            ivInstallDelete.setVisibility(View.GONE);
        }

        if (data.isWire()) {
            tvTitle.setText("新有线设备");
            rlInstall.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setText("新无线设备");
            rlInstall.setVisibility(View.GONE);
            ivInstallDelete.setVisibility(View.GONE);
        }

        if (data.isComplete()) {
            rlItem.setBackgroundResource(R.drawable.bg_item);
            ivLocate.setBackgroundResource(R.color.colorNull);
            ivInstallPic.setBackgroundResource(R.color.colorNull);
            ivPositionPic.setBackgroundResource(R.color.colorNull);
            tvTip1.setVisibility(View.GONE);
            tvTip2.setVisibility(View.GONE);
            tvTip3.setVisibility(View.GONE);
        } else {
            rlItem.setBackgroundResource(R.drawable.bg_item_orange);
            //  定位按钮
            if (0 == data.getModel()) {
                ivLocate.setBackgroundResource(R.drawable.bg_edit_orange);
                tvTip1.setVisibility(View.VISIBLE);
                tvTip1.setText(context.getString(R.string.tip_locate));
            } else {
                ivLocate.setBackgroundResource(R.color.colorNull);
            }
            //  设备号
            if (null == tNoNew || "".equals(tNoNew)) {
                tvTip1.setText(context.getString(R.string.tip_imei));
                tvTip1.setVisibility(View.VISIBLE);
            }
            if (null != tNoNew && !"".equals(tNoNew) && 0 != data.getModel()) {
                tvTip1.setVisibility(View.GONE);
            }

            //  安装位置图
            if (null == data.getPositionPicUrl()) {
                ivPositionPic.setBackgroundResource(R.drawable.bg_edit_orange);
                tvTip2.setVisibility(View.VISIBLE);
                tvTip2.setText(context.getString(R.string.tip_pic));
            } else {
                ivPositionPic.setBackgroundResource(R.color.colorNull);
            }
            //  安装位置
            if (null == data.getPosition() || "".equals(data.getPosition())) {
                tvTip2.setVisibility(View.VISIBLE);
                tvTip2.setText(context.getString(R.string.tip_position));
            }
            if (null != data.getPositionPicUrl() && null != data.getPosition() && !"".equals(data.getPosition())) {
                tvTip2.setVisibility(View.GONE);
            }

            //  接线图
            if (null == data.getInstallPicUrl()) {
                ivInstallPic.setBackgroundResource(R.drawable.bg_edit_orange);
                tvTip3.setVisibility(View.VISIBLE);
            } else {
                ivInstallPic.setBackgroundResource(R.color.colorNull);
                tvTip3.setVisibility(View.GONE);
            }
        }

        etTNoNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG, "afterTextChanged: text-->" + editable.toString());
                data.settNoNew(editable.toString());
                myHandler.removeMessages(Data.MSG_1);
                Bundle bundle = new Bundle();
                bundle.putString(IMEI, editable.toString());
                bundle.putInt(POSITION, positionFinal);
                Message message = new Message();
                message.what = Data.MSG_1;
                message.obj = bundle;
                myHandler.sendMessageDelayed(message, DELAY);
            }
        });

        etTNoNew.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.i(TAG, "onFocusChange: focus-->" + b);
                myHandler.removeMessages(Data.MSG_2);
                if (!b) {
                    Message message = new Message();
                    message.arg1 = positionFinal;
                    message.what = Data.MSG_2;
                    myHandler.sendMessageDelayed(message, DELAY);
                }
            }
        });

        etPosition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                myHandler.removeMessages(Data.MSG_3);
                Message message = new Message();
                message.what = Data.MSG_3;
                message.arg1 = positionFinal;
                message.obj = editable.toString();
                myHandler.sendMessageDelayed(message, DELAY);
            }
        });

        tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  2017/7/31 暴露给外部，存储数据
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onStatusClick(positionFinal);
            }
        });

        ivScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  2017/7/31 扫描条形码，暴露给外部
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onScannerClick(positionFinal);
            }
        });

        ivLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/31 快速定位，暴露给外部
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onLocateClick(positionFinal);
            }
        });

        ivPositionPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onPositionPicClick(positionFinal);
            }
        });

        ivInstallPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onInstallPicClick(positionFinal);
            }
        });

        ivPositionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemOperateListener) {
                    throw new NullPointerException("OnItemOperateListener is null");
                }
                mOnItemOperateListener.onPositionPicDelete(positionFinal);
            }
        });

        ivInstallDelete.setOnClickListener(new View.OnClickListener() {
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

    public interface OnItemOperateListener {
        void onScannerClick(int position);

        void onTextChanged(int position, String imei);

        void onLoseFocus(int position);

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
                case Data.MSG_2: {
                    int position = msg.arg1;
                    if (null == mOnItemOperateListener) {
                        throw new NullPointerException("OnItemOperateListener is null");
                    }
                    mOnItemOperateListener.onLoseFocus(position);
                    break;
                }
                case Data.MSG_3: {
                    String position = (String) msg.obj;
                    int item = msg.arg1;
                    mListDatas.get(item).setPosition(position);
                    break;
                }
            }
        }
    }
}
