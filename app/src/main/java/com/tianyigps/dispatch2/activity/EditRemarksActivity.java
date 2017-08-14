package com.tianyigps.dispatch2.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.base.BaseActivity;

public class EditRemarksActivity extends BaseActivity {

    private EditText mEditTextRemarks;
    private TextView mTextViewCount, mTextViewSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remarks);

        init();

        setEventListener();
    }

    private void init() {
        this.setTitleText("填写原因");

        mEditTextRemarks = findViewById(R.id.et_activity_edit_remarks);
        mTextViewCount = findViewById(R.id.tv_activity_edit_remarks_count);
        mTextViewSubmit = findViewById(R.id.tv_activity_edit_remarks_next);
    }

    private void setEventListener() {
        mTextViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/20 下一步
            }
        });
    }
}
