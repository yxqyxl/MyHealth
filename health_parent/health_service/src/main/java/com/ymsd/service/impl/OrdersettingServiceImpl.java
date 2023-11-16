package com.ymsd.service.impl;

import com.ymsd.dao.IOrdersettingDao;
import com.ymsd.pojo.OrderSetting;
import com.ymsd.service.IOrdersettingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class OrdersettingServiceImpl implements IOrdersettingService {
    @Resource
    private IOrdersettingDao ordersettingDao;

    @Override
    public void importPOIOrdersetting(List<String[]> list) {
        for (String[] strings : list) {
            OrderSetting orderSetting = new OrderSetting();
            orderSetting.setOrderDate(new Date(strings[0]));
            orderSetting.setNumber(Integer.parseInt(strings[1]));
            ordersettingDao.importPOIOrdersetting(orderSetting);
        }
    }

    @Override
    public List<OrderSetting> findAllOrderSetting() {
        return ordersettingDao.findAllOrderSetting();
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String orderDate = sdf.format(orderSetting.getOrderDate());
//        Integer number = ordersettingDao.findNumberByDate(orderDate);
//        System.out.println(number);
//        if (number == null){
//            ordersettingDao.addNumberDate(orderSetting);
//        } else {
//            HashMap<Object, Object> map = new HashMap<>();
//            map.put("number",orderSetting.getNumber());
//            map.put("orderDate",orderDate);
//            ordersettingDao.update(map);
//        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String orderDate = sdf.format(orderSetting.getOrderDate());
        OrderSetting or = ordersettingDao.findNumberByDate(orderDate);
        HashMap<Object, Object> map = new HashMap<>();
        Integer oldNumber = null;
        if (or != null){
            oldNumber = or.getNumber();
        }
        map.put("oldNumber",oldNumber);
        map.put("number",orderSetting.getNumber());
        map.put("orderDate",orderDate);
        ordersettingDao.update2(map);

    }





}
