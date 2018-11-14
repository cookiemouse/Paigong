package com.tianyigps.dispatch2.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.manager.LocateManager;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.TimerU;

public class PositionService extends Service {

    private static final String TAG = "PService";

    private TimerU mTimerU;

    private SharedpreferenceManager mSharedpreferenceManager;
    private NetworkManager mNetworkManager;

    private LocateManager mLocateManager;

    //  CPU锁
    private PowerManager.WakeLock mWakeLock = null;

    public PositionService() {
        mTimerU = new TimerU(120);
        mNetworkManager = new NetworkManager();

        mTimerU.setOnTickListener(new TimerU.OnTickListener() {
            @Override
            public void onTick(int time) {
            }

            @Override
            public void onEnd() {
                Log.i(TAG, "onEnd: ");
                mLocateManager.startLocate();
                mTimerU.start();
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedpreferenceManager = new SharedpreferenceManager(getApplicationContext());
        mLocateManager = new LocateManager(getApplicationContext());

        mLocateManager.setOnReceiveLocationListener(new LocateManager.OnReceiveLocationListener() {
            @Override
            public void onReceive(LatLng latLng) {
                uploadData(latLng);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != mTimerU) {
            mTimerU.start();
        }
        acquireWakeLock();
        foregroundService();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        releaseWakeLock();
        if (null != mLocateManager) {
            mLocateManager.stopLocate();
        }
        super.onDestroy();
    }

    //  前台服务
    private void foregroundService() {
        //启用前台服务，主要是startForeground()
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText("定位中...");
        Notification notification = builder.getNotification();
        startForeground(Data.NOTIFICATION_ID, notification);
    }

    //申请设备电源锁
    public void acquireWakeLock() {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (null != pm) {
                mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "WakeLock");
                if (null != mWakeLock) {
                    mWakeLock.acquire();
                }
            }
        }
    }

    //释放设备电源锁
    public void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    //  上传数据
    private void uploadData(LatLng latLng) {
        if (null == mSharedpreferenceManager) {
            return;
        }
        if (null == mNetworkManager) {
            return;
        }
        String eid = mSharedpreferenceManager.getEid() + "";
        String token = mSharedpreferenceManager.getToken();
        if ("0".equals(eid)){
            return;
        }
        Log.i(TAG, "uploadData: eid-->" + eid);
        Log.i(TAG, "uploadData: token-->" + token);
        Log.i(TAG, "uploadData: latitude-->" + latLng.latitude);
        Log.i(TAG, "uploadData: longitude-->" + latLng.longitude);
        mNetworkManager.postPosition(eid, token, latLng.latitude + "", latLng.longitude + "");
    }
}
