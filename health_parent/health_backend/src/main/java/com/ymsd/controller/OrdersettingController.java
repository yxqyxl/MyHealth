package com.ymsd.controller;

import com.ymsd.constant.MessageConstant;
import com.ymsd.entity.Result;
import com.ymsd.pojo.OrderSetting;
import com.ymsd.pojo.OrderSettingOV;
import com.ymsd.service.IOrdersettingService;
import com.ymsd.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ordersetting")
public class OrdersettingController {
    @Resource
    private IOrdersettingService ordersettingService;

    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            //解析excel
            List<String[]> list = POIUtils.readExcel(excelFile);
            ordersettingService.importPOIOrdersetting(list);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }


    @GetMapping("/findAllOrdersetting")
    public Result findAllOrderSetting(){
        try {
            List<OrderSetting> list = ordersettingService.findAllOrderSetting();
            ArrayList<OrderSettingOV> OrderSettingOVs = new ArrayList<>();
            for (OrderSetting orderSetting : list) {
                OrderSettingOV ov = new OrderSettingOV();
                ov.setDate(orderSetting.getOrderDate().getDate());
                ov.setNumber(orderSetting.getNumber());
                ov.setReservations(orderSetting.getReservations());
                OrderSettingOVs.add(ov);
            }
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,OrderSettingOVs);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_ORDER_FAIL);
        }
    }


    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            ordersettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
