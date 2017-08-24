package com.tianyigps.dispatch2.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.bean.TerminalInfoBean;
import com.tianyigps.dispatch2.bean.WholeImeiBean;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnGetTerminalInfoListener;
import com.tianyigps.dispatch2.interfaces.OnGetWholeIMEIListener;
import com.tianyigps.dispatch2.manager.LocateManager;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.SnackbarU;
import com.tianyigps.dispatch2.utils.TimeFormatU;
import com.tianyigps.dispatch2.utils.TimerU;

import static com.tianyigps.dispatch2.data.Data.DATA_INTENT_SCANNER_REQUEST;
import static com.tianyigps.dispatch2.data.Data.DATA_INTENT_SCANNER_RESULT;
import static com.tianyigps.dispatch2.data.Data.DATA_SCANNER;
import static com.tianyigps.dispatch2.data.Data.MSG_2;
import static com.tianyigps.dispatch2.data.Data.MSG_ERO;

public class LocateActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LocateActivity";

    private static final int MIN_10 = 600000;

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Overlay mOverlayMarker;

    private EditText mEditTextImei;
    private ImageView mImageViewScanner, mImageViewLocate;
    private TextView mTextViewLook, mTextViewAddress, mTextViewNormal, mTextViewSatellate, mTextViewFlush;
    private LinearLayout mLinearLayout;

    private LocateManager mLocateManager;

    private TimerU mTimerU;

    private SharedpreferenceManager mSharedpreferenceManager;
    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private int eid;
    private String token;
    private String userName;
    private String mStringTitle, mStringContent;
    private double lat, lng;

    private String wholeImei;
    private String errMsg;

    //  地理编码
    private GeoCoder mGeoCoderSearch;
    private String mStringAddress;

    private Intent mIntent;
    private boolean mIsShow = false;

    //  LoadingFragment
    private LoadingDialogFragment mLoadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mLocateManager.stopLocate();
        mGeoCoderSearch.destroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DATA_INTENT_SCANNER_REQUEST && resultCode == DATA_INTENT_SCANNER_RESULT) {
            Log.i(TAG, "onActivityResult: qrcode-->" + data.getStringExtra(DATA_SCANNER));
            String imei = data.getStringExtra(DATA_SCANNER);
            mEditTextImei.setText(imei);
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
                startActivityForResult(intent, DATA_INTENT_SCANNER_REQUEST);
                break;
            }
            case R.id.iv_layout_map_control_locate: {
                // 2017/7/12 定位
                mLocateManager.startLocate();
                break;
            }
            case R.id.tv_activity_locate_look: {
                // 2017/7/12 查看设备定位
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditTextImei.getWindowToken(), 0);
                String imei = mEditTextImei.getText().toString();
                getWholeImei(imei);
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
                // 2017/7/12 刷新位置
                mTextViewFlush.setEnabled(false);
                mTimerU.start();
                if (null != wholeImei) {
                    getImeiLocation(wholeImei);
                    return;
                }
                String imei = mEditTextImei.getText().toString();
                getWholeImei(imei);
                break;
            }
            default: {
                Log.i(TAG, "onSignClick: default");
            }
        }
    }

    private void init() {
        this.setTitleText(R.string.look_location);

        mMapView = findViewById(R.id.mv_layout_map);
        mBaiduMap = mMapView.getMap();

        mLoadingDialogFragment = new LoadingDialogFragment();

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
        mLinearLayout = findViewById(R.id.ll_activity_locate);

        mLocateManager = new LocateManager(getApplicationContext());

        mSharedpreferenceManager = new SharedpreferenceManager(this);
        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();

        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();

        mGeoCoderSearch = GeoCoder.newInstance();

        mIntent = getIntent();
        mIsShow = mIntent.getBooleanExtra(Data.DATA_INTENT_LOCATE_TYPE, true);
        wholeImei = mIntent.getStringExtra(Data.DATA_INTENT_LOCATE_IMEI);
        Log.i(TAG, "init: wholeImei-->" + wholeImei);
        if (mIsShow) {
            mTextViewLook.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mTextViewLook.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.GONE);
            getImeiLocation(wholeImei);
        }
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
                if (mIsShow) {
                    moveToCenter(latLng);
                }
            }
        });

        //  由经纬度查询地址
        mGeoCoderSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (null == reverseGeoCodeResult || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //无结果
                } else {
                    //获取反向地理编码结果
                    mStringAddress = reverseGeoCodeResult.getAddress() + reverseGeoCodeResult.getSematicDescription();
                    myHandler.sendEmptyMessage(MSG_2);
                }
            }
        });

        mNetworkManager.setOnGetWholeIMEIListener(new OnGetWholeIMEIListener() {
            @Override
            public void onFailure() {
                // 2017/7/24 请求数据失败
                errMsg = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                WholeImeiBean wholeImeiBean = gson.fromJson(result, WholeImeiBean.class);
                if (!wholeImeiBean.isSuccess()) {
                    errMsg = wholeImeiBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_4);
                    return;
                }
                wholeImei = wholeImeiBean.getObj().getImei();
                myHandler.sendEmptyMessage(Data.MSG_3);
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
                String type;
                switch (redisObjBean.getLocate_type()) {
                    case 0: {
                        type = "基站定位";
                        break;
                    }
                    case 1: {
                        type = "GPS定位";
                        break;
                    }
                    case 2: {
                        type = "上线不定位";
                        break;
                    }
                    default: {
                        type = "";
                    }
                }

                mIntent.putExtra(Data.DATA_LOCATE_TYPE, (getLocateType(redisObjBean.getLocate_type()
                        , redisObjBean.getGps_time()
                        , redisObjBean.getLbs_time())));
                mIntent.putExtra(Data.DATA_LOCATE_MODEL, Integer.parseInt(objBean.getModel()));

                setResult(Data.DATA_INTENT_LOCATE_RESULT, mIntent);

                lat = redisObjBean.getLatitudeF();
                lng = redisObjBean.getLongitudeF();

                if (null == redisObjBean.getCurrent_time() || null == redisObjBean.getLocate_time()) {
                    mStringContent = null;
                    myHandler.sendEmptyMessage(Data.MSG_1);
                    return;
                }

                mStringContent = "设备编号：" + objBean.getImei()
                        + "\n状态：" + objBean.getInfoWindowStatus()
                        + "\n定位类型：" + type
                        + "\n信号时间：" + redisObjBean.getCurrent_time()
                        + "\n定位时间：" + redisObjBean.getLocate_time() + "\n\n";


                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });

        mTimerU.setOnTickListener(new TimerU.OnTickListener() {
            @Override
            public void onTick(int time) {
                mTextViewFlush.setText("刷新（" + time + "秒）");
            }

            @Override
            public void onEnd() {
                mTextViewFlush.setEnabled(true);
                mTextViewFlush.setText(R.string.flush);
            }
        });
    }

    private void markCar(LatLng latLng) {
        //定义Maker坐标点
        if (null == latLng) {
            return;
        }
        if (null != mOverlayMarker) {
            mOverlayMarker.remove();
        }
        //构建Marker图标
        View viewMarker = LayoutInflater.from(LocateActivity.this).inflate(R.layout.view_map_marker_car, null);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(viewMarker);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(latLng).icon(bitmap).anchor(0.5f, 0.5f);
        //在地图上添加Marker，并显示
        mOverlayMarker = mBaiduMap.addOverlay(option);
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

    //  获取完整imei
    private void getWholeImei(String imei) {
        showLoading();
        mNetworkManager.getWholeImei(eid, token, imei, userName);
    }

    //  获取目标位置
    private void getImeiLocation(String imei) {
        showLoading();
        mNetworkManager.getTerminalInfo(eid, token, imei, userName);
    }

    //  计算定位类型
    private int getLocateType(int locateType, String gps, String lbs) {
        TimeFormatU timeFormatU = new TimeFormatU();
        if (null == lbs) {
            lbs = "";
        }
        if (null == gps) {
            gps = "";
        }
        long now = System.currentTimeMillis();
        switch (locateType) {
            case 0: {
                long mm = timeFormatU.dateToMillis(lbs);
                if (now - mm <= MIN_10) {
                    locateType = 2;
                } else {
                    locateType = 3;
                }
                break;
            }
            case 1: {
                long mm = timeFormatU.dateToMillis(gps);
                if (now - mm <= MIN_10) {
                    locateType = 1;
                } else {
                    locateType = 3;
                }
                break;
            }
            default: {
                locateType = 3;
            }
        }
        return locateType;
    }

    private void showToast(String message) {
        LinearLayout linearLayout = findViewById(R.id.activity_locate);
        View viewToast = LayoutInflater.from(LocateActivity.this).inflate(R.layout.layout_top_toast, null);
        TextView textViewInfo = viewToast.findViewById(R.id.tv_layout_top_toast);
        textViewInfo.setText(message);
        new SnackbarU()
                .make(linearLayout, viewToast, Snackbar.LENGTH_SHORT)
                .setBackground(Color.WHITE)
                .setGravity(Gravity.TOP)
                .show();
    }

    //  显示LoadingFragment
    private void showLoading() {
        mLoadingDialogFragment.show(getFragmentManager(), "LoadingFragment");
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mLoadingDialogFragment.isAdded()) {
                mLoadingDialogFragment.dismiss();
            }

            switch (msg.what) {
                case Data.MSG_ERO: {
                    break;
                }
                case Data.MSG_1: {
                    LatLng latLng = new LatLng(lat, lng);
                    markCar(latLng);
                    moveToCenter(latLng);

                    if (null == mStringContent){
                        mTextViewAddress.setText(mStringTitle + "：未启用");
                        break;
                    }
                    //  查询地址
                    mGeoCoderSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));

                    showInfoWindow(latLng, mStringTitle, mStringContent);
                    break;
                }
                case Data.MSG_2: {
                    //  获取物理地址
                    mTextViewAddress.setText(mStringAddress);
                    break;
                }
                case Data.MSG_3: {
                    //  获取到WholeImei
                    mEditTextImei.setText(wholeImei);
                    getImeiLocation(wholeImei);
                    break;
                }
                case Data.MSG_4: {
                    //  获取WholeImei失败
                    showToast(errMsg);
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
