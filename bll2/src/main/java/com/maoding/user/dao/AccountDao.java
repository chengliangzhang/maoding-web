package com.maoding.user.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.core.base.dto.BaseShowDTO;
import com.maoding.user.dto.UserQueryDTO;
import com.maoding.user.entity.AccountEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AccountDao extends BaseDao<AccountEntity>{

	/**
	 * 方法描述：根据手机号查找注册信息
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-上午11:34:57
	 * @param cellphone
	 * @return
	 */
	public AccountEntity getAccountByCellphoneOrEmail(String cellphone);

	/**
	 * 方法描述： 把默认公司为companyId都设置为null
	 * 作        者：MaoSF
	 * 日        期：2016年7月15日-下午2:04:11
	 * @param defaultCompanyId
	 */
	public int updatedeAllfaultCompanyId(String defaultCompanyId);


	/**
	 * 方法描述：获取所有账户信息（注册IM使用）
	 * 作者：MaoSF
	 * 日期：2016/8/8
	 * @param:
	 * @return:
	 */
	public List<AccountEntity> selectAll();

	/** 根据用户名和用户所在公司名称获取用户ID */
	String getUserIdByCompanyNameAndUserName(@Param("companyName") String companyName, @Param("userName") String userName);
	/** 根据用户ID取得用户名 */
	String getUserName(String id);

	/**
	 * @author  张成亮
	 * @date    2018/7/19
	 * @description     通用用户
	 * @param   query 查询条件
	 * @return  用户列表
	 **/
	List<BaseShowDTO> listBaseShowByQuery(UserQueryDTO query);

}
