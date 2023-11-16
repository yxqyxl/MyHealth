package com.ymsd.service;

import com.ymsd.entity.Result;

import java.util.Map;

public interface IOrderInfoService {
    Result submitOrderInfo(Map map) throws Exception;
}
