package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.List;

/**
 * Created by cookiemouse on 2017/7/24.
 */

public class OperateInstallListAdapter extends BaseAdapter {

    private static final String TAG = "OperateInstallAdapter";

    private Context context;
    private List<AdapterOperateInstallListData> mListDatas;

    private OnItemOperateListener mOnItemOperateListener;

    private boolean isChange = true;

    public OperateInstallListAdapter(Context context, List<AdapterOperateInstallListData> mListDatas) {
        this.context = context;
        this.mListDatas = mListDatas;
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

        AdapterOperateInstallListData data = mListDatas.get(position);

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

            viewHolder.etPosition = contentView.findViewById(R.id.et_item_operate_install_position);

            viewHolder.ivScanner = contentView.findViewById(R.id.iv_item_operate_install_new_scanner);
            viewHolder.ivLocate = contentView.findViewById(R.id.iv_item_operate_install_new_locate);
            viewHolder.ivPositionPic = contentView.findViewById(R.id.iv_item_operate_install_position_pic);
            viewHolder.ivInstallPic = contentView.findViewById(R.id.iv_item_operate_install_install_pic);

            viewHolder.ivPositionDelete = contentView.findViewById(R.id.iv_item_operate_install_position_pic_delete);
            viewHolder.ivInstallDelete = contentView.findViewById(R.id.iv_item_operate_install_install_pic_delete);

            viewHolder.rlOld = contentView.findViewById(R.id.rl_item_operate_install_old);
            viewHolder.rlItem = contentView.findViewById(R.id.rl_item_operate_install_list);

            viewHolder.etTNoNew.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    //  2017/7/31 检查imei号是否完整，应暴露给外部
                    String imei = editable.toString();
                    if (isChange) {
                        return;
                    }
                    if (null == mOnItemOperateListener) {
                        throw new NullPointerException("OnItemOperateListener is null");
                    }
                    mOnItemOperateListener.onTextChanged(positionFinal, imei);
                }
            });

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        String tNoOld = data.gettNoOld();
        String tNoNew = data.gettNoNew();

        isChange = true;
        if (null != tNoOld && !tNoOld.equals("")) {
            viewHolder.rlOld.setVisibility(View.VISIBLE);
            viewHolder.tvStatus.setText(R.string.not_replace);

            viewHolder.tvTNoOld.setText(tNoOld);
            viewHolder.tvStatus.setEnabled(true);
        } else {
            viewHolder.rlOld.setVisibility(View.GONE);
            viewHolder.tvStatus.setText(R.string.repair_replace);
            if (null != tNoNew && !tNoNew.equals("")) {
                viewHolder.tvStatus.setEnabled(true);
            } else {
                viewHolder.tvStatus.setEnabled(false);
            }
        }

        if (data.isWire()) {
            viewHolder.tvTitle.setText("新有线设备");
        } else {
            viewHolder.tvTitle.setText("新无线设备");
        }

        viewHolder.etTNoNew.setText(data.gettNoNew());
        viewHolder.etPosition.setText(data.getPosition());
        isChange = false;

        if (data.getPositionPicUrl() != null) {
            Picasso.with(context)
                    .load(data.getPositionPic())
                    .fit()
                    .centerInside()
                    .error(R.drawable.ic_camera)
                    .into(viewHolder.ivPositionPic);

            viewHolder.ivPositionDelete.setVisibility(View.VISIBLE);
        } else {
            Picasso.with(context)
                    .load(R.drawable.ic_camera)
                    .fit()
                    .centerInside()
                    .into(viewHolder.ivPositionPic);
            viewHolder.ivPositionDelete.setVisibility(View.GONE);
        }

        if (data.getInstallPicUrl() != null) {
            Picasso.with(context)
                    .load(data.getInstallPic())
                    .fit()
                    .centerInside()
                    .error(R.drawable.ic_camera)
                    .into(viewHolder.ivInstallPic);

            viewHolder.ivInstallDelete.setVisibility(View.VISIBLE);
        } else {
            Picasso.with(context)
                    .load(R.drawable.ic_camera)
                    .fit()
                    .centerInside()
                    .into(viewHolder.ivInstallPic);
            viewHolder.ivInstallDelete.setVisibility(View.GONE);
        }

        if (data.isComplete()) {
            viewHolder.rlItem.setBackgroundResource(R.drawable.bg_item);
            viewHolder.ivLocate.setBackgroundResource(R.color.colorNull);
            viewHolder.ivInstallPic.setBackgroundResource(R.color.colorNull);
            viewHolder.ivPositionPic.setBackgroundResource(R.color.colorNull);
        } else {
            viewHolder.rlItem.setBackgroundResource(R.drawable.bg_item_orange);
            if (0 == data.getModel()) {
                viewHolder.ivLocate.setBackgroundResource(R.drawable.bg_edit_orange);
            }
            if (null == tNoNew || "".equals(tNoNew)) {
                viewHolder.tvTip1.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvTip1.setVisibility(View.GONE);
            }
            if (null == data.getPosition() || "".equals(data.getPosition())) {
                viewHolder.tvTip2.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvTip2.setVisibility(View.GONE);
            }
            if (null == data.getPositionPicUrl()) {
                viewHolder.ivPositionPic.setBackgroundResource(R.drawable.bg_edit_orange);
            }
            if (null == data.getInstallPicUrl()) {
                viewHolder.ivInstallPic.setBackgroundResource(R.drawable.bg_edit_orange);
            }
        }

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
        private TextView tvTip1, tvTip2;
        private EditText etTNoNew, etPosition;
        private ImageView ivScanner, ivLocate, ivPositionPic, ivInstallPic;
        private RelativeLayout rlOld, rlItem;
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
}
