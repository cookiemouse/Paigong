package com.tianyigps.xiepeng.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/9.
 */

public class ChoiceWorkerBean {
    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":[{"account":"","area":3,"bankCardNo":"","census":"","chargeArea":"","city":846,"companyAddress":"","companyCode":"","companyName":"","coverage":"","duties":1,"homeFee":"0","id":284,"idcardNo":"","installTime":"","installWireFee":"0","installWirelessFee":"0","isIndividual":1,"jobNo":"10067","mailbox":"","name":"xpxiaohao","onDuty":1,"openBankName":"","phoneNo":"15195972319","pwd":"12345678","qq":"312312312312","recruitPerson":"","region":1,"relationLoginName":"","tearWireFee":"0","tearWirelessFee":"0","type":1}],"msg":"操作成功","success":true}
     * msg : 操作成功
     * obj : [{"account":"","area":3,"bankCardNo":"","census":"","chargeArea":"","city":846,"companyAddress":"","companyCode":"","companyName":"","coverage":"","duties":1,"homeFee":"0","id":284,"idcardNo":"","installTime":"","installWireFee":"0","installWirelessFee":"0","isIndividual":1,"jobNo":"10067","mailbox":"","name":"xpxiaohao","onDuty":1,"openBankName":"","phoneNo":"15195972319","pwd":"12345678","qq":"312312312312","recruitPerson":"","region":1,"relationLoginName":"","tearWireFee":"0","tearWirelessFee":"0","type":1}]
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
         * account :
         * area : 3
         * bankCardNo :
         * census :
         * chargeArea :
         * city : 846
         * companyAddress :
         * companyCode :
         * companyName :
         * coverage :
         * duties : 1
         * homeFee : 0
         * id : 284
         * idcardNo :
         * installTime :
         * installWireFee : 0
         * installWirelessFee : 0
         * isIndividual : 1
         * jobNo : 10067
         * mailbox :
         * name : xpxiaohao
         * onDuty : 1
         * openBankName :
         * phoneNo : 15195972319
         * pwd : 12345678
         * qq : 312312312312
         * recruitPerson :
         * region : 1
         * relationLoginName :
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
