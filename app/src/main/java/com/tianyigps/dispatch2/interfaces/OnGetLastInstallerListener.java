package com.tianyigps.dispatch2.interfaces;

import com.tianyigps.dispatch2.base.BaseInterface;

/**
 * Created by djc on 2017/7/17.
 */

public interface OnGetLastInstallerListener extends BaseInterface{
    @Override
    void onFailure();

    @Override
    void onSuccess(String result);
}
