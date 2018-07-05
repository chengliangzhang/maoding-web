package com.maoding.org.dao;

import java.util.List;
import java.util.Map;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.org.entity.OrgUserEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：OrgDao
 * 类描述：组织基础类
 * 作    者：MaoSF
 * 日    期：2016年5月30日-下午4:19:05
 */
public interface OrgUserDao extends BaseDao<OrgUserEntity>{
	/**
	 * 方法描述：根据参数删除记录
	 * 作        者：MaoSF
	 * 日        期：2016年5月30日-下午4:33:59
	 * @param param
	 * @return
	 */
	public int deleteOrgUserByParam(Map<String,Object>param);

	/**
	 * 方法描述：根据param(orgIds or cuId)查询（用于删除部门使用）
	 * 作        者：MaoSF
	 * 日        期：2016年5月30日-下午4:33:59
	 * @param param(orgIds)
	 * @return
	 */
	public List<OrgUserEntity> selectByParam(Map<String,Object>param);

	/**
	 * 方法描述：获取某组织中最大的seq值
	 * 作者：MaoSF
	 * 日期：2016/8/5
	 * @param:
	 * @return:
	 */
	public int getMaxOrgUserSeq();

}
