package com.tianyigps.xiepeng.interfaces;

import com.tianyigps.xiepeng.base.BaseInterface;

/**
 * Created by cookiemouse on 2017/8/8.
 */

public interface OnInstallCountListener extends BaseInterface {
    @Override
    void onFailure();

    @Override
    void onSuccess(String result);
}
