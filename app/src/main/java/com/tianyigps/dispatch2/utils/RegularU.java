package com.tianyigps.dispatch2.utils;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cookiemouse on 2017/8/4.
 */

public class RegularU {

    private static final String TAG = "RegularU";
    private static final String CHECK_CAR_NO = "[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5,6}$";
    private static final String CHECK_WEB_ADDRESS = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$";
    private static final String CHECK_FRAME_NO = "^[A-HJ-NPR-Z0-9]{17}$";

    public static boolean checkCarNo(String carNo) {
        Log.i(TAG, "checkCarNo: carNo-->" + carNo);
        Pattern pattern = Pattern.compile(CHECK_CAR_NO);
        Matcher matcher = pattern.matcher(carNo);
        Log.i(TAG, "checkCarNo: result-->" + matcher.matches());
        return matcher.matches();
    }

    public static boolean isEmpty(String content) {
        return null == content || "".equals(content);
    }

    //  是否为网址
    public static boolean isWebAddress(String address) {
        Pattern pattern = Pattern.compile(CHECK_WEB_ADDRESS);
        Matcher matcher = pattern.matcher(address);
        return matcher.matches();
    }

    //  是否为合法车架号
    public static boolean isFrameNo(String number) {
        String nine = "";
        int sum = 0;

        Map<Character, Integer> kv = new HashMap<>();
        //  0-9
        for (int i = 0; i < 10; i++) {
            kv.put(String.valueOf(i).charAt(0), i);
        }
        //  a-z
        kv.put('a', 1);
        kv.put('b', 2);
        kv.put('c', 3);
        kv.put('d', 4);
        kv.put('e', 5);
        kv.put('f', 6);
        kv.put('g', 7);
        kv.put('h', 8);
        kv.put('j', 1);
        kv.put('k', 2);
        kv.put('l', 3);
        kv.put('m', 4);
        kv.put('n', 5);
        kv.put('p', 7);
        kv.put('q', 8);
        kv.put('r', 9);
        kv.put('s', 2);
        kv.put('t', 3);
        kv.put('u', 4);
        kv.put('v', 5);
        kv.put('w', 6);
        kv.put('x', 7);
        kv.put('y', 8);
        kv.put('z', 9);

        int[] coefficients = {8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2};

        Pattern pattern = Pattern.compile(CHECK_FRAME_NO);
        Matcher matcher = pattern.matcher(number);
        if (!matcher.matches()) {
            return false;
        }
        number = number.toLowerCase();
        char[] noArr = number.toCharArray();
        for (int i = 0; i < noArr.length; i++) {
            if (8 == i) {
                nine = noArr[i] + "";
            }
            sum += kv.get(noArr[i]) * coefficients[i];
        }
        int check = sum % 11;
        if (10 == check) {
            return "x".equals(nine);
        } else {
            return (check + "").equals(nine);
        }
    }

    //  验证是否含有表情
    public static boolean hadEmoji(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }
}
