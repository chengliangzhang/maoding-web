package com.maoding.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/31
 * @package: TraceUtils
 * @description : 跟踪日志类通用方法
 */
public class TraceUtils {
	/** 打印消息显示前缀 */
	private static final String PREFIX_INFO = "\t===>>>\t";
	/** 错误消息显示前缀 */
	private static final String PREFIX_ERROR = "\t!!!>>>\t";
	/** 内部错误显示前缀 */
	private static final String PREFIX_INTERNAL_ERROR = "\t!!!!!\t";

	/********* 可配置项起始 ****************/
    /** 是否打印进入退出信息 */
    private static boolean isLogEnterAndExitInfo = true;
    /** 是否打印调试信息 */
    private static boolean isLogDebugInfo = true;
    /** 是否检查断言条件 */
    private static boolean isCheckCondition = true;
    /** 是否抛出异常 */
    private static boolean isThrow = true;
    /** 使用字符串调用时是否产生异常 */
    private static boolean isThrowAuto = true;
    /** 是否在异常内增加调用位置 */
    private static boolean isShowCaller = true;
	/********* 可配置项结束 ****************/
	
    //内部保存的日志
    private static Map<String,Logger> loggerMap = null;

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     进入方法时打印日志
     * @param   obs 进入方法时要打印的变量
     * @return  当前时间
     **/
    public static long enter(Object... obs){
        if (isLogEnterAndExitInfo) {
            getLogger().info(PREFIX_INFO + "进入" + getCaller(null)
                    + ((obs != null) ? ":" + getJsonString(obs) : ""));
        }
        return System.currentTimeMillis();
    }

