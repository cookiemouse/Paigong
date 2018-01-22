package com.tianyigps.dispatch2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tianyigps.dispatch2.service.SpeakService;

/**
 * Created by cookiemouse on 2018/1/19.
 */

public class SpeakReceiver extends BroadcastReceiver {

    private static final String TAG = "SpeakReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");

        Intent intentService = new Intent(context, SpeakService.class);
        context.startService(intentService);
    }
}
