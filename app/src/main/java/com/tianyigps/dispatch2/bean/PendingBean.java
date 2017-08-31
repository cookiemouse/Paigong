package com.tianyigps.dispatch2.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/8.
 */

public class PendingBean {
    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":[{"contactPhone":"79864832251","detail":"","wirelessNum":4,"removeWiredNum":0,"orderType":1,"wiredNum":0,"orderStatus":1,"removeWirelessNum":0,"city":"苏州市","reviseFlag":0,"orderNo":"TY20170808154055497","doorTime":1502264501000,"contactName":"旺达","province":"江苏省","custName":"天易根客户1","district":"常熟市","orderId":862}],"msg":"操作成功","success":true}
     * msg : 操作成功
     * obj : [{"contactPhone":"79864832251","detail":"","wirelessNum":4,"removeWiredNum":0,"orderType":1,"wiredNum":0,"orderStatus":1,"removeWirelessNum":0,"city":"苏州市","reviseFlag":0,"orderNo":"TY20170808154055497","doorTime":1502264501000,"contactName":"旺达","province":"江苏省","custName":"天易根客户1","district":"常熟市","orderId":862}]
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
         * contactPhone : 79864832251
         * detail :
         * wirelessNum : 4
         * removeWiredNum : 0
         * orderType : 1
         * wiredNum : 0
         * orderStatus : 1
         * removeWirelessNum : 0
         * city : 苏州市
         * reviseFlag : 0
         * orderNo : TY20170808154055497
         * doorTime : 1502264501000
         * contactName : 旺达
         * province : 江苏省
         * custName : 天易根客户1
         * district : 常熟市
         * orderId : 862
         */

        private String contactPhone;
        private String detail;
        private int wirelessNum;
        private int removeWiredNum;
        private int orderType;
        private int node;
        private int wiredNum;
        private int orderStatus;
        private int removeWirelessNum;
        private String city;
        private int reviseFlag;
        private String orderNo;
        private long doorTime;
        private long reviseTime;
        private String contactName;
        private String eName;
        private String province;
        private String custName;
        private String district;
        private String reasonFilled;
        private String reasonChoosed;
        private String phoneNo;
        private String jobNo;
        private int orderId;

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

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
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

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getNode() {
            return node;
        }

        public void setNode(int node) {
            this.node = node;
        }

        public long getReviseTime() {
            return reviseTime;
        }

        public void setReviseTime(long reviseTime) {
            this.reviseTime = reviseTime;
        }

        public String getReasonFilled() {
            return reasonFilled;
        }

        public void setReasonFilled(String reasonFilled) {
            this.reasonFilled = reasonFilled;
        }

        public String getReasonChoosed() {
            return reasonChoosed;
        }

        public void setReasonChoosed(String reasonChoosed) {
            this.reasonChoosed = reasonChoosed;
        }

        public String geteName() {
            return eName;
        }

        public void seteName(String eName) {
            this.eName = eName;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getJobNo() {
            return jobNo;
        }

        public void setJobNo(String jobNo) {
            this.jobNo = jobNo;
        }
    }
}
