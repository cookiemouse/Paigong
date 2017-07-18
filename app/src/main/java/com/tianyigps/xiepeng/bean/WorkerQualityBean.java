package com.tianyigps.xiepeng.bean;

/**
 * Created by djc on 2017/7/18.
 */

public class WorkerQualityBean {
    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":{"commitTimeoutNum":0,"eId":204,"eJobNo":"10004","eName":"胡梅","id":"a4e202b8-6b65-11e7-97f7-00163e00004f","installNotPositionedNum":4,"installWiredOfflineNum":0,"installWiredOutageNum":0,"installWirelessOfflineNum":0,"installWirelessOutageNum":0,"month":201707,"pictureExceptionNum":0,"repairNotPositionedNum":0,"repairWiredOfflineNum":0,"repairWiredOutageNum":0,"repairWirelessOfflineNum":0,"repairWirelessOutageNum":0,"signExceptionNum":1},"msg":"操作成功","success":true}
     * msg : 操作成功
     * obj : {"commitTimeoutNum":0,"eId":204,"eJobNo":"10004","eName":"胡梅","id":"a4e202b8-6b65-11e7-97f7-00163e00004f","installNotPositionedNum":4,"installWiredOfflineNum":0,"installWiredOutageNum":0,"installWirelessOfflineNum":0,"installWirelessOutageNum":0,"month":201707,"pictureExceptionNum":0,"repairNotPositionedNum":0,"repairWiredOfflineNum":0,"repairWiredOutageNum":0,"repairWirelessOfflineNum":0,"repairWirelessOutageNum":0,"signExceptionNum":1}
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
         * commitTimeoutNum : 0
         * eId : 204
         * eJobNo : 10004
         * eName : 胡梅
         * id : a4e202b8-6b65-11e7-97f7-00163e00004f
         * installNotPositionedNum : 4
         * installWiredOfflineNum : 0
         * installWiredOutageNum : 0
         * installWirelessOfflineNum : 0
         * installWirelessOutageNum : 0
         * month : 201707
         * pictureExceptionNum : 0
         * repairNotPositionedNum : 0
         * repairWiredOfflineNum : 0
         * repairWiredOutageNum : 0
         * repairWirelessOfflineNum : 0
         * repairWirelessOutageNum : 0
         * signExceptionNum : 1
         */

        private int commitTimeoutNum;
        private int eId;
        private String eJobNo;
        private String eName;
        private String id;
        private int installNotPositionedNum;
        private int installWiredOfflineNum;
        private int installWiredOutageNum;
        private int installWirelessOfflineNum;
        private int installWirelessOutageNum;
        private int month;
        private int pictureExceptionNum;
        private int repairNotPositionedNum;
        private int repairWiredOfflineNum;
        private int repairWiredOutageNum;
        private int repairWirelessOfflineNum;
        private int repairWirelessOutageNum;
        private int signExceptionNum;

        public int getCommitTimeoutNum() {
            return commitTimeoutNum;
        }

        public void setCommitTimeoutNum(int commitTimeoutNum) {
            this.commitTimeoutNum = commitTimeoutNum;
        }

        public int getEId() {
            return eId;
        }

        public void setEId(int eId) {
            this.eId = eId;
        }

        public String getEJobNo() {
            return eJobNo;
        }

        public void setEJobNo(String eJobNo) {
            this.eJobNo = eJobNo;
        }

        public String getEName() {
            return eName;
        }

        public void setEName(String eName) {
            this.eName = eName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getInstallNotPositionedNum() {
            return installNotPositionedNum;
        }

        public void setInstallNotPositionedNum(int installNotPositionedNum) {
            this.installNotPositionedNum = installNotPositionedNum;
        }

        public int getInstallWiredOfflineNum() {
            return installWiredOfflineNum;
        }

        public void setInstallWiredOfflineNum(int installWiredOfflineNum) {
            this.installWiredOfflineNum = installWiredOfflineNum;
        }

        public int getInstallWiredOutageNum() {
            return installWiredOutageNum;
        }

        public void setInstallWiredOutageNum(int installWiredOutageNum) {
            this.installWiredOutageNum = installWiredOutageNum;
        }

        public int getInstallWirelessOfflineNum() {
            return installWirelessOfflineNum;
        }

        public void setInstallWirelessOfflineNum(int installWirelessOfflineNum) {
            this.installWirelessOfflineNum = installWirelessOfflineNum;
        }

        public int getInstallWirelessOutageNum() {
            return installWirelessOutageNum;
        }

        public void setInstallWirelessOutageNum(int installWirelessOutageNum) {
            this.installWirelessOutageNum = installWirelessOutageNum;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getPictureExceptionNum() {
            return pictureExceptionNum;
        }

        public void setPictureExceptionNum(int pictureExceptionNum) {
            this.pictureExceptionNum = pictureExceptionNum;
        }

        public int getRepairNotPositionedNum() {
            return repairNotPositionedNum;
        }

        public void setRepairNotPositionedNum(int repairNotPositionedNum) {
            this.repairNotPositionedNum = repairNotPositionedNum;
        }

        public int getRepairWiredOfflineNum() {
            return repairWiredOfflineNum;
        }

        public void setRepairWiredOfflineNum(int repairWiredOfflineNum) {
            this.repairWiredOfflineNum = repairWiredOfflineNum;
        }

        public int getRepairWiredOutageNum() {
            return repairWiredOutageNum;
        }

        public void setRepairWiredOutageNum(int repairWiredOutageNum) {
            this.repairWiredOutageNum = repairWiredOutageNum;
        }

        public int getRepairWirelessOfflineNum() {
            return repairWirelessOfflineNum;
        }

        public void setRepairWirelessOfflineNum(int repairWirelessOfflineNum) {
            this.repairWirelessOfflineNum = repairWirelessOfflineNum;
        }

        public int getRepairWirelessOutageNum() {
            return repairWirelessOutageNum;
        }

        public void setRepairWirelessOutageNum(int repairWirelessOutageNum) {
            this.repairWirelessOutageNum = repairWirelessOutageNum;
        }

        public int getSignExceptionNum() {
            return signExceptionNum;
        }

        public void setSignExceptionNum(int signExceptionNum) {
            this.signExceptionNum = signExceptionNum;
        }
    }
}
