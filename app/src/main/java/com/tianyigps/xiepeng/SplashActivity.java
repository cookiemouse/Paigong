package com.tianyigps.xiepeng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tianyigps.xiepeng.activity.ManagerFragmentContentActivity;
import com.tianyigps.xiepeng.activity.WorkerFragmentContentActivity;
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;

public class SplashActivity extends Activity {

    private ImageView mImageViewSplash;
    private Handler mHandler;
    private Runnable mRunnable;

    private SharedpreferenceManager mSharedpreferenceManager;

    private static final int DELAY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, DELAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onBackPressed() {
        //  注销掉返回键功能
//        super.onBackPressed();
    }

    //  初始化
    private void init() {
        mImageViewSplash = (ImageView) findViewById(R.id.iv_activity_splash);

        mSharedpreferenceManager = new SharedpreferenceManager(this);

        mHandler = new Handler();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (Data.DATA_S_STATE_2 == mSharedpreferenceManager.getLaunchMode()) {
                    toManager();
                    return;
                }
                toWorker();
//                toManager();
            }
        };
    }

    //  跳转到安装师傅
    private void toWorker() {
        Intent intent = new Intent(SplashActivity.this, WorkerFragmentContentActivity.class);
        startActivity(intent);
        this.finish();
    }

    //  跳转到主任
    private void toManager() {
        Intent intent = new Intent(SplashActivity.this, ManagerFragmentContentActivity.class);
        startActivity(intent);
        this.finish();
    }
}
