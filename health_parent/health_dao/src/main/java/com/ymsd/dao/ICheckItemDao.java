package com.ymsd.dao;

import com.github.pagehelper.Page;
import com.ymsd.pojo.CheckItem;
import com.ymsd.entity.QueryPageBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICheckItemDao {
    void addCheckItem(CheckItem checkItem);

    Page<CheckItem> findPage(String queryString);

    void deleteById(Integer checkItemId);

    void updateCheckItem(CheckItem checkItem);

    List<CheckItem> findAll();

    List<CheckItem> findByCheckGroupId(Integer checkGroupId);



}
