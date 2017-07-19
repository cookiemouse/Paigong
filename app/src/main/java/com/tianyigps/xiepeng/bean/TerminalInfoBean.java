package com.tianyigps.xiepeng.bean;

/**
 * Created by djc on 2017/7/18.
 */

public class TerminalInfoBean {

    /**
     * errCode :
     * jsonStr : {"time":1500428074513,"errCode":"","obj":{"install_location":"不知道","max_speed":10,"model":"1","offline_time":1491806822000,"remark":"","model_type_detail":"1","car_vin":"LNBSCBAF5DR306551","car_brand":"拆装","install_time":1500279921000,"sync_status":0,"sale_time":1493913600000,"car_type":"","online_time":1491806818000,"id":"4028818b531d032101531db38c1e0233","redisObj":{"locate_time":"2017/05/05 18:00:01","distance":334,"gps_time":"2017/05/05 18:00:01","speed":50,"direction":90,"imei":"123456789012340","locate_type":1,"current_time":"2017/05/05 18:00:01","longitude":118.775337,"latitude":31.971091,"terminal_type":"TY01","expire_time":"2018/04/10 14:47:01","acc_status":"01","longitudeF":118.78700240188502,"latitudeF":31.97525188429618},"end_date":1518710400000,"name":"苏A306551拆装有线","contact_name":"拆装","car_no":"苏A306551","icon":2,"idCard":"","imei":"123456789012340","group_id":678,"repayment_date":1462464000000,"contact_phone":"","alarm_speed":0,"update_time":1500279921000,"create_time":1460102784000,"infoWindowStatus":"离线(74天15小时34分钟33秒)","install_person":"10004 胡梅","sim":"1964822809612","start_date":1464105600000,"cus_remark":"","customer_id":667},"msg":"操作成功","success":true}
     * msg : 操作成功
     * obj : {"install_location":"不知道","max_speed":10,"model":"1","offline_time":1491806822000,"remark":"","model_type_detail":"1","car_vin":"LNBSCBAF5DR306551","car_brand":"拆装","install_time":1500279921000,"sync_status":0,"sale_time":1493913600000,"car_type":"","online_time":1491806818000,"id":"4028818b531d032101531db38c1e0233","redisObj":{"locate_time":"2017/05/05 18:00:01","distance":334,"gps_time":"2017/05/05 18:00:01","speed":50,"direction":90,"imei":"123456789012340","locate_type":1,"current_time":"2017/05/05 18:00:01","longitude":118.775337,"latitude":31.971091,"terminal_type":"TY01","expire_time":"2018/04/10 14:47:01","acc_status":"01","longitudeF":118.78700240188502,"latitudeF":31.97525188429618},"end_date":1518710400000,"name":"苏A306551拆装有线","contact_name":"拆装","car_no":"苏A306551","icon":2,"idCard":"","imei":"123456789012340","group_id":678,"repayment_date":1462464000000,"contact_phone":"","alarm_speed":0,"update_time":1500279921000,"create_time":1460102784000,"infoWindowStatus":"离线(74天15小时34分钟33秒)","install_person":"10004 胡梅","sim":"1964822809612","start_date":1464105600000,"cus_remark":"","customer_id":667}
     * success : true
     * time : 1500428074513
     */

