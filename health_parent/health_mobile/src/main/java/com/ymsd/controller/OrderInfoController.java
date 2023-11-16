package com.ymsd.controller;

import com.ymsd.constant.MessageConstant;
import com.ymsd.dao.ISetMealDao;
import com.ymsd.entity.Result;
import com.ymsd.pojo.Setmeal;
import com.ymsd.service.IOrderInfoService;
import com.ymsd.utils.MessageUtils;
import com.ymsd.utils.TenxunUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/orderInfo")
public class OrderInfoController {

    @Resource
    private IOrderInfoService orderInfoService;

    @Resource
    private ISetMealDao setMealDao;

    @GetMapping("/sendValidateCode/{phone}")
    private Result sendMessage(@PathVariable("phone")String phone, HttpServletRequest request){
        try {
            String message4 = MessageUtils.getMessage4();
//            TenxunUtils.sendMessage(phone,message4);
            HttpSession session = request.getSession();
            System.out.println("作用域手机号"+phone);
            session.setAttribute(phone,message4);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }


    @PostMapping("/submitOrderInfo")
    public Result submitOrderInfo(@RequestBody Map map,HttpServletRequest request) throws Exception{
        String telephone = (String) map.get("telephone");
        HttpSession session = request.getSession();
        String attributeValidateCode = (String) session.getAttribute(telephone);
        String validateCode = (String) map.get("validateCode");
        //判断验证码是否一致
        if (attributeValidateCode.equals(validateCode)){
            //调用service判断，如果成功则发送短息
            Result result = orderInfoService.submitOrderInfo(map);
            if (result.isFlag()){
//                TenxunUtils.successful(phone);
            }
            return result;
        } else {
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }

    }

    @GetMapping("/findSetmealById/{id}")
    public Result findSetmealById(@PathVariable("id") Integer setmealId){
        try {
            Setmeal setmeal = setMealDao.findSetmealById(setmealId);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
