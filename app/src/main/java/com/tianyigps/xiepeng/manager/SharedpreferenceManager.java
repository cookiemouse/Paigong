package com.tianyigps.xiepeng.manager;

import android.content.Context;
import android.content.SharedPreferences;

import static com.tianyigps.xiepeng.data.Data.DATA_SHAREDPREFERENCES;
import static com.tianyigps.xiepeng.data.Data.DATA_S_STATE;
import static com.tianyigps.xiepeng.data.Data.DATA_S_STATE_1;

/**
 * Created by djc on 2017/7/10.
 */

public class SharedpreferenceManager {

    private SharedPreferences mSharedPreferences;

    public SharedpreferenceManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(DATA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
    }

    //  保存登陆模式
    public void saveLaunchMode(int state) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(DATA_S_STATE, state);
        editor.apply();
    }

    //  获取登陆模式
    public int getLaunchMode() {
        return mSharedPreferences.getInt(DATA_S_STATE, DATA_S_STATE_1);
    }
}
