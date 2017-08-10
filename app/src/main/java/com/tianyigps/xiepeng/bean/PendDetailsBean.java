package com.tianyigps.xiepeng.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/10.
 */

public class PendDetailsBean {
    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":{"address":"江苏省苏州市张家港市","checkStatus":0,"contactName":"二龙山-鲁达","contactPhone":"19556325478","createTime":1502269683000,"doorTime":1502356093000,"engineer":{"isIndividual":1,"jobNo":"null","name":"null","onDuty":1,"phoneNo":"null"},"isEdit":0,"orderCarList":[{"carBrand":"BMW320","carNo":"苏A123555","carVin":"LSVAM4187C2184847","createTime":1502269683000,"id":1233,"orderId":940,"ownerCard":"","ownerName":"123","removeFlag":0,"wiredAnnual":0,"wiredNum":0,"wirelessAnnual":5,"wirelessNum":3}],"orderNode":{"beOperator":"谢鹏","beOperatorId":205,"beOperatorType":2,"createTime":1502269683000,"id":6771,"node":1,"operator":"admin","operatorId":1,"operatorType":1,"orderId":940,"orderStatus":1,"type":2,"updateTime":1502269683000},"orderStatus":1,"orderType":1,"remark":"【天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘】","removeWiredNum":0,"removeWirelessNum":0,"wiredNum":0,"wirelessNum":3},"msg":"操作成功","success":true}
     * msg : 操作成功
     * obj : {"address":"江苏省苏州市张家港市","checkStatus":0,"contactName":"二龙山-鲁达","contactPhone":"19556325478","createTime":1502269683000,"doorTime":1502356093000,"engineer":{"isIndividual":1,"jobNo":"null","name":"null","onDuty":1,"phoneNo":"null"},"isEdit":0,"orderCarList":[{"carBrand":"BMW320","carNo":"苏A123555","carVin":"LSVAM4187C2184847","createTime":1502269683000,"id":1233,"orderId":940,"ownerCard":"","ownerName":"123","removeFlag":0,"wiredAnnual":0,"wiredNum":0,"wirelessAnnual":5,"wirelessNum":3}],"orderNode":{"beOperator":"谢鹏","beOperatorId":205,"beOperatorType":2,"createTime":1502269683000,"id":6771,"node":1,"operator":"admin","operatorId":1,"operatorType":1,"orderId":940,"orderStatus":1,"type":2,"updateTime":1502269683000},"orderStatus":1,"orderType":1,"remark":"【天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘】","removeWiredNum":0,"removeWirelessNum":0,"wiredNum":0,"wirelessNum":3}
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
         * address : 江苏省苏州市张家港市
         * checkStatus : 0
         * contactName : 二龙山-鲁达
         * contactPhone : 19556325478
         * createTime : 1502269683000
         * doorTime : 1502356093000
         * engineer : {"isIndividual":1,"jobNo":"null","name":"null","onDuty":1,"phoneNo":"null"}
         * isEdit : 0
         * orderCarList : [{"carBrand":"BMW320","carNo":"苏A123555","carVin":"LSVAM4187C2184847","createTime":1502269683000,"id":1233,"orderId":940,"ownerCard":"","ownerName":"123","removeFlag":0,"wiredAnnual":0,"wiredNum":0,"wirelessAnnual":5,"wirelessNum":3}]
         * orderNode : {"beOperator":"谢鹏","beOperatorId":205,"beOperatorType":2,"createTime":1502269683000,"id":6771,"node":1,"operator":"admin","operatorId":1,"operatorType":1,"orderId":940,"orderStatus":1,"type":2,"updateTime":1502269683000}
         * orderStatus : 1
         * orderType : 1
         * remark : 【天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘天塘】
         * removeWiredNum : 0
         * removeWirelessNum : 0
         * wiredNum : 0
         * wirelessNum : 3
         */

        private String address;
        private int checkStatus;
        private String contactName;
        private String contactPhone;
        private long createTime;
        private long doorTime;
        private EngineerBean engineer;
        private int isEdit;
        private OrderNodeBean orderNode;
        private int orderStatus;
        private int orderType;
        private String remark;
        private int removeWiredNum;
        private int removeWirelessNum;
        private int wiredNum;
        private int wirelessNum;
        private List<OrderCarListBean> orderCarList;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getDoorTime() {
            return doorTime;
        }

        public void setDoorTime(long doorTime) {
            this.doorTime = doorTime;
        }

        public EngineerBean getEngineer() {
            return engineer;
        }

        public void setEngineer(EngineerBean engineer) {
            this.engineer = engineer;
        }

        public int getIsEdit() {
            return isEdit;
        }

