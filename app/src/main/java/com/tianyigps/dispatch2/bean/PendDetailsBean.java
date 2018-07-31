package com.tianyigps.dispatch2.bean;

import com.tianyigps.dispatch2.utils.RegularU;

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
        private String installDemand;
        private String partSubReason;
        private int removeWiredNum;
        private int removeWirelessNum;
        private int wiredNum;
        private int reviseFlag;
        private int wirelessNum;
        private int finishWiredNum;
        private int finishWirelessNum;
        private int remoFinWiredNum;
        private int remoFinWirelessNum;
        private List<OrderCarListBean> orderCarList;

        public String getPartSubReason() {
            return partSubReason;
        }

        public void setPartSubReason(String partSubReason) {
            this.partSubReason = partSubReason;
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

        public int getRemoFinWiredNum() {
            return remoFinWiredNum;
        }

        public void setRemoFinWiredNum(int remoFinWiredNum) {
            this.remoFinWiredNum = remoFinWiredNum;
        }

        public int getRemoFinWirelessNum() {
            return remoFinWirelessNum;
        }

        public void setRemoFinWirelessNum(int remoFinWirelessNum) {
            this.remoFinWirelessNum = remoFinWirelessNum;
        }

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

        public int getReviseFlag() {
            return reviseFlag;
        }

        public void setReviseFlag(int reviseFlag) {
            this.reviseFlag = reviseFlag;
        }

        public String getInstallDemand() {
            return installDemand;
        }

        public void setInstallDemand(String installDemand) {
            this.installDemand = installDemand;
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
             * beOperator : 测试
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
            private String reasonChoosed;
            private String reasonFilled;
            private String checkFalseReason;
            private int operatorId;
            private int operatorType;
            private int orderId;
            private int orderStatus;
            private int reviseStatus;
            private int type;
            private long updateTime;
            private long reviseTime;

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

            public String getReasonChoosed() {
                return reasonChoosed;
            }

            public void setReasonChoosed(String reasonChoosed) {
                this.reasonChoosed = reasonChoosed;
            }

            public String getReasonFilled() {
                return reasonFilled;
            }

            public void setReasonFilled(String reasonFilled) {
                this.reasonFilled = reasonFilled;
            }

            public String getCheckFalseReason() {
                return checkFalseReason;
            }

            public void setCheckFalseReason(String checkFalseReason) {
                this.checkFalseReason = checkFalseReason;
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

            public long getReviseTime() {
                return reviseTime;
            }

            public void setReviseTime(long reviseTime) {
                this.reviseTime = reviseTime;
            }

            public int getReviseStatus() {
                return reviseStatus;
            }

            public void setReviseStatus(int reviseStatus) {
                this.reviseStatus = reviseStatus;
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
            private List<CarTerminalListBean> carTerminalList;

            public String getCarBrand() {
                if (RegularU.isEmpty(carBrand)){
                    return "";
                }
                return carBrand;
            }

            public void setCarBrand(String carBrand) {
                this.carBrand = carBrand;
            }

            public String getCarNo() {
                if (RegularU.isEmpty(carNo)){
                    return "";
                }
                return carNo;
            }

            public void setCarNo(String carNo) {
                this.carNo = carNo;
            }

            public String getCarVin() {
                if (RegularU.isEmpty(carVin)){
                    return "";
                }
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

            public List<CarTerminalListBean> getCarTerminalList() {
                return carTerminalList;
            }

            public void setCarTerminalList(List<CarTerminalListBean> carTerminalList) {
                this.carTerminalList = carTerminalList;
            }

            public static class CarTerminalListBean {
                /**
                 * createTime : 1532998926000
                 * id : 1112843
                 * installLocation : 他呢
                 * installLocationPic : TY20180212134919032/4c0fc519af7e4c769af64a8b0e4fd79b.jpg
                 * malDesc : 334211
                 * orderCarId : 113950
                 * orderId : 32683
                 * repairJudge : 1
                 * repairSuggest : aaaa
                 * repaireStatus : 0
                 * sim : 17701010112
                 * tNo : 80088008811
                 * terminalName : 被如何公路软环境法国人发...013488便携式1
                 * terminalType : 2
                 */

                private long createTime;
                private int id;
                private String installLocation;
                private String installLocationPic;
                private String malDesc;
                private int orderCarId;
                private int orderId;
                private int repairJudge;
                private String repairSuggest;
                private int repaireStatus;
                private String sim;
                private String tNo;
                private String terminalName;
                private int terminalType;

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

                public String getInstallLocation() {
                    return installLocation;
                }

                public void setInstallLocation(String installLocation) {
                    this.installLocation = installLocation;
                }

                public String getInstallLocationPic() {
                    return installLocationPic;
                }

                public void setInstallLocationPic(String installLocationPic) {
                    this.installLocationPic = installLocationPic;
                }

                public String getMalDesc() {
                    return malDesc;
                }

                public void setMalDesc(String malDesc) {
                    this.malDesc = malDesc;
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

                public int getRepairJudge() {
                    return repairJudge;
                }

                public void setRepairJudge(int repairJudge) {
                    this.repairJudge = repairJudge;
                }

                public String getRepairSuggest() {
                    return repairSuggest;
                }

                public void setRepairSuggest(String repairSuggest) {
                    this.repairSuggest = repairSuggest;
                }

                public int getRepaireStatus() {
                    return repaireStatus;
                }

                public void setRepaireStatus(int repaireStatus) {
                    this.repaireStatus = repaireStatus;
                }

                public String getSim() {
                    return sim;
                }

                public void setSim(String sim) {
                    this.sim = sim;
                }

                public String getTNo() {
                    return tNo;
                }

                public void setTNo(String tNo) {
                    this.tNo = tNo;
                }

                public String getTerminalName() {
                    return terminalName;
                }

                public void setTerminalName(String terminalName) {
                    this.terminalName = terminalName;
                }

                public int getTerminalType() {
                    return terminalType;
                }

                public void setTerminalType(int terminalType) {
                    this.terminalType = terminalType;
                }
            }
        }
    }
}
