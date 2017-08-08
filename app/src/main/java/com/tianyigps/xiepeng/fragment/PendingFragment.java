package com.tianyigps.xiepeng.fragment;

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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.activity.OrderDetailsActivity;
import com.tianyigps.xiepeng.activity.WorkerFragmentContentActivity;
import com.tianyigps.xiepeng.adapter.PendingAdapter;
import com.tianyigps.xiepeng.adapter.PopupAdapter;
import com.tianyigps.xiepeng.bean.PendingBean;
import com.tianyigps.xiepeng.data.AdapterPendingData;
import com.tianyigps.xiepeng.data.AdapterPopupData;
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.dialog.ChoiceMapDialogFragment;
import com.tianyigps.xiepeng.interfaces.OnPendingOrderListener;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;
import com.tianyigps.xiepeng.utils.TimeFormatU;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djc on 2017/7/11.
 */

public class PendingFragment extends Fragment {

    private static final String TAG = "PendingFragment";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mImageViewSearch;
    private EditText mEditTextSearch;
    private ListView mListView;

    //  标题栏
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;
    private TextView mTextViewTitle;

    private List<AdapterPendingData> mAdapterPendingDataList;
    private PendingAdapter mPendingAdapter;

    private SharedpreferenceManager mSharedpreferenceManager;
    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private String jobNo;
    private String token;
    private String userName;
    private String mStringMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View viewRoot = inflater.inflate(R.layout.fragment_pending, container, false);

        init(viewRoot);

        setEventListener();

