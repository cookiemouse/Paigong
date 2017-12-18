package com.tianyigps.dispatch2.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/7/25.
 */

public class StartOrderInfoBean {

    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":{"accessStatusTime":1500280742000,"beginInstallTime":1500279820000,"cName":"天易根客户1","cPath":"#1#","carList":[{"carBrand":"","carNo":"","carTerminalList":[{"id":314,"newInstallLocation":"Eeeeeeeeeeeee","newInstallLocationPic":"TY20170717143432075/2de3a21e0bda4f06acf8bffe62065a48.png","newSim":"3525440721108","newTNo":"352544072110852","newTerminalType":1,"newWiringDiagramPic":"TY20170717143432075/dd5a838856314802be6a43d9139ecc51.png","operateTime":1500280742000,"operateType":3,"orderCarId":444,"orderId":415,"positionType":3,"solution":"换设备","tNo":"866686022343064","terminalType":1}],"carVin":"LNBSCBAF5DR306551","createTime":1500273272000,"id":444,"newCarBrand":"Ddd","newCarNo":"","newCarVin":"LNBSCBAF5DR306551","orderId":415,"ownerCard":"","ownerName":"","removeFlag":0,"wiredAnnual":5,"wiredNum":1,"wirelessAnnual":1,"wirelessNum":0},{"carNo":"浙BK071D","carTerminalList":[{"id":306,"orderCarId":443,"orderId":415,"removeStatus":0,"sim":"1064822809690","tNo":"352544070783197","terminalName":"浙BK071D老面包","terminalType":1}],"carVin":"111122232","id":443,"orderId":415,"removeFlag":1,"wiredNum":1,"wirelessNum":0}],"checkInAddress":"江苏省南京市雨花台区文竹路南京乐荣科技有限公司东227米","checkInCoordinate":"118.78678456957314,31.978756278908243","checkInTime":1500279817000,"checkStatus":3,"cid":1,"cityId":2,"cityName":"北京市","createTime":1500273272000,"currentResponsiblePerson":"10004 胡梅 18256295795","detailAddress":"","dispatchContactName":"12312","dispatchContactPhone":"23123121212","dispatchTime":1500279788000,"districtId":3,"districtName":"东城区","doorTime":1500359702000,"eId":204,"finishWiredNum":1,"finishWirelessNum":0,"id":415,"lCid":1,"lName":"天易根客户1","lstCustomerUpdateTime":1500279788000,"lstUpdateTime":1500281497000,"orderNo":"TY20170717143432075","partSubmitReason":"","payDoorFee":0,"provinceId":1,"provinceName":"北京市","readStatusCustomer":1,"readStatusTy":0,"remark":"【天塘】","removeFinishWiredNum":0,"removeFinishWirelessNum":0,"removeReason":"asdsad","reviseStatus":0,"sceneContactName":"121212sdas","sceneContactPhone":"11111111111","signature":"data:image/png;base64,/9j/4ACgAoA//Z","status":3,"submitAddress":"","submitCoordinate":"-73.94253093699584,30.333243036283754","submitTime":1500280742000,"type":3},"msg":"操作成功","success":true}
     * msg : 操作成功
     * obj : {"accessStatusTime":1500280742000,"beginInstallTime":1500279820000,"cName":"天易根客户1","cPath":"#1#","carList":[{"carBrand":"","carNo":"","carTerminalList":[{"id":314,"newInstallLocation":"Eeeeeeeeeeeee","newInstallLocationPic":"TY20170717143432075/2de3a21e0bda4f06acf8bffe62065a48.png","newSim":"3525440721108","newTNo":"352544072110852","newTerminalType":1,"newWiringDiagramPic":"TY20170717143432075/dd5a838856314802be6a43d9139ecc51.png","operateTime":1500280742000,"operateType":3,"orderCarId":444,"orderId":415,"positionType":3,"solution":"换设备","tNo":"866686022343064","terminalType":1}],"carVin":"LNBSCBAF5DR306551","createTime":1500273272000,"id":444,"newCarBrand":"Ddd","newCarNo":"","newCarVin":"LNBSCBAF5DR306551","orderId":415,"ownerCard":"","ownerName":"","removeFlag":0,"wiredAnnual":5,"wiredNum":1,"wirelessAnnual":1,"wirelessNum":0},{"carNo":"浙BK071D","carTerminalList":[{"id":306,"orderCarId":443,"orderId":415,"removeStatus":0,"sim":"1064822809690","tNo":"352544070783197","terminalName":"浙BK071D老面包","terminalType":1}],"carVin":"111122232","id":443,"orderId":415,"removeFlag":1,"wiredNum":1,"wirelessNum":0}],"checkInAddress":"江苏省南京市雨花台区文竹路南京乐荣科技有限公司东227米","checkInCoordinate":"118.78678456957314,31.978756278908243","checkInTime":1500279817000,"checkStatus":3,"cid":1,"cityId":2,"cityName":"北京市","createTime":1500273272000,"currentResponsiblePerson":"10004 胡梅 18256295795","detailAddress":"","dispatchContactName":"12312","dispatchContactPhone":"23123121212","dispatchTime":1500279788000,"districtId":3,"districtName":"东城区","doorTime":1500359702000,"eId":204,"finishWiredNum":1,"finishWirelessNum":0,"id":415,"lCid":1,"lName":"天易根客户1","lstCustomerUpdateTime":1500279788000,"lstUpdateTime":1500281497000,"orderNo":"TY20170717143432075","partSubmitReason":"","payDoorFee":0,"provinceId":1,"provinceName":"北京市","readStatusCustomer":1,"readStatusTy":0,"remark":"【天塘】","removeFinishWiredNum":0,"removeFinishWirelessNum":0,"removeReason":"asdsad","reviseStatus":0,"sceneContactName":"121212sdas","sceneContactPhone":"11111111111","signature":"data:image/png;base64AoA//Z","status":3,"submitAddress":"","submitCoordinate":"-73.94253093699584,30.333243036283754","submitTime":1500280742000,"type":3}
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
         * accessStatusTime : 1500280742000
         * beginInstallTime : 1500279820000
         * cName : 天易根客户1
         * cPath : #1#
         * carList : [{"carBrand":"","carNo":"","carTerminalList":[{"id":314,"newInstallLocation":"Eeeeeeeeeeeee","newInstallLocationPic":"TY20170717143432075/2de3a21e0bda4f06acf8bffe62065a48.png","newSim":"3525440721108","newTNo":"352544072110852","newTerminalType":1,"newWiringDiagramPic":"TY20170717143432075/dd5a838856314802be6a43d9139ecc51.png","operateTime":1500280742000,"operateType":3,"orderCarId":444,"orderId":415,"positionType":3,"solution":"换设备","tNo":"866686022343064","terminalType":1}],"carVin":"LNBSCBAF5DR306551","createTime":1500273272000,"id":444,"newCarBrand":"Ddd","newCarNo":"","newCarVin":"LNBSCBAF5DR306551","orderId":415,"ownerCard":"","ownerName":"","removeFlag":0,"wiredAnnual":5,"wiredNum":1,"wirelessAnnual":1,"wirelessNum":0},{"carNo":"浙BK071D","carTerminalList":[{"id":306,"orderCarId":443,"orderId":415,"removeStatus":0,"sim":"1064822809690","tNo":"352544070783197","terminalName":"浙BK071D老面包","terminalType":1}],"carVin":"111122232","id":443,"orderId":415,"removeFlag":1,"wiredNum":1,"wirelessNum":0}]
         * checkInAddress : 江苏省南京市雨花台区文竹路南京乐荣科技有限公司东227米
         * checkInCoordinate : 118.78678456957314,31.978756278908243
         * checkInTime : 1500279817000
         * checkStatus : 3
         * cid : 1
         * cityId : 2
         * cityName : 北京市
         * createTime : 1500273272000
         * currentResponsiblePerson : 10004 胡梅 18256295795
         * detailAddress :
         * dispatchContactName : 12312
         * dispatchContactPhone : 23123121212
         * dispatchTime : 1500279788000
         * districtId : 3
         * districtName : 东城区
         * doorTime : 1500359702000
         * eId : 204
         * finishWiredNum : 1
         * finishWirelessNum : 0
         * id : 415
         * lCid : 1
         * lName : 天易根客户1
         * lstCustomerUpdateTime : 1500279788000
         * lstUpdateTime : 1500281497000
         * orderNo : TY20170717143432075
         * partSubmitReason :
         * payDoorFee : 0
         * provinceId : 1
         * provinceName : 北京市
         * readStatusCustomer : 1
         * readStatusTy : 0
         * remark : 【天塘】
         * removeFinishWiredNum : 0
         * removeFinishWirelessNum : 0
         * removeReason : asdsad
         * reviseStatus : 0
         * sceneContactName : 121212sdas
         * sceneContactPhone : 11111111111
         * signature : data:image/png;base64AoA//Z
         * status : 3
         * submitAddress :
         * submitCoordinate : -73.94253093699584,30.333243036283754
         * submitTime : 1500280742000
         * type : 3
         */

