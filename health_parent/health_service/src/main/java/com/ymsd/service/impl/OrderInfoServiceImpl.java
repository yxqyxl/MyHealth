package com.ymsd.service.impl;

import com.ymsd.constant.MessageConstant;
import com.ymsd.dao.IMemberDao;
import com.ymsd.dao.IOrderDao;
import com.ymsd.dao.IOrdersettingDao;
import com.ymsd.dto.OrderSettingDTO;
import com.ymsd.entity.Result;
import com.ymsd.pojo.Member;
import com.ymsd.pojo.Order;
import com.ymsd.pojo.OrderSetting;
import com.ymsd.service.IMemberService;
import com.ymsd.service.IOrderInfoService;
import com.ymsd.service.IOrdersettingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class OrderInfoServiceImpl implements IOrderInfoService {

    @Resource
    private IOrdersettingDao ordersettingDao;

    @Resource
    private IMemberDao memberDao;

    @Resource
    private IOrderDao orderDao;

    @Override
    public Result submitOrderInfo(Map map) throws Exception{
        String orderDate = (String) map.get("orderDate");
        String setmealId = (String) map.get("setmealId");
        String idCard = (String) map.get("idCard");
        String telephone = (String) map.get("telephone");
        Integer memberId = null;

        //判断选择日期是否还能预约
        OrderSetting orderSetting = ordersettingDao.findNumberByDate(orderDate);
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (orderSetting == null){
            return new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //判断所选择的预约日期是否已经约满
        //已预约数量
        if (number <= reservations) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }


        //检查是否为会员
        Member member = memberDao.checkIdCardAndOrderDate(idCard);
        if (member == null) {
            Member member1 = new Member();
            member1.setName((String) map.get("name"));
            member1.setIdCard(idCard);
            member1.setPhoneNumber(telephone);
            member1.setRegTime(new Date());
            member1.setSex((String) map.get("sex"));
            memberDao.addMember(member1);
            memberId = member1.getId();
        }


        //检查用户是否重复预约
        HashMap<Object, Object> map1 = new HashMap<>();
        map1.put("setmealId", setmealId);
        map1.put("orderDate", orderDate);
        map1.put("memberId", memberId);
        Order order = orderDao.findOrder(map1);
        Integer orderId = null;
        if (order == null) {
            Order order1 = new Order();
            order1.setMemberId(memberId);
            order1.setOrderDate(orderDate);
            order1.setOrderType("微信预约");
            order1.setOrderStatus("未到诊");
            order1.setSetmealId(Integer.parseInt(setmealId));
            orderDao.addOrder(order1);
            orderId = order1.getId();
        } else {
            return new Result(false, MessageConstant.HAS_ORDERED);
        }

        //更新ordersetting已预约数量

//        orderSetting.setReservations(orderSetting.getReservations()+1);
//        HashMap<Object, Object> map2 = new HashMap<>();
//        map2.put("orderDate",orderDate);
//        map2.put("newReservations",orderSetting.getReservations());
//        map2.put("number",number - 1);
//        ordersettingDao.updateReservations(map2);

        //使用dto
        OrderSettingDTO orderSettingDTO = new OrderSettingDTO();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(orderDate);
        orderSettingDTO.setOrderDate(date);
        orderSettingDTO.setReservations(orderSetting.getReservations());
        orderSettingDTO.setNumber(number-1);
        ordersettingDao.updateReservations(orderSettingDTO);



        return new Result(true,MessageConstant.ORDER_SUCCESS,orderId);

    }

}
