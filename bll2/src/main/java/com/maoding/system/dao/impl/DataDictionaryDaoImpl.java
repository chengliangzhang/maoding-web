package com.maoding.system.dao.impl;


import java.util.List;
import java.util.Map;

import com.maoding.system.dto.DataDictionaryDataDTO;
import org.springframework.stereotype.Service;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.system.dao.DataDictionaryDao;
import com.maoding.system.entity.DataDictionaryEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DataDictionaryDaoImpl
 * 类描述：数据字典DaoImpl
 * 作    者：wangrb
 * 日    期：2015年11月26日-下午2:25:25
 */
@Service("dataDictionaryDao")
public class DataDictionaryDaoImpl extends GenericDao<DataDictionaryEntity> implements DataDictionaryDao{

	@Override
	public List<DataDictionaryEntity> getSubDataByCode(String code) {
		return this.sqlSession.selectList("DataDictionaryEntityMapper.selectSubsetByCode",code);
	}

	@Override
	public List<DataDictionaryEntity> getDataByParemeter(Map<String, Object> map) {
		return this.sqlSession.selectList("DataDictionaryEntityMapper.selectByParemeter",map);
	}

	@Override
	public List<DataDictionaryEntity> selectParentAndSubByCode(
			Map<String, Object> map) {
		return this.sqlSession.selectList("DataDictionaryEntityMapper.selectParentAndSubByCode",map);
	}

	@Override
	public List<DataDictionaryDataDTO> getExpTypeList() {
		return getExpTypeList("gdfy");
	}

	@Override
	public List<DataDictionaryDataDTO> getExpTypeList(String code) {
		return this.sqlSession.selectList("DataDictionaryEntityMapper.getExpTypeList",code);
	}
}
