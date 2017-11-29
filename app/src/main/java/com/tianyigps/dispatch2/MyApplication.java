package com.tianyigps.dispatch2;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by cookiemouse on 2017/9/19.
 */

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    private boolean mIsToBack = false, mIsInBack = true;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                Log.i(TAG, "onActivityCreated: activity-->" + activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.i(TAG, "onActivityStarted: activity-->" + activity);
                if (mIsInBack) {
                    Log.i(TAG, "onActivityStarted: 进入前台");
                }
                mIsToBack = false;
                mIsInBack = false;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.i(TAG, "onActivityResumed: activity-->" + activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.i(TAG, "onActivityPaused: activity-->" + activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.i(TAG, "onActivityStopped: activity-->" + activity);
                if (mIsToBack) {
                    mIsInBack = true;
                    Log.i(TAG, "onActivityStopped: 进入后台");
                }
                mIsToBack = true;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                Log.i(TAG, "onActivitySaveInstanceState: activity-->" + activity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.i(TAG, "onActivityDestroyed: activity-->" + activity);
            }
        });
    }
}
