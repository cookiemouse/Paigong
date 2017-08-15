package com.tianyigps.dispatch2.data;

/**
 * Created by djc on 2017/7/17.
 */

public class Urls {
    private static final String IP = "http://sit.tianyigps.cn/dispatch-services";

    public static final String URL_CHECK_USER = IP + "/dispatch4AppController/checkuser?";
    public static final String URL_CHANGE_PSD = IP + "/dispatch4AppController/changePwd?";

    public static final String URL_WORKER_ORDER = IP + "/dispatch4AppController/getPendingOrders4Engineer?";
    public static final String URL_WORKER_INSTALL_BACK = IP + "/dispatch4AppController/returnOrder4Engineer?";
    public static final String URL_WORKER_CONTACT_SITE = IP + "/dispatch4AppController/contactSite4Engineer?";
    public static final String URL_WORKER_SIGNED = IP + "/dispatch4AppController/signed4Engineer?";
    public static final String URL_WORKER_GET_TERMINAL_INFO = IP + "/dispatch4AppController/getTerminalGpsInfo4Engineer?";
    public static final String URL_WORKER_ORDER_HANDING = IP + "/dispatch4AppController/getTakingOrders4Engineer?";
    public static final String URL_WORKER_ORDER_INFO_HANDING = IP + "/dispatch4AppController/getTakingOrderInfo4Engineer?";
    public static final String URL_WORKER_START_HANDING = IP + "/dispatch4AppController/startOrder4Engineer?";
    public static final String URL_WORKER_START_INFO_HANDING = IP + "/dispatch4AppController/getStartOrderInfo4Engineer?";
    public static final String URL_WORKER_REMOVE_TERMINAL = IP + "/dispatch4AppController/removeTerminal4Engineer?";
    public static final String URL_WORKER_CHECK_IMEI = IP + "/dispatch4AppController/checkImeiInfo4Engineer?";
    public static final String URL_WORKER_ORDER_HANDED = IP + "/dispatch4AppController/getFinishOrders4Engineer?";
    public static final String URL_WORKER_QUALITY_COUNT = IP + "/dispatch4AppController/getQualityCount4Engineer?";
    public static final String URL_WORKER_LAST_INSTALLER = IP + "/dispatch4AppController/getLastInstaller4Engineer?";
    public static final String URL_WORKER_SAVE_ORDER_INFO = IP + "/dispatch4AppController/saveOrderInfo4Engineer";
    public static final String URL_WORKER_REMOVE_TERMINAL_INFO = IP + "/dispatch4AppController/removeOrderCarTerminal?";
    public static final String URL_WORKER_UPLOAD_PIC = IP + "/dispatch4AppController/uploadPic4Engineer";
    public static final String URL_WORKER_DELETE_PIC = IP + "/dispatch4AppController/deletePic4Engineer?";
    public static final String URL_WORKER_WHOLE_IMEI = IP + "/dispatch4AppController/getWholeImei4Engineer?";

    //================================分割线========================================
    public static final String URL_MANAGER_PENDING_ORDER = IP + "/dispatch4AppController/getPendingOrders4Director?";
    public static final String URL_MANAGER_WORKER = IP + "/dispatch4AppController/getEngineers4Director?";
    public static final String URL_MANAGER_INSTALL_COUNT = IP + "/dispatch4AppController/getInstallCount4Director?";
    public static final String URL_MANAGER_ORDER_TRACK = IP + "/dispatch4AppController/getOrderFlowByOrderId?";
    public static final String URL_MANAGER_ORDER_PENDED = IP + "/dispatch4AppController/getHistoryOrders4Director?";
    public static final String URL_MANAGER_PEND = IP + "/dispatch4AppController/dispatchOrder?";
    public static final String URL_MANAGER_PENDING_NUM = IP + "/dispatch4AppController/getOrderCountForPending?";
    public static final String URL_MANAGER_PENDED_NUM = IP + "/dispatch4AppController/getOrderCountForProcessed?";
    public static final String URL_MANAGER_PENDED_DETAILS = IP + "/dispatch4AppController/getOrderInfoByOrderNo?";
    public static final String URL_MANAGER_MODIFY_DATE = IP + "/dispatch4AppController/changeDoorDateByOrderNo?";
}
