package com.tianyigps.dispatch2.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.cycleprogressview.CycleProgressView;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.adapter.PendDetailsAdapter;
import com.tianyigps.dispatch2.bean.PendDetailsBean;
import com.tianyigps.dispatch2.customview.MyListView;
import com.tianyigps.dispatch2.data.AdapterPendDetailsData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.OrderTrackDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnPendDetailsListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.TimeFormatU;

import java.util.ArrayList;
import java.util.List;

public class PendDetailsActivity extends Activity {

    private static final String TAG = "PendDetailsActivity";

    //Title栏
    private TextView mTextViewTitle;
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;

    private TextView mTextViewNode;
    private TextView mTextViewTime;
    private CycleProgressView mCycleProgressView;

    private TextView mTextViewContact, mTextViewContactPhone, mTextViewDoorTime, mTextViewAddress, mTextViewModify, mTextViewRemarks, mTextViewInstallType, mTextViewInstallContent, mTextViewInfoTitle, mTextViewInfoContent, mTextViewRemoveContent;

    private RelativeLayout mRelativeLayoutRemove;

    private MyListView mListView;
    private List<AdapterPendDetailsData> mAdapterPendDetailsDataList;
    private PendDetailsAdapter mPendDetailsAdapter;

    private Button mButtonPend;

    private String mContact, mContactPhone, mAddress, mRemarks, mInstallType, mInstallContent = "", mInfoTitle, mInfoContent = "", mRemoveContent = "";
    private long mDoorTime;
    private boolean isModify;

    private SharedpreferenceManager mSharedpreferenceManager;
    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private String jobNo;
    private String token;
    private String userName;
    private String orderNo;
    private int orderStatus;
    private int orderId;

    private Intent mIntent;

