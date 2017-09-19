package com.tianyigps.dispatch2;

import android.app.Application;

/**
 * Created by cookiemouse on 2017/9/19.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }
}
