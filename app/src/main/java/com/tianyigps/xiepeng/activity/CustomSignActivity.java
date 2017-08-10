package com.tianyigps.xiepeng.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.tianyigps.signviewlibrary.SignView;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.bean.CarInfo;
import com.tianyigps.xiepeng.bean.CarInfoOut;
import com.tianyigps.xiepeng.bean.SaveOrderInfoBean;
import com.tianyigps.xiepeng.bean.TerminalInfo;
import com.tianyigps.xiepeng.bean.TerminalInfoOut;
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.interfaces.OnSaveOrderInfoListener;
import com.tianyigps.xiepeng.manager.DatabaseManager;
import com.tianyigps.xiepeng.manager.LocateManager;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;
import com.tianyigps.xiepeng.utils.Base64U;

import java.util.ArrayList;
import java.util.List;

public class CustomSignActivity extends BaseActivity {

    private static final String TAG = "CustomSignActivity";

    private static final int TYPE_INSTALL = 0;
    private static final int TYPE_REPAIR = 1;
    private static final int TYPE_REMOVE = 2;

    private SignView mSignView;
    private LinearLayout mLinearLayoutClear;
    private Button mButtonSubmit;

    private SharedpreferenceManager mSharedpreferenceManager;
    private NetworkManager mNetworkManager;
    private DatabaseManager mDatabaseManager;
    private LocateManager mLocateManager;
    private MyHandler myHandler;
    private int eid;
    private String token;
    private String userName;
    private String mOrderNo;
    private String mPartReason = "";
    private String mSignature;

    private String mJson;

    private int installType;

