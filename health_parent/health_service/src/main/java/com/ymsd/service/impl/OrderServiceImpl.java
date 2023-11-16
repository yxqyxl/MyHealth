package com.ymsd.service.impl;

import com.ymsd.dao.IMemberDao;
import com.ymsd.dao.IOrderDao;
import com.ymsd.dao.ISetMealDao;
import com.ymsd.pojo.Member;
import com.ymsd.pojo.Order;
import com.ymsd.pojo.Setmeal;
import com.ymsd.service.IOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {
    @Resource
    private IOrderDao orderDao;
    @Resource
    private IMemberDao memberDao;
    @Resource
    private ISetMealDao setMealDao;


    @Override
    public Map findById(Integer id) {
//        Map<Object, Object> map = new HashMap<>();
//        Order order = orderDao.findById(id);
//        System.out.println(order);
//        Member member = memberDao.findById(order.getMemberId());
//        System.out.println("member"+member);
//        Setmeal setmeal = setMealDao.findSetmealById(order.getSetmealId());
//        System.out.println(member.getName() + setmeal.getName());
//        map.put("member",member.getName());
//        map.put("setmeal",setmeal.getName());
//        map.put("orderDate",order.getOrderDate());
//        map.put("orderType",order.getOrderType());
//        return map;

        Map result = orderDao.findById(id);
        return result;

    }
}
