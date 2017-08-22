package com.tianyigps.dispatch2.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.cycleprogressview.CycleProgressView;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.adapter.PendDetailsAdapter;
import com.tianyigps.dispatch2.bean.ModifyDateBean;
import com.tianyigps.dispatch2.bean.PendDetailsBean;
import com.tianyigps.dispatch2.customview.MyListView;
import com.tianyigps.dispatch2.data.AdapterPendDetailsData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragment;
import com.tianyigps.dispatch2.dialog.OrderTrackDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnModifyDateListener;
import com.tianyigps.dispatch2.interfaces.OnPendDetailsListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.BitmapU;
import com.tianyigps.dispatch2.utils.NodeU;
import com.tianyigps.dispatch2.utils.TimeFormatU;
import com.tianyigps.dispatch2.utils.ToastU;
import com.yundian.bottomdialog.BottomDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PendDetailsActivity extends Activity {

    private static final String TAG = "PendDetailsActivity";

    private static final int ORDER_STATUS_1 = 1;

    //Title栏
    private TextView mTextViewTitle;
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;

    private ImageView mImageViewTitle;
    private Bitmap mBitmap;

    private TextView mTextViewNode;
    private TextView mTextViewTime;
    private View mViewRight, mViewTop;
    private CycleProgressView mCycleProgressView;
    private FrameLayout mFrameLayoutCycle;

    private TextView mTextViewContact, mTextViewContactPhone, mTextViewDoorTime, mTextViewAddress, mTextViewModify, mTextViewRemarks, mTextViewInstallType, mTextViewInstallContent, mTextViewInfoTitle, mTextViewInfoContent, mTextViewRemoveContent;

    private RelativeLayout mRelativeLayoutRemove;

    private MyListView mListView;
    private List<AdapterPendDetailsData> mAdapterPendDetailsDataList;
    private PendDetailsAdapter mPendDetailsAdapter;

    private Button mButtonPend;

    private String mContact, mContactPhone, mAddress, mRemarks, mInstallType, mInstallContent = "", mInfoTitle, mInfoContent = "", mRemoveContent = "";
    private long mDoorTime;
    private int mOrderStatusGet;
    private int mNode;
    private boolean isModify;

    private SharedpreferenceManager mSharedpreferenceManager;
    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private String jobNo;
    private String token;
    private String userName;
    private String orderNo;
    private int orderStatus;
    private int mOrderId;

    private Intent mIntent;

    private String mStringMessage;

    //  改约
    private String mDays[];
    private String mHours[];
    private String mMins[];
    private int mDay, mHour, mMin;
    private String mReason;

    //  LoadingFragment
    private LoadingDialogFragment mLoadingDialogFragment;

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
    protected void onStop() {
        super.onStop();
        if (null != mBitmap) {
            mBitmap.recycle();
        }
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

        mImageViewTitle = findViewById(R.id.iv_activity_pend_details);
        mBitmap = BitmapU.getBitmap(this, R.drawable.bg_order_details_top, 320, 160);
        mImageViewTitle.setImageBitmap(mBitmap);

        mTextViewTitle.setVisibility(View.GONE);
        mImageViewTitleRight.setVisibility(View.GONE);
        mImageViewTitleLeft.setImageResource(R.drawable.ic_back);

        mDays = getResources().getStringArray(R.array.picker_day);
        mHours = getResources().getStringArray(R.array.picker_hour);
        mMins = getResources().getStringArray(R.array.picker_min);

        mLoadingDialogFragment = new LoadingDialogFragment();
        mSharedpreferenceManager = new SharedpreferenceManager(this);
        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();

        mIntent = getIntent();
        orderNo = mIntent.getStringExtra(Data.DATA_INTENT_ORDER_NO);
        orderStatus = mIntent.getIntExtra(Data.DATA_INTENT_ORDER_STATUS, 0);

        jobNo = mSharedpreferenceManager.getJobNo();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();

        mViewRight = findViewById(R.id.view_layout_pend_details_content_node);
        mViewTop = findViewById(R.id.view_layout_pend_details_content_node_top);
        mFrameLayoutCycle = findViewById(R.id.fl_layout_cycle_time);
        mCycleProgressView = findViewById(R.id.cpv_layout_cycle_time);
        mCycleProgressView.setStrokWidth(10);
        mCycleProgressView.setDefaultColor(getResources().getColor(R.color.colorCycleGray));

        mTextViewNode = findViewById(R.id.tv_layout_pend_details_content_node);
        mTextViewTime = findViewById(R.id.tv_layout_cycle_time);
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

        mPendDetailsAdapter = new PendDetailsAdapter(PendDetailsActivity.this, mAdapterPendDetailsDataList);

        mListView.setAdapter(mPendDetailsAdapter);

        mRelativeLayoutRemove = findViewById(R.id.rl_layout_pend_details_content_remove);

        showLoading();
        if (0 == orderStatus) {
            mNetworkManager.getPendDetails(jobNo, token, orderNo, userName);
        } else {
            mNetworkManager.getPendDetails(jobNo, token, orderNo, userName, orderStatus);
        }
    }

    private void setEventListener() {

        mImageViewTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PendDetailsActivity.this.finish();
            }
        });

        mTextViewModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showModifyDateDialog();
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
                OrderTrackDialogFragment orderTrackDialogFragment = OrderTrackDialogFragment.getInstance();
                Bundle bundle = new Bundle();
                bundle.putInt(Data.DATA_INTENT_ORDER_ID, mOrderId);
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

                mOrderId = nodeBean.getOrderId();

                mRemarks = objBean.getRemark();
                mDoorTime = objBean.getDoorTime();
                mAddress = objBean.getAddress();
                mContact = objBean.getContactName();
                mContactPhone = objBean.getContactPhone();
                mOrderStatusGet = objBean.getOrderStatus();
                mNode = nodeBean.getNode();

                if (Data.NODE_3 == mNode) {
                    if (Data.STATUS_98 == mOrderStatusGet) {
                        mAdapterPendDetailsDataList.add(new AdapterPendDetailsData(R.drawable.ic_modify_date_uppass
                                , "改约不通过"
                                , nodeBean.getCheckFalseReason()
                                , true));

                        String reason = nodeBean.getReasonChoosed() + "，" + nodeBean.getReasonFilled();
                        mAdapterPendDetailsDataList.add(new AdapterPendDetailsData(R.drawable.ic_modify_date_reason
                                , "" + nodeBean.getReviseTime()
                                , reason));
                    } else {
                        mAdapterPendDetailsDataList.add(new AdapterPendDetailsData(R.drawable.ic_modify_date_pass
                                , "改约通过"
                                , "" + nodeBean.getReviseTime()));
                    }
                }
                if (Data.NODE_8 == mNode || Data.NODE_9 == mNode || Data.NODE_10 == mNode) {
                    String engineer = engineerBean.getName();
                    String phone = engineerBean.getPhoneNo();
                    mAdapterPendDetailsDataList.add(new AdapterPendDetailsData(R.drawable.ic_modify_date_engineer, engineer, phone));
                }
                if (Data.NODE_5 == mNode) {
                    String reason = nodeBean.getReasonChoosed() + "，" + nodeBean.getReasonFilled();
                    mAdapterPendDetailsDataList.add(new AdapterPendDetailsData(R.drawable.ic_modify_date_reason, "取消原因", reason));
                }
                if (Data.NODE_13 == mNode) {
                    String reason = nodeBean.getReasonChoosed() + "，" + nodeBean.getReasonFilled();
                    mAdapterPendDetailsDataList.add(new AdapterPendDetailsData(R.drawable.ic_modify_date_return, "退回客户", reason));
                }

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

        mNetworkManager.setOnModifyDateListener(new OnModifyDateListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                ModifyDateBean modifyDateBean = gson.fromJson(result, ModifyDateBean.class);
                mStringMessage = modifyDateBean.getMsg();
                if (!modifyDateBean.isSuccess()) {
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }
                myHandler.sendEmptyMessage(Data.MSG_2);
            }
        });
    }

    //  改约
    private void modifyDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        calendar.get(Calendar.DAY_OF_YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        day = day + mDay;
        calendar.set(year, month, day, mHour, mMin);
        long modify = calendar.getTimeInMillis();

        String newTime = new TimeFormatU().millisToDate(modify);

        showLoading();
        mNetworkManager.modifyDate(jobNo, userName, token, orderNo, orderStatus, newTime, mReason);
    }

    //  选择改约时间
    private void showModifyDateDialog() {
        final BottomDialog bottomDialog = new BottomDialog();
        View viewDate = LayoutInflater.from(this).inflate(R.layout.dialog_modify_date, null);
        bottomDialog.setContentView(viewDate);

        final NumberPicker mpDay = viewDate.findViewById(R.id.np_dialog_modify_day);
        final NumberPicker mpHour = viewDate.findViewById(R.id.np_dialog_modify_hour);
        final NumberPicker mpMin = viewDate.findViewById(R.id.np_dialog_modify_min);

        TextView tvCancel = viewDate.findViewById(R.id.tv_dialog_modify_cancel);
        TextView tvEnsure = viewDate.findViewById(R.id.tv_dialog_modify_ensure);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
            }
        });

        tvEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
                mDay = mpDay.getValue();
                mHour = mpHour.getValue();
                mMin = mpMin.getValue();
                Log.i(TAG, "onClick: day-->" + mDay);
                Log.i(TAG, "onClick: hour-->" + mHour);
                Log.i(TAG, "onClick: min-->" + mMin);

                showModifyReason();
            }
        });

        mpDay.setDisplayedValues(mDays);
        mpDay.setMinValue(0);
        mpDay.setMaxValue(mDays.length - 1);

        mpHour.setDisplayedValues(mHours);
        mpHour.setMinValue(0);
        mpHour.setMaxValue(mHours.length - 1);

        mpMin.setDisplayedValues(mMins);
        mpMin.setMinValue(0);
        mpMin.setMaxValue(mMins.length - 1);

        bottomDialog.show(getFragmentManager(), "modify date");
    }

    //  填写改约原因
    private void showModifyReason() {
        final BottomDialog bottomDialog = new BottomDialog();
        View viewReason = LayoutInflater.from(this).inflate(R.layout.dialog_modify_reason, null);
        bottomDialog.setContentView(viewReason);

        TextView tvTitle = viewReason.findViewById(R.id.tv_dialog_modify_title);
        TextView tvCancel = viewReason.findViewById(R.id.tv_dialog_modify_reason_cancel);
        Button btnSubmit = viewReason.findViewById(R.id.btn_dialog_modify_reason_submit);
        final EditText etReason = viewReason.findViewById(R.id.et_dialog_modify_reason);
        final TextView tvInfo = viewReason.findViewById(R.id.tv_dialog_modify_reason_info);

        String title = mDays[mDay] + " " + mHour + ":" + mMins[mMin];
        tvTitle.setText(title);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReason = etReason.getText().toString();
                if ("".equals(mReason)) {
                    tvInfo.setText("原因不能为空");
                    return;
                }
                tvInfo.setText(null);
                // 2017/8/15 提交
                modifyDate();
                bottomDialog.dismiss();
            }
        });

        bottomDialog.show(getFragmentManager(), "modify reason");
    }

    //  显示信息Dialog
    private void showMessageDialog(String msg) {
        if (isFinishing()) {
            return;
        }
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

    //  显示Toast
    private void showMessageToast(String msg) {
        new ToastU(this).showToast(msg);
    }

    //  显示LoadingFragment
    private void showLoading() {
        mLoadingDialogFragment.show(getFragmentManager(), "LoadingFragment");
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mLoadingDialogFragment.isAdded()) {
                mLoadingDialogFragment.dismiss();
            }
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
                    } else {
                        mRelativeLayoutRemove.setVisibility(View.GONE);
                    }

                    if (Data.STATUS_1 != mOrderStatusGet) {
                        mFrameLayoutCycle.setVisibility(View.GONE);
                        mViewRight.setVisibility(View.GONE);
                        mViewTop.setVisibility(View.GONE);
                    } else {
                        mFrameLayoutCycle.setVisibility(View.VISIBLE);
                        mViewRight.setVisibility(View.VISIBLE);
                        mViewTop.setVisibility(View.VISIBLE);
                    }

                    mTextViewNode.setText(NodeU.getNode(mNode));

                    if (Data.NODE_14 == mNode
                            || Data.NODE_13 == mNode
                            || Data.NODE_10 == mNode
                            || Data.NODE_9 == mNode
                            || Data.NODE_8 == mNode
                            || Data.NODE_5 == mNode) {
                        mTextViewModify.setVisibility(View.GONE);
                    } else {
                        mTextViewModify.setVisibility(View.VISIBLE);
                    }

                    if (Data.NODE_1 == mNode
                            || Data.NODE_6 == mNode
                            || Data.NODE_11 == mNode) {
                        mButtonPend.setVisibility(View.VISIBLE);
                    } else {
                        mButtonPend.setVisibility(View.GONE);
                    }

                    mPendDetailsAdapter.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_2: {
                    //  改约成功
                    showMessageToast(mStringMessage);
                    //  刷新页面
                    showLoading();
                    if (0 == orderStatus) {
                        mNetworkManager.getPendDetails(jobNo, token, orderNo, userName);
                    } else {
                        mNetworkManager.getPendDetails(jobNo, token, orderNo, userName, orderStatus);
                    }
                }
                default: {
                    Log.i(TAG, "handleMessage: default-->" + msg.what);
                }
            }
        }
    }
}
