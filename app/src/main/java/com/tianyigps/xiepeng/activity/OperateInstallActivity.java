package com.tianyigps.xiepeng.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.adapter.OperateInstallAdapter;
import com.tianyigps.xiepeng.adapter.OperateInstallListAdapter;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.bean.WholeImeiBean;
import com.tianyigps.xiepeng.customview.MyListView;
import com.tianyigps.xiepeng.customview.MyRecyclerView;
import com.tianyigps.xiepeng.data.AdapterOperateInstallListData;
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.interfaces.OnGetWholeIMEIListener;
import com.tianyigps.xiepeng.manager.FileManager;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;
import com.tianyigps.xiepeng.utils.Uri2FileU;
import com.yundian.bottomdialog.BottomDialog;

import java.util.ArrayList;
import java.util.List;

public class OperateInstallActivity extends BaseActivity {

    private static final String TAG = "OperateInstall";
    private static final int DELAY = 2000;

    private ImageView mImageViewCarNo, mImageViewFrameNo;

    private MyRecyclerView mRecyclerView;
    private MyListView mListView;

    private OperateInstallListAdapter mOperateInstallListAdapter;

    private List<AdapterOperateInstallListData> mAdapterOperateInstallListDataList;

    //  正在操作中的item
    private int itemPosition;

    private NetworkManager mNetworkManager;
    private SharedpreferenceManager mSharedpreferenceManager;
    private MyHandler myHandler;
    private String mStringMessage = "请求数据失败，请检查网络！";
    private int eid;
    private String token;

    //  选择图片或拍照
    private static final int INTENT_CHOICE_P = 1;
    private static final int INTENT_PHOTO_P = 2;
    private static final int INTENT_CHOICE_I = 3;
    private static final int INTENT_PHOTO_I = 4;
    private static final int INTENT_CHOICE_C = 5;
    private static final int INTENT_PHOTO_C = 6;
    private static final int INTENT_CHOICE_F = 7;
    private static final int INTENT_PHOTO_F = 8;
    private int picType = 1;   //  1 = 安装位置，3 = 接线图

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

