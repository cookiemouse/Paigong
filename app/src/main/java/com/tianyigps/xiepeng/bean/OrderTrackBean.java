package com.tianyigps.xiepeng.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/8.
 */

public class OrderTrackBean {
    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":[{"orderNode":{"createTime":1501813200000,"id":5624,"node":12,"operator":"胡梅","operatorId":204,"operatorType":3,"orderId":608,"orderStatus":3,"reasonFilled":"Tttttt","submitAddress":"","type":2,"updateTime":1501813200000}},{"orderNode":{"createTime":1501579397000,"id":4947,"node":12,"operator":"胡梅","operatorId":204,"operatorType":3,"orderId":608,"orderStatus":3,"reasonFilled":"Ccccc","submitAddress":"","type":2,"updateTime":1501579397000}},{"orderNode":{"createTime":1501579382000,"id":4945,"node":9,"operator":"胡梅","operatorId":204,"operatorType":3,"orderId":608,"orderStatus":3,"type":2,"updateTime":1501579382000}},{"orderNode":{"createTime":1501465041000,"id":4412,"node":8,"operator":"胡梅","operatorId":204,"operatorType":3,"orderId":608,"orderStatus":3,"submitAddress":"江苏省南京市雨花台区花神庙枢纽鼎捷集团东南262米","type":2,"updateTime":1501465041000}},{"engineer":{"account":"","area":2,"bankCardNo":"","census":"","chargeArea":"","city":808,"companyAddress":"","companyCode":"","companyName":"","coverage":"","duties":2,"entryDate":1498665600000,"homeFee":"0","id":204,"idcardNo":"","installTime":"","installWireFee":"0","installWirelessFee":"0","isIndividual":1,"jobNo":"10004","mailbox":"","name":"胡梅","onDuty":1,"openBankName":"","phoneNo":"18256295795","pwd":"12345678","qq":"3004137082","recruitPerson":"","region":1,"relationLoginName":"humei","tearWireFee":"0","tearWirelessFee":"0","type":1},"orderNode":{"beOperator":"胡梅","beOperatorId":204,"beOperatorType":3,"createTime":1501465029000,"id":4406,"node":4,"operator":"胡梅","operatorId":204,"operatorType":2,"orderId":608,"orderStatus":1,"payDoorFee":0,"type":2,"updateTime":1501465029000}},{"orderNode":{"createTime":1501465015000,"id":4398,"node":6,"operator":"admin","operatorId":1,"operatorType":1,"orderId":608,"orderStatus":1,"type":2,"updateTime":1501465015000}},{"orderNode":{"createTime":1501465015000,"id":4400,"node":1,"operator":"admin","operatorId":1,"operatorType":1,"orderId":608,"orderStatus":1,"type":2,"updateTime":1501465015000}}],"msg":"操作成功","success":true}
     * msg : 操作成功
     * obj : [{"orderNode":{"createTime":1501813200000,"id":5624,"node":12,"operator":"胡梅","operatorId":204,"operatorType":3,"orderId":608,"orderStatus":3,"reasonFilled":"Tttttt","submitAddress":"","type":2,"updateTime":1501813200000}},{"orderNode":{"createTime":1501579397000,"id":4947,"node":12,"operator":"胡梅","operatorId":204,"operatorType":3,"orderId":608,"orderStatus":3,"reasonFilled":"Ccccc","submitAddress":"","type":2,"updateTime":1501579397000}},{"orderNode":{"createTime":1501579382000,"id":4945,"node":9,"operator":"胡梅","operatorId":204,"operatorType":3,"orderId":608,"orderStatus":3,"type":2,"updateTime":1501579382000}},{"orderNode":{"createTime":1501465041000,"id":4412,"node":8,"operator":"胡梅","operatorId":204,"operatorType":3,"orderId":608,"orderStatus":3,"submitAddress":"江苏省南京市雨花台区花神庙枢纽鼎捷集团东南262米","type":2,"updateTime":1501465041000}},{"engineer":{"account":"","area":2,"bankCardNo":"","census":"","chargeArea":"","city":808,"companyAddress":"","companyCode":"","companyName":"","coverage":"","duties":2,"entryDate":1498665600000,"homeFee":"0","id":204,"idcardNo":"","installTime":"","installWireFee":"0","installWirelessFee":"0","isIndividual":1,"jobNo":"10004","mailbox":"","name":"胡梅","onDuty":1,"openBankName":"","phoneNo":"18256295795","pwd":"12345678","qq":"3004137082","recruitPerson":"","region":1,"relationLoginName":"humei","tearWireFee":"0","tearWirelessFee":"0","type":1},"orderNode":{"beOperator":"胡梅","beOperatorId":204,"beOperatorType":3,"createTime":1501465029000,"id":4406,"node":4,"operator":"胡梅","operatorId":204,"operatorType":2,"orderId":608,"orderStatus":1,"payDoorFee":0,"type":2,"updateTime":1501465029000}},{"orderNode":{"createTime":1501465015000,"id":4398,"node":6,"operator":"admin","operatorId":1,"operatorType":1,"orderId":608,"orderStatus":1,"type":2,"updateTime":1501465015000}},{"orderNode":{"createTime":1501465015000,"id":4400,"node":1,"operator":"admin","operatorId":1,"operatorType":1,"orderId":608,"orderStatus":1,"type":2,"updateTime":1501465015000}}]
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
         * orderNode : {"createTime":1501813200000,"id":5624,"node":12,"operator":"胡梅","operatorId":204,"operatorType":3,"orderId":608,"orderStatus":3,"reasonFilled":"Tttttt","submitAddress":"","type":2,"updateTime":1501813200000}
         * engineer : {"account":"","area":2,"bankCardNo":"","census":"","chargeArea":"","city":808,"companyAddress":"","companyCode":"","companyName":"","coverage":"","duties":2,"entryDate":1498665600000,"homeFee":"0","id":204,"idcardNo":"","installTime":"","installWireFee":"0","installWirelessFee":"0","isIndividual":1,"jobNo":"10004","mailbox":"","name":"胡梅","onDuty":1,"openBankName":"","phoneNo":"18256295795","pwd":"12345678","qq":"3004137082","recruitPerson":"","region":1,"relationLoginName":"humei","tearWireFee":"0","tearWirelessFee":"0","type":1}
         */

