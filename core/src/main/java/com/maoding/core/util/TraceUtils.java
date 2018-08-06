package com.maoding.core.util;

import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/31
 * @package: TraceUtils
 * @description : 跟踪日志类通用方法
 */
public class TraceUtils {
    /** 是否打印进入退出信息 */
    private static boolean isLogEnterAndExitInfo = true;
    /** 是否打印调试信息 */
    private static boolean isLogDebugInfo = true;
    /** 是否检查断言条件 */
    private static boolean isCheckCondition = true;
    /** 是否抛出异常 */
    private static boolean isThrow = true;

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     进入方法时打印日志
     * @param   log 调用日志的类所使用的日志对象
     * @param   obs 进入方法时要打印的变量
     * @return  当前时间
     **/
    public static long enter(Logger log, Object... obs){
        if (isLogEnterAndExitInfo) {
            log.info("\t===>>> 进入" + Thread.currentThread().getStackTrace()[2].getMethodName() + ":" + getJsonString(obs));
        }
        return System.currentTimeMillis();
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     退出方法时打印日志
     * @param   log 调用日志的类所使用的日志对象
     * @param   t   系统当前时间
     * @param   obs 退出方法时要打印的变量
     **/
    public static void exit(Logger log, long t, Object... obs){
        if (isLogEnterAndExitInfo) {
            log.info("\t<<<=== 退出" + Thread.currentThread().getStackTrace()[2].getMethodName() + ":" + (System.currentTimeMillis() - t) + "ms," + getJsonString(obs));
        }
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     打印一行日志信息
     * @param   log 调用日志的类所使用的日志对象
     * @param   t   系统当前时间
     * @param   obs 要打印的变量
     * @return  系统当前时间
     **/
    public static long info(Logger log, String message, long t, Object... obs){
        if (isLogDebugInfo) {
            log.info("\t===>>> " + message + ":" + (System.currentTimeMillis() - t) + "ms," + getJsonString(obs));
        }
        return System.currentTimeMillis();
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     检查断言条件，如果断言条件为假则打印日志，并且抛出异常
     * @param   condition   断言条件
     * @param   log 调用日志的类所使用的日志对象
     * @param   eClass 要抛出的异常的类型
     * @param   message 异常信息
     **/
    public static void check(boolean condition, Logger log, Class<? extends RuntimeException> eClass, String message) {
        if (isCheckCondition && !(condition)) {
            if (eClass != null) {
                try {
                    RuntimeException e;
                    if (StringUtils.isEmpty(message)) {
                        e = eClass.newInstance();
                    } else {
                        Constructor<?> c = getConstructor(eClass,String.class);
                        e = eClass.cast(c.newInstance(message));
                    }
                    if (e != null) {
                        log.error("\t!!!>>> " + Thread.currentThread().getStackTrace()[3].getMethodName() + ":" + e.getMessage());
                        if (isThrow) {
                            throw e;
                        }
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    log.error("\t!!!!! " + ex.getMessage());
                }
            } else {
                log.warn("\t!!!>>> " + Thread.currentThread().getStackTrace()[3].getMethodName() + "存在错误");
            }
        }
    }

    public static void check(boolean condition, Logger log) {
        check(condition,log,null,null);
    }

    public static void check(boolean condition) {
        assert(condition);
    }

    //获取要打印的字符串
    private static String getJsonString(Object... obs){
        return StringUtils.toJsonString(obs);
    }

    public static boolean isIsLogEnterAndExitInfo() {
        return isLogEnterAndExitInfo;
    }

    public static void setIsLogEnterAndExitInfo(boolean isLogEnterAndExitInfo) {
        TraceUtils.isLogEnterAndExitInfo = isLogEnterAndExitInfo;
    }

    public static boolean isIsLogDebugInfo() {
        return isLogDebugInfo;
    }

    public static void setIsLogDebugInfo(boolean isLogDebugInfo) {
        TraceUtils.isLogDebugInfo = isLogDebugInfo;
    }

    public static boolean isIsCheckCondition() {
        return isCheckCondition;
    }

    public static void setIsCheckCondition(boolean isCheckCondition) {
        TraceUtils.isCheckCondition = isCheckCondition;
    }

    public static boolean isIsThrow() {
        return isThrow;
    }

    public static void setIsThrow(boolean isThrow) {
        TraceUtils.isThrow = isThrow;
    }

    //获取以paramClassArray类型为参数的构造函数
    private static Constructor<?> getConstructor(Class<?> clazz,Class<?>... lookForParamClassArray){
        if (clazz == null){
            return null;
        }


        //参数个数
        int pCnt = (lookForParamClassArray == null) ? 0 : lookForParamClassArray.length;


        //查找构造函数
        Constructor<?> result = null;
        Constructor<?>[] constructorArray = clazz.getConstructors();
        for (Constructor<?> c : constructorArray) {
            if (c.getParameterCount() == pCnt) {
                if (lookForParamClassArray == null) {
                    result = c;
                    break;
                } else {
                    Class<?>[] paramArray = c.getParameterTypes();
                    boolean isMatch = true;
                    for (int i = 0; i < pCnt; i++) {
                        if (!paramArray[i].isAssignableFrom(lookForParamClassArray[i])) {
                            isMatch = false;
                            break;
                        }
                    }
                    if (isMatch) {
                        result = c;
                        break;
                    }
                }
            }
        }
        return result;
    }
}
