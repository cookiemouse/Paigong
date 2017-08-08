package com.tianyigps.xiepeng.activity;

import android.os.Bundle;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.base.BaseActivity;

public class PendingDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_details);

        init();

        setEventListener();
    }

    private void init() {
        this.setTitleText("");
        this.setTitleRightButtonVisibilite(false);
    }

    private void setEventListener() {
    }
}