    private String mStringMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_pend_details);

        init();

        setEventListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: requestCode-->" + requestCode);
        Log.i(TAG, "onActivityResult: requestCode-->" + requestCode);
        if (requestCode == Data.DATA_INTENT_CHOICE_WORKER_REQUEST && resultCode == Data.DATA_INTENT_CHOICE_WORKER_RESULT) {
            // 2017/8/14 派单是否成功，如果成功则刷新页面，如果没有则显示对话框并刷新
            boolean success = data.getBooleanExtra(Data.DATA_INTENT_PEND_RESULT, false);
            mIntent.putExtra(Data.DATA_INTENT_PEND_RESULT, success);
            setResult(Data.DATA_INTENT_CHOICE_WORKER_RESULT, mIntent);
        }
    }

    private void init() {
        mTextViewTitle = findViewById(R.id.tv_layout_title_base_middle);
        mImageViewTitleLeft = findViewById(R.id.iv_layout_title_base_left);
        mImageViewTitleRight = findViewById(R.id.iv_layout_title_base_right);

        mTextViewTitle.setVisibility(View.GONE);
        mImageViewTitleRight.setVisibility(View.GONE);
        mImageViewTitleLeft.setImageResource(R.drawable.ic_back);

        mSharedpreferenceManager = new SharedpreferenceManager(this);
        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();

        mIntent = getIntent();
        orderNo = mIntent.getStringExtra(Data.DATA_INTENT_ORDER_NO);
        orderStatus = mIntent.getIntExtra(Data.DATA_INTENT_ORDER_STATUS, 0);

        jobNo = mSharedpreferenceManager.getJobNo();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();

        mTextViewNode = findViewById(R.id.tv_layout_pend_details_content_node);
        mTextViewTime = findViewById(R.id.tv_layout_cycle_time);
        mCycleProgressView = findViewById(R.id.cpv_layout_cycle_time);
        mButtonPend = findViewById(R.id.btn_layout_pend_details);
        mTextViewContact = findViewById(R.id.tv_layout_pend_details_content_contact_name);
        mTextViewContactPhone = findViewById(R.id.tv_layout_pend_details_content_contact_phone);
        mTextViewDoorTime = findViewById(R.id.tv_layout_pend_details_content_time);
        mTextViewAddress = findViewById(R.id.tv_layout_pend_details_content_order_address);
        mTextViewModify = findViewById(R.id.tv_layout_pend_details_content_modify);
        mTextViewRemarks = findViewById(R.id.tv_layout_pend_details_content_order_remarks_content);
        mTextViewInstallType = findViewById(R.id.tv_layout_pend_details_content_order_install_title);
        mTextViewInstallContent = findViewById(R.id.tv_layout_pend_details_content_order_install_content);
        mTextViewInfoTitle = findViewById(R.id.tv_layout_pend_details_content_info_title);
        mTextViewRemoveContent = findViewById(R.id.tv_layout_pend_details_content_remove_content);
        mTextViewInfoContent = findViewById(R.id.tv_layout_pend_details_content_info_content);

        mListView = findViewById(R.id.lv_layout_pend_content);
        mAdapterPendDetailsDataList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            mAdapterPendDetailsDataList.add(new AdapterPendDetailsData(R.drawable.ic_address, "改约不通过", "原因表述不清楚"));
        }

        mPendDetailsAdapter = new PendDetailsAdapter(PendDetailsActivity.this, mAdapterPendDetailsDataList);

        mListView.setAdapter(mPendDetailsAdapter);

        mRelativeLayoutRemove = findViewById(R.id.rl_layout_pend_details_content_remove);

        mNetworkManager.getPendDetails(jobNo, token, orderNo, userName, orderStatus);
    }

    private void setEventListener() {

        mImageViewTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PendDetailsActivity.this.finish();
            }
        });

        mButtonPend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/8/10 派单
                Intent intent = new Intent(PendDetailsActivity.this, ChoiceWorkerActivity.class);
                intent.putExtra(Data.DATA_INTENT_ORDER_NO, orderNo);
                intent.putExtra(Data.DATA_INTENT_ORDER_STATUS, orderStatus);
                startActivityForResult(intent, Data.DATA_INTENT_CHOICE_WORKER_REQUEST);
            }
        });

        mTextViewNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderId = 895;
                OrderTrackDialogFragment orderTrackDialogFragment = OrderTrackDialogFragment.getInstance();
                Bundle bundle = new Bundle();
                bundle.putInt(Data.DATA_INTENT_ORDER_ID, orderId);
                orderTrackDialogFragment.setArguments(bundle);
                if (orderTrackDialogFragment.isAdded()) {
                    return;
                }
                orderTrackDialogFragment.show(getFragmentManager(), "OrderTrackDialogFragment");
            }
        });

        mNetworkManager.setOnPendDetailsListener(new OnPendDetailsListener() {
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
                PendDetailsBean pendDetailsBean = gson.fromJson(result, PendDetailsBean.class);
                if (!pendDetailsBean.isSuccess()) {
                    mStringMessage = pendDetailsBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }
                PendDetailsBean.ObjBean objBean = pendDetailsBean.getObj();
                PendDetailsBean.ObjBean.OrderNodeBean nodeBean = objBean.getOrderNode();
                PendDetailsBean.ObjBean.EngineerBean engineerBean = objBean.getEngineer();

                mRemarks = objBean.getRemark();
                mDoorTime = objBean.getDoorTime();
                mAddress = objBean.getAddress();
                mContact = objBean.getContactName();
                mContactPhone = objBean.getContactPhone();

                for (PendDetailsBean.ObjBean.OrderCarListBean carListBean : objBean.getOrderCarList()) {
                    String carVin = carListBean.getCarVin();
                    String carBrand = carListBean.getCarBrand();
                    if (null == carVin) {
                        carVin = "";
                    }
                    if (null == carBrand) {
                        carBrand = "";
                    }
                    if (1 == carListBean.getRemoveFlag()) {
                        mRemoveContent += carVin + "，" + carBrand + "\n";
                    } else {
                        mInfoContent += carVin + "，" + carBrand + "\n";
                    }
                }

                switch (objBean.getOrderType()) {
                    case 1: {
                        mInstallType = "安装";
                        mInfoTitle = "安装车辆信息";
                        mInstallContent += "有线" + objBean.getWiredNum() + "个，无线" + objBean.getWirelessNum() + "个";
                        break;
                    }
                    case 2: {
                        mInstallType = "维修";
                        mInfoTitle = "维修车辆信息";
                        mInstallContent += "有线" + objBean.getWiredNum() + "个，无线" + objBean.getWirelessNum() + "个";
                        break;
                    }
                    case 3: {
                        mInstallType = "拆改";
                        mInfoTitle = "安装车辆信息";
                        mInstallContent += "拆除：有线" + objBean.getRemoveWiredNum() + "个，无线" + objBean.getRemoveWirelessNum() + "个\n";
                        mInstallContent += "安装：有线" + objBean.getWiredNum() + "个，无线" + objBean.getWirelessNum() + "个";
                        break;
                    }
                    default: {
                        mInfoTitle = "车辆信息";
                        Log.i(TAG, "onSuccess: default-->" + objBean.getOrderType());
                    }
                }

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });
    }

    //  显示信息Dialog
    private void showMessageDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PendDetailsActivity.this);
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
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    mTextViewContact.setText(mContact);
                    mTextViewContactPhone.setText(mContactPhone);
                    mTextViewDoorTime.setText(new TimeFormatU().millisToDate(mDoorTime));
                    mTextViewAddress.setText(mAddress);
                    mTextViewRemarks.setText(mRemarks);
                    mTextViewInstallType.setText(mInstallType);
                    mTextViewInstallContent.setText(mInstallContent);
                    mTextViewInfoTitle.setText(mInfoTitle);
                    mTextViewInfoContent.setText(mInfoContent);
                    if (mInstallType.equals("拆改")) {
                        mRelativeLayoutRemove.setVisibility(View.VISIBLE);
                        mTextViewRemoveContent.setText(mRemoveContent);
                    }

                    mPendDetailsAdapter.notifyDataSetChanged();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default-->" + msg.what);
                }
            }
        }
    }
}
