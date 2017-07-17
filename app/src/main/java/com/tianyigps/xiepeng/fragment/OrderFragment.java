package com.tianyigps.xiepeng.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.activity.LocateActivity;
import com.tianyigps.xiepeng.adapter.OrderAdapter;
import com.tianyigps.xiepeng.bean.WorkerOrderBean;
import com.tianyigps.xiepeng.data.AdapterOrderData;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.utils.TimeFormatU;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.tianyigps.xiepeng.data.Data.MSG_1;
import static com.tianyigps.xiepeng.data.Data.MSG_ERO;

/**
 * Created by djc on 2017/7/11.
 */

public class OrderFragment extends Fragment {

    private static final String TAG = "OrderFragment";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mImageViewSearch;
    private EditText mEditTextSearch;
    private ListView mListView;

    //  标题栏
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;
    private TextView mTextViewTitle;

    private List<AdapterOrderData> mAdapterOrderDataList;
    private OrderAdapter mOrderAdapter;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;

    private String mStringContactPhone, mStringDetail, mStringCity
            , mStringOrderNum, mStringContactName, mStringProvince
            , mStringCustName, mStringDistrict;
    private int mIntOrderType, mIntWirelessNum, mIntRemoveWireNum
            , mIntWireNum, mIntOrderStaus, mIntRemoveWirelessNum
            , mIntReviseFlag, mIntOrderId;
    private long mLongDoorTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_worker_order, container, false);

        init(viewRoot);

        setEventListener();

        return viewRoot;
    }

    private void init(View view) {

        mImageViewTitleLeft = view.findViewById(R.id.iv_layout_title_base_left);
        mImageViewTitleRight = view.findViewById(R.id.iv_layout_title_base_right);
        mTextViewTitle = view.findViewById(R.id.tv_layout_title_base_middle);
        initTitle();

        mSwipeRefreshLayout = view.findViewById(R.id.srl_fragment_worker_order);
        mImageViewSearch = view.findViewById(R.id.iv_layout_search);
        mEditTextSearch = view.findViewById(R.id.et_layout_search);
        mListView = view.findViewById(R.id.lv_fragment_worker_order);

        mEditTextSearch.clearFocus();

        mSwipeRefreshLayout.setColorSchemeColors(0xff3cabfa);

        mAdapterOrderDataList = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            mAdapterOrderDataList.add(new AdapterOrderData("万惠南宁"
//                    , "2017-01-02 17:30"
//                    , "上海市浦东区东方路985号一百杉杉大厦"
//                    , "TY2017010215542001"
//                    , "南柱赫"
//                    , "1234567890"
//                    , "orderType"
//                    , i, 2));
//        }

        mOrderAdapter = new OrderAdapter(getContext(), mAdapterOrderDataList);

        mListView.setAdapter(mOrderAdapter);

        mNetworkManager = NetworkManager.getInstance();

        myHandler = new MyHandler();
    }

    private void initTitle() {
        mTextViewTitle.setText(R.string.order_task);
        mImageViewTitleLeft.setImageResource(R.drawable.ic_switch_account);
        mImageViewTitleRight.setImageResource(R.drawable.ic_locate);
    }

    private void setEventListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNetworkManager.reStart();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 2000);
            }
        });

        mSwipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
//                if (mListView.getFirstVisiblePosition() > 0 || )
//                return false;
                return mListView.getChildCount() > 0 &&
                        (mListView.getFirstVisiblePosition() > 0
                                || mListView.getChildAt(0).getTop() < mListView.getPaddingTop());
            }
        });

        mImageViewTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LocateActivity.class);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout.setRefreshing(true);
        mNetworkManager.getWorkerOrder("205", "25d55ad283aa400af464c76d713c07ad", "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(MSG_ERO);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: ");
                mAdapterOrderDataList.clear();
                Gson gson = new Gson();
                WorkerOrderBean workerOrderBean = gson.fromJson(response.body().string(), WorkerOrderBean.class);
                if (workerOrderBean.isSuccess()) {
                    for (WorkerOrderBean.ObjBean objBean : workerOrderBean.getObj()) {
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

                        mLongDoorTime = objBean.getDoorTime();
                        mAdapterOrderDataList.add(new AdapterOrderData(mStringCustName
                                , new TimeFormatU().millisToDate(mLongDoorTime)
                                , mStringProvince + mStringCity + mStringDistrict
                                , mStringOrderNum
                                , mStringContactName
                                , mStringContactPhone, "", mIntWirelessNum, mIntWireNum));
                    }

                    myHandler.sendEmptyMessage(MSG_1);
                }
            }
        });
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSwipeRefreshLayout.setRefreshing(false);
            switch (msg.what){
                case MSG_ERO:{
                    break;
                }
                case MSG_1:{
                    mOrderAdapter.notifyDataSetChanged();
                    break;
                }
                default:{
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
