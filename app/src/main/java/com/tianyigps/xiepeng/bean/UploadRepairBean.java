package com.tianyigps.xiepeng.bean;

/**
 * Created by cookiemouse on 2017/8/3.
 */

public class UploadRepairBean {
    /**
     * terminalInfo : {"tid":0,"oldImei":"imeiOld","newImei":"imeiNew","modle":1,"locateType":1,"newInstallPosition":"","repaireRemark":""}
     */

    private TerminalInfoBean terminalInfo;

    public TerminalInfoBean getTerminalInfo() {
        return terminalInfo;
    }

    public void setTerminalInfo(TerminalInfoBean terminalInfo) {
        this.terminalInfo = terminalInfo;
    }

    public static class TerminalInfoBean {
        /**
         * tid : 0
         * oldImei : imeiOld
         * newImei : imeiNew
         * modle : 1
         * locateType : 1
         * newInstallPosition :
         * repaireRemark :
         */

        private int tid;
        private String oldImei;
        private String newImei;
        private int modle;
        private int locateType;
        private String newInstallPosition;
        private String repaireRemark;

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public String getOldImei() {
            return oldImei;
        }

        public void setOldImei(String oldImei) {
            this.oldImei = oldImei;
        }

        public String getNewImei() {
            return newImei;
        }

        public void setNewImei(String newImei) {
            this.newImei = newImei;
        }

        public int getModle() {
            return modle;
        }

        public void setModle(int modle) {
            this.modle = modle;
        }

        public int getLocateType() {
            return locateType;
        }

        public void setLocateType(int locateType) {
            this.locateType = locateType;
        }

        public String getNewInstallPosition() {
            return newInstallPosition;
        }

        public void setNewInstallPosition(String newInstallPosition) {
            this.newInstallPosition = newInstallPosition;
        }

        public String getRepaireRemark() {
            return repaireRemark;
        }

        public void setRepaireRemark(String repaireRemark) {
            this.repaireRemark = repaireRemark;
        }
    }
}
