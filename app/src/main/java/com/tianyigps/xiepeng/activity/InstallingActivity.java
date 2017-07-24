package com.tianyigps.xiepeng.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.adapter.InstallingAdapter;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.data.AdapterInstallingData;

import java.util.ArrayList;
import java.util.List;

public class InstallingActivity extends BaseActivity {

    private TextView mTextViewRemarks;
    private ListView mListView;

    private List<AdapterInstallingData> mAdapterInstallingDataList;
    private InstallingAdapter mInstallingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installing);

        init();

        setEventListener();
    }

    private void init() {
        this.setTitleText("");

        mTextViewRemarks = findViewById(R.id.tv_layout_activity_installing_remarks);
        mListView = findViewById(R.id.lv_activity_installing);

        mAdapterInstallingDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mAdapterInstallingDataList.add(new AdapterInstallingData("32132131", 1, 2, 0, 0));
        }

        mInstallingAdapter = new InstallingAdapter(InstallingActivity.this, mAdapterInstallingDataList);

        mListView.setAdapter(mInstallingAdapter);
    }

    private void setEventListener() {
    }
}
