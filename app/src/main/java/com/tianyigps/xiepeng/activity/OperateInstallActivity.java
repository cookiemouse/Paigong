package com.tianyigps.xiepeng.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.adapter.OperateInstallAdapter;
import com.tianyigps.xiepeng.adapter.OperateInstallListAdapter;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.bean.UploadPicBean;
import com.tianyigps.xiepeng.bean.WholeImeiBean;
import com.tianyigps.xiepeng.customview.MyListView;
import com.tianyigps.xiepeng.customview.MyRecyclerView;
import com.tianyigps.xiepeng.data.AdapterOperateInstallListData;
import com.tianyigps.xiepeng.data.AdapterOperateInstallRecyclerData;
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.interfaces.OnGetWholeIMEIListener;
import com.tianyigps.xiepeng.interfaces.OnUploadPicListener;
import com.tianyigps.xiepeng.manager.DatabaseManager;
import com.tianyigps.xiepeng.manager.FileManager;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;
import com.tianyigps.xiepeng.utils.TinyU;
import com.tianyigps.xiepeng.utils.UploadPicU;
import com.tianyigps.xiepeng.utils.Uri2FileU;
import com.yundian.bottomdialog.BottomDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OperateInstallActivity extends BaseActivity {

    private static final String TAG = "OperateInstall";
    private static final int DELAY = 2000;
    private static final int PIC_MAX = 5;

    private ImageView mImageViewCarNo, mImageViewFrameNo;
    private EditText mEditTextCarNo, mEditTextCarType;
    private TextView mTextFViewrameNo;

    private Button mButtonSave;
    private View mViewSave;

    private MyRecyclerView mRecyclerView;
    private MyListView mListView;

    private OperateInstallAdapter mOperateInstallAdapter;
    private OperateInstallListAdapter mOperateInstallListAdapter;

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
    private String mStringMessage = "请求数据失败，请检查网络！";
    private int eid;
    private String token;
    private String orderNo;
    private String frameNo;
    private int carId;
    private int wiredNum;
    private int wirelessNum;

    // id，主键
    // TODO: 2017/8/1 测试车辆数据库
    private int idMainCar = 100;
    private String idMainTerminal = idMainCar + "T";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_install);

        init();

        setEventListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Data.DATA_INTENT_SCANNER_REQUEST && resultCode == Data.DATA_INTENT_SCANNER_RESULT) {
            Log.i(TAG, "onActivityResult: qrcode-->" + data.getStringExtra(Data.DATA_SCANNER));
            String imei = data.getStringExtra(Data.DATA_SCANNER);
            Message message = new Message();
            message.obj = imei;
            message.what = Data.MSG_2;
            myHandler.sendMessage(message);
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
                itemPath = path;

                String imgUrl = mAdapterOperateInstallListDataList.get(itemPosition).getPositionPicUrl();

                uploadTerminalPic(Data.DATA_UPLOAD_TYPE_3, imgUrl, path);
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

                itemPath = path;

                String imgUrl = mAdapterOperateInstallListDataList.get(itemPosition).getPositionPicUrl();

                uploadTerminalPic(Data.DATA_UPLOAD_TYPE_3, imgUrl, path);
                break;
            }
            case INTENT_CHOICE_I: {
                Uri selectedImage = data.getData();

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(selectedImage);
                Log.i(TAG, "onActivityResult: path-->" + path);

                itemPath = path;

                String imgUrl = mAdapterOperateInstallListDataList.get(itemPosition).getInstallPicUrl();

                uploadTerminalPic(Data.DATA_UPLOAD_TYPE_4, imgUrl, path);
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

                itemPath = path;

                String imgUrl = mAdapterOperateInstallListDataList.get(itemPosition).getInstallPicUrl();

                uploadTerminalPic(Data.DATA_UPLOAD_TYPE_4, imgUrl, path);
                break;
            }
            case INTENT_CHOICE_C: {
                Uri selectedImage = data.getData();

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(selectedImage);
                Log.i(TAG, "onActivityResult: path-->" + path);
                itemPath = path;

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
                itemPath = path;
                uploadCarPic(Data.DATA_UPLOAD_TYPE_1, urlCarNoPic, path);
                break;
            }
            case INTENT_CHOICE_F: {
                Uri selectedImage = data.getData();

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(selectedImage);
                Log.i(TAG, "onActivityResult: path-->" + path);

                itemPath = path;

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
                itemPath = path;
                uploadCarPic(Data.DATA_UPLOAD_TYPE_2, urlFrameNoPic, path);
                break;
            }
            case INTENT_CHOICE_R: {
                Uri selectedImage = data.getData();

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(selectedImage);
                itemPath = path;
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
                itemPath = path;

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
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        mDatabaseManager.close();
        super.onDestroy();
    }

    private void init() {
        this.setTitleText("");

        Intent intent = getIntent();
        orderNo = intent.getStringExtra(Data.DATA_INTENT_ORDER_NO);
        frameNo = intent.getStringExtra(Data.DATA_INTENT_FRAME_NO);
        carId = intent.getIntExtra(Data.DATA_INTENT_INSTALL_CAR_ID, 0);
        wiredNum = intent.getIntExtra(Data.DATA_INTENT_INSTALL_WIRED_NUM, 0);
        wirelessNum = intent.getIntExtra(Data.DATA_INTENT_INSTALL_WIRELESS_NUM, 0);

        Log.i(TAG, "init: orderNo-->" + orderNo);
        Log.i(TAG, "init: frameNo-->" + frameNo);
        Log.i(TAG, "init: carId-->" + carId);
        Log.i(TAG, "init: wiredNum-->" + wiredNum);
        Log.i(TAG, "init: wirelessNum-->" + wirelessNum);

        mImageViewCarNo = findViewById(R.id.iv_layout_operate_install_car_pic);
        mImageViewFrameNo = findViewById(R.id.iv_layout_operate_install_frame_pic);
        mEditTextCarNo = findViewById(R.id.et_layout_operate_install_car_no);
        mTextFViewrameNo = findViewById(R.id.tv_layout_operate_install_frame_no);
        mEditTextCarType = findViewById(R.id.et_layout_operate_install_car_type);

        mViewSave = LayoutInflater.from(this).inflate(R.layout.layout_activity_installing_next, null);
        mButtonSave = mViewSave.findViewById(R.id.btn_layout_activity_installing_next);
        mButtonSave.setText(R.string.save);

        mTextFViewrameNo.setText(frameNo);

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

        mOperateInstallListAdapter = new OperateInstallListAdapter(OperateInstallActivity.this,
                mAdapterOperateInstallListDataList);
        mListView.setAdapter(mOperateInstallListAdapter);

        mListView.addFooterView(mViewSave);

        mNetworkManager = new NetworkManager();
        mSharedpreferenceManager = new SharedpreferenceManager(this);
        myHandler = new MyHandler();

        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();

        loadCarData();

        loadTerminalData();
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

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  2017/8/3
                saveData();
            }
        });

        mOperateInstallAdapter.setOnItemOperateListener(new OperateInstallAdapter.OnItemOperateListener() {
            @Override
            public void onDeleteClick(int position) {
                //  2017/8/1 删除图片
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

        mOperateInstallListAdapter.setOnItemOperateListener(new OperateInstallListAdapter.OnItemOperateListener() {
            @Override
            public void onScannerClick(int position) {
                //  2017/7/31 扫描
                itemPosition = position;
                toScanner();
            }

            @Override
            public void onTextChanged(int position, String imei) {
                //  2017/7/31 检测imei
                itemPosition = position;

                myHandler.removeMessages(Data.MSG_1);

                if ("".equals(imei)) {
                    Message message = new Message();
                    message.obj = imei;
                    message.what = Data.MSG_2;
                    myHandler.sendMessageDelayed(message, DELAY);
                    return;
                }

                Message message = new Message();
                message.obj = imei;
                message.what = Data.MSG_1;
                myHandler.sendMessageDelayed(message, DELAY);
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
                String tNo = mAdapterOperateInstallListDataList.get(position).gettNoNew();
                toLocate(tNo);
            }

            @Override
            public void onPositionPicClick(int position) {
                //  2017/7/31 安装位置图片
                itemPosition = position;

                picType = 1;
                showChoiceDialog();
            }

            @Override
            public void onInstallPicClick(int position) {
                //  2017/7/31 接线图图片
                itemPosition = position;

                picType = 3;
                showChoiceDialog();
            }
        });

        mNetworkManager.setOnGetWholeIMEIListener(new OnGetWholeIMEIListener() {
            @Override
            public void onFailure() {
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                WholeImeiBean wholeImeiBean = gson.fromJson(result, WholeImeiBean.class);
                if (!wholeImeiBean.isSuccess()) {
                    mStringMessage = wholeImeiBean.getMsg();
                    onFailure();
                    return;
                }
                Message message = new Message();
                WholeImeiBean.ObjBean objBean = wholeImeiBean.getObj();
                message.obj = objBean.getImei();
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

                Log.i(TAG, "onSuccess: picType-->" + picType);
                switch (picType) {
                    //  Recycler
                    case INTENT_CHOICE_R: {
                    }
                    case INTENT_PHOTO_R: {
                        mDatabaseManager.addCarPics(idMainCar, itemRecycler, itemPath, imgUrl);
                        myHandler.sendEmptyMessage(Data.MSG_3);
                        break;
                    }
                    // carNo
                    case INTENT_CHOICE_C: {
                    }
                    case INTENT_PHOTO_C: {
                        mDatabaseManager.addCarNoPic(idMainCar, itemPath, imgUrl);
                        myHandler.sendEmptyMessage(Data.MSG_4);
                        break;
                    }
                    // frameNo
                    case INTENT_CHOICE_F: {
                    }
                    case INTENT_PHOTO_F: {
                        mDatabaseManager.addCarFrameNoPic(idMainCar, itemPath, imgUrl);
                        myHandler.sendEmptyMessage(Data.MSG_5);
                        break;
                    }

                    //  positionPic
                    case INTENT_CHOICE_P: {
                    }
                    case INTENT_PHOTO_P: {

                        mDatabaseManager.addOrder(orderNo, carId, id);

                        idMainTerminal = idMainTerminal + itemPosition;
                        mDatabaseManager.addTerPositionPic(idMainTerminal, itemPath, imgUrl);
                        mDatabaseManager.addTerId(idMainTerminal, id);
                        AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(itemPosition);
                        data.setPositionPic(itemPath);
                        data.setPositionPicUrl(imgUrl);
                        myHandler.sendEmptyMessage(Data.MSG_6);
                        break;
                    }

                    //  installPic
                    case INTENT_CHOICE_I: {
                    }
                    case INTENT_PHOTO_I: {

                        mDatabaseManager.addOrder(orderNo, carId, id);

                        idMainTerminal = idMainTerminal + itemPosition;
                        mDatabaseManager.addTerInstallPic(idMainTerminal, itemPath, imgUrl);
                        mDatabaseManager.addTerId(idMainTerminal, id);
                        AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(itemPosition);
                        data.setInstallPic(itemPath);
                        data.setInstallPicUrl(imgUrl);
                        myHandler.sendEmptyMessage(Data.MSG_7);
                        break;
                    }
                }
            }
        });
    }

    //  获取并显示数据库里的车辆数据
    private void loadCarData() {
        Cursor cursor = mDatabaseManager.getCar(idMainCar);
        if (null != cursor && cursor.moveToFirst()) {
            Log.i(TAG, "loadCardata: idMain-->" + cursor.getInt(0));
            String carNo = cursor.getString(1);
            String frameNo = cursor.getString(2);
            String carType = cursor.getString(3);
            String carNoPic = cursor.getString(4);
            String frameNoPic = cursor.getString(5);
            urlCarNoPic = cursor.getString(6);
            urlFrameNoPic = cursor.getString(7);

            mEditTextCarNo.setText(carNo);
//            mTextFViewrameNo.setText(frameNo);
            mEditTextCarType.setText(carType);

            Log.i(TAG, "loadCarData: carNoPic-->" + carNoPic);
            Log.i(TAG, "loadCarData: urlCarNoPic-->" + urlCarNoPic);
            Log.i(TAG, "loadCarData: frameNoPic-->" + frameNoPic);
            Log.i(TAG, "loadCarData: urlFrameNoPic-->" + urlFrameNoPic);

            if (null != carNoPic) {
                Picasso.with(this)
                        .load(new File(carNoPic))
                        .fit()
                        .centerInside()
                        .error(R.drawable.ic_camera)
                        .into(mImageViewCarNo);
            }

            if (null != frameNoPic) {
                Picasso.with(this)
                        .load(new File(frameNoPic))
                        .fit()
                        .centerInside()
                        .error(R.drawable.ic_camera)
                        .into(mImageViewFrameNo);
            }
            cursor.close();
        }
        //  加载Recycler图片
        myHandler.sendEmptyMessage(Data.MSG_3);
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
            Cursor cursor = mDatabaseManager.getTer(idMainTerminal + i);
            if (null != cursor && cursor.moveToFirst()) {
                String id = cursor.getString(0);
                String tNoOld = cursor.getString(1);
                String tNoNew = cursor.getString(2);
                String position = cursor.getString(3);
                String positionPic = cursor.getString(4);
                String installPic = cursor.getString(5);
                String positionPicUrl = cursor.getString(6);
                String installPicUrl = cursor.getString(7);

                Log.i(TAG, "loadTerminalData: id-->" + id);
                Log.i(TAG, "loadTerminalData: tNoOld-->" + tNoOld);
                Log.i(TAG, "loadTerminalData: tNoNew-->" + tNoNew);
                Log.i(TAG, "loadTerminalData: position-->" + position);
                Log.i(TAG, "loadTerminalData: positionPic-->" + positionPic);
                Log.i(TAG, "loadTerminalData: installPic-->" + installPic);
                Log.i(TAG, "loadTerminalData: positionPicUrl-->" + positionPicUrl);
                Log.i(TAG, "loadTerminalData: installPicUrl-->" + installPicUrl);
                Log.i(TAG, "........................................");

                cursor.close();

                AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(i);
                data.settNoNew(tNoNew);
                data.setPosition(position);
                data.setPositionPic(positionPic);
                data.setInstallPic(installPic);
                data.setPositionPicUrl(positionPicUrl);
                data.setInstallPicUrl(installPicUrl);
            }
        }
        mOperateInstallListAdapter.notifyDataSetChanged();
    }

    //  保存数据
    private void saveData() {
        String carNo = mEditTextCarNo.getText().toString();
        String carType = mEditTextCarType.getText().toString();

        mDatabaseManager.addCarInfo(idMainCar, carNo, carType);

        int i = 0;
        for (AdapterOperateInstallListData data : mAdapterOperateInstallListDataList) {
            String tNoOld = data.gettNoOld();
            String tNoNew = data.gettNoNew();
            EditText editText = mListView.getChildAt(i).findViewById(R.id.et_item_operate_install_position);
            String position = editText.getText().toString();

            Log.i(TAG, "onClick: tNoOld-->" + tNoOld);
            Log.i(TAG, "onClick: tNoNew-->" + tNoNew);
            Log.i(TAG, "onClick: position-->" + position);
            data.setPosition(position);

            String id = idMainTerminal + i;
            Log.i(TAG, "onClick: idMainTerminal-->" + id);

            mDatabaseManager.addTerInfo(id, tNoOld, tNoNew, position);
            i++;
//            if (null != tNoNew && !"".equals(tNoNew) || !"".equals(position) || null != tNoOld) {
//            }

            Log.i(TAG, "----------------------------------------");
        }

        isComplete();
    }

    //  RecycleView删除图片
    private void removePicFromRecycler(int position) {
        mAdapterOperateInstallRecyclerDataList.remove(position);
        int last = mAdapterOperateInstallRecyclerDataList.size() - 1;
        if (null != mAdapterOperateInstallRecyclerDataList.get(last).getPath()) {
            mAdapterOperateInstallRecyclerDataList.add(new AdapterOperateInstallRecyclerData());
        }
        mOperateInstallAdapter.notifyDataSetChanged();
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
        startActivity(intent);
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
        String pathT = TinyU.tinyPic(path);
        // TODO: 2017/8/1 上传图片，从intent传相关值
        new UploadPicU(mNetworkManager).uploadCarPic(eid, token, orderNo, carId, type, imgUrl, pathT);
    }

    //  上传图片
    private void uploadTerminalPic(int type, String imgUrl, String path) {
        //  压缩图片
        String pathT = TinyU.tinyPic(path);
        //  上传
        new UploadPicU(mNetworkManager).uploadPic(eid, token, orderNo, carId, type, 1, imgUrl, pathT);
    }

    //  获取完整imei
    private void getWholeImei(String imei) {
        mNetworkManager.getWholeImei(eid, token, imei);
    }

    //  check数据
    private boolean isComplete() {
        // TODO: 2017/8/4 检测数据完整性

        boolean complete = true;
        boolean completeAll = true;

        for (int i = 0; i < mAdapterOperateInstallListDataList.size(); i++) {
            Cursor cursor = mDatabaseManager.getTer(idMainTerminal + i);
            if (null != cursor && cursor.moveToFirst()) {
                String id = cursor.getString(0);
                String tNoOld = cursor.getString(1);
                String tNoNew = cursor.getString(2);
                String position = cursor.getString(3);
                String positionPic = cursor.getString(4);
                String installPic = cursor.getString(5);
                String positionPicUrl = cursor.getString(6);
                String installPicUrl = cursor.getString(7);

                Log.i(TAG, "loadTerminalData: tNoOld-->" + tNoOld);
                Log.i(TAG, "loadTerminalData: tNoNew-->" + tNoNew);
                Log.i(TAG, "loadTerminalData: position-->" + position);
                Log.i(TAG, "loadTerminalData: positionPic-->" + positionPic);
                Log.i(TAG, "loadTerminalData: installPic-->" + installPic);

                if (null == tNoNew || "".equals(tNoNew) || "".equals(position) && null == positionPic && null == installPic) {
                    complete = true;
                } else if ("".equals(position) || null == positionPic || null == installPic) {
                    complete = false;
                } else if (!"".equals(tNoNew) && !"".equals(position) && !"".equals(positionPic) && !"".equals(installPic)) {
                    complete = true;
                }

                Log.i(TAG, "isComplete: position-->" + idMainTerminal + i);
                Log.i(TAG, "isComplete: complete-->" + complete);

                if (!complete) {
                    AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(i);
                    data.setComplete(false);
                    completeAll = false;
                }

                cursor.close();
            }
        }
        if (!completeAll) {
            mOperateInstallListAdapter.notifyDataSetChanged();
        }
        return completeAll;
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
                    //  whole imei
                    String imei = (String) msg.obj;
                    AdapterOperateInstallListData data = mAdapterOperateInstallListDataList.get(itemPosition);
                    data.settNoNew(imei);
                    mOperateInstallListAdapter.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_3: {
                    //  loadCarPics, 加载Recycler图片
                    loadCarPics();
                    break;
                }
                case Data.MSG_4: {
                    //  加载carNo图片
                    Picasso.with(OperateInstallActivity.this)
                            .load(new File(itemPath))
                            .fit()
                            .centerInside()
                            .error(R.drawable.ic_camera)
                            .into(mImageViewCarNo);
                    break;
                }
                case Data.MSG_5: {
                    //  加载frameNo图片
                    Picasso.with(OperateInstallActivity.this)
                            .load(new File(itemPath))
                            .fit()
                            .centerInside()
                            .error(R.drawable.ic_camera)
                            .into(mImageViewFrameNo);
                    break;
                }
                case Data.MSG_6: {
                    //  加载listview position图片
                    ImageView imageView = mListView.getChildAt(itemPosition).findViewById(R.id.iv_item_operate_install_position_pic);
                    Picasso.with(OperateInstallActivity.this)
                            .load(new File(itemPath))
                            .fit()
                            .centerInside()
                            .error(R.drawable.ic_camera)
                            .into(imageView);
                    break;
                }
                case Data.MSG_7: {
                    //  加载listview install图片
                    ImageView imageView = mListView.getChildAt(itemPosition).findViewById(R.id.iv_item_operate_install_install_pic);
                    Picasso.with(OperateInstallActivity.this)
                            .load(new File(itemPath))
                            .fit()
                            .centerInside()
                            .error(R.drawable.ic_camera)
                            .into(imageView);
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
