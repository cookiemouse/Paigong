package com.tianyigps.dispatch2.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.tianyigps.cycleprogressview.CycleProgressView;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.bean.OrderDetailsBean;
import com.tianyigps.dispatch2.bean.SignWorkerBean;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragment;
import com.tianyigps.dispatch2.dialog.ReturnOrderDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderInfoHandingListener;
import com.tianyigps.dispatch2.interfaces.OnSignedWorkerListener;
import com.tianyigps.dispatch2.manager.LocateManager;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.TimeFormatU;

import static com.tianyigps.dispatch2.data.Data.DATA_INTENT_ORDER_DETAILS_RESULT_SIGNED;
import static com.tianyigps.dispatch2.data.Data.DATA_INTENT_ORDER_NO;
import static com.tianyigps.dispatch2.data.Data.MSG_1;
import static com.tianyigps.dispatch2.data.Data.MSG_2;
import static com.tianyigps.dispatch2.data.Data.MSG_3;
import static com.tianyigps.dispatch2.data.Data.MSG_ERO;

public class OrderDetailsActivity extends Activity {

    private static final String TAG = "OrderDetailsActivity";

    private static final long TIME_2_HOUR = 7200000;
    private static final long TIME_1_SEC = 1000;

    //Title栏
    private TextView mTextViewTitle;
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;

    //  内容
    private TextView mTextViewOrderName, mTextViewOrderNum, mTextViewCallName, mTextViewTime;
    private TextView mTextViewAddress, mTextViewRemarks, mTextViewInstallTitle, mTextViewInstallContent;
    private TextView mTextViewInfoTitle, mTextViewInfoContent, mTextViewReturnOrder, mTextViewTimeRemain;
    private TextView mTextViewRemove;

    private RelativeLayout mRelativeLayoutRemove;

    private ImageView mImageViewCall;
    private Button mButtonSign;

    private boolean isChecked = false;

    private CycleProgressView mCycleProgressView;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private SharedpreferenceManager mSharedpreferenceManager;
    private int eid;
    private String token;
    private String userName;
    private String eName;
    private String orderNo;

    private String mStringMessage;

    //  签到定位
    private static final String MAP_TYPE = "bd";
    private LocateManager mLocateManager;
    private LatLng mLatLngLocate;

    private String mStringContactPhone, mStringDetail, mStringCity, mStringOrderNum,
            mStringContactName, mStringProvince, mStringCustName, mStringDistrict, mStringTypeTitle,
            mStringTypeContent, mStringInfoTitle, mStringInstallInfo = "", mStringTno;
    private String mStringRemoveContent = "";
    private int mIntOrderType, mIntWirelessNum, mIntRemoveWireNum, mIntWireNum, mIntOrderStaus, mIntRemoveWirelessNum, mIntReviseFlag, mIntOrderId;
    private long mLongDoorTime;

    //  退单对话框
    private ReturnOrderDialogFragment mReturnOrderDialogFragment;

