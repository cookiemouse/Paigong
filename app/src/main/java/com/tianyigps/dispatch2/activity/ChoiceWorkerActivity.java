package com.tianyigps.dispatch2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.adapter.ChoiceWorkerAdapter;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.bean.ChoiceWorkerBean;
import com.tianyigps.dispatch2.bean.PendBean;
import com.tianyigps.dispatch2.data.AdapterChoiceWorkerData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnPendListener;
import com.tianyigps.dispatch2.interfaces.OnWorkersListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.ToastU;

import java.util.ArrayList;
import java.util.List;

public class ChoiceWorkerActivity extends BaseActivity {

    private static final String TAG = "ChoiceWorkerActivity";

    private TextView mTextViewId, mTextViewName, mTextViewArea;
    private EditText mEditTextSearch;
    private ImageView mImageViewSearch;
    private CheckBox mCheckBoxPay;
    private Button mButtonSelf, mButtonPend;

    private ListView mListView;
    private ChoiceWorkerAdapter mChoiceWorkerAdapter;
    private List<AdapterChoiceWorkerData> mAdapterChoiceWorkerDataList;

    private SharedpreferenceManager mSharedpreferenceManager;
    private NetworkManager mNetworkManager;
    private int eid, eidChoice;
    private String jobNo, jobNoChoice;
    private String token;
    private String userName;
    private MyHandler myHandler;

    private String mStringMessage;

    private Intent mIntent;
    private String orderNo;
    private int orderStatus;

    //  LoadingFragment
    private LoadingDialogFragment mLoadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_worker);

        init();

        setEventListener();
    }

    private void init() {
        this.setTitleText("选择工程师");
        this.setTitleRightButtonVisibilite(false);

        mIntent = getIntent();
        orderNo = mIntent.getStringExtra(Data.DATA_INTENT_ORDER_NO);
        orderStatus = mIntent.getIntExtra(Data.DATA_INTENT_ORDER_STATUS, 1);

        mSharedpreferenceManager = new SharedpreferenceManager(this);
        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();
        mLoadingDialogFragment = new LoadingDialogFragment();

        eid = mSharedpreferenceManager.getEid();
        jobNo = mSharedpreferenceManager.getJobNo();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();

        mTextViewId = findViewById(R.id.tv_activity_choice_worker_id);
        mTextViewName = findViewById(R.id.tv_activity_choice_worker_name);
        mTextViewArea = findViewById(R.id.tv_activity_choice_worker_area);
        mEditTextSearch = findViewById(R.id.et_activity_choice_worker_search);
        mImageViewSearch = findViewById(R.id.iv_activity_choice_worker_search);
        mCheckBoxPay = findViewById(R.id.cb_activity_choice_worker);
        mButtonSelf = findViewById(R.id.btn_activity_choice_worker_self);
        mButtonPend = findViewById(R.id.btn_activity_choice_worker_pend);
        mListView = findViewById(R.id.lv_activity_choice_worker);

        mAdapterChoiceWorkerDataList = new ArrayList<>();

        mChoiceWorkerAdapter = new ChoiceWorkerAdapter(this, mAdapterChoiceWorkerDataList);
        mListView.setAdapter(mChoiceWorkerAdapter);

        showLoading();
        mNetworkManager.getWorkers(jobNo, token, "", userName);
    }

    private void setEventListener() {

        mImageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strSearch = mEditTextSearch.getText().toString();
                search(strSearch);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AdapterChoiceWorkerData data = mAdapterChoiceWorkerDataList.get(i);
                if (data.isSelect()) {
                    data.setSelect(false);
                    mChoiceWorkerAdapter.notifyDataSetChanged();
                    mTextViewId.setText(null);
                    mTextViewName.setText(null);
                    mTextViewArea.setText(null);
                    mCheckBoxPay.setVisibility(View.GONE);

                    eidChoice = 0;
                    jobNoChoice = null;
                    return;
                }
                for (AdapterChoiceWorkerData dataF : mAdapterChoiceWorkerDataList) {
                    dataF.setSelect(false);
                }
                mTextViewId.setText(data.getJobNo());
                mTextViewName.setText(data.getName());
                mTextViewArea.setText(data.getArea());
                eidChoice = data.getId();
                jobNoChoice = data.getJobNo();
                if (1 == data.getType()) {
                    mCheckBoxPay.setVisibility(View.GONE);
                } else {
                    mCheckBoxPay.setVisibility(View.VISIBLE);
                }
                data.setSelect(true);
                mChoiceWorkerAdapter.notifyDataSetChanged();
            }
        });

        mButtonSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/8/7 派单给自己
                pendOrder(eid);
            }
        });

        mButtonPend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/8/7 派单
                if ("".equals(jobNoChoice) || null == jobNoChoice) {
                    mStringMessage = "请选择安装工程师！";
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }
                pendOrder(eidChoice);
            }
        });

        mNetworkManager.setOnWorkersListener(new OnWorkersListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                myHandler.sendEmptyMessage(Data.MSG_1);
                Gson gson = new Gson();
                ChoiceWorkerBean choiceWorkerBean = gson.fromJson(result, ChoiceWorkerBean.class);
                if (!choiceWorkerBean.isSuccess()) {
                    mStringMessage = choiceWorkerBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                mAdapterChoiceWorkerDataList.clear();
                for (ChoiceWorkerBean.ObjBean objBean : choiceWorkerBean.getObj()) {
                    mAdapterChoiceWorkerDataList.add(new AdapterChoiceWorkerData(objBean.getId()
                            , objBean.getJobNo()
                            , objBean.getName()
                            , objBean.getChargeArea()
                            , objBean.getType()));
                }

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });

        mNetworkManager.setOnPendListener(new OnPendListener() {
            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                PendBean pendBean = gson.fromJson(result, PendBean.class);
                if (!pendBean.isSuccess()) {
                    mStringMessage = pendBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_3);
                    return;
                }
                myHandler.sendEmptyMessage(Data.MSG_2);
            }
        });
    }

    //  搜索
    private void search(String key) {
        showLoading();
        mNetworkManager.getWorkers(jobNo, token, key, userName);
    }

    //  派工
    private void pendOrder(int eidChoice) {
        int isPay = 0;
        if (mCheckBoxPay.isChecked()) {
            isPay = 1;
        }
        showLoading();
        mNetworkManager.pendOrder(jobNo, userName, token, orderNo, orderStatus, eidChoice, isPay);
    }

    private void showMessageDialog(String msg) {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ChoiceWorkerActivity.this);
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
            if (mLoadingDialogFragment.isAdded()) {
                mLoadingDialogFragment.dismiss();
            }
            switch (msg.what) {
                case Data.MSG_ERO: {
                    showMessageDialog(mStringMessage);
                    Log.i(TAG, "handleMessage: mes_ero");
                }
                case Data.MSG_1: {
                    mChoiceWorkerAdapter.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_2: {
                    //  派单成功
                    new ToastU(ChoiceWorkerActivity.this).showToast("派单成功");
                    mIntent.putExtra(Data.DATA_INTENT_PEND_RESULT, true);
                    setResult(Data.DATA_INTENT_CHOICE_WORKER_RESULT, mIntent);
                    ChoiceWorkerActivity.this.finish();
                    break;
                }
                case Data.MSG_3: {
                    mIntent.putExtra(Data.DATA_INTENT_PEND_RESULT, false);
                    setResult(Data.DATA_INTENT_CHOICE_WORKER_RESULT, mIntent);
                    ChoiceWorkerActivity.this.finish();
                    //  派单失败
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default-->" + msg.what);
                }
            }
        }
    }
}
