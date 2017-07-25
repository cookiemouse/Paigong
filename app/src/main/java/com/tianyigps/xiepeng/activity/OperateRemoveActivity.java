package com.tianyigps.xiepeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.adapter.OperateRemoveAdapter;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.bean.StartOrderInfoBean;
import com.tianyigps.xiepeng.data.AdapterOperateRemoveData;
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.interfaces.OnGetWorkerOrderInfoStartListener;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.utils.TimeFormatU;

import java.util.ArrayList;
import java.util.List;

public class OperateRemoveActivity extends BaseActivity {

    private static final String TAG = "OperateRemoveActivity";

    private ListView mListViewRemove;
    private List<AdapterOperateRemoveData> mAdapterOperateRemoveDataList = new ArrayList<>();
    private OperateRemoveAdapter mOperateRemoveAdapter;

    private NetworkManager mNetworkManager;
    private int eid;
    private String token;
    private String orderNo;

    private String mStringMsg = "数据请求失败，请检查网络！";
    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_remove);

        init();

        setEventListener();
    }

    private void init() {
        mListViewRemove = findViewById(R.id.lv_activity_operate_remove);

        Intent intent = getIntent();
        eid = intent.getIntExtra(Data.DATA_INTENT_EID, 0);
        token = intent.getStringExtra(Data.DATA_INTENT_TOKEN);
        orderNo = intent.getStringExtra(Data.DATA_INTENT_ORDER_NO);

        mNetworkManager = NetworkManager.getInstance();
        myHandler = new MyHandler();

        mNetworkManager.getWorkerOrderInfoStart(eid, token, orderNo);

        for (int i = 0; i < 10; i++) {
            mAdapterOperateRemoveDataList.add(new AdapterOperateRemoveData("carNo"
                    , "frameNo"
                    , "有线"
                    , "（微贷网2165467987）"
                    , "tNo"
                    , "驾驶舱仪表内"
                    , "TY20170717143432075/2de3a21e0bda4f06acf8bffe62065a48.png"
                    , "TY20170717143432075/dd5a838856314802be6a43d9139ecc51.png"
                    , "2018-08-08"
                    , "杨某某"
                    , "17900000001"));
        }

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
                                , objBean.getDispatchContactPhone()));
                    }
                }

                myHandler.sendEmptyMessage(Data.MSG_1);

            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_1: {
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
