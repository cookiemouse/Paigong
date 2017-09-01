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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.adapter.OperateInstallAdapter;
import com.tianyigps.dispatch2.adapter.OperateInstallListAdapter2;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.bean.CheckImeiBean;
import com.tianyigps.dispatch2.bean.DeletePicBean;
import com.tianyigps.dispatch2.bean.StartOrderInfoBean;
import com.tianyigps.dispatch2.bean.UploadPicBean;
import com.tianyigps.dispatch2.bean.WholeImeiBean;
import com.tianyigps.dispatch2.customview.MyListView;
import com.tianyigps.dispatch2.customview.MyRecyclerView;
import com.tianyigps.dispatch2.data.AdapterOperateInstallListData;
import com.tianyigps.dispatch2.data.AdapterOperateInstallRecyclerData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnCheckIMEIListener;
import com.tianyigps.dispatch2.interfaces.OnDeletePicListener;
import com.tianyigps.dispatch2.interfaces.OnGetWholeIMEIListener;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderInfoStartListener;
import com.tianyigps.dispatch2.interfaces.OnUploadPicListener;
import com.tianyigps.dispatch2.manager.DatabaseManager;
import com.tianyigps.dispatch2.manager.FileManager;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.RegularU;
import com.tianyigps.dispatch2.utils.TinyU;
import com.tianyigps.dispatch2.utils.ToastU;
import com.tianyigps.dispatch2.utils.UploadPicU;
import com.tianyigps.dispatch2.utils.Uri2FileU;
import com.yundian.bottomdialog.BottomDialog;
import com.zxy.tiny.callback.FileCallback;

import java.util.ArrayList;
import java.util.List;

public class OperateInstallActivity extends BaseActivity {

    private static final String TAG = "OperateInstall";
    private static final int PIC_MAX = 5;

    private static final String KEY_IMEI = "imei";
    private static final String KEY_REPLACE = "replace";

    private ImageView mImageViewCarNo, mImageViewFrameNo;
    private ImageView mImageViewCarNoDelete, mImageViewFrameNoDelete;
    private EditText mEditTextCarNo, mEditTextCarType;
    private TextView mTextViewFrameNo;

    //  提示
    private TextView mTextViewTip1, mTextViewTip2, mTextViewTip3;

    private Button mButtonSave;
    private View mViewSave;

    private MyRecyclerView mRecyclerView;
    private MyListView mListView;

    private OperateInstallAdapter mOperateInstallAdapter;
    private OperateInstallListAdapter2 mOperateInstallListAdapter;

    private List<AdapterOperateInstallRecyclerData> mAdapterOperateInstallRecyclerDataList;
    private List<AdapterOperateInstallListData> mAdapterOperateInstallListDataList;

    //  正在操作中的item
    private int itemRecycler;   //  Recycler操作位置
    private int itemPosition;   //  listview操作位置
    private String itemPath;    //  从相册或拍照得到的图片物理位置
    private String urlCarNoPic, urlFrameNoPic;  //  车牌号图片Url、车架号图片url

    private NetworkManager mNetworkManager;
    private SharedpreferenceManager mSharedpreferenceManager;
    private MyHandler myHandler;
    private String mStringMessage;
    private int eid;
    private String token;
    private String userName;
    private String orderNo;
    private String frameNo;
    private int carId;
    private int wiredNum;
    private int wirelessNum;

    // id，主键
    private int idMainCar;
    private String ID_MAIN_TERMINAL;
    private String idMainTerminal;

    private DatabaseManager mDatabaseManager;

    //  选择图片或拍照
    private static final int INTENT_CHOICE_P = 1;
    private static final int INTENT_PHOTO_P = 2;
    private static final int INTENT_CHOICE_I = 3;
    private static final int INTENT_PHOTO_I = 4;
    private static final int INTENT_CHOICE_C = 5;
    private static final int INTENT_PHOTO_C = 6;
    private static final int INTENT_CHOICE_F = 7;
    private static final int INTENT_PHOTO_F = 8;
    private static final int INTENT_CHOICE_R = 9;
    private static final int INTENT_PHOTO_R = 10;
    private int picType = 1;   //  1 = 安装位置，3 = 接线图, 5 = 车牌号, 7 = 车架号, 9 = RecyclerView

    private Uri mUriPhoto;
    private String fileTempName;

    private String mBaseImg;
    private String mCarNoPic, mCarNoPicUrl;
    private String mCarFramePic, mCarFramePicUrl;
    private String mCarNo;
    private String mCarFrameNo;
    private String mCarBrand;

    //  LoadingFragment
    private LoadingDialogFragment mLoadingDialogFragment;

    //  压缩图片temp
    private int mTempType, mTempId, mTempModel;
    private String mTempImgUrl;

    //  删除图片
    private int mDeleteType = Data.DATA_UPLOAD_TYPE_1;

    //  设备里是否有信息，如果有则判断车辆是否填写完成
    private boolean haveTerData = false;

    //  设备完成数量
    private int mCompleteCount = 0;

