package com.tianyigps.dispatch2.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.adapter.LocateWarnAdapter;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.bean.LocateWarnBean;
import com.tianyigps.dispatch2.data.AdapterLocateWarnData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnLocateWarnListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.TimeFormatU;
import com.tianyigps.dispatch2.utils.ToastU;

import java.util.ArrayList;
import java.util.List;

public class WarnInfoActivity extends BaseActivity {

    private static final String TAG = "WarnInfoActivity";

    private SharedpreferenceManager mSharedpreferenceManager;
    private NetworkManager mNetworkManager;
    private MyHandler myHandler;

    private ListView mListViewWarn;

    private int eid;
    private String token;
    private String userName;
    private String mImei;
    private String mMessage;

    private List<AdapterLocateWarnData> mAdapterLocateWarnDataList;
    private LocateWarnAdapter mLocateWarnAdapter;

    private ToastU mToastU;

    private LoadingDialogFragment mLoadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_info);

        init();

        setEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImei = getIntent().getStringExtra(Data.DATA_INTENT_LOCATE_IMEI);
        getWarnInfo(mImei);
    }

    private void init() {
        this.setTitleText("查看设备信息");

        mListViewWarn = findViewById(R.id.lv_activity_warn_info);

        mLoadingDialogFragment = new LoadingDialogFragment();

        mSharedpreferenceManager = new SharedpreferenceManager(this);
        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();

        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();

        mToastU = new ToastU(WarnInfoActivity.this);

        mAdapterLocateWarnDataList = new ArrayList<>();
        mLocateWarnAdapter = new LocateWarnAdapter(this, mAdapterLocateWarnDataList);
        mListViewWarn.setAdapter(mLocateWarnAdapter);
    }

    private void setEventListener() {
        mNetworkManager.setOnLocateWarnListener(new OnLocateWarnListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                final LocateWarnBean locateWarnBean = gson.fromJson(result, LocateWarnBean.class);
                if (null == locateWarnBean) {
                    mMessage = Data.DEFAULT_MESSAGE;
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }
                if (!locateWarnBean.isSuccess()) {
                    mMessage = locateWarnBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapterLocateWarnDataList.clear();
                        mAdapterLocateWarnDataList.add(new AdapterLocateWarnData("报警类型", "报警时间"));
                        for (LocateWarnBean.ObjBean objBean : locateWarnBean.getObj()) {
                            mAdapterLocateWarnDataList.add(new AdapterLocateWarnData(objBean.getName(), new TimeFormatU().millisToDate(objBean.getCreate_time())));
                        }
                        mLocateWarnAdapter.notifyDataSetChanged();
                    }
                });
                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });
    }

    private void showLoading() {
        mLoadingDialogFragment.show(getFragmentManager(), "LoadingFragment");
    }

    //  获取完整imei
    private void getWarnInfo(String imei) {
        showLoading();
        mNetworkManager.getLocateWary(userName, token, imei);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mLoadingDialogFragment.isAdded()) {
                mLoadingDialogFragment.dismissAllowingStateLoss();
            }
            switch (msg.what) {
                case Data.MSG_ERO: {
                    mToastU.showToast(mMessage);
                    break;
                }
                case Data.MSG_1: {
//                    mLocateWarnAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }
}
