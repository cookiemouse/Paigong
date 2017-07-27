package com.tianyigps.xiepeng.data;

/**
 * Created by djc on 2017/7/10.
 */

public class Data {
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

    //  intent传值
    public final static String DATA_INTENT_EID = "paigong_intent_eid";
    public final static String DATA_INTENT_TOKEN = "paigong_intent_token";
    public final static String DATA_INTENT_ORDER_NO = "paigong_intent_order_no";
    public final static String DATA_INTENT_T_NO = "paigong_intent_t_no";
    public final static String DATA_INTENT_ADDRESS = "paigong_intent_address";
    public final static String DATA_INTENT_INSTALL_TYPE = "paigong_intent_install_type";

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

    //  数据库
    public final static String DATA_TAB_REPAIR = "paigong_database_tab_repair";
    public final static String DATA_TAB_REMOVE = "paigong_database_tab_remove";
    public final static String DATA_TAB_INSTALL_CAR = "paigong_database_tab_install_car";
    public final static String DATA_TAB_INSTALL_TERMINAL = "paigong_database_tab_install_terminal";
    public final static String DATA_DB_NAME = "paigong_database_db.db";

    //  测试
    public final static int EID = 204;
    public final static String TOKEN = "25d55ad283aa400af464c76d713c07ad";
}
