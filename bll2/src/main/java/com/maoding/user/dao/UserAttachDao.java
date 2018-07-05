package com.maoding.user.dao;

import java.util.List;
import java.util.Map;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.user.entity.UserAttachEntity;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：UserAttachDao
 * 类描述：用户附件Dao
 * 作    者：MaoSF
 * 日    期：2015年11月23日-下午2:44:39
 */
public interface UserAttachDao extends BaseDao<UserAttachEntity>{
	
	/**
	 * 方法描述：根据参数查询附件
	 * 作        者：MaoSF
	 * 日        期：2015年11月23日-下午4:48:56
	 * @param map
	 * @return
	 */
	public List<UserAttachEntity> getUserAttachByUserId(String userId);
	
	
	/**
	 * 方法描述：根据type获取附件
	 * 作        者：TangY
	 * 日        期：2016年3月21日-上午10:29:56
	 * @param paraMap
	 * @return
	 */
	public  List<UserAttachEntity>  getAttachByType(Map<String,Object>paraMap);


	/**
	 * 方法描述：获取头像
	 * 作        者：TangY
	 * 日        期：2016年3月21日-上午10:29:56
	 */
	public  String  getHeadImg(String userId);

	
	/**
	 * 方法描述：根据参数删除附件
	 * 作        者：MaoSF
	 * 日        期：2015年11月23日-下午4:52:17
	 * @param map
	 * @return
	 */
	public int delUserAttachByUserId(String userId);
}
