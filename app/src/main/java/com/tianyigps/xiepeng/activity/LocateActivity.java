package com.tianyigps.xiepeng.activity;

import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.base.BaseActivity;

public class LocateActivity extends BaseActivity {

    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_locate);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    private void init(){
        mMapView = findViewById(R.id.mv_layout_map);
    }

    private void markCar(){
    }

    private void markPosition(){
    }

    private void showInfoWindow(){
    }
}
