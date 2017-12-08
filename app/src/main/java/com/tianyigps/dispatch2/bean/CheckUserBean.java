package com.tianyigps.dispatch2.bean;

import java.util.List;

/**
 * Created by djc on 2017/7/17.
 */

public class CheckUserBean {
    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":{"phoneNo":"13817234567","area":275,"token":"25d55ad283aa400af464c76d713c07ad","name":"张恩恩","directorPhone":"13817194123","duties":1,"jobNo":"10052","eid":367,"headPhone":"18017325972","imgBaseUrl":"http://121.43.178.183/file/pic/"},"msg":"验证通过","success":true}
     * msg : 验证通过
     * obj : {"phoneNo":"13817234567","area":275,"token":"25d55ad283aa400af464c76d713c07ad","name":"张恩恩","directorPhone":"13817194123","duties":1,"jobNo":"10052","eid":367,"headPhone":"18017325972","imgBaseUrl":"http://121.43.178.183/file/pic/"}
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
         * phoneNo : 13817234567
         * area : 275
         * token : 25d55ad283aa400af464c76d713c07ad
         * name : 张恩恩
         * directorPhone : 13817194123
         * duties : 1
         * jobNo : 10052
         * eid : 367
         * headPhone : 18017325972
         * imgBaseUrl : http://121.43.178.183/file/pic/
         */

        private String phoneNo;
        private int area;
        private String token;
        private String name;
        private String directorPhone;
        private int duties;
        private String jobNo;
        private int eid;
        private String headPhone;
        private String imgBaseUrl;
        private List<HeadPhoneList> headPhoneList;

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public int getArea() {
            return area;
        }

        public void setArea(int area) {
            this.area = area;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDirectorPhone() {
            return directorPhone;
        }

        public void setDirectorPhone(String directorPhone) {
            this.directorPhone = directorPhone;
        }

        public int getDuties() {
            return duties;
        }

        public void setDuties(int duties) {
            this.duties = duties;
        }

        public String getJobNo() {
            return jobNo;
        }

        public void setJobNo(String jobNo) {
            this.jobNo = jobNo;
        }

        public int getEid() {
            return eid;
        }

        public void setEid(int eid) {
            this.eid = eid;
        }

        public String getHeadPhone() {
            return headPhone;
        }

        public void setHeadPhone(String headPhone) {
            this.headPhone = headPhone;
        }

        public String getImgBaseUrl() {
            return imgBaseUrl;
        }

        public void setImgBaseUrl(String imgBaseUrl) {
            this.imgBaseUrl = imgBaseUrl;
        }

        public List<HeadPhoneList> getHeadPhoneList() {
            return headPhoneList;
        }

        public void setHeadPhoneList(List<HeadPhoneList> headPhoneList) {
            this.headPhoneList = headPhoneList;
        }

        public static class HeadPhoneList{
            private String contactPhone;

            public String getContactPhone() {
                return contactPhone;
            }

            public void setContactPhone(String contactPhone) {
                this.contactPhone = contactPhone;
            }
        }

    }
}
