package com.maoding.core.base.service;



import org.springframework.beans.factory.annotation.Autowired;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.core.base.entity.BaseEntity;
import org.springframework.transaction.annotation.Transactional;


/**深圳市设计同道技术有限公司
 * 类    名：GenericService
 * 类描述：通用Service，所有Service都应该继承此Service
 * 作    者：Chenxj
 * 日    期：2015年10月19日-下午5:45:31
 */
@Transactional(rollbackFor=Exception.class)
public class GenericService<T extends BaseEntity> implements BaseService<T>{

	@Autowired
	protected BaseDao<T> baseDao;

	public int insert(T entity) {
		return baseDao.insert(entity);
	}

	public int updateById(T entity) {
		return baseDao.updateById(entity);
	}

	public int deleteById(Object id) {
		return baseDao.deleteById(id);
	}

	public T selectById(Object id) {
		return baseDao.selectById(id);
	}

}
	
