package com.maoding.core.util;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Created by Wuwq on 2016/11/14.
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

    /**
     * 注解到对象复制，只复制能匹配上的方法。
     * @param annotation
     * @param object
     */
    public static void annotationToObject(Object annotation, Object object){
        if (annotation != null){
            Class<?> annotationClass = annotation.getClass();
            Class<?> objectClass = object.getClass();
            for (Method m : objectClass.getMethods()){
                if (StringUtils.startsWith(m.getName(), "set")){
                    try {
                        String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
                        Object obj = annotationClass.getMethod(s).invoke(annotation);
                        if (obj != null && !"".equals(obj.toString())){
                            if (object == null){
                                object = objectClass.newInstance();
                            }
                            m.invoke(object, obj);
                        }
                    } catch (Exception e) {
                        // 忽略所有设置失败方法
                    }
                }
            }
        }
    }

    /**
     * 序列化对象
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            if (object != null){
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                return baos.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反序列化对象
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            if (bytes != null && bytes.length > 0){
                bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     判断对象是否为空
     * @param   object 要判断的对象
     * @return  如果对象为null/空数组/空列表/空字符串，返回真，否则返回假
     **/
    public static boolean isEmpty(Object object){
        if (object == null) {
            return true;
        } else if (object.getClass().isArray()) {
            return (Array.getLength(object)) <= 0;
        } else if (object instanceof Collection) {
            return (((Collection) object).size() <= 0);
        } else {
            return (object instanceof String) && (StringUtils.isEmpty((String) object));
        }
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     判断对象是否为空
     * @param   object 要判断的对象
     * @return  如果对象为null/空数组/空列表/空字符串，返回假，否则返回真
     **/
    public static boolean isNotEmpty(Object object){
        return !isEmpty(object);
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     获取列表内第一项
     * @param   list 输入的列表
     * @return  如果列表为空，返回null，否则返回列表内第一项元素
     **/
    public static <T> T getFirst(List<T> list){
        if (isNotEmpty(list)){
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * @author  张成亮
     * @date    2018/7/31
     * @description     判断输入类型是否是基本类型
     * @param   clazz 输入类型
     * @return  如果输入类型是 内部类型/数字类型/字符串/数组，返回真，否则返回假
     **/
    public static boolean isBasicType(final Class<?> clazz){
        return (clazz.isPrimitive()) ||
                (DigitUtils.isDigitalClass(clazz)) ||
                (clazz.isAssignableFrom(String.class)) ||
                (clazz.isArray());
    }
}