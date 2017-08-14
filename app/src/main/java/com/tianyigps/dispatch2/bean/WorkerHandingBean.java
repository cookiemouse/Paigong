package com.tianyigps.dispatch2.bean;

import java.util.List;

/**
 * Created by djc on 2017/7/18.
 */

public class WorkerHandingBean {
    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":[{"removeFinishWirelessNum":0,"contactPhone":"11111111111","detail":"","wirelessNum":0,"removeWiredNum":1,"orderType":3,"submitTime":1500280742000,"removeFinishWiredNum":0,"wiredNum":1,"orderStatus":3,"removeWirelessNum":0,"city":"北京市","reviseFlag":0,"orderNo":"TY20170717143432075","doorTime":1500359702000,"checkStatus":3,"contactName":"121212sdas","finishWiredNum":1,"province":"北京市","custName":"天易根客户1","district":"东城区","finishWirelessNum":0,"orderId":415},{"removeFinishWirelessNum":0,"contactPhone":"17052562223","detail":"","wirelessNum":3,"removeWiredNum":0,"orderType":1,"submitTime":1500339034000,"removeFinishWiredNum":0,"wiredNum":2,"orderStatus":3,"removeWirelessNum":0,"city":"南京市","reviseFlag":0,"orderNo":"TY20170717115258899","doorTime":1500522778000,"checkStatus":3,"contactName":"xp","finishWiredNum":1,"province":"江苏省","custName":"天易根客户1","district":"雨花台区","finishWirelessNum":0,"orderId":413}],"msg":"操作成功","success":true}
     * msg : 操作成功
     * obj : [{"removeFinishWirelessNum":0,"contactPhone":"11111111111","detail":"","wirelessNum":0,"removeWiredNum":1,"orderType":3,"submitTime":1500280742000,"removeFinishWiredNum":0,"wiredNum":1,"orderStatus":3,"removeWirelessNum":0,"city":"北京市","reviseFlag":0,"orderNo":"TY20170717143432075","doorTime":1500359702000,"checkStatus":3,"contactName":"121212sdas","finishWiredNum":1,"province":"北京市","custName":"天易根客户1","district":"东城区","finishWirelessNum":0,"orderId":415},{"removeFinishWirelessNum":0,"contactPhone":"17052562223","detail":"","wirelessNum":3,"removeWiredNum":0,"orderType":1,"submitTime":1500339034000,"removeFinishWiredNum":0,"wiredNum":2,"orderStatus":3,"removeWirelessNum":0,"city":"南京市","reviseFlag":0,"orderNo":"TY20170717115258899","doorTime":1500522778000,"checkStatus":3,"contactName":"xp","finishWiredNum":1,"province":"江苏省","custName":"天易根客户1","district":"雨花台区","finishWirelessNum":0,"orderId":413}]
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
         * removeFinishWirelessNum : 0
         * contactPhone : 11111111111
         * detail :
         * wirelessNum : 0
         * removeWiredNum : 1
         * orderType : 3
         * submitTime : 1500280742000
         * removeFinishWiredNum : 0
         * wiredNum : 1
         * orderStatus : 3
         * removeWirelessNum : 0
         * city : 北京市
         * reviseFlag : 0
         * orderNo : TY20170717143432075
         * doorTime : 1500359702000
         * checkStatus : 3
         * contactName : 121212sdas
         * finishWiredNum : 1
         * province : 北京市
         * custName : 天易根客户1
         * district : 东城区
         * finishWirelessNum : 0
         * orderId : 415
         */

        private int removeFinishWirelessNum;
        private String contactPhone;
        private String detail;
        private int wirelessNum;
        private int removeWiredNum;
        private int orderType;
        private long submitTime;
        private int removeFinishWiredNum;
        private int wiredNum;
        private int orderStatus;
        private int removeWirelessNum;
        private String city;
        private int reviseFlag;
        private String orderNo;
        private long doorTime;
        private int checkStatus;
        private String contactName;
        private int finishWiredNum;
        private String province;
        private String custName;
        private String district;
        private int finishWirelessNum;
        private int orderId;

        public int getRemoveFinishWirelessNum() {
            return removeFinishWirelessNum;
        }

        public void setRemoveFinishWirelessNum(int removeFinishWirelessNum) {
            this.removeFinishWirelessNum = removeFinishWirelessNum;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public int getWirelessNum() {
            return wirelessNum;
        }

        public void setWirelessNum(int wirelessNum) {
            this.wirelessNum = wirelessNum;
        }

        public int getRemoveWiredNum() {
            return removeWiredNum;
        }

        public void setRemoveWiredNum(int removeWiredNum) {
            this.removeWiredNum = removeWiredNum;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public long getSubmitTime() {
            return submitTime;
        }

        public void setSubmitTime(long submitTime) {
            this.submitTime = submitTime;
        }

        public int getRemoveFinishWiredNum() {
            return removeFinishWiredNum;
        }

        public void setRemoveFinishWiredNum(int removeFinishWiredNum) {
            this.removeFinishWiredNum = removeFinishWiredNum;
        }

        public int getWiredNum() {
            return wiredNum;
        }

        public void setWiredNum(int wiredNum) {
            this.wiredNum = wiredNum;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getRemoveWirelessNum() {
            return removeWirelessNum;
        }

        public void setRemoveWirelessNum(int removeWirelessNum) {
            this.removeWirelessNum = removeWirelessNum;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getReviseFlag() {
            return reviseFlag;
        }

        public void setReviseFlag(int reviseFlag) {
            this.reviseFlag = reviseFlag;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public long getDoorTime() {
            return doorTime;
        }

        public void setDoorTime(long doorTime) {
            this.doorTime = doorTime;
        }

        public int getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(int checkStatus) {
            this.checkStatus = checkStatus;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public int getFinishWiredNum() {
            return finishWiredNum;
        }

        public void setFinishWiredNum(int finishWiredNum) {
            this.finishWiredNum = finishWiredNum;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCustName() {
            return custName;
        }

        public void setCustName(String custName) {
            this.custName = custName;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public int getFinishWirelessNum() {
            return finishWirelessNum;
        }

        public void setFinishWirelessNum(int finishWirelessNum) {
            this.finishWirelessNum = finishWirelessNum;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }
    }
}
