package com.tianyigps.xiepeng.activity;

import android.os.Bundle;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.base.BaseActivity;

public class EditRemarksActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remarks);

        init();

        setEventListener();
    }

    private void init() {
        this.setTitleText("填写原因");
    }

    private void setEventListener() {
    }
}
