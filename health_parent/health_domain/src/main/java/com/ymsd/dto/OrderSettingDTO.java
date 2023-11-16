package com.ymsd.dto;

import java.util.Date;

public class OrderSettingDTO {
    private Date orderDate;
    private Integer number;
    private  Integer reservations;

    public OrderSettingDTO() {
    }

    public OrderSettingDTO(Date orderDate, Integer number, Integer reservations) {
        this.orderDate = orderDate;
        this.number = number;
        this.reservations = reservations;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getReservations() {
        return reservations;
    }

    public void setReservations(Integer reservations) {
        this.reservations = reservations;
    }
}
