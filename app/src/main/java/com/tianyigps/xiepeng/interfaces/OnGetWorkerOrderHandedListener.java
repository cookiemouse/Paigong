package com.tianyigps.xiepeng.interfaces;

import com.tianyigps.xiepeng.base.BaseInterface;

/**
 * Created by djc on 2017/7/17.
 */

public interface OnGetWorkerOrderHandedListener extends BaseInterface{
    @Override
    void onFailure();

    @Override
    void onSuccess(String result);
}
