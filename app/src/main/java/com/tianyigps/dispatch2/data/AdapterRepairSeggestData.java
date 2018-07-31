package com.tianyigps.dispatch2.data;

/**
 * Created by cookiemouse on 2017/8/8.
 */

public class AdapterRepairSeggestData {
    private String car, imei, suggest;

    public AdapterRepairSeggestData(String car, String imei, String suggest) {
        this.car = car;
        this.imei = imei;
        this.suggest = suggest;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }
}
