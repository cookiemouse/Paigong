package com.tianyigps.xiepeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.manager.LocateManager;
import com.tianyigps.xiepeng.utils.TimerU;

import static com.tianyigps.xiepeng.data.Data.DATA_SCANNER;
import static com.tianyigps.xiepeng.data.Data.DATA_SCANNER_REQUEST;
import static com.tianyigps.xiepeng.data.Data.DATA_SCANNER_RESULT;

public class LocateActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LocateActivity";

    private MapView mMapView;
    BaiduMap mBaiduMap;

    private EditText mEditTextImei;
    private ImageView mImageViewScanner, mImageViewLocate;
    private TextView mTextViewLook, mTextViewAddress, mTextViewNormal, mTextViewSatellate, mTextViewFlush;

    private LocateManager mLocateManager;

    private TimerU mTimerU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_locate);

        init();

        setEventListener();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DATA_SCANNER_REQUEST && resultCode == DATA_SCANNER_RESULT){
            Log.i(TAG, "onActivityResult: qrcode-->" + data.getStringExtra(DATA_SCANNER));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_activity_locate_imei: {
                // TODO: 2017/7/12 跳转到二维码页面
                Intent intent = new Intent(LocateActivity.this, ScannerActivity.class);
                startActivityForResult(intent, DATA_SCANNER_REQUEST);
                break;
            }
            case R.id.iv_layout_map_control_locate: {
                // TODO: 2017/7/12 定位
                mLocateManager.startLocate();
                break;
            }
            case R.id.tv_activity_locate_look: {
                // TODO: 2017/7/12 查看设备定位
                break;
            }
            case R.id.tv_layout_map_control_normal: {
                //  2017/7/12 普通地图
                mTextViewNormal.setBackgroundResource(R.drawable.bg_map_control_select);
                mTextViewSatellate.setBackgroundResource(R.drawable.bg_map_control);

                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            }
            case R.id.tv_layout_map_control_satellite: {
                //  2017/7/12 卫星地图
                mTextViewNormal.setBackgroundResource(R.drawable.bg_map_control);
                mTextViewSatellate.setBackgroundResource(R.drawable.bg_map_control_select);

                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            }
            case R.id.tv_layout_map_control_flush: {
                // TODO: 2017/7/12 刷新位置
                mTextViewFlush.setEnabled(true);
                mTimerU.start();
                break;
            }
            default: {
                Log.i(TAG, "onClick: default");
            }
        }
    }

    private void init() {
        mMapView = findViewById(R.id.mv_layout_map);
        mBaiduMap = mMapView.getMap();

        mTimerU = new TimerU(10);

        //  开启地位图层
        mBaiduMap.setMyLocationEnabled(true);

        mEditTextImei = findViewById(R.id.et_activity_locate_imei);
        mImageViewScanner = findViewById(R.id.iv_activity_locate_imei);
        mImageViewLocate = findViewById(R.id.iv_layout_map_control_locate);
        mTextViewLook = findViewById(R.id.tv_activity_locate_look);
        mTextViewAddress = findViewById(R.id.tv_layout_map_address);
        mTextViewNormal = findViewById(R.id.tv_layout_map_control_normal);
        mTextViewSatellate = findViewById(R.id.tv_layout_map_control_satellite);
        mTextViewFlush = findViewById(R.id.tv_layout_map_control_flush);

        mLocateManager = new LocateManager(getApplicationContext());
    }

    private void setEventListener() {
        mImageViewScanner.setOnClickListener(this);
        mImageViewLocate.setOnClickListener(this);
        mTextViewLook.setOnClickListener(this);
        mTextViewAddress.setOnClickListener(this);
        mTextViewNormal.setOnClickListener(this);
        mTextViewSatellate.setOnClickListener(this);
        mTextViewFlush.setOnClickListener(this);

        mLocateManager.setOnReceiveLocationListener(new LocateManager.OnReceiveLocationListener() {
            @Override
            public void onReceive(LatLng latLng) {
                // 构造定位数据
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(0)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(latLng.latitude)
                        .longitude(latLng.longitude).build();
                // 设置定位数据
                mBaiduMap.setMyLocationData(locData);
                // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
                /*
                mCurrentMarker = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_geo);
                MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
                mBaiduMap.setMyLocationConfiguration(config);
                */
                moveToCenter(latLng);

//                markCar(latLng);

                showInfoWindow(latLng);
            }
        });

        mTimerU.setOnTickListener(new TimerU.OnTickListener() {
            @Override
            public void onTick(int time) {
                mTextViewFlush.setText("" + time);
            }

            @Override
            public void onEnd() {
                mTextViewFlush.setEnabled(true);
            }
        });
    }

    private void markCar(LatLng latLng) {
        //定义Maker坐标点
        if (null == latLng) {
            return;
        }
        //构建Marker图标
        View viewMarker = LayoutInflater.from(LocateActivity.this).inflate(R.layout.view_map_marker_car, null);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(viewMarker);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(latLng).icon(bitmap).anchor(0.5f, 0.5f);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    private void showInfoWindow(LatLng latLng) {
        View viewInfo = LayoutInflater.from(LocateActivity.this).inflate(R.layout.view_map_info_window, null);

        InfoWindow mInfoWindow = new InfoWindow(viewInfo, latLng, 0);
        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    private void moveToCenter(LatLng latLng) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng);
        MapStatus status = builder.build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        mBaiduMap.animateMapStatus(update);
    }
}
