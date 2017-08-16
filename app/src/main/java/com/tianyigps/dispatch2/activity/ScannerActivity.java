package com.tianyigps.dispatch2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.data.Data;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScannerActivity extends BaseActivity {

    private static final String TAG = "ScannerActivity";

    private ZBarScannerView mZBarScannerView;

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
        mZBarScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mZBarScannerView.stopCamera();
    }

    private void init() {
        mIntent = getIntent();
        mZBarScannerView = findViewById(R.id.zbsv_activity_scanner);
    }

    private void setEventListener() {
        mZBarScannerView.setResultHandler(new ZBarScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                Log.i(TAG, "handleResult: result-->" + result.getContents());
                String code = result.getContents();
                mIntent.putExtra(Data.DATA_SCANNER, code);
                setResult(Data.DATA_INTENT_SCANNER_RESULT, mIntent);
                finish();
            }
        });
    }
}
