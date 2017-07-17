package com.tianyigps.xiepeng.manager;

import android.content.Context;
import android.content.SharedPreferences;

import static com.tianyigps.xiepeng.data.Data.DATA_JSON_EID;
import static com.tianyigps.xiepeng.data.Data.DATA_JSON_TOKEN;
import static com.tianyigps.xiepeng.data.Data.DATA_SHAREDPREFERENCES;
import static com.tianyigps.xiepeng.data.Data.DATA_S_STATE;
import static com.tianyigps.xiepeng.data.Data.DATA_S_STATE_1;
import static com.tianyigps.xiepeng.data.Data.DEFAULT_TOKEN;

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

    //  保存eid
    public void saveEid(int eid) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(DATA_JSON_EID, eid);
        editor.apply();
    }

    //  获取Eid
    public int getEid() {
        return mSharedPreferences.getInt(DATA_JSON_EID, 0);
    }

    //  保存token
    public void saveToken(String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(DATA_JSON_TOKEN, token);
        editor.apply();
    }

    //  获取token
    public String getToken() {
        return mSharedPreferences.getString(DATA_JSON_TOKEN, DEFAULT_TOKEN);
    }
}
