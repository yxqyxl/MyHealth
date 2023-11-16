package com.ymsd.controller;

import com.ymsd.utils.SignUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * @Author 老高
 * @Version 1.0
 */
@RestController
@RequestMapping("/weixin")
@CrossOrigin
public class HealthWeixinReplyController {

    @GetMapping(value = "/reply")
    public void get(HttpServletRequest request, HttpServletResponse response) throws Exception {

        System.out.println("========WechatController========= ");


        Enumeration pNames = request.getParameterNames();
        while (pNames.hasMoreElements()) {
            String name = (String) pNames.nextElement();
            String value = request.getParameter(name);
            // out.print(name + "=" + value);
            String log = "name =" + name + "     value =" + value;
        }
        System.out.println("========WechatControllertets========= ");
        String signature = request.getParameter("signature");/// 微信加密签名
        String timestamp = request.getParameter("timestamp");/// 时间戳
        String nonce = request.getParameter("nonce"); /// 随机数
        String echostr = request.getParameter("echostr"); // 随机字符串
        PrintWriter out = response.getWriter();

        if (SignUtils.checkSignature(signature, timestamp, nonce)) {
            System.out.println("校验成功！！！！！！！！！！！！！");
            out.print(echostr);
        }

        out.close();
        out = null;



    }

    @GetMapping("/sendMessage")
    public void autoMessage(HttpServletRequest request,HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.print("找你爹干嘛啊");
        out.close();

    }


}
