package com.ymsd.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ymsd.dao.ICheckItemDao;
import com.ymsd.pojo.CheckItem;
import com.ymsd.entity.QueryPageBean;
import com.ymsd.entity.PageResult;
import com.ymsd.service.ICheckItemService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class CheckItemServiceImpl implements ICheckItemService {
    @Resource
    private ICheckItemDao checkItemDao;

    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public void addCheckItem(CheckItem checkItem) {
        checkItemDao.addCheckItem(checkItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) throws Exception{
        Page<CheckItem> page = null;
        ObjectMapper om = new ObjectMapper();
        //从redis中获取数据
        String checkItemsData = redisTemplate.boundValueOps("checkItemDao.findPage").get();
        if (null == checkItemsData){
            if ("null".equals(queryPageBean.getQueryString())){
                queryPageBean.setQueryString(null);
            }
            PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
            page = checkItemDao.findPage(queryPageBean.getQueryString());
            //数据转换成json格式字符串
            checkItemsData = om.writeValueAsString(page);
            redisTemplate.boundValueOps("checkItemDao.findPage").set(checkItemsData);
            //设置时效
            redisTemplate.expire("checkItemDao.findPage",20, TimeUnit.MINUTES);
            System.out.println("数据库");
        }else {
            page = om.readValue(checkItemsData, new TypeReference<Page<CheckItem>>() {});
            System.out.println("redis");
        }
        PageResult result = new PageResult(page.getTotal(), page.getResult());
        return result;
    }

    @Override
    public void deleteById(Integer checkItemId) {
        checkItemDao.deleteById(checkItemId);
    }

    @Override
    public void updateCheckItem(CheckItem checkItem) {
        checkItemDao.updateCheckItem(checkItem);
    }

    @Override
    public List<CheckItem> findAll() throws Exception{
        //从redis中获取数据
        String checkItemsData = redisTemplate.boundValueOps("checkItemDao.findAll").get();
        List<CheckItem> checkItems = null;
        ObjectMapper om = new ObjectMapper();
        //判断redis里面是否有数据
        if (null == checkItemsData){
            checkItems = checkItemDao.findAll();
            //转换成json格式字符串
            checkItemsData = om.writeValueAsString(checkItems);
            redisTemplate.boundValueOps("checkItemDao.findAll").set(checkItemsData);
            //设置时效
            redisTemplate.expire("checkItemDao.findAll",20, TimeUnit.MINUTES);
            System.out.println("数据库");
        } else {
            /**
             *  Jackson 库中的 ObjectMapper 类的 readValue() 方法
             *  来将 Redis 中存储的 JSON 字符串 checkItemsData
             *  转换为 List<CheckItem> 对象。
             */
            checkItems = om.readValue(checkItemsData, new TypeReference<List<CheckItem>>() {});
            System.out.println("redis");
        }


        return checkItems;
    }
}
