package com.ymsd;

import com.ymsd.utils.QiniuUtils;

public class TestQiniuUtil {
    public static void main(String[] args) {
        String path = "E:\\hh\\hhh.jpg";
        QiniuUtils.upload2Qiniu(path,"hhh.jpg");
    }
}
