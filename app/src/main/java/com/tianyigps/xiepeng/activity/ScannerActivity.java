package com.tianyigps.xiepeng.activity;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.base.BaseActivity;

import static com.tianyigps.xiepeng.data.Data.DATA_SCANNER;
import static com.tianyigps.xiepeng.data.Data.DATA_SCANNER_RESULT;

public class ScannerActivity extends BaseActivity {

    private static final String TAG = "ScannerActivity";

    private QRCodeReaderView mQrCodeReaderView;

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
        mQrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mQrCodeReaderView.stopCamera();
    }

    private void init() {
        mIntent = getIntent();
        mQrCodeReaderView = findViewById(R.id.qrv_activity);
    }

    private void setEventListener() {
        mQrCodeReaderView.setOnQRCodeReadListener(new QRCodeReaderView.OnQRCodeReadListener() {
            @Override
            public void onQRCodeRead(String text, PointF[] points) {
                Log.i(TAG, "onQRCodeRead: -->" + text);
                mIntent.putExtra(DATA_SCANNER, text);
                setResult(DATA_SCANNER_RESULT, mIntent);
                finish();
            }
        });
    }
}
