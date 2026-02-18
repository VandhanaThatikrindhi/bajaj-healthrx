package com.bajaj.healthrx.util;

public class RegNoUtil {

    public static boolean isOdd(String regNo) {
        String digits = regNo.replaceAll("\\D", "");
        int lastTwo = Integer.parseInt(digits.substring(digits.length() - 2));
        return lastTwo % 2 != 0;
    }
}
