package com.tianyigps.dispatch2.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.adapter.ChoicePhoneAdapter;
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
import com.tianyigps.dispatch2.manager.FileManager;
import com.tianyigps.dispatch2.manager.LocateManager;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.Base64U;
import com.tianyigps.dispatch2.utils.RegularU;
import com.tianyigps.signviewlibrary.SignView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.yundian.bottomdialog.BottomDialog;

import java.util.ArrayList;
import java.util.Arrays;
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
    private FileManager mFileManager;
    private MyHandler myHandler;
    private int eid;
    private String token;
    private String userName;
    private String mOrderNo;
    private String mPartReason = "";
    private String mSignature;

    private List<String> mPhoneList;

    private String mJson;

    private int installType;

    private String mStringMessage = "";

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
    public void onBackPressed() {
        if (mFileManager.isExists()) {
            mFileManager.delete();
        }
        mFileManager.saveBitmapPng(mSignView.getPic());
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
//        mDatabaseManager.close();
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
        mPartReason = intent.getStringExtra(Data.DATA_INTENT_REASON);

        mFileManager = new FileManager(mOrderNo);
        if (mFileManager.isExists()) {
            mSignView.setBitmap(BitmapFactory.decodeFile(mFileManager.getPath()));
        }
    }

    private void setEventListener() {
        this.setOnTitleBackClickListener(new OnTitleBackClickListener() {
            @Override
            public void onClick() {
                CustomSignActivity.this.onBackPressed();
            }
        });

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

                if (mSignView.isNull()) {
                    showNoSignDialog();
                    return;
                }

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
                applyPermiss();
            }
        });

        mLocateManager.setOnReceiveLocationListener(new LocateManager.OnReceiveLocationListener() {
            @Override
            public void onReceive(LatLng latLng) {

                Log.i(TAG, "onReceive: latitude-->" + latLng.latitude);
                Log.i(TAG, "onReceive: longitude-->" + latLng.longitude);
                mLocateManager.stopLocate();
                if (latLng.latitude == 4.9E-324 || latLng.longitude == 4.9E-324) {
                    mNetworkManager.saveOrderInfo(eid
                            , token
                            , mOrderNo
                            , mJson
                            , mPartReason
                            , mSignature
                            , "0"
                            , "0"
                            , Data.LOCATE_TYPE_BAIDU
                            , userName);
                } else {
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
            do {
                String orderNo = cursor.getString(0);
                int carId = cursor.getInt(1);
                int tId = cursor.getInt(2);
                List<TerminalInfo> terminalInfoList = new ArrayList<>();

                Log.i(TAG, "getInstallJson: orderNo-->" + orderNo + " ,carId-->" + carId + " ,tId-->" + tId);

                //  设备信息
                TerminalInfo terminalInfo = null;
                Cursor cursorTer = mDatabaseManager.getTer2ByTid(tId);
                if (null != cursorTer && cursorTer.moveToFirst()) {
                    String tNoOld = cursorTer.getString(0);
                    String tNoNew = cursorTer.getString(1);
                    String position = cursorTer.getString(2);
                    String positionPic = cursorTer.getString(4);
                    String installPic = cursorTer.getString(5);
                    int item = cursorTer.getInt(3);
                    int model = cursorTer.getInt(6);
                    int tid = cursorTer.getInt(7);
                    int locateType = cursorTer.getInt(8);
                    int carid = cursorTer.getInt(9);
                    int wire = cursorTer.getInt(10);

                    Log.i(TAG, "loadTerminalData: tNoOld-->" + tNoOld);
                    Log.i(TAG, "loadTerminalData: tNoNew-->" + tNoNew);
                    Log.i(TAG, "loadTerminalData: position-->" + position);
                    Log.i(TAG, "loadTerminalData: positionPic-->" + positionPic);
                    Log.i(TAG, "loadTerminalData: installPic-->" + installPic);
                    Log.i(TAG, "loadTerminalData: model-->" + model);
                    Log.i(TAG, "loadTerminalData: tid-->" + tid);
                    Log.i(TAG, "loadTerminalData: item-->" + item);
                    Log.i(TAG, "loadTerminalData: carid-->" + carid);
                    Log.i(TAG, "loadTerminalData: locateType-->" + locateType);
                    Log.i(TAG, "loadTerminalData: wire-->" + wire);
                    if (RegularU.isEmpty(position)) {
                        continue;
                    }
                    if (null == tNoOld) {
                        tNoOld = "";
                    }
                    if (locateType == 0) {
                        locateType = 3;
                    }
                    //  直接上传框的有线或无线
                    if (wire == 0) {
                        model = 2;
                    } else if (wire == 1) {
                        model = 1;
                    } else {
                        model = 0;
                    }
                    if (RegularU.isEmpty(tNoOld)) {
                        terminalInfo = new TerminalInfo(tid, tNoNew, tNoOld, model, locateType, position);
                    } else {
                        terminalInfo = new TerminalInfo(tid, tNoOld, tNoNew, model, locateType, position);
                    }

                    cursorTer.close();
                }

                //  车辆信息
                Cursor cursorCar = mDatabaseManager.getCar2(carId);
                Log.i(TAG, "getOrder: cursorCar-->" + cursorCar);
                if (null != cursorCar && cursorCar.moveToFirst()) {

                    String carNo = cursorCar.getString(1);
                    String carVin = cursorCar.getString(2);
                    String carType = cursorCar.getString(3);

                    Log.i(TAG, "getOrder: carNo-->" + carNo);
                    Log.i(TAG, "getOrder: carVin-->" + carVin);
                    Log.i(TAG, "getOrder: carType-->" + carType);
                    Log.i(TAG, "getOrder: carId-->" + carId);

                    if (carInfoOutList.size() > 0) {
                        boolean had = false;
                        for (int i = 0; i < carInfoOutList.size(); i++) {
                            CarInfoOut carInfoOut2 = carInfoOutList.get(i);
                            if (carInfoOut2.getCarInfo().getCarId() == carId) {
                                had = true;
                                carInfoOut2.getCarInfo().getTerminalInfo().add(terminalInfo);
                                break;
                            }
                        }
                        if (!had) {
                            terminalInfoList.add(terminalInfo);
                            CarInfo carInfo = new CarInfo(carId, carNo, carVin, carType, terminalInfoList);
                            CarInfoOut carInfoOut = new CarInfoOut(carInfo);
                            carInfoOutList.add(carInfoOut);
                        }
                    } else {
                        terminalInfoList.add(terminalInfo);
                        CarInfo carInfo = new CarInfo(carId, carNo, carVin, carType, terminalInfoList);
                        CarInfoOut carInfoOut = new CarInfoOut(carInfo);
                        carInfoOutList.add(carInfoOut);
                    }

                    cursorCar.close();
                }

            } while (cursor.moveToNext());

            cursor.close();
        }

        //  判断是否为空单
        boolean isNullOrder = true;
        for (CarInfoOut carInfoOut : carInfoOutList) {
            for (TerminalInfo terminalInfo : carInfoOut.getCarInfo().getTerminalInfo()) {
                if (null != terminalInfo) {
                    if (terminalInfo.getModel() != 0
                            || !RegularU.isEmpty(terminalInfo.getNewImei())
                            || !RegularU.isEmpty(terminalInfo.getNewInstallPosition())) {
                        isNullOrder = false;
                    }
                }
            }
        }
        if (isNullOrder) {
            carInfoOutList.clear();
            return "";
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
                    int locateType = cursorR.getInt(10);
                    int wire = cursorR.getInt(11);

                    if (0 == locateType) {
                        locateType = 3;
                    }

                    if (null == imeiNew) {
                        imeiNew = "";
                    }
                    if (null == positionNew) {
                        positionNew = "";
                    }

                    int model;
                    if (wire == 1) {
                        model = 1;
                    } else {
                        model = 2;
                    }
                    Log.i(TAG, "getRepairJson: tid-->" + cursorR.getInt(0));
                    Log.i(TAG, "getRepairJson: imeiOld-->" + imeiOld);
                    Log.i(TAG, "getRepairJson: positionNew-->" + positionNew);
                    Log.i(TAG, "getRepairJson: explain-->" + explain);
                    Log.i(TAG, "getRepairJson: imeiNew-->" + imeiNew);
                    Log.i(TAG, "getRepairJson: model-->" + model);
                    Log.i(TAG, "getRepairJson: locateType-->" + locateType);

                    if (!RegularU.isEmpty(explain)) {
                        TerminalInfo terminalInfo = new TerminalInfo(tId, imeiOld, imeiNew, model, locateType, positionNew, explain);
                        TerminalInfoOut terminalInfoOut = new TerminalInfoOut(terminalInfo);
                        terminalInfoOurList.add(terminalInfoOut);
                    }

                    cursorR.close();
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        //  判断是否为空单
        boolean isNullOrder = true;
        for (TerminalInfoOut terminalInfoOut : terminalInfoOurList) {
            TerminalInfo terminalInfo = terminalInfoOut.getTerminalInfo();
            if (!RegularU.isEmpty(terminalInfo.getRepaireRemark())) {
                isNullOrder = false;
            }
        }
        if (isNullOrder) {
            terminalInfoOurList.clear();
            return "";
        }

        Gson gson = new Gson();
        String json = gson.toJson(terminalInfoOurList);
        Log.i(TAG, "getRepairJson: json-->" + json);
        return json;
    }

    //  运行时权限
    private void applyPermiss() {
        AndPermission
                .with(CustomSignActivity.this)
                .requestCode(100)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(CustomSignActivity.this, rationale).show();
                    }
                })
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        showLoading();
                        mLocateManager.startLocate();
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(CustomSignActivity.this, deniedPermissions)) {
                            showMessageDialog("应用权限被禁止，请打开相关权限");
                        }
                    }
                }).start();
    }

    //  显示未签名Dialog
    private void showNoSignDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_message_editable, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        TextView textView = view.findViewById(R.id.tv_dialog_message_message);
        Button button = view.findViewById(R.id.btn_dialog_message_cancel);
        textView.setText("请让客户签字！");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
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

    /*
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
                // 2017/8/22 跳转到已处理Fragment
                Intent intent = new Intent(CustomSignActivity.this, WorkerFragmentContentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Data.DATA_INTENT_WORKER_FRAGMENT, Data.DATA_INTENT_WORKER_FRAGMENT_HANDED);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    */

    //  显示部分完成Dialog
    private void showPartCompleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomSignActivity.this);
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_message_editable, null);
        builder.setView(viewDialog);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();

        TextView tvMessage = viewDialog.findViewById(R.id.tv_dialog_message_message);
        Button btnCall = viewDialog.findViewById(R.id.btn_dialog_message_cancel);

        tvMessage.setText("信息已上传，请联系后台完成订单！");
        btnCall.setText("拨打后台电话");

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                showChoicePhoneDialog();
            }
        });

        alertDialog.show();
    }

    //  显示选择电话对话框
    private void showChoicePhoneDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_choice_phone, null);
        //  选择电话
        ListView listViewChoicePhone = view.findViewById(R.id.lv_dialog_choice_phone);
        String headPhoneList = mSharedpreferenceManager.getHeadPhoneList();
        String[] headPhones = headPhoneList.split(",");
        mPhoneList = Arrays.asList(headPhones);
        ChoicePhoneAdapter choicePhoneAdapter = new ChoicePhoneAdapter(this, mPhoneList);
        listViewChoicePhone.setAdapter(choicePhoneAdapter);
        listViewChoicePhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toCall(mPhoneList.get(i));
            }
        });

        BottomDialog bottomDialog = new BottomDialog();
        bottomDialog.setContentView(view);
        bottomDialog.setCancelable(false);
        bottomDialog.show(getFragmentManager(), "ChoicePhone");
    }

    //  显示LoadingFragment
    private void showLoading() {
        mLoadingDialogFragment.show(getFragmentManager(), "LoadingFragment");
    }

    //  删除本地数据库的订单信息
    private void deleteOrderInfo() {
        Cursor cursor = mDatabaseManager.getOrder(mOrderNo);
        if (null != cursor && cursor.moveToFirst()) {
            do {
                String orderNo = cursor.getString(0);
                int carId = cursor.getInt(1);
                int tId = cursor.getInt(2);
                Log.i(TAG, "deleteOrderInfo: orderNo-->" + orderNo + " ,carId-->" + carId + " ,tId-->" + tId);
//                mDatabaseManager.deleteTerByTid(tId);
                mDatabaseManager.deleteTer2ByTid(tId);
//                mDatabaseManager.deleteCar(tId);
                mDatabaseManager.deleteCar2(carId);
                mDatabaseManager.deleteRepair(tId);

            } while (cursor.moveToNext());

            cursor.close();
        }
        mDatabaseManager.deleteOrder(mOrderNo);
    }

    //  拨打电话
    private void toCall(String number) {
        //  跳转到进行中
        Intent intent = new Intent(CustomSignActivity.this, WorkerFragmentContentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Data.DATA_INTENT_WORKER_FRAGMENT, Data.DATA_INTENT_WORKER_FRAGMENT_HANDING);

        // 2017/8/16 拨打后台电话，即总部电话
        Intent intentCall = new Intent();
        intentCall.setAction(Intent.ACTION_DIAL);
        intentCall.setData(Uri.parse("tel:" + number));

        startActivities(new Intent[]{intent, intentCall});
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mLoadingDialogFragment.isAdded()) {
                mLoadingDialogFragment.dismissAllowingStateLoss();
            }

            switch (msg.what) {
                case Data.MSG_ERO: {
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    //  完成
                    deleteOrderInfo();
                    mFileManager.delete();
                    // 2017/8/22 跳转到已处理Fragment
                    Intent intent = new Intent(CustomSignActivity.this, WorkerFragmentContentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Data.DATA_INTENT_WORKER_FRAGMENT, Data.DATA_INTENT_WORKER_FRAGMENT_HANDED);
                    startActivity(intent);
                    finish();
                    break;
                }
                case Data.MSG_2: {
                    //  部分完成
                    deleteOrderInfo();
                    mFileManager.delete();
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