    //  LoadingFragment
    private LoadingDialogFragment mLoadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_order_details);

        init();

        setEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLongDoorTime > 0) {
            updateTime();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        myHandler.removeMessages(Data.MSG_2);
    }

    private void init() {
        mTextViewTitle = findViewById(R.id.tv_layout_title_base_middle);
        mImageViewTitleLeft = findViewById(R.id.iv_layout_title_base_left);
        mImageViewTitleRight = findViewById(R.id.iv_layout_title_base_right);

        mTextViewTitle.setVisibility(View.GONE);
        mImageViewTitleRight.setVisibility(View.GONE);
        mImageViewTitleLeft.setImageResource(R.drawable.ic_back);

        mReturnOrderDialogFragment = new ReturnOrderDialogFragment();
        mLoadingDialogFragment = new LoadingDialogFragment();

        Intent intent = getIntent();
        orderNo = intent.getStringExtra(DATA_INTENT_ORDER_NO);


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
        mTextViewTimeRemain = findViewById(R.id.tv_activity_order_details_time);
        mRelativeLayoutRemove = findViewById(R.id.rl_layout_order_details_content_remove);
        mTextViewRemove = findViewById(R.id.tv_layout_order_details_content_remove_content);

        mButtonSign = findViewById(R.id.btn_layout_order_details_sign);

        mImageViewCall = findViewById(R.id.iv_layout_order_details_content_call);

        mCycleProgressView = findViewById(R.id.cpv_activity_order_details);

        mCycleProgressView.setStrokWidth(10);
        mCycleProgressView.setDefaultColor(getResources().getColor(R.color.colorCycleGray));

        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();
        mSharedpreferenceManager = new SharedpreferenceManager(this);
        mLocateManager = new LocateManager(this);

        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();
        eName = mSharedpreferenceManager.getName();

        showLoading();
        mNetworkManager.getWorkerOrderInfoHanding(eid, token, orderNo, userName);
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

        mButtonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChecked) {
                    // 2017/8/2 已签到，开始
                    Intent intent = new Intent(OrderDetailsActivity.this, InstallingActivity.class);
                    intent.putExtra(Data.DATA_INTENT_ORDER_NO, orderNo);
                    intent.putExtra(Data.DATA_INTENT_INSTALL_TYPE, (mIntOrderType - 1));
                    startActivity(intent);
                    return;
                }
                // 2017/8/2 签到
                showAskSignDialog();
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

        mLocateManager.setOnReceiveLocationListener(new LocateManager.OnReceiveLocationListener() {
            @Override
            public void onReceive(LatLng latLng) {
                mLatLngLocate = latLng;
                myHandler.sendEmptyMessage(Data.MSG_3);
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
                mStringDistrict = objBean.getDistrict() + objBean.getDetail();
                mStringDetail = objBean.getInstallDemand();
                mIntOrderType = objBean.getOrderType();
                mIntOrderStaus = objBean.getOrderStatus();

                mIntWireNum = objBean.getWiredNum();
                mIntWirelessNum = objBean.getWirelessNum();
                mIntRemoveWireNum = objBean.getRemoveWiredNum();
                mIntRemoveWirelessNum = objBean.getRemoveWirelessNum();

                long checkTime = objBean.getCheckInTime();
                Log.i(TAG, "onSuccess: checkTime-->" + checkTime);
                isChecked = (0 != checkTime);

                switch (mIntOrderType) {
                    case 1: {
                        //  新安装
                        mStringTypeTitle = "安装";
                        mStringInfoTitle = mStringTypeTitle + "车辆信息";
                        mStringTypeContent = "安装：有线" + mIntWireNum + "个" +
                                "，无线" + mIntWirelessNum + "个";
                        break;
                    }
                    case 2: {
                        //  维修
                        mStringTypeTitle = "维修";
                        mStringInfoTitle = mStringTypeTitle + "车辆信息";
                        mStringTypeContent = "维修：有线" + mIntWireNum + "个" +
                                "，无线" + mIntWirelessNum + "个";
                        break;
                    }
                    case 3: {
                        //  拆改
                        mStringTypeTitle = "安装";
                        mStringInfoTitle = mStringTypeTitle + "车辆信息";
                        mStringTypeContent = "拆除：有线" + mIntRemoveWireNum + "个" +
                                "，无线" + mIntRemoveWirelessNum + "个\n";
                        mStringTypeContent += "安装：有线" + mIntWireNum + "个" + "， 无线" + mIntWirelessNum + "个";
                        break;
                    }
                    default: {
                        Log.i(TAG, "handleMessage: OrderType.default");
                    }
                }

                for (OrderDetailsBean.ObjBean.CarInfoBean carInfoBean : objBean.getCarInfo()) {
                    if (carInfoBean.getRemoveFlag() == 0) {
                        String carVin = carInfoBean.getCarVin();
                        if (null != carVin) {
                            mStringInstallInfo += carVin;
                        }
                        String carBrand = carInfoBean.getCarBrand();
                        if (null != carBrand && !"".equals(carBrand)) {
                            mStringInstallInfo += ("，" + carInfoBean.getCarBrand());
                        }
                        mStringInstallInfo += "\n";
                    } else {
                        String carVin = carInfoBean.getCarVin();
                        if (null != carVin) {
                            mStringRemoveContent += carVin;
                        }
                        String carBrand = carInfoBean.getCarBrand();
                        if (null != carBrand && !"".equals(carBrand)) {
                            mStringRemoveContent += ("，" + carInfoBean.getCarBrand());
                        }
                        mStringRemoveContent += "\n";
                    }
                }

                myHandler.sendEmptyMessage(MSG_1);
            }
        });

        mNetworkManager.setSignedWorkerListener(new OnSignedWorkerListener() {
            @Override
            public void onFailure() {
                mStringMessage = "数据请求失败，请检查网络！";
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                SignWorkerBean signWorkerBean = gson.fromJson(result, SignWorkerBean.class);
                if (!signWorkerBean.isSuccess()) {
                    mStringMessage = signWorkerBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }
                myHandler.sendEmptyMessage(Data.MSG_4);
            }
        });
    }

    private void toCalll(String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void updateTime() {
        long timeNow = System.currentTimeMillis();
        long timeRemain = mLongDoorTime - timeNow;

        Log.i(TAG, "updateTime.timeRemain: -->" + timeRemain);
        Log.i(TAG, "updateTime.TIME_2_HOUR: -->" + TIME_2_HOUR);
        Log.i(TAG, "updateTime: -->" + new TimeFormatU().millisToColock(timeRemain));

        if (timeRemain > TIME_2_HOUR) {
            mCycleProgressView.setProgress(100);
            mTextViewTimeRemain.setText(new TimeFormatU().millisToColock(TIME_2_HOUR));
        } else if (timeRemain < 0) {
            mCycleProgressView.setProgress(0);
            mCycleProgressView.setDefaultColor(getResources().getColor(R.color.colorOrange));
            mTextViewTimeRemain.setText("00:00");
            mTextViewTimeRemain.setTextColor(getResources().getColor(R.color.colorRed));
        } else {
            mTextViewTimeRemain.setText(new TimeFormatU().millisToColock(timeRemain));
            mCycleProgressView.setProgress((int) (timeRemain * 100 / TIME_2_HOUR));
        }

        myHandler.sendEmptyMessageDelayed(MSG_2, TIME_1_SEC);
    }

    //  显示信息Dialog
    private void showMessageDialog(String msg) {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
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

    //  显示退单对话框
    private void showReturnOrderDialog() {
        Bundle bundle = new Bundle();
        bundle.putString(DATA_INTENT_ORDER_NO, orderNo);
        mReturnOrderDialogFragment.setArguments(bundle);
        mReturnOrderDialogFragment.show(getFragmentManager(), "ReturnOrder");
    }

    //  确认签到对话框
    private void showAskSignDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_ask_sign, null);
        builder.setView(viewDialog);

        final Dialog dialog = builder.create();
        Button buttonCancel = viewDialog.findViewById(R.id.btn_dialog_ask_sign_cancel);
        Button buttonEnsure = viewDialog.findViewById(R.id.btn_dialog_ask_sign_ensure);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        buttonEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/20 签到
                mLocateManager.startLocate();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //  显示LoadingFragment
    private void showLoading() {
        mLoadingDialogFragment.show(getFragmentManager(), "LoadingFragment");
    }

    //  Handler
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage: ");
            if (mLoadingDialogFragment.isAdded()) {
                mLoadingDialogFragment.dismiss();
            }

            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ERO: {
                    showMessageDialog(mStringMessage);
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
                    mTextViewRemove.setText(mStringRemoveContent);

                    if (mIntOrderType == 3) {
                        mRelativeLayoutRemove.setVisibility(View.VISIBLE);
                    } else {
                        mRelativeLayoutRemove.setVisibility(View.GONE);
                    }

                    if (isChecked) {
                        isChecked = true;
                        mButtonSign.setText("开始");
                        mTextViewReturnOrder.setVisibility(View.GONE);
                    } else {
                        mButtonSign.setText("签到");
                        mTextViewReturnOrder.setVisibility(View.VISIBLE);
                    }

                    updateTime();
                    break;
                }
                case MSG_2: {
                    updateTime();
                    break;
                }
                case MSG_3: {
                    //  获取到当前位置，并签到
                    showLoading();
                    mNetworkManager.signedWorker(eid, token, eName, orderNo
                            , mLatLngLocate.latitude, mLatLngLocate.longitude
                            , MAP_TYPE
                            , userName);
                    break;
                }
                case Data.MSG_4: {
                    //  签到成功，应该finish，然后显示HandingFragment
                    getIntent().putExtra(DATA_INTENT_ORDER_DETAILS_RESULT_SIGNED, true);
                    finish();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
