package com.tianyigps.dispatch2.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.tianyigps.dispatch2.receiver.OpenActivityReceiver;
import com.tianyigps.dispatch2.receiver.SpeakReceiver;

/**
 * Created by cookiemouse on 2018/1/19.
 */

public class MyAlarmManager {

    private static final String TAG = "MyAlarmManager";

    private AlarmManager mAlarmManager;
    private Context mContext;

    public MyAlarmManager(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void start() {
        int min = 1 * 60 * 1000;  // 300秒
        long triggerAtTime = SystemClock.elapsedRealtime() + min;
        Intent intent = new Intent(mContext, SpeakReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 11, intent, 0);
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        Log.i(TAG, "start: ");
    }

    public void cancle() {
        Intent intent = new Intent(mContext, OpenActivityReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 11, intent, 0);
        mAlarmManager.cancel(pi);
    }
}
