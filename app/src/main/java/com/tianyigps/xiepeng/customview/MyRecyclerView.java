package com.tianyigps.xiepeng.customview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by cookiemouse on 2017/7/24.
 */

public class MyRecyclerView extends RecyclerView {

    public MyRecyclerView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, expandSpec);
//        super.onMeasure(widthSpec, heightSpec);
    }
}
