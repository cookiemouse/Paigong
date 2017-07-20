package com.tianyigps.xiepeng.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianyigps.signviewlibrary.SignView;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.base.BaseActivity;

public class CustomSignActivity extends BaseActivity {

    private SignView mSignView;
    private LinearLayout mLinearLayoutClear;
    private TextView mTextViewSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_sign);

        init();

        setEventListener();
    }

    private void init() {
        this.setTitleText("客户签字");

        mSignView = findViewById(R.id.sv_activity_custom_sign);
        mLinearLayoutClear = findViewById(R.id.ll_activity_custom_sign);
        mTextViewSubmit = findViewById(R.id.tv_activity_custom_sign);
    }

    private void setEventListener() {
        mLinearLayoutClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignView.clearPath();
            }
        });

        mTextViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/20 提交
            }
        });
    }
}
