package com.tianyigps.xiepeng.data;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public class AdapterChoiceWorkerData {
    private String id, name, area;
    private boolean select;

    public AdapterChoiceWorkerData(String id, String name, String area) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.select = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
