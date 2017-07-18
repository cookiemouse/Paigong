package com.tianyigps.xiepeng.fragment;

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
import com.tianyigps.xiepeng.adapter.HandledAdapter;
import com.tianyigps.xiepeng.bean.WorkerHandedBean;
import com.tianyigps.xiepeng.data.AdapterHandledData;
import com.tianyigps.xiepeng.interfaces.OnGetWorkerOrderHandedListener;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.utils.TimeFormatU;

import java.util.ArrayList;
import java.util.List;

import static com.tianyigps.xiepeng.data.Data.EID;
import static com.tianyigps.xiepeng.data.Data.MSG_1;
import static com.tianyigps.xiepeng.data.Data.MSG_ERO;
import static com.tianyigps.xiepeng.data.Data.TOKEN;

/**
 * Created by djc on 2017/7/13.
 */

public class HandledFragment extends Fragment {

    private static final String TAG = "HandledFragment";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mImageViewSearch;
    private EditText mEditTextSearch;
    private ListView mListViewHandled;

    //  标题栏
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;
    private TextView mTextViewTitle;

    private List<AdapterHandledData> mAdapterHandledDataList;
    private HandledAdapter mHandledAdapter;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_handled, container, false);

        init(viewRoot);

        setEventListener();

        return viewRoot;
    }

    private void init(View view) {
        mImageViewTitleLeft = view.findViewById(R.id.iv_layout_title_base_left);
        mImageViewTitleRight = view.findViewById(R.id.iv_layout_title_base_right);
        mTextViewTitle = view.findViewById(R.id.tv_layout_title_base_middle);
        initTitle();

        mSwipeRefreshLayout = view.findViewById(R.id.srl_fragment_worker_handled);
        mImageViewSearch = view.findViewById(R.id.iv_layout_search);
        mEditTextSearch = view.findViewById(R.id.et_layout_search);
        mListViewHandled = view.findViewById(R.id.lv_fragment_worker_handled);

        mEditTextSearch.clearFocus();

        mSwipeRefreshLayout.setColorSchemeColors(0xff3cabfa);

        mAdapterHandledDataList = new ArrayList<>();
        mHandledAdapter = new HandledAdapter(getContext(), mAdapterHandledDataList);

        mListViewHandled.setAdapter(mHandledAdapter);

        mNetworkManager = NetworkManager.getInstance();
        myHandler = new MyHandler();

        mSwipeRefreshLayout.setRefreshing(true);
        mNetworkManager.getWorkerOrderHanded(EID, TOKEN, "", "");
    }

    private void initTitle() {
        mTextViewTitle.setText(R.string.handled);
        mImageViewTitleLeft.setImageResource(R.drawable.ic_switch_account);
        mImageViewTitleRight.setVisibility(View.GONE);
    }

    private void setEventListener() {

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNetworkManager.getWorkerOrderHanded(EID, TOKEN, "", "");
            }
        });

        mSwipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
//                if (mListView.getFirstVisiblePosition() > 0 || )
//                return false;
                return mListViewHandled.getChildCount() > 0 &&
                        (mListViewHandled.getFirstVisiblePosition() > 0
                                || mListViewHandled.getChildAt(0).getTop() < mListViewHandled.getPaddingTop());
            }
        });

        mImageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/13 搜索
            }
        });

        mNetworkManager.setOnGetWorkerOrderHandedListener(new OnGetWorkerOrderHandedListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {

                mAdapterHandledDataList.clear();

                Gson gson = new Gson();
                WorkerHandedBean workerHandedBean = gson.fromJson(result, WorkerHandedBean.class);

                if (!workerHandedBean.isSuccess()) {
                    onFailure();
                    return;
                }
                for (WorkerHandedBean.ObjBean objBean : workerHandedBean.getObj()) {

                    String orderType;
                    int wire, wireless;
                    switch (objBean.getOrderType()) {
                        case 1: {
                            orderType = "安装：";
                            wire = objBean.getFinishWiredNum();
                            wireless = objBean.getFinishWirelessNum();
                            break;
                        }
                        case 2: {
                            orderType = "维修：";
                            wire = objBean.getFinishWiredNum();
                            wireless = objBean.getFinishWirelessNum();
                            break;
                        }
                        case 3: {
                            orderType = "拆改：";
                            wire = objBean.getRemoveFinishWiredNum();
                            wireless = objBean.getRemoveFinishWirelessNum();
                            break;
                        }
                        default: {
                            orderType = "安装：";
                            wire = 0;
                            wireless = 0;
                            Log.i(TAG, "onResponse: default");
                        }
                    }

                    mAdapterHandledDataList.add(new AdapterHandledData(objBean.getCustName()
                            , new TimeFormatU().millisToDate(objBean.getDoorTime())
                            , objBean.getProvince() + objBean.getCity() + objBean.getDistrict()
                            , objBean.getOrderNo(), orderType, wire, wireless));
                }

                myHandler.sendEmptyMessage(MSG_1);
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSwipeRefreshLayout.setRefreshing(false);
            switch (msg.what) {
                case MSG_ERO: {
                    break;
                }
                case MSG_1: {
                    mHandledAdapter.notifyDataSetChanged();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
