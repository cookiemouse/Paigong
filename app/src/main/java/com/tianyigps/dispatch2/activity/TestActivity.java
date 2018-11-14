package com.tianyigps.dispatch2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;

import java.util.Set;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TestActivity";

    private Button mButtonAdd, mButtonDelete, mButtonGet;

    private SharedpreferenceManager mSharedpreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        init();

        setEventListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_test_add: {
                mSharedpreferenceManager.savePushOrderId("1234");
                break;
            }
            case R.id.btn_activity_test_delete: {
                mSharedpreferenceManager.deletePushOrderId("123");
                break;
            }
            case R.id.btn_activity_test_get: {
                Set<String> set = mSharedpreferenceManager.getPushOrderIds();
                Log.i(TAG, "onClick: -----------------");
                for (String str : set) {
                    Log.i(TAG, "onClick: str-->" + str);
                }
                Log.i(TAG, "onClick: =================");
                break;
            }
        }
    }

    private void init() {
        mSharedpreferenceManager = new SharedpreferenceManager(this);

        mButtonAdd = findViewById(R.id.btn_activity_test_add);
        mButtonDelete = findViewById(R.id.btn_activity_test_delete);
        mButtonGet = findViewById(R.id.btn_activity_test_get);
    }

    private void setEventListener() {
        mButtonAdd.setOnClickListener(this);
        mButtonDelete.setOnClickListener(this);
        mButtonGet.setOnClickListener(this);
    }
}
