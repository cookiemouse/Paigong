package com.tianyigps.dispatch2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.base.BaseActivity;

public class EditRemarksActivity extends BaseActivity {

    private static final String TAG = "EditRemarksActivity";

    private EditText mEditTextRemarks;
    private TextView mTextViewCount;
    private Button mButtonSubmit;

    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remarks);

        init();

        setEventListener();
    }

    private void init() {
        this.setTitleText("填写原因");

        mIntent = getIntent();

        mEditTextRemarks = findViewById(R.id.et_activity_edit_remarks);
        mTextViewCount = findViewById(R.id.tv_activity_edit_remarks_count);
        mButtonSubmit = findViewById(R.id.btn_activity_edit_remarks_next);
    }

    private void setEventListener() {
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/20 下一步
//                String reason = mEditTextRemarks.getText().toString();
//                mIntent.putExtra(Data.DATA_INTENT_REASON, reason);
//                setResult(Data.DATA_INTENT_REASON_RESULT, mIntent);
//                finish();

                String test = "工程师迟到,TY20170719102653670,天易根客户1";
                String splits[] = test.split(",");
                Log.i(TAG, "onClick: split.size-->" + test.split(",").length);
                Log.i(TAG, "onClick: split.size-->" + splits.length);
                Log.i(TAG, "----");
                Log.i(TAG, "onClick: split.size-->" + test.split(",")[0]);
                Log.i(TAG, "onClick: split.size-->" + splits[0]);
                Log.i(TAG, "----");
            }
        });

        mEditTextRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String count = editable.length() + "/200";
                mTextViewCount.setText(count);
            }
        });
    }

    //  显示信息Dialog
    private void showMessageDialog(String msg) {
        if (isFinishing()){
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(EditRemarksActivity.this);
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
}
