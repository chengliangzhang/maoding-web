package com.maoding.core.base.dao;

import java.lang.reflect.ParameterizedType;


import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**深圳市设计同道技术有限公司
 * 类    名：AbstractDao
 * 类描述：基础Dao抽象类
 * 作    者：Chenxj
 * 日    期：2015年10月19日-下午4:17:54
 */
public abstract class AbstractDao<T> {
	protected final Logger log=LoggerFactory.getLogger(getClass());
	protected Class<T>entityClass;
	protected String tableName;
	@Autowired
	protected SqlSessionTemplate sqlSession;
	
	public AbstractDao (){
		this.entityClass=getEntityClass();
	}
	
	/**
	 * 方法描述：获取T的Class
	 * 作者：Chenxj
	 * 日期：2015年7月6日 - 上午11:25:00
	 * @return Class
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> getEntityClass(){
		return (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

}
