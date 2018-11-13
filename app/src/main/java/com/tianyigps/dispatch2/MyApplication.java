package com.tianyigps.dispatch2;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.tianyigps.dispatch2.services.PositionService;

/**
 * Created by cookiemouse on 2017/9/19.
 */

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    private int mCountLive = 0;
    private boolean mServiceOpened = false;

    private Intent mServiceIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        mServiceIntent = new Intent(this, PositionService.class);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mCountLive++;
                if (!mServiceOpened) {
                    startService(mServiceIntent);
                    mServiceOpened = true;
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mCountLive--;
                if (0 == mCountLive) {
                    stopService(mServiceIntent);
                }
            }
        });
    }
}
