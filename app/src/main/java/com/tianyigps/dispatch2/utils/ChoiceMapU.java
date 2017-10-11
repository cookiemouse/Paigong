package com.tianyigps.dispatch2.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/10/11.
 */

public class ChoiceMapU {

    private static final String PN_BAIDU = "com.baidu.BaiduMap";
    private static final String PN_GAODE = "com.autonavi.minimap";

    public static void toGaodeMap(Context context, String address) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        String url = "androidamap://keywordNavi?sourceApplication=amap&keyword=" + address + "&style=2";
        intent.setData(Uri.parse(url));
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }

    public static void toBaiduMap(Context context, String address) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("baidumap://map/navi?query=" + address));
        context.startActivity(intent);
    }

    //  是否已安装地图
    private static boolean isAvilibleMap(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
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

    public static boolean toMap(Context context, String address) {
        SharedpreferenceManager mSharedpreferenceManager = new SharedpreferenceManager(context);
        if (mSharedpreferenceManager.getWitchMap().equals(Data.MAP_BAIDU)){
            if (isAvilibleMap(context, PN_BAIDU)){
                toBaiduMap(context, address);
                return true;
            }
            return false;
        }
        if (mSharedpreferenceManager.getWitchMap().equals(Data.MAP_GAODE)){
            if (isAvilibleMap(context, PN_GAODE)){
                toGaodeMap(context, address);
                return true;
            }
            return false;
        }
        return false;

    }
}
