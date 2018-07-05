package com.maoding.user.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.user.entity.UserEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：UserDao
 * 类描述：用户信息Dao
 * 作    者：MaoSF
 * 日    期：2015年11月23日-下午2:44:39
 */
public interface UserDao extends BaseDao<UserEntity>{
	
//	/**
//	 * 方法描述：查询人员列表
//	 * 作        者：MaoSF
//	 * 日        期：2015年11月23日-下午3:15:27
//	 * @param parmaMap
//	 * @return
//	 */
//	public List<UserEntity> findUserByPage(Map<String,Object> parmaMap);
//	
//	/**
//	 * 方法描述：查询人员列表数目
//	 * 作        者：MaoSF
//	 * 日        期：2015年11月23日-下午3:15:31
//	 * @param parmaMap
//	 * @return
//	 */
//	public int findUserByPageCount(Map<String,Object> parmaMap);
//	
//	/**
//	 * 方法描述：
//	 * 作        者：MaoSF
//	 * 日        期：2015年11月25日-上午10:35:39
//	 * @param map
//	 * @return
//	 */
//	public UserEntity getById(Map<String,Object> map);
//	
//	/**
//	 * 方法描述：
//	 * 作        者：wangrb
//	 * 日        期：2015年12月8日-上午11:17:16
//	 * @param userId
//	 * @return
//	 */
//	public UserEntity getByLoginUserId(String userId);
//	
//	/**
//	 * 方法描述：根据权限Code查询该企业下的人员
//	 * 作        者：MaoSF
//	 * 日        期：2016年3月18日-上午9:57:19
//	 * @param paraMap
//	 * @return
//	 */
//	public List<UserEntity> selectUserByRoleCode(Map<String,Object> paraMap);
//	
//	/**
//	 * 方法描述：个人中心信息（移动端接口）
//	 * 作        者：MaoSF
//	 * 日        期：2016年3月25日-上午11:53:24
//	 * @param paraMap
//	 * @return
//	 */
//	public Map<String,Object> getUserInfoByUserId(Map<String,Object> paraMap);
//	
//	/**
//	 * 方法描述：移动端使用（人员审核列表）
//	 * 作        者：MaoSF
//	 * 日        期：2016年3月25日-下午2:58:05
//	 * @param paraMap
//	 * @return
//	 */
//	public List<Map<String, Object>> getVerifyUser(Map<String,Object> paraMap);

}