    //  imei号是否校验过
    private boolean isCheckedImei = true;
    private boolean isToLocate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_install);

        init();

        setEventListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myHandler.removeMessages(Data.MSG_1);
        myHandler.removeMessages(Data.MSG_2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Data.DATA_INTENT_SCANNER_REQUEST && resultCode == Data.DATA_INTENT_SCANNER_RESULT) {
            Log.i(TAG, "onActivityResult: qrcode-->" + data.getStringExtra(Data.DATA_SCANNER));
            isCheckedImei = false;
            String imei = data.getStringExtra(Data.DATA_SCANNER);
            int model;
            if (mAdapterOperateInstallListDataList.get(itemPosition).isWire()) {
                model = 1;
            } else {
                model = 2;
            }
            showLoading();
            mNetworkManager.checkIMEI(eid, token, imei, model, orderNo, userName, "");
        }

        if (requestCode == Data.DATA_INTENT_LOCATE_REQUEST && resultCode == Data.DATA_INTENT_LOCATE_RESULT) {
            int locateType = data.getIntExtra(Data.DATA_LOCATE_TYPE, 3);
            int model = data.getIntExtra(Data.DATA_LOCATE_MODEL, 0);
            Log.i(TAG, "onActivityResult: locateType-->" + locateType);
            Log.i(TAG, "onActivityResult: model-->" + model);
            Log.i(TAG, "onActivityResult: itemPosition-->" + itemPosition);
            AdapterOperateInstallListData listData = mAdapterOperateInstallListDataList.get(itemPosition);
            mDatabaseManager.addTerLocateType(idMainTerminal, locateType);
            mDatabaseManager.addTerModel(idMainTerminal, model);
            listData.setModel(model);
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
                Log.i(TAG, "onActivityResult: uri-->" + selectedImage);

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(selectedImage);
                Log.i(TAG, "onActivityResult: path-->" + path);

                String imgUrl = mAdapterOperateInstallListDataList.get(itemPosition).getPositionPicUrl();

                mDatabaseManager.getTerId(idMainTerminal);

                Log.i(TAG, "onActivityResult: idMainTerminal-->" + idMainTerminal);
                int tid = mDatabaseManager.getTerId(idMainTerminal);
                Log.i(TAG, "onActivityResult: tid-->" + tid);

                uploadTerminalPic(tid, Data.DATA_UPLOAD_TYPE_3, imgUrl, path);
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

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(uri);
                Log.i(TAG, "onActivityResult: path-->" + path);

                String imgUrl = mAdapterOperateInstallListDataList.get(itemPosition).getPositionPicUrl();

                Log.i(TAG, "onActivityResult: idMainTerminal-->" + idMainTerminal);
                int tid = mDatabaseManager.getTerId(idMainTerminal);
                Log.i(TAG, "onActivityResult: tid-->" + tid);
                uploadTerminalPic(tid, Data.DATA_UPLOAD_TYPE_3, imgUrl, path);
                break;
            }
            case INTENT_CHOICE_I: {
                Uri selectedImage = data.getData();

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(selectedImage);
                Log.i(TAG, "onActivityResult: path-->" + path);

                String imgUrl = mAdapterOperateInstallListDataList.get(itemPosition).getInstallPicUrl();

                Log.i(TAG, "onActivityResult: idMainTerminal-->" + idMainTerminal);
                int tid = mDatabaseManager.getTerId(idMainTerminal);
                Log.i(TAG, "onActivityResult: tid-->" + tid);
                uploadTerminalPic(tid, Data.DATA_UPLOAD_TYPE_4, imgUrl, path);
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
                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(uri);
                Log.i(TAG, "onActivityResult: path-->" + path);

                String imgUrl = mAdapterOperateInstallListDataList.get(itemPosition).getInstallPicUrl();

                Log.i(TAG, "onActivityResult: idMainTerminal-->" + idMainTerminal);
                int tid = mDatabaseManager.getTerId(idMainTerminal);
                Log.i(TAG, "onActivityResult: tid-->" + tid);

                uploadTerminalPic(tid, Data.DATA_UPLOAD_TYPE_4, imgUrl, path);
                break;
            }
            case INTENT_CHOICE_C: {
                Uri selectedImage = data.getData();

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(selectedImage);
                Log.i(TAG, "onActivityResult: path-->" + path);

                uploadCarPic(Data.DATA_UPLOAD_TYPE_1, urlCarNoPic, path);
                break;
            }
            case INTENT_PHOTO_C: {
                Uri uri = null;
                if (data != null && data.getData() != null) {
                    uri = data.getData();
                }
                if (uri == null) {
                    if (mUriPhoto != null) {
                        uri = mUriPhoto;
                    }
                }

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(uri);
                uploadCarPic(Data.DATA_UPLOAD_TYPE_1, urlCarNoPic, path);
                break;
            }
            case INTENT_CHOICE_F: {
                Uri selectedImage = data.getData();

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(selectedImage);
                Log.i(TAG, "onActivityResult: path-->" + path);

                uploadCarPic(Data.DATA_UPLOAD_TYPE_2, urlFrameNoPic, path);
                break;
            }
            case INTENT_PHOTO_F: {
                Uri uri = null;
                if (data != null && data.getData() != null) {
                    uri = data.getData();
                }
                if (uri == null) {
                    if (mUriPhoto != null) {
                        uri = mUriPhoto;
                    }
                }

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(uri);
                uploadCarPic(Data.DATA_UPLOAD_TYPE_2, urlFrameNoPic, path);
                break;
            }
            case INTENT_CHOICE_R: {
                Uri selectedImage = data.getData();

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(selectedImage);
                Log.i(TAG, "onActivityResult: path-->" + path);
                String imgUrl = mAdapterOperateInstallRecyclerDataList.get(itemRecycler).getImgUrl();

                uploadCarPic(Data.DATA_UPLOAD_TYPE_5, imgUrl, path);
                break;
            }
            case INTENT_PHOTO_R: {
                Uri uri = null;
                if (data != null && data.getData() != null) {
                    uri = data.getData();
                }
                if (uri == null) {
                    if (mUriPhoto != null) {
                        uri = mUriPhoto;
                    }
                }

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(uri);

                String imgUrl = mAdapterOperateInstallRecyclerDataList.get(itemRecycler).getImgUrl();

                uploadCarPic(Data.DATA_UPLOAD_TYPE_5, imgUrl, path);
                break;
            }
            default: {
                Log.i(TAG, "onActivityResult: default");
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        saveData();
        if (isComplete()) {
            if (mCompleteCount == mAdapterOperateInstallListDataList.size() || mCompleteCount == 0) {
                finish();
            } else {
                // 2017/8/31 对话框
                showPartDialog();
            }
        } else {
            if (mTextViewTip1.getVisibility() == View.VISIBLE && !RegularU.isEmpty(mCarNo)) {
                showNotCompleteDialog("车牌号填写不正确");
            } else {
                showNotCompleteDialog("车辆信息内有未填写内容，请填写完整");
            }
        }
    }

    @Override
    protected void onDestroy() {
        mDatabaseManager.close();
        super.onDestroy();
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

    private void init() {
        this.setTitleText("");

        Intent intent = getIntent();
        orderNo = intent.getStringExtra(Data.DATA_INTENT_ORDER_NO);
        frameNo = intent.getStringExtra(Data.DATA_INTENT_FRAME_NO);
        carId = intent.getIntExtra(Data.DATA_INTENT_INSTALL_CAR_ID, 0);
        idMainCar = carId;
        ID_MAIN_TERMINAL = idMainCar + "T";
        wiredNum = intent.getIntExtra(Data.DATA_INTENT_INSTALL_WIRED_NUM, 0);
        wirelessNum = intent.getIntExtra(Data.DATA_INTENT_INSTALL_WIRELESS_NUM, 0);

        Log.i(TAG, "init: orderNo-->" + orderNo);
        Log.i(TAG, "init: frameNo-->" + frameNo);
        Log.i(TAG, "init: carId-->" + carId);
        Log.i(TAG, "init: wiredNum-->" + wiredNum);
        Log.i(TAG, "init: wirelessNum-->" + wirelessNum);

        mImageViewCarNo = findViewById(R.id.iv_layout_operate_install_car_pic);
        mImageViewFrameNo = findViewById(R.id.iv_layout_operate_install_frame_pic);
        mImageViewCarNoDelete = findViewById(R.id.iv_layout_operate_install_car_pic_delete);
        mImageViewFrameNoDelete = findViewById(R.id.iv_layout_operate_install_frame_pic_delete);
        mEditTextCarNo = findViewById(R.id.et_layout_operate_install_car_no);
        mTextViewFrameNo = findViewById(R.id.tv_layout_operate_install_frame_no);
        mEditTextCarType = findViewById(R.id.et_layout_operate_install_car_type);

        mTextViewTip1 = findViewById(R.id.tv_activity_operate_install_tip_1);
        mTextViewTip2 = findViewById(R.id.tv_activity_operate_install_tip_2);
        mTextViewTip3 = findViewById(R.id.tv_activity_operate_install_tip_3);

        mLoadingDialogFragment = new LoadingDialogFragment();

        mViewSave = LayoutInflater.from(this).inflate(R.layout.layout_activity_installing_next, null);
        mButtonSave = mViewSave.findViewById(R.id.btn_layout_activity_installing_next);
        mButtonSave.setText(R.string.save);

        mTextViewFrameNo.setText(frameNo);

        mRecyclerView = findViewById(R.id.rv_layout_activity_operate_install);
        mListView = findViewById(R.id.lv_activity_operate_install);


        mAdapterOperateInstallRecyclerDataList = new ArrayList<>();
        mAdapterOperateInstallListDataList = new ArrayList<>();

        mDatabaseManager = new DatabaseManager(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        for (int i = 0; i < wiredNum; i++) {
            mAdapterOperateInstallListDataList.add(new AdapterOperateInstallListData(true));
        }
        for (int i = 0; i < wirelessNum; i++) {
            mAdapterOperateInstallListDataList.add(new AdapterOperateInstallListData(false));
        }

        mOperateInstallAdapter = new OperateInstallAdapter(this, mAdapterOperateInstallRecyclerDataList);

        mRecyclerView.setAdapter(mOperateInstallAdapter);

        mOperateInstallListAdapter = new OperateInstallListAdapter2(OperateInstallActivity.this,
                mAdapterOperateInstallListDataList);
        mListView.setAdapter(mOperateInstallListAdapter);

        mListView.addFooterView(mViewSave);

        mNetworkManager = new NetworkManager();
        mSharedpreferenceManager = new SharedpreferenceManager(this);
        myHandler = new MyHandler();

        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();
        mBaseImg = mSharedpreferenceManager.getImageBaseUrl();

        showLoading();
        mNetworkManager.getWorkerOrderInfoStart(eid, token, orderNo, userName);

//        loadCarData();
    }

    private void setEventListener() {
        this.setOnTitleBackClickListener(new OnTitleBackClickListener() {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });

        mImageViewCarNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/31 选择图片
                picType = 5;
                showChoiceDialog();
            }
        });

        mImageViewFrameNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  2017/7/31 选择图片
                picType = 7;
                showChoiceDialog();
            }
        });

        mImageViewCarNoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/8/18 删除车牌号图片
                mDeleteType = Data.DATA_UPLOAD_TYPE_1;
                showDeletePicDialog(0, Data.DATA_UPLOAD_TYPE_1, mCarNoPicUrl);
