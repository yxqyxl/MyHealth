package com.ymsd.dao;

import com.ymsd.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IOrderDao {
    Order findOrder(Map map);

    void addOrder(Order order1);

    Map findById(Integer id);

    Integer findTodayNewMember(String nowDate);

    Integer findOrderCountByDate(String nowDate);

    Integer findVisitCountByDate(String nowDate);

    Integer findOrderCountAfterDate(String value);

    Integer findVisitCountAfterDate(String value);

    List<Map> findHotSetmeal();
}
