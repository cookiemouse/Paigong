package com.tianyigps.dispatch2.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.bean.CheckUserBean;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnCheckUserListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.SnackbarU;

import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText mEditTextAccount, mEditTextPassword;
    private Button mButtonLogin;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;

    private SharedpreferenceManager mSharedpreferenceManager;
    private int launchMode = Data.DATA_LAUNCH_MODE_WORKER;
    private String launchAccount = "";

    private String mStringMessage = "数据请求失败";

    //  LoadingFragment
    private LoadingDialogFragment mLoadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏

        //  透明状态栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //  透明标题栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_login);

        init();

        setEventListener();
    }

    private void init() {
        mEditTextAccount = (EditText) findViewById(R.id.et_activity_login_account);
        mEditTextPassword = (EditText) findViewById(R.id.et_activity_login_password);
        mButtonLogin = (Button) findViewById(R.id.btn_activity_login);

        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();

        mSharedpreferenceManager = new SharedpreferenceManager(getApplicationContext());
        mLoadingDialogFragment = new LoadingDialogFragment();

        String account = mSharedpreferenceManager.getAccount();
        mEditTextAccount.setText(account);
    }

    private void setEventListener() {
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/14 登录
                launchAccount = mEditTextAccount.getText().toString();
                String password = mEditTextPassword.getText().toString();

                if (null == launchAccount || "".equals(launchAccount)) {
                    showToast("请输入账号");
                    return;
                }
                if ("".equals(password)) {
                    showToast("请输入密码");
                    return;
                }

                showLoading();
                mNetworkManager.checkUser(launchAccount, password, "");
            }
        });

        mNetworkManager.setCheckUserListener(new OnCheckUserListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                CheckUserBean checkUserBean = gson.fromJson(result, CheckUserBean.class);

                if (!checkUserBean.isSuccess()) {
                    mStringMessage = checkUserBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                CheckUserBean.ObjBean objBean = checkUserBean.getObj();

                launchMode = objBean.getDuties();

                if (launchMode == Data.DATA_LAUNCH_MODE_WORKER) {
                    mSharedpreferenceManager.saveUserData(objBean.getEid()
                            , objBean.getToken()
                            , objBean.getName()
                            , objBean.getDirectorPhone()
                            , objBean.getHeadPhone()
                            , objBean.getJobNo()
                            , objBean.getImgBaseUrl()
                            , launchMode);
                } else {
                    mSharedpreferenceManager.saveUserData(objBean.getEid()
                            , objBean.getToken()
                            , objBean.getName()
                            , objBean.getPhoneNo()
                            , objBean.getHeadPhone()
                            , objBean.getJobNo()
                            , objBean.getImgBaseUrl()
                            , launchMode);
                }
                String headPhoneList = "";
                if (null != objBean.getHeadPhoneList()) {
                    for (CheckUserBean.ObjBean.HeadPhoneList phoneList : objBean.getHeadPhoneList()) {
                        headPhoneList += phoneList.getContactPhone() + ",";
                    }
                }
                mSharedpreferenceManager.saveHeadPhoneList(headPhoneList);

                mSharedpreferenceManager.saveAccount(launchAccount);

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });
    }

    //  跳转到安装师傅
    private void toWorker() {
        Intent intent = new Intent(LoginActivity.this, WorkerFragmentContentActivity.class);
        startActivity(intent);
        this.finish();
    }

    //  跳转到主任
    private void toManager() {
        Intent intent = new Intent(LoginActivity.this, ManagerFragmentContentActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void showToast(String message) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_login);
        View viewToast = LayoutInflater.from(LoginActivity.this).inflate(R.layout.layout_top_toast, null);
        TextView textViewInfo = viewToast.findViewById(R.id.tv_layout_top_toast);
        textViewInfo.setText(message);
        new SnackbarU()
                .make(linearLayout, viewToast, Snackbar.LENGTH_SHORT)
                .setBackground(Color.WHITE)
                .setGravity(Gravity.TOP)
                .show();
    }

    //  显示LoadingFragment
    private void showLoading() {
        mLoadingDialogFragment.show(getFragmentManager(), "LoadingFragment");
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mLoadingDialogFragment.isAdded()) {
                mLoadingDialogFragment.dismissAllowingStateLoss();
            }
            switch (msg.what) {
                case Data.MSG_ERO: {
                    showToast(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    Log.i(TAG, "handleMessage: launchMode-->" + launchMode);
                    JPushInterface.setAlias(LoginActivity.this, 0, launchAccount);
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