        private OrderNodeBean orderNode;
        private EngineerBean engineer;

        public OrderNodeBean getOrderNode() {
            return orderNode;
        }

        public void setOrderNode(OrderNodeBean orderNode) {
            this.orderNode = orderNode;
        }

        public EngineerBean getEngineer() {
            return engineer;
        }

        public void setEngineer(EngineerBean engineer) {
            this.engineer = engineer;
        }

        public static class OrderNodeBean {
            /**
             * createTime : 1501813200000
             * id : 5624
             * node : 12
             * operator : 胡梅
             * operatorId : 204
             * operatorType : 3
             * orderId : 608
             * orderStatus : 3
             * payDoorFee: 0
             * reasonFilled : Tttttt
             * submitAddress :
             * type : 2
             * updateTime : 1501813200000
             */

            private long createTime;
            private long reviseTime;
            private int reviseStatus;
            private int id;
            private int node;
            private String operator;
            private int operatorId;
            private int operatorType;
            private int orderId;
            private int orderStatus;
            private int payDoorFee;
            private String reasonFilled;
            private String submitAddress;
            private String checkFalseReason;
            private int type;
            private long updateTime;

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

            public int getPayDoorFee() {
                return payDoorFee;
            }

            public int getReviseStatus() {
                return reviseStatus;
            }

            public void setReviseStatus(int reviseStatus) {
                this.reviseStatus = reviseStatus;
            }

            public String getCheckFalseReason() {
                return checkFalseReason;
            }

            public void setCheckFalseReason(String checkFalseReason) {
                this.checkFalseReason = checkFalseReason;
            }

