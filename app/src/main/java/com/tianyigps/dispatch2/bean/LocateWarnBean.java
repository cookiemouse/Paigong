package com.tianyigps.dispatch2.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/12/11.
 */

public class LocateWarnBean {
    /**
     * errCode :
     * jsonStr : {"time":0,"errCode":"","obj":[{"name":"断电报警","create_time":1473741742000},{"name":"断电报警","create_time":1469900355000},{"name":"断电报警","create_time":1469241048000},{"name":"断电报警","create_time":1468221716000},{"name":"断电报警","create_time":1468080645000},{"name":"断电报警","create_time":1466926658000},{"name":"断电报警","create_time":1466827203000},{"name":"断电报警","create_time":1466824901000},{"name":"断电报警","create_time":1466824837000},{"name":"断电报警","create_time":1464600330000}],"msg":"操作成功","success":true}
     * msg : 操作成功
     * obj : [{"name":"断电报警","create_time":1473741742000},{"name":"断电报警","create_time":1469900355000},{"name":"断电报警","create_time":1469241048000},{"name":"断电报警","create_time":1468221716000},{"name":"断电报警","create_time":1468080645000},{"name":"断电报警","create_time":1466926658000},{"name":"断电报警","create_time":1466827203000},{"name":"断电报警","create_time":1466824901000},{"name":"断电报警","create_time":1466824837000},{"name":"断电报警","create_time":1464600330000}]
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
         * name : 断电报警
         * create_time : 1473741742000
         */

        private String name;
        private long create_time;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }
    }
}
