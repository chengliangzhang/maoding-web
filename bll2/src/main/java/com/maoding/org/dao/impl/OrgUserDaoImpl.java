package com.maoding.org.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.org.dao.OrgUserDao;
import com.maoding.org.entity.OrgUserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
/**
 * 深圳市设计同道技术有限公司
 * 类    名：OrgDaoImpl
 * 类描述：人员组织中间表（人员--公司，人员--部门）Dao
 * 作    者：MaoSF
 * 日    期：2016年5月30日-下午4:21:06
 */
@Service("orgUserDao")
public class OrgUserDaoImpl extends GenericDao<OrgUserEntity> implements OrgUserDao{

	@Override
	public int insert(OrgUserEntity entity){
		int seq = getMaxOrgUserSeq();
		entity.setSeq(seq);
		return  super.insert(entity);
	}

	public int deleteOrgUserByParam(Map<String,Object>param) {
		return this.sqlSession.delete("OrgUserEntityMapper.deleteOrgUserByParam", param);
	}

	@Override
	public List<OrgUserEntity> selectByParam(Map<String, Object> param) {
		return this.sqlSession.selectList("OrgUserEntityMapper.selectByParam", param);
	}

	/**
	 * 方法描述：获取某组织中最大的seq值
	 * 作者：MaoSF
	 * 日期：2016/8/5
	 * @param:
	 * @return:
	 */
	@Override
	public int getMaxOrgUserSeq() {
		return this.sqlSession.selectOne("OrgUserEntityMapper.getMaxOrgUserSeq");
	}

}
