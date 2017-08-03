package com.tianyigps.xiepeng.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/3.
 */

public class UploadInstallBean {
    /**
     * carInfo : {"carId":0,"carNo":"carNo","carVin":"carVin","carBrand":"carBrand","terminalInfo":[{"tid":0,"oldImei":"imeiOld","newImei":"imeiNew","modle":1,"locateType":1,"newInstallPosition":""}]}
     */

    private CarInfoBean carInfo;

    public CarInfoBean getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(CarInfoBean carInfo) {
        this.carInfo = carInfo;
    }

    public static class CarInfoBean {
        /**
         * carId : 0
         * carNo : carNo
         * carVin : carVin
         * carBrand : carBrand
         * terminalInfo : [{"tid":0,"oldImei":"imeiOld","newImei":"imeiNew","modle":1,"locateType":1,"newInstallPosition":""}]
         */

        private int carId;
        private String carNo;
        private String carVin;
        private String carBrand;
        private List<TerminalInfoBean> terminalInfo;

        public int getCarId() {
            return carId;
        }

        public void setCarId(int carId) {
            this.carId = carId;
        }

        public String getCarNo() {
            return carNo;
        }

        public void setCarNo(String carNo) {
            this.carNo = carNo;
        }

        public String getCarVin() {
            return carVin;
        }

        public void setCarVin(String carVin) {
            this.carVin = carVin;
        }

        public String getCarBrand() {
            return carBrand;
        }

        public void setCarBrand(String carBrand) {
            this.carBrand = carBrand;
        }

        public List<TerminalInfoBean> getTerminalInfo() {
            return terminalInfo;
        }

        public void setTerminalInfo(List<TerminalInfoBean> terminalInfo) {
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
             */

            private int tid;
            private String oldImei;
            private String newImei;
            private int modle;
            private int locateType;
            private String newInstallPosition;

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
        }
    }
}
