package com.ymsd.dao;

import com.github.pagehelper.Page;
import com.ymsd.pojo.CheckItem;
import com.ymsd.pojo.Setmeal;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface ISetMealDao {
    void addSetmeal(Setmeal setmeal);

    void setSetmealAndCheckGroup(HashMap<String, Integer> map);

    Page<CheckItem> findPage(String queryString);

    int deleteById(Integer setmealId);


    void deleteSetmealIdAndCheckGroupId(Integer setmealId);

    Integer[] findBySetmealId(Integer setmealId);

    void updateSetmeal(Setmeal setmeal);

    List<Setmeal> getFindAll();

    Setmeal findById(Integer id);

    Setmeal findSetmealById(Integer setmealId);

    ArrayList<Map<String, Object>> findSetmealCount();
}