        private long accessStatusTime;
        private long beginInstallTime;
        private String cName;
        private String cPath;
        private String checkInAddress;
        private String checkInCoordinate;
        private long checkInTime;
        private int checkStatus;
        private int cid;
        private int cityId;
        private String cityName;
        private long createTime;
        private String currentResponsiblePerson;
        private String detailAddress;
        private String dispatchContactName;
        private String dispatchContactPhone;
        private long dispatchTime;
        private int districtId;
        private String districtName;
        private long doorTime;
        private int eId;
        private int finishWiredNum;
        private int finishWirelessNum;
        private int id;
        private int lCid;
        private String lName;
        private long lstCustomerUpdateTime;
        private long lstUpdateTime;
        private String orderNo;
        private String partSubmitReason;
        private int payDoorFee;
        private int provinceId;
        private String provinceName;
        private int readStatusCustomer;
        private int readStatusTy;
        private String remark;
        private int removeFinishWiredNum;
        private int removeFinishWirelessNum;
        private String removeReason;
        private int reviseStatus;
        private String sceneContactName;
        private String sceneContactPhone;
        private String signature;
        private int status;
        private String submitAddress;
        private String submitCoordinate;
        private long submitTime;
        private int type;
        private List<CarListBean> carList;

        public long getAccessStatusTime() {
            return accessStatusTime;
        }

