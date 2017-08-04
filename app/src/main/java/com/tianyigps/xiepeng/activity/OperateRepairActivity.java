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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.bean.StartOrderInfoBean;
import com.tianyigps.xiepeng.bean.UploadPicBean;
import com.tianyigps.xiepeng.bean.WholeImeiBean;
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.interfaces.OnGetWholeIMEIListener;
import com.tianyigps.xiepeng.interfaces.OnGetWorkerOrderInfoStartListener;
import com.tianyigps.xiepeng.interfaces.OnUploadPicListener;
import com.tianyigps.xiepeng.manager.DatabaseManager;
import com.tianyigps.xiepeng.manager.FileManager;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;
import com.tianyigps.xiepeng.utils.MessageDialogU;
import com.tianyigps.xiepeng.utils.TinyU;
import com.tianyigps.xiepeng.utils.UploadPicU;
import com.tianyigps.xiepeng.utils.Uri2FileU;
import com.yundian.bottomdialog.BottomDialog;

import java.io.File;

public class OperateRepairActivity extends BaseActivity {

    private static final String TAG = "OperateRepairActivity";

    private static final int PIC_WIDTH = 300;
    private static final int PIC_HEIGHT = 150;

    private static final int INTENT_CHOICE_P = 1;
    private static final int INTENT_PHOTO_P = 2;
    private static final int INTENT_CHOICE_I = 3;
    private static final int INTENT_PHOTO_I = 4;
    private int picType = 1;   //  1 = 安装位置，3 = 接线图

    private ImageView mImageViewLocate, mImageViewPositionOld, mImageViewInstallOld, mImageViewPositionNew, mImageViewInstallNew;
    private TextView mTextViewCarNo, mTextViewFrameNo, mTextViewTypeAndName, mTextViewtNo, mTextViewPositionOld,
            mTextViewInstallName, mTextViewInstallPhone, mTextViewDescribe, mTextViewCount;
    private EditText mEditTextPosition, mEditTextExplain;
    private TextView mTextViewSave;

    private RelativeLayout mRelativeLayoutReplace;
    private TextView mTextViewState;
    private ImageView mImageViewScanner, mImageViewReplaceLocate;
    private EditText mEditTextNewImei;

    private DatabaseManager mDatabaseManager;
    private String mStringPosition, mStringPositionPath, mStringInstallPath, mStringExplain, mStringNewtNo;

    private Uri mUriPhoto;
    private String fileTempName;

    //  网络数据请求
    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private SharedpreferenceManager mSharedpreferenceManager;
    private int eid;
    private String token;
    private String orderNo;
    private String tNo;
    private String baseUrl;
    private String mStringMessage = "数据请求失败，请检查网络！";
    private String wholeImei;
    private int tid;
    private int tType;

    //  网络数据返回
    private String carNoG, frameNoG, typeAndNameG, positionG, positionPicG, installPicG, installNameG, installPhoneG;

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
            String imei = data.getStringExtra(Data.DATA_SCANNER);
            mEditTextNewImei.setText(imei);
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

                mDatabaseManager.addRepairPositionPic(tid, path);
                String imgUrl = mDatabaseManager.getRepairPositionUrl(tid);
                uploadPic(Data.DATA_UPLOAD_TYPE_3, imgUrl, path);

                Picasso.with(this).load(selectedImage).resize(PIC_WIDTH, PIC_HEIGHT).error(R.drawable.ic_camera).into
                        (mImageViewPositionNew);
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

                mDatabaseManager.addRepairPositionPic(tid, path);
                String imgUrl = mDatabaseManager.getRepairPositionUrl(tid);
                uploadPic(Data.DATA_UPLOAD_TYPE_3, imgUrl, path);

