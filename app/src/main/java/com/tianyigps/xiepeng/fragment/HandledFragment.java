package com.tianyigps.xiepeng.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.activity.ManagerFragmentContentActivity;
import com.tianyigps.xiepeng.adapter.HandledAdapter;
import com.tianyigps.xiepeng.bean.WorkerHandedBean;
import com.tianyigps.xiepeng.data.AdapterHandledData;
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.interfaces.OnGetWorkerOrderHandedListener;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;
import com.tianyigps.xiepeng.utils.TimeFormatU;

import java.util.ArrayList;
import java.util.List;

import static com.tianyigps.xiepeng.data.Data.MSG_1;
import static com.tianyigps.xiepeng.data.Data.MSG_3;
import static com.tianyigps.xiepeng.data.Data.MSG_ERO;

/**
 * Created by djc on 2017/7/13.
 */

public class HandledFragment extends Fragment {

    private static final String TAG = "HandledFragment";

    private static final int DELAY_GONE = 1000;
    private static final int DELAY_LAST = 2000;
    private static final int DELAY_ERROR = 3000;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mImageViewSearch;
    private EditText mEditTextSearch;
    private ListView mListViewHandled;

    //  加载更多
    private View mViewMore;
    private TextView mTextViewMore;
    private ProgressBar mProgressBarMore;
    private boolean addAble = true, isLast = false;

    //  标题栏
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;
    private TextView mTextViewTitle;

    private List<AdapterHandledData> mAdapterHandledDataList;
    private HandledAdapter mHandledAdapter;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private SharedpreferenceManager mSharedpreferenceManager;
    private int eid;
    private String token;
    private String userName;

    private String mKey = "";

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

        mViewMore = LayoutInflater.from(getContext()).inflate(R.layout.layout_load_more, null);
        mTextViewMore = mViewMore.findViewById(R.id.tv_layout_load_more);
        mProgressBarMore = mViewMore.findViewById(R.id.pb_layout_load_more);

        mEditTextSearch.clearFocus();

        mSwipeRefreshLayout.setColorSchemeColors(0xff3cabfa);

        mAdapterHandledDataList = new ArrayList<>();
        mHandledAdapter = new HandledAdapter(getContext(), mAdapterHandledDataList);

        mListViewHandled.setAdapter(mHandledAdapter);

        mListViewHandled.addFooterView(mViewMore);

        mNetworkManager = NetworkManager.getInstance();
        myHandler = new MyHandler();

        mSharedpreferenceManager = new SharedpreferenceManager(getContext());
        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();

        mSwipeRefreshLayout.setRefreshing(true);
        mNetworkManager.getWorkerOrderHanded(eid, token, "", "", userName);
    }

    private void initTitle() {
        mTextViewTitle.setText(R.string.handled);
        mImageViewTitleLeft.setImageResource(R.drawable.ic_switch_account);
        mImageViewTitleRight.setVisibility(View.GONE);
    }

    private void setEventListener() {

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
                mKey = "";
                mEditTextSearch.setText(null);
                mNetworkManager.getWorkerOrderHanded(eid, token, "", "", userName);
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

        mListViewHandled.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount < 0) {
                    return;
                }
                if (totalItemCount == visibleItemCount) {
                    return;
                }
                if (totalItemCount == (firstVisibleItem + visibleItemCount)) {
                    // TODO: 2017/8/2 开始加载
                    if (addAble && !mSwipeRefreshLayout.isRefreshing()) {
                        addAble = false;
                        if (isLast) {
                            mProgressBarMore.setVisibility(View.GONE);
                            mTextViewMore.setVisibility(View.VISIBLE);
                            mTextViewMore.setText("我是有底线的!");
                            myHandler.sendEmptyMessageDelayed(Data.MSG_2, DELAY_LAST);
                            return;
                        }
                        mProgressBarMore.setVisibility(View.VISIBLE);
                        mTextViewMore.setVisibility(View.VISIBLE);
                        mTextViewMore.setText("正在加载...");
                        // TODO: 17-8-2 加载下面数据
                        mNetworkManager.getWorkerOrderHanded(eid
                                , token
                                , mKey
                                , "" + mAdapterHandledDataList.get(mAdapterHandledDataList.size() - 1).getLastId()
                                , userName);
                    }
                }
            }
        });

        mImageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/13 搜索
                mKey = mEditTextSearch.getText().toString();
                mSwipeRefreshLayout.setRefreshing(true);
                mNetworkManager.getWorkerOrderHanded(eid, token, mKey, "", userName);
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

                Gson gson = new Gson();
                WorkerHandedBean workerHandedBean = gson.fromJson(result, WorkerHandedBean.class);

                if (!workerHandedBean.isSuccess()) {
                    onFailure();
                    return;
                }

                isLast = (workerHandedBean.getObj().size() == 0);
                if (isLast) {
                    myHandler.sendEmptyMessageDelayed(MSG_3, DELAY_LAST);
                    return;
                }

                if (mSwipeRefreshLayout.isRefreshing()) {
                    mAdapterHandledDataList.clear();
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
                            , objBean.getProvince() + objBean.getCity() + objBean.getDistrict() + objBean.getDetail()
                            , objBean.getOrderNo()
                            , orderType, wire, wireless
                            , objBean.getOrderId()));
                }

                myHandler.sendEmptyMessage(MSG_1);
            }
        });
    }

    //  显示信息Dialog
    private void showMessageDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

            mSwipeRefreshLayout.setRefreshing(false);

            switch (msg.what) {
                case MSG_ERO: {
                    myHandler.sendEmptyMessageDelayed(Data.MSG_3, DELAY_ERROR);
                    break;
                }
                case MSG_1: {
                    mHandledAdapter.notifyDataSetChanged();
                    myHandler.sendEmptyMessage(Data.MSG_2);
                    break;
                }
                case Data.MSG_2: {
                    //  加载更多完成
                    if (!isLast) {
                        mTextViewMore.setText("加载完成");
                    }
                    mProgressBarMore.setVisibility(View.GONE);
                    myHandler.sendEmptyMessageDelayed(Data.MSG_3, DELAY_GONE);
                    break;
                }
                case Data.MSG_3: {
                    //  加载完成，隐藏footer，并归零
                    mProgressBarMore.setVisibility(View.GONE);
                    mTextViewMore.setVisibility(View.GONE);

                    myHandler.sendEmptyMessageDelayed(Data.MSG_4, 200);
                    break;
                }
                case Data.MSG_4: {
                    //  保存不刷新两次
                    addAble = true;
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default-->" + msg.what);
                }
            }
        }
    }
}