    public static long enter(){
        return enter((Object[]) null);
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     退出方法时打印日志
     * @param   t   系统当前时间
     * @param   obs 退出方法时要打印的变量
     **/
    public static void exit(long t, Object... obs){
        if (isLogEnterAndExitInfo) {
            getLogger().info(PREFIX_INFO + "退出" + getCaller(null)
                    + ((t > 0) ? ":" + (System.currentTimeMillis() - t) + "ms" : "")
                    + ((obs != null) ? "," + getJsonString(obs) : ""));
        }
    }

    public static void exit(Object... obs){
        exit(0,obs);
    }

    public static void exit(long t){
        exit(t,(Object[]) null);
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     打印一行日志信息
     * @param   t   系统当前时间
     * @param   obs 要打印的变量
     * @return  系统当前时间
     **/
    public static long info(String message, long t, Object... obs){
        if (isLogDebugInfo) {
            getLogger().info(PREFIX_INFO + getCaller(null)
                    + ":" + message
                    + ((t > 0) ?  "," + (System.currentTimeMillis() - t) + "ms" : "")
                    + ((obs != null) ? "," + getJsonString(obs) : ""));
        }
        return System.currentTimeMillis();
    }

    public static long info(String message, Object... obs){
        return info(message,0,obs);
    }

    public static long info(String message, long t){
        return info(message,t,(Object[]) null);
    }

    public static long info(String message){
        return info(message,0, (Object[]) null);
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     打印一行错误信息
     * @param   obs 要打印的变量
     * @return  系统当前时间
     **/
    public static void error(String message, boolean isError, Object... obs){
        if (isError) {
            getLogger().error(PREFIX_ERROR + getCaller(null) + "出错"
                    + ":" + message
                    + ((obs != null) ? "," + getJsonString(obs) : ""));
        } else {
            getLogger().warn(PREFIX_ERROR + getCaller(null) + "出错"
                    + ":" + message
                    + ((obs != null) ? "," + getJsonString(obs) : ""));
        }
    }

    public static void error(String message, Object... obs){
        error(message,true,obs);
    }

    public static void error(String message){
        error(message,true,(Object[]) null);
    }

    public static void warn(String message, Object... obs){
        error(message,false,obs);
    }

    public static void warn(String message){
        error(message,false,(Object[]) null);
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     检查断言条件，如果断言条件为假则打印日志，并且抛出异常
     * @param   condition   断言条件
     * @param   eClass 要抛出的异常的类型
     * @param   message 异常信息
     **/
    public static void check(boolean condition, Class<? extends RuntimeException> eClass, String message) {
        if (isCheckCondition && !(condition)) {
            //生成异常
            RuntimeException e = null;
            if (eClass != null) {
                try {
                    if (StringUtils.isNotEmpty(message)) {
                        Constructor<?> c = getConstructor(eClass, String.class);
                        e = eClass.cast(c.newInstance(isShowCaller ? message : message + ":" + getCaller(null)));
                    } else {
                        e = eClass.newInstance();
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    getLogger(TraceUtils.class).error(PREFIX_INTERNAL_ERROR + ex.getMessage(),ex);
                }
            }

            //记录日志并抛出异常
            if (e != null) {
                error(message);
                if (isThrow) {
                    throw e;
                }
            } else {
                warn(message);
            }
        }
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     检查断言条件，如果断言条件为假则打印日志，并且抛出异常
     * @param   condition   断言条件
     * @param   message 异常信息，如果以“!”起始，产生参数异常，否则如果不为空产生未知类型异常，为空不产生异常
     **/
    public static void check(boolean condition, String message) {
        if (isCheckCondition) {
            //参数检查错误前导字符
            final String prefixIllegalArgumentMessage = "!";
            //警告前导字符
            final String prefixIgnoreException = "~";
            if (StringUtils.startsWith(message, prefixIllegalArgumentMessage)) {
                check(condition, IllegalArgumentException.class, StringUtils.right(message,prefixIllegalArgumentMessage));
            } else if (StringUtils.startsWith(message, prefixIgnoreException)) {
                check(condition, null, StringUtils.right(message,prefixIgnoreException));
            } else if (isThrowAuto) {
                check(condition, IllegalStateException.class, message);
            } else {
                check(condition, null, message);
            }
        }
    }

    public static void check(boolean condition) {
        assert(condition);
    }

    //获取要打印的字符串
    private static String getJsonString(Object... obs){
        return StringUtils.toJsonString(obs).replaceAll("\\s","");
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

    public static boolean isIsThrowAuto() {
        return isThrowAuto;
    }

    public static void setIsThrowAuto(boolean isThrowAuto) {
        TraceUtils.isThrowAuto = isThrowAuto;
    }

    public static boolean isIsShowCaller() {
        return isShowCaller;
    }

    public static void setIsShowCaller(boolean isShowCaller) {
        TraceUtils.isShowCaller = isShowCaller;
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

    /**
     * 描述     获取日志对象
     * 日期     2018/8/16
     * @author  张成亮
     * @return  日志对象
     * @param   clazz 类名
     **/
    public static Logger getLogger(Class<?> clazz) {
        Logger log = null;
        if (loggerMap != null){
            log = loggerMap.get(clazz.getName());
        } else {
            loggerMap = new HashMap<>();
        }

        if (log == null){
            log = LoggerFactory.getLogger(clazz);
            loggerMap.put(clazz.getName(),log);
        }
        return log;
    }

    public static Logger getLogger() {
        return getLogger(getCallerClass());
    }


    /**
     * 描述     获取调用者信息，即堆栈中第一个不包含指定类名的类及方法
     * 日期     2018/8/14
     * @author  张成亮
     * @return  调用者信息
     * @param   excludeClassName 查找调用者信息的类的类名
     **/
    public static String getCaller(String excludeClassName){
        String caller = "";
        StackTraceElement[] stackArray = Thread.currentThread().getStackTrace();
        for (StackTraceElement theStack : stackArray) {
            if (isCaller(theStack.getClassName(), excludeClassName)) {
                caller = StringUtils.lastRight(theStack.getClassName(), ".")
                        + "." + theStack.getMethodName();
                break;
            }
        }
        return caller;
    }

    private static boolean isCaller(String className,String excludeClassName){
        return StringUtils.isNotSame(className,Thread.class.getName())
                && StringUtils.isNotSame(className,TraceUtils.class.getName())
                && (StringUtils.isEmpty(excludeClassName)
                        || (StringUtils.isNotEmpty(excludeClassName) &&
                                StringUtils.isNotSame(className,excludeClassName)));
    }

    /**
     * 描述     获取调用者的方法名信息，即堆栈中第一个不包含指定类名的类及方法名
     * 日期     2018/8/14
     * @author  张成亮
     * @return  调用者信息
     **/
    public static String getCaller(){
        return getCaller(getCallerClass().getName());
    }

    /**
     * 描述     获取调用者的类名信息，即堆栈中第一个不包含指定类名的类
     * 日期     2018/8/14
     * @author  张成亮
     * @return  调用者信息
     **/
    private static Class<?> getCallerClass(){
        Class<?> callerClass = null;
        StackTraceElement[] stackArray = Thread.currentThread().getStackTrace();
        for (StackTraceElement theStack : stackArray) {
            if (isCaller(theStack.getClassName(),null)) {
                try {
                    callerClass = Class.forName(theStack.getClassName());
                    break;
                } catch (ClassNotFoundException ex) {
                    getLogger(TraceUtils.class).error(PREFIX_INTERNAL_ERROR + ex.getMessage(),ex);
                }
            }
        }
        return callerClass;
    }
}
