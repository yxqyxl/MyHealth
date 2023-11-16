package com.ymsd.dao;

import com.ymsd.dto.OrderSettingDTO;
import com.ymsd.pojo.OrderSetting;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface IOrdersettingDao {
    void importPOIOrdersetting(OrderSetting orderSetting);

    List<OrderSetting> findAllOrderSetting();

    OrderSetting findNumberByDate(String orderDate);

    void addNumberDate(OrderSetting orderSetting);

    void update(HashMap<Object, Object> map);

    void update2(HashMap<Object, Object> map);


    void updateReservations(String orderDate, int newReservations);

    void updateReservations(OrderSettingDTO orderSettingDTO);
}
