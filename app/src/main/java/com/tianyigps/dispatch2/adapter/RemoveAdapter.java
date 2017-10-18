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
import com.tianyigps.dispatch2.data.AdapterRemoveData;
import com.tianyigps.dispatch2.utils.RegularU;

import java.util.List;

/**
 * Created by cookiemouse on 2017/7/25.
 */

public class RemoveAdapter extends BaseAdapter {

    private static final String TAG = "RemoveAdapter";

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_REMOVE = 1;
    private static final int TYPE_INSTALL = 2;

    private Context context;
    private List<AdapterRemoveData> mDataList;

    public RemoveAdapter(Context context, List<AdapterRemoveData> mDataList) {
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
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        if (position < mDataList.size()) {
            return mDataList.get(position).getType();
        } else {
            return TYPE_TITLE;
        }
    }

    @Override
    public int getViewTypeCount() {
//        return super.getViewTypeCount();
        return 3;
    }

    @Override
    public boolean isEnabled(int position) {
//        return super.isEnabled(position);
        return (this.getItemViewType(position) != TYPE_TITLE);
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {

        AdapterRemoveData data = mDataList.get(position);
        ViewHolderTitle viewHolderTitle = null;
        ViewHolderRomeve viewHolderRomeve = null;
        ViewHolderInstall viewHolderInstall = null;

        switch (this.getItemViewType(position)) {
            case TYPE_TITLE: {
                if (null == contentView) {
                    contentView = LayoutInflater.from(context).inflate(R.layout.item_remove_type_title, null);
                    viewHolderTitle = new ViewHolderTitle();

                    viewHolderTitle.textViewTitle = contentView.findViewById(R.id.tv_item_remove_type_title);

                    contentView.setTag(viewHolderTitle);
                } else {
                    viewHolderTitle = (ViewHolderTitle) contentView.getTag();
                }

                if (RegularU.isEmpty(data.getTypeName())) {
                    viewHolderTitle.textViewTitle.setText("");
                } else {
                    viewHolderTitle.textViewTitle.setText(data.getTypeName());
                }
                break;
            }
            case TYPE_REMOVE: {
                if (null == contentView) {
                    contentView = LayoutInflater.from(context).inflate(R.layout.item_remove, null);
                    viewHolderRomeve = new ViewHolderRomeve();

                    viewHolderRomeve.textViewOnlineR = contentView.findViewById(R.id.tv_item_remove_remove_online);
                    viewHolderRomeve.textViewOfflineR = contentView.findViewById(R.id.tv_item_remove_remove_offline);
                    viewHolderRomeve.textViewOnlineRC = contentView.findViewById(R.id.tv_item_remove_complete_online);
                    viewHolderRomeve.textViewOfflineRC = contentView.findViewById(R.id.tv_item_remove_complete_offline);
                    viewHolderRomeve.tvGo = contentView.findViewById(R.id.tv_item_remove_go);
                    viewHolderRomeve.ivGo = contentView.findViewById(R.id.iv_item_remove_go);
                    viewHolderRomeve.frameLayout = contentView.findViewById(R.id.fl_item_remove);

                    contentView.setTag(viewHolderRomeve);
                } else {
                    viewHolderRomeve = (ViewHolderRomeve) contentView.getTag();
                }

                int wire = data.getOnline();
                int wireless = data.getOffline();
                int wireComplete = data.getOnlineComplete();
                int wirelessComplete = data.getOfflineComplete();

                viewHolderRomeve.textViewOnlineR.setText("" + wire);
                viewHolderRomeve.textViewOfflineR.setText("" + wireless);
                viewHolderRomeve.textViewOnlineRC.setText("" + wireComplete);
                viewHolderRomeve.textViewOfflineRC.setText("" + wirelessComplete);

                if (data.isComplete()) {
                    if (wire == wireComplete && wireless == wirelessComplete) {
                        viewHolderRomeve.ivGo.setVisibility(View.GONE);
                        viewHolderRomeve.tvGo.setVisibility(View.VISIBLE);
                    } else {
                        viewHolderRomeve.ivGo.setVisibility(View.VISIBLE);
                        viewHolderRomeve.tvGo.setVisibility(View.GONE);
                    }

                } else {
                    viewHolderRomeve.ivGo.setVisibility(View.VISIBLE);
                    viewHolderRomeve.tvGo.setVisibility(View.GONE);
                    viewHolderRomeve.frameLayout.setBackgroundResource(R.drawable.bg_item_installing_orange);
                }

                break;
            }
            case TYPE_INSTALL: {
                if (null == contentView) {
                    contentView = LayoutInflater.from(context).inflate(R.layout.item_installing, null);
                    viewHolderInstall = new ViewHolderInstall();

                    viewHolderInstall.textViewFrameNo = contentView.findViewById(R.id.tv_item_installing_frame_no);
                    viewHolderInstall.textViewOnlineP = contentView.findViewById(R.id.tv_item_installing_pending_online);
                    viewHolderInstall.textViewOfflineP = contentView.findViewById(R.id.tv_item_installing_pending_offline);
                    viewHolderInstall.textViewOnlinePC = contentView.findViewById(R.id.tv_item_installing_complete_online);
                    viewHolderInstall.textViewOfflinePC = contentView.findViewById(R.id.tv_item_installing_complete_offline);
                    viewHolderInstall.frameLayout = contentView.findViewById(R.id.fl_item_installing);
                    viewHolderInstall.ivGo = contentView.findViewById(R.id.iv_item_installing_go);
                    viewHolderInstall.tvGo = contentView.findViewById(R.id.tv_item_installing_go);
                    viewHolderInstall.tvPosition = contentView.findViewById(R.id.tv_item_installing_position);

                    contentView.setTag(viewHolderInstall);
                } else {
                    viewHolderInstall = (ViewHolderInstall) contentView.getTag();
                }

                int wire = data.getOnline();
                int wireless = data.getOffline();
                int wireComplete = data.getOnlineComplete();
                int wirelessComplete = data.getOfflineComplete();

                viewHolderInstall.textViewFrameNo.setText(data.getFrameNo());
                viewHolderInstall.tvPosition.setText("" + (position - 2));
                viewHolderInstall.textViewOnlineP.setText("" + wire);
                viewHolderInstall.textViewOfflineP.setText("" + wireless);
                viewHolderInstall.textViewOnlinePC.setText("" + wireComplete);
                viewHolderInstall.textViewOfflinePC.setText("" + wirelessComplete);

                if (position % 2 == 0) {
                    viewHolderInstall.frameLayout.setBackgroundResource(R.drawable.bg_item_installing_blue);
                } else {
                    viewHolderInstall.frameLayout.setBackgroundResource(R.drawable.bg_item_installing_green);
                }

                if (data.isComplete()) {
                    if (data.getOnlineComplete() == data.getOnline() && data.getOfflineComplete() == data.getOffline()) {
                        viewHolderInstall.ivGo.setVisibility(View.GONE);
                        viewHolderInstall.tvGo.setVisibility(View.VISIBLE);
                    } else {
                        viewHolderInstall.ivGo.setVisibility(View.VISIBLE);
                        viewHolderInstall.tvGo.setVisibility(View.GONE);
                    }
                } else {
                    viewHolderInstall.frameLayout.setBackgroundResource(R.drawable.bg_item_installing_orange);
                    viewHolderInstall.ivGo.setVisibility(View.VISIBLE);
                    viewHolderInstall.tvGo.setVisibility(View.GONE);
                }

                break;
            }
            default: {
                Log.i(TAG, "getView: default");
            }
        }

        return contentView;
    }

    private class ViewHolderTitle {
        private TextView textViewTitle;
    }

    private class ViewHolderRomeve {
        private TextView textViewOnlineR, textViewOfflineR, textViewOnlineRC, textViewOfflineRC;
        private ImageView ivGo;
        private TextView tvGo;
        private FrameLayout frameLayout;
    }

    private class ViewHolderInstall {
        private FrameLayout frameLayout;
        private TextView textViewFrameNo, textViewOnlineP, textViewOfflineP, textViewOnlinePC, textViewOfflinePC;
        private ImageView ivGo;
        private TextView tvGo;
        private TextView tvPosition;
    }
}
