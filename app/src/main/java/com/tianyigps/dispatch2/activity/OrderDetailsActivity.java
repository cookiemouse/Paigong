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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.tianyigps.cycleprogressview.CycleProgressView;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.bean.OrderDetailsBean;
import com.tianyigps.dispatch2.bean.SignWorkerBean;
import com.tianyigps.dispatch2.bean.StartHandingBean;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragment;
import com.tianyigps.dispatch2.dialog.ReturnOrderDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderInfoHandingListener;
import com.tianyigps.dispatch2.interfaces.OnSignedWorkerListener;
import com.tianyigps.dispatch2.interfaces.OnStartHandingListener;
import com.tianyigps.dispatch2.manager.LocateManager;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.ClipU;
import com.tianyigps.dispatch2.utils.RegularU;
import com.tianyigps.dispatch2.utils.TimeFormatU;
import com.tianyigps.dispatch2.utils.ToastU;

public class OrderDetailsActivity extends Activity {

    private static final String TAG = "OrderDetailsActivity";

    private static final long TIME_2_HOUR = 7200000;
    private static final long TIME_1_MIN = 60000;
    private static final long TIME_1_SEC = 1000;

    //Title栏
    private TextView mTextViewTitle;
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;

    //  内容
    private TextView mTextViewOrderName, mTextViewOrderNum, mTextViewCallName, mTextViewTime;
    private TextView mTextViewAddress, mTextViewRemarks, mTextViewInstallTitle, mTextViewInstallContent;
    private TextView mTextViewInfoTitle, mTextViewInfoContent, mTextViewReturnOrder, mTextViewTimeRemain;
    private TextView mTextViewRemove, mTextViewDoorTimeModify, mTextViewCaigai;

    private RelativeLayout mRelativeLayoutRemove, mRelativeLayoutCaigai;

    private ImageView mImageViewCall;
    private Button mButtonSign;

    private boolean isChecked = false;

    private CycleProgressView mCycleProgressView;
    private FrameLayout mFrameLayoutCycle;
    private TextView mTextViewCycleTitle;
    private String mStringCaigai = "";
    private ImageView mImageViewInstall;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private SharedpreferenceManager mSharedpreferenceManager;
    private int eid;
    private String token;
    private String userName;
    private String eName;
    private String orderNo;

    private String mStringMessage = "";

    //  签到定位
    private static final String MAP_TYPE = "bd";
    private LocateManager mLocateManager;
    private LatLng mLatLngLocate;

    private String mStringContactPhone, mStringDetail = "", mStringCity, mStringOrderNum,
            mStringContactName, mStringProvince, mStringCustName, mStringDistrict, mStringTypeTitle,
            mStringTypeContent = "", mStringInfoTitle, mStringInstallInfo = "", mStringTno;
    private String mStringRemoveContent = "";
    private int mIntOrderType, mIntWirelessNum, mIntRemoveWireNum, mIntWireNum, mIntOrderStaus, mIntRemoveWirelessNum, mIntReviseFlag, mIntOrderId;
    private int mIntWireFinish, mIntWirelessFinish, mIntRemoveWireFinish, mIntRemoveWirelessFinish;
    private long mLongDoorTime;

    //  退单对话框
    private ReturnOrderDialogFragment mReturnOrderDialogFragment;

    //  LoadingFragment
    private LoadingDialogFragment mLoadingDialogFragment;

    private ToastU mToastU;

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
        mToastU = new ToastU(this);
        mTextViewTitle = findViewById(R.id.tv_layout_title_base_middle);
        mImageViewTitleLeft = findViewById(R.id.iv_layout_title_base_left);
        mImageViewTitleRight = findViewById(R.id.iv_layout_title_base_right);

        mTextViewTitle.setVisibility(View.GONE);
        mImageViewTitleRight.setVisibility(View.GONE);
        mImageViewTitleLeft.setImageResource(R.drawable.ic_back);

        mReturnOrderDialogFragment = new ReturnOrderDialogFragment();
        mLoadingDialogFragment = new LoadingDialogFragment();

        Intent intent = getIntent();
        orderNo = intent.getStringExtra(Data.DATA_INTENT_ORDER_NO);


