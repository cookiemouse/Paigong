package com.tianyigps.dispatch2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.bean.ModifyPasswordBean;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnModifyPasswordListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.MD5U;
import com.tianyigps.dispatch2.utils.SnackbarU;

import static com.tianyigps.dispatch2.data.Data.MSG_2;

public class ModifyPasswordActivity extends BaseActivity {

    private static final String TAG = "ModifyPasswordActivity";

    private EditText mEditTextOld, mEditTextNew, mEditTextEnsure;
    private Button mButtonEnsure;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;

    private SharedpreferenceManager mSharedpreferenceManager;

    private String mStringMessage = "";

    //  LoadingFragment
    private LoadingDialogFragment mLoadingDialogFragment;

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

        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();
        mSharedpreferenceManager = new SharedpreferenceManager(this);

        mLoadingDialogFragment = new LoadingDialogFragment();
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

                if (!newPsd.equals(ensurePsd)) {
                    mStringMessage = "两次密码输入不一致！";
                    showToast(mStringMessage);
                    return;
                }
                if (newPsd.length() < 8) {
                    mStringMessage = "密码至少8个字符！";
                    showToast(mStringMessage);
                    return;
                }
                if (newPsd.length() > 20){
                    mStringMessage = "密码超过20位！";
                    showToast(mStringMessage);
                    return;
                }
                showLoading();
                mNetworkManager.modifyPassword(userName, token, oldPsd, newPsd);
            }
        });

        mEditTextOld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mEditTextOld.setTextColor(getResources().getColor(R.color.colorText));
            }
        });

        mNetworkManager.setModifyPasswordListener(new OnModifyPasswordListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ModifyPasswordBean modifyPasswordBean = gson.fromJson(result, ModifyPasswordBean.class);
                if (!modifyPasswordBean.isSuccess()) {
                    mStringMessage = "原密码输入错误";
                    myHandler.sendEmptyMessage(Data.MSG_2);
                    return;
                }
                myHandler.sendEmptyMessage(Data.MSG_1);
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
        if (isFinishing()) {
            return;
        }
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

    //  显示Toast信息
    private void showToast(String message) {
        LinearLayout linearLayout = findViewById(R.id.activity_modify_password);
        View viewToast = LayoutInflater.from(ModifyPasswordActivity.this).inflate(R.layout.layout_top_toast, null);
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
                    //  修改成功，重新登陆
                    showLoginDialog();
                    break;
                }
                case MSG_2: {
                    //  原密码错误
                    showToast(mStringMessage);
                    mEditTextOld.setTextColor(getResources().getColor(R.color.colorOrange));
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
