package com.tianyigps.dispatch2.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/9.
 */

public class PendedBean {
    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":[{"contactPhone":"15987465892","detail":"","orderType":3,"orderStatus":3,"eName":"谢鹏","city":"苏州市","phoneNo":"17092562223","reviseFlag":0,"orderNo":"TY20170809104002152","doorTime":1502419241000,"contactName":"周鱼","province":"江苏省","jobNo":"10005","custName":"天易根客户1","district":"吴中区","orderId":895},{"contactPhone":"79864832251","detail":"","orderType":1,"orderStatus":3,"eName":"xpxiaohao","city":"苏州市","phoneNo":"15195972319","reviseFlag":0,"orderNo":"TY20170808154055497","doorTime":1502264501000,"contactName":"旺达","province":"江苏省","jobNo":"10067","custName":"天易根客户1","district":"常熟市","orderId":862},{"contactPhone":"13956897412","detail":"","orderType":3,"orderStatus":3,"eName":"谢鹏","city":"苏州市","phoneNo":"17092562223","reviseFlag":0,"orderNo":"TY20170809103814005","doorTime":1502332705000,"contactName":"朱三","province":"江苏省","jobNo":"10005","custName":"天易根客户1","district":"相城区","orderId":894},{"contactPhone":"15874956895","detail":"","orderType":1,"orderStatus":3,"eName":"谢鹏","city":"苏州市","phoneNo":"17092562223","reviseFlag":0,"orderNo":"TY20170808153158987","doorTime":1502350356000,"contactName":"刘三","province":"江苏省","jobNo":"10005","custName":"天易根客户1","district":"姑苏区","orderId":858}],"msg":"操作成功","success":true}
     * msg : 操作成功
     * obj : [{"contactPhone":"15987465892","detail":"","orderType":3,"orderStatus":3,"eName":"谢鹏","city":"苏州市","phoneNo":"17092562223","reviseFlag":0,"orderNo":"TY20170809104002152","doorTime":1502419241000,"contactName":"周鱼","province":"江苏省","jobNo":"10005","custName":"天易根客户1","district":"吴中区","orderId":895},{"contactPhone":"79864832251","detail":"","orderType":1,"orderStatus":3,"eName":"xpxiaohao","city":"苏州市","phoneNo":"15195972319","reviseFlag":0,"orderNo":"TY20170808154055497","doorTime":1502264501000,"contactName":"旺达","province":"江苏省","jobNo":"10067","custName":"天易根客户1","district":"常熟市","orderId":862},{"contactPhone":"13956897412","detail":"","orderType":3,"orderStatus":3,"eName":"谢鹏","city":"苏州市","phoneNo":"17092562223","reviseFlag":0,"orderNo":"TY20170809103814005","doorTime":1502332705000,"contactName":"朱三","province":"江苏省","jobNo":"10005","custName":"天易根客户1","district":"相城区","orderId":894},{"contactPhone":"15874956895","detail":"","orderType":1,"orderStatus":3,"eName":"谢鹏","city":"苏州市","phoneNo":"17092562223","reviseFlag":0,"orderNo":"TY20170808153158987","doorTime":1502350356000,"contactName":"刘三","province":"江苏省","jobNo":"10005","custName":"天易根客户1","district":"姑苏区","orderId":858}]
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
         * contactPhone : 15987465892
         * detail :
         * orderType : 3
         * orderStatus : 3
         * eName : 谢鹏
         * city : 苏州市
         * phoneNo : 17092562223
         * reviseFlag : 0
         * orderNo : TY20170809104002152
         * doorTime : 1502419241000
         * contactName : 周鱼
         * province : 江苏省
         * jobNo : 10005
         * custName : 天易根客户1
         * district : 吴中区
         * orderId : 895
         */

        private String contactPhone;
        private String detail;
        private int orderType;
        private int orderStatus;
        private String eName;
        private String city;
        private String phoneNo;
        private int reviseFlag;
        private String orderNo;
        private long doorTime;
        private String contactName;
        private String province;
        private String jobNo;
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

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getEName() {
            return eName;
        }

        public void setEName(String eName) {
            this.eName = eName;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
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

        public String getJobNo() {
            return jobNo;
        }

        public void setJobNo(String jobNo) {
            this.jobNo = jobNo;
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
    }
}
