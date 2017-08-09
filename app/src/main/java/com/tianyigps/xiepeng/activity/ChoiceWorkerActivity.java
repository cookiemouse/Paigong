package com.tianyigps.xiepeng.activity;

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
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.adapter.ChoiceWorkerAdapter;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.bean.ChoiceWorkerBean;
import com.tianyigps.xiepeng.data.AdapterChoiceWorkerData;
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.interfaces.OnWorkersListener;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;

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
    private List<AdapterChoiceWorkerData> mAdapterChoiceWorkerDataListSearch;

    private SharedpreferenceManager mSharedpreferenceManager;
    private NetworkManager mNetworkManager;
    private int eid, eidChoice;
    private String jobNo, jobNoChoice;
    private String token;
    private String userName;
    private MyHandler myHandler;

    private String mStringMessage;

    private Intent mIntent;

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

        mSharedpreferenceManager = new SharedpreferenceManager(this);
        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();

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
        mAdapterChoiceWorkerDataListSearch = new ArrayList<>();

        mChoiceWorkerAdapter = new ChoiceWorkerAdapter(this, mAdapterChoiceWorkerDataListSearch);
        mListView.setAdapter(mChoiceWorkerAdapter);

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

                    eidChoice = 0;
                    jobNoChoice = null;
                    return;
                }
                for (AdapterChoiceWorkerData dataF : mAdapterChoiceWorkerDataList) {
                    dataF.setSelect(false);
                    mTextViewId.setText(data.getJobNo());
                    mTextViewName.setText(dataF.getName());
                    mTextViewArea.setText(dataF.getArea());

                    eidChoice = data.getId();
                    jobNoChoice = data.getJobNo();
                }
                mAdapterChoiceWorkerDataList.get(i).setSelect(true);
                mChoiceWorkerAdapter.notifyDataSetChanged();
            }
        });

        mButtonSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/8/7 派单给自己
                int isPay = 0;
                if (mCheckBoxPay.isChecked()) {
                    isPay = 1;
                }
                pendResult(eid, jobNo, isPay);
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
                int isPay = 0;
                if (mCheckBoxPay.isChecked()) {
                    isPay = 1;
                }
                pendResult(eidChoice, jobNoChoice, isPay);
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
                            , objBean.getChargeArea()));
                }

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });
    }

    //  搜索
    private void search(String key) {
        mAdapterChoiceWorkerDataListSearch.clear();

        if (null == key || "".equals(key)) {
            mAdapterChoiceWorkerDataListSearch.addAll(mAdapterChoiceWorkerDataList);
            mChoiceWorkerAdapter.notifyDataSetChanged();
            return;
        }

        for (AdapterChoiceWorkerData data : mAdapterChoiceWorkerDataList) {
            String id = "" + data.getId();
            if (id.contains(key)
                    || data.getName().contains(key)
                    || data.getArea().contains(key)) {

                mAdapterChoiceWorkerDataListSearch.add(data);
            }
        }

        mChoiceWorkerAdapter.notifyDataSetChanged();
    }

    //  返回数据
    private void pendResult(int eid, String jobNo, int isPay) {
        mIntent.putExtra(Data.DATA_INTENT_CHOICE_WORKER_EID, eid);
        mIntent.putExtra(Data.DATA_INTENT_CHOICE_WORKER_JOBNO, jobNo);
        mIntent.putExtra(Data.DATA_INTENT_CHOICE_WORKER_ISPAY, isPay);
        setResult(Data.DATA_INTENT_CHOICE_WORKER_RESULT, mIntent);
        finish();
    }

    private void showMessageDialog(String msg) {
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

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_ERO: {
                    showMessageDialog(mStringMessage);
                    Log.i(TAG, "handleMessage: mes_ero");
                }
                case Data.MSG_1: {
                    search(null);
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default-->" + msg.what);
                }
            }
        }
    }
}
