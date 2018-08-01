package com.maoding.core.util;

import org.slf4j.Logger;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/31
 * @package: TraceUtils
 * @description : 跟踪日志类通用方法
 */
public class TraceUtils {
    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     进入方法时打印日志
     * @param   log 调用日志的类所使用的日志对象
     * @param   obs 进入方法时要打印的变量
     * @return  当前时间
     **/
    public static long enter(Logger log, Object... obs){
        log.info("\t===>>> 进入" + Thread.currentThread().getStackTrace()[2].getMethodName() + ":" + getJsonString(obs));
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
        log.info("\t<<<=== 退出" + Thread.currentThread().getStackTrace()[2].getMethodName() + ":" + (System.currentTimeMillis() - t) + "ms," + getJsonString(obs));
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
        log.info("\t===>>> " + message + ":" + (System.currentTimeMillis() - t) + "ms," + getJsonString(obs));
        return System.currentTimeMillis();
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     检查断言条件，如果断言条件为假则打印日志，并且抛出异常
     * @param   log 调用日志的类所使用的日志对象
     * @param   condition   断言条件
     * @param   eClass 要抛出的异常的类型
     **/
    public static void check(boolean condition, Logger log, Class<? extends RuntimeException> eClass) {
        if (!(condition)) {
            if (eClass != null) {
                RuntimeException e = null;
                try {
                    e = eClass.newInstance();
                    log.error("\t!!!>>> " + Thread.currentThread().getStackTrace()[2].getMethodName() + ":" + e.getMessage());
                } catch (InstantiationException | IllegalAccessException ex) {
                    log.error("\t!!!!! " + ex.getMessage());
                }
                if (e != null) {
                    throw e;
                }
            } else {
                log.warn("\t!!!>>> " + Thread.currentThread().getStackTrace()[2].getMethodName() + "存在错误");
            }
        }
    }

    //获取要打印的字符串
    private static String getJsonString(Object... obs){
        return StringUtils.toJsonString(obs);
    }
}
