package com.tianyigps.xiepeng.interfaces;

import com.tianyigps.xiepeng.base.BaseInterface;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public interface OnPendingOrderListener extends BaseInterface{
    @Override
    void onFailure();

    @Override
    void onSuccess(String result);
}