    private String mStringMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_sign);

        init();

        setEventListener();
    }

    private void init() {
        this.setTitleText("客户签字");

        mSignView = findViewById(R.id.sv_activity_custom_sign);
        mLinearLayoutClear = findViewById(R.id.ll_activity_custom_sign);
        mButtonSubmit = findViewById(R.id.btn_activity_custom_sign_submit);

        mSharedpreferenceManager = new SharedpreferenceManager(this);
        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();

        mNetworkManager = new NetworkManager();
        mDatabaseManager = new DatabaseManager(this);
        mLocateManager = new LocateManager(this);
        myHandler = new MyHandler();

        Intent intent = getIntent();
        installType = intent.getIntExtra(Data.DATA_INTENT_INSTALL_TYPE, TYPE_INSTALL);
        mOrderNo = intent.getStringExtra(Data.DATA_INTENT_ORDER_NO);
    }

    private void setEventListener() {
        mLinearLayoutClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignView.clearPath();
            }
        });

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/20 提交

                Bitmap bitmap = mSignView.getPic();

                Matrix matrix = new Matrix();
                matrix.postScale(0.2f, 0.2f);
                Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);


                String base64 = Base64U.encode(bitmap2);

                mSignature = Data.DATA_PIC_SIGN_HEAD + base64;

                Log.i(TAG, "onClick: base64-->" + mSignature);

                String json = null;

                switch (installType) {
                    case TYPE_INSTALL: {
                        json = getInstallJson();
                        break;
                    }
                    case TYPE_REMOVE: {
                        json = getInstallJson();
                        Log.i(TAG, "onClick: 拆除");
                        break;
                    }
                    case TYPE_REPAIR: {
                        json = getRepairJson();
                        break;
                    }
                    default: {
                        Log.i(TAG, "onClick: default-->" + installType);
                    }
                }

                Log.i(TAG, "onClick: json-->" + json);

                mJson = json;

                mLocateManager.startLocate();

                getOrder();


                //保存图片，可以不要

                /*
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileManager f = new FileManager(Data.DATA_PIC_SIGN);
                        f.saveBitmapPng(bitmap2);
                    }
                });
                thread.start();
                */
            }
        });

        mLocateManager.setOnReceiveLocationListener(new LocateManager.OnReceiveLocationListener() {
            @Override
            public void onReceive(LatLng latLng) {

                mLocateManager.stopLocate();

                mNetworkManager.saveOrderInfo(eid
                        , token
                        , mOrderNo
                        , mJson
                        , mPartReason
                        , mSignature
                        , "" + latLng.latitude
                        , "" + latLng.longitude
                        , Data.LOCATE_TYPE_BAIDU, userName);
            }
        });

        mNetworkManager.setOnSaveOrderInfoListener(new OnSaveOrderInfoListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                SaveOrderInfoBean saveOrderInfoBean = gson.fromJson(result, SaveOrderInfoBean.class);

                mStringMessage = saveOrderInfoBean.getMsg();

                if (!saveOrderInfoBean.isSuccess()){
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });
    }

    private void getOrder() {
        Cursor cursor = mDatabaseManager.getOrder(mOrderNo);
        if (null != cursor && cursor.moveToFirst()) {
            do {
                String orderNo = cursor.getString(0);
                int carId = cursor.getInt(1);
                int tId = cursor.getInt(2);

                Log.i(TAG, "getRepairJson: orderNo-->" + orderNo + " ,carId-->" + carId + " ,tId-->" + tId);
            } while (cursor.moveToNext());
        }
    }

    private String getInstallJson() {
        TerminalInfo terminalInfo1 = new TerminalInfo(111, "222", "333", 1, 1, "444");
        TerminalInfo terminalInfo2 = new TerminalInfo(1111, "222", "333", 2, 2, "444");
        TerminalInfo terminalInfo3 = new TerminalInfo(111, "222", "333", 3, 3, "444");

        TerminalInfo terminalInfo4 = new TerminalInfo(111, "222", "333", 4, 4, "444");
        TerminalInfo terminalInfo5 = new TerminalInfo(111, "222", "333", 5, 5, "444", "5555");

        List<TerminalInfo> terminalInfoList1 = new ArrayList<>();
        terminalInfoList1.add(terminalInfo1);
        terminalInfoList1.add(terminalInfo2);
        terminalInfoList1.add(terminalInfo3);

        List<TerminalInfo> terminalInfoList2 = new ArrayList<>();
        terminalInfoList2.add(terminalInfo4);
        terminalInfoList2.add(terminalInfo5);

        CarInfo carInfo1 = new CarInfo(1, "cN1111", "cV1111", "111", terminalInfoList1);
        CarInfo carInfo2 = new CarInfo(2, "cN2222", "cV2222", "222", terminalInfoList2);

        CarInfoOut carInfoOut1 = new CarInfoOut(carInfo1);
        CarInfoOut carInfoOut2 = new CarInfoOut(carInfo2);

        List<CarInfoOut> carInfoOutList = new ArrayList<>();
        carInfoOutList.add(carInfoOut1);
        carInfoOutList.add(carInfoOut2);

        Gson gson = new Gson();
        String json = gson.toJson(carInfoOutList);
        Log.i(TAG, "getInstallJson: json-->" + json);
        return json;
    }

    private String getRepairJson() {
        Cursor cursor = mDatabaseManager.getOrder(mOrderNo);
        if (null != cursor && cursor.moveToFirst()) {
            do {
                String orderNo = cursor.getString(0);
                int carId = cursor.getInt(1);
                int tId = cursor.getInt(2);

                Log.i(TAG, "getRepairJson: orderNo-->" + orderNo + " ,carId-->" + carId + " ,tId-->" + tId);

                Cursor cursorR = mDatabaseManager.getRepair(tId);

                if (null != cursorR && cursorR.moveToFirst()) {

                    cursorR.close();
                }
            } while (cursor.moveToNext());

            cursor.close();
        }


        TerminalInfo terminalInfo1 = new TerminalInfo(111, "222", "333", 1, 1, "444", "5555");
        TerminalInfo terminalInfo2 = new TerminalInfo(111, "222", "333", 2, 2, "444", "5555");
        TerminalInfo terminalInfo3 = new TerminalInfo(111, "222", "333", 3, 3, "444", "5555");
        TerminalInfo terminalInfo4 = new TerminalInfo(111, "222", "333", 4, 4, "444", "5555");

        TerminalInfoOut terminalInfoOut1 = new TerminalInfoOut(terminalInfo1);
        TerminalInfoOut terminalInfoOut2 = new TerminalInfoOut(terminalInfo2);
        TerminalInfoOut terminalInfoOut3 = new TerminalInfoOut(terminalInfo3);
        TerminalInfoOut terminalInfoOut4 = new TerminalInfoOut(terminalInfo4);

        List<TerminalInfoOut> terminalInfoOurList = new ArrayList<>();

        terminalInfoOurList.add(terminalInfoOut1);
        terminalInfoOurList.add(terminalInfoOut2);
        terminalInfoOurList.add(terminalInfoOut3);
        terminalInfoOurList.add(terminalInfoOut4);

        Gson gson = new Gson();
        String json = gson.toJson(terminalInfoOurList);
        Log.i(TAG, "getRepairJson: json-->" + json);
        return json;
    }

    //  显示信息Dialog
    private void showMessageDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomSignActivity.this);
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

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Data.MSG_ERO:{
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_1:{
                    showMessageDialog(mStringMessage);
                    break;
                }
                default:{
                    Log.i(TAG, "handleMessage: default-->" + msg.what);
                }
            }
        }
    }
}
