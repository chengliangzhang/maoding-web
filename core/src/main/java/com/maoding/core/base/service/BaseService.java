package com.maoding.core.base.service;


/**深圳市设计同道技术有限公司
 * 类    名：BaseService
 * 类描述：所有服务接口都应该继承此基础服务接口
 * 作    者：Chenxj
 * 日    期：2015年10月19日-下午5:32:47
 */
public interface BaseService<T> {
	
	//MyBaties 通用增删改方法
	public int	 						insert				(T entity);
	public int							updateById			(T entity);
	public int							deleteById			(Object id);
	public T							selectById			(Object id);
	
}
