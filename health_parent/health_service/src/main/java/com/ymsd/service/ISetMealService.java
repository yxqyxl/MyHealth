package com.ymsd.service;

import com.ymsd.entity.PageResult;
import com.ymsd.entity.QueryPageBean;
import com.ymsd.pojo.Setmeal;

import java.util.ArrayList;
import java.util.Map;

public interface ISetMealService {
    void addSetmeal(Integer[] checkGroupIds, Setmeal setmeal) throws Exception;

    PageResult findPage(QueryPageBean queryPageBean);

    void deleteById(Integer setmealId);
//    void deleteById(Integer setmealId,String filename);


    Integer[] findBySetmealId(Integer setmealId);

    void updateSetmeal(Integer[] checkgroupIds, Setmeal setmeal);

    Setmeal findSetmealById(Integer setmealId);

    ArrayList<Map<String, Object>> findSetmealCount();

    Map<String, Object> getBusinessReportData() throws Exception;
}
