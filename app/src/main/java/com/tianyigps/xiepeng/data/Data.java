package com.tianyigps.xiepeng.data;

/**
 * Created by djc on 2017/7/10.
 */

public class Data {
    public final static String DATA_SHAREDPREFERENCES = "paigong_sharedpreferences";

    //  账号类型，worker或manager
    public final static String DATA_S_STATE = "paigong_sharedpreferences_state";
    public final static int DATA_S_STATE_1 = 0;
    public final static int DATA_S_STATE_2 = 1;

    //  二维码扫描，Activity跳转
    public final static String DATA_SCANNER = "paigong_scanner";
    public final static int DATA_SCANNER_REQUEST = 0x001;
    public final static int DATA_SCANNER_RESULT = 0x002;

    //  数据请求所需数据
    public final static String DATA_JSON = "paigong_json_";
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
}
