package com.tianyigps.xiepeng;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.tianyigps.xiepeng.activity.LoginActivity;
import com.tianyigps.xiepeng.activity.ManagerFragmentContentActivity;
import com.tianyigps.xiepeng.activity.WorkerFragmentContentActivity;
import com.tianyigps.xiepeng.bean.CheckUserBean;
import com.tianyigps.xiepeng.interfaces.OnCheckUserListener;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import static com.tianyigps.xiepeng.data.Data.DATA_LAUNCH_MODE_WORKER;
import static com.tianyigps.xiepeng.data.Data.MSG_1;
import static com.tianyigps.xiepeng.data.Data.MSG_ERO;

public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";

    private ImageView mImageViewSplash;
    private Runnable mRunnable;

    private SharedpreferenceManager mSharedpreferenceManager;

    private static final int DELAY = 500;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private int launchMode = DATA_LAUNCH_MODE_WORKER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();

        setEventListener();

        applyPermiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myHandler.removeCallbacks(mRunnable);
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

        mRunnable = new Runnable() {
            @Override
            public void run() {
                String account = mSharedpreferenceManager.getAccount();
                String token = mSharedpreferenceManager.getToken();
                mNetworkManager.checkUser(account, "", token);
            }
        };

        mNetworkManager = NetworkManager.getInstance();
        myHandler = new MyHandler();
    }

    //  设置事件监听
    private void setEventListener() {
        mNetworkManager.setCheckUserListener(new OnCheckUserListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                CheckUserBean checkUserBean = gson.fromJson(result, CheckUserBean.class);
                if (!checkUserBean.isSuccess()) {
                    onFailure();
                    return;
                }

//                launchMode = checkUserBean.getObj().getDuties();
                launchMode = 1;

                myHandler.sendEmptyMessage(MSG_1);
            }
        });
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

    //  跳转到登陆页
    private void toLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    //  运行时权限

    private void applyPermiss() {
        AndPermission
                .with(SplashActivity.this)
                .requestCode(100)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.CAMERA)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(SplashActivity.this, rationale).show();
                    }
                })
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        myHandler.postDelayed(mRunnable, DELAY);
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(SplashActivity.this, deniedPermissions)) {
                            showMessageDialog();
                        }
                    }
                }).start();
    }

    private void showMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setMessage("应用权限被禁止，请打开相关权限");
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SplashActivity.this.finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ERO: {
                    toLogin();
                    break;
                }
                case MSG_1: {
                    if (DATA_LAUNCH_MODE_WORKER == launchMode) {
                        toWorker();
                        break;
                    }
                    toManager();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
