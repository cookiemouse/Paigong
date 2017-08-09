package com.tianyigps.xiepeng.interfaces;

import com.tianyigps.xiepeng.base.BaseInterface;

/**
 * Created by cookiemouse on 2017/8/9.
 */

public interface OnPendedListener extends BaseInterface {
    @Override
    void onFailure();

    @Override
    void onSuccess(String result);
}
