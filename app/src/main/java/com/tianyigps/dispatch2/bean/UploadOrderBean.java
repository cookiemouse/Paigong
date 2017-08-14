package com.tianyigps.dispatch2.bean;

/**
 * Created by cookiemouse on 2017/8/4.
 */

public class UploadOrderBean {
    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","msg":"车辆cN1111,cV1111 carId不存在，出错了","success":false}
     * msg : 车辆cN1111,cV1111 carId不存在，出错了
     * success : false
     * time : 0
     */

    private String errCode;
    private String jsonStr;
    private String msg;
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
}
