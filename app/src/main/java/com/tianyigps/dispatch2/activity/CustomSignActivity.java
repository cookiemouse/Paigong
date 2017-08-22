package com.tianyigps.dispatch2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.bean.CarInfo;
import com.tianyigps.dispatch2.bean.CarInfoOut;
import com.tianyigps.dispatch2.bean.SaveOrderInfoBean;
import com.tianyigps.dispatch2.bean.TerminalInfo;
import com.tianyigps.dispatch2.bean.TerminalInfoOut;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnSaveOrderInfoListener;
import com.tianyigps.dispatch2.manager.DatabaseManager;
import com.tianyigps.dispatch2.manager.LocateManager;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.Base64U;
import com.tianyigps.signviewlibrary.SignView;

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

    //  LoadingFragment
    private LoadingDialogFragment mLoadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_sign);

        init();

        setEventListener();
    }

    @Override
    protected void onDestroy() {
        mDatabaseManager.close();
        super.onDestroy();
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
        mLoadingDialogFragment = new LoadingDialogFragment();

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
                // 2017/7/20 提交

                Bitmap bitmap = mSignView.getPic();

                Matrix matrix = new Matrix();
                matrix.postScale(0.2f, 0.2f);
                Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);


                String base64 = Base64U.encode(bitmap2);

                mSignature = Data.DATA_PIC_SIGN_HEAD + base64;

                Log.i(TAG, "onClick: base64-->" + mSignature);
                Log.i(TAG, "onClick: base64.size-->" + mSignature.length());

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

                showLoading();
                mLocateManager.startLocate();
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
                        , Data.LOCATE_TYPE_BAIDU
                        , userName);
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

                if (!saveOrderInfoBean.isSuccess()) {
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }
                if ("部分完成".equals(mStringMessage)) {
                    myHandler.sendEmptyMessage(Data.MSG_2);
                    return;
                }
                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });
    }

    private String getInstallJson() {

        List<CarInfoOut> carInfoOutList = new ArrayList<>();

        Cursor cursor = mDatabaseManager.getOrder(mOrderNo);
        if (null != cursor && cursor.moveToFirst()) {
            int carId = cursor.getInt(1);
            List<TerminalInfo> terminalInfoList = new ArrayList<>();
            do {
                String orderNo = cursor.getString(0);
                int tId = cursor.getInt(2);


                Log.i(TAG, "getRepairJson: orderNo-->" + orderNo + " ,carId-->" + carId + " ,tId-->" + tId);
                Cursor cursorTer = mDatabaseManager.getTer(tId);
                if (null != cursorTer && cursorTer.moveToFirst()) {
                    String id = cursorTer.getString(0);
                    String tNoOld = cursorTer.getString(1);
                    String tNoNew = cursorTer.getString(2);
                    String position = cursorTer.getString(3);
                    String positionPic = cursorTer.getString(4);
                    String installPic = cursorTer.getString(5);
                    String positionPicUrl = cursorTer.getString(6);
                    String installPicUrl = cursorTer.getString(7);
                    int model = cursorTer.getInt(8);
                    int tid = cursorTer.getInt(9);
                    int locateType = cursorTer.getInt(10);

                    Log.i(TAG, "loadTerminalData: id-->" + id);
                    Log.i(TAG, "loadTerminalData: tNoOld-->" + tNoOld);
                    Log.i(TAG, "loadTerminalData: tNoNew-->" + tNoNew);
                    Log.i(TAG, "loadTerminalData: position-->" + position);
                    Log.i(TAG, "loadTerminalData: positionPic-->" + positionPic);
                    Log.i(TAG, "loadTerminalData: installPic-->" + installPic);
                    Log.i(TAG, "loadTerminalData: positionPicUrl-->" + positionPicUrl);
                    Log.i(TAG, "loadTerminalData: installPicUrl-->" + installPicUrl);
                    Log.i(TAG, "loadTerminalData: model-->" + model);
                    Log.i(TAG, "loadTerminalData: tid-->" + tid);
                    Log.i(TAG, "loadTerminalData: locateType-->" + locateType);
                    if (null == tNoOld) {
                        tNoOld = "";
                    }
                    if (locateType == 0) {
                        locateType = 3;
                    }
                    TerminalInfo terminalInfo = new TerminalInfo(tid, tNoNew, tNoOld, model, locateType, position);
                    terminalInfoList.add(terminalInfo);
                }

            } while (cursor.moveToNext());

            Cursor cursorCar = mDatabaseManager.getCar(carId);
            Log.i(TAG, "getOrder: cursorCar-->" + cursorCar);
            if (null != cursorCar && cursorCar.moveToFirst()) {

                String carNo = cursorCar.getString(1);
                String carVin = cursorCar.getString(2);
                String carType = cursorCar.getString(3);

                Log.i(TAG, "getOrder: carNo-->" + carNo);
                Log.i(TAG, "getOrder: carVin-->" + carVin);
                Log.i(TAG, "getOrder: carType-->" + carType);

                CarInfo carInfo = new CarInfo(carId, carNo, carVin, carType, terminalInfoList);

                CarInfoOut carInfoOut = new CarInfoOut(carInfo);

                carInfoOutList.add(carInfoOut);

                cursorCar.close();
            }

            cursor.close();
        }

        Gson gson = new Gson();
        String json = gson.toJson(carInfoOutList);
        Log.i(TAG, "getOrder: json-->" + json);

        return json;
    }


    private String getRepairJson() {

        List<TerminalInfoOut> terminalInfoOurList = new ArrayList<>();

        Log.i(TAG, "getRepairJson: mOrderNo-->" + mOrderNo);
        Cursor cursor = mDatabaseManager.getOrder(mOrderNo);
        if (null != cursor && cursor.moveToFirst()) {
            do {
                String orderNo = cursor.getString(0);
                int carId = cursor.getInt(1);
                int tId = cursor.getInt(2);
                Log.i(TAG, "getRepairJson: orderNo-->" + orderNo + " ,carId-->" + carId + " ,tId-->" + tId);

                Cursor cursorR = mDatabaseManager.getRepair(tId);
                if (null != cursorR && cursorR.moveToFirst()) {
                    String imeiOld = cursorR.getString(1);
                    String positionNew = cursorR.getString(2);
                    String explain = cursorR.getString(5);
                    String imeiNew = cursorR.getString(6);
                    int model = cursorR.getInt(9);
                    int locateType = cursorR.getInt(10);

                    if (0 == locateType) {
                        locateType = 3;
                    }

                    if (null == imeiNew) {
                        imeiNew = "";
                    }
                    Log.i(TAG, "getRepairJson: tid-->" + cursorR.getInt(0));
                    Log.i(TAG, "getRepairJson: imeiOld-->" + imeiOld);
                    Log.i(TAG, "getRepairJson: positionNew-->" + positionNew);
                    Log.i(TAG, "getRepairJson: explain-->" + explain);
                    Log.i(TAG, "getRepairJson: imeiNew-->" + imeiNew);
                    Log.i(TAG, "getRepairJson: model-->" + model);
                    Log.i(TAG, "getRepairJson: locateType-->" + locateType);

                    TerminalInfo terminalInfo = new TerminalInfo(tId, imeiOld, imeiNew, model, locateType, positionNew, explain);
                    TerminalInfoOut terminalInfoOut = new TerminalInfoOut(terminalInfo);
                    terminalInfoOurList.add(terminalInfoOut);
                    cursorR.close();
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        Gson gson = new Gson();
        String json = gson.toJson(terminalInfoOurList);
        Log.i(TAG, "getRepairJson: json-->" + json);
        return json;
    }

    //  显示信息Dialog
    private void showMessageDialog(String msg) {
        if (isFinishing()) {
            return;
        }
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

    //  显示完成Dialog
    private void showCompleteDialog(String msg) {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomSignActivity.this);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: 2017/8/22 跳转到已处理Fragment
                CustomSignActivity.this.finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //  显示部分完成Dialog
    private void showPartCompleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomSignActivity.this);
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_message_editable, null);
        builder.setView(viewDialog);
        AlertDialog alertDialog = builder.create();

        TextView tvMessage = viewDialog.findViewById(R.id.tv_dialog_message_message);
        Button btnCall = viewDialog.findViewById(R.id.btn_dialog_message_cancel);

        tvMessage.setText("信息已上传，请联系后台完成订单！");
        btnCall.setText("拨打后台电话");

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/8/16 拨打后台电话，即总部电话
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "18017325972"));
                startActivity(intent);
            }
        });

        alertDialog.show();
    }

    //  显示LoadingFragment
    private void showLoading() {
        mLoadingDialogFragment.show(getFragmentManager(), "LoadingFragment");
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mLoadingDialogFragment.isAdded()) {
                mLoadingDialogFragment.dismiss();
            }

            switch (msg.what) {
                case Data.MSG_ERO: {
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    showCompleteDialog(mStringMessage);
                    break;
                }
                case Data.MSG_2: {
                    //  部分完成
                    showPartCompleteDialog();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default-->" + msg.what);
                }
            }
        }
    }
}
