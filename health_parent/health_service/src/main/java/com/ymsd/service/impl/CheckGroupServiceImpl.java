package com.ymsd.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ymsd.dao.ICheckGroupDao;
import com.ymsd.entity.PageResult;
import com.ymsd.entity.QueryPageBean;
import com.ymsd.pojo.CheckGroup;
import com.ymsd.pojo.CheckItem;
import com.ymsd.service.ICheckGroupService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class CheckGroupServiceImpl implements ICheckGroupService {
    @Resource
    private ICheckGroupDao checkGroupDao;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void addCheckGroup(Integer[] checkItem, CheckGroup checkGroup) {
        checkGroupDao.addCheckGroup(checkGroup);
        Integer checkGroupId = checkGroup.getId();
        HashMap<String, Integer> map = new HashMap<>();
        map.put("checkGroupId", checkGroupId);
        for (Integer checkItemId : checkItem) {
            map.put("checkItemId", checkItemId);
            checkGroupDao.setCheckGroupAndCheckItem(map);

        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) throws Exception{
        //查询redis是否有数据
        String checkGroupData = redisTemplate.boundValueOps("checkGroup.findPage").get();
        Page<CheckGroup> page = null;
        ObjectMapper objectMapper = new ObjectMapper();
        //如果为空走数据库
        if (null==checkGroupData){
            if ("null".equals(queryPageBean.getQueryString())) {
                queryPageBean.setQueryString(null);
            }
            PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
            page = checkGroupDao.findPage(queryPageBean.getQueryString());
            //数据转换为json数据格式存储到redis
            checkGroupData = objectMapper.writeValueAsString(page);
            //将数据存到redis中
            redisTemplate.boundValueOps("checkGroup.findPage").set(checkGroupData);
            redisTemplate.expire("checkGroup.findPage",20, TimeUnit.MINUTES);
            System.out.println("数据库");
        }else {
            page = objectMapper.readValue(checkGroupData, new TypeReference<Page<CheckGroup>>() {});
            System.out.println("redis");
        }
        PageResult result = new PageResult(page.getTotal(), page.getResult());
        return result;
    }

    @Override
    public void deleteById(Integer checkGroupId) {
        checkGroupDao.deleteById(checkGroupId);
        checkGroupDao.deleteCheckItemIdAndCheckGroupId(checkGroupId);
    }

    @Override
    public void updateCheckGroup(Integer[] checkItem, CheckGroup checkGroup) {
        checkGroupDao.updateCheckGroup(checkGroup);
        checkGroupDao.deleteCheckItemIdAndCheckGroupId(checkGroup.getId());
        Integer checkGroupId = checkGroup.getId();
        HashMap<String, Integer> map = new HashMap<>();

        map.put("checkGroupId", checkGroupId);
        for (Integer checkItemId : checkItem) {
            map.put("checkItemId", checkItemId);
            checkGroupDao.setCheckGroupAndCheckItem(map);

        }
    }

    @Override
    public Integer[] findByCheckGroup(Integer checkGroupId) throws Exception {
        Integer[] checkItemIds = null;
        ObjectMapper objectMapper = new ObjectMapper();
        //查询redis是否有数据
        String checkGroupData = redisTemplate.boundValueOps("checkGroup.findByCheckGroup").get();
        if (null == checkGroupData){
            //走数据库
            checkItemIds = checkGroupDao.findByCheckGroup(checkGroupId);
            //转换为json格式的字符串
            checkGroupData = objectMapper.writeValueAsString(checkItemIds);
            //存储到redis中
            redisTemplate.boundValueOps("checkGroup.findByCheckGroup").set(checkGroupData);
            redisTemplate.expire("checkGroup.findByCheckGroup",20,TimeUnit.MINUTES);
            System.out.println("数据库");
        }else {
            checkItemIds = objectMapper.readValue(checkGroupData, new TypeReference<Integer[]>() {});
            System.out.println("redis");
        }
        return checkItemIds;
    }

    @Override
    public List<CheckGroup> findAll() throws Exception {
        List<CheckGroup> checkGroups = null;
        ObjectMapper objectMapper = new ObjectMapper();
        //查询redis
        String checkGroupData = redisTemplate.boundValueOps("checkGroup.findAll").get();
        if (null==checkGroupData){
            //走数据库查询
            checkGroups = checkGroupDao.findAll();
            //转换为json格式存储到redis中
            checkGroupData = objectMapper.writeValueAsString(checkGroups);
            redisTemplate.boundValueOps("checkGroup.findAll").set(checkGroupData);
            redisTemplate.expire("checkGroup.findAll",20,TimeUnit.MINUTES);
            System.out.println("数据库");
        }else {
            checkGroups = objectMapper.readValue(checkGroupData, new TypeReference<List<CheckGroup>>() {});
            System.out.println("redis");
        }
        return checkGroups;
    }
}