        AdapterOperateInstallListData adapterOperateInstallListData = mAdapterOperateInstallListDataList.get(itemPosition);
        switch (requestCode) {
            case INTENT_CHOICE_P: {
                Uri selectedImage = data.getData();

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(selectedImage);
                Log.i(TAG, "onActivityResult: path-->" + path);

//                mDatabaseManager.addRepairPositionPic(tNo, path);
//                String imgUrl = mDatabaseManager.getRepairPositionUrl(tNo);
//                uploadPic(Data.DATA_UPLOAD_TYPE_3, imgUrl, path);
//
//                Picasso.with(this)
//                        .load(selectedImage)
//                        .resize(Data.DATA_PIC_SIZE_WIDTH, Data.DATA_PIC_SIZE_HEIGHT)
//                        .error(R.drawable.ic_camera).into
//                        (mImageViewPositionNew);

                adapterOperateInstallListData.setPositionPic(selectedImage);
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

//                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(uri);
//                Log.i(TAG, "onActivityResult: path-->" + path);
//
//                mDatabaseManager.addRepairPositionPic(tNo, path);
//                String imgUrl = mDatabaseManager.getRepairPositionUrl(tNo);
//                uploadPic(Data.DATA_UPLOAD_TYPE_3, imgUrl, path);
//
//                Picasso.with(this).load(uri).resize(PIC_WIDTH, PIC_HEIGHT).error(R.drawable.ic_camera).into(mImageViewPositionNew);

                adapterOperateInstallListData.setPositionPic(uri);
                break;
            }
            case INTENT_CHOICE_I: {
                Uri selectedImage = data.getData();

//                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(selectedImage);
//                Log.i(TAG, "onActivityResult: path-->" + path);
//
//                mDatabaseManager.addRepairInstallPic(tNo, path);
//                String imgUrl = mDatabaseManager.getRepairInstallUrl(tNo);
//                uploadPic(Data.DATA_UPLOAD_TYPE_4, imgUrl, path);
//
//                Picasso.with(this).load(selectedImage).resize(PIC_WIDTH, PIC_HEIGHT).error(R.drawable.ic_camera).into(mImageViewInstallNew);

                adapterOperateInstallListData.setInstallPic(selectedImage);
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
//                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(uri);
//                Log.i(TAG, "onActivityResult: path-->" + path);
//                mDatabaseManager.addRepairInstallPic(tNo, path);
//                String imgUrl = mDatabaseManager.getRepairInstallUrl(tNo);
//                uploadPic(Data.DATA_UPLOAD_TYPE_4, imgUrl, path);
//
//                Picasso.with(this).load(uri).resize(PIC_WIDTH, PIC_HEIGHT).error(R.drawable.ic_camera).into(mImageViewInstallNew);

                adapterOperateInstallListData.setInstallPic(uri);
                break;
            }
            case INTENT_CHOICE_C: {
                Uri selectedImage = data.getData();

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(selectedImage);
                Log.i(TAG, "onActivityResult: path-->" + path);

                Picasso.with(this)
                        .load(selectedImage)
                        .resize(Data.DATA_PIC_SIZE_WIDTH, Data.DATA_PIC_SIZE_HEIGHT)
                        .error(R.drawable.ic_camera)
                        .into(mImageViewCarNo);
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

                Picasso.with(this)
                        .load(uri)
                        .resize(Data.DATA_PIC_SIZE_WIDTH, Data.DATA_PIC_SIZE_HEIGHT)
                        .error(R.drawable.ic_camera)
                        .into(mImageViewCarNo);
                break;
            }
            case INTENT_CHOICE_F: {
                Uri selectedImage = data.getData();

                String path = new Uri2FileU(OperateInstallActivity.this).getRealPathFromUri(selectedImage);
                Log.i(TAG, "onActivityResult: path-->" + path);

                Picasso.with(this)
                        .load(selectedImage)
                        .resize(Data.DATA_PIC_SIZE_WIDTH, Data.DATA_PIC_SIZE_HEIGHT)
                        .error(R.drawable.ic_camera)
                        .into(mImageViewFrameNo);
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

                Picasso.with(this)
                        .load(uri)
                        .resize(Data.DATA_PIC_SIZE_WIDTH, Data.DATA_PIC_SIZE_HEIGHT)
                        .error(R.drawable.ic_camera)
                        .into(mImageViewFrameNo);
                break;
            }
            default: {
                Log.i(TAG, "onActivityResult: default");
            }
        }

        mOperateInstallListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        // TODO: 2017/7/31 保存数据
        super.onStop();
    }

    private void init() {
        this.setTitleText("");

        mImageViewCarNo = findViewById(R.id.iv_layout_operate_install_car_pic);
        mImageViewFrameNo = findViewById(R.id.iv_layout_operate_install_frame_pic);

        mRecyclerView = findViewById(R.id.rv_layout_activity_operate_install);
        mListView = findViewById(R.id.lv_activity_operate_install);

        mAdapterOperateInstallListDataList = new ArrayList<>();

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("");
            mAdapterOperateInstallListDataList.add(new AdapterOperateInstallListData("1111111111", "仪表盘下", null, null));

        }
        mAdapterOperateInstallListDataList.get(0).settNoNew("352544072172191");
        mAdapterOperateInstallListDataList.get(1).settNoNew(null);
        mAdapterOperateInstallListDataList.get(2).settNoOld("123546789");
        mAdapterOperateInstallListDataList.get(2).settNoNew(null);

        mRecyclerView.setAdapter(new OperateInstallAdapter(this, list));

        mOperateInstallListAdapter = new OperateInstallListAdapter(OperateInstallActivity.this,
                mAdapterOperateInstallListDataList);
        mListView.setAdapter(mOperateInstallListAdapter);

        mNetworkManager = new NetworkManager();
        mSharedpreferenceManager = new SharedpreferenceManager(this);
        myHandler = new MyHandler();

        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
    }

    private void setEventListener() {
        mImageViewCarNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/31 选择图片
                picType = 5;
                showChoiceDialog();
            }
        });

        mImageViewFrameNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/31 选择图片
                picType = 7;
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

    //  获取完整imei
    private void getWholeImei(String imei) {
        mNetworkManager.getWholeImei(eid, token, imei);
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
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
