package com.maoding.core.base.dto;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：BaseDTO
 * 类描述：DTO基类
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:18:22
 */
@SuppressWarnings("serial")
public abstract class BaseDTO implements Serializable {

    private String id;

       /**
     * app使用的token标示
     */
    private String token;

    private String appOrgId;

    /**
     * 账号id（当前用户账号id）
     */
    private String accountId;

    /**
     * 当前公司
     */
    private String currentCompanyId;

    /**
     * 复制属性到Map，为null的属性不复制，包括基类的属性
     */
    public static void copyFields(Object source, Map<String,Object> dest) throws Exception {
        if (source == null || dest == null)
            return;

        BeanInfo sourceBeanInfo = Introspector.getBeanInfo(source.getClass(), Object.class);
        PropertyDescriptor[] sourceProperties = sourceBeanInfo.getPropertyDescriptors();

        for (PropertyDescriptor pty : sourceProperties) {
            Object val = pty.getReadMethod().invoke(source);
            if (val != null) {
                dest.put(pty.getName(),val);
            }
        }
    }

    /**
     * 复制Map字段到Dto，没有定义属性的字段不复制
     */
    public static void copyFields(Map<String,Object> source, Object dest) throws Exception {
        if (source == null || dest == null)
            return;

        BeanInfo destBeanInfo = Introspector.getBeanInfo(dest.getClass(), Object.class);
        PropertyDescriptor[] destProperties = destBeanInfo.getPropertyDescriptors();

        for (PropertyDescriptor pty : destProperties) {
            if (source.containsKey(pty.getName())){
                pty.getWriteMethod().invoke(dest, source.get(pty.getName()));
            }
        }
    }

    /**
     * entity对象和dto对象互相复制属性，包括基类的属性
     */
    public static void copyFields(Object source, Object dest) throws Exception {
    	if (source == null || dest == null)
    		return;
    	
        BeanInfo sourceBeanInfo = Introspector.getBeanInfo(source.getClass(), Object.class);
        PropertyDescriptor[] sourceProperties = sourceBeanInfo.getPropertyDescriptors();
        BeanInfo destBeanInfo = Introspector.getBeanInfo(dest.getClass(), Object.class);
        PropertyDescriptor[] destProperties = destBeanInfo.getPropertyDescriptors();

        for (int i = 0; i < sourceProperties.length; i++) {

            for (int j = 0; j < destProperties.length; j++) {

                if (sourceProperties[i].getName().equals(destProperties[j].getName())) {
                    //调用source的getter方法和dest的setter方法
                    destProperties[j].getWriteMethod().invoke(dest, sourceProperties[i].getReadMethod().invoke(source));
                    break;
                }
            }
        }
    }

    /**
     * 根据entityList复制dtoList
     */
    public static Object copyFields(Object source, Class destClass) throws Exception {
        Object dest = destClass.newInstance();
        copyFields(source, dest);
        return dest;
    }

    /**
     * 根据entityList复制dtoList
     */
    public static List copyFields(List sourceList, Class destClass) throws Exception {
        List destList = new ArrayList();
        for (Object source : sourceList) {
            Object dest = destClass.newInstance();
            copyFields(source, dest);
            destList.add(dest);
        }
        return destList;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppOrgId() {
        return appOrgId;
    }

    public void setAppOrgId(String appOrgId) {
        this.appOrgId = appOrgId;
    }

    public String getCurrentCompanyId() {
        return currentCompanyId;
    }

    public void setCurrentCompanyId(String currentCompanyId) {
        this.currentCompanyId = currentCompanyId;
    }
}
