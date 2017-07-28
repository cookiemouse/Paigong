package com.tianyigps.xiepeng.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import com.tianyigps.xiepeng.manager.DatabaseManager;
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

    private String mStringRemarks;
    private String mStringMsg = "数据请求失败，请检查网络";

    private MyHandler myHandler;

    //  AdapterType
    private int mAdapterType;

    //  数据库
    private DatabaseManager mDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installing);

        init();

        setEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //将背景色复原
        resetRepairList();
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

        mDatabaseManager = new DatabaseManager(this);

        myHandler = new MyHandler();

        mInstallingAdapter = new InstallingAdapter(InstallingActivity.this, mAdapterInstallingDataList);
        mRemoveAdapter = new RemoveAdapter(InstallingActivity.this, mAdapterRemoveDataList);
        mRepairAdapter = new RepairAdapter(InstallingActivity.this, mAdapterRepairDataList);

        mAdapterType = TYPE_REPAIR;

        switch (mAdapterType) {
            case TYPE_INSTALL: {
                mListView.setAdapter(mInstallingAdapter);
                break;
            }
            case TYPE_REMOVE: {
                mListView.setAdapter(mRemoveAdapter);
                break;
            }
            case TYPE_REPAIR: {
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
//                toActivity(OperateInstallActivity.class);
//                toRemoveActivity();
                toRepairActivity(mAdapterRepairDataList.get(i).getId());
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkRepairList()) {
                    toActivity(CustomSignActivity.class);
                    return;
                }
//                toActivity(EditRemarksActivity.class);
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
                mAdapterInstallingDataList.clear();
                mAdapterRepairDataList.clear();
                mAdapterRemoveDataList.clear();

                Gson gson = new Gson();
                StartOrderInfoBean startOrderInfoBean = gson.fromJson(result, StartOrderInfoBean.class);

                if (!startOrderInfoBean.isSuccess()) {
                    mStringMsg = startOrderInfoBean.getMsg();
                    onFailure();
                    return;
                }
                StartOrderInfoBean.ObjBean objBean = startOrderInfoBean.getObj();
                mStringRemarks = objBean.getRemark();

                switch (mAdapterType) {
                    case TYPE_INSTALL: {
                        for (StartOrderInfoBean.ObjBean.CarListBean carListBean : objBean.getCarList()) {
                            carListBean.getCarVin();
                            mAdapterInstallingDataList.add(
                                    new AdapterInstallingData(carListBean.getNewCarVin()
                                            , carListBean.getWiredNum()
                                            , carListBean.getWirelessNum()));
                        }

                        myHandler.sendEmptyMessage(Data.MSG_1);
                        break;
                    }
                    case TYPE_REMOVE: {
                        mAdapterRemoveDataList.add(new AdapterRemoveData("拆除"));

                        for (StartOrderInfoBean.ObjBean.CarListBean carListBean : objBean.getCarList()) {
                            mAdapterRemoveDataList.add(new AdapterRemoveData(carListBean.getWiredNum(), carListBean.getWirelessNum()));
                        }

                        mAdapterRemoveDataList.add(new AdapterRemoveData("安装"));

                        for (StartOrderInfoBean.ObjBean.CarListBean carListBean : objBean.getCarList()) {
                            String frameNo = carListBean.getCarVin();
                            mAdapterRemoveDataList.add(new AdapterRemoveData(frameNo
                                    , carListBean.getWiredNum()
                                    , carListBean.getWirelessNum()));
                        }

                        myHandler.sendEmptyMessage(Data.MSG_2);
                        break;
                    }
                    case TYPE_REPAIR: {

                        for (StartOrderInfoBean.ObjBean.CarListBean carListBean : objBean.getCarList()) {
                            String frameNo = carListBean.getCarVin();
                            String carNo = carListBean.getCarNo();
                            for (StartOrderInfoBean.ObjBean.CarListBean.CarTerminalListBean carTerminalListBean
                                    : carListBean.getCarTerminalList()) {

                                int type = carTerminalListBean.getTerminalType();

                                String terminalName = carTerminalListBean.getTerminalName();
                                if (null == terminalName) {
                                    terminalName = "";
                                }

                                mAdapterRepairDataList.add(new AdapterRepairData(type
                                        , carTerminalListBean.getTNo()
                                        , terminalName
                                        , carNo
                                        , frameNo));
                            }
                        }

                        myHandler.sendEmptyMessage(Data.MSG_3);
                        break;
                    }
                    default: {
                        Log.i(TAG, "onSuccess: default");
                    }
                }
            }
        });
    }

    private void toActivity(Class cls) {
        Intent intent = new Intent(InstallingActivity.this, cls);
        startActivity(intent);
    }

    private void toRemoveActivity() {
        Intent intent = new Intent(InstallingActivity.this, OperateRemoveActivity.class);
        intent.putExtra(Data.DATA_INTENT_EID, eid);
        intent.putExtra(Data.DATA_INTENT_TOKEN, token);
        intent.putExtra(Data.DATA_INTENT_ORDER_NO, orderNo);
        startActivity(intent);
    }

    private void toRepairActivity(String tNo) {
        Intent intent = new Intent(InstallingActivity.this, OperateRepairActivity.class);
        intent.putExtra(Data.DATA_INTENT_EID, eid);
        intent.putExtra(Data.DATA_INTENT_TOKEN, token);
        intent.putExtra(Data.DATA_INTENT_ORDER_NO, orderNo);
        intent.putExtra(Data.DATA_INTENT_T_NO, tNo);
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

    //  将维修背景色初始化
    private void resetRepairList() {
        if (mAdapterRepairDataList.size() > 0) {
            for (AdapterRepairData data : mAdapterRepairDataList) {
                data.setComplete(true);
            }
        }
        myHandler.sendEmptyMessage(Data.MSG_3);
    }

    //  检验维修列表是否已完成
    private boolean checkRepairList() {
        boolean nextAble = true;
        if (mAdapterRepairDataList.size() > 0) {
            for (AdapterRepairData data : mAdapterRepairDataList) {
                boolean isComplete = false;
                Cursor cursor = mDatabaseManager.getRepair(data.getId());
                if (cursor.moveToFirst()){
                    String position = cursor.getString(1);
                    String explain = cursor.getString(4);
                    String positionUrl = cursor.getString(6);
                    String installUrl = cursor.getString(7);

                    Log.i(TAG, "onResume: 1-->" + position);
                    Log.i(TAG, "onResume: 2-->" + explain);
                    Log.i(TAG, "onResume: 3-->" + positionUrl);
                    Log.i(TAG, "onResume: 4-->" + installUrl);

                    isComplete = ((null != position)
                            && (null != explain)
                            && (null != positionUrl)
                            && (null != installUrl));
                }

                data.setComplete(isComplete);

                if (nextAble){
                    nextAble = isComplete;
                }
            }
        }
        myHandler.sendEmptyMessage(Data.MSG_3);

        return nextAble;
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTextViewRemarks.setText(mStringRemarks);
            switch (msg.what) {
                case Data.MSG_ERO: {
                    showFailureDialog(mStringMsg);
                    break;
                }
                case Data.MSG_1: {
                    //  install
                    mInstallingAdapter.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_2: {
                    //  remove
                    mRemoveAdapter.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_3: {
                    mRepairAdapter.notifyDataSetChanged();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