//                mNetworkManager.deletePic(eid, token, orderNo, carId, Data.DATA_UPLOAD_TYPE_1, mCarFramePicUrl, userName);
            }
        });

        mImageViewFrameNoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/8/18 删除车架号图片
                mDeleteType = Data.DATA_UPLOAD_TYPE_2;
                showDeletePicDialog(0, Data.DATA_UPLOAD_TYPE_2, mCarFramePicUrl);
//                mNetworkManager.deletePic(eid, token, orderNo, carId, Data.DATA_UPLOAD_TYPE_2, mCarFramePicUrl, userName);
            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  2017/8/3
                OperateInstallActivity.this.onBackPressed();
            }
        });

        mOperateInstallAdapter.setOnItemOperateListener(new OperateInstallAdapter.OnItemOperateListener() {
            @Override
            public void onDeleteClick(int position) {
                //  2017/8/1 删除图片
                mDeleteType = Data.DATA_UPLOAD_TYPE_5;
                itemRecycler = position;
                removePicFromRecycler(position);
            }

            @Override
            public void onPicClick(int position) {
                // 2017/8/1 添加图片
                itemRecycler = position;
                picType = 9;
                showChoiceDialog();
            }
        });

        mOperateInstallListAdapter.setOnItemOperateListener(new OperateInstallListAdapter2.OnItemOperateListener() {
            @Override
            public void onScannerClick(int position) {
                //  2017/7/31 扫描
                itemPosition = position;
                idMainTerminal = ID_MAIN_TERMINAL + itemPosition;
                toScanner();
            }

            @Override
            public void onTextChanged(int position, String imei) {
                //  2017/7/31 检测imei
                Log.i(TAG, "onTextChanged: position-->" + position + ", imie-->" + imei);
                itemPosition = position;
                idMainTerminal = ID_MAIN_TERMINAL + itemPosition;

                AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(position);
                data.settNoNew(imei);
                isCheckedImei = false;

                mDatabaseManager.addTerModel(idMainTerminal, 0);
            }

            @Override
            public void onLoseFocus(int position) {
                Log.i(TAG, "onLoseFocus: position-->" + position);
                itemPosition = position;
                idMainTerminal = ID_MAIN_TERMINAL + itemPosition;

                AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(position);
                String imei = data.gettNoNew();
                isCheckedImei = false;
                if (RegularU.isEmpty(imei)) {
                    //  imei为空
                    Log.i(TAG, "onFocusChange: imei为空");
                } else {
                    getWholeImei(imei);
                }
            }

            @Override
            public void onStatusClick(int position) {
                //  2017/7/31 改变状态
                itemPosition = position;
                AdapterOperateInstallListData adapterOperateInstallListData = mAdapterOperateInstallListDataList.get(position);
                String imeiOld = adapterOperateInstallListData.gettNoOld();
                String imeiNew = adapterOperateInstallListData.gettNoNew();
                if (null == imeiOld || "".equals(imeiOld)) {
                    adapterOperateInstallListData.settNoOld(imeiNew);
                    adapterOperateInstallListData.settNoNew(null);
                } else {
                    adapterOperateInstallListData.settNoOld(null);
                    adapterOperateInstallListData.settNoNew(imeiOld);
                }
                mOperateInstallListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLocateClick(int position) {
                //  2017/7/31 快速定位
                itemPosition = position;
                idMainTerminal = ID_MAIN_TERMINAL + itemPosition;

                AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(position);
                String imei = data.gettNoNew();
                int model;
                if (data.isWire()) {
                    model = 1;
                } else {
                    model = 2;
                }
                if (isCheckedImei) {
                    toLocate(imei);
                } else {
                    isToLocate = true;
                    showLoading();
                    mNetworkManager.checkIMEI(eid, token, imei, model, orderNo, userName, "");
                }
            }

            @Override
            public void onPositionPicClick(int position) {
                //  2017/7/31 安装位置图片
                itemPosition = position;

                idMainTerminal = ID_MAIN_TERMINAL + itemPosition;

                picType = 1;
                showChoiceDialog();
            }

            @Override
            public void onInstallPicClick(int position) {
                //  2017/7/31 接线图图片
                itemPosition = position;

                idMainTerminal = ID_MAIN_TERMINAL + itemPosition;

                picType = 3;
                showChoiceDialog();
            }

            @Override
            public void onPositionPicDelete(int position) {
                // TODO: 2017/8/18 删除位置图片
                itemPosition = position;
                mDeleteType = Data.DATA_UPLOAD_TYPE_3;
                AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(itemPosition);
                int tid = data.gettId();
                String url = data.getPositionPicUrl();
                showDeletePicDialog(tid, mDeleteType, url);
//                mNetworkManager.deletePic(eid, token, orderNo, carId, tid, mDeleteType, url, userName);
            }

            @Override
            public void onInstallPicDelete(int position) {
                // TODO: 2017/8/18 删除安装图片
                itemPosition = position;
                mDeleteType = Data.DATA_UPLOAD_TYPE_4;
                AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(itemPosition);
                int tid = data.gettId();
                String url = data.getInstallPicUrl();
                showDeletePicDialog(tid, mDeleteType, url);
//                mNetworkManager.deletePic(eid, token, orderNo, carId, tid, mDeleteType, url, userName);
            }
        });

        mNetworkManager.setGetWorkerOrderInfoStartListener(new OnGetWorkerOrderInfoStartListener() {
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
                StartOrderInfoBean startOrderInfoBean = gson.fromJson(result, StartOrderInfoBean.class);

                if (!startOrderInfoBean.isSuccess()) {
                    mStringMessage = startOrderInfoBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                StartOrderInfoBean.ObjBean objBean = startOrderInfoBean.getObj();
//                StartOrderInfoBean.ObjBean.CarListBean carListBean = objBean.getCarList().get(0);
                for (StartOrderInfoBean.ObjBean.CarListBean carListBean : objBean.getCarList()) {
                    if (carListBean.getId() == carId) {
                        mCarNo = carListBean.getCarNo();
                        mCarFrameNo = carListBean.getCarVin();
                        mCarBrand = carListBean.getCarBrand();
                        mCarNoPicUrl = carListBean.getCarNoPic();
                        mCarFramePicUrl = carListBean.getCarVinPic();
                        mCarNoPic = mBaseImg + mCarNoPicUrl;
                        mCarFramePic = mBaseImg + mCarFramePicUrl;

                        mDatabaseManager.addCarNoPic(idMainCar, mCarNoPic, carListBean.getCarNoPic());
                        mDatabaseManager.addCarFrameNoPic(idMainCar, mCarFramePic, carListBean.getCarVinPic());

                        String pic1 = carListBean.getPic1();
                        String pic2 = carListBean.getPic2();
                        String pic3 = carListBean.getPic3();
                        String pic4 = carListBean.getPic4();
                        String pic5 = carListBean.getPic5();
                        String pic6 = carListBean.getPic6();

                        if (null != pic1) {
                            mAdapterOperateInstallRecyclerDataList.add(new AdapterOperateInstallRecyclerData(mBaseImg + pic1, pic1));
//                    mDatabaseManager.addCarPics(idMainCar, 0, mBaseImg + pic1, pic1);
                        }
                        if (null != pic2) {
                            mAdapterOperateInstallRecyclerDataList.add(new AdapterOperateInstallRecyclerData(mBaseImg + pic2, pic2));
//                    mDatabaseManager.addCarPics(idMainCar, 1, mBaseImg + pic1, pic1);
                        }
                        if (null != pic3) {
                            mAdapterOperateInstallRecyclerDataList.add(new AdapterOperateInstallRecyclerData(mBaseImg + pic3, pic3));
//                    mDatabaseManager.addCarPics(idMainCar, 2, mBaseImg + pic1, pic1);
                        }
                        if (null != pic4) {
                            mAdapterOperateInstallRecyclerDataList.add(new AdapterOperateInstallRecyclerData(mBaseImg + pic4, pic4));
//                    mDatabaseManager.addCarPics(idMainCar, 3, mBaseImg + pic1, pic1);
                        }
                        if (null != pic5) {
                            mAdapterOperateInstallRecyclerDataList.add(new AdapterOperateInstallRecyclerData(mBaseImg + pic5, pic5));
//                    mDatabaseManager.addCarPics(idMainCar, 4, mBaseImg + pic1, pic1);
                        }
                        if (null != pic6) {
                            mAdapterOperateInstallRecyclerDataList.add(new AdapterOperateInstallRecyclerData(mBaseImg + pic6, pic6));
//                    mDatabaseManager.addCarPics(idMainCar, 5, mBaseImg + pic1, pic1);
                        }
                        List<StartOrderInfoBean.ObjBean.CarListBean.CarTerminalListBean> carTerminalListBeanList = carListBean.getCarTerminalList();
                        for (int i = 0; i < carTerminalListBeanList.size(); i++) {

                            if (i >= mAdapterOperateInstallListDataList.size()) {
                                continue;
                            }
                            StartOrderInfoBean.ObjBean.CarListBean.CarTerminalListBean carTerminalListBean = carTerminalListBeanList.get(i);
                            String tNo = carTerminalListBean.getTNo();
                            int tId = carTerminalListBean.getId();
                            String position = carTerminalListBean.getNewInstallLocation();
                            String positionPic = carTerminalListBean.getNewInstallLocationPic();
                            String installPic = carTerminalListBean.getNewWiringDiagramPic();
                            int type = carTerminalListBean.getTerminalType();

                            for (AdapterOperateInstallListData data : mAdapterOperateInstallListDataList) {
                                if ((data.isWire() && type == 1) || (!data.isWire() && type == 2)) {
                                    data.settId(tId);
                                    data.settNoNew(tNo);
                                    data.setPosition(position);
                                    data.setPositionPic(mBaseImg + positionPic);
                                    data.setPositionPicUrl(positionPic);
                                    data.setInstallPic(mBaseImg + installPic);
                                    data.setInstallPicUrl(installPic);
                                }
                            }

                            String idTemp = ID_MAIN_TERMINAL + i;
                            mDatabaseManager.addTerId(idTemp, tId, carId);
                        }
                    }
                }

                myHandler.sendEmptyMessage(Data.MSG_9);
            }
        });

        mNetworkManager.setOnGetWholeIMEIListener(new OnGetWholeIMEIListener() {
            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                WholeImeiBean wholeImeiBean = gson.fromJson(result, WholeImeiBean.class);
                if (!wholeImeiBean.isSuccess()) {
                    mStringMessage = wholeImeiBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_11);
                    return;
                }
                WholeImeiBean.ObjBean objBean = wholeImeiBean.getObj();
                String imei = objBean.getImei();
                //验证imei
                AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(itemPosition);
                int model;
                if (data.isWire()) {
                    model = 1;
                } else {
                    model = 2;
                }
                mNetworkManager.checkIMEI(eid, token, imei, model, orderNo, userName, "");
            }
        });

        mNetworkManager.setCheckIMEIListener(new OnCheckIMEIListener() {
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
                CheckImeiBean checkImeiBean = gson.fromJson(result, CheckImeiBean.class);
                mStringMessage = checkImeiBean.getMsg();
                if (!checkImeiBean.isSuccess()) {
                    myHandler.sendEmptyMessage(Data.MSG_11);
                    return;
                }
                CheckImeiBean.ObjBean objBean = checkImeiBean.getObj();
                if (null == objBean) {
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }
                Message message = new Message();
                String imei = objBean.getImei();
                boolean replaceAble = false;
                if ("0".equals(objBean.getChangeFlag())) {
                    replaceAble = true;
                }

                Bundle bundle = new Bundle();
                bundle.putString(KEY_IMEI, imei);
                bundle.putBoolean(KEY_REPLACE, replaceAble);
                message.setData(bundle);
                message.what = Data.MSG_2;
                myHandler.sendMessage(message);
            }
        });

        mNetworkManager.setOnUploadPicListener(new OnUploadPicListener() {
            @Override
            public void onFailure() {
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                UploadPicBean uploadPicBean = gson.fromJson(result, UploadPicBean.class);
                if (!uploadPicBean.isSuccess()) {
                    mStringMessage = uploadPicBean.getMsg();
                    onFailure();
                    return;
                }
                UploadPicBean.ObjBean objBean = uploadPicBean.getObj();

                int id = objBean.getId();

                String imgUrl = objBean.getImgUrl();
                itemPath = mBaseImg + imgUrl;

                Log.i(TAG, "onSuccess: picType-->" + picType);
                switch (picType) {
                    //  Recycler
                    case INTENT_CHOICE_R: {
                    }
                    case INTENT_PHOTO_R: {
//                        mDatabaseManager.addCarPics(idMainCar, itemRecycler, itemPath, imgUrl);
                        Log.i(TAG, "onSuccess: itemRecycle-->" + itemRecycler);
                        AdapterOperateInstallRecyclerData data = mAdapterOperateInstallRecyclerDataList.get(itemRecycler);
                        if (null == data || null == data.getImgUrl()) {
                            int size = mAdapterOperateInstallRecyclerDataList.size();
                            if (size < 6) {
                                mAdapterOperateInstallRecyclerDataList.add(new AdapterOperateInstallRecyclerData());
                            }
                        }
                        data.setPath(itemPath);
                        data.setImgUrl(imgUrl);
                        myHandler.sendEmptyMessage(Data.MSG_3);
                        break;
                    }
                    // carNo
                    case INTENT_CHOICE_C: {
                    }
                    case INTENT_PHOTO_C: {
                        mCarNoPicUrl = imgUrl;
                        mCarNoPic = mBaseImg + mCarNoPicUrl;

                        mDatabaseManager.addCarNoPic(idMainCar, itemPath, imgUrl);
                        myHandler.sendEmptyMessage(Data.MSG_4);
                        break;
                    }
                    // frameNo
                    case INTENT_CHOICE_F: {
                    }
                    case INTENT_PHOTO_F: {
                        mCarFramePicUrl = imgUrl;
                        mCarFramePic = mBaseImg + mCarFramePicUrl;

                        mDatabaseManager.addCarFrameNoPic(idMainCar, itemPath, imgUrl);
                        myHandler.sendEmptyMessage(Data.MSG_5);
                        break;
                    }

                    //  positionPic
                    case INTENT_CHOICE_P: {
                    }
                    case INTENT_PHOTO_P: {

                        mDatabaseManager.addTerPositionPic(idMainTerminal, itemPath, imgUrl);
                        mDatabaseManager.addTerId(idMainTerminal, id, carId);
                        AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(itemPosition);
                        data.settId(id);
                        data.setPositionPic(itemPath);
                        data.setPositionPicUrl(imgUrl);
                        myHandler.sendEmptyMessage(Data.MSG_6);
                        break;
                    }

                    //  installPic
                    case INTENT_CHOICE_I: {
                    }
                    case INTENT_PHOTO_I: {

                        mDatabaseManager.addTerInstallPic(idMainTerminal, itemPath, imgUrl);
                        mDatabaseManager.addTerId(idMainTerminal, id, carId);
                        AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(itemPosition);
                        data.settId(id);
                        data.setInstallPic(itemPath);
                        data.setInstallPicUrl(imgUrl);
                        myHandler.sendEmptyMessage(Data.MSG_7);
                        break;
                    }
                    default: {
                        //  防止加载框不取消的情况
                        myHandler.sendEmptyMessage(Data.MSG_11);
                    }
                }
            }
        });

        mNetworkManager.setOnDeletePicListener(new OnDeletePicListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(Data.MSG_12);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                DeletePicBean deletePicBean = gson.fromJson(result, DeletePicBean.class);
                mStringMessage = deletePicBean.getMsg();
                if (!deletePicBean.isSuccess()) {
                    myHandler.sendEmptyMessage(Data.MSG_12);
                    return;
                }

                myHandler.sendEmptyMessage(Data.MSG_8);
            }
        });
    }

    //  获取并显示数据库里的车辆数据
    private void loadCarData() {
        Cursor cursor = mDatabaseManager.getCar(idMainCar);
        if (null != cursor && cursor.moveToFirst()) {
            Log.i(TAG, "loadCardata: idMain-->" + cursor.getInt(0));
            String carNo = cursor.getString(1);
            String carType = cursor.getString(3);

            if (null != carNo && !"".equals(carNo)) {
                mCarNo = carNo;
            }
            if (null != carType && !"".equals(carType)) {
                mCarBrand = carType;
            }

            cursor.close();
        }
    }

    //  加载Recycler图片
    private void loadCarPics() {
        mAdapterOperateInstallRecyclerDataList.clear();
        Cursor cursor = mDatabaseManager.getCarPics(idMainCar);
        if (null != cursor && cursor.moveToFirst()) {
            for (int i = 0; i < 6; i++) {
                String pic = cursor.getString(i);
                String url = cursor.getString(i + 6);
                Log.i(TAG, "loadCarPics: pic-->" + pic);
                Log.i(TAG, "loadCarPics: url-->" + url);
                if (null != pic) {
                    mAdapterOperateInstallRecyclerDataList.add(new AdapterOperateInstallRecyclerData(pic, url));
                }
            }
            cursor.close();
        }
        if (mAdapterOperateInstallRecyclerDataList.size() <= PIC_MAX) {
            mAdapterOperateInstallRecyclerDataList.add(new AdapterOperateInstallRecyclerData());
        }
        mOperateInstallAdapter.notifyDataSetChanged();
    }

    //  RecyclerView添加图片
    private void addPicToRecycler(String path) {
        AdapterOperateInstallRecyclerData adapterOperateInstallRecyclerData = mAdapterOperateInstallRecyclerDataList.get(itemRecycler);
        adapterOperateInstallRecyclerData.setPath(path);
        if (mAdapterOperateInstallRecyclerDataList.size() <= PIC_MAX) {
            mAdapterOperateInstallRecyclerDataList.add(new AdapterOperateInstallRecyclerData());
        }
        mOperateInstallAdapter.notifyDataSetChanged();
    }

    //  加载ListView数据库里的数据
    private void loadTerminalData() {
        for (int i = 0; i < mAdapterOperateInstallListDataList.size(); i++) {
            Cursor cursor = mDatabaseManager.getTer(ID_MAIN_TERMINAL + i);
            if (null != cursor && cursor.moveToFirst()) {
                String id = cursor.getString(0);
                String tNoOld = cursor.getString(1);
                String tNoNew = cursor.getString(2);
                String position = cursor.getString(3);
                int model = cursor.getInt(8);
                int tid = cursor.getInt(9);

                Log.i(TAG, "loadTerminalData: id-->" + id);
                Log.i(TAG, "loadTerminalData: tNoOld-->" + tNoOld);
                Log.i(TAG, "loadTerminalData: tNoNew-->" + tNoNew);
                Log.i(TAG, "loadTerminalData: position-->" + position);
                Log.i(TAG, "loadTerminalData: model-->" + model);
                Log.i(TAG, "loadTerminalData: tid-->" + tid);
                Log.i(TAG, "........................................");

                cursor.close();

                AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(i);
                data.settNoNew(tNoNew);
                data.setPosition(position);
                data.setModel(model);
            }
        }
        mOperateInstallListAdapter.notifyDataSetChanged();
    }

    //  保存数据
    private void saveData() {
        String carNo = mEditTextCarNo.getText().toString();

        String carType = mEditTextCarType.getText().toString();
        String carFrameNo = mTextViewFrameNo.getText().toString();

        mDatabaseManager.addCarInfo(idMainCar, carNo, carFrameNo, carType);

        int i = 0;
        for (AdapterOperateInstallListData data : mAdapterOperateInstallListDataList) {
            String tNoOld = data.gettNoOld();
            String tNoNew = data.gettNoNew();
            EditText editText = mListView.getChildAt(i).findViewById(R.id.et_item_operate_install_position);
            String position = editText.getText().toString();
            String positionPic = data.getPositionPic();
            String installPic = data.getInstallPic();
            String positionPicUrl = data.getPositionPicUrl();
            String installPicUrl = data.getInstallPicUrl();

            Log.i(TAG, "saveData: tNoOld-->" + tNoOld);
            Log.i(TAG, "saveData: tNoNew-->" + tNoNew);
            Log.i(TAG, "saveData: position-->" + position);
            data.setPosition(position);

            String id = ID_MAIN_TERMINAL + i;
            Log.i(TAG, "saveData: idMainTerminal-->" + id);
            Log.i(TAG, "saveData: positionPic-->" + positionPic);
            Log.i(TAG, "saveData: installPic-->" + installPic);
            Log.i(TAG, "saveData: positionPicUrl-->" + positionPicUrl);
            Log.i(TAG, "saveData: installPicUrl-->" + installPicUrl);

            int wire = 0;
            if (data.isWire()) {
                wire = 1;
            }

            mDatabaseManager.addTer(id, tNoOld, tNoNew, position
                    , positionPic, installPic
                    , positionPicUrl, installPicUrl, carId, wire);
            i++;
            Log.i(TAG, "----------------------------------------");
        }
    }

    //  RecycleView删除图片
    private void removePicFromRecycler(int position) {
        AdapterOperateInstallRecyclerData data = mAdapterOperateInstallRecyclerDataList.get(position);
        String url = data.getImgUrl();
        if (null == url || "".equals(url)) {
            return;
        }
        showDeletePicDialog(carId, Data.DATA_UPLOAD_TYPE_5, url);
//        mNetworkManager.deletePic(eid, token, orderNo, carId, Data.DATA_UPLOAD_TYPE_5, url, userName);
    }

    //  跳转到快速定位页面
    private void toLocate(String imei) {
        if (null == imei || "".equals(imei)) {
            showMessageDialog("IMEI号不可用！");
            return;
        }
        Intent intent = new Intent(OperateInstallActivity.this, LocateActivity.class);
        intent.putExtra(Data.DATA_INTENT_LOCATE_TYPE, false);
        intent.putExtra(Data.DATA_INTENT_LOCATE_IMEI, imei);
        startActivityForResult(intent, Data.DATA_INTENT_LOCATE_REQUEST);
    }

    //  跳转到条形码扫描页面
    private void toScanner() {
        Intent intent = new Intent(OperateInstallActivity.this, ScannerActivity.class);
        startActivityForResult(intent, Data.DATA_INTENT_SCANNER_REQUEST);
    }

    //  显示底部选择对话框
    private void showChoiceDialog() {
        final BottomDialog bottomDialog = new BottomDialog();
        View viewDialog = LayoutInflater.from(OperateInstallActivity.this).inflate(R.layout.dialog_choice_pic, null);
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

    //  显示未完成对话框
    private void showNotCompleteDialog(String msg) {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_message_editable, null);
        Button button = view.findViewById(R.id.btn_dialog_message_cancel);
        TextView textView = view.findViewById(R.id.tv_dialog_message_message);
        textView.setText(msg);
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

    //  部分完成对话框
    private void showPartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OperateInstallActivity.this);
        View viewDialog = LayoutInflater.from(OperateInstallActivity.this).inflate(R.layout.dialog_button_editable, null);
        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        TextView textView = viewDialog.findViewById(R.id.tv_dialog_editable_msg);
        textView.setText("有未完成设备卡片，仍然保存？");
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
                finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //  跳转到选择图片
    private void toChoicePic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        startActivityForResult(intent, picType);
    }

    //  跳转到拍照
    private void toPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues contentValues = new ContentValues(1);
        FileManager fileManager = new FileManager(FileManager.TYPE_PNG);
        fileTempName = fileManager.getPath();
        contentValues.put(MediaStore.Images.Media.DATA, fileTempName);
        mUriPhoto = OperateInstallActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhoto);
        startActivityForResult(intent, picType + 1);
    }

    //  显示信息Dialog
    private void showMessageDialog(String msg) {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    //  上传图片
    private void uploadCarPic(int type, String imgUrl, String path) {
        //  压缩图片
        if (null == imgUrl) {
            imgUrl = "";
        }
        mTempType = type;
        mTempImgUrl = imgUrl;
        showLoading();
        TinyU.tinyPic(path, new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile) {
                //  上传
                if (isSuccess) {
                    new UploadPicU(mNetworkManager).uploadCarPic(eid, token, orderNo, carId, mTempType, mTempImgUrl, outfile, userName);
                } else {
                    mStringMessage = "选择图片出错，请重新上传！";
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                }
            }
        });
    }

    //  上传图片
    private void uploadTerminalPic(int tId, int type, String imgUrl, String path) {
        //  压缩图片
        if (null == imgUrl) {
            imgUrl = "";
        }
        mTempType = type;
        if (mAdapterOperateInstallListDataList.get(itemPosition).isWire()) {
            mTempModel = 1;
        } else {
            mTempModel = 2;
        }
        mTempImgUrl = imgUrl;
        mTempId = tId;
        showLoading();
        TinyU.tinyPic(path, new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile) {
                //  上传
                new UploadPicU(mNetworkManager).uploadPic(eid, token, orderNo, carId, mTempId, mTempType, mTempModel, mTempImgUrl, outfile, userName);
            }
        });
    }

    //  获取完整imei
    private void getWholeImei(String imei) {
        showLoading();
        mNetworkManager.getWholeImei(eid, token, imei, userName);
    }

    //  checkTerminal数据
    private boolean isComplete() {
        return (checkTerComplete() && (!haveTerData || isCarComplete()));
//        for (int i = 0; i < mAdapterOperateInstallListDataList.size(); i++) {
//            Cursor cursor = mDatabaseManager.getTer(ID_MAIN_TERMINAL + i);
//            if (null != cursor && cursor.moveToFirst()) {
//                String id = cursor.getString(0);
//                String tNoOld = cursor.getString(1);
//                String tNoNew = cursor.getString(2);
//                String position = cursor.getString(3);
//                String positionPic = cursor.getString(4);
//                String installPic = cursor.getString(5);
//                String positionPicUrl = cursor.getString(6);
//                String installPicUrl = cursor.getString(7);
//                int model = cursor.getInt(8);
//                int tId = cursor.getInt(9);
//
//                Log.i(TAG, "loadTerminalData: id-->" + id);
//                Log.i(TAG, "loadTerminalData: tNoOld-->" + tNoOld);
//                Log.i(TAG, "loadTerminalData: tNoNew-->" + tNoNew);
//                Log.i(TAG, "loadTerminalData: position-->" + position);
//                Log.i(TAG, "loadTerminalData: positionPic-->" + positionPic);
//                Log.i(TAG, "loadTerminalData: installPic-->" + installPic);
//                Log.i(TAG, "loadTerminalData: positionPicUrl-->" + positionPicUrl);
//                Log.i(TAG, "loadTerminalData: installPicUrl-->" + installPicUrl);
//                Log.i(TAG, "loadTerminalData: model-->" + model);
//                Log.i(TAG, "loadTerminalData: tId-->" + tId);
//
//                if ((null == tNoNew || "".equals(tNoNew))
//                        && (null == position || "".equals(position))
//                        && (null == positionPicUrl || "".equals(positionPicUrl))
//                        && (null == installPicUrl || "".equals(installPicUrl))) {
//                    complete = true;
//                } else if (null != tNoNew && !"".equals(tNoNew)
//                        && null != position && !"".equals(position)
//                        && null != positionPicUrl && !"".equals(positionPicUrl)
//                        && null != installPicUrl && !"".equals(installPicUrl)) {
//                    complete = true;
//                } else {
//                    complete = false;
//                }
//
//                Log.i(TAG, "isComplete: position-->" + idMainTerminal + "-->" + i);
//                Log.i(TAG, "isComplete: complete-->" + complete);
//                Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//
//                if (!complete && completeAll) {
//                    completeAll = false;
//                }
//                AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(i);
//                data.setComplete(complete);
//
//                cursor.close();
//            }
//        }
//        if (!completeAll) {
//            mOperateInstallListAdapter.notifyDataSetChanged();
//        }
//
//        return completeAll;
    }

    //  checkTerminal数据
    private boolean checkTerComplete() {
        boolean completeAll = true;
        boolean complete = true;
        mCompleteCount = 0;
        haveTerData = false;
        int count = mAdapterOperateInstallListDataList.size();
        for (int i = 0; i < count; i++) {
            AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(i);
            Cursor cursor = mDatabaseManager.getTer(ID_MAIN_TERMINAL + i);
            if (null != cursor && cursor.moveToFirst()) {
                String id = cursor.getString(0);
                String tNoOld = cursor.getString(1);
                String tNoNew = cursor.getString(2);
                String position = cursor.getString(3);
                String positionPic = cursor.getString(4);
                String installPic = cursor.getString(5);
                String positionPicUrl = cursor.getString(6);
                String installPicUrl = cursor.getString(7);
                int model = cursor.getInt(8);
                int tId = cursor.getInt(9);

                Log.i(TAG, "loadTerminalData: id-->" + id);
                Log.i(TAG, "loadTerminalData: tNoOld-->" + tNoOld);
                Log.i(TAG, "loadTerminalData: tNoNew-->" + tNoNew);
                Log.i(TAG, "loadTerminalData: position-->" + position);
                Log.i(TAG, "loadTerminalData: positionPic-->" + positionPic);
                Log.i(TAG, "loadTerminalData: installPic-->" + installPic);
                Log.i(TAG, "loadTerminalData: positionPicUrl-->" + positionPicUrl);
                Log.i(TAG, "loadTerminalData: installPicUrl-->" + installPicUrl);
                Log.i(TAG, "loadTerminalData: model-->" + model);
                Log.i(TAG, "loadTerminalData: tId-->" + tId);

                if (data.isWire()) {
                    if ((null == tNoNew || "".equals(tNoNew))
                            && (null == position || "".equals(position))
                            && (null == positionPicUrl || "".equals(positionPicUrl))
                            && (null == installPicUrl || "".equals(installPicUrl))
                            && (0 == model)) {
                        complete = true;
                    } else if (null != tNoNew && !"".equals(tNoNew)
                            && null != position && !"".equals(position)
                            && null != positionPicUrl && !"".equals(positionPicUrl)
                            && null != installPicUrl && !"".equals(installPicUrl)
                            && (0 != model)) {
                        complete = true;
                        haveTerData = true;
                        mCompleteCount++;
                    } else {
                        complete = false;
                        haveTerData = true;
                    }
                } else {
                    if ((null == tNoNew || "".equals(tNoNew))
                            && (null == position || "".equals(position))
                            && (null == positionPicUrl || "".equals(positionPicUrl))
                            && (0 == model)) {
                        complete = true;
                    } else if (null != tNoNew && !"".equals(tNoNew)
                            && null != position && !"".equals(position)
                            && null != positionPicUrl && !"".equals(positionPicUrl)
                            && (0 != model)) {
                        complete = true;
                        haveTerData = true;
                        mCompleteCount++;
                    } else {
                        complete = false;
                        haveTerData = true;
                    }
                }

                data.setComplete(complete);

                Log.i(TAG, "isComplete: position-->" + idMainTerminal + "-->" + i);
                Log.i(TAG, "isComplete: complete-->" + complete);
                Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

                cursor.close();

                if (!complete) {
                    completeAll = false;
                }
            }
        }

        mOperateInstallListAdapter.notifyDataSetChanged();
        return completeAll;
    }

    //checkCar数据
    private boolean isCarComplete() {

        boolean carComplete = true;

        Cursor cursor = mDatabaseManager.getCar(idMainCar);
        if (null != cursor && cursor.moveToFirst()) {
            String carId = cursor.getString(0);
            String carNo = cursor.getString(1);
            String frameNo = cursor.getString(2);
            String carType = cursor.getString(3);
            String carNoPic = cursor.getString(4);
            String frameNoPic = cursor.getString(5);
            String carNoPicUrl = cursor.getString(6);
            String frameNoPicUrl = cursor.getString(7);


            Log.i(TAG, "isCarComplete: carId-->" + carId);
            Log.i(TAG, "isCarComplete: carNo-->" + carNo);
            Log.i(TAG, "isCarComplete: frameNo-->" + frameNo);
            Log.i(TAG, "isCarComplete: carType-->" + carType);
            Log.i(TAG, "isCarComplete: carNoPic-->" + carNoPic);
            Log.i(TAG, "isCarComplete: frameNoPic-->" + frameNoPic);
            Log.i(TAG, "isCarComplete: carNoPicUrl-->" + carNoPicUrl);
            Log.i(TAG, "isCarComplete: frameNoPicUrl-->" + frameNoPicUrl);

            if (!RegularU.isEmpty(carNo) || !RegularU.isEmpty(carNoPicUrl)) {
                if (RegularU.isEmpty(carNoPicUrl)) {
                    carComplete = false;
                    mImageViewCarNo.setBackgroundResource(R.drawable.bg_edit_orange);
                    mTextViewTip1.setVisibility(View.VISIBLE);
                    mTextViewTip1.setText(getString(R.string.tip_pic));
                }
                if (RegularU.isEmpty(carNo)) {
                    carComplete = false;
                    mTextViewTip1.setVisibility(View.VISIBLE);
                    mTextViewTip1.setText(getString(R.string.tip_carno));
                } else if (!RegularU.checkCarNo(carNo)) {
                    // 17-8-20 较验车牌
                    carComplete = false;
                    mTextViewTip1.setVisibility(View.VISIBLE);
                    mTextViewTip1.setText(getString(R.string.tip_carno_fault));
                }
                if (!RegularU.isEmpty(carNo) && !RegularU.isEmpty(carNoPicUrl) && RegularU.checkCarNo(carNo)) {
                    mTextViewTip1.setVisibility(View.GONE);
                    mImageViewCarNo.setBackgroundResource(R.color.colorNull);
                }
            } else {
                mTextViewTip1.setVisibility(View.GONE);
                mImageViewCarNo.setBackgroundResource(R.color.colorNull);
            }

            if (null == carType || "".equals(carType)) {
                carComplete = false;
                mTextViewTip2.setVisibility(View.VISIBLE);
            } else {
                mTextViewTip2.setVisibility(View.GONE);
            }
            if (null == frameNoPicUrl || "".equals(frameNoPicUrl)) {
                carComplete = false;
                mTextViewTip3.setVisibility(View.VISIBLE);
                mImageViewFrameNo.setBackgroundResource(R.drawable.bg_edit_orange);
            } else {
                mImageViewFrameNo.setBackgroundResource(R.color.colorNull);
                mTextViewTip3.setVisibility(View.GONE);
            }
        }

        return carComplete;

    }

    //  显示LoadingFragment
    private void showLoading() {
        if (mLoadingDialogFragment.isAdded()) {
            mLoadingDialogFragment.dismiss();
        }
        mLoadingDialogFragment.show(getFragmentManager(), "LoadingFragment");
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
                if (0 == tid) {
                    mNetworkManager.deletePic(eid, token, orderNo, carId, type, url, userName);
                } else {
                    mNetworkManager.deletePic(eid, token, orderNo, carId, tid, type, url, userName);
                }
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

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mLoadingDialogFragment.isAdded()) {
                mLoadingDialogFragment.dismiss();
            }

            Log.i(TAG, "handleMessage: msg.what-->" + msg.what);
            switch (msg.what) {
                case Data.MSG_ERO: {
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    String imei = (String) msg.obj;
                    getWholeImei(imei);
                    break;
                }
                case Data.MSG_2: {
                    //  check imei success
                    isCheckedImei = true;
                    Bundle bundle = msg.getData();
                    String imei = bundle.getString(KEY_IMEI);
                    boolean repalce = bundle.getBoolean(KEY_REPLACE);
                    AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(itemPosition);
                    data.settNoNew(imei);
                    data.setReplaceAble(repalce);
                    mOperateInstallListAdapter.notifyDataSetChanged();

                    if (isToLocate) {
                        toLocate(imei);
                        isToLocate = false;
                    }
                    break;
                }
                case Data.MSG_11: {
                    //  check imei failure或者获取 whole imei失败
                    isCheckedImei = false;
                    AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(itemPosition);
                    data.settNoNew(null);
                    mOperateInstallListAdapter.notifyDataSetChanged();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    break;
                }
                case Data.MSG_3: {
                    //  loadCarPics, 加载Recycler图片
                    mOperateInstallAdapter.notifyDataSetChanged();
//                    loadCarPics();
                    break;
                }
                case Data.MSG_4: {
                    //  加载carNo图片
                    Picasso.with(OperateInstallActivity.this)
                            .load(mCarNoPic)
                            .fit()
                            .centerInside()
                            .error(R.drawable.ic_camera)
                            .into(mImageViewCarNo);

                    mImageViewCarNo.setBackgroundResource(R.color.colorNull);
                    mImageViewCarNoDelete.setVisibility(View.VISIBLE);
                    break;
                }
                case Data.MSG_5: {
                    //  加载frameNo图片
                    Picasso.with(OperateInstallActivity.this)
                            .load(mCarFramePic)
                            .fit()
                            .centerInside()
                            .error(R.drawable.ic_camera)
                            .into(mImageViewFrameNo);

                    mImageViewFrameNo.setBackgroundResource(R.color.colorNull);
                    mImageViewFrameNoDelete.setVisibility(View.VISIBLE);
                    break;
                }
                case Data.MSG_6: {
                    //  加载listview position图片
//                    ImageView imageView = mListView.getChildAt(itemPosition).findViewById(R.id.iv_item_operate_install_position_pic);
//                    Picasso.with(OperateInstallActivity.this)
//                            .load(itemPath)
//                            .fit()
//                            .centerInside()
//                            .error(R.drawable.ic_camera)
//                            .into(imageView);
                    mOperateInstallListAdapter.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_7: {
                    //  加载listview install图片
//                    ImageView imageView = mListView.getChildAt(itemPosition).findViewById(R.id.iv_item_operate_install_install_pic);
//                    Picasso.with(OperateInstallActivity.this)
//                            .load(itemPath)
//                            .fit()
//                            .centerInside()
//                            .error(R.drawable.ic_camera)
//                            .into(imageView);
                    mOperateInstallListAdapter.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_8: {
                    //  删除图片
                    myHandler.sendEmptyMessage(Data.MSG_12);

                    //  删除车牌号图片
                    if (Data.DATA_UPLOAD_TYPE_1 == mDeleteType) {
                        mCarNoPicUrl = null;
                        mCarNoPic = null;

                        Picasso.with(OperateInstallActivity.this)
                                .load(R.drawable.ic_camera)
                                .fit()
                                .centerInside()
                                .into(mImageViewCarNo);

                        mDatabaseManager.addCarNoPic(idMainCar, null, null);
                        mImageViewCarNoDelete.setVisibility(View.GONE);
                        break;
                    }
                    //  删除车架号图片
                    if (Data.DATA_UPLOAD_TYPE_2 == mDeleteType) {
                        mCarFramePicUrl = null;
                        mCarFramePic = null;

                        Picasso.with(OperateInstallActivity.this)
                                .load(R.drawable.ic_camera)
                                .fit()
                                .centerInside()
                                .into(mImageViewFrameNo);

                        mDatabaseManager.addCarFrameNoPic(idMainCar, null, null);
                        mImageViewFrameNoDelete.setVisibility(View.GONE);
                        break;
                    }

                    //  删除列表中安装位置图片
                    if (Data.DATA_UPLOAD_TYPE_3 == mDeleteType) {
//                        itemPosition 正在操作中的item位置
                        AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(itemPosition);
                        data.setPositionPic(null);
                        data.setPositionPicUrl(null);
                        mOperateInstallListAdapter.notifyDataSetChanged();
                        break;
                    }

                    //  删除列表中接线图片
                    if (Data.DATA_UPLOAD_TYPE_4 == mDeleteType) {
                        AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(itemPosition);
                        data.setInstallPic(null);
                        data.setInstallPicUrl(null);
                        mOperateInstallListAdapter.notifyDataSetChanged();
                        break;
                    }

                    //  删除RecyclerView图片
                    if (Data.DATA_UPLOAD_TYPE_5 == mDeleteType) {
                        mAdapterOperateInstallRecyclerDataList.remove(itemRecycler);
                        int last = mAdapterOperateInstallRecyclerDataList.size() - 1;
                        if (null != mAdapterOperateInstallRecyclerDataList.get(last).getPath()) {
                            mAdapterOperateInstallRecyclerDataList.add(new AdapterOperateInstallRecyclerData());
                        }
                        mOperateInstallAdapter.notifyDataSetChanged();

                        mDatabaseManager.modifyCarPics(idMainCar, itemRecycler, null, null);
                        break;
                    }
                    break;
                }
                case Data.MSG_9: {
                    //  获取订单信息
                    if (null != mCarNoPicUrl) {
                        Picasso.with(OperateInstallActivity.this)
                                .load(mCarNoPic)
                                .fit()
                                .centerInside()
                                .error(R.drawable.ic_camera)
                                .into(mImageViewCarNo);
                        mImageViewCarNoDelete.setVisibility(View.VISIBLE);
                    } else {
                        mImageViewCarNoDelete.setVisibility(View.GONE);
                    }

                    if (null != mCarFramePicUrl) {
                        Picasso.with(OperateInstallActivity.this)
                                .load(mCarFramePic)
                                .fit()
                                .centerInside()
                                .error(R.drawable.ic_camera)
                                .into(mImageViewFrameNo);
                        mImageViewFrameNoDelete.setVisibility(View.VISIBLE);
                    } else {
                        mImageViewFrameNoDelete.setVisibility(View.GONE);
                    }

                    if (mAdapterOperateInstallRecyclerDataList.size() <= PIC_MAX) {
                        mAdapterOperateInstallRecyclerDataList.add(new AdapterOperateInstallRecyclerData());
                    }

                    Log.i(TAG, "handleMessage: mCarNoPic-->" + mCarNoPic);
                    Log.i(TAG, "handleMessage: mCarFramePic-->" + mCarFramePic);
                    Log.i(TAG, "handleMessage: mCarFrameNo-->" + mCarFrameNo);
                    Log.i(TAG, "handleMessage: mCarNo-->" + mCarNo);
                    Log.i(TAG, "handleMessage: mCarBrand-->" + mCarBrand);

                    //  判定车牌号及车型是否可编辑
                    if (null == mCarNo || "".equals(mCarNo)) {
                        mEditTextCarNo.setEnabled(true);
                    } else {
                        mEditTextCarNo.setEnabled(false);
                    }
                    if (null == mCarBrand || "".equals(mCarBrand)) {
                        mEditTextCarType.setEnabled(true);
                    } else {
                        mEditTextCarType.setEnabled(false);
                    }

                    //  将车辆信息置为本地最新
                    loadCarData();
                    mTextViewFrameNo.setText(mCarFrameNo);
                    mEditTextCarNo.setText(mCarNo);
                    mEditTextCarType.setText(mCarBrand);

                    mOperateInstallAdapter.notifyDataSetChanged();
                    mOperateInstallListAdapter.notifyDataSetChanged();

                    myHandler.sendEmptyMessage(Data.MSG_10);
                    break;
                }
                case Data.MSG_10: {
                    loadTerminalData();
                    break;
                }
                case Data.MSG_12: {
                    //  Toast
                    new ToastU(OperateInstallActivity.this).showToast(mStringMessage);
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default-->" + msg.what);
                }
            }
        }
    }
}
