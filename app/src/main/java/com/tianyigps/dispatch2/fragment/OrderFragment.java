package com.tianyigps.dispatch2.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.activity.LocateActivity;
import com.tianyigps.dispatch2.activity.ManagerFragmentContentActivity;
import com.tianyigps.dispatch2.activity.OrderDetailsActivity;
import com.tianyigps.dispatch2.activity.WorkerFragmentContentActivity;
import com.tianyigps.dispatch2.adapter.OrderAdapter;
import com.tianyigps.dispatch2.bean.SignWorkerBean;
import com.tianyigps.dispatch2.bean.WorkerOrderBean;
import com.tianyigps.dispatch2.data.AdapterOrderData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.ChoiceMapDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnContactSiteListener;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderListener;
import com.tianyigps.dispatch2.interfaces.OnSignedWorkerListener;
import com.tianyigps.dispatch2.manager.LocateManager;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;

import java.util.ArrayList;
import java.util.List;

import static com.tianyigps.dispatch2.data.Data.DATA_INTENT_ADDRESS;
import static com.tianyigps.dispatch2.data.Data.MSG_1;
import static com.tianyigps.dispatch2.data.Data.MSG_2;
import static com.tianyigps.dispatch2.data.Data.MSG_3;
import static com.tianyigps.dispatch2.data.Data.MSG_4;
import static com.tianyigps.dispatch2.data.Data.MSG_ERO;

/**
 * Created by cookiemouse on 2017/7/11.
 */

public class OrderFragment extends Fragment {

    private static final String TAG = "OrderFragment";

    //  标题栏
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;
    private TextView mTextViewTitle;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mLinearLayoutDefault;
    private ImageView mImageViewSearch, mImageViewDelete;
    private EditText mEditTextSearch;
    private ListView mListView;

    private List<AdapterOrderData> mAdapterOrderDataList;
    private OrderAdapter mOrderAdapter;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;

    private String mStringContactPhone, mStringDetail, mStringCity, mStringOrderNum, mStringContactName, mStringProvince, mStringCustName, mStringDistrict;
    private int mIntOrderType, mIntWirelessNum, mIntRemoveWireNum, mIntWireNum, mIntOrderStaus, mIntRemoveWirelessNum, mIntReviseFlag, mIntOrderId;
    private long mLongDoorTime;

    private ChoiceMapDialogFragment mChoiceMapDialogFragment;

    //  获取定位
    private LocateManager mLocateManager;
    private LatLng mLatLngLocate;
    private String orderNoPosition;

    private String strMessage;

