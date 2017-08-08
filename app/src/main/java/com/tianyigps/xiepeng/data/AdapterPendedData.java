package com.tianyigps.xiepeng.data;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public class AdapterPendedData {
    private String title, time, address;
    private String worker, workerPhone, contactName, contactPhone;
    //
    private int status;

    public AdapterPendedData(String title, String time, String address, String worker, String workerPhone, String contactName, String contactPhone, int status) {
        this.title = title;
        this.time = time;
        this.address = address;
        this.worker = worker;
        this.workerPhone = workerPhone;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
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

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getWorkerPhone() {
        return workerPhone;
    }

    public void setWorkerPhone(String workerPhone) {
        this.workerPhone = workerPhone;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
