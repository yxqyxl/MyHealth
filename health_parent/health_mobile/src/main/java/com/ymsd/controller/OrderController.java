package com.ymsd.controller;

import com.ymsd.constant.MessageConstant;
import com.ymsd.entity.Result;
import com.ymsd.service.IOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private IOrderService orderService;

    @PostMapping("/findById")
    public Result findById(@RequestParam Integer id){
        try {
            Map result = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
