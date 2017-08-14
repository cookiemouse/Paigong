package com.tianyigps.dispatch2.data;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public class AdapterChoiceWorkerData {
    private int id;
    private String jobNo, name, area;
    private boolean select;

    public AdapterChoiceWorkerData(int id, String jobNo, String name, String area) {
        this.id = id;
        this.jobNo = jobNo;
        this.name = name;
        this.area = area;
        this.select = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }
}
