package com.mmall.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
    public static BigDecimal mul(double a, double b){
        return new BigDecimal(a*b);
    }
    public static BigDecimal add(double a,double b){
        return new BigDecimal(a+b);
    }
}
