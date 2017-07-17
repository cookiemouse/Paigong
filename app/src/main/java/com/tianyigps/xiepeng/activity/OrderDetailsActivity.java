package com.tianyigps.xiepeng.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.bean.WorkerOrderBean;
import com.tianyigps.xiepeng.manager.NetworkManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.tianyigps.xiepeng.data.Data.MSG_1;
import static com.tianyigps.xiepeng.data.Data.MSG_ERO;

public class OrderDetailsActivity extends Activity {

    private static final String TAG = "OrderDetailsActivity";

    //Title栏
    private TextView mTextViewTitle;
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;

    //  内容
    private TextView mTextViewOrderName, mTextViewOrderNum, mTextViewCallName
            , mTextViewTime, mTextViewAddress, mTextViewRemarks
            , mTextViewInstallTitle, mTextViewInstallContent, mTextViewInfoTitle
            , mTextViewInfoContent;

    private ImageView mImageViewCall;
    private Button mButtonSign;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;

    private String mStringContactPhone, mStringDetail, mStringCity
            , mStringOrderNum, mStringContactName, mStringProvince
            , mStringCustName, mStringDistrict;
    private int mIntOrderType, mIntWirelessNum, mIntRemoveWireNum
            , mIntWireNum, mIntOrderStaus, mIntRemoveWirelessNum
            , mIntReviseFlag, mIntOrderId;
    private float mFloatDoorTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_order_details);

        init();

        setEventListener();
    }

    private void init() {
        mTextViewTitle = findViewById(R.id.tv_layout_title_base_middle);
        mImageViewTitleLeft = findViewById(R.id.iv_layout_title_base_left);
        mImageViewTitleRight = findViewById(R.id.iv_layout_title_base_right);

        mTextViewTitle.setVisibility(View.GONE);
        mImageViewTitleRight.setVisibility(View.GONE);
        mImageViewTitleLeft.setImageResource(R.drawable.ic_back);

        //  内容
        mTextViewOrderName = findViewById(R.id.tv_layout_order_details_content_order_title);
        mTextViewOrderNum = findViewById(R.id.tv_layout_order_details_content_order_number);
        mTextViewCallName = findViewById(R.id.tv_layout_order_details_content_order_name);
        mTextViewTime = findViewById(R.id.tv_layout_order_details_content_order_time);
        mTextViewAddress = findViewById(R.id.tv_layout_order_details_content_order_address);
        mTextViewRemarks = findViewById(R.id.tv_layout_order_details_content_order_remarks_content);
        mTextViewInstallTitle = findViewById(R.id.tv_layout_order_details_content_order_install_title);
        mTextViewInstallContent = findViewById(R.id.tv_layout_order_details_content_order_install_content);
        mTextViewInfoTitle = findViewById(R.id.tv_layout_order_details_content_order_info_title);
        mTextViewInfoContent = findViewById(R.id.tv_layout_order_details_content_order_info_content);

        mButtonSign = findViewById(R.id.btn_layout_order_details_sign);

        mImageViewCall = findViewById(R.id.iv_layout_order_details_content_call);

        mNetworkManager = NetworkManager.getInstance();

        myHandler = new MyHandler();
    }

    private void setEventListener() {
        mImageViewTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // TODO: 2017/7/17 只做测试，并不是调用该接口
        mNetworkManager.getWorkerOrder("205", "25d55ad283aa400af464c76d713c07ad", "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: ");
                Gson gson = new Gson();
                WorkerOrderBean workerOrderBean = gson.fromJson(response.body().string(), WorkerOrderBean.class);
                if (workerOrderBean.isSuccess()) {
//                    for (WorkerOrderBean.ObjBean objBean : workerOrderBean.getObj()) {
//
//                    }
                    WorkerOrderBean.ObjBean objBean = workerOrderBean.getObj().get(0);
                    mStringContactPhone = objBean.getContactPhone();
                    mStringContactName = objBean.getContactName();
                    mStringCustName = objBean.getCustName();
                    mStringDetail = objBean.getDetail();
                    mStringCity = objBean.getCity();
                    mStringProvince = objBean.getProvince();
                    mStringDistrict = objBean.getDistrict();
                    mStringOrderNum = objBean.getOrderNo();

                    mIntWirelessNum = objBean.getWirelessNum();
                    mIntRemoveWireNum = objBean.getRemoveWiredNum();
                    mIntOrderType = objBean.getOrderType();
                    mIntWireNum = objBean.getWiredNum();
                    mIntOrderStaus = objBean.getOrderStatus();
                    mIntRemoveWirelessNum = objBean.getRemoveWirelessNum();
                    mIntReviseFlag = objBean.getReviseFlag();
                    mIntOrderId = objBean.getOrderId();

                    mFloatDoorTime = objBean.getDoorTime();

//                    Message message = new Message();
//                    message.arg1 = MSG_1;
//                    myHandler.sendMessage();
                    myHandler.sendEmptyMessage(MSG_1);
                }
            }
        });
    }

    //  Handler
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage: ");
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ERO: {
                    break;
                }
                case MSG_1: {
                    mTextViewOrderName.setText(mStringCustName);
                    mTextViewOrderNum.setText(mStringOrderNum);
                    mTextViewCallName.setText(mStringContactName);

                    mTextViewTime.setText("" + mFloatDoorTime);

                    mTextViewAddress.setText(mStringProvince + mStringCity + mStringDistrict);
                    mTextViewRemarks.setText(mStringDetail);

                    switch (mIntOrderType){
                        case 1:{
                            //  新安装
                            mTextViewInstallTitle.setText("安装");
                            mTextViewInstallContent.setText("有线" + mIntWireNum + "个" +
                                    "，无线"  + mIntWirelessNum + "个");
                            break;
                        }
                        case 2:{
                            mTextViewInstallTitle.setText("维修");
                            //  维修
                            mTextViewInstallContent.setText("有线" + mIntWireNum + "个" +
                                    "，无线"  + mIntWirelessNum + "个");
                            break;
                        }
                        case 3:{
                            mTextViewInstallTitle.setText("拆改");
                            mTextViewInstallContent.setText("有线" + mIntRemoveWireNum + "个" +
                                    "，无线"  + mIntRemoveWirelessNum + "个");
                            //  拆改
                            break;
                        }
                        default:{
                            Log.i(TAG, "handleMessage: OrderType.default");
                        }
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
