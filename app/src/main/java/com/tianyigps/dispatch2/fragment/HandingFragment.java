package com.tianyigps.dispatch2.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.activity.InstallingActivity;
import com.tianyigps.dispatch2.activity.ManagerFragmentContentActivity;
import com.tianyigps.dispatch2.activity.OrderDetailsActivity;
import com.tianyigps.dispatch2.adapter.ChoicePhoneAdapter;
import com.tianyigps.dispatch2.adapter.HandingAdapter;
import com.tianyigps.dispatch2.bean.StartHandingBean;
import com.tianyigps.dispatch2.bean.WorkerHandingBean;
import com.tianyigps.dispatch2.data.AdapterHandingData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragmentV4;
import com.tianyigps.dispatch2.interfaces.OnContactSiteListener;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderHandingListener;
import com.tianyigps.dispatch2.interfaces.OnStartHandingListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.yundian.bottomdialog.v4.BottomDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private String mStringMessage;

    private SharedpreferenceManager mSharedpreferenceManager;
    private int eid;
    private String token;
    private String userName;
    private String eName;

    private List<String> mPhoneList;

    //  正在操作的item
    private int mPosition = 0;

    //  LoadingFragment
    private LoadingDialogFragmentV4 mLoadingDialogFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_handing, container, false);

        init(viewRoot);

        setEventListener();

        return viewRoot;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mSwipeRefreshLayout.setRefreshing(true);
            mNetworkManager.getWorkerOrderHanding(eid, token, userName);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fragment_handing_head: {
                showChoicePhoneDialog();
//                String phone = mSharedpreferenceManager.getHeadPhone();
//                toCall(phone);
                break;
            }
            case R.id.tv_fragment_handing_manager: {
                String phone = mSharedpreferenceManager.getManagerPhone();
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
        mLoadingDialogFragment = new LoadingDialogFragmentV4();

        mTextViewHead = view.findViewById(R.id.tv_fragment_handing_head);
        mTextViewManager = view.findViewById(R.id.tv_fragment_handing_manager);

        mListViewHanding = view.findViewById(R.id.lv_fragment_handling);

        mAdapterHandingDataList = new ArrayList<>();

        mHandingAdapter = new HandingAdapter(getContext(), mAdapterHandingDataList);
        mListViewHanding.setAdapter(mHandingAdapter);

        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();

        mSharedpreferenceManager = new SharedpreferenceManager(getActivity());
        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();
        eName = mSharedpreferenceManager.getName();

        int launchMode = mSharedpreferenceManager.getLaunchMode();
        if (Data.DATA_LAUNCH_MODE_WORKER == launchMode) {
            mImageViewTitleLeft.setVisibility(View.GONE);
        } else {
            mImageViewTitleLeft.setVisibility(View.VISIBLE);
        }

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

        mTextViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListViewHanding.smoothScrollToPositionFromTop(0, 0);
            }
        });

        mImageViewTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ManagerFragmentContentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                getActivity().finish();
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

        mHandingAdapter.setItemListener(new HandingAdapter.OnItemListener() {
            @Override
            public void onStart(int position) {
                mPosition = position;
                showConfirmDialog();
            }

            @Override
            public void onCall(int position) {
                AdapterHandingData data = mAdapterHandingDataList.get(position);
                //  联系现场
                mNetworkManager.contactSite(eid, token, data.getId(), data.getCallName(), userName);
                toCall(data.getCallNumber());
            }

            @Override
            public void onItem(int position) {
                AdapterHandingData data = mAdapterHandingDataList.get(position);
                if (1 == data.getCheckStatus()) {
                    return;
                }
                Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
                intent.putExtra(Data.DATA_INTENT_ORDER_NO, data.getId());
                intent.putExtra(Data.DATA_INTENT_ORDER_STATUS, data.getOrderStatus());
                startActivity(intent);
            }
        });

        mNetworkManager.setGetWorkerOrderHandingListener(new OnGetWorkerOrderHandingListener() {
            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
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

                    long submitTime = objBean.getSubmitTime();
                    int checkStatus;
                    if (0 == submitTime) {
                        submitTime = System.currentTimeMillis();
                        checkStatus = 0;
                    } else {
                        checkStatus = objBean.getCheckStatus();
                    }

                    mAdapterHandingDataList.add(new AdapterHandingData(objBean.getCustName()
                            , submitTime
                            , objBean.getDoorTime()
                            , objBean.getProvince() + objBean.getCity() + objBean.getDistrict() + objBean.getDetail()
                            , objBean.getOrderNo()
                            , objBean.getContactName()
                            , objBean.getContactPhone()
                            , objBean.getOrderType()
                            , checkStatus
                            , objBean.getOrderStatus()
                            , wire, wireless, removeWire, removeWireless, modify));
                }

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });

        mNetworkManager.setStartHandingListener(new OnStartHandingListener() {
            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                StartHandingBean startHandingBean = gson.fromJson(result, StartHandingBean.class);
                if (!startHandingBean.isSuccess()) {
                    mStringMessage = startHandingBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }
                myHandler.sendEmptyMessage(Data.MSG_2);
            }
        });

        mNetworkManager.setContactSiteListener(new OnContactSiteListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: 联系现场请求失败");
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: 联系现场请求成功");
            }
        });
    }

    //  显示LoadingFragment
    private void showLoading() {
        mLoadingDialogFragment.show(getChildFragmentManager(), "LoadingFragment");
    }

    //  显示选择电话对话框
    private void showChoicePhoneDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_choice_phone, null);
        //  选择电话
        ListView listViewChoicePhone = view.findViewById(R.id.lv_dialog_choice_phone);
        String headPhoneList = mSharedpreferenceManager.getHeadPhoneList();
        String[] headPhones = headPhoneList.split(",");
        mPhoneList = Arrays.asList(headPhones);
        ChoicePhoneAdapter choicePhoneAdapter = new ChoicePhoneAdapter(getContext(), mPhoneList);
        listViewChoicePhone.setAdapter(choicePhoneAdapter);
        listViewChoicePhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toCall(mPhoneList.get(i));
            }
        });

        BottomDialog bottomDialog = new BottomDialog();
        bottomDialog.setContentView(view);
        bottomDialog.show(getChildFragmentManager(), "ChoicePhone");
    }

    //  拨打电话
    private void toCall(String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    //  开始前确认对话框
    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_button_editable, null);
        Button ensure = view.findViewById(R.id.btn_dialog_editable_ensure);
        Button cancle = view.findViewById(R.id.btn_dialog_editable_cancel);
        TextView textView = view.findViewById(R.id.tv_dialog_editable_msg);
        textView.setText("开始安装前，请确认已核对车架号等订单信息!");
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdapterHandingData data = mAdapterHandingDataList.get(mPosition);
                showLoading();
                mNetworkManager.startHanding(eid, token, data.getId(), eName, userName);
                dialog.dismiss();
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //  显示信息Dialog
    private void showMessageDialog(String msg) {
        if (null == getActivity()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
            if (mLoadingDialogFragment.isAdded()) {
                mLoadingDialogFragment.dismissAllowingStateLoss();
            }
            mSwipeRefreshLayout.setRefreshing(false);
            switch (msg.what) {
                case Data.MSG_ERO: {
                    Log.i(TAG, "handleMessage: ERO");
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    //  获取信息
                    mHandingAdapter.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_2: {
                    //  开始安装
                    AdapterHandingData data = mAdapterHandingDataList.get(mPosition);
                    Intent intent = new Intent(getActivity(), InstallingActivity.class);
                    intent.putExtra(Data.DATA_INTENT_ORDER_NO, data.getId());
                    intent.putExtra(Data.DATA_INTENT_INSTALL_TYPE, (data.getOrderType() - 1));
                    startActivity(intent);
                    break;
                }
                case Data.MSG_3: {
                    //  联系现场
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
