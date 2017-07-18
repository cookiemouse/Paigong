package com.tianyigps.xiepeng.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.tianyigps.xiepeng.data.Data;

import static com.tianyigps.xiepeng.data.Data.DATA_JSON_EID;
import static com.tianyigps.xiepeng.data.Data.DATA_JSON_HEAD_PHONE;
import static com.tianyigps.xiepeng.data.Data.DATA_JSON_JOB_NO;
import static com.tianyigps.xiepeng.data.Data.DATA_JSON_NAME;
import static com.tianyigps.xiepeng.data.Data.DATA_JSON_TOKEN;
import static com.tianyigps.xiepeng.data.Data.DATA_LAUNCH_ACCOUNT;
import static com.tianyigps.xiepeng.data.Data.DATA_LAUNCH_MODE;
import static com.tianyigps.xiepeng.data.Data.DATA_LAUNCH_MODE_WORKER;
import static com.tianyigps.xiepeng.data.Data.DATA_SHAREDPREFERENCES;
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
        editor.putInt(DATA_LAUNCH_MODE, state);
        editor.apply();
    }

    //  获取登陆模式
    public int getLaunchMode() {
        return mSharedPreferences.getInt(DATA_LAUNCH_MODE, DATA_LAUNCH_MODE_WORKER);
    }

    //  保存eid
    public void saveEid(int eid) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(DATA_JSON_EID, eid);
        editor.apply();
    }

    //  获取Eid
    public int getEid() {
//        return mSharedPreferences.getInt(DATA_JSON_EID, 0);
        return Data.EID;
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

    //  保存账号
    public void saveAccount(String account) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Data.DATA_LAUNCH_ACCOUNT, account);
        editor.apply();
    }

    //  获取账号
    public String getAccount() {
        return mSharedPreferences.getString(DATA_LAUNCH_ACCOUNT, "");
    }

    //  保存名字
    public void saveName(String name) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(DATA_JSON_NAME, name);
        editor.apply();
    }

    //  获取名字
    public String getName() {
        return mSharedPreferences.getString(DATA_JSON_NAME, "");
    }

    //  服务主任号码
    public void saveHeadPhone(String phone) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(DATA_JSON_HEAD_PHONE, phone);
        editor.apply();
    }

    //  获取服务主任号码
    public String getHeadPhone() {
        return mSharedPreferences.getString(DATA_JSON_HEAD_PHONE, "");
    }

    //  保存工号
    public void saveJobNo(String jobNo) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(DATA_JSON_JOB_NO, jobNo);
        editor.apply();
    }

    //  获取工号
    public String getJobNo() {
        return mSharedPreferences.getString(DATA_JSON_JOB_NO, "");
    }

    public void saveUserData(int eid, String token, String name, String headPhone, String jobNo
            , int launchMode) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(DATA_JSON_EID, eid);
        editor.putString(DATA_JSON_TOKEN, token);
        editor.putString(DATA_JSON_NAME, name);
        editor.putString(DATA_JSON_HEAD_PHONE, headPhone);
        editor.putString(DATA_JSON_JOB_NO, jobNo);
        editor.putInt(DATA_LAUNCH_MODE, launchMode);
        editor.apply();
    }
}
