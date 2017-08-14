package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.AdapterRemoveData;

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
        return mDataList.get(position).getType();
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

                viewHolderTitle.textViewTitle.setText(data.getTypeName());
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

                    contentView.setTag(viewHolderRomeve);
                } else {
                    viewHolderRomeve = (ViewHolderRomeve) contentView.getTag();
                }

                viewHolderRomeve.textViewOnlineR.setText("" + data.getOnline());
                viewHolderRomeve.textViewOfflineR.setText("" + data.getOffline());
                viewHolderRomeve.textViewOnlineRC.setText("" + data.getOnlineComplete());
                viewHolderRomeve.textViewOfflineRC.setText("" + data.getOfflineComplete());
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

                    contentView.setTag(viewHolderInstall);
                } else {
                    viewHolderInstall = (ViewHolderInstall) contentView.getTag();
                }

                viewHolderInstall.textViewFrameNo.setText(data.getFrameNo());
                viewHolderInstall.textViewOnlineP.setText("" + data.getOnline());
                viewHolderInstall.textViewOfflineP.setText("" + data.getOffline());
                viewHolderInstall.textViewOnlinePC.setText("" + data.getOnlineComplete());
                viewHolderInstall.textViewOfflinePC.setText("" + data.getOfflineComplete());

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
    }

    private class ViewHolderInstall {
        private TextView textViewFrameNo, textViewOnlineP, textViewOfflineP, textViewOnlinePC, textViewOfflinePC;
    }
}
