package com.maoding.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.system.dto.DataDictionaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maoding.core.base.service.GenericService;
import com.maoding.system.dao.DataDictionaryDao;
import com.maoding.system.entity.DataDictionaryEntity;
import com.maoding.system.service.DataDictionaryService;

/**深圳市设计同道技术有限公司
 * 类    名：DataDictionaryServiceImpl
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年12月31日-上午11:10:52
 */
@Service("dataDictionaryService")
public class DataDictionaryServiceImpl extends GenericService<DataDictionaryEntity> implements DataDictionaryService{
	@Autowired
	private DataDictionaryDao dataDictionaryDao;

	@Override
	public List<DataDictionaryEntity> selectParentAndSubByCode(Map<String, Object> map) {
		return dataDictionaryDao.selectParentAndSubByCode(map);
	}

	@Override
	public List<DataDictionaryEntity> getSubDataByCode(String code) {
		return dataDictionaryDao.getSubDataByCode(code);
	}

	/**
	 * 方法描述：根据code查出所有子集
	 * 作        者：wangrb
	 * 日        期：2015年11月26日-下午2:44:44
	 *
	 * @param code
	 * @return
	 */
	@Override
	public List<DataDictionaryDTO> getSubDataByCodeToDTO(String code) throws Exception{
		List<DataDictionaryEntity> list = this.getSubDataByCode(code);
		return BaseDTO.copyFields(list,DataDictionaryDTO.class);
	}

	@Override
	public List<DataDictionaryEntity> getDataByParemeter(Map<String, Object> map) {
		return dataDictionaryDao.getDataByParemeter(map);
	}

}
