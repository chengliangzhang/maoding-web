package com.maoding.core.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by Wuwq on 2016/11/14.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 转换为字节数组
     * @param str
     * @return
     */
    public static byte[] getBytes(String str){
        if (str != null){
            try {
                return str.getBytes(CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * 转换为字符串
     * @param bytes
     * @return
     */
    public static String toString(byte[] bytes){
        try {
            return new String(bytes, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            return EMPTY;
        }
    }
}
