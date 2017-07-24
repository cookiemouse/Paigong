package com.tianyigps.xiepeng.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.utils.GeoCoderU;

import java.util.ArrayList;
import java.util.List;

import static com.tianyigps.xiepeng.data.Data.DATA_INTENT_ADDRESS;

/**
 * Created by djc on 2017/7/19.
 */

public class ChoiceMapDialogFragment extends DialogFragment {

    private static final String TAG = "ChoiceMapDialog";

    private static final String PN_BAIDU = "com.baidu.BaiduMap";
    private static final String PN_GAODE = "com.autonavi.minimap";

    private String address;
    private double mLat, mLng;
    private GeoCoderU mGeoCoderU;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        address = bundle.getString(DATA_INTENT_ADDRESS);
        Log.i(TAG, "onCreate: address-->" + address);

        mGeoCoderU = new GeoCoderU();
        mGeoCoderU.setOnGetGeoGodeListener(new GeoCoderU.OnGetGeoCodeListener() {
            @Override
            public void onGetLatlng(double lat, double lng) {
                mLat = lat;
                mLng = lng;
            }

            @Override
            public void onGetAddress(String address) {
            }
        });

        mGeoCoderU.searchLatlng(address);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewDialog = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_choice_map, null);

        TableRow tableRowBaidu = viewDialog.findViewById(R.id.tr_dialog_choice_map_baidu);
        TableRow tableRowGaode = viewDialog.findViewById(R.id.tr_dialog_choice_map_gaode);

        if (isAvilibleMap(PN_BAIDU)) {
            tableRowBaidu.setVisibility(View.VISIBLE);
        } else {
            tableRowBaidu.setVisibility(View.GONE);
        }

        if (isAvilibleMap(PN_GAODE)) {
            tableRowGaode.setVisibility(View.VISIBLE);
        } else {
            tableRowGaode.setVisibility(View.GONE);
        }

        if (tableRowBaidu.getVisibility() == View.GONE && tableRowGaode.getVisibility() == View.GONE) {
            Log.i(TAG, "onCreateDialog: nomap");
            View viewDialogNoMap = LayoutInflater.from(getActivity())
                    .inflate(R.layout.dialog_message_no_map, null);

            TextView textViewKnown = viewDialogNoMap.findViewById(R.id.tv_dialog_message_no_map_cancel);

            textViewKnown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            builder.setView(viewDialogNoMap);
        } else {
            Log.i(TAG, "onCreateDialog: have map");
            builder.setView(viewDialog);
        }

        tableRowBaidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/19 百度地图
                toBaiduMap(address);
                dismiss();
            }
        });

        tableRowGaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/19 高德地图
//                toGaodeMap("36.547901", "104.258354", address);
                toGaodeMap(mLat, mLng, address);
                dismiss();
            }
        });

        return builder.create();
    }

    private void toGaodeMap(double lat, double lng, String address) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        String uri = "androidamap://navi?sourceApplication=amap" +
                "&poiname=" + address +
                "&lat=" + lat +
                "&lon=" + lng +
                "&dev=1&style=0";
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    private void toBaiduMap(String address) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("baidumap://map/navi?query=" + address));
        startActivity(intent);
    }

    //  是否已安装地图
    private boolean isAvilibleMap(String packageName) {
        //获取packagemanager
        final PackageManager packageManager = getActivity().getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}