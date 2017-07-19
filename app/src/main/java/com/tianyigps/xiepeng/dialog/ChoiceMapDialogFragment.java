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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.tianyigps.xiepeng.R;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        address = bundle.getString(DATA_INTENT_ADDRESS);
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
                toGaodeMap();
                dismiss();
            }
        });

        builder.setView(viewDialog);

        return builder.create();
    }

    private void toGaodeMap() {
        Intent intent = new Intent("android.intent.action.VIEW",
                android.net.Uri.parse("androidamap://showTraffic?sourceApplication=softname&poiid=BGVIS1&lat=36.2&lon=116.1&level=10&dev=0"));
        intent.setPackage("com.autonavi.minimap");
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
