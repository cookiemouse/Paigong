package com.tianyigps.xiepeng.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.adapter.OperateInstallAdapter;
import com.tianyigps.xiepeng.adapter.OperateInstallListAdapter;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.customview.MyListView;
import com.tianyigps.xiepeng.customview.MyRecyclerView;
import com.tianyigps.xiepeng.data.AdapterOperateInstallListData;

import java.util.ArrayList;
import java.util.List;

public class OperateInstallActivity extends BaseActivity {

    private MyRecyclerView mRecyclerView;
    private MyListView mListView;

    private OperateInstallListAdapter mOperateInstallListAdapter;

    private List<AdapterOperateInstallListData> mAdapterOperateInstallListDataList;

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

        mAdapterOperateInstallListDataList = new ArrayList<>();

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("");
            mAdapterOperateInstallListDataList.add(new AdapterOperateInstallListData());

        }
        mRecyclerView.setAdapter(new OperateInstallAdapter(this, list));

        mOperateInstallListAdapter = new OperateInstallListAdapter(OperateInstallActivity.this,
                mAdapterOperateInstallListDataList);
        mListView.setAdapter(mOperateInstallListAdapter);
    }

    private void setEventListener() {
    }

}
