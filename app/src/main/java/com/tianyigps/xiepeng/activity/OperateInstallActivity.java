package com.tianyigps.xiepeng.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.adapter.OperateInstallAdapter;
import com.tianyigps.xiepeng.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class OperateInstallActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_install);

        init();

        setEventListener();
    }

    private void init() {
        mRecyclerView = findViewById(R.id.rv_layout_activity_operate_install);
        mListView = findViewById(R.id.lv_activity_operate_install);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("");
        }
        mRecyclerView.setAdapter(new OperateInstallAdapter(this, list));
    }

    private void setEventListener() {
    }

}
