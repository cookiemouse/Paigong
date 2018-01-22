package com.tianyigps.dispatch2.data;

/**
 * Created by djc on 2017/7/10.
 */

public final class Data {
    public final static String DATA_SHAREDPREFERENCES = "paigong_sharedpreferences";

    //  账号类型，worker或manager
    public final static String DATA_LAUNCH_ACCOUNT = "paigong_sharedpreferences_launch_account";
    public final static String DATA_LAUNCH_PASSWORD = "paigong_sharedpreferences_launch_password";
    public final static String DATA_LAUNCH_IS_REMBNBER = "paigong_sharedpreferences_launch_isremenber";
    public final static String DATA_LAUNCH_MODE = "paigong_sharedpreferences_launch_mode";
    public final static int DATA_LAUNCH_MODE_WORKER = 1;
    public final static int DATA_LAUNCH_MODE_MANAGER = 2;

    public final static String DATA_UI_MODE = "paigong_sharedpreferences_ui_mode";

    //  二维码扫描，Activity跳转
    public final static String DATA_SCANNER = "paigong_scanner";
    public final static int DATA_INTENT_SCANNER_REQUEST = 0x001;
    public final static int DATA_INTENT_SCANNER_RESULT = 0x002;


    public final static int DATA_INTENT_ORDER_DETAILS_REQUEST = 0x003;
    public final static int DATA_INTENT_ORDER_DETAILS_RESULT = 0x004;

    public final static int DATA_INTENT_CHOICE_WORKER_REQUEST = 0x005;
    public final static int DATA_INTENT_CHOICE_WORKER_RESULT = 0x006;

    public final static int DATA_INTENT_REASON_REQUEST = 0x009;
    public final static int DATA_INTENT_REASON_RESULT = 0x010;

    public final static String DATA_LOCATE_TYPE = "paigong_locate_type";
    public final static String DATA_LOCATE_MODEL = "paigong_locate_model";
    public final static int DATA_INTENT_LOCATE_REQUEST = 0x007;
    public final static int DATA_INTENT_LOCATE_RESULT = 0x008;

    //  intent传值
    public final static String DATA_INTENT_EID = "paigong_intent_eid";
    public final static String DATA_INTENT_TOKEN = "paigong_intent_token";
    public final static String DATA_INTENT_ORDER_NO = "paigong_intent_order_no";
    public final static String DATA_INTENT_ORDER_ID = "paigong_intent_order_id";
    public final static String DATA_INTENT_ORDER_STATUS = "paigong_intent_order_status";
    public final static String DATA_INTENT_T_ID = "paigong_intent_t_id";
    public final static String DATA_INTENT_T_NO = "paigong_intent_t_no";
    public final static String DATA_INTENT_FRAME_NO = "paigong_intent_frame_no";
    public final static String DATA_INTENT_ADDRESS = "paigong_intent_address";
    public final static String DATA_INTENT_INSTALL_TYPE = "paigong_intent_install_type";
    public final static String DATA_INTENT_INSTALL_WIRED_NUM = "paigong_intent_install_wired_num";
    public final static String DATA_INTENT_INSTALL_WIRELESS_NUM = "paigong_intent_install_wireless_num";
    public final static String DATA_INTENT_INSTALL_CAR_ID = "paigong_intent_install_car_id";
    public final static String DATA_INTENT_CAR_ID = "paigong_intent_car_id";

    public final static String DATA_INTENT_ORDER_DETAILS_RESULT_SIGNED = "paigong_intent_order_details_is_signed";

    public final static String DATA_INTENT_LOCATE_TYPE = "paigong_intent_locate_type";
    public final static String DATA_INTENT_LOCATE_IMEI = "paigong_intent_locate_imei";

    public final static String DATA_INTENT_PEND_RESULT = "paigong_intent_pend_result";

    public final static String DATA_INTENT_REASON = "paigong_intent_reason";

    //  WorkerFragmentContentActivity显示Fragment
    public final static String DATA_INTENT_WORKER_FRAGMENT = "paigong_intent_worker_fragment";
    public final static int DATA_INTENT_WORKER_FRAGMENT_HANDING = 1;
    public final static int DATA_INTENT_WORKER_FRAGMENT_HANDED = 2;

    //  数据请求所需数据
    private final static String DATA_JSON = "paigong_json_";
    public final static String DATA_JSON_PHONE_NO = DATA_JSON + "phone_number";
    public final static String DATA_JSON_AREA = DATA_JSON + "area";
    public final static String DATA_JSON_TOKEN = DATA_JSON + "token";
    public final static String DATA_JSON_NAME = DATA_JSON + "name";
    public final static String DATA_JSON_DUTIES = DATA_JSON + "duties";
    public final static String DATA_JSON_JOB_NO = DATA_JSON + "job_number";
    public final static String DATA_JSON_EID = DATA_JSON + "eid";
    public final static String DATA_JSON_HEAD_PHONE = DATA_JSON + "head_phone";
    public final static String DATA_JSON_IMG_BASE_URL = DATA_JSON + "img_base_url";

