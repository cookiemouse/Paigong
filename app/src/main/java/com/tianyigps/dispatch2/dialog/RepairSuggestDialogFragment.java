package com.tianyigps.dispatch2.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.adapter.OrderTrackAdapter;
import com.tianyigps.dispatch2.adapter.RepairSuggestAdapter;
import com.tianyigps.dispatch2.bean.OrderTrackBean;
import com.tianyigps.dispatch2.bean.PendDetailsBean;
import com.tianyigps.dispatch2.data.AdapterOrderTrackData;
import com.tianyigps.dispatch2.data.AdapterPendDetailsData;
import com.tianyigps.dispatch2.data.AdapterRepairSeggestData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.interfaces.OnOrderTrackListener;
import com.tianyigps.dispatch2.interfaces.OnPendDetailsListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.RegularU;
import com.tianyigps.dispatch2.utils.TimeFormatU;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/8/9.
 */

public class RepairSuggestDialogFragment extends DialogFragment {

    private static final String TAG = "RepairSuggest";

    private ListView mListView;
    private List<AdapterRepairSeggestData> mAdapterRepairSeggestDataList;
    private RepairSuggestAdapter mRepairSuggestAdapter;

    private SharedpreferenceManager mSharedpreferenceManager;
    private String jobNo;
    private String token;
    private String userName;
    private String orderNo;

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private String mStringMessage;
    private int mEngineerType = 0;

    private View mView;

    private static RepairSuggestDialogFragment mROrderTrackDialogFragment;

    public static RepairSuggestDialogFragment getInstance() {
        if (null == mROrderTrackDialogFragment) {
            mROrderTrackDialogFragment = new RepairSuggestDialogFragment();
            synchronized (RepairSuggestDialogFragment.class) {
                if (null == mROrderTrackDialogFragment) {
                    mROrderTrackDialogFragment = new RepairSuggestDialogFragment();
                }
            }
        }
        return mROrderTrackDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  获取参数
        Bundle bundle = getArguments();

        mView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_order_track, null);

        init(mView);

        setEventListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(mView);

        AlertDialog dialog = builder.create();

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setBackgroundAlpha(0.4f);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        setBackgroundAlpha(1f);
    }

    private void init(View view) {

        mSharedpreferenceManager = new SharedpreferenceManager(getActivity());
        jobNo = mSharedpreferenceManager.getJobNo();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();
        orderNo = getActivity().getIntent().getStringExtra(Data.DATA_INTENT_ORDER_NO);

        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();

        mListView = view.findViewById(R.id.lv_activity_order_track);
        mAdapterRepairSeggestDataList = new ArrayList<>();

        mRepairSuggestAdapter = new RepairSuggestAdapter(getActivity(), mAdapterRepairSeggestDataList);

        mListView.setAdapter(mRepairSuggestAdapter);

        mNetworkManager.getPendDetails(jobNo, token, orderNo, userName);
    }

    private void setEventListener() {
        mNetworkManager.setOnPendDetailsListener(new OnPendDetailsListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                mAdapterRepairSeggestDataList.clear();
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                PendDetailsBean pendDetailsBean = gson.fromJson(result, PendDetailsBean.class);
                if (!pendDetailsBean.isSuccess()) {
                    mStringMessage = pendDetailsBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }
                PendDetailsBean.ObjBean objBean = pendDetailsBean.getObj();
                List<PendDetailsBean.ObjBean.OrderCarListBean> carListBeans = objBean.getOrderCarList();
                for (PendDetailsBean.ObjBean.OrderCarListBean carListBean : carListBeans) {
                    String carVin = carListBean.getCarVin();
                    String carNo = carListBean.getCarNo();
                    String owner = carListBean.getOwnerName();
                    String carBrand = carListBean.getCarBrand();

                    StringBuilder sb = new StringBuilder();
                    if (RegularU.isEmpty(carVin)){
                        if (!RegularU.isEmpty(carNo)){
                            sb.append(carNo);
                        }
                    }else {
                        sb.append(carVin);
                    }
                    if (RegularU.isEmpty(sb.toString())){
                        if (!RegularU.isEmpty(owner)){
                            sb.append(owner);
                        }
                    }else {
                        if (!RegularU.isEmpty(owner)){
                            sb.append(",");
                            sb.append(owner);
                        }
                    }
                    if (RegularU.isEmpty(sb.toString())){
                        if (!RegularU.isEmpty(carBrand)){
                            sb.append(carBrand);
                        }
                    }else {
                        if (!RegularU.isEmpty(carBrand)){
                            sb.append(",");
                            sb.append(carBrand);
                        }
                    }

                    List<PendDetailsBean.ObjBean.OrderCarListBean.CarTerminalListBean> carTerminalListBeans = carListBean.getCarTerminalList();
                    for (PendDetailsBean.ObjBean.OrderCarListBean.CarTerminalListBean carTerminalListBean : carTerminalListBeans) {
                        String imei = carTerminalListBean.getTNo();
                        String suggest = carTerminalListBean.getRepairSuggest();

                        mAdapterRepairSeggestDataList.add(new AdapterRepairSeggestData(sb.toString(), imei, suggest));
                    }
                }

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RepairSuggestDialogFragment.this.dismiss();
            }
        });
    }


    //  显示时背景的改变
    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        getActivity().getWindow().setAttributes(lp);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_ERO: {
                    break;
                }
                case Data.MSG_1: {
                    mRepairSuggestAdapter.notifyDataSetChanged();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default-->" + msg.what);
                }
            }
        }
    }
}
