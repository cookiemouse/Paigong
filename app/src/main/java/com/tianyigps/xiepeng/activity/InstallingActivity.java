package com.tianyigps.xiepeng.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.adapter.InstallingAdapter;
import com.tianyigps.xiepeng.adapter.RemoveAdapter;
import com.tianyigps.xiepeng.adapter.RepairAdapter;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.bean.StartOrderInfoBean;
import com.tianyigps.xiepeng.data.AdapterInstallingData;
import com.tianyigps.xiepeng.data.AdapterRemoveData;
import com.tianyigps.xiepeng.data.AdapterRepairData;
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.interfaces.OnGetWorkerOrderInfoStartListener;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 这里需要三个Adapter，因为页面结构一样
 * 所以复用title和listview
 * 让listview加载三个不同的adapter来实现功能
 * 还可以复用数据请求
 * <p>
 * 其中RemoveAdapter中有三种不同的item
 * <p>
 * 这是一个有意思的页面
 **/

public class InstallingActivity extends BaseActivity {

    private static final String TAG = "InstallingActivity";

    private static final int TYPE_INSTALL = 0;
    private static final int TYPE_REPAIR = 1;
    private static final int TYPE_REMOVE = 2;

    private TextView mTextViewRemarks;
    private ListView mListView;
    private Button mButtonNext;

    private List<AdapterInstallingData> mAdapterInstallingDataList = new ArrayList<>();
    private InstallingAdapter mInstallingAdapter;

    private List<AdapterRemoveData> mAdapterRemoveDataList = new ArrayList<>();
    private RemoveAdapter mRemoveAdapter;

    private List<AdapterRepairData> mAdapterRepairDataList = new ArrayList<>();
    private RepairAdapter mRepairAdapter;

    //  网络请求
    private NetworkManager mNetworkManager;
    private int eid;
    private String token;
    private String orderNo;
    private SharedpreferenceManager mSharedpreferenceManager;

    private String mRemarks;
    private String mStringMsg = "数据请求失败，请检查网络";

    private MyHandler myHandler;

    //  AdapterType
    private int mAdapterType;

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
        mButtonNext = findViewById(R.id.btn_activity_installing_next);

        mSharedpreferenceManager = new SharedpreferenceManager(InstallingActivity.this);
        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();

        Intent intent = getIntent();
        orderNo = intent.getStringExtra(Data.DATA_INTENT_ORDER_NO);
        mAdapterType = intent.getIntExtra(Data.DATA_INTENT_INSTALL_TYPE, TYPE_INSTALL);

        mNetworkManager = NetworkManager.getInstance();

        myHandler = new MyHandler();

        mInstallingAdapter = new InstallingAdapter(InstallingActivity.this, mAdapterInstallingDataList);
        mRemoveAdapter = new RemoveAdapter(InstallingActivity.this, mAdapterRemoveDataList);
        mRepairAdapter = new RepairAdapter(InstallingActivity.this, mAdapterRepairDataList);

        switch (mAdapterType) {
            case TYPE_INSTALL: {
                for (int i = 0; i < 10; i++) {
                    mAdapterInstallingDataList.add(new AdapterInstallingData("32132131", 1, 2, 0, 0));
                }
                mListView.setAdapter(mInstallingAdapter);
                break;
            }
            case TYPE_REMOVE: {
                mAdapterRemoveDataList.add(new AdapterRemoveData("拆除"));
                mAdapterRemoveDataList.add(new AdapterRemoveData(2, 3, 1, 0));
                mAdapterRemoveDataList.add(new AdapterRemoveData("安装"));
                mAdapterRemoveDataList.add(new AdapterRemoveData("321313", 1, 3, 1, 0));
                mAdapterRemoveDataList.add(new AdapterRemoveData("56456", 2, 2, 1, 0));
                mAdapterRemoveDataList.add(new AdapterRemoveData("321798789321", 3, 1, 1, 0));
                mListView.setAdapter(mRemoveAdapter);
                break;
            }
            case TYPE_REPAIR: {
                for (int i = 0; i < 5; i++) {
                    mAdapterRepairDataList.add(new AdapterRepairData("有线设备"
                            , "654654698736546"
                            , "微贷网564564987"
                            , "沪A2345",
                            "LVC564HTE"));
                }
                mListView.setAdapter(mRepairAdapter);
                break;
            }
            default: {
                Log.i(TAG, "init: default");
            }
        }

        mNetworkManager.getWorkerOrderInfoStart(eid, token, orderNo);
    }

    private void setEventListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: 2017/7/24 测试RecyclerView
                toActivity(OperateInstallActivity.class);
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toActivity(CustomSignActivity.class);
            }
        });

        mNetworkManager.setGetWorkerOrderInfoStartListener(new OnGetWorkerOrderInfoStartListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                StartOrderInfoBean startOrderInfoBean = gson.fromJson(result, StartOrderInfoBean.class);

                if (!startOrderInfoBean.isSuccess()) {
                    mStringMsg = startOrderInfoBean.getMsg();
                    onFailure();
                    return;
                }
                mRemarks = startOrderInfoBean.getObj().getRemark();

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });
    }

    private void toActivity(Class cls) {
        Intent intent = new Intent(InstallingActivity.this, cls);
        startActivity(intent);
    }

    private void showFailureDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(InstallingActivity.this);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                InstallingActivity.this.finish();
                //  do nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_ERO: {
                    showFailureDialog(mStringMsg);
                    break;
                }
                case Data.MSG_1: {
                    mTextViewRemarks.setText(mRemarks);
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
