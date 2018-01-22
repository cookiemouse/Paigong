package com.tianyigps.dispatch2.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.manager.MyAlarmManager;

/**
 * Created by cookiemouse on 2018/1/22.
 */

public class SpeakService extends Service {

    private static final String TAG = "SpeakService";

    //  科大讯飞
    private SpeechSynthesizer mSpeechSynthesizer;

    private SpeakBinder speakBinder = new SpeakBinder();

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        openNotification();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return speakBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        closeNotification();
        super.onDestroy();
    }

    //  打开前台通知
    private void openNotification() {
        Log.i(TAG, "openNotification: ");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle(getResources().getString(R.string.app_name));
        builder.setContentText("正在安装");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = builder.build();

        startForeground(Data.NOTIFICATION_ID, notification);
    }

    //  关闭前台通知
    private void closeNotification() {
        Log.i(TAG, "closeNotification: ");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (null != notificationManager) {
            notificationManager.cancel(Data.NOTIFICATION_ID);
        }
    }

    public void speak(String msg) {
        Log.i(TAG, "speak: ");
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=575f6945");
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(getApplicationContext(), new InitListener() {
            @Override
            public void onInit(int i) {
                Log.i(TAG, "onInit: ");

                mSpeechSynthesizer.startSpeaking("安装订单 T Y20180119155546535 即将超时", new SynthesizerListener() {
                    @Override
                    public void onSpeakBegin() {
                        Log.i(TAG, "onSpeakBegin: ");
                    }

                    @Override
                    public void onBufferProgress(int i, int i1, int i2, String s) {
                        Log.i(TAG, "onBufferProgress: ");
                    }

                    @Override
                    public void onSpeakPaused() {
                        Log.i(TAG, "onSpeakPaused: ");
                    }

                    @Override
                    public void onSpeakResumed() {
                        Log.i(TAG, "onSpeakResumed: ");
                    }

                    @Override
                    public void onSpeakProgress(int i, int i1, int i2) {
                        Log.i(TAG, "onSpeakProgress: ");
                    }

                    @Override
                    public void onCompleted(SpeechError speechError) {
                        Log.i(TAG, "onCompleted: ");
                        mSpeechSynthesizer.destroy();
                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {
                        Log.i(TAG, "onEvent: ");
                    }
                });
            }
        });

        new MyAlarmManager(getApplicationContext()).cancle();
    }

    public class SpeakBinder extends Binder {
        public SpeakService getService(){
            return SpeakService.this;
        }
    }
}
