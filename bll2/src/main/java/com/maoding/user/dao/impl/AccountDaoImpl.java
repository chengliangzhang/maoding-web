package com.maoding.user.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.core.base.dto.BaseShowDTO;
import com.maoding.user.dao.AccountDao;
import com.maoding.user.dto.UserQueryDTO;
import com.maoding.user.entity.AccountEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：AccountDaoImpl
 * 类描述：用户账号信息Dao
 * 作    者：MaoSF
 * 日    期：2016年7月7日-上午11:48:29
 */
@Service("accountDao")
public class AccountDaoImpl extends GenericDao<AccountEntity> implements AccountDao {

	@Override
	public AccountEntity getAccountByCellphoneOrEmail(String cellphone) {
		return this.sqlSession.selectOne("AccountEntityMapper.getAccountByCellphoneOrEmail",cellphone);
	}

	@Override
	public int updatedeAllfaultCompanyId(String defaultCompanyId) {
		return this.sqlSession.update("AccountEntityMapper.updatedeAllfaultCompanyId",defaultCompanyId);
	}

	/**
	 * 方法描述：获取所有账户信息（注册IM使用）
	 * 作者：MaoSF
	 * 日期：2016/8/8
	 *
	 * @param:
	 * @return:
	 */
	@Override
	public List<AccountEntity> selectAll() {
		return this.sqlSession.selectList("AccountEntityMapper.selectAll");
	}

	/**
	 * 根据用户名和用户所在公司名称获取用户ID
	 *
	 * @param companyName
	 * @param userName
	 */
	@Override
	public String getUserIdByCompanyNameAndUserName(@Param("companyName") String companyName, @Param("userName") String userName) {
		Map<String,String> map = new HashMap<>();
		map.put("companyName",companyName);
		map.put("userName",userName);
		return this.sqlSession.selectOne("UserMapper.getUserIdByCompanyNameAndUserName");
	}

	/**
	 * 根据用户ID取得用户名
	 *
	 * @param id
	 */
	@Override
	public String getUserName(String id) {
		if (id == null) return null;
		AccountEntity entity = selectById(id);
		return (entity != null) ? entity.getUserName() : "";
	}

	/**
	 * @param query 查询条件
	 * @return 用户列表
	 * @author 张成亮
	 * @date 2018/7/19
	 * @description 查询用户
	 **/
	@Override
	public List<BaseShowDTO> listBaseShowByQuery(UserQueryDTO query) {
		return sqlSession.selectList("UserMapper.listBaseShowByQuery");
	}
}
