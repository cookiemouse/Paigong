package com.tianyigps.xiepeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.tianyigps.xiepeng.bean.OrderDetailsBean;
import com.tianyigps.xiepeng.dialog.ReturnOrderDialogFragment;
import com.tianyigps.xiepeng.interfaces.OnGetWorkerOrderInfoHandingListener;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;
import com.tianyigps.xiepeng.utils.TimeFormatU;

import static com.tianyigps.xiepeng.data.Data.DATA_INTENT_ORDER_NO;
import static com.tianyigps.xiepeng.data.Data.MSG_1;
import static com.tianyigps.xiepeng.data.Data.MSG_ERO;

public class OrderDetailsActivity extends Activity {

    private static final String TAG = "OrderDetailsActivity";

    //Title栏
    private TextView mTextViewTitle;
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;

    //  内容
    private TextView mTextViewOrderName, mTextViewOrderNum, mTextViewCallName, mTextViewTime, mTextViewAddress, mTextViewRemarks, mTextViewInstallTitle, mTextViewInstallContent, mTextViewInfoTitle, mTextViewInfoContent, mTextViewReturnOrder;

    private ImageView mImageViewCall;
    private Button mButtonSign;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private SharedpreferenceManager mSharedpreferenceManager;
    private String orderNo;

    private String mStringContactPhone, mStringDetail, mStringCity, mStringOrderNum,
            mStringContactName, mStringProvince, mStringCustName, mStringDistrict, mStringTypeTitle,
            mStringTypeContent, mStringInfoTitle, mStringInstallInfo = "";
    private int mIntOrderType, mIntWirelessNum, mIntRemoveWireNum, mIntWireNum, mIntOrderStaus, mIntRemoveWirelessNum, mIntReviseFlag, mIntOrderId;
    private long mLongDoorTime;

    //  退单对话框
    private ReturnOrderDialogFragment mReturnOrderDialogFragment;

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

        mReturnOrderDialogFragment = new ReturnOrderDialogFragment();

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
        mTextViewReturnOrder = findViewById(R.id.tv_layout_order_details_return_order);

        mButtonSign = findViewById(R.id.btn_layout_order_details_sign);

        mImageViewCall = findViewById(R.id.iv_layout_order_details_content_call);

        mNetworkManager = NetworkManager.getInstance();
        myHandler = new MyHandler();
        mSharedpreferenceManager = new SharedpreferenceManager(this);

        int eid = mSharedpreferenceManager.getEid();
        String token = mSharedpreferenceManager.getToken();
        orderNo = getIntent().getStringExtra(DATA_INTENT_ORDER_NO);
        Log.i(TAG, "init: orderNo-->" + orderNo);

        mNetworkManager.getWorkerOrderInfoHanding(eid, token, orderNo);
    }

    private void setEventListener() {
        mImageViewTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mImageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCalll(mStringContactPhone);
            }
        });

        mTextViewReturnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  2017/7/19 退单
                showReturnOrderDialog();
            }
        });

        mReturnOrderDialogFragment.setOnFinishListener(new ReturnOrderDialogFragment.OnFinishListener() {
            @Override
            public void onFinish() {
                onBackPressed();
            }
        });

        mNetworkManager.setGetWorkerOrderInfoListener(new OnGetWorkerOrderInfoHandingListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: -->" + result);
                Gson gson = new Gson();
                OrderDetailsBean orderDetailsBean = gson.fromJson(result, OrderDetailsBean.class);
                if (!orderDetailsBean.isSuccess()) {
                    onFailure();
                    return;
                }

                OrderDetailsBean.ObjBean objBean = orderDetailsBean.getObj();
                mStringCustName = objBean.getCustName();
                mStringOrderNum = objBean.getOrderNo();
                mStringContactName = objBean.getContactName();
                mStringContactPhone = objBean.getContactPhone();
                mLongDoorTime = objBean.getDoorTime();
                mStringProvince = objBean.getProvince();
                mStringCity = objBean.getCity();
                mStringDistrict = objBean.getDistrict();
                mStringDetail = objBean.getInstallDemand();
                mIntOrderType = objBean.getOrderType();

                switch (mIntOrderType) {
                    case 1: {
                        //  新安装
                        mStringTypeTitle = "安装";
                        mStringInfoTitle = mStringTypeTitle + "车辆信息";
                        mStringTypeContent = "有线" + mIntWireNum + "个" +
                                "，无线" + mIntWirelessNum + "个";
                        break;
                    }
                    case 2: {
                        //  维修
                        mStringTypeTitle = "维修";
                        mStringInfoTitle = mStringTypeTitle + "车辆信息";
                        mStringTypeContent = "有线" + mIntWireNum + "个" +
                                "，无线" + mIntWirelessNum + "个";
                        break;
                    }
                    case 3: {
                        //  拆改
                        mStringTypeTitle = "拆改";
                        mStringInfoTitle = mStringTypeTitle + "车辆信息";
                        mStringTypeContent = "有线" + mIntRemoveWireNum + "个" +
                                "，无线" + mIntRemoveWirelessNum + "个";
                        break;
                    }
                    default: {
                        Log.i(TAG, "handleMessage: OrderType.default");
                    }
                }

                for (OrderDetailsBean.ObjBean.CarInfoBean carInfoBean : objBean.getCarInfo()) {
                    mStringInstallInfo += carInfoBean.getCarVin();
                    String carBrand = carInfoBean.getCarBrand();
                    if (null != carBrand && !"".equals(carBrand)) {
                        mStringInstallInfo += ("，" + carInfoBean.getCarBrand());
                    }
                    mStringInstallInfo += "\n";
                }

                myHandler.sendEmptyMessage(MSG_1);
            }
        });
    }

    private void toCalll(String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    //  显示退单对话框
    private void showReturnOrderDialog() {
        Bundle bundle = new Bundle();
        bundle.putString(DATA_INTENT_ORDER_NO, orderNo);
        mReturnOrderDialogFragment.setArguments(bundle);
        mReturnOrderDialogFragment.show(getFragmentManager(), "ReturnOrder");
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

                    mTextViewTime.setText(new TimeFormatU().millisToDate(mLongDoorTime));

                    mTextViewAddress.setText(mStringProvince + mStringCity + mStringDistrict);
                    mTextViewRemarks.setText(mStringDetail);
                    mTextViewInstallTitle.setText(mStringTypeTitle);
                    mTextViewInstallContent.setText(mStringTypeContent);
                    mTextViewInfoTitle.setText(mStringInfoTitle);
                    mTextViewInfoContent.setText(mStringInstallInfo);

                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