        return viewRoot;
    }

    private void init(View view) {
        mImageViewTitleLeft = view.findViewById(R.id.iv_layout_title_base_left);
        mImageViewTitleRight = view.findViewById(R.id.iv_layout_title_base_right);
        mTextViewTitle = view.findViewById(R.id.tv_layout_title_base_middle);
        initTitle();

        mSwipeRefreshLayout = view.findViewById(R.id.srl_fragment_pending);
        mImageViewSearch = view.findViewById(R.id.iv_layout_search);
        mEditTextSearch = view.findViewById(R.id.et_layout_search);
        mListView = view.findViewById(R.id.lv_fragment_pending);

        mEditTextSearch.clearFocus();

        mSwipeRefreshLayout.setColorSchemeColors(0xff3cabfa);

        mAdapterPendingDataList = new ArrayList<>();

        mPendingAdapter = new PendingAdapter(getContext(), mAdapterPendingDataList);

        mListView.setAdapter(mPendingAdapter);

        mSharedpreferenceManager = new SharedpreferenceManager(getContext());
        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();

        jobNo = mSharedpreferenceManager.getJobNo();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();

        mSwipeRefreshLayout.setRefreshing(true);
        mNetworkManager.getPenddingOrder(jobNo, token, "", "", userName);
    }

    private void initTitle() {
        mTextViewTitle.setText(R.string.pending);
        mImageViewTitleLeft.setImageResource(R.drawable.ic_switch_account);
        mImageViewTitleRight.setImageResource(R.drawable.ic_popup_window);
    }

    private void setEventListener() {
        mImageViewTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WorkerFragmentContentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        mImageViewTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWindow();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNetworkManager.getPenddingOrder(jobNo, token, "", "", userName);
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

        mPendingAdapter.setOnItemListener(new PendingAdapter.OnItemListener() {
            @Override
            public void onMap(int position) {
                Log.i(TAG, "onMap: position-->" + position);
                ChoiceMapDialogFragment mChoiceMapDialogFragment = new ChoiceMapDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Data.DATA_INTENT_ADDRESS, mAdapterPendingDataList.get(position).getAddress());
                mChoiceMapDialogFragment.setArguments(bundle);
                mChoiceMapDialogFragment.show(getChildFragmentManager(), "ChoiceMapDialog");
            }

            @Override
            public void onCall(int position) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mAdapterPendingDataList.get(position).getPhoneNumber()));
                startActivity(intent);
            }

            @Override
            public void onPend(int position) {
                Log.i(TAG, "onPend: position-->" + position);
                showFlushDialog();
            }

            @Override
            public void onItem(int position) {
                Log.i(TAG, "onItem: position-->" + position);
                Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
                intent.putExtra(Data.DATA_INTENT_ORDER_NO, mAdapterPendingDataList.get(position).getOrder());
                intent.putExtra(Data.DATA_INTENT_ORDER_DETAILS_IS_CHECKED, true);
                startActivity(intent);
            }
        });

        mNetworkManager.setOnPendingOrderListener(new OnPendingOrderListener() {
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
                PendingBean pendingBean = gson.fromJson(result, PendingBean.class);
                if (!pendingBean.isSuccess()) {
                    mStringMessage = pendingBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                mAdapterPendingDataList.clear();

                for (PendingBean.ObjBean objBean : pendingBean.getObj()) {
                    String custName = objBean.getCustName();
                    long time = objBean.getDoorTime();
                    String address = objBean.getProvince() + objBean.getCity() + objBean.getDistrict();
                    String orderType;
                    int wire = 0;
                    int wireless = 0;
                    switch (objBean.getOrderType()) {
                        case 1: {
                            orderType = "安装：";
                            wire = objBean.getWiredNum();
                            wireless = objBean.getWirelessNum();
                            break;
                        }
                        case 2: {
                            orderType = "维修：";
                            wire = objBean.getWiredNum();
                            wireless = objBean.getWirelessNum();
                            break;
                        }
                        case 3: {
                            orderType = "折改：";
                            wire = objBean.getRemoveWiredNum();
                            wireless = objBean.getRemoveWirelessNum();
                            break;
                        }
                        default: {
                            orderType = "";
                            Log.i(TAG, "onSuccess: orderType.default-->" + objBean.getOrderType());
                        }
                    }

                    mAdapterPendingDataList.add(new AdapterPendingData(objBean.getOrderNo()
                            , custName
                            , objBean.getContactName()
                            , objBean.getContactPhone()
                            , new TimeFormatU().millisToDate(time)
                            , address
                            , orderType
                            , wire
                            , wireless
                            , objBean.getOrderId()));
                }

                myHandler.sendEmptyMessage(Data.MSG_1);
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

    //  显示刷新Dialog
    private void showFlushDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_message_editable, null);
        TextView tvInfo = view.findViewById(R.id.tv_dialog_message_message);
        Button button = view.findViewById(R.id.btn_dialog_message_cancel);

        builder.setView(view);

        tvInfo.setText("订单变更为待审核，无法派单!");
        button.setText("刷新");

        final AlertDialog dialog = builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeRefreshLayout.setRefreshing(true);
                mNetworkManager.getPenddingOrder(jobNo, token, "", "", userName);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //显示popupWindow
    private void showPopupWindow() {

        View viewPop = LayoutInflater.from(getContext()).inflate(R.layout.view_popup_window, null);

        ListView listView = viewPop.findViewById(R.id.lv_view_popup);

        final PopupWindow popupWindow = new PopupWindow(viewPop
                , ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT
                , true);

        List<AdapterPopupData> adapterPopupDataList = new ArrayList<>();
        adapterPopupDataList.add(new AdapterPopupData("待派单", 12));
        adapterPopupDataList.add(new AdapterPopupData("安装退回", 2));
        adapterPopupDataList.add(new AdapterPopupData("待审核", 12));
        PopupAdapter popupAdapter = new PopupAdapter(getContext(), adapterPopupDataList);
        listView.setAdapter(popupAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                popupWindow.dismiss();
            }
        });

        setBackgroundAlpha(0.5f);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });

        popupWindow.showAsDropDown(mImageViewTitleRight);
    }

    //  显示popup时背景的改变
    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        getActivity().getWindow().setAttributes(lp);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            switch (msg.what) {
                case Data.MSG_ERO: {
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    mPendingAdapter.notifyDataSetChanged();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default-->" + msg.what);
                }
            }
        }
    }
}
