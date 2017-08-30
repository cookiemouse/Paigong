package com.tianyigps.dispatch2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.utils.MessageDialogU;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScannerActivity extends BaseActivity {

    private static final String TAG = "ScannerActivity";

    private ZXingView mZXingView;

    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        init();

        setEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mZXingView.startCamera();
        mZXingView.startSpot();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mZXingView.stopSpot();
        mZXingView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy();
        super.onDestroy();
    }

    private void init() {
        mIntent = getIntent();
        mZXingView = findViewById(R.id.zxv_activity_scanner);
    }

    private void setEventListener() {
        mZXingView.setDelegate(new QRCodeView.Delegate() {
            @Override
            public void onScanQRCodeSuccess(String result) {
                Log.i(TAG, "onScanQRCodeSuccess: result-->" + result);
                mIntent.putExtra(Data.DATA_SCANNER, result);
                setResult(Data.DATA_INTENT_SCANNER_RESULT, mIntent);
                finish();
            }

            @Override
            public void onScanQRCodeOpenCameraError() {
                new MessageDialogU(ScannerActivity.this).show("扫描出错");
            }
        });
    }
}