    public final static String DATA_WITCH_MAP = "witch_map";

    public final static String SH_HEAD_PHONE_LIST = "head_phone_list";

    public final static String MAP_BAIDU = "map_baidu";
    public final static String MAP_GAODE = "map_gaode";

    //  default
    public final static String DEFAULT_TOKEN = "";
    public final static String DEFAULT_MESSAGE = "请求数据失败，请检网络！";

    //  Handler分类
    public final static int MSG_ERO = 0x8888;
    public final static int MSG_1 = 0x01;
    public final static int MSG_2 = 0x02;
    public final static int MSG_3 = 0x03;
    public final static int MSG_4 = 0x04;
    public final static int MSG_5 = 0x05;
    public final static int MSG_6 = 0x06;
    public final static int MSG_7 = 0x07;
    public final static int MSG_8 = 0x08;
    public final static int MSG_9 = 0x09;
    public final static int MSG_10 = 0x10;
    public final static int MSG_11 = 0x11;
    public final static int MSG_12 = 0x12;

    //  数据库
    public final static String DATA_TAB_ORDER = "paigong_database_tab_order";
    public final static String DATA_TAB_REPAIR = "paigong_database_tab_repair";
    public final static String DATA_TAB_REMOVE = "paigong_database_tab_remove";
    public final static String DATA_TAB_INSTALL_CAR = "paigong_database_tab_install_car";
    public final static String DATA_TAB_INSTALL_TERMINAL = "paigong_database_tab_install_terminal";
    public final static String DATA_DB_NAME = "paigong_database_db.db";

    //  上传图片type
    public final static int DATA_UPLOAD_TYPE_1 = 1;     //车牌号照片
    public final static int DATA_UPLOAD_TYPE_2 = 2;     //安装位置照片
    public final static int DATA_UPLOAD_TYPE_3 = 3;     //安装位置照片
    public final static int DATA_UPLOAD_TYPE_4 = 4;     //接线图照片
    public final static int DATA_UPLOAD_TYPE_5 = 5;     //附加照片

    //  上传图片model
    public final static int DATA_UPLOAD_MODEL_1 = 1;    //有线
    public final static int DATA_UPLOAD_MODEL_2 = 2;    //无线

    //  签字图片
    public final static String DATA_PIC_SIGN = "sign.png";
    public final static String DATA_PIC_SIGN_HEAD = "data:image/png;base64,";

    //  定位类型
    public static final String LOCATE_TYPE_BAIDU = "bd";
    public static final String LOCATE_TYPE_GPS = "gps";

    //  定单状态
    public static final int STATUS_1 = 1;   //  待派单
    public static final int STATUS_2 = 2;   //  空单
    public static final int STATUS_3 = 3;   //  已派单
    public static final int STATUS_4 = 4;   //  退回客户
    public static final int STATUS_5 = 5;   //  已取消
    public static final int STATUS_6 = 6;   //  安装退回
    public static final int STATUS_7 = 7;   //  已完成
    public static final int STATUS_98 = 98; //  改约不通过
    public static final int STATUS_99 = 99; //  待审核

    //  节点状态
    public static final int NODE_1 = 1;   //  收到订单
    public static final int NODE_2 = 2;   //  申请改约
    public static final int NODE_3 = 3;   //  审核改约
    public static final int NODE_4 = 4;   //  派单
    public static final int NODE_5 = 5;   //  取消订单
    public static final int NODE_6 = 6;   //  修改订单
    public static final int NODE_7 = 7;   //  联系现场
    public static final int NODE_8 = 8;   //  工程师已到达
    public static final int NODE_9 = 9;   //  开始安装
    public static final int NODE_10 = 10; //  继续安装
    public static final int NODE_11 = 11; //  安装退回
    public static final int NODE_12 = 12; //  部分完成
    public static final int NODE_13 = 13; //  退回客户
    public static final int NODE_14 = 14; //  完成订单

    //  广播接收
    //  极光推送
    public static final String BROAD_FILTER = "cn.jpush.android.intent.NOTIFICATION_RECEIVED";
    public static final String BROAD_CATEGORY = "com.tianyigps.dispatch2";

    //  服务ID
    public static final int SERVICE_ID = 1001;
    public static final int NOTIFICATION_ID = 1002;
}
