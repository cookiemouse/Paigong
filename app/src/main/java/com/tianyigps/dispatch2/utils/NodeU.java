package com.tianyigps.dispatch2.utils;

import android.util.Log;

import com.tianyigps.dispatch2.data.Data;

/**
 * Created by cookiemouse on 2017/8/15.
 */

public class NodeU {

    private static final String TAG = "NodeU";

    public static String getNode(int node) {
        Log.i(TAG, "getNode: node-->" + node);
        String strNode = "";
        switch (node) {
            case Data.NODE_1: {
                strNode = "收到订单";
                break;
            }
            case Data.NODE_2: {
                strNode = "申请改约";
                break;
            }
            case Data.NODE_3: {
                strNode = "审核改约";
                break;
            }
            case Data.NODE_4: {
                strNode = "派单";
                break;
            }
            case Data.NODE_5: {
                strNode = "取消订单";
                break;
            }
            case Data.NODE_6: {
                strNode = "修改订单";
                break;
            }
            case Data.NODE_7: {
                strNode = "联系现场";
                break;
            }
            case Data.NODE_8: {
                strNode = "工程师已到达";
                break;
            }
            case Data.NODE_9: {
                strNode = "开始安装";
                break;
            }
            case Data.NODE_10: {
                strNode = "继续安装";
                break;
            }
            case Data.NODE_11: {
                strNode = "安装退回";
                break;
            }
            case Data.NODE_12: {
                strNode = "部分完成";
                break;
            }
            case Data.NODE_13: {
                strNode = "退回客户";
                break;
            }
            case Data.NODE_14: {
                strNode = "完成订单";
                break;
            }
            default: {
                Log.i(TAG, "getNode: -->" + node);
            }
        }
        return strNode;
    }
}
