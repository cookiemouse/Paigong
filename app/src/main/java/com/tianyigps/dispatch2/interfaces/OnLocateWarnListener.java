package com.tianyigps.dispatch2.interfaces;

import com.tianyigps.dispatch2.base.BaseInterface;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public interface OnLocateWarnListener extends BaseInterface {
    @Override
    void onFailure();

    @Override
    void onSuccess(String result);
}
