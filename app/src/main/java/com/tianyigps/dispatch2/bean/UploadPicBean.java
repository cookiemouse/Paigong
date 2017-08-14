package com.tianyigps.dispatch2.bean;

/**
 * Created by cookiemouse on 2017/7/28.
 */

public class UploadPicBean {
    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":{"imgUrl":"TY20170717143432075/28f0abc473c843c98cffca0294d0900c.png","id":370},"msg":"上传图片成功","success":true}
     * msg : 上传图片成功
     * obj : {"imgUrl":"TY20170717143432075/28f0abc473c843c98cffca0294d0900c.png","id":370}
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
         * imgUrl : TY20170717143432075/28f0abc473c843c98cffca0294d0900c.png
         * id : 370
         */

        private String imgUrl;
        private int id;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
