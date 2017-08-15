package com.tianyigps.dispatch2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.adapter.OperateRemoveAdapter;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.bean.RemoveTerminalBean;
import com.tianyigps.dispatch2.bean.StartOrderInfoBean;
import com.tianyigps.dispatch2.data.AdapterOperateRemoveData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderInfoStartListener;
import com.tianyigps.dispatch2.interfaces.OnRemoveTerminalListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.TimeFormatU;

import java.util.ArrayList;
import java.util.List;

public class OperateRemoveActivity extends BaseActivity {

    private static final String TAG = "OperateRemoveActivity";

    private static final int STATE_NO_REMOVE = 0;
    private static final int STATE_REMOVED = 1;
    private static final int STATE_REMOVED_INSTALL = 2;

    private ListView mListViewRemove;
    private List<AdapterOperateRemoveData> mAdapterOperateRemoveDataList = new ArrayList<>();
    private OperateRemoveAdapter mOperateRemoveAdapter;

    private NetworkManager mNetworkManager;
    private SharedpreferenceManager mSharedpreferenceManager;
    private int eid;
    private String token;
    private String userName;
    private String orderNo;

    private int mCarId;

    private String mStringMsg = "数据请求失败，请检查网络！";
    private MyHandler myHandler;
    private int positionNow;
    private AlertDialog dialogRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_remove);

        init();

        setEventListener();
    }

    private void init() {
        mListViewRemove = findViewById(R.id.lv_activity_operate_remove);

        mSharedpreferenceManager = new SharedpreferenceManager(this);

        Intent intent = getIntent();
        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();
        orderNo = intent.getStringExtra(Data.DATA_INTENT_ORDER_NO);
        mCarId = intent.getIntExtra(Data.DATA_INTENT_CAR_ID, 0);

        mNetworkManager = NetworkManager.getInstance();
        myHandler = new MyHandler();

        mNetworkManager.getWorkerOrderInfoStart(eid, token, orderNo, userName);

//        for (int i = 0; i < 10; i++) {
//            mAdapterOperateRemoveDataList.add(new AdapterOperateRemoveData("carNo"
//                    , "frameNo"
//                    , "有线"
//                    , "（微贷网2165467987）"
//                    , "tNo"
//                    , "驾驶舱仪表内"
//                    , "TY20170717143432075/2de3a21e0bda4f06acf8bffe62065a48.png"
//                    , "TY20170717143432075/dd5a838856314802be6a43d9139ecc51.png"
//                    , "2018-08-08"
//                    , "杨某某"
//                    , "17900000001"
//                    , 0));
//        }

        mOperateRemoveAdapter = new OperateRemoveAdapter(OperateRemoveActivity.this, mAdapterOperateRemoveDataList);

        mListViewRemove.setAdapter(mOperateRemoveAdapter);
    }

    private void setEventListener() {
        mNetworkManager.setGetWorkerOrderInfoStartListener(new OnGetWorkerOrderInfoStartListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
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

                StartOrderInfoBean.ObjBean objBean = startOrderInfoBean.getObj();

                for (StartOrderInfoBean.ObjBean.CarListBean carListBean : objBean.getCarList()) {
                    if (carListBean.getId() != mCarId) {
                        continue;
                    }
                    String frameNo = carListBean.getCarVin();
                    String carNo = carListBean.getCarNo();
                    for (StartOrderInfoBean.ObjBean.CarListBean.CarTerminalListBean carTerminalListBean
                            : carListBean.getCarTerminalList()) {

                        int type = carTerminalListBean.getTerminalType();
                        String terminalType;
                        String terminalName = carTerminalListBean.getTerminalName();
                        if (null == terminalName) {
                            terminalName = "";
                        } else {
                            terminalName = "（" + terminalName + "）";
                        }
                        switch (type) {
                            case 1: {
                                terminalType = "有线";
                                break;
                            }
                            case 2: {
                                terminalType = "无线";
                                break;
                            }
                            default: {
                                terminalType = "";
                                Log.i(TAG, "onSuccess: default");
                            }
                        }

                        mAdapterOperateRemoveDataList.add(new AdapterOperateRemoveData(carNo
                                , frameNo
                                , terminalType
                                , terminalName
                                , carTerminalListBean.getTNo()
                                , carTerminalListBean.getNewInstallLocation()
                                , carTerminalListBean.getNewInstallLocationPic()
                                , carTerminalListBean.getNewWiringDiagramPic()
                                , new TimeFormatU().millisToDate(carListBean.getWiredAnnual())
                                , objBean.getDispatchContactName()
                                , objBean.getDispatchContactPhone()
                                , carTerminalListBean.getRemoveStatus()));
                    }
                }

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });

        mNetworkManager.setRemoveTerminalListener(new OnRemoveTerminalListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                RemoveTerminalBean removeTerminalBean = gson.fromJson(result, RemoveTerminalBean.class);
                if (!removeTerminalBean.isSuccess()) {
                    onFailure();
                    return;
                }

                myHandler.sendEmptyMessage(Data.MSG_2);
            }
        });

        mOperateRemoveAdapter.setRemoveListener(new OperateRemoveAdapter.OnRemoveListener() {
            @Override
            public void onRemove(int position) {
                positionNow = position;
                showRemoveDialog();
            }
        });
    }

    private void showRemoveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OperateRemoveActivity.this);
        View viewDialog = LayoutInflater.from(OperateRemoveActivity.this).inflate(R.layout.dialog_remove, null);
        builder.setView(viewDialog);
        dialogRemove = builder.create();
        TextView ensure = viewDialog.findViewById(R.id.btn_dialog_remove_ensure);
        TextView cancel = viewDialog.findViewById(R.id.btn_dialog_remove_cancel);
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imei = mAdapterOperateRemoveDataList.get(positionNow).gettNo();
                mNetworkManager.removeTerminal(eid, token, orderNo, imei, userName);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRemove.dismiss();
            }
        });
        dialogRemove.show();
    }

    private void showMessageDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OperateRemoveActivity.this);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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
                    showMessageDialog(mStringMsg);
                    Log.i(TAG, "handleMessage: msg_ero");
                }
                case Data.MSG_1: {
                    mOperateRemoveAdapter.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_2: {
                    if (null != dialogRemove) {
                        dialogRemove.dismiss();
                    }
                    mAdapterOperateRemoveDataList.get(positionNow).setRemoveState(STATE_REMOVED);
                    mOperateRemoveAdapter.notifyDataSetChanged();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
