package com.tianyigps.dispatch2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.tianyigps.dispatch2.activity.LoginActivity;
import com.tianyigps.dispatch2.activity.ManagerFragmentContentActivity;
import com.tianyigps.dispatch2.activity.WorkerFragmentContentActivity;
import com.tianyigps.dispatch2.bean.CheckUserBean;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.interfaces.OnCheckUserListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.BitmapU;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

import static com.tianyigps.dispatch2.data.Data.DATA_LAUNCH_MODE_WORKER;
import static com.tianyigps.dispatch2.data.Data.MSG_1;
import static com.tianyigps.dispatch2.data.Data.MSG_ERO;

public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";

    private ImageView mImageViewSplash;
    private Runnable mRunnable;

    private SharedpreferenceManager mSharedpreferenceManager;

    private static final int DELAY = 500;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private int launchMode = DATA_LAUNCH_MODE_WORKER;

    private String userName;
    private String token;

    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        //百度地图
        SDKInitializer.initialize(getApplicationContext());
        //极光推送
//        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

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
    protected void onStop() {
        if (null != mBitmap) {
            mBitmap.recycle();
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        //  注销掉返回键功能
//        super.onBackPressed();
    }

    //  初始化
    private void init() {
        mImageViewSplash = findViewById(R.id.iv_activity_splash);

        mBitmap = BitmapU.getBitmap(this, R.drawable.bg_splash);
        mImageViewSplash.setImageBitmap(mBitmap);

        mSharedpreferenceManager = new SharedpreferenceManager(this);
        userName = mSharedpreferenceManager.getAccount();
        token = mSharedpreferenceManager.getToken();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mNetworkManager.checkUser(userName, "", token);
            }
        };

        mNetworkManager = new NetworkManager();
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

                CheckUserBean.ObjBean objBean = checkUserBean.getObj();

                launchMode = objBean.getDuties();

                mSharedpreferenceManager.saveUserData(objBean.getEid()
                        , objBean.getToken()
                        , objBean.getName()
                        , objBean.getHeadPhone()
                        , objBean.getJobNo()
                        , objBean.getImgBaseUrl()
                        , launchMode);

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
                    JPushInterface.setAlias(SplashActivity.this, 0, userName);
                    if (Data.DATA_LAUNCH_MODE_WORKER == launchMode) {
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
