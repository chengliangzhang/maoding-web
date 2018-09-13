package com.maoding.core.util;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/31
 * @package: BeanUtils
 * @description : 通用POJO操作，包括复制属性、创建POJO等操作
 */
public final class BeanUtils extends org.springframework.beans.BeanUtils{
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);

    /** get方法前缀 */
    private static final String GET = "get";
    /** set方法前缀 */
    private static final String SET = "set";
    /** 点分隔符 */
    private static final String DOT = ".";
    /** 逗号分隔符 */
    private static final String COMMA = ",";
    /** 默认关键字属性 */
    private static final String DEFAULT_KEY_NAME = "Id";

    /** 类get/set方法缓存 */
    private static Map<Class, MethodAccess> methodMap = new HashMap<>();
    /** 类get/set方法在对象接口定义内的序号缓存 */
    private static Map<String, Integer> methodIndexMap = new HashMap<>();
    /** 类接口定义缓存 */
    private static Map<Class, List<String>> fieldMap = new HashMap<>();

    /**
     * 描述     缓存类的get/set方法
     * 日期     2018/7/31
     * @author  张成亮
     **/
    private static synchronized MethodAccess cache(Class<?> clazz) {
        MethodAccess methodAccess = MethodAccess.get(clazz);
        List<Field> fieldList = new ArrayList<>();
        List<Method> methodList = new ArrayList<>();
        for (Class<?> c=clazz; c != Object.class; c = c.getSuperclass()){
            Field[] fields = c.getDeclaredFields();
            Collections.addAll(fieldList, fields);
            Method[] methods = c.getDeclaredMethods();
            Collections.addAll(methodList, methods);
        }

        if ((fieldList.size() > 0) && (methodList.size() > 0)) {
            List<String> fieldNameList = new ArrayList<>(fieldList.size());
            for (Field field : fieldList) {
                if (!Modifier.isStatic(field.getModifiers())) { //是否是静态的
                    String fieldName = StringUtils.capitalize(field.getName()); // 获取属性名称
                    String getKey = GET + fieldName;
                    String setKey = SET + fieldName;
                    int n = 0;
                    for (Method method : methodList) {
                        if (StringUtils.isSame(method.getName(),getKey) && Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 0){
                            int getIndex = methodAccess.getIndex(getKey,0); // 获取get方法的下标
                            methodIndexMap.put(clazz.getName() + DOT + getKey, getIndex); // 将类名get方法名，方法下标注册到map中
                            if (++n > 2) break;
                        } else if (StringUtils.isSame(method.getName(),setKey) && Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 1) {
                            int setIndex = methodAccess.getIndex(setKey,method.getParameterTypes()); // 获取set方法的下标
                            methodIndexMap.put(clazz.getName() + DOT + setKey, setIndex); // 将类名set方法名，方法下标注册到map中
                            if (++n > 2) break;
                        }
                    }
                    fieldNameList.add(fieldName); // 将属性名称放入集合里
                }
            }
            fieldMap.put(clazz, fieldNameList); // 将类名列表放入到map中
        }

        assert(methodAccess != null);
        methodMap.put(clazz, methodAccess); // 将类的方法列表放入到map中
        return methodAccess;
    }

    /**
     * 描述     根据对象的属性创建Map,如果isClean为真，值为空的属性不添加到map内
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <K,V> Map<K,V> createMapFrom(final Object input,Class<? extends K> keyClass,Class<? extends V> valClass,boolean isClean){
        assert (keyClass != null) && (!keyClass.isPrimitive()) && (!keyClass.isArray());
        assert (valClass != null) && (!valClass.isPrimitive()) && (!valClass.isArray());
        if (input == null) return null;

        MethodAccess inputMethodAccess = methodMap.get(input.getClass());
        if (inputMethodAccess == null) inputMethodAccess = cache(input.getClass());

        Map<K,V> output = new HashMap<>();

        List<String> fieldList = fieldMap.get(input.getClass());
        if (fieldList != null) {
            for (String field : fieldList) {
                String getKey = input.getClass().getName() + DOT + GET + field;
                Integer getIndex = methodIndexMap.get(getKey);
                if (getIndex != null){
                    V val = createFrom(inputMethodAccess.invoke(input, getIndex),valClass,isClean);
                    if (val != null) {
                        K key = createFrom(field,keyClass,false);
                        output.put(key,val);
                    }
                }
            }
        }
        return output;
    }

    /**
     * 描述     根据对象的属性创建Map,键为keyClass类型，值为valClass类型
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <K,V> Map<K,V> createMapFrom(final Object input,Class<? extends K> keyClass,Class<? extends V> valClass){
        return createMapFrom(input,keyClass,valClass,false);
    }

    /**
     * 描述     根据对象的属性创建Map,键为String类型，值为Object类型，如果isClean为真，值为空的属性不放入Map内
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static Map<String,Object> createMapFrom(final Object input,boolean isClean){
        return createMapFrom(input,String.class,Object.class,isClean);
    }

    /**
     * 描述     根据对象的属性创建Map,键为String类型，值为Object类型，值为空的属性也要放入Map内
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static Map<String,Object> createMapFrom(final Object input){
        return createMapFrom(input,false);
    }

    /**
     * 描述     根据对象的属性创建Map,键为keyClass类型，值为valClass类型，值为空的属性不放入Map内
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <K,V> Map<K,V> createCleanMapFrom(final Object input,Class<? extends K> keyClass,Class<? extends V> valClass){
        return createMapFrom(input,keyClass,valClass,true);
    }

    /**根据对象的属性创建Map,键为String类型，值为Object类型，值为空的属性不放入Map内
     * 描述
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static Map<String,Object> createCleanMapFrom(final Object input){
        return createMapFrom(input,true);
    }

    /**
     * 描述     根据Json字符串创建对象
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <T> T createFromJson(@NotNull String input, Class<? extends T> outClass){
        ObjectMapper mapper = StringUtils.getObjectMapper();
        try {
            return mapper.readValue(input, outClass);
        } catch (IOException e) {
            log.error(getErrorMessage(input,true));
            throw new UnsupportedOperationException(getErrorMessage(input,false));
        }
    }

    /**
     * 描述     根据对象创建Json字符串
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static String createJsonFrom(@NotNull Object input){
        return StringUtils.toJsonString(input);
    }

    //获取错误字符串
    private static String getErrorMessage(@NotNull String json, boolean isAddCaller){
        String msg = "转换" + json + "时出现错误";
        if (isAddCaller){
            msg += ":" + TraceUtils.getCaller();
        }
        return msg;
    }

    /**
     * 描述     把Map内的值复制到对象的相应属性上，如果isClean为真，值为空的属性不复制
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <K,V> void copyProperties(final Map<K,V> input, Object output, boolean isClean) {
        assert (output != null) && (!output.getClass().isPrimitive()) && (!output.getClass().isArray());
        if ((input == null) || (input.getClass().isPrimitive()) || input.getClass().isArray()) return;

        MethodAccess outputMethodAccess = methodMap.get(output.getClass());
        if (outputMethodAccess == null) outputMethodAccess = cache(output.getClass());

        List<String> fieldList = fieldMap.get(output.getClass());
        if (fieldList != null) {
            assert (outputMethodAccess != null);
            Class[][] outputParameterTypes = outputMethodAccess.getParameterTypes();
            for (K k : input.keySet()) {
                Object val = input.get(k);
                if (val != null) {
                    String setKey = output.getClass().getName() + DOT + SET + StringUtils.capitalize(k.toString());
                    Integer setIndex = methodIndexMap.get(setKey);
                    if (setIndex != null) {
                        assert (outputParameterTypes != null);
                        Class<?> outputFieldClass = outputParameterTypes[setIndex][0];
                        callSetMethod(outputMethodAccess, output, setIndex, outputFieldClass, val, isClean);
                    }
                }
            }
        }
    }

    /**
     * 描述     把输入对象的属性复制到输出数组的元素的相应属性上
     *          如果输入对象是POJO，复制到输出数组的第一个元素上
     *          如果输入对象是list或数组，依次复制对应的元素
     *          如果isClean为真，值为空的属性不复制
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static void copyProperties(final Object input, Object[] output, boolean isClean) {
        int n = Array.getLength(output);
        if (input.getClass().isArray()){
            int m = Array.getLength(input);
            if (n > m) n = m;
            for (int i=0; i<n; i++){
                copyProperties(Array.get(input,i),Array.get(output,i),isClean);
            }
        } else if (input instanceof List){
            List<?> inputList = (List<?>) input;
            int i = 0;
            for (Object inputElement : inputList){
                if (inputElement == null) continue;
                copyProperties(inputElement,Array.get(output,i),isClean);
                if (++i >= n) break;
            }
        } else {
            copyProperties(input,Array.get(output,0),isClean);
        }
    }

    /**
     * 复制Bean属性到Map
     */
    public static void copyProperties(final Object input, final Map<String, Object> output) {
        if (input == null || output == null)
            return;

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(input.getClass(), Object.class);
            PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor pty : properties) {
                Object val = pty.getReadMethod().invoke(input);
                if (val != null) {
                    output.put(pty.getName(),val);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 描述     复制输入对象的属性到输出对象上
     *         如果isClean为真，值为空的属性不复制
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static void copyProperties(final Object input, Object output, boolean isClean) {
        assert (output != null) && (!output.getClass().isPrimitive());
        if (input == null) return;

        if (output.getClass().isArray()) {
            copyProperties(input,(Object[])output,isClean);
        } else if (input instanceof Map) {
            copyProperties((Map<?, ?>) input, output, isClean);
        } else if (input.getClass().isArray()) {
            copyProperties(Array.get(input,0),output,isClean);
        } else {
            MethodAccess outputMethodAccess = methodMap.get(output.getClass());
            if (outputMethodAccess == null) outputMethodAccess = cache(output.getClass());
            MethodAccess inputMethodAccess = methodMap.get(input.getClass());
            if (inputMethodAccess == null) inputMethodAccess = cache(input.getClass());

            List<String> fieldList = fieldMap.get(input.getClass());
            if (fieldList != null) {
                assert (outputMethodAccess != null);
                Class[][] outputParameterTypes = outputMethodAccess.getParameterTypes();
                for (String field : fieldList) {
                    String getKey = input.getClass().getName() + DOT + GET + field;
                    Integer getIndex = methodIndexMap.get(getKey);
                    if (getIndex != null) {
                        String setKey = output.getClass().getName() + DOT + SET + field;
                        Integer setIndex = methodIndexMap.get(setKey);
                        if (setIndex != null) {
                            //获取设置属性方法的参数类型
                            assert (outputParameterTypes != null);
                            Class<?> outputFieldClass = outputParameterTypes[setIndex][0];
                            //设置属性
                            callSetMethod(outputMethodAccess, output, setIndex, outputFieldClass, inputMethodAccess.invoke(input, getIndex), isClean);
                        }
                    }
                }
            }
        }
    }

    /**
     * 描述     复制输入对象的属性到输出对象上,不判断属性是否为空
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static void copyProperties(final Object input, Object output) {
        copyProperties(input,output,false);
    }

    /**
     * 描述     复制输入对象的属性到输出对象上,属性为空不复制
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static void copyCleanProperties(final Object input, Object output){
        copyProperties(input,output,true);
    }

    /**
     * 描述     设置输出对象的某属性值
     * 日期     2018/7/31
     * @author  张成亮
     **/
    private static void callSetMethod(MethodAccess outputMethodAccess,Object output,Integer setIndex,Class<?> outputFieldClass,Object inputValue,boolean isClean){
        //如果输入为空，不做处理
        if (inputValue != null) {
            if (outputFieldClass.isAssignableFrom(inputValue.getClass())) {
                if ((!isClean) || ObjectUtils.isNotEmpty(inputValue)) { //如果isClean为真，仅设置非空值
                    outputMethodAccess.invoke(output, setIndex, inputValue);
                }
            } else if (outputFieldClass == boolean.class) {
                outputMethodAccess.invoke(output, setIndex, DigitUtils.parseBoolean(inputValue));
            } else if (outputFieldClass == char.class) {
                outputMethodAccess.invoke(output,setIndex,DigitUtils.parseChar(inputValue));
            } else if (outputFieldClass == byte.class) {
                outputMethodAccess.invoke(output,setIndex,DigitUtils.parseByte(inputValue));
            } else if (outputFieldClass == short.class) {
                outputMethodAccess.invoke(output,setIndex,DigitUtils.parseShort(inputValue));
            } else if (outputFieldClass == int.class) {
                outputMethodAccess.invoke(output,setIndex,DigitUtils.parseInt(inputValue));
            } else if (outputFieldClass == long.class) {
                outputMethodAccess.invoke(output,setIndex,DigitUtils.parseLong(inputValue));
            } else if (outputFieldClass == float.class) {
                outputMethodAccess.invoke(output,setIndex,DigitUtils.parseFloat(inputValue));
            } else if (outputFieldClass == double.class) {
                outputMethodAccess.invoke(output,setIndex,DigitUtils.parseDouble(inputValue));
            } else {
                outputMethodAccess.invoke(output,setIndex,createFrom(inputValue,outputFieldClass,isClean));
            }
        }
    }

    /**
     * 描述     根据某输入对象创建类型为outputClass的输出对象
     *          如果isClean为真，值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <D> D createFrom(Object input,Class<? extends D> outputClass,boolean isClean){
        assert ((outputClass != null) && (!outputClass.isPrimitive()));
        if (input == null) {
            return null;
        } else if (outputClass.isInstance(input)) {
            D output = outputClass.cast(input);
            return (isClean) ? cleanProperties(output) : output;
        } else if (outputClass.isArray()) {
            Class<?> elementClass = outputClass.getComponentType();
            if (input.getClass().isArray() && elementClass.isAssignableFrom(input.getClass().getComponentType())) {
                return outputClass.cast((isClean) ? cleanProperties(input) : input);
            } else {
                return outputClass.cast(createArrayObjectFrom(input, outputClass.getComponentType(), isClean));
            }
        } else if (outputClass.isAssignableFrom(List.class)) {
            return outputClass.cast(createListFrom(input,Object.class,isClean));
        } else if (input instanceof Map) {
            D output = constructNull(outputClass);
            copyProperties(input, output, isClean);
            return output;
        } else {
            return constructFrom(input,outputClass, isClean);
        }
    }

    /**
     * 描述     根据某输入对象创建类型为outputClass的输出对象
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <D> D createFrom(Object input, Class<? extends D> outputClass){
        return createFrom(input,outputClass,false);
    }

    /**
     * 描述     根据某输入对象创建类型为outputClass的输出对象
     *          值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <D> D createCleanFrom(Object input, Class<? extends D> outputClass){
        return createFrom(input,outputClass,true);
    }

    /**
     * 描述     根据某输入对象创建元素类型为elementClass的输出对象序列
     *          如果isClean为真，值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <D> List<D> createListFrom(Object input, Class<? extends D> elementClass,boolean isClean){
        assert (elementClass != null) && (!elementClass.isPrimitive());
        List<D> outputList = null;
        if (input != null) {
            if (input.getClass().isArray()) {
                outputList = new ArrayList<>();
                for (int i = 0; i < Array.getLength(input); i++) {
                    outputList.add(createFrom(Array.get(input,i),elementClass,isClean));
                }
            } else if (input instanceof List) {
                outputList = new ArrayList<>();
                List<?> inputList = (List<?>) input;
                for (Object inputElement : inputList) {
                    if (inputElement == null) continue;
                    if (elementClass.isAssignableFrom(inputElement.getClass())) {
                        outputList.add(elementClass.cast((isClean) ? cleanProperties(inputElement) : inputElement));
                    } else {
                        outputList.add(createFrom(inputElement,elementClass,isClean));
                    }
                }
            } else if (elementClass.isInstance(input)) {
                outputList = new ArrayList<>();
                outputList.add(elementClass.cast((isClean) ? cleanProperties(input) : input));
            } else if ((input instanceof String) && (((String) input).contains(","))) {
                String[] inputArray = ((String) input).split(",");
                outputList = createListFrom(inputArray, elementClass);
            } else {
                outputList = new ArrayList<>();
                outputList.add(createFrom(input,elementClass,isClean));
            }
        }
        return outputList;
    }


    /**
     * 描述     根据某输入对象创建元素类型为elementClass的输出对象序列
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <D> List<D> createListFrom(Object input, Class<? extends D> elementClass){
        return createListFrom(input,elementClass,false);
    }

    /**
     * 描述     根据某输入对象创建元素类型为elementClass的输出对象序列
     *          值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <D> List<D> createCleanListFrom(Object input, Class<? extends D> elementClass){
        return createListFrom(input,elementClass,true);
    }

    /**
     * 描述     根据某输入对象创建元素类型为elementClass的输出对象数组
     *          如果isClean为真，值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    @SuppressWarnings("unchecked")
    public static <D> D[] createArrayFrom(Object input, Class<? extends D> elementClass,boolean isClean){
        assert (elementClass != null) && (!elementClass.isPrimitive());
        D[] outputArray = null;
        if (input != null){
            if (input.getClass().isArray()) {
                outputArray = (D[])Array.newInstance(elementClass,Array.getLength(input));
                for (int i=0; i<Array.getLength(input); i++){
                    outputArray[i] = createFrom(Array.get(input,i),elementClass,isClean);
                }
            } else if (elementClass.isInstance(input)) {
                outputArray = (D[]) Array.newInstance(elementClass, 1);
                outputArray[0] = elementClass.cast((isClean) ? cleanProperties(input) : input);
            } else if ((input instanceof String) && ((String) input).contains(COMMA)) {
                String[] inputArray = ((String) input).split(COMMA);
                outputArray = createArrayFrom(inputArray, elementClass, isClean);
            } else {
                outputArray = (D[]) Array.newInstance(elementClass, 1);
                outputArray[0] = createFrom(input,elementClass,isClean);
            }
        }
        return outputArray;
    }

    /**
     * 描述     根据某输入对象创建元素类型为elementClass的数组对象
     *          如果isClean为真，值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static Object createArrayObjectFrom(Object input,Class<?> elementClass,boolean isClean){
        assert (elementClass != null);
        if (elementClass == boolean.class) return createBooleanArrayFrom(input,isClean);
        else if (elementClass == char.class) return createCharArrayFrom(input,isClean);
        else if (elementClass == byte.class) return createByteArrayFrom(input,isClean);
        else if (elementClass == short.class) return createShortArrayFrom(input,isClean);
        else if (elementClass == int.class) return createIntArrayFrom(input,isClean);
        else if (elementClass == long.class) return createLongArrayFrom(input,isClean);
        else if (elementClass == float.class) return createFloatArrayFrom(input,isClean);
        else if (elementClass == double.class) return createDoubleArrayFrom(input,isClean);
        else return createArrayFrom(input,elementClass,isClean);
    }

    /**
     * 描述     根据某输入对象创建元素类型为boolean的输出对象数组
     *          如果isClean为真，值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static boolean[] createBooleanArrayFrom(Object input,boolean isClean){
        Boolean[] tmp = createArrayFrom(input,Boolean.class,isClean);
        boolean[] output = null;
        if (tmp != null){
            output = new boolean[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : false;
            }
        }
        return output;
    }

    /**
     * 描述     根据某输入对象创建元素类型为char的输出对象数组
     *          如果isClean为真，值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static char[] createCharArrayFrom(Object input,boolean isClean){
        Character[] tmp = createArrayFrom(input,Character.class,isClean);
        char[] output = null;
        if (tmp != null){
            output = new char[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : (char)0;
            }
        }
        return output;
    }

    /**
     * 描述     根据某输入对象创建元素类型为byte的输出对象数组
     *          如果isClean为真，值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static byte[] createByteArrayFrom(Object input,boolean isClean){
        Byte[] tmp = createArrayFrom(input,Byte.class,isClean);
        byte[] output = null;
        if (tmp != null){
            output = new byte[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : (byte)0;
            }
        }
        return output;
    }

    /**
     * 描述     根据某输入对象创建元素类型为boolean的输出对象数组
     *          如果isClean为真，值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static short[] createShortArrayFrom(Object input,boolean isClean){
        Short[] tmp = createArrayFrom(input,Short.class,isClean);
        short[] output = null;
        if (tmp != null){
            output = new short[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : (short)0;
            }
        }
        return output;
    }

    /**
     * 描述     根据某输入对象创建元素类型为int的输出对象数组
     *          如果isClean为真，值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static int[] createIntArrayFrom(Object input,boolean isClean){
        Integer[] tmp = createArrayFrom(input,Integer.class,isClean);
        int[] output = null;
        if (tmp != null){
            output = new int[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : (int)0;
            }
        }
        return output;
    }

    /**
     * 描述     根据某输入对象创建元素类型为long的输出对象数组
     *          如果isClean为真，值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static long[] createLongArrayFrom(Object input,boolean isClean){
        Long[] tmp = createArrayFrom(input,Long.class,isClean);
        long[] output = null;
        if (tmp != null){
            output = new long[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : (long)0;
            }
        }
        return output;
    }

    /**
     * 描述     根据某输入对象创建元素类型为float的输出对象数组
     *          如果isClean为真，值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static float[] createFloatArrayFrom(Object input,boolean isClean){
        Float[] tmp = createArrayFrom(input,Float.class,isClean);
        float[] output = null;
        if (tmp != null){
            output = new float[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : (float)0;
            }
        }
        return output;
    }

    /**
     * 描述     根据某输入对象创建元素类型为double的输出对象数组
     *          如果isClean为真，值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static double[] createDoubleArrayFrom(Object input,boolean isClean){
        Double[] tmp = createArrayFrom(input,Double.class,isClean);
        double[] output = null;
        if (tmp != null){
            output = new double[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : (double)0;
            }
        }
        return output;
    }

    /**
     * 描述     通过outputClass的构造函数创建输出对象，并复制输入对象属性
     *          如果isClean为真，值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <D> D constructFrom(Object input,Class<? extends D> outputClass, boolean isClean){
        assert (outputClass != null) && (!outputClass.isPrimitive()) && (!outputClass.isArray());
        if (input == null) {
            return (isClean) ? null : constructNull(outputClass);
        }

        //使用以input、string为参数构造
        D output = null;

        //如果有以input类型为参数的构造函数，直接调用
        Constructor<?>[] constructors = outputClass.getConstructors();
        Constructor<?> stringConstructor = null;
        Class<?>[] classes = input.getClass().getClasses();
        Boolean found = false;
        for (Constructor<?> c : constructors) {
            if (c.getParameterCount() == 1) {
                Class<?> ptype = (c.getParameterTypes())[0];
                if (!found) {
                    for (Class<?> ic : classes) {
                        if (ptype == ic) {
                            try {
                                output = outputClass.cast(c.newInstance(input));
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                                log.warn(outputClass.getName() + "没有以" + input.getClass().getName() + "为参数的构造方法");
                                output = null;
                            }
                            found = true;
                            break;
                        }
                    }
                }
                if ((stringConstructor == null) && (ptype == String.class)) stringConstructor = c;
                if ((found) && (stringConstructor != null)) break;
            }
        }

        //如果outputClass是基本数字类型，使用基本数字类型调用构造函数
        if ((output == null) && (DigitUtils.isDigitalClass(outputClass))) {
            if (outputClass.isAssignableFrom(Boolean.class)) {
                output = outputClass.cast(DigitUtils.parseBoolean(input));
            } else if (outputClass.isAssignableFrom(Character.class)) {
                output = outputClass.cast(DigitUtils.parseChar(input));
            } else if (outputClass.isAssignableFrom(Byte.class)) {
                output = outputClass.cast(DigitUtils.parseByte(input));
            } else if (outputClass.isAssignableFrom(Short.class)) {
                output = outputClass.cast(DigitUtils.parseShort(input));
            } else if (outputClass.isAssignableFrom(Integer.class)) {
                output = outputClass.cast(DigitUtils.parseInt(input));
            } else if (outputClass.isAssignableFrom(Long.class)) {
                output = outputClass.cast(DigitUtils.parseLong(input));
            } else if (outputClass.isAssignableFrom(Float.class)) {
                output = outputClass.cast(DigitUtils.parseFloat(input));
            } else if (outputClass.isAssignableFrom(Double.class)) {
                output = outputClass.cast(DigitUtils.parseDouble(input));
            } else {
                output = outputClass.cast(0);
            }
        }

        //如果outputClass有以String为参数的构造函数，使用字符串调用构造函数
        if ((output == null) && (stringConstructor != null)){
            try {
                output = outputClass.cast(stringConstructor.newInstance(input.toString()));
            } catch ( InstantiationException | IllegalAccessException | InvocationTargetException e) {
                log.warn(outputClass.getName() + "不能以" + input.toString() + "为参数调用构造方法");
                output = null;
            }
        }

        //其他情况，先构造一个空对象，再复制属性
        if (output == null){
            output = constructNull(outputClass);
            copyProperties(input,output,isClean);
        }

        return output;
    }

    /**
     * 描述     通过outputClass的构造函数创建输出对象，并复制输入对象属性
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <D> D constructFrom(Object input,Class<? extends D> outputClass){
        return constructFrom(input,outputClass,false);
    }

    /**
     * 描述     通过outputClass的构造函数创建输出对象，并复制输入对象属性
     *          值为空的属性不进行设置
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <D> D cleanConstructFrom(Object input,Class<? extends D> outputClass){
        return constructFrom(input,outputClass,true);
    }

    public static <D> D constructNull(Class<? extends D> outputClass){
        assert (outputClass != null) && (!outputClass.isPrimitive()) && (!outputClass.isArray());
        try {
            return outputClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.warn("无法创建" + outputClass.getName() + "对象");
        }
        return null;
    }

    /**
     * 清理Bean内为空字符串的属性，将其替换为null
     */
    public static <T> T cleanProperties(T obj){
        if (ObjectUtils.isEmpty(obj)) {
            return null;
        } else if (ObjectUtils.isBasicType(obj.getClass())) {
            return obj;
        }

        MethodAccess objMethodAccess = methodMap.get(obj.getClass());
        if (objMethodAccess == null) objMethodAccess = cache(obj.getClass());

        boolean isValid = false;
        List<String> fieldList = fieldMap.get(obj.getClass());
        if (fieldList != null) {
            assert (objMethodAccess != null);
            Class[][] objParameterTypes = objMethodAccess.getParameterTypes();
            boolean foundPty = false;
            for (String field : fieldList) {
                String getKey = obj.getClass().getName() + DOT + GET + field;
                String setKey = obj.getClass().getName() + DOT + SET + field;
                Integer getIndex = methodIndexMap.get(getKey);
                Integer setIndex = methodIndexMap.get(setKey);
                if ((setIndex != null) && (getIndex != null)) {
                    foundPty = true;
                    Class<?> fieldClass = objParameterTypes[setIndex][0];
                    if (fieldClass.isPrimitive()) {
                        isValid = true;
                    } else {
                        if (ObjectUtils.isEmpty(objMethodAccess.invoke(obj,getIndex))) {
                            objMethodAccess.invoke(obj, setIndex, fieldClass.cast(null));
                        } else if (cleanProperties(objMethodAccess.invoke(obj,getIndex)) == null){
                            objMethodAccess.invoke(obj, setIndex, fieldClass.cast(null));
                        } else {
                            isValid = true;
                        }
                    }
                }
            }
            if (!foundPty) {
                isValid = true;
            }
        }
        return (isValid) ? obj : null;
    }

    /**
     * 描述     清理Map内值为空的值
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <K,V> Map<K,V> cleanProperties(Map<K,V> obj){
        for (K k : obj.keySet()) {
            V v = obj.get(k);
            if (ObjectUtils.isEmpty(v)){
                obj.remove(k);
            }
        }
        return obj;
    }

    /**
     * 描述     获取输入对象内的属性，如果isClean为真，且属性为空，返回null
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <T,K> K getProperty(T obj,String ptyName,Class<? extends K> ptyClass,boolean isClean){
        if ((ObjectUtils.isEmpty(obj)) || (ObjectUtils.isBasicType(obj.getClass())) || (StringUtils.isEmpty(ptyName))){
            return null;
        }

        MethodAccess objMethodAccess = methodMap.get(obj.getClass());
        if (objMethodAccess == null) objMethodAccess = cache(obj.getClass());

        String getKey = obj.getClass().getName() + DOT + GET + StringUtils.capitalize(ptyName);
        Integer getIndex = methodIndexMap.get(getKey);
        if ((objMethodAccess == null) || (getIndex == null)){
            return null;
        }

        if (isClean) {
            return ptyClass.cast(cleanProperties(objMethodAccess.invoke(obj, getIndex)));
        } else {
            return ptyClass.cast(objMethodAccess.invoke(obj, getIndex));
        }
    }

    public static <T,K> K getProperty(T obj,String ptyName,Class<? extends K> ptyClass){
        return getProperty(obj,ptyName,ptyClass,false);
    }

    public static <T> Object getProperty(T obj, String ptyName) {
        return getProperty(obj,ptyName,Object.class);
    }

    /**
     * 描述     设置输入对象内的属性
     * 日期     2018/7/31
     * @author  张成亮
     **/
    public static <T,K> void setProperty(T obj,String ptyName,K ptyValue){
        if ((obj != null) && (!ObjectUtils.isBasicType(obj.getClass())) && (StringUtils.isNotEmpty(ptyName))){
            Class<?> objClass = obj.getClass();
            MethodAccess objMethodAccess = methodMap.get(obj.getClass());
            if (objMethodAccess == null) {
                objMethodAccess = cache(objClass);
            }

            if (objMethodAccess != null){
                String setKey = objClass.getName() + DOT + SET + StringUtils.capitalize(ptyName);
                Integer setIndex = methodIndexMap.get(setKey);
                if (setIndex != null){
                    Class[][] paramTypes = objMethodAccess.getParameterTypes();
                    Class<?> paramType = paramTypes[setIndex][0];
                    if (paramType != null) {
                        objMethodAccess.invoke(obj, setIndex, createFrom(ptyValue,paramType));
                    }
                }
            }
        }
    }

    /**
     * 根据entityList复制dtoList
     */
    public static List copyFields(List sourceList, Class destClass) throws Exception {
        List destList = new ArrayList();
        for (Object source : sourceList) {
            Object dest = destClass.newInstance();
            copyProperties(source, dest);
            destList.add(dest);
        }
        return destList;
    }

    public static <T> String getId(T obj){
        return getProperty(obj,DEFAULT_KEY_NAME,String.class,false);
    }

    /**
     * 描述       查看是否有指定属性
     * 日期       2018/9/10
     * @author   张成亮
     **/
    public static boolean hasField(Class<?> objClass,String ptyName,Class<?> fieldClass){
        boolean error = false;

        if ((objClass == null) || (ObjectUtils.isBasicType(objClass)) || (StringUtils.isEmpty(ptyName))){
            error = true;
        }

        MethodAccess objMethodAccess = null;
        if (!error) {
            objMethodAccess = methodMap.get(objClass);
            if (objMethodAccess == null) {
                objMethodAccess = cache(objClass);
            }
            if (objMethodAccess == null){
                error = true;
            }
        }

        if (!error) {
            String getKey = objClass.getName() + DOT + GET + StringUtils.capitalize(ptyName);
            Integer getIndex = methodIndexMap.get(getKey);
            if (getIndex == null){
                error = true;
            } else if (fieldClass != null){
                Class[] returnTypes = objMethodAccess.getReturnTypes();
                Class<?> returnType = returnTypes[getIndex];
                if ((returnType == null) || !(returnType.isAssignableFrom(fieldClass))){
                    error = true;
                }
            }
        }

        return !error;
    }

    public static boolean hasField(Class<?> objClass,String ptyName){
        return hasField(objClass,ptyName,null);
    }
}
