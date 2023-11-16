package com.ymsd.pojo;

import java.util.Date;

public class OrderSettingOV {
    private int date;//预约设置日期
    private int number;//可预约人数
    private int reservations ;//已预约人数

    public OrderSettingOV() {
    }

    public OrderSettingOV(int date, int number, int reservations) {
        this.date = date;
        this.number = number;
        this.reservations = reservations;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getReservations() {
        return reservations;
    }

    public void setReservations(int reservations) {
        this.reservations = reservations;
    }
}
