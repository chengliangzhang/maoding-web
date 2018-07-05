package com.maoding.user.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maoding.user.dto.UserAttachDTO;
import org.springframework.web.multipart.MultipartFile;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.user.entity.UserAttachEntity;

public interface UserAttachService extends BaseService<UserAttachEntity>{
	
	
	/**
	 * 方法描述：根据参数查询附件
	 * 作        者：MaoSF
	 * 日        期：2015年11月23日-下午4:48:56
	 * @param map
	 * @return
	 */
	public List<UserAttachEntity> getUserAttachByUserId(String userId) throws Exception;
	
	
	/**
	 * 方法描述：根据type获取附件
	 * 作        者：TangY
	 * 日        期：2016年3月21日-上午10:29:56
	 * @param paraMap(userId 用户Id，attachType 附件类型)
	 * @return
	 */
	public  List<UserAttachEntity>  getAttachByType(Map<String,Object>paraMap) throws Exception;

	/**
	 * 方法描述：根据type获取附件
	 * 作        者：TangY
	 * 日        期：2016年3月21日-上午10:29:56
	 * @param paraMap(userId 用户Id，attachType 附件类型)
	 * @return
	 */
	public List<UserAttachDTO> getAttachByTypeToDTO(Map<String,Object>paraMap) throws Exception;

	/**
	 * 方法描述：保存或修改附件
	 * 作        者：TangY
	 * 日        期：2016年7月11日-下午2:39:26
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public int saveOrUpdateAttach(UserAttachEntity entity)throws Exception;
	
	/**
	 * 方法描述：上传用户附件
	 * 作        者：TangY
	 * 日        期：2016年7月11日-下午3:15:53
	 * @param file
	 * @param param
	 * @return UserAttachEntity
	 * @throws Exception
	 */
	public Object saveUserAttach(MultipartFile file,Map<String,Object>param)throws Exception;

	String getHeadImgUrl(String userId) throws Exception;
}
