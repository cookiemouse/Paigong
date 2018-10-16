package com.tianyigps.dispatch2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.tianyigps.dispatch2.adapter.LocateWarnAdapter;
import com.tianyigps.dispatch2.base.BaseActivity;
import com.tianyigps.dispatch2.bean.LocateWarnBean;
import com.tianyigps.dispatch2.bean.TerminalInfoBean;
import com.tianyigps.dispatch2.bean.WholeImeiBean;
import com.tianyigps.dispatch2.data.AdapterLocateWarnData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.LoadingDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnGetTerminalInfoListener;
import com.tianyigps.dispatch2.interfaces.OnGetWholeIMEIListener;
import com.tianyigps.dispatch2.interfaces.OnLocateWarnListener;
import com.tianyigps.dispatch2.manager.LocateManager;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.RegularU;
import com.tianyigps.dispatch2.utils.TimeFormatU;
import com.tianyigps.dispatch2.utils.TimerU;
import com.tianyigps.dispatch2.utils.ToastU;

import java.util.ArrayList;
import java.util.List;

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
    private ImageView mImageViewScanner, mImageViewLocateIcon, mImageViewLocate;
    private TextView mTextViewLook, mTextViewAddress, mTextViewNormal, mTextViewSatellate, mTextViewFlush;
    private TextView mTextViewFlushCycle;
    private TextView mTextViewWarn;
    private ListView mListViewWarn;
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

    private String wholeImei = "";
    private String errMsg;

    private List<AdapterLocateWarnData> mAdapterLocateWarnDataList;
    private LocateWarnAdapter mLocateWarnAdapter;

    //  地理编码
    private GeoCoder mGeoCoderSearch;
    private String mStringAddress;

    private Intent mIntent;
    private boolean mIsShow = false;

    private long mNow = System.currentTimeMillis();

    private ToastU mToastU;

    //  LoadingFragment
    private LoadingDialogFragment mLoadingDialogFragment;

    //  是否为查询报警信息
    private boolean mIsWarn = false;

    //  电量
    private int mElectricity = -1;

    //  地图加载完成
    private boolean mMapEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_locate);

        init();

        setEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        Log.i(TAG, "onResume: mIsShow-->" + mIsShow);
        if (mIsShow) {
            mTextViewLook.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.VISIBLE);
            mTextViewFlush.setVisibility(View.VISIBLE);
            mTextViewWarn.setVisibility(View.GONE);
            mTextViewFlushCycle.setVisibility(View.GONE);
        } else {
            mTextViewLook.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.GONE);
            mTextViewFlush.setVisibility(View.GONE);
            mTextViewWarn.setVisibility(View.VISIBLE);
            mTextViewFlushCycle.setVisibility(View.VISIBLE);
        }
        if (mMapEnable){
//            myHandler.sendEmptyMessage(Data.MSG_5);
            getImeiLocation(wholeImei);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBaiduMap.hideInfoWindow();
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
            case R.id.iv_activity_locate_locate: {
                //  查看设备定位
                mIsWarn = false;
                mEditTextImei.clearFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                try{
                    imm.hideSoftInputFromWindow(mEditTextImei.getWindowToken(), 0);
                }catch (Exception e){
                    Log.i(TAG, "onClick: e-->" + e);
                }
                String imei = mEditTextImei.getText().toString();
                if (imei.length() > 7) {
                    mListViewWarn.setVisibility(View.GONE);
                    getWholeImei(imei);
                } else {
                    showToast("请至少输入IMEI号后8位数");
                }
                break;
            }
            case R.id.iv_layout_map_control_locate: {
                // 2017/7/12 定位
                mLocateManager.startLocate();
                break;
            }
            case R.id.tv_activity_locate_look: {
                // 2017/7/12 查看报警记录
                mIsWarn = true;
                mEditTextImei.clearFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    imm.hideSoftInputFromWindow(mEditTextImei.getWindowToken(), 0);
                } catch (NullPointerException e) {
                    Log.i(TAG, "onClick: e-->" + e);
                }
                String imei = mEditTextImei.getText().toString();
                if (imei.length() > 7) {
                    mListViewWarn.setVisibility(View.VISIBLE);
                    getWholeImei(imei);
                } else {
                    showToast("请至少输入IMEI号后8位数");
                }
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
                mIsWarn = false;
                String imei = mEditTextImei.getText().toString();
                if (imei.length() > 7) {
                    getWholeImei(imei);
                } else {
                    showToast("请至少输入IMEI号后8位数");
                }
                break;
            }
            case R.id.tv_layout_map_control_flush_cycle: {
                mTextViewFlushCycle.setEnabled(false);
                mTimerU.start();
                if (null != wholeImei) {
                    getImeiLocation(wholeImei);
                    return;
                }
                mIsWarn = false;
                String imei = mEditTextImei.getText().toString();
                if (imei.length() > 7) {
                    getWholeImei(imei);
                } else {
                    showToast("请至少输入IMEI号后8位数");
                }
                break;
            }
            default: {
                Log.i(TAG, "onSignClick: default");
            }
        }
    }

    private void init() {
        this.setTitleText("查看设备信息");

        mMapView = findViewById(R.id.mv_layout_map);
        mBaiduMap = mMapView.getMap();

        mLoadingDialogFragment = new LoadingDialogFragment();

        mTimerU = new TimerU(10);

        //  开启地位图层
        mBaiduMap.setMyLocationEnabled(true);

        mEditTextImei = findViewById(R.id.et_activity_locate_imei);
        mImageViewScanner = findViewById(R.id.iv_activity_locate_imei);
        mImageViewLocateIcon = findViewById(R.id.iv_activity_locate_locate);
        mImageViewLocate = findViewById(R.id.iv_layout_map_control_locate);
        mTextViewLook = findViewById(R.id.tv_activity_locate_look);
        mTextViewAddress = findViewById(R.id.tv_layout_map_address);
        mTextViewNormal = findViewById(R.id.tv_layout_map_control_normal);
        mTextViewSatellate = findViewById(R.id.tv_layout_map_control_satellite);
        mTextViewFlush = findViewById(R.id.tv_layout_map_control_flush);
        mTextViewFlushCycle = findViewById(R.id.tv_layout_map_control_flush_cycle);
        mTextViewWarn = findViewById(R.id.tv_layout_map_control_warn);
        mLinearLayout = findViewById(R.id.ll_activity_locate);
        mListViewWarn = findViewById(R.id.lv_activity_locate_warn);

        mLocateManager = new LocateManager(getApplicationContext());

        mAdapterLocateWarnDataList = new ArrayList<>();
        mLocateWarnAdapter = new LocateWarnAdapter(this, mAdapterLocateWarnDataList);
        mListViewWarn.setAdapter(mLocateWarnAdapter);

        mSharedpreferenceManager = new SharedpreferenceManager(this);
        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();

        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();

        mToastU = new ToastU(LocateActivity.this);

        mGeoCoderSearch = GeoCoder.newInstance();

        mIntent = getIntent();
        mIsShow = mIntent.getBooleanExtra(Data.DATA_INTENT_LOCATE_TYPE, true);
        wholeImei = mIntent.getStringExtra(Data.DATA_INTENT_LOCATE_IMEI);
        Log.i(TAG, "init: wholeImei-->" + wholeImei);

        mLocateManager.startLocate();
    }

    private void setEventListener() {
        mImageViewScanner.setOnClickListener(this);
        mImageViewLocateIcon.setOnClickListener(this);
        mImageViewLocate.setOnClickListener(this);
        mTextViewFlushCycle.setOnClickListener(this);
        mTextViewLook.setOnClickListener(this);
        mTextViewAddress.setOnClickListener(this);
        mTextViewNormal.setOnClickListener(this);
        mTextViewSatellate.setOnClickListener(this);
        mTextViewFlush.setOnClickListener(this);
        mTextViewWarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocateActivity.this, WarnInfoActivity.class);
                intent.putExtra(Data.DATA_INTENT_LOCATE_IMEI, wholeImei);
                startActivity(intent);
            }
        });

        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMapEnable = true;
