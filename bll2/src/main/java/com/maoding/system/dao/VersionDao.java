package com.maoding.system.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.system.dto.UpdateDTO;
import com.maoding.system.dto.UpdateQueryDTO;
import com.maoding.system.entity.VersionEntity;

import java.util.List;
import java.util.Map;



/**深圳市设计同道技术有限公司
 * 类    名：VersionDao
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年12月30日-上午11:28:15
 */
public interface VersionDao extends BaseDao<VersionEntity> {
	/**
	 * 方法描述：系统更新 
	 * 作        者：TangY
	 * 日        期：2016年3月25日-上午11:29:05
	 * @param paraMap
	 * @return
	 */
	public VersionEntity selectByPlatform(Map<String, Object> paraMap);

	/**
	 * 方法：获取更新历史列表
	 * 作者：Zhangchengliang
	 * 日期：2017/8/4
	 * @param query 查询条件
	 * @return 一页更新记录列表
	 */
	List<UpdateDTO> listUpdate(UpdateQueryDTO query);

	/**
	 * 方法：在分页获取记录时，获取最后select语句所能查找到的记录条数总数
	 * 作者：Zhangchengliang
	 * 日期：2017/8/4
	 *
	 * @return 最后select语句所能查找到的记录条数总数
	 * 注：由于BaseDao接口在BaseService内被使用，而没有定义其实现类，因此无法在BaseDao接口内定义此方法，
	 * 只能在BaseDao的实现基类GenericDao内定义实现方法，在各个Dao接口内定义此方法并调用基类实现
	 */
	int getLastQueryCount();
}
