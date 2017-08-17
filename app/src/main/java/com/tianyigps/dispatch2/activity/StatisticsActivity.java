package com.tianyigps.dispatch2.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.adapter.StatisticsManagerAdapter;
import com.tianyigps.dispatch2.adapter.StatisticsWorkerAdapter;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.bean.InstallCountBean;
import com.tianyigps.dispatch2.bean.WorkerQualityBean;
import com.tianyigps.dispatch2.data.AdapterStatisticsManagerData;
import com.tianyigps.dispatch2.data.AdapterStatisticsWorkderData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.DatePickerDialogFragment;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnGetQualityCountListener;
import com.tianyigps.dispatch2.interfaces.OnInstallCountListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.tianyigps.dispatch2.data.Data.DATA_LAUNCH_MODE_WORKER;
import static com.tianyigps.dispatch2.data.Data.MSG_1;

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
    private int eid;
    private String jobNo;
    private String token;
    private String userName;
    private int uiMode;
    private String monthP;

    private String mStringMessage;

    //  LoadingFragment
    LoadingDialogFragment mLoadingDialogFragment;

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
        eid = mSharedpreferenceManager.getEid();
        jobNo = mSharedpreferenceManager.getJobNo();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();
        uiMode = mSharedpreferenceManager.getUiMode();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MONTH, -1);
        Long mills = calendar.getTimeInMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM", Locale.CHINA);
        Date date = new Date(mills);
        monthP = simpleDateFormat.format(date);


        mTextViewDate = findViewById(R.id.tv_activity_statistics_date);
        mImageViewDate = findViewById(R.id.iv_activity_statistics_date);
        mListViewStatistics = findViewById(R.id.lv_activity_statistics);
        mFrameLayoutTitle = findViewById(R.id.fl_activity_statistics_list_title);

        SimpleDateFormat simpleDateFormatShow = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
        Date dateShow = new Date(mills);
        mTextViewDate.setText(simpleDateFormatShow.format(dateShow));

        mDatePickerDialogFragment = new DatePickerDialogFragment();
        mDatePickerDialogFragment.setCancelable(false);

        mLoadingDialogFragment = new LoadingDialogFragment();

        mNetworkManager = new NetworkManager();

        myHandler = new MyHandler();

        showLoading();
        if (Data.DATA_LAUNCH_MODE_WORKER == uiMode) {
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
                if (month < 10) {
                    monthP = "0" + month;
                } else {
                    monthP = "" + month;
                }
                mTextViewDate.setText(year + "年" + monthP + "月");

                monthP = "" + year + monthP;

                showLoading();
                if (DATA_LAUNCH_MODE_WORKER == uiMode) {
                    mNetworkManager.getQualityCount(eid, token, monthP, userName);
                } else {
                    mNetworkManager.getInstallCount(jobNo, token, monthP, userName);
                }
            }
        });

        mNetworkManager.setOnGetQualityCountListener(new OnGetQualityCountListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                WorkerQualityBean workerQualityBean = gson.fromJson(result, WorkerQualityBean.class);
                if (!workerQualityBean.isSuccess()) {
                    mStringMessage = workerQualityBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                WorkerQualityBean.ObjBean objBean = workerQualityBean.getObj();
                if (null == objBean) {
                    myHandler.sendEmptyMessage(MSG_1);
                    return;
                }

                mStatisticsWorkderDataList.clear();

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

        mNetworkManager.setOnInstallCountListener(new OnInstallCountListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                InstallCountBean installCountBean = gson.fromJson(result, InstallCountBean.class);
                if (!installCountBean.isSuccess()) {
                    mStringMessage = installCountBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                mStatisticsManagerDataList.clear();

                for (InstallCountBean.ObjBean objBean : installCountBean.getObj()) {
                    String temp = objBean.getEngineerJobNo() + objBean.getEngineerName();
                    mStatisticsManagerDataList.add(new AdapterStatisticsManagerData(temp
                            , objBean.getDoorNum()
                            , objBean.getFinishCarNum()
                            , objBean.getFinishWiredNum()
                            , objBean.getFinishWirelessNum()));
                }
                myHandler.sendEmptyMessage(Data.MSG_2);
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

        mNetworkManager.getQualityCount(eid, token, monthP, userName);
    }

    //  显示Manager列表
    private void showManagerList() {
        View viewTitleManager = LayoutInflater.from(StatisticsActivity.this)
                .inflate(R.layout.layout_statistics_list_manager_title, null);

        mFrameLayoutTitle.addView(viewTitleManager);

        mStatisticsManagerDataList = new ArrayList<>();

        mStatisticsManagerAdapter = new StatisticsManagerAdapter(StatisticsActivity.this, mStatisticsManagerDataList);

        mListViewStatistics.setAdapter(mStatisticsManagerAdapter);

        mNetworkManager.getInstallCount(jobNo, token, monthP, userName);
    }

    //  选择日期
    private void choiceDate() {
        mDatePickerDialogFragment.show(getFragmentManager(), "");
    }

    //  信息对话框
    private void showMessageDialog(String message) {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_message_editable, null);
        TextView textViewMessage = viewDialog.findViewById(R.id.tv_dialog_message_message);
        Button buttonKnow = viewDialog.findViewById(R.id.btn_dialog_message_cancel);
        textViewMessage.setText(message);
        builder.setView(viewDialog);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();

        buttonKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //  显示LoadingFragment
    private void showLoading() {
        mLoadingDialogFragment.show(getFragmentManager(), "LoadingFragment");
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mLoadingDialogFragment.isAdded()) {
                mLoadingDialogFragment.dismiss();
            }

            switch (msg.what) {
                case Data.MSG_ERO: {
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    //  安装工程师
                    mStatisticsWorkerAdapter.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_2: {
                    //  服务主任
                    mStatisticsManagerAdapter.notifyDataSetChanged();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
