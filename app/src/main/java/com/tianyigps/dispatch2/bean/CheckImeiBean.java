package com.tianyigps.dispatch2.bean;

/**
 * Created by cookiemouse on 2017/8/22.
 */

public class CheckImeiBean {

    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":{"changeFlag":"1","imei":"14470000583"},"msg":"验证通过","success":true}
     * msg : 验证通过
     * obj : {"changeFlag":"1","imei":"14470000583"}
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
         * changeFlag : 1
         * imei : 14470000583
         */

        private String changeFlag;
        private String imei;

        public String getChangeFlag() {
            return changeFlag;
        }

        public void setChangeFlag(String changeFlag) {
            this.changeFlag = changeFlag;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }
    }
}
