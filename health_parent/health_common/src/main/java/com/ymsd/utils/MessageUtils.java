package com.ymsd.utils;

public class MessageUtils {


    public static String getMessage4(){
        long millis = System.currentTimeMillis();
        String str = millis + "";
        String substring = str.substring(str.length() - 4);
        System.out.println(substring);
        return substring;
    }
}