    private SharedpreferenceManager mSharedpreferenceManager;
    private int eid;
    private String token;
    private String userName;
    private String name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_worker_order, container, false);

        init(viewRoot);

        setEventListener();

        return viewRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSwipeRefreshLayout.setRefreshing(true);
        mNetworkManager.getWorkerOrder(eid, token, "", userName);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mSwipeRefreshLayout.setRefreshing(true);
            mNetworkManager.getWorkerOrder(eid, token, "", userName);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Data.DATA_INTENT_ORDER_DETAILS_REQUEST &&
                resultCode == Data.DATA_INTENT_ORDER_DETAILS_RESULT) {
            if (data.getBooleanExtra(Data.DATA_INTENT_ORDER_DETAILS_RESULT_SIGNED, false)) {
                WorkerFragmentContentActivity activity = (WorkerFragmentContentActivity) getActivity();
                activity.showHandingFragment();
            }
        }
    }

    private void init(View view) {

        mImageViewTitleLeft = view.findViewById(R.id.iv_layout_title_base_left);
        mImageViewTitleRight = view.findViewById(R.id.iv_layout_title_base_right);
        mTextViewTitle = view.findViewById(R.id.tv_layout_title_base_middle);
        initTitle();

        mChoiceMapDialogFragment = new ChoiceMapDialogFragment();

        mSwipeRefreshLayout = view.findViewById(R.id.srl_fragment_worker_order);
        mLinearLayoutDefault = view.findViewById(R.id.ll_fragment_order_default);
        mImageViewSearch = view.findViewById(R.id.iv_layout_search);
        mImageViewDelete = view.findViewById(R.id.iv_layout_search_delete);
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

        mNetworkManager = new NetworkManager();

        myHandler = new MyHandler();

        mLocateManager = new LocateManager(getActivity());

        mSharedpreferenceManager = new SharedpreferenceManager(getActivity());

        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();
        name = mSharedpreferenceManager.getName();
        int launchMode = mSharedpreferenceManager.getLaunchMode();
        if (Data.DATA_LAUNCH_MODE_WORKER == launchMode) {
            mImageViewTitleLeft.setVisibility(View.GONE);
        } else {
            mImageViewTitleLeft.setVisibility(View.VISIBLE);
        }
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
                mEditTextSearch.setText(null);
                mImageViewDelete.setVisibility(View.GONE);
                mNetworkManager.getWorkerOrder(eid, token, "", userName);
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

        mImageViewTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LocateActivity.class);
                startActivity(intent);
            }
        });

        mImageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = mEditTextSearch.getText().toString();
                mSwipeRefreshLayout.setRefreshing(true);
                mNetworkManager.getWorkerOrder(eid, token, key, userName);

                if (!"".equals(key)) {
                    mImageViewDelete.setVisibility(View.VISIBLE);
                }
            }
        });

        mImageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditTextSearch.setText(null);
                mSwipeRefreshLayout.setRefreshing(true);
                mNetworkManager.getWorkerOrder(eid, token, "", userName);

                mImageViewDelete.setVisibility(View.GONE);
            }
        });

        mOrderAdapter.setOnItemListener(new OrderAdapter.OnItemListener() {

            @Override
            public void onMapClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString(DATA_INTENT_ADDRESS, mAdapterOrderDataList.get(position).getAddress());
                mChoiceMapDialogFragment.setArguments(bundle);
                mChoiceMapDialogFragment.show(getChildFragmentManager(), "ChoiceMapDialog");
            }

            @Override
            public void onSignClick(int position) {
                orderNoPosition = mAdapterOrderDataList.get(position).getId();

                showAskSignDialog();
            }

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
                intent.putExtra(Data.DATA_INTENT_ORDER_NO, mAdapterOrderDataList.get(position).getId());
                intent.putExtra(Data.DATA_INTENT_ORDER_STATUS, mIntOrderStaus);
                startActivityForResult(intent, Data.DATA_INTENT_ORDER_DETAILS_REQUEST);
            }

            @Override
            public void onCallClick(int position) {
                AdapterOrderData data = mAdapterOrderDataList.get(position);

                //  联系现场
                mNetworkManager.contactSite(eid, token, data.getId(), data.getPhoneName(), userName);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + data.getPhoneNumber()));
                startActivity(intent);
            }
        });

        mLocateManager.setOnReceiveLocationListener(new LocateManager.OnReceiveLocationListener() {
            @Override
            public void onReceive(LatLng latLng) {
                mLatLngLocate = latLng;
                myHandler.sendEmptyMessage(MSG_2);
            }
        });

        mNetworkManager.setGetWorkerOrderListener(new OnGetWorkerOrderListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onResponse: ");

                Gson gson = new Gson();
                WorkerOrderBean workerOrderBean = gson.fromJson(result, WorkerOrderBean.class);
                if (!workerOrderBean.isSuccess()) {
                    onFailure();
                    return;
                }

                mAdapterOrderDataList.clear();

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

                    mAdapterOrderDataList.add(new AdapterOrderData(mStringOrderNum
                            , mStringCustName
                            , mStringContactPhone
                            , mStringContactName
                            , mLongDoorTime
                            , mStringProvince + mStringCity + mStringDistrict + mStringDetail
                            , objBean.getOrderType()
                            , objBean.getWiredNum()
                            , objBean.getWirelessNum()
                            , objBean.getRemoveWiredNum()
                            , objBean.getRemoveWirelessNum()));
                }

                myHandler.sendEmptyMessage(MSG_1);
            }
        });

        mNetworkManager.setSignedWorkerListener(new OnSignedWorkerListener() {
            @Override
            public void onFailure() {
            }

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                SignWorkerBean signWorkerBean = gson.fromJson(result, SignWorkerBean.class);
                strMessage = signWorkerBean.getMsg();
                if (signWorkerBean.isSuccess()) {
                    myHandler.sendEmptyMessage(MSG_3);
                } else {
                    myHandler.sendEmptyMessage(MSG_4);
                }
            }
        });

        mNetworkManager.setContactSiteListener(new OnContactSiteListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: 联系现场失败");
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onFailure: 联系现场成功");
            }
        });
    }

    //  确认签到对话框
    private void showAskSignDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        View viewDialog = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_ask_sign, null);
        builder.setView(viewDialog);

        final Dialog dialog = builder.create();
        Button buttonCancel = viewDialog.findViewById(R.id.btn_dialog_ask_sign_cancel);
        Button buttonEnsure = viewDialog.findViewById(R.id.btn_dialog_ask_sign_ensure);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/20 dismiss
                dialog.dismiss();
            }
        });

        buttonEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/20 签到
                mLocateManager.startLocate();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showMessageDialog(String message) {
        if (getActivity().isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewDialog = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_message_editable, null);
        TextView textViewMessage = viewDialog.findViewById(R.id.tv_dialog_message_message);
        Button buttonKnow = viewDialog.findViewById(R.id.btn_dialog_message_cancel);
        textViewMessage.setText(message);
        builder.setView(viewDialog);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();

        buttonKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private class MyHandler extends Handler {

        //  MSG_1   获取Order数据
        //  MSG_2   获取定位数据
        //  MSG_3   签到回调    成功
        //  MSG_4   签到回调    失败
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSwipeRefreshLayout.setRefreshing(false);
            switch (msg.what) {
                case MSG_ERO: {
                    break;
                }
                case MSG_1: {
                    if (mAdapterOrderDataList.size() == 0) {
                        mLinearLayoutDefault.setVisibility(View.VISIBLE);
                    } else {
                        mLinearLayoutDefault.setVisibility(View.GONE);
                    }
                    mOrderAdapter.notifyDataSetChanged();
                    break;
                }
                case MSG_2: {
                    mNetworkManager.signedWorker(eid, token, name, orderNoPosition
                            , mLatLngLocate.latitude
                            , mLatLngLocate.longitude
                            , Data.LOCATE_TYPE_BAIDU
                            , userName);
                    break;
                }
                case MSG_3: {
                    WorkerFragmentContentActivity activity = (WorkerFragmentContentActivity) getActivity();
                    activity.showHandingFragment();
                    break;
                }
                case MSG_4: {
                    showMessageDialog(strMessage);
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
