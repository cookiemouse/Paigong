package com.tianyigps.dispatch2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.SplashActivity;
import com.tianyigps.dispatch2.activity.OrderDetailsActivity;
import com.tianyigps.dispatch2.activity.PendDetailsActivity;
import com.tianyigps.dispatch2.bean.PersonBean;
import com.tianyigps.dispatch2.data.Data;

import cn.jpush.android.api.JPushInterface;

public class OpenActivityReceiver extends BroadcastReceiver {

    private static final String TAG = "OpenActivityReceiver";

    private String orderNo;

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i(TAG, "onReceive: ");
        Bundle bundle = intent.getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
        int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        String file = bundle.getString(JPushInterface.EXTRA_MSG_ID);

        Log.i(TAG, "onReceive: title-->" + title);
        Log.i(TAG, "onReceive: content-->" + content);
        Log.i(TAG, "onReceive: type-->" + type);
        Log.i(TAG, "onReceive: notificationId-->" + notificationId);
        Log.i(TAG, "onReceive: file-->" + file);

        Gson gson = new Gson();
        PersonBean personBean = gson.fromJson(type, PersonBean.class);
        String person = personBean.getPerson();

        String splits[] = content.split("，");
        if (splits.length < 1){
            splits = content.split(",");
        }
        if (splits.length > 1) {
            orderNo = splits[1];
        }
        Log.i(TAG, "onReceive: orderNo-->" + orderNo);

        switch (person) {
            case "1": {
                toOrderDetails();
                break;
            }
            case "2": {
                toPendDetails();
                break;
            }
            default: {
                toSplash();
                Log.i(TAG, "onReceive: default-->" + person);
            }
        }
    }

    private void toSplash() {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void toOrderDetails() {
        if (null == orderNo) {
            toSplash();
            return;
        }
        Intent intent = new Intent(context, OrderDetailsActivity.class);
        intent.putExtra(Data.DATA_INTENT_ORDER_NO, orderNo);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void toPendDetails() {
        if (null == orderNo) {
            toSplash();
            return;
        }
        Intent intent = new Intent(context, PendDetailsActivity.class);
        intent.putExtra(Data.DATA_INTENT_ORDER_NO, orderNo);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
