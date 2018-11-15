package com.tianyigps.dispatch2.bean;

import java.util.List;

/**
 * Created by djc on 2017/7/17.
 */

public class WorkerOrderBean {
    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":[{"contactPhone":"12312312312","detail":"","wirelessNum":1,"removeWiredNum":0,"orderType":1,"wiredNum":1,"orderStatus":3,"removeWirelessNum":0,"city":"苏州市","reviseFlag":0,"orderNo":"TY20170717111427818","doorTime":1500606837000,"contactName":"asasas","province":"江苏省","custName":"天易根客户1","district":"吴中区","orderId":412}],"msg":"操作成功","success":true}
     * msg : 操作成功
     * obj : [{"contactPhone":"12312312312","detail":"","wirelessNum":1,"removeWiredNum":0,"orderType":1,"wiredNum":1,"orderStatus":3,"removeWirelessNum":0,"city":"苏州市","reviseFlag":0,"orderNo":"TY20170717111427818","doorTime":1500606837000,"contactName":"asasas","province":"江苏省","custName":"天易根客户1","district":"吴中区","orderId":412}]
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
         * contactPhone : 12312312312
         * detail :
         * wirelessNum : 1
         * removeWiredNum : 0
         * orderType : 1
         * wiredNum : 1
         * orderStatus : 3
         * removeWirelessNum : 0
         * city : 苏州市
         * reviseFlag : 0
         * orderNo : TY20170717111427818
         * doorTime : 1500606837000
         * contactName : asasas
         * province : 江苏省
         * custName : 天易根客户1
         * district : 吴中区
         * orderId : 412
         */

        private String contactPhone;
        private String detail;
        private int wirelessNum;
        private int removeWiredNum;
        private int orderType;
        private int is_modified;
        private int wiredNum;
        private int orderStatus;
        private int removeWirelessNum;
        private String city;
        private int reviseFlag;
        private String orderNo;
        private long doorTime;
        private String contactName;
        private String province;
        private String custName;
        private String district;
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

        public int getIs_modified() {
            return is_modified;
        }

        public void setIs_modified(int is_modified) {
            this.is_modified = is_modified;
        }
    }
}
