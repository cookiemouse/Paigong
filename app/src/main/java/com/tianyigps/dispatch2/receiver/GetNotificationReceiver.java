package com.tianyigps.dispatch2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.bean.PersonBean;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.RegularU;

import cn.jpush.android.api.JPushInterface;

public class GetNotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "GetNotificationReceiver";

    private SharedpreferenceManager mSharedManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        mSharedManager = new SharedpreferenceManager(context);
        Log.i(TAG, "onReceive: ");
        Bundle bundle = intent.getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
        int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        String file = bundle.getString(JPushInterface.EXTRA_MSG_ID);

        Log.i(TAG, "onReceive: bundle-->" + bundle.toString());
        Log.i(TAG, "onReceive: title-->" + title);
        Log.i(TAG, "onReceive: content-->" + content);
        Log.i(TAG, "onReceive: type-->" + type);
        Log.i(TAG, "onReceive: notificationId-->" + notificationId);
        Log.i(TAG, "onReceive: file-->" + file);

        Gson gson = new Gson();
        PersonBean personBean = gson.fromJson(type, PersonBean.class);
        String orderId = personBean.getOrderId4New();

        if (RegularU.isEmpty(orderId)) {
            return;
        }
        Log.i(TAG, "onReceive: orderId-->" + orderId);
        mSharedManager.savePushOrderId(orderId);
    }
}
