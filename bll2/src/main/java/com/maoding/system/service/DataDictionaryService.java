package com.maoding.system.service;

import java.util.List;
import java.util.Map;

import com.maoding.core.base.service.BaseService;
import com.maoding.system.dto.DataDictionaryDTO;
import com.maoding.system.entity.DataDictionaryEntity;

/**深圳市设计同道技术有限公司
 * 类    名：DataDictionaryService
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年12月31日-上午11:10:17
 */
public interface DataDictionaryService extends BaseService<DataDictionaryEntity> {
	/**
	 * 方法描述：根据code查询自己及子集
	 * 作        者：MaoSF
	 * 日        期：2016年3月24日-下午3:04:58
	 *
	 * @param map
	 * @return
	 */
	public List<DataDictionaryEntity> selectParentAndSubByCode(Map<String, Object> map);

	/**
	 * 方法描述：根据code查出所有子集
	 * 作        者：wangrb
	 * 日        期：2015年11月26日-下午2:44:44
	 *
	 * @param code
	 * @return
	 */
	public List<DataDictionaryEntity> getSubDataByCode(String code);

	/**
	 * 方法描述：根据code查出所有子集
	 * 作        者：wangrb
	 * 日        期：2015年11月26日-下午2:44:44
	 *
	 * @param code
	 * @return
	 */
	public List<DataDictionaryDTO> getSubDataByCodeToDTO(String code) throws Exception;

	/**
	 * 方法描述：根据相关参数查找
	 * 作        者：wangrb
	 * 日        期：2015年11月26日-下午2:46:28
	 *
	 * @param map
	 * @return
	 */
	public List<DataDictionaryEntity> getDataByParemeter(Map<String, Object> map);
}

