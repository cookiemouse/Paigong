package com.tianyigps.xiepeng.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.adapter.StatisticsManagerAdapter;
import com.tianyigps.xiepeng.adapter.StatisticsWorkerAdapter;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.bean.WorkerQualityBean;
import com.tianyigps.xiepeng.data.AdapterStatisticsManagerData;
import com.tianyigps.xiepeng.data.AdapterStatisticsWorkderData;
import com.tianyigps.xiepeng.dialog.DatePickerDialogFragment;
import com.tianyigps.xiepeng.interfaces.OnGetQualityCountListener;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.tianyigps.xiepeng.data.Data.DATA_LAUNCH_MODE_WORKER;
import static com.tianyigps.xiepeng.data.Data.EID;
import static com.tianyigps.xiepeng.data.Data.MSG_1;
import static com.tianyigps.xiepeng.data.Data.MSG_ERO;
import static com.tianyigps.xiepeng.data.Data.TOKEN;

public class StatisticsActivity extends BaseActivity {

    private static final String TAG = "StatisticsActivity";

    private TextView mTextViewDate;
    private ImageView mImageViewDate;
    private ListView mListViewStatistics;
    private FrameLayout mFrameLayoutTitle;

    private List<AdapterStatisticsWorkderData> mStatisticsWorkderDataList;
    private StatisticsWorkerAdapter mStatisticsWorkerAdapter;

    private List<AdapterStatisticsManagerData> mStatisticsManagerDataList;
    private StatisticsManagerAdapter mStatisticsManagerAdapter;

    private DatePickerDialogFragment mDatePickerDialogFragment;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private SharedpreferenceManager mSharedpreferenceManager;
    private int launchMode;
    private String monthP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        init();

        setEventListener();

    }

    private void init() {
        this.setTitleText(R.string.statistics);

        mSharedpreferenceManager = new SharedpreferenceManager(StatisticsActivity.this);
        launchMode = mSharedpreferenceManager.getLaunchMode();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        monthP = simpleDateFormat.format(date);

        mTextViewDate = findViewById(R.id.tv_activity_statistics_date);
        mImageViewDate = findViewById(R.id.iv_activity_statistics_date);
        mListViewStatistics = findViewById(R.id.lv_activity_statistics);
        mFrameLayoutTitle = findViewById(R.id.fl_activity_statistics_list_title);

        SimpleDateFormat simpleDateFormatShow = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
        Date dateShow = new Date(System.currentTimeMillis());
        mTextViewDate.setText(simpleDateFormatShow.format(dateShow));

        mDatePickerDialogFragment = new DatePickerDialogFragment();
        mDatePickerDialogFragment.setCancelable(false);

        mNetworkManager = NetworkManager.getInstance();

        myHandler = new MyHandler();

        // TODO: 2017/7/14 由帐户类型决定添加什么Title，以及加载什么Adapter
        if (DATA_LAUNCH_MODE_WORKER == launchMode) {
            showWorkerList();
        } else {
            showManagerList();
        }
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
                if (month < 10) {
                    monthP = year + "0" + month;
                } else {
                    monthP = "" + year + month;
                }

                if (DATA_LAUNCH_MODE_WORKER == launchMode) {
                    mNetworkManager.getQualityCount(EID, TOKEN, monthP);
                } else {
                    // TODO: 2017/7/18 Manager质量统计
                }
            }
        });

        mNetworkManager.setOnGetQualityCountListener(new OnGetQualityCountListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                mStatisticsWorkderDataList.clear();
                Gson gson = new Gson();
                WorkerQualityBean workerQualityBean = gson.fromJson(result, WorkerQualityBean.class);
                if (!workerQualityBean.isSuccess()) {
                    return;
                }

                WorkerQualityBean.ObjBean objBean = workerQualityBean.getObj();
                if (null == objBean) {
                    myHandler.sendEmptyMessage(MSG_1);
                    return;
                }

                mStatisticsWorkderDataList.add(new AdapterStatisticsWorkderData("离线设备-新装&改装（有线）"
                        , objBean.getInstallWiredOfflineNum()));
                mStatisticsWorkderDataList.add(new AdapterStatisticsWorkderData("离线设备-新装&改装（无线）"
                        , objBean.getInstallWirelessOfflineNum()));
                mStatisticsWorkderDataList.add(new AdapterStatisticsWorkderData("离线设备-维修（有线）"
                        , objBean.getRepairWiredOfflineNum()));
                mStatisticsWorkderDataList.add(new AdapterStatisticsWorkderData("离线设备-维修（无线）"
                        , objBean.getRepairWirelessOfflineNum()));
                mStatisticsWorkderDataList.add(new AdapterStatisticsWorkderData("断电设备-新装&改装（有线）"
                        , objBean.getInstallWiredOutageNum()));
                mStatisticsWorkderDataList.add(new AdapterStatisticsWorkderData("断电设备-新装&改装（无线）"
                        , objBean.getInstallWirelessOutageNum()));
                mStatisticsWorkderDataList.add(new AdapterStatisticsWorkderData("断电设备-维修（有线）"
                        , objBean.getRepairWiredOutageNum()));
                mStatisticsWorkderDataList.add(new AdapterStatisticsWorkderData("断电设备-维修（无线）"
                        , objBean.getRepairWirelessOutageNum()));
                mStatisticsWorkderDataList.add(new AdapterStatisticsWorkderData("未定位提交-新装&改装"
                        , objBean.getInstallNotPositionedNum()));
                mStatisticsWorkderDataList.add(new AdapterStatisticsWorkderData("未定位提交-维修"
                        , objBean.getRepairNotPositionedNum()));
                mStatisticsWorkderDataList.add(new AdapterStatisticsWorkderData("提交超时"
                        , objBean.getCommitTimeoutNum()));
                mStatisticsWorkderDataList.add(new AdapterStatisticsWorkderData("签到异常"
                        , objBean.getSignExceptionNum()));
                mStatisticsWorkderDataList.add(new AdapterStatisticsWorkderData("照片异常"
                        , objBean.getPictureExceptionNum()));

                myHandler.sendEmptyMessage(MSG_1);
            }
        });
    }

    //  显示Worker列表
    private void showWorkerList() {
        View viewTitleWorker = LayoutInflater.from(StatisticsActivity.this)
                .inflate(R.layout.layout_statistics_list_worker_title, null);

        mFrameLayoutTitle.addView(viewTitleWorker);

        mStatisticsWorkderDataList = new ArrayList<>();

        mStatisticsWorkerAdapter = new StatisticsWorkerAdapter(StatisticsActivity.this, mStatisticsWorkderDataList);

        mListViewStatistics.setAdapter(mStatisticsWorkerAdapter);

        mNetworkManager.getQualityCount(EID, TOKEN, monthP);
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

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ERO: {
                    break;
                }
                case MSG_1: {
                    if (DATA_LAUNCH_MODE_WORKER == launchMode) {
                        mStatisticsWorkerAdapter.notifyDataSetChanged();
                    } else {
                        mStatisticsManagerAdapter.notifyDataSetChanged();
                    }
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
