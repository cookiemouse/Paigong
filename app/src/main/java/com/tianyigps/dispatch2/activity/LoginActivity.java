package com.tianyigps.dispatch2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.bean.CheckUserBean;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.interfaces.OnCheckUserListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;

import cn.jpush.android.api.JPushInterface;

import static com.tianyigps.dispatch2.data.Data.MSG_1;
import static com.tianyigps.dispatch2.data.Data.MSG_ERO;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    private EditText mEditTextAccount, mEditTextPassword;
    private Button mButtonLogin;

    private Toast mToast;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;

    private SharedpreferenceManager mSharedpreferenceManager;
    private int launchMode = Data.DATA_LAUNCH_MODE_WORKER;
    private String launchAccount = "";

    private String mStringMessage = "数据请求失败";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_login);

        init();

        setEventListener();
    }

    private void init() {
        mEditTextAccount = findViewById(R.id.et_activity_login_account);
        mEditTextPassword = findViewById(R.id.et_activity_login_password);
        mButtonLogin = findViewById(R.id.btn_activity_login);

        mToast = Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT);

        mNetworkManager = NetworkManager.getInstance();
        myHandler = new MyHandler();

        mSharedpreferenceManager = new SharedpreferenceManager(getApplicationContext());
    }

    private void setEventListener() {
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/14 登录
                launchAccount = mEditTextAccount.getText().toString();
                String password = mEditTextPassword.getText().toString();

                if (null == launchAccount || "".equals(launchAccount)) {
                    showToast("请输入账号");
                    return;
                }
                if ("".equals(password)) {
                    showToast("请输入密码");
                }

                mNetworkManager.checkUser(launchAccount, password, "");
            }
        });

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
                    mStringMessage = checkUserBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                CheckUserBean.ObjBean objBean = checkUserBean.getObj();

                // TODO: 2017/8/7 先进入worker页面
                launchMode = 1;

                mSharedpreferenceManager.saveUserData(objBean.getEid()
                        , objBean.getToken()
                        , objBean.getName()
                        , objBean.getHeadPhone()
                        , objBean.getJobNo()
                        , objBean.getImgBaseUrl()
                        , launchMode);

                mSharedpreferenceManager.saveAccount(launchAccount);

                myHandler.sendEmptyMessage(MSG_1);
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
        View viewToast = LayoutInflater.from(LoginActivity.this).inflate(R.layout.layout_top_toast, null);
        TextView textViewInfo = viewToast.findViewById(R.id.tv_layout_top_toast);
        textViewInfo.setText(message);
        mToast.setView(viewToast);
        mToast.setGravity(Gravity.TOP, 0, 0);
        mToast.show();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ERO: {
                    showToast(mStringMessage);
                    break;
                }
                case MSG_1: {
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