                Picasso.with(this).load(uri).resize(PIC_WIDTH, PIC_HEIGHT).error(R.drawable.ic_camera).into(mImageViewPositionNew);
                break;
            }
            case INTENT_CHOICE_I: {
                Uri selectedImage = data.getData();

                String path = new Uri2FileU(OperateRepairActivity.this).getRealPathFromUri(selectedImage);
                Log.i(TAG, "onActivityResult: path-->" + path);

                mDatabaseManager.addRepairInstallPic(tid, path);
                String imgUrl = mDatabaseManager.getRepairInstallUrl(tid);
                uploadPic(Data.DATA_UPLOAD_TYPE_4, imgUrl, path);

                Picasso.with(this).load(selectedImage).resize(PIC_WIDTH, PIC_HEIGHT).error(R.drawable.ic_camera).into(mImageViewInstallNew);
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
                mDatabaseManager.addRepairInstallPic(tid, path);
                String imgUrl = mDatabaseManager.getRepairInstallUrl(tid);
                uploadPic(Data.DATA_UPLOAD_TYPE_4, imgUrl, path);

                Picasso.with(this).load(uri).resize(PIC_WIDTH, PIC_HEIGHT).error(R.drawable.ic_camera).into(mImageViewInstallNew);
                break;
            }
            default: {
                Log.i(TAG, "onActivityResult: default");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseManager.close();
    }

    private void init() {
        this.setTitleText("维修");

        Intent intent = getIntent();
        eid = intent.getIntExtra(Data.DATA_INTENT_EID, 0);
        token = intent.getStringExtra(Data.DATA_INTENT_TOKEN);
        orderNo = intent.getStringExtra(Data.DATA_INTENT_ORDER_NO);
        tNo = intent.getStringExtra(Data.DATA_INTENT_T_NO);
        tid = intent.getIntExtra(Data.DATA_INTENT_T_ID, 0);

        mImageViewLocate = findViewById(R.id.iv_activity_operate_default_locate);
        mImageViewPositionOld = findViewById(R.id.iv_activity_operate_default_position_pic);
        mImageViewInstallOld = findViewById(R.id.iv_activity_operate_default_install_pic);
        mImageViewPositionNew = findViewById(R.id.iv_activity_operate_default_position_pic_new);
        mImageViewInstallNew = findViewById(R.id.iv_activity_operate_default_install_pic_new);

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

        mTextViewSave = findViewById(R.id.tv_activity_operate_default_install_save);

        mTextViewState = findViewById(R.id.tv_activity_operate_remove_state);
        mRelativeLayoutReplace = findViewById(R.id.rl_activity_operate_replace);
        mImageViewScanner = findViewById(R.id.iv_activity_operate_replace_scanner);
        mImageViewReplaceLocate = findViewById(R.id.iv_activity_operate_replace_locate);
        mEditTextNewImei = findViewById(R.id.et_activity_operate_replace_device_no);

        mDatabaseManager = new DatabaseManager(OperateRepairActivity.this);

        loadSavedData();

        Log.i(TAG, "init: tNo-->" + tNo);
        mTextViewFrameNo.setText(tNo);

        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();
        mSharedpreferenceManager = new SharedpreferenceManager(this);
        baseUrl = mSharedpreferenceManager.getImageBaseUrl();

        mNetworkManager.getWorkerOrderInfoStart(eid, token, orderNo);
    }

    private void setEventListener() {

        mImageViewLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/26 定位
                toLocate(tNo);
            }
        });

        mTextViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/26 保存
                mStringPosition = mEditTextPosition.getText().toString();
                mStringExplain = mEditTextExplain.getText().toString();
                mStringNewtNo = mEditTextNewImei.getText().toString();
                Log.i(TAG, "onSignClick: mStringNewtNo-->" + mStringNewtNo);
                if ("".equals(mStringPosition)) {
                    mStringPosition = null;
                }
                if ("".equals(mStringExplain)) {
                    mStringExplain = null;
                }
                if ("".equals(mStringNewtNo) || mRelativeLayoutReplace.getVisibility() == View.GONE) {
                    mDatabaseManager.addRepair(tid, mStringPosition, mStringExplain);
                } else {
                    mDatabaseManager.addRepair(tid, mStringPosition, mStringExplain, mStringNewtNo);
                }

                showFinishDialog("数据已保存！");
            }
        });

        mImageViewPositionNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/26 选择图片或拍照
                picType = 1;
                showChoiceDialog();
            }
        });

        mImageViewInstallNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/26 选择图片或拍照
                picType = 3;
                showChoiceDialog();
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

        mImageViewReplaceLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imei = mEditTextNewImei.getText().toString();
                getWholeImei(imei);
            }
        });

        mEditTextExplain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String count = i1 + "/200";
                mTextViewCount.setText(count);
            }

            @Override
            public void afterTextChanged(Editable editable) {
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
                        if (tNo.equals(carTerminalListBean.getTNo())) {
                            carNoG = carListBean.getCarNo();
                            frameNoG = carListBean.getCarVin();

                            int type = carTerminalListBean.getTerminalType();
                            String terminalType;
                            String terminalName = carTerminalListBean.getTerminalName();
                            if (null == terminalName) {
                                terminalName = "";
                            } else {
                                terminalName = "（" + terminalName + "）";
                            }
                            switch (type) {
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
                                    Log.i(TAG, "onSuccess: default");
                                }
                            }
                            typeAndNameG = terminalType + terminalName;
                            positionG = carTerminalListBean.getNewInstallLocation();
                            positionPicG = baseUrl + carTerminalListBean.getNewInstallLocationPic();
                            installPicG = baseUrl + carTerminalListBean.getNewWiringDiagramPic();
                            installNameG = objBean.getDispatchContactName();
                            installPhoneG = objBean.getDispatchContactPhone();
                            tType = carTerminalListBean.getNewTerminalType();

                            myHandler.sendEmptyMessage(Data.MSG_1);
                            return;
                        }
                    }
                }

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
                    onFailure();
                    return;
                }
                wholeImei = wholeImeiBean.getObj().getImei();
                myHandler.sendEmptyMessage(Data.MSG_2);
            }
        });

        mNetworkManager.setOnUploadPicListener(new OnUploadPicListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: uploadpic");
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
                    mDatabaseManager.addRepairPositionUrl(tid, imgUrl);
                    return;
                }
                mDatabaseManager.addRepairInstallUrl(tid, imgUrl);
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
        new MessageDialogU(this).show(msg);
    }

    private void loadSavedData() {
        Cursor cursor = mDatabaseManager.getRepair(tid);
        if (null != cursor && cursor.moveToFirst()) {
            mStringPosition = cursor.getString(2);
            mStringPositionPath = cursor.getString(3);
            mStringInstallPath = cursor.getString(4);
            mStringExplain = cursor.getString(5);
            mStringNewtNo = cursor.getString(6);
            Log.i(TAG, "loadSavedData: tid-->" + cursor.getInt(0));
            Log.i(TAG, "loadSavedData: mStringPosition-->" + mStringPosition);
            Log.i(TAG, "loadSavedData: mStringPositionPath-->" + mStringPositionPath);
            Log.i(TAG, "loadSavedData: mStringInstallPath-->" + mStringInstallPath);
            Log.i(TAG, "loadSavedData: mStringExplain-->" + mStringExplain);
            Log.i(TAG, "loadSavedData: mStringNewtNo-->" + mStringNewtNo);

            mEditTextPosition.setText(mStringPosition);
            mEditTextExplain.setText(mStringExplain);
            if (null != mStringPositionPath) {
                File file = new File(mStringPositionPath);
                if (file.exists()) {
                    Picasso.with(this).load(file).resize(PIC_WIDTH, PIC_HEIGHT).error(R.drawable.ic_camera).into(mImageViewPositionNew);
                }
            }
            if (null != mStringInstallPath) {
                File file = new File(mStringInstallPath);
                if (file.exists()) {
                    Picasso.with(this).load(file).resize(PIC_WIDTH, PIC_HEIGHT).error(R.drawable.ic_camera).into(mImageViewInstallNew);
                }
            }
            if (null != mStringNewtNo) {
                mRelativeLayoutReplace.setVisibility(View.VISIBLE);
                mEditTextNewImei.setText(mStringNewtNo);
                mTextViewCount.setText(R.string.not_replace);
            } else {
                mRelativeLayoutReplace.setVisibility(View.GONE);
                mTextViewCount.setText(R.string.repair_replace);
            }
            cursor.close();
        }
    }

    //  上传图片
    private void uploadPic(int type, String imgUrl, String path) {
        //  压缩图片
        String pathT = TinyU.tinyPic(path);
        new UploadPicU(mNetworkManager).uploadPic(eid, token, orderNo, tid, type, tType, imgUrl, pathT);
    }

    //  获取完整imei
    private void getWholeImei(String imei) {
        mNetworkManager.getWholeImei(eid, token, imei);
    }

    private void toLocate(String imei) {
        Intent intent = new Intent(OperateRepairActivity.this, LocateActivity.class);
        intent.putExtra(Data.DATA_INTENT_LOCATE_TYPE, false);
        intent.putExtra(Data.DATA_INTENT_LOCATE_IMEI, imei);
        startActivity(intent);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
                    mTextViewInstallName.setText(installNameG);
                    mTextViewInstallPhone.setText(installPhoneG);
                    Picasso.with(OperateRepairActivity.this)
                            .load(positionPicG)
                            .error(R.color.colorNull)
                            .resize(PIC_WIDTH, PIC_HEIGHT)
                            .into(mImageViewPositionOld);
                    Picasso.with(OperateRepairActivity.this)
                            .load(installPicG)
                            .error(R.color.colorNull)
                            .resize(PIC_WIDTH, PIC_HEIGHT)
                            .into(mImageViewInstallOld);
                    break;
                }
                case Data.MSG_2: {
                    mEditTextNewImei.setText(wholeImei);
                    toLocate(wholeImei);
                    break;
                }
                case Data.MSG_3: {
                    showMessageDialog(mStringMessage);
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
