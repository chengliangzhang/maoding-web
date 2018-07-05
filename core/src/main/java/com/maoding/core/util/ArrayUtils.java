package com.maoding.core.util;

import java.math.BigDecimal;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/16 18:02
 * 描    述 :
 */
public class ArrayUtils {
    public static String sum(String data[]) {
        BigDecimal sum = new BigDecimal(0);
        for (int i = 1; i < data.length - 1; i++) {
            BigDecimal bd = new BigDecimal(data[i]);
            sum = sum.add(bd);
        }
        return sum.toString();
    }
}
