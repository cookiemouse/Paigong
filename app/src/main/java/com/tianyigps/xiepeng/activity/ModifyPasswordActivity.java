package com.tianyigps.xiepeng.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.interfaces.OnModifyPasswordListener;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;
import com.tianyigps.xiepeng.utils.MD5U;

import static com.tianyigps.xiepeng.data.Data.MSG_1;
import static com.tianyigps.xiepeng.data.Data.MSG_ERO;

public class ModifyPasswordActivity extends BaseActivity {

    private static final String TAG = "ModifyPasswordActivity";

    private EditText mEditTextOld, mEditTextNew, mEditTextEnsure;
    private Button mButtonEnsure;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;

    private SharedpreferenceManager mSharedpreferenceManager;

    private String mStringMessage;

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

                oldPsd = MD5U.getMd5(oldPsd);

                if (newPsd.equals(ensurePsd)) {
                    mStringMessage = "两次密码输入不同，请检查后提交！";
                    showMessageDialog(mStringMessage);
                    return;
                }
                mNetworkManager.modifyPassword(userName, token, oldPsd, newPsd);
            }
        });

        mNetworkManager.setModifyPasswordListener(new OnModifyPasswordListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ModifyPasswordBean modifyPasswordBean = gson.fromJson(result, ModifyPasswordBean.class);
                if (!modifyPasswordBean.isSuccess()) {
                    mStringMessage = modifyPasswordBean.getMsg();
                    myHandler.sendEmptyMessage(MSG_ERO);
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

    //  显示修改成功对话框
    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ModifyPasswordActivity.this);
        builder.setMessage("修改密码成功，需重新登陆");
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                toLogin();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //  显示信息对话框
    private void showMessageDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ModifyPasswordActivity.this);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  do nothing
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
                    showMessageDialog(mStringMessage);
                    break;
                }
                case MSG_1: {
                    showLoginDialog();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
