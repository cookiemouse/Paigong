package com.tianyigps.xiepeng.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.adapter.StatisticsManagerAdapter;
import com.tianyigps.xiepeng.adapter.StatisticsWorkerAdapter;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.data.AdapterStatisticsManagerData;
import com.tianyigps.xiepeng.data.AdapterStatisticsWorkderData;
import com.tianyigps.xiepeng.dialog.DatePickerDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends BaseActivity {

    private TextView mTextViewDate;
    private ImageView mImageViewDate;
    private ListView mListViewStatistics;
    private FrameLayout mFrameLayoutTitle;

    private List<AdapterStatisticsWorkderData> mStatisticsWorkderDataList;
    private StatisticsWorkerAdapter mStatisticsWorkerAdapter;

    private List<AdapterStatisticsManagerData> mStatisticsManagerDataList;
    private StatisticsManagerAdapter mStatisticsManagerAdapter;

    private DatePickerDialogFragment mDatePickerDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        init();

        setEventListener();

    }

    private void init() {
        this.setTitleText(R.string.statistics);

        mTextViewDate = findViewById(R.id.tv_activity_statistics_date);
        mImageViewDate = findViewById(R.id.iv_activity_statistics_date);
        mListViewStatistics = findViewById(R.id.lv_activity_statistics);
        mFrameLayoutTitle = findViewById(R.id.fl_activity_statistics_list_title);

        mDatePickerDialogFragment = new DatePickerDialogFragment();
        mDatePickerDialogFragment.setCancelable(false);

        // TODO: 2017/7/14 由帐户类型决定添加什么Title，以及加载什么Adapter
        showManagerList();
    }

    private void setEventListener() {
        mTextViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceDate();
            }
        });

        mImageViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceDate();
            }
        });

        mDatePickerDialogFragment.setOnEnsureListener(new DatePickerDialogFragment.OnEnsureListener() {
            @Override
            public void onEnsure(int year, int month) {
                mTextViewDate.setText(year + "年" + month + "月");
            }
        });
    }

    //  显示Worker列表
    private void showWorkerList() {
        View viewTitleWorker = LayoutInflater.from(StatisticsActivity.this)
                .inflate(R.layout.layout_statistics_list_worker_title, null);

        mFrameLayoutTitle.addView(viewTitleWorker);

        mStatisticsWorkderDataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mStatisticsWorkderDataList.add(new AdapterStatisticsWorkderData("离线设备-新装&改装（有线）", 7));
        }

        mStatisticsWorkerAdapter = new StatisticsWorkerAdapter(StatisticsActivity.this, mStatisticsWorkderDataList);

        mListViewStatistics.setAdapter(mStatisticsWorkerAdapter);
    }

    //  显示Manager列表
    private void showManagerList() {
        View viewTitleManager = LayoutInflater.from(StatisticsActivity.this)
                .inflate(R.layout.layout_statistics_list_manager_title, null);

        mFrameLayoutTitle.addView(viewTitleManager);

        mStatisticsManagerDataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mStatisticsManagerDataList.add(new AdapterStatisticsManagerData("1001南柱赫", 21, 25, 21, 25));
        }

        mStatisticsManagerAdapter = new StatisticsManagerAdapter(StatisticsActivity.this, mStatisticsManagerDataList);
        mListViewStatistics.setAdapter(mStatisticsManagerAdapter);
    }

    //  选择日期
    private void choiceDate() {
        mDatePickerDialogFragment.show(getFragmentManager(), "");
    }
}