            public void setPayDoorFee(int payDoorFee) {
                this.payDoorFee = payDoorFee;
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

            public long getReviseTime() {
                return reviseTime;
            }

            public void setReviseTime(long reviseTime) {
                this.reviseTime = reviseTime;
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

            public String getReasonFilled() {
                return reasonFilled;
            }

            public void setReasonFilled(String reasonFilled) {
                this.reasonFilled = reasonFilled;
            }

            public String getSubmitAddress() {
                return submitAddress;
            }

            public void setSubmitAddress(String submitAddress) {
                this.submitAddress = submitAddress;
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

        public static class EngineerBean {
            /**
             * account :
             * area : 2
             * bankCardNo :
             * census :
             * chargeArea :
             * city : 808
             * companyAddress :
             * companyCode :
             * companyName :
             * coverage :
             * duties : 2
             * entryDate : 1498665600000
             * homeFee : 0
             * id : 204
             * idcardNo :
             * installTime :
             * installWireFee : 0
             * installWirelessFee : 0
             * isIndividual : 1
             * jobNo : 10004
             * mailbox :
             * name : 胡梅
             * onDuty : 1
             * openBankName :
             * phoneNo : 18256295795
             * pwd : 12345678
             * qq : 3004137082
             * recruitPerson :
             * region : 1
             * relationLoginName : humei
             * tearWireFee : 0
             * tearWirelessFee : 0
             * type : 1
             */

            private String account;
            private int area;
            private String bankCardNo;
            private String census;
            private String chargeArea;
            private int city;
            private String companyAddress;
            private String companyCode;
            private String companyName;
            private String coverage;
            private int duties;
            private long entryDate;
            private String homeFee;
            private int id;
            private String idcardNo;
            private String installTime;
            private String installWireFee;
            private String installWirelessFee;
            private int isIndividual;
            private String jobNo;
            private String mailbox;
            private String name;
            private int onDuty;
            private String openBankName;
            private String phoneNo;
            private String pwd;
            private String qq;
            private String recruitPerson;
            private int region;
            private String relationLoginName;
            private String tearWireFee;
            private String tearWirelessFee;
            private int type;

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public int getArea() {
                return area;
            }

            public void setArea(int area) {
                this.area = area;
            }

            public String getBankCardNo() {
                return bankCardNo;
            }

            public void setBankCardNo(String bankCardNo) {
                this.bankCardNo = bankCardNo;
            }

            public String getCensus() {
                return census;
            }

            public void setCensus(String census) {
                this.census = census;
            }

            public String getChargeArea() {
                return chargeArea;
            }

            public void setChargeArea(String chargeArea) {
                this.chargeArea = chargeArea;
            }

            public int getCity() {
                return city;
            }

            public void setCity(int city) {
                this.city = city;
            }

            public String getCompanyAddress() {
                return companyAddress;
            }

            public void setCompanyAddress(String companyAddress) {
                this.companyAddress = companyAddress;
            }

            public String getCompanyCode() {
                return companyCode;
            }

            public void setCompanyCode(String companyCode) {
                this.companyCode = companyCode;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getCoverage() {
                return coverage;
            }

            public void setCoverage(String coverage) {
                this.coverage = coverage;
            }

            public int getDuties() {
                return duties;
            }

            public void setDuties(int duties) {
                this.duties = duties;
            }

            public long getEntryDate() {
                return entryDate;
            }

            public void setEntryDate(long entryDate) {
                this.entryDate = entryDate;
            }

            public String getHomeFee() {
                return homeFee;
            }

            public void setHomeFee(String homeFee) {
                this.homeFee = homeFee;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIdcardNo() {
                return idcardNo;
            }

            public void setIdcardNo(String idcardNo) {
                this.idcardNo = idcardNo;
            }

            public String getInstallTime() {
                return installTime;
            }

            public void setInstallTime(String installTime) {
                this.installTime = installTime;
            }

            public String getInstallWireFee() {
                return installWireFee;
            }

            public void setInstallWireFee(String installWireFee) {
                this.installWireFee = installWireFee;
            }

            public String getInstallWirelessFee() {
                return installWirelessFee;
            }

            public void setInstallWirelessFee(String installWirelessFee) {
                this.installWirelessFee = installWirelessFee;
            }

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

            public String getMailbox() {
                return mailbox;
            }

            public void setMailbox(String mailbox) {
                this.mailbox = mailbox;
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

            public String getOpenBankName() {
                return openBankName;
            }

            public void setOpenBankName(String openBankName) {
                this.openBankName = openBankName;
            }

            public String getPhoneNo() {
                return phoneNo;
            }

            public void setPhoneNo(String phoneNo) {
                this.phoneNo = phoneNo;
            }

            public String getPwd() {
                return pwd;
            }

            public void setPwd(String pwd) {
                this.pwd = pwd;
            }

            public String getQq() {
                return qq;
            }

            public void setQq(String qq) {
                this.qq = qq;
            }

            public String getRecruitPerson() {
                return recruitPerson;
            }

            public void setRecruitPerson(String recruitPerson) {
                this.recruitPerson = recruitPerson;
            }

            public int getRegion() {
                return region;
            }

            public void setRegion(int region) {
                this.region = region;
            }

            public String getRelationLoginName() {
                return relationLoginName;
            }

            public void setRelationLoginName(String relationLoginName) {
                this.relationLoginName = relationLoginName;
            }

            public String getTearWireFee() {
                return tearWireFee;
            }

            public void setTearWireFee(String tearWireFee) {
                this.tearWireFee = tearWireFee;
            }

            public String getTearWirelessFee() {
                return tearWirelessFee;
            }

            public void setTearWirelessFee(String tearWirelessFee) {
                this.tearWirelessFee = tearWirelessFee;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
