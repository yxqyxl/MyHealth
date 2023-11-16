package com.ymsd.service;

import com.ymsd.entity.PageResult;
import com.ymsd.entity.QueryPageBean;
import com.ymsd.pojo.CheckGroup;

import java.util.List;

public interface ICheckGroupService {
    void addCheckGroup(Integer[] checkItem, CheckGroup checkGroup);

    PageResult findPage(QueryPageBean queryPageBean) throws Exception;

    void deleteById(Integer checkGroupId);

    void updateCheckGroup(Integer[] checkItem,CheckGroup checkGroup);

    Integer[] findByCheckGroup(Integer checkGroupId) throws Exception;

    List<CheckGroup> findAll() throws Exception;
}
