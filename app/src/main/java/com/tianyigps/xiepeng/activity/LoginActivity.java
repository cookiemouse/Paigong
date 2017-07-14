package com.tianyigps.xiepeng.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tianyigps.xiepeng.R;

public class LoginActivity extends Activity {

    private EditText mEditTextAccount, mEditTextPassword;
    private Button mButtonLogin;

    private Toast mToast;

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
    }

    private void setEventListener() {
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/14 登录
                showToast("请输入密码");
            }
        });
    }

    private void showToast(String message) {
        View viewToast = LayoutInflater.from(LoginActivity.this).inflate(R.layout.layout_top_toast, null);
        TextView textViewInfo = viewToast.findViewById(R.id.tv_layout_top_toast);
        textViewInfo.setText(message);
        mToast.setView(viewToast);
        mToast.setGravity(Gravity.TOP, 0, 0);
        mToast.show();
    }
}
