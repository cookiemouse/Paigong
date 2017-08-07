package com.tianyigps.xiepeng.data;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public class AdapterPendedData {
    private String title, time, address;
    private String contactName, contactPhone, worker;

    //
    private int status;

    public AdapterPendedData(String title, String time, String address, String contactName, String contactPhone, String worker, int status) {
        this.title = title;
        this.time = time;
        this.address = address;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.worker = worker;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
