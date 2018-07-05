package com.maoding.org.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.org.entity.CompanyInviteEntity;

import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyInviteDao
 * 类描述：部门 Dao
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:42:48
 */
public interface CompanyInviteDao extends BaseDao<CompanyInviteEntity> {


	CompanyInviteEntity selectByParam(Map<String, Object> param);

}
