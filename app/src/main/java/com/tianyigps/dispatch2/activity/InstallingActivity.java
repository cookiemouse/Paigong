package com.tianyigps.dispatch2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.adapter.InstallingAdapter;
import com.tianyigps.dispatch2.adapter.RemoveAdapter;
import com.tianyigps.dispatch2.adapter.RepairAdapter;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.bean.StartOrderInfoBean;
import com.tianyigps.dispatch2.customview.MyListView;
import com.tianyigps.dispatch2.data.AdapterInstallingData;
import com.tianyigps.dispatch2.data.AdapterRemoveData;
import com.tianyigps.dispatch2.data.AdapterRepairData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderInfoStartListener;
import com.tianyigps.dispatch2.manager.DatabaseManager;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.RegularU;

import java.util.ArrayList;
import java.util.List;

/**
 * 这里需要三个Adapter，因为页面结构一样
 * 所以复用title和listview
 * 让listview加载三个不同的adapter来实现功能
 * 还可以复用数据请求
 * <p>
 * 其中RemoveAdapter中有三种不同的item
 * <p>
 * 这是一个有意思的页面
 **/

public class InstallingActivity extends BaseActivity {

    private static final String TAG = "InstallingActivity";

    private static final int TYPE_INSTALL = 0;
    private static final int TYPE_REPAIR = 1;
    private static final int TYPE_REMOVE = 2;

    private LinearLayout mLinearLayoutRemark;
    private TextView mTextViewRemarks;
    private MyListView mListView;
    private Button mButtonNext;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<AdapterInstallingData> mAdapterInstallingDataList = new ArrayList<>();
    private InstallingAdapter mInstallingAdapter;

    private List<AdapterRemoveData> mAdapterRemoveDataList = new ArrayList<>();
    private RemoveAdapter mRemoveAdapter;

    private List<AdapterRepairData> mAdapterRepairDataList = new ArrayList<>();
    private RepairAdapter mRepairAdapter;

    //  网络请求
    private NetworkManager mNetworkManager;
    private int eid;
    private String token;
    private String userName;
    private String orderNo;
    private SharedpreferenceManager mSharedpreferenceManager;
    private String mBaseImg;
    private String ID_MAIN_TERMINAL;

    private String mStringRemarks;
    private String mStringMsg = "数据请求失败，请检查网络";

    private MyHandler myHandler;

    //  AdapterType
    private int mAdapterType;

    //  数据库
    private DatabaseManager mDatabaseManager;

    //  完成数量
    private int mCompleteCountRepair = 0, mCompleteCountInstall = 0, mCompleteCountRemove = 0;
    //  卡片中是否已有填写内容
    private boolean isFilled = false;
    private boolean isInstallCarFilled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installing);

        init();

        setEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //将背景色复原
        mSwipeRefreshLayout.setRefreshing(true);
        mNetworkManager.getWorkerOrderInfoStart(eid, token, orderNo, userName);
