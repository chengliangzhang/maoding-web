package com.maoding.system.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.system.dao.VersionDao;
import com.maoding.system.dto.UpdateDTO;
import com.maoding.system.dto.UpdateQueryDTO;
import com.maoding.system.entity.VersionEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



/**深圳市设计同道技术有限公司
 * 类    名：VersionDaoImpl
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年12月30日-上午11:28:41
 */
@Service("versionDao")
public class VersionDaoImpl extends GenericDao<VersionEntity> implements VersionDao {

	public VersionEntity selectByPlatform(Map <String,Object> paraMap){
		return this.sqlSession.selectOne("VersionEntityMapper.selectByPlatform", paraMap);
	}

	/**
	 * 方法：获取更新历史列表
	 * 作者：Zhangchengliang
	 * 日期：2017/8/4
	 *
	 * @param query 查询条件
	 * @return 一页更新记录列表
	 */
	@Override
	public List<UpdateDTO> listUpdate(UpdateQueryDTO query) {
		return sqlSession.selectList("VersionMapper.listUpdate", query);
	}
}
