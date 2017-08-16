package com.tianyigps.dispatch2.fragment;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.activity.InstallingActivity;
import com.tianyigps.dispatch2.activity.ManagerFragmentContentActivity;
import com.tianyigps.dispatch2.adapter.HandingAdapter;
import com.tianyigps.dispatch2.bean.WorkerHandingBean;
import com.tianyigps.dispatch2.data.AdapterHandingData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderHandingListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.TimeFormatU;

import java.util.ArrayList;
import java.util.List;

import static com.tianyigps.dispatch2.data.Data.MSG_1;
import static com.tianyigps.dispatch2.data.Data.MSG_ERO;

/**
 * Created by djc on 2017/7/13.
 */

public class HandingFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "HandingFragment";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTextViewHead, mTextViewManager;
    private ListView mListViewHanding;

    //  标题栏
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;
    private TextView mTextViewTitle;

    private List<AdapterHandingData> mAdapterHandingDataList;
    private HandingAdapter mHandingAdapter;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;

    private SharedpreferenceManager mSharedpreferenceManager;
    private int eid;
    private String token;
    private String userName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_handing, container, false);

        init(viewRoot);

        setEventListener();

        return viewRoot;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fragment_handing_head: {
                // TODO: 2017/7/18 总部号码
                toCall("18017325972");
                break;
            }
            case R.id.tv_fragment_handing_manager: {
                String phone = mSharedpreferenceManager.getHeadPhone();
                toCall(phone);
                break;
            }
            default: {
                Log.i(TAG, "onSignClick: default");
            }
        }
    }

    private void init(View view) {
        mImageViewTitleLeft = view.findViewById(R.id.iv_layout_title_base_left);
        mImageViewTitleRight = view.findViewById(R.id.iv_layout_title_base_right);
        mTextViewTitle = view.findViewById(R.id.tv_layout_title_base_middle);
        initTitle();

        mSwipeRefreshLayout = view.findViewById(R.id.srl_fragment_handing);
        mSwipeRefreshLayout.setColorSchemeColors(0xff3cabfa);

        mTextViewHead = view.findViewById(R.id.tv_fragment_handing_head);
        mTextViewManager = view.findViewById(R.id.tv_fragment_handing_manager);

        mListViewHanding = view.findViewById(R.id.lv_fragment_handling);

        mAdapterHandingDataList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            mAdapterHandingDataList.add(new AdapterHandingData("万惠南宁"
//                    , "2017-01-02 17:30"
//                    , "上海市浦东区东方路985号一百杉杉大厦"
//                    , "TY2017010215542001"
//                    , "南柱赫"
//                    , "1234567890"
//                    , 5, 5));
//        }

        mHandingAdapter = new HandingAdapter(getContext(), mAdapterHandingDataList);
        mListViewHanding.setAdapter(mHandingAdapter);

        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();

        mSharedpreferenceManager = new SharedpreferenceManager(getContext());
        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();

        mSwipeRefreshLayout.setRefreshing(true);
        mNetworkManager.getWorkerOrderHanding(eid, token, userName);
    }

    private void initTitle() {
        mTextViewTitle.setText(R.string.handling);
        mImageViewTitleLeft.setImageResource(R.drawable.ic_switch_account);
        mImageViewTitleRight.setVisibility(View.GONE);
    }

    private void setEventListener() {

        mTextViewHead.setOnClickListener(this);
        mTextViewManager.setOnClickListener(this);

        mImageViewTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ManagerFragmentContentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNetworkManager.getWorkerOrderHanding(eid, token, userName);
            }
        });

        mSwipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
//                if (mListView.getFirstVisiblePosition() > 0 || )
//                return false;
                return mListViewHanding.getChildCount() > 0 &&
                        (mListViewHanding.getFirstVisiblePosition() > 0
                                || mListViewHanding.getChildAt(0).getTop() < mListViewHanding.getPaddingTop());
            }
        });

        mHandingAdapter.setStartClickListener(new HandingAdapter.OnStartClickListener() {
            @Override
            public void onClick(int position) {
                AdapterHandingData data = mAdapterHandingDataList.get(position);
                Intent intent = new Intent(getActivity(), InstallingActivity.class);
                intent.putExtra(Data.DATA_INTENT_ORDER_NO, data.getId());
                intent.putExtra(Data.DATA_INTENT_INSTALL_TYPE, (data.getOrderType() - 1));
                startActivity(intent);
            }
        });

        mNetworkManager.setGetWorkerOrderHandingListener(new OnGetWorkerOrderHandingListener() {
            @Override
            public void onFailure() {
                myHandler.sendEmptyMessage(MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                WorkerHandingBean workerHandingBean = gson.fromJson(result, WorkerHandingBean.class);
                if (!workerHandingBean.isSuccess()) {
                    onFailure();
                    return;
                }

                mAdapterHandingDataList.clear();

                for (WorkerHandingBean.ObjBean objBean : workerHandingBean.getObj()) {

                    int wire, wireless, removeWire, removeWireless;
                    wire = objBean.getWiredNum();
                    wireless = objBean.getWirelessNum();
                    removeWire = objBean.getRemoveWiredNum();
                    removeWireless = objBean.getRemoveWirelessNum();

                    boolean modify;
                    switch (objBean.getReviseFlag()) {
                        case 0: {
                            modify = false;
                            break;
                        }
                        case 1: {
                            modify = true;
                            break;
                        }
                        default: {
                            modify = false;
                            Log.i(TAG, "onSuccess: default");
                        }
                    }

                    mAdapterHandingDataList.add(new AdapterHandingData(objBean.getCustName()
                            , new TimeFormatU().millisToDate(objBean.getDoorTime())
                            , objBean.getProvince() + objBean.getCity() + objBean.getDistrict() + objBean.getDetail()
                            , objBean.getOrderNo()
                            , objBean.getContactName()
                            , objBean.getContactPhone()
                            , objBean.getOrderType()
                            , objBean.getCheckStatus()
                            , wire, wireless, removeWire, removeWireless, modify));
                }

                myHandler.sendEmptyMessage(MSG_1);
            }
        });
    }

    //  拨打电话
    private void toCall(String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSwipeRefreshLayout.setRefreshing(false);
            switch (msg.what) {
                case MSG_ERO: {
                    Log.i(TAG, "handleMessage: ERO");
                }
                case MSG_1: {
                    mHandingAdapter.notifyDataSetChanged();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
