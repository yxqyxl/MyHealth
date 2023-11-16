package com.ymsd.service;

import com.ymsd.pojo.OrderSetting;

import java.util.List;

public interface IOrdersettingService {
    void importPOIOrdersetting(List<String[]> list);

    List<OrderSetting> findAllOrderSetting();

    void editNumberByDate(OrderSetting orderSetting);



}
