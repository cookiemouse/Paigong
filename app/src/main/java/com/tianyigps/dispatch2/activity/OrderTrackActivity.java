package com.tianyigps.dispatch2.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.adapter.OrderTrackAdapter;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.bean.OrderTrackBean;
import com.tianyigps.dispatch2.data.AdapterOrderTrackData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.interfaces.OnOrderTrackListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.TimeFormatU;

import java.util.ArrayList;
import java.util.List;

public class OrderTrackActivity extends BaseActivity {

    private static final String TAG = "OrderTrackActivity";

    private ListView mListView;
    private List<AdapterOrderTrackData> mAdapterOrderTrackDataList;
    private OrderTrackAdapter mOrderTrackAdapter;

    private SharedpreferenceManager mSharedpreferenceManager;
    private String jobNo;
    private String token;
    private String userName;
    private int orderId;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private String mStringMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_track);

        init();

        setEventListener();
    }

    private void init() {
        this.setTitleText("订单跟踪");
        this.setTitleRightButtonVisibilite(false);

        mSharedpreferenceManager = new SharedpreferenceManager(this);
        jobNo = mSharedpreferenceManager.getJobNo();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();

        orderId = getIntent().getIntExtra(Data.DATA_INTENT_ORDER_ID, 0);

        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();

        mListView = findViewById(R.id.lv_activity_order_track);
        mAdapterOrderTrackDataList = new ArrayList<>();

        mOrderTrackAdapter = new OrderTrackAdapter(this, mAdapterOrderTrackDataList);

        mListView.setAdapter(mOrderTrackAdapter);

        mNetworkManager.getOrderTrack(jobNo, token, userName, orderId);
    }

    private void setEventListener() {

        mNetworkManager.setOnOrderTrackListener(new OnOrderTrackListener() {
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
                OrderTrackBean orderTrackBean = gson.fromJson(result, OrderTrackBean.class);
                if (!orderTrackBean.isSuccess()) {
                    mStringMessage = Data.DEFAULT_MESSAGE;
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                mAdapterOrderTrackDataList.clear();

                for (OrderTrackBean.ObjBean objBean : orderTrackBean.getObj()) {
                    mAdapterOrderTrackDataList.add(new AdapterOrderTrackData(objBean.getOrderNode().getUpdateTime(), func(objBean)));
                }

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });
    }

    private String func(OrderTrackBean.ObjBean objBean) {
        String info = "";
        OrderTrackBean.ObjBean.OrderNodeBean orderNodeBean = objBean.getOrderNode();
        int node = orderNodeBean.getNode();
        switch (node) {
            case 1: {
                info += "收到订单";     //ok
                break;
            }
            case 2: {
                info += "申请改约\n";     //not complete
                String time = new TimeFormatU().millisToDate2(orderNodeBean.getReviseTime());
                info += time + "\n";
                info += orderNodeBean.getReasonFilled();
                break;
            }
            case 3: {
                info += "审核改约：";    //ok
                if (orderNodeBean.getReviseStatus() == 1) {
                    info += "不通过\n";
                    info += orderNodeBean.getReasonFilled();
                } else {
                    info += "通过";
                }
                break;
            }
            case 4: {
                info += "派单\n";     //ok
                OrderTrackBean.ObjBean.EngineerBean engineerBean = objBean.getEngineer();
                info += engineerBean.getJobNo() + "\t" + engineerBean.getName() + "\t";
                if (orderNodeBean.getPayDoorFee() == 0) {
                    info += "不支付上门费";
                } else {
                    info += "支付上门费";
                }
                break;
            }
            case 5: {
                info += "取消订单\n";       //ok
                info += orderNodeBean.getCheckFalseReason();
                break;
            }
            case 6: {
                info += "修改订单";     //ok
                break;
            }
            case 7: {
                info += "联系现场";     //ok
                break;
            }
            case 8: {
                info += "工程师已到达\n";     //ok
                info += orderNodeBean.getSubmitAddress();
                break;
            }
            case 9: {
                info += "开始安装";     //ok
                break;
            }
            case 10: {
                info += "继续安装";     //ok
                break;
            }
            case 11: {
                info += "安装退回：";        //ok
                info += orderNodeBean.getCheckFalseReason();
                info += orderNodeBean.getReasonFilled();
                break;
            }
            case 12: {
                info += "部分完成：";     //ok
                info += orderNodeBean.getReasonFilled() + "\n";
                info += orderNodeBean.getSubmitAddress();
                break;
            }
            case 13: {
                info += "退回客户\n";       //ok
                info += orderNodeBean.getCheckFalseReason();
                info += orderNodeBean.getReasonFilled();
                break;
            }
            case 14: {
                info += "完成订单"; //ok
                info += orderNodeBean.getSubmitAddress();
                break;
            }
            default: {
                Log.i(TAG, "func: default-->" + node);
            }
        }
        return info;
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_ERO: {
                    break;
                }
                case Data.MSG_1: {
                    mOrderTrackAdapter.notifyDataSetChanged();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default-->" + msg.what);
                }
            }
        }
    }
}
