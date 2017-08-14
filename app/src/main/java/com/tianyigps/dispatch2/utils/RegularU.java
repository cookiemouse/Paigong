package com.tianyigps.dispatch2.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cookiemouse on 2017/8/4.
 */

public class RegularU {
    public static boolean checkCarNo(String carNo) {
        Pattern pattern = Pattern.compile("/^[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5,6}$/");
        Matcher matcher = pattern.matcher(carNo);
        return matcher.matches();
    }
}