        public void setIsEdit(int isEdit) {
            this.isEdit = isEdit;
        }

        public OrderNodeBean getOrderNode() {
            return orderNode;
        }

        public void setOrderNode(OrderNodeBean orderNode) {
            this.orderNode = orderNode;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getRemoveWiredNum() {
            return removeWiredNum;
        }

        public void setRemoveWiredNum(int removeWiredNum) {
            this.removeWiredNum = removeWiredNum;
        }

        public int getRemoveWirelessNum() {
            return removeWirelessNum;
        }

        public void setRemoveWirelessNum(int removeWirelessNum) {
            this.removeWirelessNum = removeWirelessNum;
        }

        public int getWiredNum() {
            return wiredNum;
        }

        public void setWiredNum(int wiredNum) {
            this.wiredNum = wiredNum;
        }

        public int getWirelessNum() {
            return wirelessNum;
        }

        public void setWirelessNum(int wirelessNum) {
            this.wirelessNum = wirelessNum;
        }

        public List<OrderCarListBean> getOrderCarList() {
            return orderCarList;
        }

        public void setOrderCarList(List<OrderCarListBean> orderCarList) {
            this.orderCarList = orderCarList;
        }

        public static class EngineerBean {
            /**
             * isIndividual : 1
             * jobNo : null
             * name : null
             * onDuty : 1
             * phoneNo : null
             */

            private int isIndividual;
            private String jobNo;
            private String name;
            private int onDuty;
            private String phoneNo;

            public int getIsIndividual() {
                return isIndividual;
            }

            public void setIsIndividual(int isIndividual) {
                this.isIndividual = isIndividual;
            }

            public String getJobNo() {
                return jobNo;
            }

            public void setJobNo(String jobNo) {
                this.jobNo = jobNo;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOnDuty() {
                return onDuty;
            }

            public void setOnDuty(int onDuty) {
                this.onDuty = onDuty;
            }

            public String getPhoneNo() {
                return phoneNo;
            }

            public void setPhoneNo(String phoneNo) {
                this.phoneNo = phoneNo;
            }
        }

        public static class OrderNodeBean {
            /**
             * beOperator : 谢鹏
             * beOperatorId : 205
             * beOperatorType : 2
             * createTime : 1502269683000
             * id : 6771
             * node : 1
             * operator : admin
             * operatorId : 1
             * operatorType : 1
             * orderId : 940
             * orderStatus : 1
             * type : 2
             * updateTime : 1502269683000
             */

            private String beOperator;
            private int beOperatorId;
            private int beOperatorType;
            private long createTime;
            private int id;
            private int node;
            private String operator;
            private int operatorId;
            private int operatorType;
            private int orderId;
            private int orderStatus;
            private int type;
            private long updateTime;

            public String getBeOperator() {
                return beOperator;
            }

            public void setBeOperator(String beOperator) {
                this.beOperator = beOperator;
            }

            public int getBeOperatorId() {
                return beOperatorId;
            }

            public void setBeOperatorId(int beOperatorId) {
                this.beOperatorId = beOperatorId;
            }

            public int getBeOperatorType() {
                return beOperatorType;
            }

            public void setBeOperatorType(int beOperatorType) {
                this.beOperatorType = beOperatorType;
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

            public int getNode() {
                return node;
            }

            public void setNode(int node) {
                this.node = node;
            }

            public String getOperator() {
                return operator;
            }

            public void setOperator(String operator) {
                this.operator = operator;
            }

            public int getOperatorId() {
                return operatorId;
            }

            public void setOperatorId(int operatorId) {
                this.operatorId = operatorId;
            }

            public int getOperatorType() {
                return operatorType;
            }

            public void setOperatorType(int operatorType) {
                this.operatorType = operatorType;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public int getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(int orderStatus) {
                this.orderStatus = orderStatus;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }
        }

        public static class OrderCarListBean {
            /**
             * carBrand : BMW320
             * carNo : 苏A123555
             * carVin : LSVAM4187C2184847
             * createTime : 1502269683000
             * id : 1233
             * orderId : 940
             * ownerCard :
             * ownerName : 123
             * removeFlag : 0
             * wiredAnnual : 0
             * wiredNum : 0
             * wirelessAnnual : 5
             * wirelessNum : 3
             */

            private String carBrand;
            private String carNo;
            private String carVin;
            private long createTime;
            private int id;
            private int orderId;
            private String ownerCard;
            private String ownerName;
            private int removeFlag;
            private int wiredAnnual;
            private int wiredNum;
            private int wirelessAnnual;
            private int wirelessNum;

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
        }
    }
}
