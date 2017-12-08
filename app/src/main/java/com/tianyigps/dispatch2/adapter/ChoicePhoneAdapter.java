package com.tianyigps.dispatch2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;

import java.util.List;

/**
 * Created by cookiemouse on 2017/12/8.
 */

public class ChoicePhoneAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mStringList;

    public ChoicePhoneAdapter(Context context, List<String> mStringList) {
        this.mContext = context;
        this.mStringList = mStringList;
    }

    @Override
    public int getCount() {
        return mStringList.size();
    }

    @Override
    public Object getItem(int i) {
        return mStringList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        String phone = mStringList.get(i);
        ViewHolder viewHolder = null;

        if (null == view) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_choice_phone, null);
            viewHolder.tvPhone = view.findViewById(R.id.tv_item_choice_phone);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvPhone.setText(phone);

        return view;
    }

    private class ViewHolder {
        TextView tvPhone;
    }
}
