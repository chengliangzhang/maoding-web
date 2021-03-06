package com.maoding.core.base.dao;


import org.springframework.util.ObjectUtils;

import java.util.List;

/**深圳市设计同道技术有限公司
 * 类    名：BaseDao
 * 类描述：所有Dao接口都应该基础Dao接口
 * 作    者：Chenxj
 * 日    期：2015年10月19日-下午4:25:15
 */
public interface BaseDao<T> {

	//MyBaties 通用增删改方法
	/**
	 * 方法描述：MyBaties 增加方法
	 * 作        者：MaoSF
	 * 日        期：2015年12月25日-上午11:51:18
	 * @param entity
	 * @return
	 */
	public int 							insert				(T entity);
	/**
	 * 方法描述：MyBaties 修改方法
	 * 作        者：MaoSF
	 * 日        期：2015年12月25日-上午11:50:55
	 * @param entity
	 * @return
	 */
	public int							updateById			(T entity);
	/**
	 * 方法描述：MyBaties 删除方法
	 * 作        者：MaoSF
	 * 日        期：2015年12月25日-上午11:51:33
	 * @param id
	 * @return
	 */
	public int							deleteById			(Object id);
	/**
	 * 方法描述：MyBaties 查询方法
	 * 作        者：MaoSF
	 * 日        期：2015年12月25日-上午11:51:45
	 * @param id
	 * @return
	 */
	public T							selectById			(Object id);

	/**
	 * @author  张成亮
	 * @date    2018/7/16
	 * @description     插入列表
	 * @param   entityList 要插入的列表
	 * @return  插入的记录数
	 **/
	default int insert(List<T> entityList){
		int n = 0;
		if (!ObjectUtils.isEmpty(entityList)){
			for (T entity : entityList) {
				insert(entity);
				n++;
			}
		}
		return n;
	}
}
