package com.tianyigps.dispatch2.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.bean.CheckImeiBean;
import com.tianyigps.dispatch2.bean.DeletePicBean;
import com.tianyigps.dispatch2.bean.LastInstallerBean;
import com.tianyigps.dispatch2.bean.StartOrderInfoBean;
import com.tianyigps.dispatch2.bean.UploadPicBean;
import com.tianyigps.dispatch2.bean.WholeImeiBean;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnCheckIMEIListener;
import com.tianyigps.dispatch2.interfaces.OnDeletePicListener;
import com.tianyigps.dispatch2.interfaces.OnGetLastInstallerListener;
import com.tianyigps.dispatch2.interfaces.OnGetWholeIMEIListener;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderInfoStartListener;
import com.tianyigps.dispatch2.interfaces.OnUploadPicListener;
import com.tianyigps.dispatch2.manager.DatabaseManager;
import com.tianyigps.dispatch2.manager.FileManager;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.MessageDialogU;
import com.tianyigps.dispatch2.utils.RegularU;
import com.tianyigps.dispatch2.utils.TinyU;
import com.tianyigps.dispatch2.utils.UploadPicU;
import com.tianyigps.dispatch2.utils.Uri2FileU;
import com.yundian.bottomdialog.BottomDialog;
import com.zxy.tiny.callback.FileCallback;

import static com.tianyigps.dispatch2.data.Data.DATA_UPLOAD_TYPE_3;

public class OperateRepairActivity extends BaseActivity {

    private static final String TAG = "OperateRepairActivity";

    private static final int INTENT_CHOICE_P = 1;
    private static final int INTENT_PHOTO_P = 2;
    private static final int INTENT_CHOICE_I = 3;
    private static final int INTENT_PHOTO_I = 4;
    private int picType = 1;   //  1 = 安装位置，3 = 接线图

    private ImageView mImageViewLocate, mImageViewPositionOld, mImageViewInstallOld;
    private ImageView mImageViewPositionNew, mImageViewInstallNew, mImageViewPositionNewDelete, mImageViewInstallNewDelete;
    private TextView mTextViewCarNo, mTextViewFrameNo, mTextViewTypeAndName, mTextViewtNo, mTextViewPositionOld,
            mTextViewInstallName, mTextViewInstallPhone, mTextViewDescribe, mTextViewCount;
    private EditText mEditTextPosition, mEditTextExplain;
    private Button mButtonSave;

    private RelativeLayout mRelativeLayoutReplace, mRelativeLayoutInstall, mRelativeLayoutInstallOld;
    private View mViewLineInstall;
    private TextView mTextViewState;
    private ImageView mImageViewScanner, mImageViewReplaceLocate;
    private TextView mTextViewNewDeviceTitle;
    private EditText mEditTextNewImei;

    private DatabaseManager mDatabaseManager;
//    private String mStringPosition, mStringPositionPath, mStringInstallPath, mStringExplain, mStringNewImei;

    private Uri mUriPhoto;
    private String fileTempName;

    //  网络数据请求
    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private SharedpreferenceManager mSharedpreferenceManager;
    private int eid;
    private String token;
    private String userName;
    private String orderNo;
    private String tNo;
    private String baseUrl;
    private String mStringMessage = "数据请求失败，请检查网络！";
    private String wholeImei;
    private int tId;
    private int carId;

    //  网络数据返回
    private String carNoG, frameNoG, typeAndNameG, positionG, positionPicG, installPicG, installNameG, installPhoneG;
    private String mDescribe;
    private String mPositionNew, mPositionPicNew, mInstallPicNew, mPositionPicUrlNew, mInstallPicUrlNew, mExplainNew, mImeiNew;
    private int mOrderTerType;
    private String mImeiOld;
    private String mLastInstaller, mLastPhoneNo;
    private String mRepaireDesc;
    private String mChangeFlag = "0";

    //  LoadingFragment
    private LoadingDialogFragment mLoadingDialogFragment;

    //  压缩图片temp
    private int mTempType;
    private String mTempImgUrl;

    //  删除图片，判定是删除的哪张图片，0=DATA_UPLOAD_TYPE_3, 1 = DATA_UPLOAD_TYPE_4
    private int mPicPosition = Data.DATA_UPLOAD_TYPE_3;

    //  输入提示文字
    private TextView mTextViewTip0, mTextViewTip1, mTextViewTip2, mTextViewTip3;

