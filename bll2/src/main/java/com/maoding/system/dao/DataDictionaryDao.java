package com.maoding.system.dao;



import java.util.List;
import java.util.Map;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.system.dto.DataDictionaryDataDTO;
import com.maoding.system.entity.DataDictionaryEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DataDictionaryDao
 * 类描述：数据字典DAO
 * 作    者：wangrb
 * 日    期：2015年11月26日-下午2:24:31
 */
public interface DataDictionaryDao extends BaseDao<DataDictionaryEntity>{
	
	/**
	 * 方法描述：根据code查出所有子集
	 * 作        者：wangrb
	 * 日        期：2015年11月26日-下午2:44:44
	 * @param code
	 * @return
	 */
	public List<DataDictionaryEntity> getSubDataByCode(String code);
	
	/**
	 * 方法描述：根据相关参数查找
	 * 作        者：wangrb
	 * 日        期：2015年11月26日-下午2:46:28
	 * @param map
	 * @return
	 */
	public List<DataDictionaryEntity> getDataByParemeter(Map<String,Object> map);
	
	/**
	 * 方法描述：根据code查询自己及子集
	 * 作        者：MaoSF
	 * 日        期：2016年3月24日-下午3:04:58
	 * @param map
	 * @return
	 */
	public List<DataDictionaryEntity> selectParentAndSubByCode(Map<String,Object> map);

	/**
	 * 固定支出
	 */
	List<DataDictionaryDataDTO> getExpTypeList();

	List<DataDictionaryDataDTO> getExpTypeList(String code);
}