        //  内容
        mTextViewOrderName = findViewById(R.id.tv_layout_order_details_content_order_title);
        mTextViewOrderNum = findViewById(R.id.tv_layout_order_details_content_order_number);
        mTextViewCallName = findViewById(R.id.tv_layout_order_details_content_order_name);
        mTextViewTime = findViewById(R.id.tv_layout_order_details_content_order_time);
        mTextViewDoorTimeModify = findViewById(R.id.tv_layout_order_details_content_order_time_modify);
        mTextViewAddress = findViewById(R.id.tv_layout_order_details_content_order_address);
        mTextViewRemarks = findViewById(R.id.tv_layout_order_details_content_order_remarks_content);
        mTextViewInstallTitle = findViewById(R.id.tv_layout_order_details_content_order_install_title);
        mTextViewInstallContent = findViewById(R.id.tv_layout_order_details_content_order_install_content);
        mTextViewInfoTitle = findViewById(R.id.tv_layout_order_details_content_order_info_title);
        mTextViewInfoContent = findViewById(R.id.tv_layout_order_details_content_order_info_content);
        mTextViewReturnOrder = findViewById(R.id.tv_layout_order_details_return_order);
        mTextViewTimeRemain = findViewById(R.id.tv_activity_order_details_time);
        mRelativeLayoutRemove = findViewById(R.id.rl_layout_order_details_content_remove);
        mRelativeLayoutCaigai = findViewById(R.id.rl_layout_order_details_content_caigai);
        mTextViewRemove = findViewById(R.id.tv_layout_order_details_content_remove_content);
        mTextViewCaigai = findViewById(R.id.tv_layout_order_details_content_order_caigai_content);
        mImageViewInstall = findViewById(R.id.iv_layout_order_details_install);

        mButtonSign = findViewById(R.id.btn_layout_order_details_sign);

        mImageViewCall = findViewById(R.id.iv_layout_order_details_content_call);

        mCycleProgressView = findViewById(R.id.cpv_activity_order_details);
        mFrameLayoutCycle = findViewById(R.id.fl_activity_order_details_cycle);
        mTextViewCycleTitle = findViewById(R.id.tv_activity_order_details_cycle_title);

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
                    showLoading();
                    mNetworkManager.startHanding(eid, token, orderNo, eName, userName);
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

