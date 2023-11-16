package com.ymsd.service;

import com.ymsd.entity.PageResult;
import com.ymsd.entity.QueryPageBean;
import com.ymsd.pojo.CheckItem;

import java.util.List;


public interface ICheckItemService {
    void addCheckItem(CheckItem checkItem);

    PageResult findPage(QueryPageBean queryPageBean) throws Exception;

    void deleteById(Integer checkItemId);

    void updateCheckItem(CheckItem checkItem);

    List<CheckItem> findAll() throws Exception;
}
