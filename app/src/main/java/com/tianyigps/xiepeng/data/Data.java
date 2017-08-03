package com.tianyigps.xiepeng.data;

/**
 * Created by djc on 2017/7/10.
 */

public final class Data {
    public final static String DATA_SHAREDPREFERENCES = "paigong_sharedpreferences";

    //  账号类型，worker或manager
    public final static String DATA_LAUNCH_ACCOUNT = "paigong_sharedpreferences_launch_account";
    public final static String DATA_LAUNCH_MODE = "paigong_sharedpreferences_launch_mode";
    public final static int DATA_LAUNCH_MODE_WORKER = 1;
    public final static int DATA_LAUNCH_MODE_MANAGER = 2;

    //  二维码扫描，Activity跳转
    public final static String DATA_SCANNER = "paigong_scanner";
    public final static int DATA_INTENT_SCANNER_REQUEST = 0x001;
    public final static int DATA_INTENT_SCANNER_RESULT = 0x002;

    public final static int DATA_INTENT_ORDER_DETAILS_REQUEST = 0x003;
    public final static int DATA_INTENT_ORDER_DETAILS_RESULT = 0x004;

    //  intent传值
    public final static String DATA_INTENT_EID = "paigong_intent_eid";
    public final static String DATA_INTENT_TOKEN = "paigong_intent_token";
    public final static String DATA_INTENT_ORDER_NO = "paigong_intent_order_no";
    public final static String DATA_INTENT_T_NO = "paigong_intent_t_no";
    public final static String DATA_INTENT_FRAME_NO = "paigong_intent_frame_no";
    public final static String DATA_INTENT_ADDRESS = "paigong_intent_address";
    public final static String DATA_INTENT_INSTALL_TYPE = "paigong_intent_install_type";
    public final static String DATA_INTENT_INSTALL_WIRED_NUM = "paigong_intent_install_wired_num";
    public final static String DATA_INTENT_INSTALL_WIRELESS_NUM = "paigong_intent_install_wireless_num";
    public final static String DATA_INTENT_INSTALL_CAR_ID = "paigong_intent_install_car_id";

    public final static String DATA_INTENT_ORDER_DETAILS_RESULT_SIGNED = "paigong_intent_order_details_is_signed";
    public final static String DATA_INTENT_ORDER_DETAILS_IS_CHECKED = "paigong_intent_order_details_is_checked";

    public final static String DATA_INTENT_LOCATE_TYPE = "paigong_intent_locate_type";
    public final static String DATA_INTENT_LOCATE_IMEI = "paigong_intent_locate_imei";

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

    //  default
    public final static String DEFAULT_TOKEN = "";

    //  Handler分类
    public final static int MSG_ERO = 0x8888;
    public final static int MSG_1 = 0x01;
    public final static int MSG_2 = 0x02;
    public final static int MSG_3 = 0x03;
    public final static int MSG_4 = 0x04;
    public final static int MSG_5 = 0x05;
    public final static int MSG_6 = 0x06;
    public final static int MSG_7 = 0x07;

    //  数据库
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

    //  图片大小
    public final static int DATA_PIC_SIZE_WIDTH = 300;  //宽度
    public final static int DATA_PIC_SIZE_HEIGHT = 150; //高度

    //  签字图片
    public final static String DATA_PIC_SIGN = "sign.png";
    public final static String DATA_PIC_SIGN_HEAD = "data:image/png;base64,";

    //  测试
    public final static int EID = 248;
    public final static String TOKEN = "25d55ad283aa400af464c76d713c07ad";
}
