package com.tianyigps.xiepeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.google.gson.Gson;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.bean.TerminalInfoBean;
import com.tianyigps.xiepeng.interfaces.OnGetTerminalInfoListener;
import com.tianyigps.xiepeng.manager.LocateManager;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;
import com.tianyigps.xiepeng.utils.TimerU;

import static com.tianyigps.xiepeng.data.Data.DATA_SCANNER;
import static com.tianyigps.xiepeng.data.Data.DATA_SCANNER_REQUEST;
import static com.tianyigps.xiepeng.data.Data.DATA_SCANNER_RESULT;
import static com.tianyigps.xiepeng.data.Data.MSG_1;
import static com.tianyigps.xiepeng.data.Data.MSG_ERO;

public class LocateActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LocateActivity";

    private MapView mMapView;
    BaiduMap mBaiduMap;

    private EditText mEditTextImei;
    private ImageView mImageViewScanner, mImageViewLocate;
    private TextView mTextViewLook, mTextViewAddress, mTextViewNormal, mTextViewSatellate, mTextViewFlush;

    private LocateManager mLocateManager;

    private TimerU mTimerU;

    private SharedpreferenceManager mSharedpreferenceManager;
    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private int eid;
    private String token;
    private String mStringTitle, mStringContent;
    private double lat, lng;

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
        mLocateManager.startLocate();
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
        if (requestCode == DATA_SCANNER_REQUEST && resultCode == DATA_SCANNER_RESULT) {
            Log.i(TAG, "onActivityResult: qrcode-->" + data.getStringExtra(DATA_SCANNER));
            String imei = data.getStringExtra(DATA_SCANNER);
            getImeiLocation(imei);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_activity_locate_imei: {
                // 2017/7/12 跳转到二维码页面
                Intent intent = new Intent(LocateActivity.this, ScannerActivity.class);
                startActivityForResult(intent, DATA_SCANNER_REQUEST);
                break;
            }
            case R.id.iv_layout_map_control_locate: {
                // 2017/7/12 定位
                mLocateManager.startLocate();
                break;
            }
            case R.id.tv_activity_locate_look: {
                // TODO: 2017/7/12 查看设备定位
                String imei = mEditTextImei.getText().toString();
                getImeiLocation(imei);
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

        mSharedpreferenceManager = new SharedpreferenceManager(this);
        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();

        mNetworkManager = NetworkManager.getInstance();
        myHandler = new MyHandler();
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
            }
        });

        mNetworkManager.setGetTerminalInfoListener(new OnGetTerminalInfoListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                TerminalInfoBean terminalInfoBean = gson.fromJson(result, TerminalInfoBean.class);
                if (!terminalInfoBean.isSuccess()) {
                    onFailure();
                    return;
                }

                TerminalInfoBean.ObjBean objBean = terminalInfoBean.getObj();
                mStringTitle = objBean.getName();

                TerminalInfoBean.ObjBean.RedisObjBean redisObjBean = objBean.getRedisObj();
                mStringContent = "设备编号：" + objBean.getImei()
                        + "\n状态："
                        + "\n定位类型："
                        + "\n信号时间：" + redisObjBean.getCurrent_time()
                        + "\n定位时间：" + redisObjBean.getLocate_time() + "\n\n";
                redisObjBean.getAcc_status();   //00-未知，01-熄火，02-点火
                redisObjBean.getLocate_type();  //0-基站定位，1-GPS定位，2-上线不定位

                lat = redisObjBean.getLatitude();
                lng = redisObjBean.getLongitude();

                myHandler.sendEmptyMessage(MSG_1);
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

    private void showInfoWindow(LatLng latLng, String title, String content) {
        View viewInfo = LayoutInflater.from(LocateActivity.this).inflate(R.layout.view_map_info_window, null);

        ImageView imageViewCancel = viewInfo.findViewById(R.id.iv_view_map_info_window_cancel);
        TextView textViewTitle = viewInfo.findViewById(R.id.tv_view_map_info_window_title);
        TextView textViewContent = viewInfo.findViewById(R.id.tv_view_map_info_window_content);

        textViewTitle.setText(title);
        textViewContent.setText(content);

        imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.hideInfoWindow();
            }
        });

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

    //  获取目标位置
    private void getImeiLocation(String imei) {
        mNetworkManager.getTerminalInfo(eid, token, imei);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ERO: {
                    break;
                }
                case MSG_1: {
                    LatLng latLng = new LatLng(lat, lng);
                    markCar(latLng);
                    showInfoWindow(latLng, mStringTitle, mStringContent);
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
