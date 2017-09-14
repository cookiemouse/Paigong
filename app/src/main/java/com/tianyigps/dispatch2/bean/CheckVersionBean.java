package com.tianyigps.dispatch2.bean;

/**
 * Created by cookiemouse on 2017/9/14.
 */

public class CheckVersionBean {
    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":{"appUpdateUrl":"www.badu.com","appVersion":"2.0"},"msg":"操作成功","success":true}
     * msg : 操作成功
     * obj : {"appUpdateUrl":"www.badu.com","appVersion":"2.0"}
     * success : true
     * time : 0
     */

    private String errCode;
    private String jsonStr;
    private String msg;
    private ObjBean obj;
    private boolean success;
    private int time;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public static class ObjBean {
        /**
         * appUpdateUrl : www.badu.com
         * appVersion : 2.0
         */

        private String appUpdateUrl;
        private String appVersion;

        public String getAppUpdateUrl() {
            return appUpdateUrl;
        }

        public void setAppUpdateUrl(String appUpdateUrl) {
            this.appUpdateUrl = appUpdateUrl;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }
    }
}
