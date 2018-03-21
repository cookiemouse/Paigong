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
import com.tianyigps.dispatch2.bean.LastInstallerBean;
import com.tianyigps.dispatch2.bean.RemoveTerminalBean;
import com.tianyigps.dispatch2.bean.StartOrderInfoBean;
import com.tianyigps.dispatch2.data.AdapterOperateRemoveData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnGetLastInstallerListener;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderInfoStartListener;
import com.tianyigps.dispatch2.interfaces.OnRemoveTerminalListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;

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

    private String mBaseUrl;

    private int mCarId;

    private MyHandler myHandler;
    private int positionNow;
    private AlertDialog dialogRemove;

    private String mStringMessage = "";

    //  LoadingFragment
    private LoadingDialogFragment mLoadingDialogFragment;

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

        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();
        mBaseUrl = mSharedpreferenceManager.getImageBaseUrl();

        Intent intent = getIntent();
        orderNo = intent.getStringExtra(Data.DATA_INTENT_ORDER_NO);
        mCarId = intent.getIntExtra(Data.DATA_INTENT_CAR_ID, 0);

        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();
        mLoadingDialogFragment = new LoadingDialogFragment();

        showLoading();
        mNetworkManager.getWorkerOrderInfoStart(eid, token, orderNo, userName);

        mOperateRemoveAdapter = new OperateRemoveAdapter(OperateRemoveActivity.this, mAdapterOperateRemoveDataList);

        mListViewRemove.setAdapter(mOperateRemoveAdapter);
    }

    private void setEventListener() {
        mNetworkManager.setGetWorkerOrderInfoStartListener(new OnGetWorkerOrderInfoStartListener() {
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
                StartOrderInfoBean startOrderInfoBean = gson.fromJson(result, StartOrderInfoBean.class);

                if (!startOrderInfoBean.isSuccess()) {
                    mStringMessage = startOrderInfoBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                StartOrderInfoBean.ObjBean objBean = startOrderInfoBean.getObj();

                for (StartOrderInfoBean.ObjBean.CarListBean carListBean : objBean.getCarList()) {
                    if (carListBean.getRemoveFlag() == 0) {
                        continue;
                    }
                    String frameNo = carListBean.getCarVin();
                    String carNo = carListBean.getCarNo();
                    for (StartOrderInfoBean.ObjBean.CarListBean.CarTerminalListBean carTerminalListBean
                            : carListBean.getCarTerminalList()) {

                        String terminalName = carTerminalListBean.getTerminalName();
                        if (null == terminalName) {
                            terminalName = "";
                        } else {
                            terminalName = "（" + terminalName + "）";
                        }
                        String location = carTerminalListBean.getInstallLocation();
                        if (null == location) {
                            location = "";
                        }

                        mAdapterOperateRemoveDataList.add(new AdapterOperateRemoveData(carNo
                                , frameNo
                                , carTerminalListBean.getTerminalType()
                                , terminalName
                                , carTerminalListBean.getTNo()
                                , location
                                , mBaseUrl + carTerminalListBean.getInstallLocationPic()
                                , mBaseUrl + carTerminalListBean.getWiringDiagramPic()
                                , ""
                                , ""
                                , ""
                                , carTerminalListBean.getRemoveStatus()));

                        mNetworkManager.getLastInstaller(eid, token, carTerminalListBean.getTNo(), userName);
                    }
                }

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });

        mNetworkManager.setRemoveTerminalListener(new OnRemoveTerminalListener() {
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
                RemoveTerminalBean removeTerminalBean = gson.fromJson(result, RemoveTerminalBean.class);
                if (!removeTerminalBean.isSuccess()) {
                    mStringMessage = removeTerminalBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                myHandler.sendEmptyMessage(Data.MSG_2);
            }
        });

        mOperateRemoveAdapter.setItemListener(new OperateRemoveAdapter.OnItemListener() {
            @Override
            public void onRemove(int position) {
                positionNow = position;
                showRemoveDialog();
            }
        });

        mNetworkManager.setOnGetLastInstallerListener(new OnGetLastInstallerListener() {
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
                LastInstallerBean lastInstallerBean = gson.fromJson(result, LastInstallerBean.class);
                if (!lastInstallerBean.isSuccess()) {
                    mStringMessage = lastInstallerBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }
                LastInstallerBean.ObjBean objBean = lastInstallerBean.getObj();
                if (null == objBean) {
                    return;
                }
                for (AdapterOperateRemoveData data : mAdapterOperateRemoveDataList) {
                    if (data.gettNo().equals(objBean.getImei())) {
                        data.setInstallName(objBean.getName());
                        data.setInstallPhone(objBean.getPhoneNo());
                        data.setDate(objBean.getEndDate());
                    }
                }

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });
    }

    private void showRemoveDialog() {
        if (isFinishing()) {
            return;
        }
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
                showLoading();
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
        if (isFinishing()) {
            return;
        }
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

    //  显示LoadingFragment
    private void showLoading() {
        mLoadingDialogFragment.show(getFragmentManager(), "LoadingFragment");
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mLoadingDialogFragment.isAdded() && !mLoadingDialogFragment.isVisible()) {
                mLoadingDialogFragment.dismissAllowingStateLoss();
            }

            switch (msg.what) {
                case Data.MSG_ERO: {
                    showMessageDialog(mStringMessage);
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
