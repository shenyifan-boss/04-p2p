package com.bjpowernode.util;

import java.util.regex.Pattern;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/28
 **/
public class CommonUtil {
    public static boolean checkPhoneFormat(String phone){
        boolean result = Pattern.matches("^1[1-9]\\d{9}$", phone);
        return result;
    }
}
