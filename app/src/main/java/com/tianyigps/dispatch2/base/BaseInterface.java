package com.tianyigps.dispatch2.base;

/**
 * Created by djc on 2017/7/17.
 */

public interface BaseInterface {
    void onFailure();

    void onSuccess(String result);
}
