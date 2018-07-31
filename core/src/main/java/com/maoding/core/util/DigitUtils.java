package com.maoding.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/9 9:41
 * 描    述 : 数值的判断、比较、转换
 */
public class DigitUtils {
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(DigitUtils.class);

    /** 用于浮点数比较的误差值 */
    private static final double LIMIT_0 = 0.0000001; //归零值

    /**
     * 描述      比较两个数值是否相同
     * 日期      2018/7/31
     * @author  张成亮
     * @return  如果数值差别小于误差值，返回真，否则返回假
     * @param   num1 第一个数值
     * @param   num2 第二个数值
     **/
    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     比较两个数值是否相同
     * @param   num1 第一个数值
     * @param   num2 第二个数值
     * @return  如果数值差别小于误差值，返回真，否则返回假
     **/
    public static Boolean isSame(final Object num1, final Object num2){
        if ((num1 == null) && (num2 == null)) return true;
        if ((num1 == null) || (num2 == null)) return false;
        String n1 = num1.toString();
        String n2 = num2.toString();
        assert (StringUtils.isNumeric(n1) && StringUtils.isNumeric(n2));
        Double d1 = Double.parseDouble(n1);
        Double d2 = Double.parseDouble(n2);
        return ((-LIMIT_0 < (d2 - d1)) && ((d2 - d1) < LIMIT_0));
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     判断类型是否是数字类型
     * @param   clazz 要判断的类型
     * @return  如果类型是 内部类型/Boolean等基本类型，返回真，否则返回假
     **/
    public static Boolean isDigitalClass(final Class<?> clazz){
        return (clazz.isPrimitive()
                || clazz.isAssignableFrom(Boolean.class)
                || clazz.isAssignableFrom(Byte.class)
                || clazz.isAssignableFrom(Character.class)
                || clazz.isAssignableFrom(Short.class)
                || clazz.isAssignableFrom(Integer.class)
                || clazz.isAssignableFrom(Long.class)
                || clazz.isAssignableFrom(Float.class)
                || clazz.isAssignableFrom(Double.class)
        );
    }

    /**
     * 描述     转换为long型数值
     * 日期     2018/7/31
     * @author  张成亮
     * @return  如果可以转换为long，返回转换后的long型数值，如果不能转换，在日志内记录一条警告后返回(long)0
     * @param   value 要转换的对象
     **/
    public static long parseLong(final Object value){
        if (ObjectUtils.isEmpty(value)) {
            return 0L;
        } else if (value.getClass().isPrimitive()) {
            if (value.getClass() == boolean.class) {
                return ((boolean)value) ? 1L : 0L;
            } else {
                return (long) value;
            }
        } else if (value instanceof Boolean) {
            return ((Boolean)value) ? 1L : 0L;
        } else {
            try {
                if ("true".equalsIgnoreCase(value.toString())) {
                    return 1L;
                } else if ("false".equalsIgnoreCase(value.toString())) {
                    return 0L;
                } else {
                    return Long.parseLong(value.toString());
                }
            } catch (NumberFormatException e) {
                log.warn("无法转换" + value.toString());
                return 0L;
            }
        }
    }

    /**
     * 描述     转换为boolean型数值
     * 日期     2018/7/31
     * @author  张成亮
     * @return  如果可以转换为boolean，返回转换后的boolean型数值，如果不能转换，在日志内记录一条警告后返回(boolean)0
     * @param   value 要转换的对象
     **/
    public static boolean parseBoolean(final Object value){
        if ((value != null) && (value.getClass() == boolean.class)) {
            return (boolean)value;
        }
        return (parseLong(value) != 0L);
    }

    /**
     * 描述     转换为char型数值
     * 日期     2018/7/31
     * @author  张成亮
     * @return  如果可以转换为char，返回转换后的char型数值，如果不能转换，在日志内记录一条警告后返回(char)0
     * @param   value 要转换的对象
     **/
    public static char parseChar(final Object value){
        if ((value != null) && (value.getClass() == char.class)) {
            return (char)value;
        }
        return (char)parseLong(value);
    }

    /**
     * 描述     转换为byte型数值
     * 日期     2018/7/31
     * @author  张成亮
     * @return  如果可以转换为byte，返回转换后的byte型数值，如果不能转换，在日志内记录一条警告后返回(byte)0
     * @param   value 要转换的对象
     **/
    public static byte parseByte(final Object value){
        if ((value != null) && (value.getClass() == byte.class)) {
            return (byte)value;
        }
        return (byte)parseLong(value);
    }

    /**
     * 描述     转换为short型数值
     * 日期     2018/7/31
     * @author  张成亮
     * @return  如果可以转换为short，返回转换后的short型数值，如果不能转换，在日志内记录一条警告后返回(short)0
     * @param   value 要转换的对象
     **/
    public static short parseShort(final Object value){
        if ((value != null) && (value.getClass() == short.class)) {
            return (short)value;
        }
        return (short)parseLong(value);
    }

    /**
     * 描述     转换为int型数值
     * 日期     2018/7/31
     * @author  张成亮
     * @return  如果可以转换为int，返回转换后的int型数值，如果不能转换，在日志内记录一条警告后返回(int)0
     * @param   value 要转换的对象
     **/
    public static int parseInt(final Object value){
        if ((value != null) && (value.getClass() == int.class)) {
            return (int)value;
        }
        return (int)parseLong(value);
    }

    /**
     * 描述     转换为double型数值
     * 日期     2018/7/31
     * @author  张成亮
     * @return  如果可以转换为double，返回转换后的double型数值，如果不能转换，在日志内记录一条警告后返回(double)0
     * @param   value 要转换的对象
     **/
    public static double parseDouble(final Object value){
        if (ObjectUtils.isEmpty(value)) {
            return (double)0;
        } else if (value.getClass().isPrimitive()) {
            if (value.getClass() == boolean.class) {
                return ((boolean)value) ? (double)1 : (double)0;
            } else {
                return (double) value;
            }
        } else if (value instanceof Boolean) {
            return ((Boolean)value) ? (double)1 : (double)0;
        } else {
            try {
                if ("true".equalsIgnoreCase(value.toString())) {
                    return (double)1;
                } else if ("false".equalsIgnoreCase(value.toString())) {
                    return (double)0;
                } else {
                    return Double.parseDouble(value.toString());
                }
            } catch (NumberFormatException e) {
                log.warn("无法转换" + value.toString());
                return (double)0;
            }
        }
    }

    /**
     * 描述     转换为float型数值
     * 日期     2018/7/31
     * @author  张成亮
     * @return  如果可以转换为float，返回转换后的float型数值，如果不能转换，在日志内记录一条警告后返回(float)0
     * @param   value 要转换的对象
     **/
    public static float parseFloat(final Object value) {
        if ((value != null) && (value.getClass() == float.class)) {
            return (float)value;
        }
        return (float)parseDouble(value);
    }
}
