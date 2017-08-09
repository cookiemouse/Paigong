package com.tianyigps.xiepeng.data;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public class AdapterPendedData {
    private String title, address, jobNo, worker, workerPhone, contact, contactPhone, orderNo;
    private long time;
    private int orderStatus, reviseFlag, orderId;

    public AdapterPendedData(String title, String address, String jobNo, String worker, String workerPhone, String contact
            , String contactPhone, long time, int orderStatus, int reviseFlag, String orderNo, int orderId) {
        this.title = title;
        this.address = address;
        this.jobNo = jobNo;
        this.worker = worker;
        this.workerPhone = workerPhone;
        this.contact = contact;
        this.contactPhone = contactPhone;
        this.time = time;
        this.orderStatus = orderStatus;
        this.reviseFlag = reviseFlag;
        this.orderNo = orderNo;
        this.orderId = orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getReviseFlag() {
        return reviseFlag;
    }

    public void setReviseFlag(int reviseFlag) {
        this.reviseFlag = reviseFlag;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
