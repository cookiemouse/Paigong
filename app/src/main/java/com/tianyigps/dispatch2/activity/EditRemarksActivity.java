package com.tianyigps.dispatch2.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.data.Data;

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
                String reason = mEditTextRemarks.getText().toString();
                if ("".equals(reason)) {
                    showNullDialog();
                    return;
                }
                mIntent.putExtra(Data.DATA_INTENT_REASON, reason);
                setResult(Data.DATA_INTENT_REASON_RESULT, mIntent);
                finish();
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

    //  显示Dialog
    private void showNullDialog() {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(EditRemarksActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_message_editable, null);
        TextView textView = view.findViewById(R.id.tv_dialog_message_message);
        Button button = view.findViewById(R.id.btn_dialog_message_cancel);
        textView.setText("未填写任何信息，请填写内容再提交！");
        button.setText(getString(R.string.ensure));
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
