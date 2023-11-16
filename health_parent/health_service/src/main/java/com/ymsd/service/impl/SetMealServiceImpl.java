package com.ymsd.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ymsd.dao.IMemberDao;
import com.ymsd.dao.IOrderDao;
import com.ymsd.dao.ISetMealDao;
import com.ymsd.entity.PageResult;
import com.ymsd.entity.QueryPageBean;
import com.ymsd.pojo.CheckItem;
import com.ymsd.pojo.Setmeal;
import com.ymsd.service.ISetMealService;
import com.ymsd.utils.DateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class SetMealServiceImpl implements ISetMealService {
    @Resource
    private ISetMealDao setMealDao;

    @Resource
    private IOrderDao orderDao;

    @Resource
    private IMemberDao memberDao;
    @Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;


    @Value("${out_put_path}")
    private String out_put_path;

    @Override
    public void addSetmeal(Integer[] checkGroupIds, Setmeal setmeal) throws Exception{
        setmeal.setStatus(1);
        setMealDao.addSetmeal(setmeal);
        Integer setmealId = setmeal.getId();
        HashMap<String, Integer> map = new HashMap<>();
        map.put("setmeal_id",setmealId);
        for (Integer checkGroupId : checkGroupIds) {
            map.put("checkgroup_id",checkGroupId);
            setMealDao.setSetmealAndCheckGroup(map);
        }


        //生成静态化页面
        getStaicHtml();
    }


    public void getStaicHtml() throws Exception{
        List<Setmeal> setmeals = setMealDao.getFindAll();
        //生成套餐列表页面
        getStaicSetmealListHtml(setmeals);
        //生成套餐详情页面
        getStaicSetmealHtml(setmeals);
    }

    //生成套餐列表页面
    public void getStaicSetmealListHtml(List<Setmeal> setmeals) throws Exception{
        Map<Object, Object> dataMap = new HashMap<>();
        dataMap.put("setmealList",setmeals);
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",dataMap);
    }

    //生成套餐详情页面
    public void getStaicSetmealHtml(List<Setmeal> setmeals) throws Exception{
        for (Setmeal setmeal : setmeals) {
            Map<Object, Object> dataMap = new HashMap<>();
            dataMap.put("setmeal",setMealDao.findById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl","m_setmeal_" + setmeal.getId() + ".html",dataMap);
        }
    }

    public void generateHtml(String templateName,String htmlName,Map<Object, Object> dataMap) throws Exception{
        Writer out = null;
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate(templateName);
        System.out.println("地址为："+out_put_path+htmlName);
        File docFile = new File(out_put_path + htmlName);
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
        template.process(dataMap,out);
        out.close();
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        if ("null".equals(queryPageBean.getQueryString())){
            queryPageBean.setQueryString(null);
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckItem> page = setMealDao.findPage(queryPageBean.getQueryString());
        PageResult result = new PageResult(page.getTotal(), page.getResult());
        return result;
    }

    @Override
    public void deleteById(Integer setmealId) {
        setMealDao.deleteById(setmealId);
    }
//    @Override
//    public void deleteById(Integer setmealId,String filename) {
//
//        int result = setMealDao.deleteById(setmealId);
//        if (result > 0){
//            QiniuUtils.deleteFileFromQiniu(filename);
//        }
//        setMealDao.deleteSetmealIdAndCheckGroupId(setmealId);
//    }

    @Override
    public Integer[] findBySetmealId(Integer setmealId) {
        return setMealDao.findBySetmealId(setmealId);
    }

    @Override
    public void updateSetmeal(Integer[] checkgroupIds, Setmeal setmeal) {
        setMealDao.updateSetmeal(setmeal);
        setMealDao.deleteSetmealIdAndCheckGroupId(setmeal.getId());
        Integer setmealId = setmeal.getId();
        HashMap<String, Integer> map = new HashMap<>();

        map.put("setmeal_id",setmealId);
        for (Integer checkgroupId : checkgroupIds) {
            map.put("checkgroup_id",checkgroupId);
            setMealDao.setSetmealAndCheckGroup(map);
        }
    }

    @Override
    public Setmeal findSetmealById(Integer setmealId) {
        return setMealDao.findSetmealById(setmealId);
    }

    @Override
    public ArrayList<Map<String, Object>> findSetmealCount() {
        return setMealDao.findSetmealCount();
    }

    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        HashMap<String, Object> map = new HashMap<>();


        //* reportDate --> String
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = sdf.format(new Date());
        map.put("reportDate",nowDate);
        //todayNewMember -> number
        Integer todayNewMember = orderDao.findTodayNewMember(nowDate);
        map.put("todayNewMember",todayNewMember);
        //totalMember -> number
        Integer totalMember = memberDao.findCount();
        map.put("totalMember",totalMember);
        //thisWeekNewMember -> number
        //获取本周的第一天
        HashMap<Object, Object> memberTime = new HashMap<>();
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        memberTime.put("startTime",thisWeekMonday);
        int endTime = Integer.parseInt(thisWeekMonday.substring(thisWeekMonday.lastIndexOf("-")+1));
        endTime = endTime + 6;
        String endTimeStr = thisWeekMonday.substring(0,thisWeekMonday.lastIndexOf("-")+1) + endTime;
        memberTime.put("endTime",endTimeStr);
        Integer thisWeekMemberCount = memberDao.findThisWeekMemberCount(memberTime);
        map.put("thisWeekNewMember",thisWeekMemberCount);
        //thisMonthNewMember -> number
        HashMap<Object, Object> memberTime2 = new HashMap<>();
        String firstDayNewMember = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        memberTime2.put("startTime",firstDayNewMember);
        int endTime2 = Integer.parseInt(firstDayNewMember.substring(firstDayNewMember.lastIndexOf("-")+1));
        endTime2 = endTime2 + 30;
        String endTimeStr2 = firstDayNewMember.substring(0,firstDayNewMember.lastIndexOf("-")+1) + endTime2;
        memberTime2.put("endTime",endTimeStr2);
        Integer thisMonthNewMember = memberDao.thisMonthNewMemberCount(memberTime2);
        map.put("thisMonthNewMember",thisMonthNewMember);

        //今日预约数  todayOrderNumber -> number
        Integer todayOrderNumber = orderDao.findOrderCountByDate(nowDate);
        //今日到诊人数 todayVisitsNumber -> number
        Integer todayVisitsNumber = orderDao.findVisitCountByDate(nowDate);
        //本周预约数  thisWeekOrderNumber -> number
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);
        //本周到诊人数 thisWeekVisitsNumber -> number
        Integer thisWeekVisitsNumber = orderDao.findVisitCountAfterDate(thisWeekMonday);
        //本月预约数  thisMonthOrderNumber -> number
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDayNewMember);
        //本月到诊人数 thisMonthVisitsNumber -> number
        Integer thisMonthVisitsNumber = orderDao.findVisitCountAfterDate(firstDayNewMember);

        //热门套餐
        List<Map> hotSetmeal = orderDao.findHotSetmeal();
        map.put("todayOrderNumber",todayOrderNumber);
        map.put("todayVisitsNumber",todayVisitsNumber);
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        map.put("hotSetmeal",hotSetmeal);
        return map;
    }
}