        public void setAccessStatusTime(long accessStatusTime) {
            this.accessStatusTime = accessStatusTime;
        }

        public long getBeginInstallTime() {
            return beginInstallTime;
        }

        public void setBeginInstallTime(long beginInstallTime) {
            this.beginInstallTime = beginInstallTime;
        }

        public String getCName() {
            return cName;
        }

        public void setCName(String cName) {
            this.cName = cName;
        }

        public String getCPath() {
            return cPath;
        }

        public void setCPath(String cPath) {
            this.cPath = cPath;
        }

        public String getCheckInAddress() {
            return checkInAddress;
        }

        public void setCheckInAddress(String checkInAddress) {
            this.checkInAddress = checkInAddress;
        }

        public String getCheckInCoordinate() {
            return checkInCoordinate;
        }

        public void setCheckInCoordinate(String checkInCoordinate) {
            this.checkInCoordinate = checkInCoordinate;
        }

        public long getCheckInTime() {
            return checkInTime;
        }

        public void setCheckInTime(long checkInTime) {
            this.checkInTime = checkInTime;
        }

        public int getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(int checkStatus) {
            this.checkStatus = checkStatus;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCurrentResponsiblePerson() {
            return currentResponsiblePerson;
        }

        public void setCurrentResponsiblePerson(String currentResponsiblePerson) {
            this.currentResponsiblePerson = currentResponsiblePerson;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public String getDispatchContactName() {
            return dispatchContactName;
        }

        public void setDispatchContactName(String dispatchContactName) {
            this.dispatchContactName = dispatchContactName;
        }

        public String getDispatchContactPhone() {
            return dispatchContactPhone;
        }

        public void setDispatchContactPhone(String dispatchContactPhone) {
            this.dispatchContactPhone = dispatchContactPhone;
        }

        public long getDispatchTime() {
            return dispatchTime;
        }

        public void setDispatchTime(long dispatchTime) {
            this.dispatchTime = dispatchTime;
        }

        public int getDistrictId() {
            return districtId;
        }

        public void setDistrictId(int districtId) {
            this.districtId = districtId;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public long getDoorTime() {
            return doorTime;
        }

        public void setDoorTime(long doorTime) {
            this.doorTime = doorTime;
        }

        public int getEId() {
            return eId;
        }

        public void setEId(int eId) {
            this.eId = eId;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLCid() {
            return lCid;
        }

        public void setLCid(int lCid) {
            this.lCid = lCid;
        }

        public String getLName() {
            return lName;
        }

        public void setLName(String lName) {
            this.lName = lName;
        }

        public long getLstCustomerUpdateTime() {
            return lstCustomerUpdateTime;
        }

        public void setLstCustomerUpdateTime(long lstCustomerUpdateTime) {
            this.lstCustomerUpdateTime = lstCustomerUpdateTime;
        }

        public long getLstUpdateTime() {
            return lstUpdateTime;
        }

        public void setLstUpdateTime(long lstUpdateTime) {
            this.lstUpdateTime = lstUpdateTime;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getPartSubmitReason() {
            return partSubmitReason;
        }

        public void setPartSubmitReason(String partSubmitReason) {
            this.partSubmitReason = partSubmitReason;
        }

        public int getPayDoorFee() {
            return payDoorFee;
        }

        public void setPayDoorFee(int payDoorFee) {
            this.payDoorFee = payDoorFee;
        }

        public int getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(int provinceId) {
            this.provinceId = provinceId;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public int getReadStatusCustomer() {
            return readStatusCustomer;
        }

        public void setReadStatusCustomer(int readStatusCustomer) {
            this.readStatusCustomer = readStatusCustomer;
        }

        public int getReadStatusTy() {
            return readStatusTy;
        }

        public void setReadStatusTy(int readStatusTy) {
            this.readStatusTy = readStatusTy;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getRemoveFinishWiredNum() {
            return removeFinishWiredNum;
        }

        public void setRemoveFinishWiredNum(int removeFinishWiredNum) {
            this.removeFinishWiredNum = removeFinishWiredNum;
        }

        public int getRemoveFinishWirelessNum() {
            return removeFinishWirelessNum;
        }

        public void setRemoveFinishWirelessNum(int removeFinishWirelessNum) {
            this.removeFinishWirelessNum = removeFinishWirelessNum;
        }

        public String getRemoveReason() {
            return removeReason;
        }

        public void setRemoveReason(String removeReason) {
            this.removeReason = removeReason;
        }

        public int getReviseStatus() {
            return reviseStatus;
        }

        public void setReviseStatus(int reviseStatus) {
            this.reviseStatus = reviseStatus;
        }

        public String getSceneContactName() {
            return sceneContactName;
        }

        public void setSceneContactName(String sceneContactName) {
            this.sceneContactName = sceneContactName;
        }

        public String getSceneContactPhone() {
            return sceneContactPhone;
        }

        public void setSceneContactPhone(String sceneContactPhone) {
            this.sceneContactPhone = sceneContactPhone;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSubmitAddress() {
            return submitAddress;
        }

        public void setSubmitAddress(String submitAddress) {
            this.submitAddress = submitAddress;
        }

        public String getSubmitCoordinate() {
            return submitCoordinate;
        }

        public void setSubmitCoordinate(String submitCoordinate) {
            this.submitCoordinate = submitCoordinate;
        }

        public long getSubmitTime() {
            return submitTime;
        }

        public void setSubmitTime(long submitTime) {
            this.submitTime = submitTime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<CarListBean> getCarList() {
            return carList;
        }

        public void setCarList(List<CarListBean> carList) {
            this.carList = carList;
        }

        public static class CarListBean {
            /**
             * carBrand :
             * carNo :
             * carTerminalList : [{"id":314,"newInstallLocation":"Eeeeeeeeeeeee","newInstallLocationPic":"TY20170717143432075/2de3a21e0bda4f06acf8bffe62065a48.png","newSim":"3525440721108","newTNo":"352544072110852","newTerminalType":1,"newWiringDiagramPic":"TY20170717143432075/dd5a838856314802be6a43d9139ecc51.png","operateTime":1500280742000,"operateType":3,"orderCarId":444,"orderId":415,"positionType":3,"solution":"换设备","tNo":"866686022343064","terminalType":1}]
             * carVin : LNBSCBAF5DR306551
             * createTime : 1500273272000
             * id : 444
             * newCarBrand : Ddd
             * newCarNo :
             * newCarVin : LNBSCBAF5DR306551
             * orderId : 415
             * ownerCard :
             * ownerName :
             * removeFlag : 0
             * wiredAnnual : 5
             * wiredNum : 1
             * wirelessAnnual : 1
             * wirelessNum : 0
             */

            private String carBrand;
            private String carNo;
            private String carVin;
            private long createTime;
            private int id;
            private String newCarBrand;
            private String newCarNo;
            private String carNoPic;
            private String newCarVin;
            private String carVinPic;
            private int orderId;
            private String ownerCard;
            private String ownerName;
            private String pic1;
            private String pic2;
            private String pic3;
            private String pic4;
            private String pic5;
            private String pic6;
            private String pic7;
            private String pic8;
            private String pic9;
            private int removeFlag;
            private int wiredAnnual;
            private int wiredNum;
            private int wirelessAnnual;
            private int wirelessNum;
            private List<CarTerminalListBean> carTerminalList;

            public String getCarBrand() {
                return carBrand;
            }

            public void setCarBrand(String carBrand) {
                this.carBrand = carBrand;
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

            public String getCarNoPic() {
                return carNoPic;
            }

            public void setCarNoPic(String carNoPic) {
                this.carNoPic = carNoPic;
            }

            public String getCarVinPic() {
                return carVinPic;
            }

            public void setCarVinPic(String carVinPic) {
                this.carVinPic = carVinPic;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNewCarBrand() {
                return newCarBrand;
            }

            public void setNewCarBrand(String newCarBrand) {
                this.newCarBrand = newCarBrand;
            }

            public String getNewCarNo() {
                return newCarNo;
            }

            public void setNewCarNo(String newCarNo) {
                this.newCarNo = newCarNo;
            }

            public String getNewCarVin() {
                return newCarVin;
            }

            public void setNewCarVin(String newCarVin) {
                this.newCarVin = newCarVin;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public String getOwnerCard() {
                return ownerCard;
            }

            public void setOwnerCard(String ownerCard) {
                this.ownerCard = ownerCard;
            }

            public String getOwnerName() {
                return ownerName;
            }

            public void setOwnerName(String ownerName) {
                this.ownerName = ownerName;
            }

            public String getPic1() {
                return pic1;
            }

            public void setPic1(String pic1) {
                this.pic1 = pic1;
            }

            public String getPic2() {
                return pic2;
            }

            public void setPic2(String pic2) {
                this.pic2 = pic2;
            }

            public String getPic3() {
                return pic3;
            }

            public void setPic3(String pic3) {
                this.pic3 = pic3;
            }

            public String getPic4() {
                return pic4;
            }

            public void setPic4(String pic4) {
                this.pic4 = pic4;
            }

            public String getPic5() {
                return pic5;
            }

            public void setPic5(String pic5) {
                this.pic5 = pic5;
            }

            public String getPic6() {
                return pic6;
            }

            public void setPic6(String pic6) {
                this.pic6 = pic6;
            }

            public String getPic7() {
                return pic7;
            }

            public void setPic7(String pic7) {
                this.pic7 = pic7;
            }

            public String getPic8() {
                return pic8;
            }

            public void setPic8(String pic8) {
                this.pic8 = pic8;
            }

            public String getPic9() {
                return pic9;
            }

            public void setPic9(String pic9) {
                this.pic9 = pic9;
            }

            public int getRemoveFlag() {
                return removeFlag;
            }

            public void setRemoveFlag(int removeFlag) {
                this.removeFlag = removeFlag;
            }

            public int getWiredAnnual() {
                return wiredAnnual;
            }

            public void setWiredAnnual(int wiredAnnual) {
                this.wiredAnnual = wiredAnnual;
            }

            public int getWiredNum() {
                return wiredNum;
            }

            public void setWiredNum(int wiredNum) {
                this.wiredNum = wiredNum;
            }

            public int getWirelessAnnual() {
                return wirelessAnnual;
            }

            public void setWirelessAnnual(int wirelessAnnual) {
                this.wirelessAnnual = wirelessAnnual;
            }

            public int getWirelessNum() {
                return wirelessNum;
            }

            public void setWirelessNum(int wirelessNum) {
                this.wirelessNum = wirelessNum;
            }

            public List<CarTerminalListBean> getCarTerminalList() {
                return carTerminalList;
            }

            public void setCarTerminalList(List<CarTerminalListBean> carTerminalList) {
                this.carTerminalList = carTerminalList;
            }

            public static class CarTerminalListBean {
                /**
                 * id : 314
                 * newInstallLocation : Eeeeeeeeeeeee
                 * newInstallLocationPic : TY20170717143432075/2de3a21e0bda4f06acf8bffe62065a48.png
                 * newSim : 3525440721108
                 * newTNo : 352544072110852
                 * newTerminalType : 1
                 * newWiringDiagramPic : TY20170717143432075/dd5a838856314802be6a43d9139ecc51.png
                 * operateTime : 1500280742000
                 * operateType : 3
                 * orderCarId : 444
                 * orderId : 415
                 * positionType : 3
                 * solution : 换设备
                 * tNo : 866686022343064
                 * terminalType : 1
                 */

                private int id;
                private String newInstallLocation;
                private String newInstallLocationPic;
                private String installLocationPic;
                private String wiringDiagramPic;
                private String installLocation;
                private String malDesc;
                private String terminalName;
                private String newSim;
                private String sim;
                private String newTNo;
                private int newTerminalType;
                private String newWiringDiagramPic;
                private long operateTime;
                private int operateType;
                private int orderCarId;
                private int orderId;
                private int removeStatus;
                private int positionType;
                private int repaireStatus;
                private String solution;
                private String tNo;
                private String repaireDesc;
                private int terminalType;

                public int getRemoveStatus() {
                    return removeStatus;
                }

                public void setRemoveStatus(int removeStatus) {
                    this.removeStatus = removeStatus;
                }

                public String getTerminalName() {
                    return terminalName;
                }

                public void setTerminalName(String terminalName) {
                    this.terminalName = terminalName;
                }

                public String getInstallLocation() {
                    return installLocation;
                }

                public void setInstallLocation(String installLocation) {
                    this.installLocation = installLocation;
                }

                public String getMalDesc() {
                    return malDesc;
                }

                public void setMalDesc(String malDesc) {
                    this.malDesc = malDesc;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getSim() {
                    return sim;
                }

                public void setSim(String sim) {
                    this.sim = sim;
                }

                public String getNewInstallLocation() {
                    return newInstallLocation;
                }

                public void setNewInstallLocation(String newInstallLocation) {
                    this.newInstallLocation = newInstallLocation;
                }

                public String getNewInstallLocationPic() {
                    return newInstallLocationPic;
                }

                public String getInstallLocationPic() {
                    return installLocationPic;
                }

                public void setInstallLocationPic(String installLocationPic) {
                    this.installLocationPic = installLocationPic;
                }

                public String getWiringDiagramPic() {
                    return wiringDiagramPic;
                }

                public void setWiringDiagramPic(String wiringDiagramPic) {
                    this.wiringDiagramPic = wiringDiagramPic;
                }

                public String gettNo() {
                    return tNo;
                }

                public void settNo(String tNo) {
                    this.tNo = tNo;
                }

                public void setNewInstallLocationPic(String newInstallLocationPic) {
                    this.newInstallLocationPic = newInstallLocationPic;
                }

                public String getNewSim() {
                    return newSim;
                }

                public void setNewSim(String newSim) {
                    this.newSim = newSim;
                }

                public String getNewTNo() {
                    return newTNo;
                }

                public void setNewTNo(String newTNo) {
                    this.newTNo = newTNo;
                }

                public int getNewTerminalType() {
                    return newTerminalType;
                }

                public void setNewTerminalType(int newTerminalType) {
                    this.newTerminalType = newTerminalType;
                }

                public String getNewWiringDiagramPic() {
                    return newWiringDiagramPic;
                }

                public void setNewWiringDiagramPic(String newWiringDiagramPic) {
                    this.newWiringDiagramPic = newWiringDiagramPic;
                }

                public long getOperateTime() {
                    return operateTime;
                }

                public void setOperateTime(long operateTime) {
                    this.operateTime = operateTime;
                }

                public int getOperateType() {
                    return operateType;
                }

                public void setOperateType(int operateType) {
                    this.operateType = operateType;
                }

                public int getOrderCarId() {
                    return orderCarId;
                }

                public void setOrderCarId(int orderCarId) {
                    this.orderCarId = orderCarId;
                }

                public int getOrderId() {
                    return orderId;
                }

                public void setOrderId(int orderId) {
                    this.orderId = orderId;
                }

                public int getPositionType() {
                    return positionType;
                }

                public void setPositionType(int positionType) {
                    this.positionType = positionType;
                }

                public String getSolution() {
                    return solution;
                }

                public void setSolution(String solution) {
                    this.solution = solution;
                }

                public String getTNo() {
                    return tNo;
                }

                public void setTNo(String tNo) {
                    this.tNo = tNo;
                }

                public int getTerminalType() {
                    return terminalType;
                }

                public void setTerminalType(int terminalType) {
                    this.terminalType = terminalType;
                }

                public int getRepaireStatus() {
                    return repaireStatus;
                }

                public void setRepaireStatus(int repaireStatus) {
                    this.repaireStatus = repaireStatus;
                }

                public String getRepaireDesc() {
                    return repaireDesc;
                }

                public void setRepaireDesc(String repaireDesc) {
                    this.repaireDesc = repaireDesc;
                }
            }
        }
    }
}