        mTextViewAddress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipU.clip(OrderDetailsActivity.this, mStringProvince + mStringCity + mStringDistrict);
                mToastU.showToast("地址已成功复制");
                return true;
            }
        });

        mTextViewOrderName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipU.clip(OrderDetailsActivity.this, mStringCustName);
                mToastU.showToast("客户名称已成功复制");
                return true;
            }
        });

        mTextViewOrderNum.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipU.clip(OrderDetailsActivity.this, mStringOrderNum);
                mToastU.showToast("订单编号已成功复制");
                return true;
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
                if (latLng.latitude == 4.9E-324 || latLng.longitude == 4.9E-324) {
                    mLatLngLocate = new LatLng(0, 0);
                } else {
                    mLatLngLocate = latLng;
                }
                myHandler.sendEmptyMessage(Data.MSG_3);
            }
        });

        mNetworkManager.setGetWorkerOrderInfoListener(new OnGetWorkerOrderInfoHandingListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(Data.MSG_ERO);
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
                if (!RegularU.isEmpty(objBean.getInstallDemand())) {
                    mStringDetail = "【" + objBean.getInstallDemand() + "】";
                }
                if (!RegularU.isEmpty(objBean.getRemark())) {
                    mStringDetail += objBean.getRemark();
                }
                mIntOrderType = objBean.getOrderType();
                mIntOrderStaus = objBean.getOrderStatus();
                mIntReviseFlag = objBean.getReviseFlag();

                mIntWireNum = objBean.getWiredNum();
                mIntWirelessNum = objBean.getWirelessNum();
                mIntRemoveWireNum = objBean.getRemoveWiredNum();
                mIntRemoveWirelessNum = objBean.getRemoveWirelessNum();

                mIntWireFinish = objBean.getFinishWiredNum();
                mIntWirelessFinish = objBean.getFinishWirelessNum();
                mIntRemoveWireFinish = objBean.getRemoveFinishWiredNum();
                mIntRemoveWirelessFinish = objBean.getRemoveFinishWirelessNum();

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
                        if (mIntWireFinish > 0 || mIntWirelessFinish > 0) {
                            mStringTypeContent += "\n完成：有线" + mIntWireFinish + "个"
                                    + "，无线" + mIntWirelessFinish + "个";
                        }
                        break;
                    }
                    case 2: {
                        //  维修
                        mStringTypeTitle = "维修";
                        mStringInfoTitle = mStringTypeTitle + "车辆信息";
                        mStringTypeContent = "维修：有线" + mIntWireNum + "个" +
                                "，无线" + mIntWirelessNum + "个";
                        if (mIntWireFinish > 0 || mIntWirelessFinish > 0) {
                            mStringTypeContent += "\n完成：有线" + mIntWireFinish + "个"
                                    + "，无线" + mIntWirelessFinish + "个";
                        }
                        break;
                    }
                    case 3: {
                        //  拆改
                        mStringTypeTitle = "安装";
                        mStringInfoTitle = mStringTypeTitle + "车辆信息";
                        mStringCaigai = "拆除：有线" + mIntRemoveWireNum + "个" +
                                "，无线" + mIntRemoveWirelessNum + "个\n";
                        if (mIntRemoveWireFinish > 0 || mIntRemoveWirelessFinish > 0
                                || mIntWireFinish > 0 || mIntWirelessFinish > 0) {
                            mStringCaigai += "完成：有线" + mIntRemoveWireFinish + "个" +
                                    "，无线" + mIntRemoveWirelessFinish + "个";
                        }
                        mStringTypeContent += "安装：有线" + mIntWireNum + "个" + "， 无线" + mIntWirelessNum + "个";
                        if (mIntRemoveWireFinish > 0 || mIntRemoveWirelessFinish > 0
                                || mIntWireFinish > 0 || mIntWirelessFinish > 0) {
                            mStringTypeContent += "\n完成：有线" + mIntWireFinish + "个" +
                                    "，无线" + mIntWirelessFinish + "个";
                        }
                        break;
                    }
                    default: {
                        Log.i(TAG, "handleMessage: OrderType.default");
                    }
                }

                for (OrderDetailsBean.ObjBean.CarInfoBean carInfoBean : objBean.getCarInfo()) {
                    if (carInfoBean.getRemoveFlag() == 0) {
                        String carVin = carInfoBean.getCarVin();
                        String carNo = carInfoBean.getCarNo();
                        String carBrand = carInfoBean.getCarBrand();
                        String owner = carInfoBean.getOwnerName();
                        if (null != carVin && !"".equals(carVin)) {
                            mStringInstallInfo += carVin;
                        } else if (null != carNo && !"".equals(carNo)) {
                            mStringInstallInfo += carNo;
                        }
                        if (!"".equals(mStringInstallInfo)){
                            if (!RegularU.isEmpty(owner)){
                                mStringInstallInfo += ("，" + owner);
                            }else {
                                mStringInstallInfo += "\n";
                            }
                        }
                        if (!"".equals(mStringInstallInfo)) {
                            if (!RegularU.isEmpty(carBrand)) {
                                mStringInstallInfo += ("，" + carBrand + "\n");
                            } else {
                                mStringInstallInfo += "\n";
                            }
                        }
                    } else {
                        String carVin = carInfoBean.getCarVin();
                        String carNo = carInfoBean.getCarNo();
                        String carBrand = carInfoBean.getCarBrand();
                        String owner = carInfoBean.getOwnerName();
                        if (null != carVin && !"".equals(carVin)) {
                            mStringRemoveContent += carVin;
                        } else if (null != carNo && !"".equals(carNo)) {
                            mStringRemoveContent += carNo;
                        }
                        if (!"".equals(mStringRemoveContent)){
                            if (!RegularU.isEmpty(owner)){
                                mStringRemoveContent += ("，" + owner);
                            }else {
                                mStringRemoveContent += "\n";
                            }
                        }
                        if (!"".equals(mStringRemoveContent)) {
                            if (!RegularU.isEmpty(carBrand)) {
                                mStringRemoveContent += ("，" + carBrand + "\n");
                            } else {
                                mStringRemoveContent += "\n";
                            }
                        }
                    }
                }

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });

        mNetworkManager.setStartHandingListener(new OnStartHandingListener() {
            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                StartHandingBean startHandingBean = gson.fromJson(result, StartHandingBean.class);
                if (!startHandingBean.isSuccess()) {
                    mStringMessage = startHandingBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }
                myHandler.sendEmptyMessage(Data.MSG_5);
            }
        });

        mNetworkManager.setSignedWorkerListener(new OnSignedWorkerListener() {
            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                SignWorkerBean signWorkerBean = gson.fromJson(result, SignWorkerBean.class);
                if (!signWorkerBean.isSuccess()) {
                    mStringMessage = signWorkerBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_6);
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
        long timeRemain = mLongDoorTime - timeNow + TIME_1_MIN;

        Log.i(TAG, "updateTime.timeRemain: -->" + timeRemain);
        Log.i(TAG, "updateTime.HourMin -->" + new TimeFormatU().millsToHourMin(timeRemain));

        if (timeRemain > TIME_2_HOUR) {
            mCycleProgressView.setProgress(100);
            mTextViewTimeRemain.setText(new TimeFormatU().millsToHourMin(TIME_2_HOUR));
        } else if (timeRemain < 0) {
            mCycleProgressView.setProgress(0);
            mCycleProgressView.setDefaultColor(getResources().getColor(R.color.colorOrange));
            mTextViewTimeRemain.setText("00:00");
            mTextViewTimeRemain.setTextColor(getResources().getColor(R.color.colorRed));
        } else {
            mTextViewTimeRemain.setText(new TimeFormatU().millsToHourMin(timeRemain));
            mCycleProgressView.setProgress((int) (timeRemain * 100 / TIME_2_HOUR));
        }

        myHandler.sendEmptyMessageDelayed(Data.MSG_2, TIME_1_SEC);
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
        bundle.putString(Data.DATA_INTENT_ORDER_NO, orderNo);
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

    //  订单已被取消对话框
    private void showNotPerfectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
        View viewDialog = LayoutInflater.from(OrderDetailsActivity.this).inflate(R.layout.dialog_message_editable, null);
        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        TextView textView = viewDialog.findViewById(R.id.tv_dialog_message_message);
        Button buttonCancel = viewDialog.findViewById(R.id.btn_dialog_message_cancel);
        textView.setText(mStringMessage);
        buttonCancel.setText(getString(R.string.known));
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailsActivity.this.finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //  显示LoadingFragment
    private void showLoading() {
        mLoadingDialogFragment.show(getFragmentManager(), "LoadingFragment");

        /*
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.add(mLoadingDialogFragment, this.getClass().getSimpleName());
        ft.commitAllowingStateLoss();
        */
    }

    //  Handler
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage: ");

            if (mLoadingDialogFragment.isAdded() && msg.what != Data.MSG_2) {
                mLoadingDialogFragment.dismissAllowingStateLoss();
            }

            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_ERO: {
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    mTextViewOrderName.setText(mStringCustName);
                    mTextViewOrderNum.setText(mStringOrderNum);
                    mTextViewCallName.setText(mStringContactName);

                    mTextViewTime.setText(new TimeFormatU().millisToDate2(mLongDoorTime));

                    mTextViewAddress.setText(mStringProvince + mStringCity + mStringDistrict);
                    mTextViewRemarks.setText(mStringDetail);
                    mTextViewInstallTitle.setText(mStringTypeTitle);
                    mTextViewInstallContent.setText(mStringTypeContent);
                    mTextViewInfoTitle.setText(mStringInfoTitle);
                    mTextViewInfoContent.setText(mStringInstallInfo);
                    mTextViewRemove.setText(mStringRemoveContent);

                    if (mIntOrderType == 3) {
                        mRelativeLayoutRemove.setVisibility(View.VISIBLE);
                        mRelativeLayoutCaigai.setVisibility(View.VISIBLE);
                        mTextViewCaigai.setText(mStringCaigai);
                        mImageViewInstall.setVisibility(View.INVISIBLE);
                    } else {
                        mRelativeLayoutRemove.setVisibility(View.GONE);
                        mRelativeLayoutCaigai.setVisibility(View.GONE);
                    }

                    if (mIntReviseFlag == 1) {
                        mTextViewDoorTimeModify.setVisibility(View.VISIBLE);
                    } else {
                        mTextViewDoorTimeModify.setVisibility(View.GONE);
                    }

                    if (isChecked) {
                        isChecked = true;
                        mButtonSign.setText("开始");
                        mTextViewReturnOrder.setVisibility(View.GONE);

                        mFrameLayoutCycle.setVisibility(View.GONE);
                        mTextViewCycleTitle.setVisibility(View.VISIBLE);
                    } else {
                        mButtonSign.setText("签到");
                        mTextViewReturnOrder.setVisibility(View.VISIBLE);
                    }

                    updateTime();
                    break;
                }
                case Data.MSG_2: {
                    updateTime();
                    break;
                }
                case Data.MSG_3: {
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
                    Intent intent = new Intent(OrderDetailsActivity.this, WorkerFragmentContentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Data.DATA_INTENT_WORKER_FRAGMENT, Data.DATA_INTENT_WORKER_FRAGMENT_HANDING);
                    startActivity(intent);
                    break;
                }
                case Data.MSG_5: {
                    //  开始
                    Intent intent = new Intent(OrderDetailsActivity.this, InstallingActivity.class);
                    intent.putExtra(Data.DATA_INTENT_ORDER_NO, orderNo);
                    intent.putExtra(Data.DATA_INTENT_INSTALL_TYPE, (mIntOrderType - 1));
                    startActivity(intent);
                    break;
                }
                case Data.MSG_6: {
                    //  签到失败，
                    showNotPerfectDialog();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
