package com.ymsd.dao;

import com.github.pagehelper.Page;
import com.ymsd.pojo.CheckGroup;
import com.ymsd.pojo.CheckItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ICheckGroupDao {
    void addCheckGroup(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(HashMap<String, Integer> map);

    Page<CheckGroup> findPage(String queryString);

    void deleteById(Integer checkGroupId);

    void updateCheckGroup(CheckGroup checkGroup);

    void deleteCheckItemIdAndCheckGroupId(Integer checkGroupId);

    Integer[] findByCheckGroup(Integer checkGroupId);

    List<CheckGroup> findAll();

    List<CheckGroup> findBySetmealId(Integer setmealId);
}