//        resetRepairList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Data.DATA_INTENT_REASON_REQUEST && resultCode == Data.DATA_INTENT_REASON_RESULT) {
            String reason = data.getStringExtra(Data.DATA_INTENT_REASON);
            Log.i(TAG, "onActivityResult: reason-->" + reason);
            Intent intent = new Intent(InstallingActivity.this, CustomSignActivity.class);
            intent.putExtra(Data.DATA_INTENT_ORDER_NO, orderNo);
            intent.putExtra(Data.DATA_INTENT_INSTALL_TYPE, mAdapterType);
            intent.putExtra(Data.DATA_INTENT_REASON, reason);
            startActivity(intent);
        }
    }

    private void init() {
        this.setTitleText("");

        mListView = findViewById(R.id.lv_activity_installing);

        mLinearLayoutRemark = findViewById(R.id.ll_layout_activity_installing_remarkds);
        mTextViewRemarks = findViewById(R.id.tv_layout_activity_installing_remarks);
        mButtonNext = findViewById(R.id.btn_layout_activity_installing_next);

        mSwipeRefreshLayout = findViewById(R.id.srl_activity_installing);
        mSwipeRefreshLayout.setColorSchemeColors(0xff3cabfa);

        mSharedpreferenceManager = new SharedpreferenceManager(InstallingActivity.this);
        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();
        mBaseImg = mSharedpreferenceManager.getImageBaseUrl();

        Intent intent = getIntent();
        orderNo = intent.getStringExtra(Data.DATA_INTENT_ORDER_NO);
        mAdapterType = intent.getIntExtra(Data.DATA_INTENT_INSTALL_TYPE, TYPE_INSTALL);

        Log.i(TAG, "init: orderType-->" + mAdapterType);

        mNetworkManager = new NetworkManager();

        mDatabaseManager = new DatabaseManager(this);

        myHandler = new MyHandler();

        mInstallingAdapter = new InstallingAdapter(InstallingActivity.this, mAdapterInstallingDataList);
        mRemoveAdapter = new RemoveAdapter(InstallingActivity.this, mAdapterRemoveDataList);
        mRepairAdapter = new RepairAdapter(InstallingActivity.this, mAdapterRepairDataList);

        switch (mAdapterType) {
            case TYPE_INSTALL: {
                mListView.setAdapter(mInstallingAdapter);
                break;
            }
            case TYPE_REMOVE: {
                mListView.setAdapter(mRemoveAdapter);
                break;
            }
            case TYPE_REPAIR: {
                mListView.setAdapter(mRepairAdapter);
                break;
            }
            default: {
                Log.i(TAG, "init: default");
            }
        }
    }

    private void setEventListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNetworkManager.getWorkerOrderInfoStart(eid, token, orderNo, userName);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                switch (mAdapterType) {
                    case TYPE_INSTALL: {
                        toInstallActivity(position);
                        break;
                    }
                    case TYPE_REMOVE: {
                        AdapterRemoveData data = mAdapterRemoveDataList.get(position);
                        if (null == data.getFrameNo()) {
                            toRemoveActivity(data.getCarId());
                            return;
                        }
                        toInstallActivity(position);
                        break;
                    }
                    case TYPE_REPAIR: {
                        AdapterRepairData data = mAdapterRepairDataList.get(position);
                        toRepairActivity(data.gettId(), data.getId(), data.getCarId());
                        break;
                    }
                    default: {
                        Log.i(TAG, "init: default");
                    }
                }
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/8/2 check
                if (checkRepairList() && checkInstallList() && checkRemoveList()) {
                    toCustomSign();
                    return;
                }
                Log.i(TAG, "onClick: mCompleteCountRepair-->" + mCompleteCountRepair);
                Log.i(TAG, "onClick: mCompleteCountInstall-->" + mCompleteCountInstall);
                Log.i(TAG, "onClick: mCompleteCountRemove-->" + mCompleteCountRemove);
                if (!checkRepairList() && mCompleteCountRepair == 0) {
                    if (isFilled) {
                        // 2017/8/30 未完善对话框
                        showNotPerfectDialog();
                        return;
                    }
                    showNullDialog();
                    return;
                }

                if (!checkInstallList() && mCompleteCountInstall == 0) {
                    if (isFilled) {
                        // 2017/8/30 未完善对话框
                        showNotPerfectDialog();
                        return;
                    }
                    showNullDialog();
                    return;
                }
                if (!checkRemoveList() && mCompleteCountRemove == 0) {
                    if (isFilled) {
                        // 2017/8/30 未完善对话框
                        showNotPerfectDialog();
                        return;
                    }
                    showNullDialog();
                    return;
                }
                showPartDialog();
            }
        });

        mNetworkManager.setGetWorkerOrderInfoStartListener(new OnGetWorkerOrderInfoStartListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                mAdapterInstallingDataList.clear();
                mAdapterRepairDataList.clear();
                mAdapterRemoveDataList.clear();

                Gson gson = new Gson();
                StartOrderInfoBean startOrderInfoBean = gson.fromJson(result, StartOrderInfoBean.class);

                if (!startOrderInfoBean.isSuccess()) {
                    mStringMsg = startOrderInfoBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }
                StartOrderInfoBean.ObjBean objBean = startOrderInfoBean.getObj();
                mStringRemarks = objBean.getRemark();

                for (StartOrderInfoBean.ObjBean.CarListBean carListBean : objBean.getCarList()) {
                    for (StartOrderInfoBean.ObjBean.CarListBean.CarTerminalListBean carTerminalListBean : carListBean.getCarTerminalList()) {
                        int cid = carListBean.getId();
                        int tid = carTerminalListBean.getId();
                        if (carListBean.getRemoveFlag() == 0) {
                            mDatabaseManager.addOrder(orderNo, cid, tid);
                        }
                    }
                    String carNo;
                    if (RegularU.isEmpty(carListBean.getNewCarNo())) {
                        carNo = carListBean.getCarNo();
                    } else {
                        carNo = carListBean.getNewCarNo();
                    }
                    String carBrand;
                    if (RegularU.isEmpty(carListBean.getNewCarBrand())) {
                        carBrand = carListBean.getCarBrand();
                    } else {
                        carBrand = carListBean.getNewCarBrand();
                    }
                    Cursor cursor = mDatabaseManager.getCar(carListBean.getId());
                    if (null == cursor || !cursor.moveToFirst()) {
                        mDatabaseManager.addCarInfo(carListBean.getId(), carNo, carListBean.getCarVin(), carBrand);
                        mDatabaseManager.addCarFrameNoPic(carListBean.getId()
                                , mBaseImg + carListBean.getCarVinPic()
                                , carListBean.getCarVinPic());
                        mDatabaseManager.addCarNoPic(carListBean.getId(), mBaseImg + carListBean.getCarNoPic()
                                , carListBean.getCarNoPic());
                    }
                }

                switch (mAdapterType) {
                    case TYPE_INSTALL: {
                        for (StartOrderInfoBean.ObjBean.CarListBean carListBean : objBean.getCarList()) {
                            ID_MAIN_TERMINAL = carListBean.getId() + "T";
                            int i = 0;
                            for (StartOrderInfoBean.ObjBean.CarListBean.CarTerminalListBean carTerminalListBean
                                    : carListBean.getCarTerminalList()) {

                                Cursor cursor = mDatabaseManager.getTer(carTerminalListBean.getId());
                                if (null == cursor || !cursor.moveToFirst()) {
                                    String tno, newtno;
                                    if (RegularU.isEmpty(carTerminalListBean.getNewTNo())) {
                                        tno = "";
                                        newtno = carTerminalListBean.getTNo();
                                    } else {
                                        tno = carTerminalListBean.getTNo();
                                        newtno = carTerminalListBean.getNewTNo();
                                    }
                                    Log.i(TAG, "onSuccess: tno-->" + tno);
                                    Log.i(TAG, "onSuccess: newtno-->" + newtno);
                                    Log.i(TAG, "onSuccess: position-->" + carTerminalListBean.getNewInstallLocation());
                                    Log.i(TAG, "onSuccess: positionurl-->" + carTerminalListBean.getNewInstallLocationPic());
                                    Log.i(TAG, "onSuccess: installurl-->" + carTerminalListBean.getNewWiringDiagramPic());
                                    Log.i(TAG, "onSuccess: tid-->" + carTerminalListBean.getId());
                                    Log.i(TAG, "onSuccess: terminalType-->" + carTerminalListBean.getTerminalType());
                                    int wire;
                                    if (carTerminalListBean.getTerminalType() == 2) {
                                        wire = 0;
                                    } else {
                                        wire = 1;
                                    }
                                    mDatabaseManager.addTer(ID_MAIN_TERMINAL + i
                                            , tno
                                            , newtno
                                            , carTerminalListBean.getNewInstallLocation()
                                            , mBaseImg + carTerminalListBean.getNewInstallLocationPic()
                                            , mBaseImg + carTerminalListBean.getNewWiringDiagramPic()
                                            , carTerminalListBean.getNewInstallLocationPic()
                                            , carTerminalListBean.getNewWiringDiagramPic()
                                            , carListBean.getId()
                                            , wire);
                                    mDatabaseManager.addTerModel(ID_MAIN_TERMINAL + i
                                            , carTerminalListBean.getTerminalType());
                                    mDatabaseManager.addTerId(ID_MAIN_TERMINAL + i
                                            , carTerminalListBean.getId()
                                            , carListBean.getId());
                                }
                                i++;
                            }
                            if (carListBean.getRemoveFlag() == 0) {
                                mAdapterInstallingDataList.add(new AdapterInstallingData(carListBean.getId()
                                        , carListBean.getCarVin()
                                        , carListBean.getWiredNum()
                                        , carListBean.getWirelessNum()));
                            }
                        }

                        myHandler.sendEmptyMessage(Data.MSG_1);
                        break;
                    }
                    case TYPE_REMOVE: {
                        int wireNum = 0, wirelessNum = 0, wireComplete = 0, wirelessComplete = 0, carId = 0;
                        if (objBean.getCarList().size() > 0) {
                            for (StartOrderInfoBean.ObjBean.CarListBean carListBean : objBean.getCarList()) {
                                if (carListBean.getRemoveFlag() == 1) {
                                    carId = carListBean.getId();
                                    for (StartOrderInfoBean.ObjBean.CarListBean.CarTerminalListBean carTerminalListBean
                                            : carListBean.getCarTerminalList()) {
                                        int status = carTerminalListBean.getRemoveStatus();
                                        //  统计已完成的设备数和需处理的数量
                                        //  status == 1，表示该设备已被拆除；status == 2，表示已拆除已安装；status == 0，表示未拆除
                                        switch (carTerminalListBean.getTerminalType()) {
                                            case 1: {
                                                //  有线
                                                wireNum++;
                                                if (status == 1 || status == 2) {
                                                    wireComplete++;
                                                }
                                                break;
                                            }
                                            case 2: {
                                                //  无线
                                                wirelessNum++;
                                                if (status == 1 || status == 2) {
                                                    wirelessComplete++;
                                                }
                                                break;
                                            }
                                            default: {
                                                Log.i(TAG, "onSuccess: default.TerminalType-->" + carTerminalListBean.getTerminalType());
                                            }
                                        }
                                    }
                                } else {
                                    ID_MAIN_TERMINAL = carListBean.getId() + "T";
                                    int i = 0;
                                    for (StartOrderInfoBean.ObjBean.CarListBean.CarTerminalListBean carTerminalListBean
                                            : carListBean.getCarTerminalList()) {

                                        Cursor cursor = mDatabaseManager.getTer(carTerminalListBean.getId());
                                        if (null == cursor || !cursor.moveToFirst()) {
                                            String tno, newtno;
                                            if (RegularU.isEmpty(carTerminalListBean.getNewTNo())) {
                                                tno = "";
                                                newtno = carTerminalListBean.getTNo();
                                            } else {
                                                tno = carTerminalListBean.getTNo();
                                                newtno = carTerminalListBean.getNewTNo();
                                            }
                                            Log.i(TAG, "onSuccess: tno-->" + tno);
                                            Log.i(TAG, "onSuccess: newtno-->" + newtno);
                                            Log.i(TAG, "onSuccess: position-->" + carTerminalListBean.getNewInstallLocation());
                                            Log.i(TAG, "onSuccess: positionurl-->" + carTerminalListBean.getNewInstallLocationPic());
                                            Log.i(TAG, "onSuccess: installurl-->" + carTerminalListBean.getNewWiringDiagramPic());
                                            Log.i(TAG, "onSuccess: tid-->" + carTerminalListBean.getId());
                                            Log.i(TAG, "onSuccess: terminalType-->" + carTerminalListBean.getTerminalType());
                                            int wire;
                                            if (carTerminalListBean.getTerminalType() == 2) {
                                                wire = 0;
                                            } else {
                                                wire = 1;
                                            }
                                            mDatabaseManager.addTer(ID_MAIN_TERMINAL + i
                                                    , tno
                                                    , newtno
                                                    , carTerminalListBean.getNewInstallLocation()
                                                    , mBaseImg + carTerminalListBean.getNewInstallLocationPic()
                                                    , mBaseImg + carTerminalListBean.getNewWiringDiagramPic()
                                                    , carTerminalListBean.getNewInstallLocationPic()
                                                    , carTerminalListBean.getNewWiringDiagramPic()
                                                    , carListBean.getId()
                                                    , wire);
                                            mDatabaseManager.addTerModel(ID_MAIN_TERMINAL + i
                                                    , carTerminalListBean.getTerminalType());
                                            mDatabaseManager.addTerId(ID_MAIN_TERMINAL + i
                                                    , carTerminalListBean.getId()
                                                    , carListBean.getId());
                                        }
                                        i++;
                                    }
                                }
                            }

                            if (wireNum > 0 || wirelessNum > 0) {
                                mAdapterRemoveDataList.add(new AdapterRemoveData("拆除"));

                                mAdapterRemoveDataList.add(new AdapterRemoveData(carId
                                        , wireNum
                                        , wirelessNum
                                        , wireComplete
                                        , wirelessComplete));

                                mAdapterRemoveDataList.add(new AdapterRemoveData("安装"));
                            }
                        }

                        for (StartOrderInfoBean.ObjBean.CarListBean carListBean : objBean.getCarList()) {
                            String frameNo = carListBean.getCarVin();
                            if (carListBean.getRemoveFlag() == 0) {
                                mAdapterRemoveDataList.add(new AdapterRemoveData(
                                        carListBean.getId()
                                        , frameNo
                                        , carListBean.getWiredNum()
                                        , carListBean.getWirelessNum()));
                            }
                        }

                        myHandler.sendEmptyMessage(Data.MSG_2);
                        break;
                    }
                    case TYPE_REPAIR: {
                        for (StartOrderInfoBean.ObjBean.CarListBean carListBean : objBean.getCarList()) {
                            String frameNo = carListBean.getCarVin();
                            String carNo = carListBean.getCarNo();
                            int carId = carListBean.getId();
                            for (StartOrderInfoBean.ObjBean.CarListBean.CarTerminalListBean carTerminalListBean
                                    : carListBean.getCarTerminalList()) {

                                int tId = carTerminalListBean.getId();

                                Cursor cursor = mDatabaseManager.getRepair(tId);
                                int wire;
                                if (carTerminalListBean.getTerminalType() == 2) {
                                    wire = 0;
                                } else {
                                    wire = 1;
                                }
                                if (null == cursor || !cursor.moveToFirst()) {
                                    mDatabaseManager.addRepair(tId
                                            , carTerminalListBean.getTNo()
                                            , carTerminalListBean.getNewInstallLocation()
                                            , mBaseImg + carTerminalListBean.getNewInstallLocationPic()
                                            , mBaseImg + carTerminalListBean.getNewWiringDiagramPic()
                                            , carTerminalListBean.getRepaireDesc()
                                            , carTerminalListBean.getNewTNo()
                                            , carTerminalListBean.getNewInstallLocationPic()
                                            , carTerminalListBean.getNewWiringDiagramPic()
                                            , carTerminalListBean.getTerminalType()
                                            , wire);
                                }

                                int type = carTerminalListBean.getTerminalType();

                                String terminalName = carTerminalListBean.getTerminalName();
                                if (null == terminalName) {
                                    terminalName = "";
                                }

                                String tNo = carTerminalListBean.getTNo();

                                mDatabaseManager.addRepair(tId, tNo);

                                mAdapterRepairDataList.add(new AdapterRepairData(
                                        tId
                                        , type
                                        , tNo
                                        , terminalName
                                        , carNo
                                        , frameNo
                                        , carId
                                        , carTerminalListBean.getRepaireStatus()));
                            }
                        }

                        myHandler.sendEmptyMessage(Data.MSG_3);
                        break;
                    }
                    default: {
                        Log.i(TAG, "onSuccess: default");
                    }
                }
            }
        });
    }

    private void toCustomSign() {
        Intent intent = new Intent(InstallingActivity.this, CustomSignActivity.class);
        intent.putExtra(Data.DATA_INTENT_ORDER_NO, orderNo);
        intent.putExtra(Data.DATA_INTENT_INSTALL_TYPE, mAdapterType);
        startActivity(intent);
    }

    private void toRemoveActivity(int carId) {
        Intent intent = new Intent(InstallingActivity.this, OperateRemoveActivity.class);
        intent.putExtra(Data.DATA_INTENT_ORDER_NO, orderNo);
        intent.putExtra(Data.DATA_INTENT_CAR_ID, carId);
        startActivity(intent);
    }

    private void toRepairActivity(int tId, String tNo, int carId) {
        Intent intent = new Intent(InstallingActivity.this, OperateRepairActivity.class);
        intent.putExtra(Data.DATA_INTENT_ORDER_NO, orderNo);
        intent.putExtra(Data.DATA_INTENT_T_NO, tNo);
        intent.putExtra(Data.DATA_INTENT_T_ID, tId);
        intent.putExtra(Data.DATA_INTENT_CAR_ID, carId);
        startActivity(intent);
    }

    private void toInstallActivity(int position) {
        Intent intent = new Intent(InstallingActivity.this, OperateInstallActivity.class);
        intent.putExtra(Data.DATA_INTENT_ORDER_NO, orderNo);
        String frameNo;
        int carId;
        int wiredNum;
        int wirelessNum;
        switch (mAdapterType) {
            case TYPE_INSTALL: {
                AdapterInstallingData data = mAdapterInstallingDataList.get(position);
                frameNo = data.getFrameNo();
                carId = data.getCarId();
                wiredNum = data.getOrderLine();
                wirelessNum = data.getOrderOffline();
                break;
            }
            case TYPE_REMOVE: {
                AdapterRemoveData data = mAdapterRemoveDataList.get(position);
                frameNo = data.getFrameNo();
                carId = data.getCarId();
                wiredNum = data.getOnline();
                wirelessNum = data.getOffline();
                break;
            }
            default: {
                frameNo = "";
                carId = 0;
                wiredNum = 0;
                wirelessNum = 0;
                Log.i(TAG, "toInstallActivity: default");
            }
        }
        intent.putExtra(Data.DATA_INTENT_FRAME_NO, frameNo);
        intent.putExtra(Data.DATA_INTENT_INSTALL_CAR_ID, carId);
        intent.putExtra(Data.DATA_INTENT_INSTALL_WIRED_NUM, wiredNum);
        intent.putExtra(Data.DATA_INTENT_INSTALL_WIRELESS_NUM, wirelessNum);
        startActivity(intent);
    }

    private void toEditRemarksActivity() {
        Intent intent = new Intent(InstallingActivity.this, EditRemarksActivity.class);
        startActivityForResult(intent, Data.DATA_INTENT_REASON_REQUEST);
    }

    private void showFailureDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(InstallingActivity.this);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                InstallingActivity.this.finish();
                //  do nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //  部分完成对话框
    private void showPartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InstallingActivity.this);
        View viewDialog = LayoutInflater.from(InstallingActivity.this).inflate(R.layout.dialog_button_editable, null);
        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        Button buttonCancel = viewDialog.findViewById(R.id.btn_dialog_editable_cancel);
        Button buttonEnsure = viewDialog.findViewById(R.id.btn_dialog_editable_ensure);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        buttonEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEditRemarksActivity();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //  空单对话框
    private void showNullDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InstallingActivity.this);
        View viewDialog = LayoutInflater.from(InstallingActivity.this).inflate(R.layout.dialog_button_editable, null);
        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        TextView textView = viewDialog.findViewById(R.id.tv_dialog_editable_msg);
        Button buttonCancel = viewDialog.findViewById(R.id.btn_dialog_editable_cancel);
        Button buttonEnsure = viewDialog.findViewById(R.id.btn_dialog_editable_ensure);
        textView.setText(getString(R.string.msg_null_complete));
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        buttonEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEditRemarksActivity();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //  未完善对话框
    private void showNotPerfectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InstallingActivity.this);
        View viewDialog = LayoutInflater.from(InstallingActivity.this).inflate(R.layout.dialog_message_editable, null);
        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        TextView textView = viewDialog.findViewById(R.id.tv_dialog_message_message);
        Button buttonCancel = viewDialog.findViewById(R.id.btn_dialog_message_cancel);
        textView.setText(getString(R.string.msg_not_perfect));
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //  将维修背景色初始化
    private void resetRepairList() {
        if (mAdapterRepairDataList.size() > 0) {
            for (AdapterRepairData data : mAdapterRepairDataList) {
                data.setComplete(0);
            }
        }
        myHandler.sendEmptyMessage(Data.MSG_3);
    }

    //  更新安装列表完成数量
    private void updateInstallComplete() {
        if (mAdapterInstallingDataList.size() > 0) {
            for (AdapterInstallingData data : mAdapterInstallingDataList) {

                boolean terComplete;
                int carId = data.getCarId();

                int wireComplete = 0, wirelessComplete = 0;

                Cursor cursorTer = mDatabaseManager.getTerByCarId(carId);
                if (null != cursorTer && cursorTer.moveToFirst()) {
                    do {
                        int idMainTer = cursorTer.getInt(0);
                        String tNoNew = cursorTer.getString(2);
                        String position = cursorTer.getString(3);
                        String positionUrl = cursorTer.getString(6);
                        String installUrl = cursorTer.getString(7);
                        int tId = cursorTer.getInt(9);
                        int wire = cursorTer.getInt(12);

                        Log.i(TAG, "updateInstallComplete: idMainTer-->" + idMainTer);
                        Log.i(TAG, "updateInstallComplete: tNoOld-->" + tNoNew);
                        Log.i(TAG, "updateInstallComplete: position-->" + position);
                        Log.i(TAG, "updateInstallComplete: positionUrl-->" + positionUrl);
                        Log.i(TAG, "updateInstallComplete: installUrl-->" + installUrl);
                        Log.i(TAG, "updateInstallComplete: wire-->" + wire);
                        Log.i(TAG, "updateInstallComplete: tId-->" + tId);

                        if (0 == wire) {
                            terComplete = (!RegularU.isEmpty(tNoNew))
                                    && (!RegularU.isEmpty(position))
                                    && !RegularU.isEmpty(positionUrl);
                            if (terComplete) {
                                wirelessComplete++;
                            }
                        } else {
                            terComplete = (!RegularU.isEmpty(tNoNew))
                                    && (!RegularU.isEmpty(position))
                                    && !RegularU.isEmpty(positionUrl)
                                    && !RegularU.isEmpty(installUrl);
                            if (terComplete) {
                                wireComplete++;
                            }
                        }
                    } while (cursorTer.moveToNext());

                    cursorTer.close();
                } else {
                    Log.i(TAG, "updateInstallComplete: cursorTer is null");
                }

                data.setCompleteLine(wireComplete);
                data.setCompleteOffline(wirelessComplete);
            }
        }
    }

    //  更新拆改列表完成数量
    private void updateRemoveComplete() {
        if (mAdapterRemoveDataList.size() > 0) {
            for (AdapterRemoveData data : mAdapterRemoveDataList) {

                if (data.getType() != 2) {
                    continue;
                }

                boolean terComplete;
                int carId = data.getCarId();

                int wireComplete = 0, wirelessComplete = 0;

                Cursor cursorTer = mDatabaseManager.getTerByCarId(carId);
                if (null != cursorTer && cursorTer.moveToFirst()) {
                    do {
                        int idMainTer = cursorTer.getInt(0);
                        String tNoNew = cursorTer.getString(2);
                        String position = cursorTer.getString(3);
                        String positionUrl = cursorTer.getString(6);
                        String installUrl = cursorTer.getString(7);
                        int wire = cursorTer.getInt(12);

                        Log.i(TAG, "updateRemoveComplete: idMainTer-->" + idMainTer);
                        Log.i(TAG, "updateRemoveComplete: tNoOld-->" + tNoNew);
                        Log.i(TAG, "updateRemoveComplete: position-->" + position);
                        Log.i(TAG, "updateRemoveComplete: positionUrl-->" + positionUrl);
                        Log.i(TAG, "updateRemoveComplete: installUrl-->" + installUrl);
                        Log.i(TAG, "updateRemoveComplete: wire-->" + wire);

                        if (0 == wire) {
                            terComplete = ((null != tNoNew && !"".equals(tNoNew))
                                    && (null != position && !"".equals(position))
                                    && (null != positionUrl) && !"".equals(positionUrl));
                            if (terComplete) {
                                wirelessComplete++;
                            }
                        } else {
                            terComplete = ((null != tNoNew && !"".equals(tNoNew))
                                    && (null != position && !"".equals(position))
                                    && (null != positionUrl) && !"".equals(positionUrl)
                                    && (null != installUrl) && !"".equals(installUrl));
                            if (terComplete) {
                                wireComplete++;
                            }
                        }
                    } while (cursorTer.moveToNext());

                    cursorTer.close();
                } else {
                    Log.i(TAG, "updateRemoveComplete: cursorTer is null");
                }

                data.setOnlineComplete(wireComplete);
                data.setOfflineComplete(wirelessComplete);
            }
        }
    }

    //  更新维修列表完成
    private void updateRepairComplete() {
        int listSize = mAdapterRepairDataList.size();
        if (listSize > 0) {
            for (AdapterRepairData data : mAdapterRepairDataList) {
                int type = data.getType();
                boolean isComplete = false;
                Cursor cursor = mDatabaseManager.getRepair(data.gettId());
                if (null != cursor && cursor.moveToFirst()) {
                    String position = cursor.getString(2);
                    String explain = cursor.getString(5);
                    String newImei = cursor.getString(6);
                    String positionUrl = cursor.getString(7);
                    String installUrl = cursor.getString(8);
                    int model = cursor.getInt(9);
                    int replace = cursor.getInt(12);

                    Log.i(TAG, "checkRepairList: position-->" + position);
                    Log.i(TAG, "checkRepairList: explain-->" + explain);
                    Log.i(TAG, "checkRepairList: newImei-->" + newImei);
                    Log.i(TAG, "checkRepairList: positionUrl-->" + positionUrl);
                    Log.i(TAG, "checkRepairList: installUrl-->" + installUrl);
                    Log.i(TAG, "checkRepairList: model-->" + model);
                    Log.i(TAG, "checkRepairList: replace-->" + replace);

                    if (1 == replace) {
                        if (1 == type) {
                            isComplete = ((null != position && !"".equals(position))
                                    && (null != explain && !"".equals(explain))
                                    && (null != newImei && !"".equals(newImei))
                                    && (null != positionUrl && !"".equals(positionUrl))
                                    && (0 != model)
                                    && (null != installUrl && !"".equals(installUrl)));
                        } else {
                            isComplete = ((null != position && !"".equals(position))
                                    && (null != explain && !"".equals(explain))
                                    && (null != newImei && !"".equals(newImei))
                                    && (null != positionUrl && !"".equals(positionUrl))
                                    && (0 != model));
                        }
                    } else {
                        isComplete = (null != explain && !"".equals(explain));
                    }

                    cursor.close();
                }

                if (isComplete) {
                    data.setComplete(1);
                } else {
                    if (data.getComplete() == 0) {
                        data.setComplete(0);
                    }
                }

            }
        }
    }

    //  检验维修列表是否已完成
    private boolean checkRepairList() {
        isFilled = false;
        boolean nextAble = false;
        int completeCount = 0;
        int listSize = mAdapterRepairDataList.size();
        if (listSize > 0) {
            for (AdapterRepairData data : mAdapterRepairDataList) {
                int type = data.getType();
                Log.i(TAG, "checkRepairList: type-->" + type);
                boolean isComplete = false;
                Cursor cursor = mDatabaseManager.getRepair(data.gettId());
                if (null != cursor && cursor.moveToFirst()) {
                    String position = cursor.getString(2);
                    String explain = cursor.getString(5);
                    String newImei = cursor.getString(6);
                    String positionUrl = cursor.getString(7);
                    String installUrl = cursor.getString(8);
                    int model = cursor.getInt(9);
                    int replace = cursor.getInt(12);

                    Log.i(TAG, "checkRepairList: position-->" + position);
                    Log.i(TAG, "checkRepairList: explain-->" + explain);
                    Log.i(TAG, "checkRepairList: newImei-->" + newImei);
                    Log.i(TAG, "checkRepairList: positionUrl-->" + positionUrl);
                    Log.i(TAG, "checkRepairList: installUrl-->" + installUrl);
                    Log.i(TAG, "checkRepairList: model-->" + model);
                    Log.i(TAG, "checkRepairList: replace-->" + replace);

                    if (1 == replace) {
                        if (1 == type) {
                            isComplete = ((null != position && !"".equals(position))
                                    && (null != explain && !"".equals(explain))
                                    && (null != newImei && !"".equals(newImei))
                                    && (null != positionUrl && !"".equals(positionUrl))
                                    && (0 != model)
                                    && (null != installUrl && !"".equals(installUrl)));
                        } else {
                            isComplete = ((null != position && !"".equals(position))
                                    && (null != explain && !"".equals(explain))
                                    && (null != newImei && !"".equals(newImei))
                                    && (null != positionUrl && !"".equals(positionUrl))
                                    && (0 != model));
                        }
                    } else {
                        isComplete = (null != explain && !"".equals(explain));
                    }

                    if (!isFilled) {
                        isFilled = (!RegularU.isEmpty(newImei))
                                || (!RegularU.isEmpty(position))
                                || (!RegularU.isEmpty(positionUrl))
                                || (!RegularU.isEmpty(installUrl));
                    }

                    cursor.close();
                }

                if (isComplete) {
                    completeCount++;
                    data.setComplete(1);
                } else {
                    data.setComplete(2);
                }

            }
        }
        myHandler.sendEmptyMessage(Data.MSG_3);

        if (completeCount == listSize) {
            nextAble = true;
        }

        mCompleteCountRepair = completeCount;

        return nextAble;
    }

    //  检验安装列表是否完成
    private boolean checkInstallList() {
        boolean nextAble = true;
        isFilled = false;
        int completeCount = 0;
        if (mAdapterInstallingDataList.size() > 0) {
            for (AdapterInstallingData data : mAdapterInstallingDataList) {

                boolean carComplete = false;
                int carId = data.getCarId();

                //  ------------分割线---------------
                //  check设备信息是否完整
                Cursor cursorTer = mDatabaseManager.getTerByCarId(carId);
                if (null != cursorTer && cursorTer.moveToFirst()) {
                    do {
                        int idMainTer = cursorTer.getInt(0);
                        String tNoNew = cursorTer.getString(2);
                        String position = cursorTer.getString(3);
                        String positionUrl = cursorTer.getString(6);
                        String installUrl = cursorTer.getString(7);

                        Log.i(TAG, "checkInstallList: idMainTer-->" + idMainTer);
                        Log.i(TAG, "checkInstallList: tNoOld-->" + tNoNew);
                        Log.i(TAG, "checkInstallList: position-->" + position);
                        Log.i(TAG, "checkInstallList: positionUrl-->" + positionUrl);
                        Log.i(TAG, "checkInstallList: installUrl-->" + installUrl);
                        if (!isFilled) {
                            isFilled = (!RegularU.isEmpty(tNoNew))
                                    || (!RegularU.isEmpty(position))
                                    || (!RegularU.isEmpty(positionUrl))
                                    || (!RegularU.isEmpty(installUrl));
                        }
                    } while (cursorTer.moveToNext());

                    cursorTer.close();
                }

                //  设备数量是否全部安装完
                completeCount = completeCount + data.getCompleteLine() + data.getCompleteOffline();

                //  check车辆信息是否完整
                Cursor cursorCar = mDatabaseManager.getCar(carId);
                if (null != cursorCar && cursorCar.moveToFirst()) {
                    String carNo = cursorCar.getString(1);
                    String frameNo = cursorCar.getString(2);
                    String carNoUrl = cursorCar.getString(6);
                    String frameNoUrl = cursorCar.getString(7);

                    Log.i(TAG, "checkInstallListCar: carNo-->" + carNo);
                    Log.i(TAG, "checkInstallListCar: frameNo-->" + frameNo);
                    Log.i(TAG, "checkInstallListCar: carNoUrl-->" + carNoUrl);
                    Log.i(TAG, "checkInstallListCar: frameNoUrl-->" + frameNoUrl);
                    Log.i(TAG, "checkInstallListCar: ---------------");

                    isInstallCarFilled = (null != frameNoUrl)
//                            || !RegularU.isEmpty(frameNo)
                            || !RegularU.isEmpty(carNoUrl)
                            || !RegularU.isEmpty(carNo);

                    carComplete = (!RegularU.isEmpty(frameNoUrl))
                            && (!RegularU.isEmpty(frameNo));

                    if (carComplete) {
                        if (!RegularU.isEmpty(carNoUrl)) {
                            carComplete = !RegularU.isEmpty(carNo);
                        }
                        if (!RegularU.isEmpty(carNo)) {
                            carComplete = !RegularU.isEmpty(carNoUrl);
                        }
                    }

                    cursorCar.close();

                    if (!carComplete && isInstallCarFilled && (completeCount == data.getOrderLine() + data.getOrderOffline())) {
                        mCompleteCountInstall = 0;
                        data.setComplete(false);
                        myHandler.sendEmptyMessage(Data.MSG_1);
                        return false;
                    }
                } else {
                    Log.i(TAG, "checkInstallList: cursorCar is null");
                }

                if (carComplete && data.getOrderLine() == data.getCompleteLine() && data.getOrderOffline() == data.getCompleteOffline()) {
                    data.setComplete(true);
                } else {
                    data.setComplete(false);
                }

                if (nextAble) {
                    nextAble = data.isComplete();
                }
            }
        }
        myHandler.sendEmptyMessage(Data.MSG_1);

        mCompleteCountInstall = completeCount;

        return nextAble;
    }

    //  检验拆除列表是否完成
    private boolean checkRemoveList() {
        boolean nextAble = true;
        int completeCount = 0;
        isFilled = false;
        if (mAdapterRemoveDataList.size() > 0) {
            for (AdapterRemoveData data : mAdapterRemoveDataList) {
                int carId = data.getCarId();
                if (0 == carId) {
                    continue;
                }

                String frameNo = data.getFrameNo();
                //  check设备信息是否完整
                if (!RegularU.isEmpty(frameNo)) {
                    Cursor cursorTer = mDatabaseManager.getTerByCarId(carId);
                    if (null != cursorTer && cursorTer.moveToFirst()) {
                        do {
                            int idMainTer = cursorTer.getInt(0);
                            String tNoNew = cursorTer.getString(2);
                            String position = cursorTer.getString(3);
                            String positionUrl = cursorTer.getString(6);
                            String installUrl = cursorTer.getString(7);

                            Log.i(TAG, "checkRemoveList: idMainTer-->" + idMainTer);
                            Log.i(TAG, "checkRemoveList: tNoOld-->" + tNoNew);
                            Log.i(TAG, "checkRemoveList: position-->" + position);
                            Log.i(TAG, "checkRemoveList: positionUrl-->" + positionUrl);
                            Log.i(TAG, "checkRemoveList: installUrl-->" + installUrl);
                            if (!isFilled) {
                                isFilled = (!RegularU.isEmpty(tNoNew))
                                        || (!RegularU.isEmpty(position))
                                        || (!RegularU.isEmpty(positionUrl))
                                        || (!RegularU.isEmpty(installUrl));
                            }
                        } while (cursorTer.moveToNext());

                        cursorTer.close();
                    }
                }

                //  设备数量是否全部安装
                int wire = data.getOnline();
                int wireless = data.getOffline();
                int wireComplete = data.getOnlineComplete();
                int wirelessComplete = data.getOfflineComplete();

                Log.i(TAG, "checkRemoveList: wire-->" + wire);
                Log.i(TAG, "checkRemoveList: wireless-->" + wireless);
                Log.i(TAG, "checkRemoveList: wireComplete-->" + wireComplete);
                Log.i(TAG, "checkRemoveList: wirelessComplete-->" + wirelessComplete);

                completeCount = completeCount + wireComplete + wirelessComplete;
                if (wire == wireComplete && wireless == wirelessComplete) {
                    data.setComplete(true);
                } else {
                    data.setComplete(false);
                }

                if (nextAble) {
                    nextAble = data.isComplete();
                }
            }
        }
        myHandler.sendEmptyMessage(Data.MSG_2);

        mCompleteCountRemove = completeCount;

        return nextAble;
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if (RegularU.isEmpty(mStringRemarks)) {
                mLinearLayoutRemark.setVisibility(View.GONE);
            } else {
                mStringRemarks = mStringRemarks.replaceAll("null", "");
                mTextViewRemarks.setText(mStringRemarks);
            }
            switch (msg.what) {
                case Data.MSG_ERO: {
                    showFailureDialog(mStringMsg);
                    break;
                }
                case Data.MSG_1: {
                    //  install
                    //更新完成数量
                    updateInstallComplete();
                    mInstallingAdapter.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_2: {
                    //  remove
                    updateRemoveComplete();
                    mRemoveAdapter.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_3: {
                    //  repair
                    updateRepairComplete();
                    mRepairAdapter.notifyDataSetChanged();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
