package com.tianyigps.xiepeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.bean.ModifyPasswordBean;
import com.tianyigps.xiepeng.interfaces.OnModifyPasswordListener;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;

import static com.tianyigps.xiepeng.data.Data.MSG_1;
import static com.tianyigps.xiepeng.data.Data.MSG_ERO;

public class ModifyPasswordActivity extends BaseActivity {

    private static final String TAG = "ModifyPasswordActivity";

    private EditText mEditTextOld, mEditTextNew, mEditTextEnsure;
    private Button mButtonEnsure;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;

    private SharedpreferenceManager mSharedpreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);

        init();

        setEventListener();
    }

    private void init() {
        mEditTextOld = findViewById(R.id.et_activity_modify_password_old);
        mEditTextNew = findViewById(R.id.et_activity_modify_password_new);
        mEditTextEnsure = findViewById(R.id.et_activity_modify_password_new_ensure);
        mButtonEnsure = findViewById(R.id.btn_activity_modify_password_ensure);

        mNetworkManager = NetworkManager.getInstance();
        myHandler = new MyHandler();
        mSharedpreferenceManager = new SharedpreferenceManager(this);
    }

    private void setEventListener() {

        mButtonEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPsd = mEditTextOld.getText().toString();
                String newPsd = mEditTextNew.getText().toString();
                String ensurePsd = mEditTextEnsure.getText().toString();
                String userName = mSharedpreferenceManager.getAccount();
                String token = mSharedpreferenceManager.getToken();
                if (newPsd.equals(ensurePsd)) {
                    mNetworkManager.modifyPassword(userName, token, oldPsd, newPsd);
                }
            }
        });

        mNetworkManager.setModifyPasswordListener(new OnModifyPasswordListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ModifyPasswordBean modifyPasswordBean = gson.fromJson(result, ModifyPasswordBean.class);
                if (!modifyPasswordBean.isSuccess()) {
                    onFailure();
                    return;
                }
                myHandler.sendEmptyMessage(MSG_1);
            }
        });
    }

    //  跳转到登陆页
    private void toLogin() {
        Intent intent = new Intent(ModifyPasswordActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ERO: {
                    break;
                }
                case MSG_1: {
                    toLogin();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