//                myHandler.sendEmptyMessage(Data.MSG_5);
                getImeiLocation(wholeImei);
            }
        });

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

                Gson gson = new Gson();
                TerminalInfoBean terminalInfoBean = gson.fromJson(result, TerminalInfoBean.class);
                if (!terminalInfoBean.isSuccess()) {
                    onFailure();
                    return;
                }

                TerminalInfoBean.ObjBean objBean = terminalInfoBean.getObj();
                mStringTitle = objBean.getName();
                mNow = terminalInfoBean.getTime();

                TerminalInfoBean.ObjBean.RedisObjBean redisObjBean = objBean.getRedisObj();
                boolean isShowTip = redisObjBean.isShowTip();
                if (isShowTip) {
                    myHandler.obtainMessage(Data.MSG_7).sendToTarget();
                }
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
                        type = "-";
                        break;
                    }
                    case 3: {
                        type = "Wifi";
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

                String dianliang = redisObjBean.getDianliang();
                if (!RegularU.isEmpty(dianliang)) {
                    mElectricity = Integer.valueOf(dianliang);
                } else {
                    mElectricity = -1;
                }

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

        mNetworkManager.setOnLocateWarnListener(new OnLocateWarnListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                myHandler.sendEmptyMessage(MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                final LocateWarnBean locateWarnBean = gson.fromJson(result, LocateWarnBean.class);
                if (null == locateWarnBean) {
                    errMsg = Data.DEFAULT_MESSAGE;
                    myHandler.sendEmptyMessage(Data.MSG_4);
                    return;
                }
                if (!locateWarnBean.isSuccess()) {
                    errMsg = locateWarnBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_4);
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapterLocateWarnDataList.clear();
                        mAdapterLocateWarnDataList.add(new AdapterLocateWarnData("报警类型", "报警时间"));
                        for (LocateWarnBean.ObjBean objBean : locateWarnBean.getObj()) {
                            mAdapterLocateWarnDataList.add(new AdapterLocateWarnData(objBean.getName(), new TimeFormatU().millisToDate(objBean.getCreate_time())));
                        }
                        mLocateWarnAdapter.notifyDataSetChanged();
                    }
                });
                myHandler.sendEmptyMessage(Data.MSG_6);
            }
        });

        mTimerU.setOnTickListener(new TimerU.OnTickListener() {
            @Override
            public void onTick(int time) {
                mTextViewFlush.setText("刷新（" + time + "秒）");
                mTextViewFlushCycle.setText("" + time);
            }

            @Override
            public void onEnd() {
                mTextViewFlush.setEnabled(true);
                mTextViewFlushCycle.setEnabled(true);
                mTextViewFlush.setText(R.string.flush);
                mTextViewFlushCycle.setText(null);
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
        TextView tvElectricity = viewInfo.findViewById(R.id.tv_view_map_info_window_electricity);
        FrameLayout flEle = viewInfo.findViewById(R.id.fl_view_map_info_window_electricity);
        ProgressBar pbEle = viewInfo.findViewById(R.id.pb_view_map_info_window_electricity);

        textViewTitle.setText(title);
        textViewContent.setText(content);

        if (mElectricity > -1) {
            tvElectricity.setText(mElectricity + "%");
            flEle.setVisibility(View.VISIBLE);
            pbEle.setProgress(mElectricity);
        } else {
            tvElectricity.setVisibility(View.GONE);
            flEle.setVisibility(View.GONE);
        }

        imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.hideInfoWindow();
            }
        });

        InfoWindow mInfoWindow = new InfoWindow(viewInfo, latLng, 0);
        //显示InfoWindow
        if (mMapEnable) {
            mBaiduMap.showInfoWindow(mInfoWindow);
        }
    }

    private void moveToCenter(LatLng latLng) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng);
        MapStatus status = builder.build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        if (null != mBaiduMap) {
            mBaiduMap.animateMapStatus(update);
        }
    }

    //  获取完整imei
    private void getWholeImei(String imei) {
        if (showLoading()) {
            mNetworkManager.getWholeImei(eid, token, imei, userName);
        }
    }

    //  获取报警信息
    private void getWarnInfo(String imei) {
//        showLoading();
        mNetworkManager.getLocateWary(userName, token, imei);
    }

    //  获取目标位置
    private void getImeiLocation(String imei) {
//        showLoading();
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
        switch (locateType) {
            case 0: {
                long mm = timeFormatU.dateToMillis2(lbs);
                if (mNow - mm <= MIN_10) {
                    locateType = 2;
                } else {
                    locateType = 3;
                }
                break;
            }
            case 1: {
                long mm = timeFormatU.dateToMillis2(gps);
                if (mNow - mm <= MIN_10) {
                    locateType = 1;
                } else {
                    locateType = 3;
                }
                break;
            }
            case 3: {
                long mm = timeFormatU.dateToMillis2(lbs);
                if (mNow - mm <= MIN_10) {
                    locateType = 2;
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
//        LinearLayout linearLayout = findViewById(R.id.activity_locate);
//        View viewToast = LayoutInflater.from(LocateActivity.this).inflate(R.layout.layout_top_toast, null);
//        TextView textViewInfo = viewToast.findViewById(R.id.tv_layout_top_toast);
//        textViewInfo.setText(message);
//        new SnackbarU()
//                .make(linearLayout, viewToast, Snackbar.LENGTH_SHORT)
//                .setBackground(Color.WHITE)
//                .setGravity(Gravity.TOP)
//                .show();
        mToastU.showToast(message);
    }

    //  显示LoadingFragment
    private boolean showLoading() {
        boolean isVisible = mLoadingDialogFragment.isVisible();
        Log.i(TAG, "showLoading: isShowed-->" + isVisible);
        if (!mLoadingDialogFragment.isAdded() && !isVisible) {
            mLoadingDialogFragment.show(getFragmentManager(), "LoadingFragment");
            return true;
        }
        return false;
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case Data.MSG_ERO: {
                    if (mLoadingDialogFragment.isAdded()) {
                        mLoadingDialogFragment.dismissAllowingStateLoss();
                    }
                    break;
                }
                case Data.MSG_1: {
                    if (mLoadingDialogFragment.isAdded()) {
                        mLoadingDialogFragment.dismissAllowingStateLoss();
                    }
                    LatLng latLng = new LatLng(lat, lng);
                    markCar(latLng);
                    moveToCenter(latLng);

                    if (null == mStringContent) {
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
                    if (mIsWarn) {
                        getWarnInfo(wholeImei);
                    } else {
                        getImeiLocation(wholeImei);
                    }
                    break;
                }
                case Data.MSG_4: {
                    //  获取WholeImei失败
                    if (mLoadingDialogFragment.isAdded()) {
                        mLoadingDialogFragment.dismissAllowingStateLoss();
                    }
                    showToast(errMsg);
                    break;
                }
                case Data.MSG_5: {
                    //  init 获取设备信息，延时了200ms
                    //  利用地图loaded，做了修改
//                    getImeiLocation(wholeImei);
                    break;
                }
                case Data.MSG_6: {
                    //  获取报警信息
                    if (mLoadingDialogFragment.isAdded()) {
                        mLoadingDialogFragment.dismissAllowingStateLoss();
                    }
                    break;
                }
                case Data.MSG_7: {
                    //  显示Toast
                    if (mLoadingDialogFragment.isAdded()) {
                        mLoadingDialogFragment.dismissAllowingStateLoss();
                    }
                    mToastU.showToast("无法解析基站定位数据");
                    break;
                }
                default: {
                    if (mLoadingDialogFragment.isAdded()) {
                        mLoadingDialogFragment.dismissAllowingStateLoss();
                    }
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }
}