    private String errCode;
    private String jsonStr;
    private String msg;
    private ObjBean obj;
    private boolean success;
    private long time;

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static class ObjBean {
        /**
         * install_location : 不知道
         * max_speed : 10
         * model : 1
         * offline_time : 1491806822000
         * remark :
         * model_type_detail : 1
         * car_vin : LNBSCBAF5DR306551
         * car_brand : 拆装
         * install_time : 1500279921000
         * sync_status : 0
         * sale_time : 1493913600000
         * car_type :
         * online_time : 1491806818000
         * id : 4028818b531d032101531db38c1e0233
         * redisObj : {"locate_time":"2017/05/05 18:00:01","distance":334,"gps_time":"2017/05/05 18:00:01","speed":50,"direction":90,"imei":"123456789012340","locate_type":1,"current_time":"2017/05/05 18:00:01","longitude":118.775337,"latitude":31.971091,"terminal_type":"TY01","expire_time":"2018/04/10 14:47:01","acc_status":"01","longitudeF":118.78700240188502,"latitudeF":31.97525188429618}
         * end_date : 1518710400000
         * name : 苏A306551拆装有线
         * contact_name : 拆装
         * car_no : 苏A306551
         * icon : 2
         * idCard :
         * imei : 123456789012340
         * group_id : 678
         * repayment_date : 1462464000000
         * contact_phone :
         * alarm_speed : 0
         * update_time : 1500279921000
         * create_time : 1460102784000
         * infoWindowStatus : 离线(74天15小时34分钟33秒)
         * install_person : 10004 胡梅
         * sim : 1964822809612
         * start_date : 1464105600000
         * cus_remark :
         * customer_id : 667
         */

        private String install_location;
        private int max_speed;
        private String model;
        private long offline_time;
        private String remark;
        private String model_type_detail;
        private String car_vin;
        private String car_brand;
        private long install_time;
        private int sync_status;
        private long sale_time;
        private String car_type;
        private long online_time;
        private String id;
        private RedisObjBean redisObj;
        private long end_date;
        private String name;
        private String contact_name;
        private String car_no;
        private int icon;
        private String idCard;
        private String imei;
        private int group_id;
        private long repayment_date;
        private String contact_phone;
        private int alarm_speed;
        private long update_time;
        private long create_time;
        private String infoWindowStatus;
        private String install_person;
        private String sim;
        private long start_date;
        private String cus_remark;
        private int customer_id;

        public String getInstall_location() {
            return install_location;
        }

        public void setInstall_location(String install_location) {
            this.install_location = install_location;
        }

        public int getMax_speed() {
            return max_speed;
        }

        public void setMax_speed(int max_speed) {
            this.max_speed = max_speed;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public long getOffline_time() {
            return offline_time;
        }

        public void setOffline_time(long offline_time) {
            this.offline_time = offline_time;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getModel_type_detail() {
            return model_type_detail;
        }

        public void setModel_type_detail(String model_type_detail) {
            this.model_type_detail = model_type_detail;
        }

        public String getCar_vin() {
            return car_vin;
        }

        public void setCar_vin(String car_vin) {
            this.car_vin = car_vin;
        }

        public String getCar_brand() {
            return car_brand;
        }

        public void setCar_brand(String car_brand) {
            this.car_brand = car_brand;
        }

        public long getInstall_time() {
            return install_time;
        }

        public void setInstall_time(long install_time) {
            this.install_time = install_time;
        }

        public int getSync_status() {
            return sync_status;
        }

        public void setSync_status(int sync_status) {
            this.sync_status = sync_status;
        }

        public long getSale_time() {
            return sale_time;
        }

        public void setSale_time(long sale_time) {
            this.sale_time = sale_time;
        }

        public String getCar_type() {
            return car_type;
        }

        public void setCar_type(String car_type) {
            this.car_type = car_type;
        }

        public long getOnline_time() {
            return online_time;
        }

        public void setOnline_time(long online_time) {
            this.online_time = online_time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public RedisObjBean getRedisObj() {
            return redisObj;
        }

        public void setRedisObj(RedisObjBean redisObj) {
            this.redisObj = redisObj;
        }

        public long getEnd_date() {
            return end_date;
        }

        public void setEnd_date(long end_date) {
            this.end_date = end_date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContact_name() {
            return contact_name;
        }

        public void setContact_name(String contact_name) {
            this.contact_name = contact_name;
        }

        public String getCar_no() {
            return car_no;
        }

        public void setCar_no(String car_no) {
            this.car_no = car_no;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public long getRepayment_date() {
            return repayment_date;
        }

        public void setRepayment_date(long repayment_date) {
            this.repayment_date = repayment_date;
        }

        public String getContact_phone() {
            return contact_phone;
        }

        public void setContact_phone(String contact_phone) {
            this.contact_phone = contact_phone;
        }

        public int getAlarm_speed() {
            return alarm_speed;
        }

        public void setAlarm_speed(int alarm_speed) {
            this.alarm_speed = alarm_speed;
        }

        public long getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(long update_time) {
            this.update_time = update_time;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public String getInfoWindowStatus() {
            return infoWindowStatus;
        }

        public void setInfoWindowStatus(String infoWindowStatus) {
            this.infoWindowStatus = infoWindowStatus;
        }

        public String getInstall_person() {
            return install_person;
        }

        public void setInstall_person(String install_person) {
            this.install_person = install_person;
        }

        public String getSim() {
            return sim;
        }

        public void setSim(String sim) {
            this.sim = sim;
        }

        public long getStart_date() {
            return start_date;
        }

        public void setStart_date(long start_date) {
            this.start_date = start_date;
        }

        public String getCus_remark() {
            return cus_remark;
        }

        public void setCus_remark(String cus_remark) {
            this.cus_remark = cus_remark;
        }

        public int getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(int customer_id) {
            this.customer_id = customer_id;
        }

        public static class RedisObjBean {
            /**
             * locate_time : 2017/05/05 18:00:01
             * distance : 334
             * gps_time : 2017/05/05 18:00:01
             * speed : 50
             * direction : 90
             * imei : 123456789012340
             * locate_type : 1
             * current_time : 2017/05/05 18:00:01
             * longitude : 118.775337
             * latitude : 31.971091
             * terminal_type : TY01
             * expire_time : 2018/04/10 14:47:01
             * acc_status : 01
             * longitudeF : 118.78700240188502
             * latitudeF : 31.97525188429618
             */

            private String locate_time;
            private int distance;
            private String gps_time;
            private int speed;
            private int direction;
            private String imei;
            private int locate_type;
            private String current_time;
            private double longitude;
            private double latitude;
            private String terminal_type;
            private String expire_time;
            private String acc_status;
            private double longitudeF;
            private double latitudeF;

            public String getLocate_time() {
                return locate_time;
            }

            public void setLocate_time(String locate_time) {
                this.locate_time = locate_time;
            }

            public int getDistance() {
                return distance;
            }

            public void setDistance(int distance) {
                this.distance = distance;
            }

            public String getGps_time() {
                return gps_time;
            }

            public void setGps_time(String gps_time) {
                this.gps_time = gps_time;
            }

            public int getSpeed() {
                return speed;
            }

            public void setSpeed(int speed) {
                this.speed = speed;
            }

            public int getDirection() {
                return direction;
            }

            public void setDirection(int direction) {
                this.direction = direction;
            }

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public int getLocate_type() {
                return locate_type;
            }

            public void setLocate_type(int locate_type) {
                this.locate_type = locate_type;
            }

            public String getCurrent_time() {
                return current_time;
            }

            public void setCurrent_time(String current_time) {
                this.current_time = current_time;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public String getTerminal_type() {
                return terminal_type;
            }

            public void setTerminal_type(String terminal_type) {
                this.terminal_type = terminal_type;
            }

            public String getExpire_time() {
                return expire_time;
            }

            public void setExpire_time(String expire_time) {
                this.expire_time = expire_time;
            }

            public String getAcc_status() {
                return acc_status;
            }

            public void setAcc_status(String acc_status) {
                this.acc_status = acc_status;
            }

            public double getLongitudeF() {
                return longitudeF;
            }

            public void setLongitudeF(double longitudeF) {
                this.longitudeF = longitudeF;
            }

            public double getLatitudeF() {
                return latitudeF;
            }

            public void setLatitudeF(double latitudeF) {
                this.latitudeF = latitudeF;
            }
        }
    }
}
