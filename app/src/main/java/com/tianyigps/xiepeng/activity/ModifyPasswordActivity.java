package com.tianyigps.xiepeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.base.BaseActivity;

public class ModifyPasswordActivity extends BaseActivity {

    private EditText mEditTextOld, mEditTextNew, mEditTextEnsure;
    private Button mButtonEnsure;

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
    }

    private void setEventListener() {

        mButtonEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModifyPasswordActivity.this, WorkerFragmentContentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
