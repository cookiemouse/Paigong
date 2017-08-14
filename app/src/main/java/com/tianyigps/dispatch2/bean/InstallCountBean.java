package com.tianyigps.dispatch2.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/8.
 */

public class InstallCountBean {

    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":[],"msg":"操作成功","success":true}
     * msg : 操作成功
     * obj : [{"engineerJobNo":"123","engineerName":"234","doorNum":4,"finishCarNum":3,"finishWiredNum":2,"finishWirelessNum":1}]
     * success : true
     * time : 0
     */

    private String errCode;
    private String jsonStr;
    private String msg;
    private boolean success;
    private int time;
    private List<ObjBean> obj;

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

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * engineerJobNo : 123
         * engineerName : 234
         * doorNum : 4
         * finishCarNum : 3
         * finishWiredNum : 2
         * finishWirelessNum : 1
         */

        private String engineerJobNo;
        private String engineerName;
        private int doorNum;
        private int finishCarNum;
        private int finishWiredNum;
        private int finishWirelessNum;

        public String getEngineerJobNo() {
            return engineerJobNo;
        }

        public void setEngineerJobNo(String engineerJobNo) {
            this.engineerJobNo = engineerJobNo;
        }

        public String getEngineerName() {
            return engineerName;
        }

        public void setEngineerName(String engineerName) {
            this.engineerName = engineerName;
        }

        public int getDoorNum() {
            return doorNum;
        }

        public void setDoorNum(int doorNum) {
            this.doorNum = doorNum;
        }

        public int getFinishCarNum() {
            return finishCarNum;
        }

        public void setFinishCarNum(int finishCarNum) {
            this.finishCarNum = finishCarNum;
        }

        public int getFinishWiredNum() {
            return finishWiredNum;
        }

        public void setFinishWiredNum(int finishWiredNum) {
            this.finishWiredNum = finishWiredNum;
        }

        public int getFinishWirelessNum() {
            return finishWirelessNum;
        }

        public void setFinishWirelessNum(int finishWirelessNum) {
            this.finishWirelessNum = finishWirelessNum;
        }
    }
}
