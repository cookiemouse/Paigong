package com.tianyigps.xiepeng.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.adapter.ChoiceWorkerAdapter;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.data.AdapterChoiceWorkerData;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class ChoiceWorkerActivity extends BaseActivity {

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
    private String jobNo;
    private String token;

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

        mSharedpreferenceManager = new SharedpreferenceManager(this);
        mNetworkManager = new NetworkManager();

        jobNo = mSharedpreferenceManager.getJobNo();
        token = mSharedpreferenceManager.getToken();

        mListView = findViewById(R.id.lv_activity_choice_worker);

        mAdapterChoiceWorkerDataList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            mAdapterChoiceWorkerDataList.add(new AdapterChoiceWorkerData("100002", "张师傅", "宝山区/嘉定区"));
        }

        mChoiceWorkerAdapter = new ChoiceWorkerAdapter(this, mAdapterChoiceWorkerDataList);
        mListView.setAdapter(mChoiceWorkerAdapter);

    }

    private void setEventListener() {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (AdapterChoiceWorkerData data : mAdapterChoiceWorkerDataList) {
                    data.setSelect(false);
                }
                mAdapterChoiceWorkerDataList.get(i).setSelect(true);
                mChoiceWorkerAdapter.notifyDataSetChanged();
            }
        });
    }
}