    //  imei号是否校验过
    private boolean isCheckedImei = true;
    private boolean isToLocate = false;
    private boolean isImeiEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_repair);

        init();

        setEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Data.DATA_INTENT_SCANNER_REQUEST && resultCode == Data.DATA_INTENT_SCANNER_RESULT) {
            Log.i(TAG, "onActivityResult: qrcode-->" + data.getStringExtra(Data.DATA_SCANNER));
            isCheckedImei = false;
            String imei = data.getStringExtra(Data.DATA_SCANNER);
            setEditTextImei(imei);
            showLoading();
            mNetworkManager.checkIMEI(eid, token, imei, mOrderTerType, orderNo, userName, mImeiOld);
        }

        if (requestCode == Data.DATA_INTENT_LOCATE_REQUEST && resultCode == Data.DATA_INTENT_LOCATE_RESULT) {
            int locateType = data.getIntExtra(Data.DATA_LOCATE_TYPE, 3);
            int model = data.getIntExtra(Data.DATA_LOCATE_MODEL, 0);
            Log.i(TAG, "onActivityResult: locateType-->" + locateType);
            Log.i(TAG, "onActivityResult: model-->" + model);
            mDatabaseManager.addRepairLocateType(tId, locateType);
            mDatabaseManager.addRepair(tId, model);

            if (mTextViewTip0.getText().toString().equals(getString(R.string.tip_locate))) {
                mTextViewTip0.setVisibility(View.GONE);
                mImageViewReplaceLocate.setBackgroundResource(R.color.colorNull);
            }
        }

        if (RESULT_OK != resultCode) {
            if (null != fileTempName) {
                new FileManager(fileTempName).delete();
            }
            return;
        }
        switch (requestCode) {
            case INTENT_CHOICE_P: {
                Uri selectedImage = data.getData();

                String path = new Uri2FileU(OperateRepairActivity.this).getRealPathFromUri(selectedImage);
                Log.i(TAG, "onActivityResult: path-->" + path);
                uploadPic(DATA_UPLOAD_TYPE_3, mPositionPicUrlNew, path);
                break;
            }
            case INTENT_PHOTO_P: {
                Uri uri = null;
                if (data != null && data.getData() != null) {
                    uri = data.getData();
                }
                if (uri == null) {
                    if (mUriPhoto != null) {
                        uri = mUriPhoto;
                    }
                }

                String path = new Uri2FileU(OperateRepairActivity.this).getRealPathFromUri(uri);
                Log.i(TAG, "onActivityResult: path-->" + path);
                uploadPic(DATA_UPLOAD_TYPE_3, mPositionPicUrlNew, path);
                break;
            }
            case INTENT_CHOICE_I: {
                Uri selectedImage = data.getData();

                String path = new Uri2FileU(OperateRepairActivity.this).getRealPathFromUri(selectedImage);
                Log.i(TAG, "onActivityResult: path-->" + path);

                uploadPic(Data.DATA_UPLOAD_TYPE_4, mInstallPicUrlNew, path);
                break;
            }
            case INTENT_PHOTO_I: {
                Uri uri = null;
                if (data != null && data.getData() != null) {
                    uri = data.getData();
                }
                if (uri == null) {
                    if (mUriPhoto != null) {
                        uri = mUriPhoto;
                    }
                }
                String path = new Uri2FileU(OperateRepairActivity.this).getRealPathFromUri(uri);
                Log.i(TAG, "onActivityResult: path-->" + path);
                uploadPic(Data.DATA_UPLOAD_TYPE_4, mInstallPicUrlNew, path);
                break;
            }
            default: {
                Log.i(TAG, "onActivityResult: default");
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                v.setFocusable(false);
                v.setFocusableInTouchMode(true);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseManager.close();
    }

    private void init() {
        this.setTitleText("维修");

        mSharedpreferenceManager = new SharedpreferenceManager(this);
        mLoadingDialogFragment = new LoadingDialogFragment();

        Intent intent = getIntent();
        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();
        orderNo = intent.getStringExtra(Data.DATA_INTENT_ORDER_NO);
        tNo = intent.getStringExtra(Data.DATA_INTENT_T_NO);
        tId = intent.getIntExtra(Data.DATA_INTENT_T_ID, 0);
        carId = intent.getIntExtra(Data.DATA_INTENT_CAR_ID, 0);
        Log.i(TAG, "init: carId-->" + carId);

        mImageViewLocate = findViewById(R.id.iv_activity_operate_default_locate);
        mImageViewPositionOld = findViewById(R.id.iv_activity_operate_default_position_pic);
        mImageViewInstallOld = findViewById(R.id.iv_activity_operate_default_install_pic);
        mImageViewPositionNew = findViewById(R.id.iv_activity_operate_default_position_pic_new);
        mImageViewInstallNew = findViewById(R.id.iv_activity_operate_default_install_pic_new);
        mImageViewPositionNewDelete = findViewById(R.id.iv_activity_operate_default_position_pic_new_delete);
        mImageViewInstallNewDelete = findViewById(R.id.iv_activity_operate_default_install_pic_new_delete);

        mTextViewCarNo = findViewById(R.id.tv_activity_operate_default_car_no);
        mTextViewFrameNo = findViewById(R.id.tv_activity_operate_default_frame_no);
        mTextViewTypeAndName = findViewById(R.id.tv_activity_operate_default_device_type_and_name);
        mTextViewtNo = findViewById(R.id.tv_activity_operate_default_device_t_no);
        mTextViewPositionOld = findViewById(R.id.tv_activity_operate_default_position);
        mTextViewInstallName = findViewById(R.id.tv_activity_operate_default_worker);
        mTextViewInstallPhone = findViewById(R.id.tv_activity_operate_default_worker_phone);
        mTextViewDescribe = findViewById(R.id.tv_activity_operate_fault_describe);
        mTextViewCount = findViewById(R.id.tv_activity_operate_default_install_explain_count);

        mEditTextPosition = findViewById(R.id.et_activity_operate_default_position_new);
        mEditTextExplain = findViewById(R.id.et_activity_operate_default_install_explain);
        mTextViewTip0 = findViewById(R.id.tv_activity_operate_repair_tip_0);
        mTextViewTip1 = findViewById(R.id.tv_activity_operate_repair_tip_1);
        mTextViewTip2 = findViewById(R.id.tv_activity_operate_repair_tip_2);
        mTextViewTip3 = findViewById(R.id.tv_activity_operate_repair_tip_3);

        mButtonSave = findViewById(R.id.btn_activity_operate_repair_save);

        mTextViewState = findViewById(R.id.tv_activity_operate_remove_state);
        mRelativeLayoutReplace = findViewById(R.id.rl_activity_operate_replace);
        mRelativeLayoutInstallOld = findViewById(R.id.rl_activity_operate_default_install);
        mRelativeLayoutInstall = findViewById(R.id.rl_activity_operate_default_install_new);
        mViewLineInstall = findViewById(R.id.view_activity_operate_default_line_4);
        mImageViewScanner = findViewById(R.id.iv_activity_operate_replace_scanner);
        mImageViewReplaceLocate = findViewById(R.id.iv_activity_operate_replace_locate);
        mTextViewNewDeviceTitle = findViewById(R.id.tv_activity_operate_replace_device_no_title);
        mEditTextNewImei = findViewById(R.id.et_activity_operate_replace_device_no);

        mDatabaseManager = new DatabaseManager(OperateRepairActivity.this);

//        loadSavedData();

        Log.i(TAG, "init: tNo-->" + tNo);
        mTextViewFrameNo.setText(tNo);

        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();
        baseUrl = mSharedpreferenceManager.getImageBaseUrl();

        showLoading();
        mNetworkManager.getWorkerOrderInfoStart(eid, token, orderNo, userName);

        mNetworkManager.getLastInstaller(eid, token, tNo, userName);
    }

    private void setEventListener() {

        this.setOnTitleBackClickListener(new OnTitleBackClickListener() {
            @Override
            public void onClick() {
                OperateRepairActivity.this.onBackPressed();
            }
        });

        mImageViewLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/26 定位
                Intent intent = new Intent(OperateRepairActivity.this, LocateActivity.class);
                intent.putExtra(Data.DATA_INTENT_LOCATE_TYPE, false);
                intent.putExtra(Data.DATA_INTENT_LOCATE_IMEI, tNo);
                startActivity(intent);
            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/26 保存
                saveData();
            }
        });

        mImageViewPositionOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RegularU.isEmpty(positionPicG) || (baseUrl + "null").equals(positionPicG)) {
                    return;
                }
                Intent intent = new Intent(OperateRepairActivity.this, ShowPicActivity.class);
                intent.putExtra("URL", positionPicG);
                startActivity(intent);
            }
        });

        mImageViewInstallOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RegularU.isEmpty(installPicG) || (baseUrl + "null").equals(installPicG)) {
                    return;
                }
                Intent intent = new Intent(OperateRepairActivity.this, ShowPicActivity.class);
                intent.putExtra("URL", installPicG);
                startActivity(intent);
            }
        });

        mImageViewPositionNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/26 选择图片或拍照
                picType = 1;
                mPicPosition = Data.DATA_UPLOAD_TYPE_3;
                showChoiceDialog();
            }
        });

        mImageViewPositionNewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/8/17 删除图片
                mPicPosition = DATA_UPLOAD_TYPE_3;
                removePic(DATA_UPLOAD_TYPE_3, mPositionPicUrlNew);
            }
        });

        mImageViewInstallNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/26 选择图片或拍照
                picType = 3;
                mPicPosition = Data.DATA_UPLOAD_TYPE_4;
                showChoiceDialog();
            }
        });

        mImageViewInstallNewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/8/17 删除图片
                mPicPosition = Data.DATA_UPLOAD_TYPE_4;
                removePic(Data.DATA_UPLOAD_TYPE_4, mInstallPicUrlNew);
            }
        });

        mTextViewState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRelativeLayoutReplace.getVisibility() == View.GONE) {
                    mRelativeLayoutReplace.setVisibility(View.VISIBLE);
                    mTextViewState.setText(R.string.not_replace);
                } else {
                    mRelativeLayoutReplace.setVisibility(View.GONE);
                    mTextViewState.setText(R.string.repair_replace);
                }
            }
        });

        mImageViewScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OperateRepairActivity.this, ScannerActivity.class);
                startActivityForResult(intent, Data.DATA_INTENT_SCANNER_REQUEST);
            }
        });

        mEditTextNewImei.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                myHandler.removeMessages(Data.MSG_11);
                myHandler.sendEmptyMessageDelayed(Data.MSG_11, 200);
            }
        });

        mEditTextNewImei.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                isImeiEdit = true;
                if (!focus) {
                    //  丢失焦点
                    String imei = mEditTextNewImei.getText().toString();
                    if (RegularU.isEmpty(imei)) {
                        //  imei为空
                        Log.i(TAG, "onFocusChange: imei为空");
                    } else if (imei.length() < 8) {
                        mStringMessage = "设备号不为唯一，请输入更多位数";
                        myHandler.sendEmptyMessage(Data.MSG_8);
                    } else {
                        isCheckedImei = false;
                        getWholeImei(imei);
                    }
                }
            }
        });

        mImageViewReplaceLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/8/30 定位
                String imei = mEditTextNewImei.getText().toString();
                if (RegularU.isEmpty(imei)) {
                    return;
                }
                if (isCheckedImei) {
                    toLocate(wholeImei);
                } else {
                    isToLocate = true;
                    showLoading();
                    mNetworkManager.checkIMEI(eid, token, imei, mOrderTerType, orderNo, userName, mImeiOld);
                }
            }
        });

        mEditTextExplain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String count = editable.length() + "/200";
                mTextViewCount.setText(count);
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
                Gson gson = new Gson();
                StartOrderInfoBean startOrderInfoBean = gson.fromJson(result, StartOrderInfoBean.class);

                if (!startOrderInfoBean.isSuccess()) {
                    mStringMessage = startOrderInfoBean.getMsg();
                    onFailure();
                    return;
                }
                StartOrderInfoBean.ObjBean objBean = startOrderInfoBean.getObj();
                for (StartOrderInfoBean.ObjBean.CarListBean carListBean : objBean.getCarList()) {
                    for (StartOrderInfoBean.ObjBean.CarListBean.CarTerminalListBean carTerminalListBean
                            : carListBean.getCarTerminalList()) {
                        Log.i(TAG, "onSuccess: tId-1->" + tId);
                        if (tId != carTerminalListBean.getId()) {
                            continue;
                        }
                        Log.i(TAG, "onSuccess: tId-2->" + carTerminalListBean.getId());
                        if (tNo.equals(carTerminalListBean.getTNo())) {
                            carNoG = carListBean.getCarNo();
                            frameNoG = carListBean.getCarVin();

                            mImeiOld = carTerminalListBean.getTNo();
                            mOrderTerType = carTerminalListBean.getTerminalType();
                            String terminalType;
                            String terminalName = carTerminalListBean.getTerminalName();
                            if (null == terminalName) {
                                terminalName = "";
                            } else {
                                terminalName = "（" + terminalName + "）";
                            }
                            switch (mOrderTerType) {
                                case 1: {
                                    terminalType = "有线";
                                    break;
                                }
                                case 2: {
                                    terminalType = "无线";
                                    break;
                                }
                                default: {
                                    terminalType = "";
                                    Log.i(TAG, "onSuccess: default.type-->" + mOrderTerType);
                                }
                            }
                            typeAndNameG = terminalType + terminalName;
                            positionG = carTerminalListBean.getInstallLocation();
                            positionPicG = baseUrl + carTerminalListBean.getInstallLocationPic();
                            installPicG = baseUrl + carTerminalListBean.getWiringDiagramPic();
//                            installNameG = objBean.getDispatchContactName();
//                            installPhoneG = objBean.getDispatchContactPhone();
                            mDescribe = carTerminalListBean.getMalDesc();
                            mRepaireDesc = carTerminalListBean.getRepaireDesc();

                            mPositionNew = carTerminalListBean.getNewInstallLocation();

                            mPositionPicUrlNew = carTerminalListBean.getNewInstallLocationPic();
                            mInstallPicUrlNew = carTerminalListBean.getNewWiringDiagramPic();
                            mPositionPicNew = baseUrl + carTerminalListBean.getNewInstallLocationPic();
                            mInstallPicNew = baseUrl + carTerminalListBean.getNewWiringDiagramPic();

                            mDatabaseManager.addRepairPositionPic(tId, mPositionPicNew, mPositionPicUrlNew);
                            mDatabaseManager.addRepairInstallPic(tId, mInstallPicNew, mInstallPicUrlNew);

                            myHandler.sendEmptyMessage(Data.MSG_1);
                            return;
                        }
                    }
                }

            }
        });

        mNetworkManager.setOnGetLastInstallerListener(new OnGetLastInstallerListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(Data.MSG_5);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                LastInstallerBean lastInstallerBean = gson.fromJson(result, LastInstallerBean.class);
                if (!lastInstallerBean.isSuccess()) {
                    return;
                }
                LastInstallerBean.ObjBean objBean = lastInstallerBean.getObj();
                if (null == objBean) {
                    return;
                }
                mLastInstaller = objBean.getName();
                mLastPhoneNo = objBean.getPhoneNo();
                if (RegularU.isEmpty(objBean.getEndDate())) {
                    mChangeFlag = "1";
                } else {
                    mChangeFlag = "0";
                }

                myHandler.sendEmptyMessage(Data.MSG_4);
            }
        });

        mNetworkManager.setOnGetWholeIMEIListener(new OnGetWholeIMEIListener() {
            @Override
            public void onFailure() {
                myHandler.sendEmptyMessage(Data.MSG_3);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                WholeImeiBean wholeImeiBean = gson.fromJson(result, WholeImeiBean.class);
                if (!wholeImeiBean.isSuccess()) {
                    mStringMessage = wholeImeiBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_3);
                    myHandler.sendEmptyMessage(Data.MSG_12);
                    return;
                }
                wholeImei = wholeImeiBean.getObj().getImei();
                myHandler.sendEmptyMessage(Data.MSG_10);
            }
        });

        mNetworkManager.setCheckIMEIListener(new OnCheckIMEIListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_3);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                CheckImeiBean checkImeiBean = gson.fromJson(result, CheckImeiBean.class);
                mStringMessage = checkImeiBean.getMsg();
                if (!checkImeiBean.isSuccess()) {
                    myHandler.sendEmptyMessage(Data.MSG_8);
                    return;
                }
                CheckImeiBean.ObjBean objBean = checkImeiBean.getObj();
                if (null == objBean) {
                    myHandler.sendEmptyMessage(Data.MSG_3);
                    return;
                }
                mChangeFlag = objBean.getChangeFlag();
                wholeImei = objBean.getImei();
                myHandler.sendEmptyMessage(Data.MSG_2);
            }
        });

        mNetworkManager.setOnUploadPicListener(new OnUploadPicListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: uploadpic");
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_3);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                UploadPicBean uploadPicBean = gson.fromJson(result, UploadPicBean.class);
                if (!uploadPicBean.isSuccess()) {
                    onFailure();
                    return;
                }
                UploadPicBean.ObjBean objBean = uploadPicBean.getObj();
                String imgUrl = objBean.getImgUrl();
                if (picType < 3) {
                    mDatabaseManager.addRepairPositionUrl(tId, imgUrl);
                    mPositionPicUrlNew = imgUrl;
                    mPositionPicNew = baseUrl + mPositionPicUrlNew;
                } else {
                    mDatabaseManager.addRepairInstallUrl(tId, imgUrl);
                    mInstallPicUrlNew = imgUrl;
                    mInstallPicNew = baseUrl + mInstallPicUrlNew;
                }

                myHandler.sendEmptyMessage(Data.MSG_7);
            }
        });

        mNetworkManager.setOnDeletePicListener(new OnDeletePicListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_3);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                DeletePicBean deletePicBean = gson.fromJson(result, DeletePicBean.class);
                mStringMessage = deletePicBean.getMsg();
                if (!deletePicBean.isSuccess()) {
                    myHandler.sendEmptyMessage(Data.MSG_3);
                    return;
                }

                myHandler.sendEmptyMessage(Data.MSG_6);
            }
        });
    }

    private void toChoicePic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        startActivityForResult(intent, picType);
    }

    private void toPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues contentValues = new ContentValues(1);
        FileManager fileManager = new FileManager(FileManager.TYPE_PNG);
        fileTempName = fileManager.getPath();
        contentValues.put(MediaStore.Images.Media.DATA, fileTempName);
        mUriPhoto = OperateRepairActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhoto);
        startActivityForResult(intent, picType + 1);
    }

    private void showChoiceDialog() {
        final BottomDialog bottomDialog = new BottomDialog();
        View viewDialog = LayoutInflater.from(OperateRepairActivity.this).inflate(R.layout.dialog_choice_pic, null);
        TextView choice = viewDialog.findViewById(R.id.tv_dialog_choice_pic);
        TextView photo = viewDialog.findViewById(R.id.tv_dialog_choice_pic_photo);

        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toChoicePic();
                bottomDialog.dismiss();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPhoto();
                bottomDialog.dismiss();
            }
        });
        bottomDialog.setContentView(viewDialog);
        bottomDialog.show(getFragmentManager(), "ChoicePic");
    }

    private void showFinishDialog(String msg) {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(OperateRepairActivity.this);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OperateRepairActivity.this.finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showMessageDialog(String msg) {
        if (isFinishing()) {
            return;
        }
        new MessageDialogU(this).show(msg);
    }

    private void saveData() {
        // 2017/8/14 保存数据
        mPositionNew = mEditTextPosition.getText().toString();
        mExplainNew = mEditTextExplain.getText().toString();
        mImeiNew = mEditTextNewImei.getText().toString();
        Log.i(TAG, "onSignClick: mStringNewImei-->" + mImeiNew);
        if ("".equals(mPositionNew)) {
            mPositionNew = null;
        }
        if ("".equals(mExplainNew)) {
            mExplainNew = null;
        }
        if (mRelativeLayoutReplace.getVisibility() == View.GONE) {
            mDatabaseManager.addRepair(tId, mPositionNew, mExplainNew, null);
            mDatabaseManager.addRepair(tId, 0);
            mDatabaseManager.addRepairReplace(tId, 0);
        } else {
            mDatabaseManager.addRepair(tId, mPositionNew, mExplainNew, mImeiNew);
            mDatabaseManager.addRepairReplace(tId, 1);
        }

        if (isComplete()) {
            this.onBackPressed();
        }
    }

    private void loadSavedData() {
        Cursor cursor = mDatabaseManager.getRepair(tId);
        if (null != cursor && cursor.moveToFirst()) {
            String positionNew = cursor.getString(2);
            String explainNew = cursor.getString(5);
            mImeiNew = cursor.getString(6);
            int replace = cursor.getInt(12);
            wholeImei = mImeiNew;

            Log.i(TAG, "loadSavedData: tId-->" + cursor.getInt(0));
            Log.i(TAG, "loadSavedData: mStringPosition-->" + mPositionNew);
            Log.i(TAG, "loadSavedData: mStringExplain-->" + mExplainNew);
            Log.i(TAG, "loadSavedData: mStringNewImei-->" + mImeiNew);
            Log.i(TAG, "loadSavedData: replace-->" + replace);

            if (!RegularU.isEmpty(explainNew)) {
                mEditTextExplain.setText(explainNew);
            } else {
                mEditTextExplain.setText(mRepaireDesc);
            }
            if (!RegularU.isEmpty(positionNew)) {
                mEditTextPosition.setText(positionNew);
            } else {
                mEditTextPosition.setText(mPositionNew);
            }

            if (0 == replace) {
                mRelativeLayoutReplace.setVisibility(View.GONE);
                mTextViewState.setText(R.string.repair_replace);
            } else {
                mRelativeLayoutReplace.setVisibility(View.VISIBLE);
                setEditTextImei(mImeiNew);
                mTextViewState.setText(R.string.not_replace);
            }
            cursor.close();
        }
    }

    //  删除图片
    private void removePic(int type, String url) {
        showDeletePicDialog(tId, type, url);
    }

    //  上传图片
    private void uploadPic(int type, String imgUrl, String path) {
        //  压缩图片
        if (null == imgUrl) {
            imgUrl = "";
        }
        final String localPath = path;
        mTempType = type;
        mTempImgUrl = imgUrl;
        showLoading();
        TinyU.tinyPic(path, new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile) {
                //  上传
                if (isSuccess) {
                    new UploadPicU(mNetworkManager).uploadPic(eid, token, orderNo, carId, tId, mTempType, mOrderTerType, mTempImgUrl, outfile, userName);
                } else {
                    FileManager fileManager = new FileManager(localPath, true);
                    if (fileManager.getFileSize() > 5) {
                        mStringMessage = "选择图片过大，请重新上传！";
                        myHandler.sendEmptyMessage(Data.MSG_3);
                    } else {
                        new UploadPicU(mNetworkManager).uploadPic(eid, token, orderNo, carId, tId, mTempType, mOrderTerType, mTempImgUrl, localPath, userName);
                    }
                }
            }
        });
    }

    //  获取完整imei
    private void getWholeImei(String imei) {
        showLoading();
        mNetworkManager.getWholeImei(eid, token, imei, userName);
    }

    //  删除图片确认框
    private void showDeletePicDialog(final int tid, final int type, final String url) {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_button_editable, null);
        Button ensure = view.findViewById(R.id.btn_dialog_editable_ensure);
        Button cancle = view.findViewById(R.id.btn_dialog_editable_cancel);
        TextView textView = view.findViewById(R.id.tv_dialog_editable_msg);
        textView.setText("是否删除图片");
        builder.setView(view);
        final AlertDialog dialog = builder.create();

        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
                mNetworkManager.deletePic(eid, token, orderNo, carId, tid, type, url, userName);
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

    //  显示imei
    private void setEditTextImei(String imei) {
        mEditTextNewImei.setText(imei);
    }

    private void toLocate(String imei) {
        if (RegularU.isEmpty(imei)) {
            showMessageDialog("IMEI号不可用！");
            return;
        }
        Intent intent = new Intent(OperateRepairActivity.this, LocateActivity.class);
        intent.putExtra(Data.DATA_INTENT_LOCATE_TYPE, false);
        intent.putExtra(Data.DATA_INTENT_LOCATE_IMEI, imei);
        startActivityForResult(intent, Data.DATA_INTENT_LOCATE_REQUEST);
    }

    //  检测数据完整是否完整
    private boolean isComplete() {
        boolean complete = true;

        Cursor cursor = mDatabaseManager.getRepair(tId);
        if (null != cursor && cursor.moveToFirst()) {

            int idMain = cursor.getInt(0);
            String tNo = cursor.getString(1);
            String position = cursor.getString(2);
            String positionPic = cursor.getString(3);
            String installPic = cursor.getString(4);
            String explain = cursor.getString(5);
            String newTNo = cursor.getString(6);
            String positionUrl = cursor.getString(7);
            String installUrl = cursor.getString(8);
            int model = cursor.getInt(9);
            int locateType = cursor.getInt(10);
            int replace = cursor.getInt(12);

            Log.i(TAG, "isComplete: idMain-->" + idMain);
            Log.i(TAG, "isComplete: tNo-->" + tNo);
            Log.i(TAG, "isComplete: position-->" + position);
            Log.i(TAG, "isComplete: positionPic-->" + positionPic);
            Log.i(TAG, "isComplete: installPic-->" + installPic);
            Log.i(TAG, "isComplete: explain-->" + explain);
            Log.i(TAG, "isComplete: newTNo-->" + newTNo);
            Log.i(TAG, "isComplete: positionUrl-->" + positionUrl);
            Log.i(TAG, "isComplete: installUrl-->" + installUrl);
            Log.i(TAG, "isComplete: model-->" + model);
            Log.i(TAG, "isComplete: locateType-->" + locateType);
            Log.i(TAG, "isComplete: replace-->" + replace);

            if (null == explain || "".equals(explain)) {
                complete = false;
                mTextViewTip3.setVisibility(View.VISIBLE);
            } else {
                mTextViewTip3.setVisibility(View.INVISIBLE);
            }

            if (mRelativeLayoutReplace.getVisibility() == View.VISIBLE) {
                if (RegularU.isEmpty(newTNo)) {
                    complete = false;
                    mTextViewTip0.setText(getString(R.string.tip_imei));
                    mTextViewTip0.setVisibility(View.VISIBLE);
                } else {
                    mTextViewTip0.setVisibility(View.GONE);

                    if (0 == model) {
                        complete = false;
                        mImageViewReplaceLocate.setBackgroundResource(R.drawable.bg_edit_orange);
                        mTextViewTip0.setText(getString(R.string.tip_locate));
                        mTextViewTip0.setVisibility(View.VISIBLE);
                    } else {
                        mImageViewReplaceLocate.setBackgroundResource(R.color.colorNull);
                        mTextViewTip0.setVisibility(View.GONE);
                    }
                }
                if (null == position || "".equals(position)) {
                    complete = false;
                    mTextViewTip1.setText(getString(R.string.tip_position));
                    mTextViewTip1.setVisibility(View.VISIBLE);
                } else {
                    mTextViewTip1.setVisibility(View.INVISIBLE);
                }
                if (null == positionUrl || "".equals(positionUrl)) {
                    complete = false;
                    if (mTextViewTip1.getVisibility() != View.VISIBLE) {
                        mTextViewTip1.setText(getString(R.string.tip_pic));
                        mTextViewTip1.setVisibility(View.VISIBLE);
                        mImageViewPositionNew.setBackgroundResource(R.drawable.bg_edit_orange);
                    }
                } else {
                    mTextViewTip1.setVisibility(View.INVISIBLE);
                    mImageViewPositionNew.setBackgroundResource(R.color.colorNull);
                }

                if (mOrderTerType == 1) {
                    if (null == installUrl || "".equals(installUrl)) {
                        complete = false;
                        mTextViewTip2.setVisibility(View.VISIBLE);
                        mImageViewInstallNew.setBackgroundResource(R.drawable.bg_edit_orange);
                    } else {
                        mTextViewTip2.setVisibility(View.INVISIBLE);
                        mImageViewInstallNew.setBackgroundResource(R.color.colorNull);
                    }
                }
            }

            if (!RegularU.isEmpty(position) || !RegularU.isEmpty(positionUrl) || !RegularU.isEmpty(installUrl)) {
                if (RegularU.isEmpty(position)) {
                    complete = false;
                    mTextViewTip1.setText(getString(R.string.tip_position));
                    mTextViewTip1.setVisibility(View.VISIBLE);
                }
                if (RegularU.isEmpty(positionUrl)) {
                    complete = false;
                    mTextViewTip1.setVisibility(View.VISIBLE);
                    mTextViewTip1.setText(getString(R.string.tip_pic));
                    mImageViewPositionNew.setBackgroundResource(R.drawable.bg_edit_orange);
                } else {
                    mImageViewPositionNew.setBackgroundResource(R.color.colorNull);
                }
                if (!RegularU.isEmpty(position) && !RegularU.isEmpty(positionUrl)) {
                    mTextViewTip1.setVisibility(View.INVISIBLE);
                }

                if (mOrderTerType == 1) {
                    if (mRelativeLayoutReplace.getVisibility() == View.VISIBLE) {
                        if (null == installUrl || "".equals(installUrl)) {
                            complete = false;
                            mTextViewTip2.setVisibility(View.VISIBLE);
                            mImageViewInstallNew.setBackgroundResource(R.drawable.bg_edit_orange);
                        } else {
                            mTextViewTip2.setVisibility(View.INVISIBLE);
                            mImageViewInstallNew.setBackgroundResource(R.color.colorNull);
                        }
                    }
                }
            }
            cursor.close();
        }

        return complete;
    }

    //  显示imei重复填写对话框
    private void showRepeatDialog() {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_message_editable, null);
        Button button = view.findViewById(R.id.btn_dialog_message_cancel);
        TextView textView = view.findViewById(R.id.tv_dialog_message_message);
        textView.setText("设备名称填写重复，请重新填写");
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //  显示LoadingFragment
    private void showLoading() {
        if (!mLoadingDialogFragment.isAdded()) {
            mLoadingDialogFragment.show(getFragmentManager(), "LoadingFragment");
        }
    }

    //  校验该订单中是否填有该imei
    private boolean isExistImei(String imei) {
        Cursor cursor = mDatabaseManager.getOrder(orderNo);
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                do {
                    int tId = cursor.getInt(2);

                    Cursor cursorTer = mDatabaseManager.getRepair(tId);
                    if (null != cursorTer) {
                        if (cursorTer.moveToFirst()) {
                            do {
                                String tNo = cursorTer.getString(1);

                                if (imei.equals(tNo)) {
                                    return true;
                                }
                            } while (cursorTer.moveToNext());
                        }
                        cursorTer.close();
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return false;
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "handleMessage: msg.what-->" + msg.what);
            if (mLoadingDialogFragment.isAdded()) {
                mLoadingDialogFragment.dismissAllowingStateLoss();
            }

            switch (msg.what) {
                case Data.MSG_ERO: {
                    showFinishDialog(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    mTextViewCarNo.setText(carNoG);
                    mTextViewFrameNo.setText(frameNoG);
                    mTextViewTypeAndName.setText(typeAndNameG);
                    mTextViewPositionOld.setText(positionG);
                    mTextViewtNo.setText(mImeiOld);

                    if (mOrderTerType == 1) {
                        mTextViewNewDeviceTitle.setText("新有线设备号");
                        mDatabaseManager.addRepairWire(tId, mOrderTerType);
                        mRelativeLayoutInstall.setVisibility(View.VISIBLE);
                        mViewLineInstall.setVisibility(View.VISIBLE);
                        mRelativeLayoutInstallOld.setVisibility(View.VISIBLE);
                    } else {
                        mDatabaseManager.addRepairWire(tId, 0);
                        mTextViewNewDeviceTitle.setText("新无线设备号");
                        mRelativeLayoutInstall.setVisibility(View.GONE);
                        mViewLineInstall.setVisibility(View.GONE);
                        mRelativeLayoutInstallOld.setVisibility(View.GONE);
                    }
//                    mTextViewInstallName.setText(installNameG);
//                    mTextViewInstallPhone.setText(installPhoneG);
                    mTextViewDescribe.setText(mDescribe);
                    Picasso.with(OperateRepairActivity.this)
                            .load(positionPicG)
                            .error(R.color.colorNull)
                            .fit()
                            .centerInside()
                            .into(mImageViewPositionOld);
                    Picasso.with(OperateRepairActivity.this)
                            .load(installPicG)
                            .error(R.color.colorNull)
                            .fit()
                            .centerInside()
                            .into(mImageViewInstallOld);
                    Log.i(TAG, "handleMessage: mPositionPicNew-->" + mPositionPicNew);
                    Picasso.with(OperateRepairActivity.this)
                            .load(mPositionPicNew)
                            .error(R.drawable.ic_camera)
                            .fit()
                            .centerInside()
                            .into(mImageViewPositionNew);
                    Log.i(TAG, "handleMessage: mInstallPicNew-->" + mInstallPicNew);
                    Picasso.with(OperateRepairActivity.this)
                            .load(mInstallPicNew)
                            .error(R.drawable.ic_camera)
                            .fit()
                            .centerInside()
                            .into(mImageViewInstallNew);

                    if (null != mPositionPicUrlNew) {
                        mImageViewPositionNewDelete.setVisibility(View.VISIBLE);
                    } else {
                        mImageViewPositionNewDelete.setVisibility(View.GONE);
                    }

                    if (null != mInstallPicUrlNew) {
                        mImageViewInstallNewDelete.setVisibility(View.VISIBLE);
                    } else {
                        mImageViewInstallNewDelete.setVisibility(View.GONE);
                    }

                    loadSavedData();
                    break;
                }
                case Data.MSG_2: {
                    //  check whole imei 成功
                    Log.i(TAG, "handleMessage: wholeImei-->" + wholeImei);

                    boolean exist = isExistImei(wholeImei);
                    Log.i(TAG, "handleMessage: exist-->" + exist);
                    if (exist) {
                        setEditTextImei(null);
                        showRepeatDialog();
                        break;
                    }
                    isCheckedImei = true;
                    setEditTextImei(wholeImei);
                    if (isToLocate) {
                        toLocate(wholeImei);
                        isToLocate = false;
                    }
                    break;
                }
                case Data.MSG_3: {
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_4: {
                    //  历史安装人
                    mTextViewInstallName.setText(mLastInstaller);
                    mTextViewInstallPhone.setText(mLastPhoneNo);

                    if (mChangeFlag.equals("0")) {
//                        mRelativeLayoutReplace.setVisibility(View.VISIBLE);
                        mTextViewState.setText(R.string.repair_replace);
                        mTextViewState.setEnabled(true);
                    } else {
//                        mRelativeLayoutReplace.setVisibility(View.GONE);
                        mTextViewState.setText(R.string.repair_replace);
                        mTextViewState.setEnabled(false);
                    }
                    break;
                }
                case Data.MSG_5: {
                    //  do nothing
                    //  取消loadingFragment
                    break;
                }
                case Data.MSG_6: {
                    //  删除图片
                    if (Data.DATA_UPLOAD_TYPE_3 == mPicPosition) {
                        mImageViewPositionNewDelete.setVisibility(View.GONE);
                        mPositionPicUrlNew = null;
                        mDatabaseManager.addRepairPositionUrl(tId, null);
                        Picasso.with(OperateRepairActivity.this)
                                .load(R.drawable.ic_camera)
                                .fit()
                                .centerInside()
                                .into(mImageViewPositionNew);
                    } else {
                        mImageViewInstallNewDelete.setVisibility(View.GONE);
                        mInstallPicUrlNew = null;
                        mDatabaseManager.addRepairInstallUrl(tId, null);
                        Picasso.with(OperateRepairActivity.this)
                                .load(R.drawable.ic_camera)
                                .fit()
                                .centerInside()
                                .into(mImageViewInstallNew);
                    }
                    break;
                }
                case Data.MSG_7: {
                    //  上传图片成功
                    if (Data.DATA_UPLOAD_TYPE_3 == mPicPosition) {
                        mImageViewPositionNewDelete.setVisibility(View.VISIBLE);
                        Picasso.with(OperateRepairActivity.this)
                                .load(mPositionPicNew)
                                .fit()
                                .centerInside().error(R.drawable.ic_camera)
                                .into(mImageViewPositionNew);
                    } else {
                        mImageViewInstallNewDelete.setVisibility(View.VISIBLE);
                        Picasso.with(OperateRepairActivity.this)
                                .load(mInstallPicNew)
                                .fit()
                                .centerInside().error(R.drawable.ic_camera)
                                .into(mImageViewInstallNew);
                    }
                    break;
                }
                case Data.MSG_8: {
                    //  check imei failure或者获取 whole imei失败
                    isCheckedImei = false;
                    setEditTextImei(null);
                    myHandler.sendEmptyMessage(Data.MSG_3);
                    break;
                }
                case Data.MSG_10: {
                    //  获取 whole imei
                    setEditTextImei(wholeImei);
                    mDatabaseManager.addRepair(tId, 0);
                    mNetworkManager.checkIMEI(eid, token, wholeImei, mOrderTerType, orderNo, userName, mImeiOld);
                    break;
                }
                case Data.MSG_11: {
                    //  EditTextNewImei TextChangedListener
                    isCheckedImei = false;
                    if (isImeiEdit) {
                        mDatabaseManager.addRepair(tId, 0);
                    }
                    break;
                }
                case Data.MSG_12: {
                    //  获取whole imei失败
                    setEditTextImei(null);
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
