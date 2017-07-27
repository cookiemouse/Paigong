package com.tianyigps.xiepeng.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.manager.DatabaseManager;
import com.tianyigps.xiepeng.manager.FileManager;
import com.yundian.bottomdialog.BottomDialog;

import java.io.File;

public class OperateRepairActivity extends BaseActivity {

    private static final String TAG = "OperateRepairActivity";

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

    private DatabaseManager mDatabaseManager;
    private String mStringPosition, mStringPositionPath, mStringInstallPath, mStringExplain;

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

        Log.i(TAG, "onActivityResult: requestCode-->" + requestCode);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case INTENT_CHOICE_P: {
                    Uri selectedImage = data.getData();
                    mImageViewPositionNew.setImageURI(selectedImage);

                    final String scheme = selectedImage.getScheme();
                    String str = null;
                    if (scheme == null)
                        str = selectedImage.getPath();
                    else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
                        str = selectedImage.getPath();
                    } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                        Cursor cursor = this.getContentResolver().query(selectedImage, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null,
                                null);
                        if (null != cursor) {
                            if (cursor.moveToFirst()) {
                                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                                if (index > -1) {
                                    str = cursor.getString(index);
                                }
                            }
                            cursor.close();
                        }
                    }
                    Log.i(TAG, "onActivityResult: str-->" + str);
                    break;
                }
                case INTENT_PHOTO_P: {
                    FileManager fileManager = new FileManager(FileManager.TYPE_PNG);
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    mImageViewPositionNew.setImageBitmap(bitmap);
                    fileManager.saveBitmapJpg(bitmap);
                    mStringPositionPath = fileManager.getPath();
                    break;
                }
                case INTENT_CHOICE_I: {
                    Uri selectedImage = data.getData();
                    mImageViewInstallNew.setImageURI(selectedImage);

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    if (null != cursor && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        mStringInstallPath = cursor.getString(columnIndex);
                        cursor.close();
                    }
                    break;
                }
                case INTENT_PHOTO_I: {
                    FileManager fileManager = new FileManager(FileManager.TYPE_PNG);
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    mImageViewInstallNew.setImageBitmap(bitmap);
                    fileManager.saveBitmapJpg(bitmap);
                    mStringInstallPath = fileManager.getPath();
                    break;
                }
                default: {
                    Log.i(TAG, "onActivityResult: default");
                }
            }
            Log.i(TAG, "onActivityResult: path-1->" + mStringPositionPath);
            Log.i(TAG, "onActivityResult: path-2->" + mStringInstallPath);
//            if (null != mStringPositionPath) {
//                Log.i(TAG, "onActivityResult: path-3->保存");
//                mDatabaseManager.addRepairPositionPic("测试tNo", mStringPositionPath);
//            }
//            if (null != mStringInstallPath) {
//                Log.i(TAG, "onActivityResult: path-4->保存");
//                mDatabaseManager.addRepairInstallPic("测试tNo", mStringInstallPath);
//            }
        }
        /*
        Bundle bundle = data.getExtras();
        if (null == bundle) {
            return;
        }
        Bitmap bitmap = (Bitmap) bundle.get("data");
        mImageViewPositionNew.setImageBitmap(bitmap);
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/paigong/test.jpg");
        if (file.exists()) {
            file.delete();
        } else {
            file.mkdirs();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        */


        /*
        Uri selectedImage = data.getData();
        mImageViewPositionNew.setImageURI(selectedImage);

        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            // TODO: 2017/7/26 保存该路径
            cursor.close();
        }
        */

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseManager.close();
    }

    private void init() {
        this.setTitleText("维修");

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

        mDatabaseManager = new DatabaseManager(OperateRepairActivity.this);



        Cursor cursor = mDatabaseManager.getRepairs();
        Log.i(TAG, "onResume:");
        if (null != cursor && cursor.moveToFirst()) {
            do {
                Log.i(TAG, "onResume:-->" + cursor.getString(0));
                if ("测试tNo".equals(cursor.getString(0))) {
                    mStringPosition = cursor.getString(1);
                    mStringPositionPath = cursor.getString(2);
                    mStringInstallPath = cursor.getString(3);
                    mStringExplain = cursor.getString(4);

                    mEditTextPosition.setText(mStringPosition);
                    mEditTextExplain.setText(mStringExplain);
                    Log.i(TAG, "onResume: position-->" + mStringPosition);
                    Log.i(TAG, "onResume: positionPath-->" + mStringPositionPath);
                    Log.i(TAG, "onResume: installPath-->" + mStringInstallPath);
                    Log.i(TAG, "onResume: explain-->" + mStringExplain);
                    if (null != mStringPositionPath) {
                        File file = new File(mStringPositionPath);
                        if (file.exists()) {
                            Picasso.with(this).load(file).into(mImageViewPositionNew);
                        }
                    }
                    if (null != mStringInstallPath) {
                        File file = new File(mStringInstallPath);
                        if (file.exists()) {
                            Picasso.with(this).load(file).into(mImageViewInstallNew);
                        }
                    }
                }
            } while (cursor.moveToNext());
        }
    }

    private void setEventListener() {

        mImageViewLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/26 定位
                Intent intent = new Intent(OperateRepairActivity.this, LocateActivity.class);
                intent.putExtra(Data.DATA_INTENT_LOCATE_TYPE, false);
                intent.putExtra(Data.DATA_INTENT_LOCATE_IMEI, "123456789012340");
                startActivity(intent);
            }
        });

        mTextViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/26 保存

                mStringPosition = mEditTextPosition.getText().toString();
                mStringExplain = mEditTextExplain.getText().toString();
                Log.i(TAG, "onClick: save.positionPath-->" + mStringPositionPath);
                Log.i(TAG, "onClick: save.installPath-->" + mStringInstallPath);

                mDatabaseManager.addRepair("测试tNo", mStringPosition, mStringPositionPath, mStringInstallPath, mStringExplain);
            }
        });

        mImageViewPositionNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/26 拍照
                picType = 1;
                showChoiceDialog();
            }
        });

        mImageViewInstallNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picType = 3;
                showChoiceDialog();
                // 2017/7/26 拍照
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
}
