package com.maoding.user.service;

import java.util.List;
import java.util.Map;

import com.maoding.core.base.dto.BaseShowDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.projectmember.dto.MemberQueryDTO;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import com.maoding.user.dto.AppUseDTO;
import com.maoding.user.dto.UserQueryDTO;
import com.maoding.user.entity.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.maoding.core.base.service.BaseService;
import com.maoding.user.dto.AccountDTO;
import com.maoding.user.dto.UserDTO;

public interface UserService extends BaseService<UserEntity>{
	
	
	/**
	 * 方法描述：保存人员信息
	 * 作        者：MaoSF
	 * 日        期：2015年11月26日-下午4:51:31
	 * @param entity
	 * @return
	 */
	public AjaxMessage saveOrUpdateUser(AccountDTO entity)throws Exception;

	/**
	 * 方法描述：saveOrUpdateAppUse
	 * 作        者：MaoSF
	 * 日        期：2015年11月26日-下午4:51:31
	 * @param entity
	 * @return
	 */
	public AjaxMessage saveOrUpdateAppUse(AppUseDTO entity)throws Exception;
	/**
	 * 方法描述：根据id获取人员信息
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午8:44:32
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public UserDTO getUserById(String id)throws Exception;
	
//	/**
//	 * 方法描述：根据userId员工获奖信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年6月30日-上午11:23:03
//	 * @param userId
//	 * @return
//	 */
//	public List<UserAwardsEntity> getUserAwardshonorsByUserId(String userId);
//	/**
//	 * 方法描述：根据userId获取个人继续教育
//	 * 作        者：MaoSF
//	 * 日        期：2015年6月30日-下午4:38:35
//	 * @param userId
//	 * @return
//	 */
//	public List<UserContinueEducationEntity> getUserContinueeducationByUserId(String userId);
//	/**
//	 * 方法描述：根据userId获取员工教育背景
//	 * 作        者：MaoSF
//	 * 日        期：2015年6月30日-下午4:38:35
//	 * @param userId
//	 * @return
//	 */
//	public List<UserEducationBackGroundEntity> getUserEducationbgByUserId(String userId);
//
//	/**
//	 * 方法描述：根据userId获取人员资质信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年6月30日-下午4:38:35
//	 * @param userId
//	 * @return
//	 */
//	public List<UserQualificationsEntity> getUserQualificationsByUserId(String userId);
//	/**
//	 * 方法描述：根据userId获取员工技术信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年6月30日-下午4:38:35
//	 * @param userId
//	 * @return
//	 */
//	public List<com.maoding.user.entity.UserTechnicalEntity> getUserTechnicalByUserId(String userId);
//	/**
//	 * 方法描述：根据userId获取员工工作经历信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年6月30日-下午4:38:35
//	 * @param userId
//	 * @return
//	 */
//	public List<UserWorkExperienceEntity> getUserWorkexperienceByUserId(String userId);
//	/**
//	 * 方法描述：根据userId获取员工工作业绩
//	 * 作        者：MaoSF
//	 * 日        期：2015年6月30日-下午4:38:35
//	 * @param userId
//	 * @return
//	 */
//	public List<UserWorkPerformanceEntity> getUserWorkperformanceByUserId(String userId);
	
//	/**
//	 * 方法描述：根据id查找员工获奖信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月3日-下午6:31:43
//	 * @param id
//	 * @return
//	 */
//	public UserAwardsEntity getAwardshonorsById(String id);
//	/**
//	 * 方法描述：根据id查找员工继续教育信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月3日-下午6:31:43
//	 * @param id
//	 * @return
//	 */
//	public UserContinueEducationEntity getContinueeducationById(String id);
//
//	/**
//	 * 方法描述：根据id查找员工教育背景
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月3日-下午6:31:43
//	 * @param id
//	 * @return
//	 */
//	public UserEducationBackGroundEntity getEducationbgById(String id) ;
//	/**
//	 * 方法描述：根据id查找员工资质信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月3日-下午6:31:43
//	 * @param id
//	 * @return
//	 */
//	public UserQualificationsEntity getQualificationsById(String id);
//	/**
//	 * 方法描述：根据id查找员工技术信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月3日-下午6:31:43
//	 * @param id
//	 * @return
//	 */
//	public UserTechnicalEntity getTechnicalById(String id);
//
//	/**
//	 * 方法描述：根据id查找员工工作经历
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月3日-下午6:31:43
//	 * @param id
//	 * @return
//	 */
//	public UserWorkExperienceEntity getWorkexperienceById(String id);
//	/**
//	 * 方法描述：根据id查找员工业绩信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月3日-下午6:31:43
//	 * @param id
//	 * @return
//	 */
//	public UserWorkPerformanceEntity getWorkperformanceById(String id);
//
//	/**
//	 * 方法描述：保存员工获奖信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月6日-下午4:31:03
//	 * @param entity
//	 * @throws Exception
//	 */
//	public int saveOrUpdateAwardshonors(UserAwardsEntity entity) ;
//
//	/**
//	 * 方法描述：保存员工继续教育信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月6日-下午4:31:27
//	 * @param entity
//	 * @throws Exception
//	 */
//	public int saveOrUpdateContinueeducation(UserContinueEducationEntity entity) ;
//
//	/**
//	 * 方法描述：保存员工教育背景信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月6日-下午4:32:11
//	 * @param entity
//	 * @throws Exception
//	 */
//	public int saveOrUpdateEducationbg(UserEducationBackGroundEntity entity);
//
//
//
//
//	/**
//	 * 方法描述：保存员工资质信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月6日-下午4:32:39
//	 * @param entity
//	 * @throws Exception
//	 */
//	public int saveOrUpdateQualifications(UserQualificationsEntity entity);
//
//	/**
//	 * 方法描述：保存员工技术信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月6日-下午4:32:57
//	 * @param entity
//	 * @throws Exception
//	 */
//	public int saveOrUpdateTechnical(UserTechnicalEntity entity) ;
//	/**
//	 * 方法描述：保存员工工作经历信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月6日-下午4:33:12
//	 * @param entity
//	 * @throws Exception
//	 */
//	public int saveOrUpdateWorkexperience(UserWorkExperienceEntity entity);
//
//
//	/**
//	 * 方法描述：保存员工工业绩历信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月6日-下午4:33:12
//	 * @param entity
//	 * @throws Exception
//	 */
//	public int saveOrUpdateWorkperformance(UserWorkPerformanceEntity entity);
//
//	/**
//	 * 方法描述：根据id删除员工获奖信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月3日-下午6:31:43
//	 * @param id
//	 * @return
//	 */
//	public int delAwardshonorsById(String id);
//	/**
//	 * 方法描述：根据id删除员工继续教育信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月3日-下午6:31:43
//	 * @param id
//	 * @return
//	 */
//	public int delContinueeducationById(String id);
//
//	/**
//	 * 方法描述：根据id删除员工教育背景
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月3日-下午6:31:43
//	 * @param id
//	 * @return
//	 */
//	public int delEducationbgById(String id) ;
	/**
	 * 方法描述：根据id删除员工其他附件信息
	 * 作        者：MaoSF
	 * 日        期：2015年7月3日-下午6:31:43
	 * @param id
	 * @return
	 */
//	public void deleteByUserId(String persoanlId,String attachType) ;
//	/**
//	 * 方法描述：根据id删除员工资质信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月3日-下午6:31:43
//	 * @param id
//	 * @return
//	 */
//	public int delQualificationsById(String id);
//	/**
//	 * 方法描述：根据id删除员工技术信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月3日-下午6:31:43
//	 * @param id
//	 * @return
//	 */
//	public int delTechnicalById(String id);
//	/**
//	 * 方法描述：根据id删除员工工作经历
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月3日-下午6:31:43
//	 * @param id
//	 * @return
//	 */
//	public int delWorkexperienceById(String id);
//	/**
//	 * 方法描述：根据id删除员工业绩信息
//	 * 作        者：MaoSF
//	 * 日        期：2015年7月3日-下午6:31:43
//	 * @param id
//	 * @return
//	 */
//	public int delWorkperformanceById(String id);
	
	/**
	 * 方法描述：根据类型查询关注
	 * 作        者：MaoSF
	 * 日        期：2016年3月18日-上午9:50:00
	 * @param paraMap
	 * @return
	 */
	public  List<UserAttachEntity>  getAttachByType(Map<String,Object>paraMap);

	/**
	 * 方法描述：getAppUseList
	 * 作        者：MaoSF
	 * 日        期：2016年3月18日-上午9:50:00
	 * @param paraMap
	 * @return
	 */
	public List<AppUseEntity> getAppUseList();
	
	/**
	 * 方法描述：保存员工其他附件信息
	 * 作        者：MaoSF
	 * 日        期：2015年7月6日-下午4:32:25
	 * @param entity
	 * @throws Exception 
	 */
	public int saveOrUpdateAttach(UserAttachEntity entity);
	
	
	/**
	 * 方法描述：上传原图
	 * 作        者：MaoSF
	 * 日        期：2016年7月13日-下午7:49:34
	 * @param file
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public AjaxMessage uploadHeadOriginalImg(MultipartFile file,Map<String,String> param) throws Exception;
	
	/**
	 * 方法描述：上传头像
	 * 作        者：MaoSF
	 * 日        期：2016年7月13日-下午6:38:43
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public AjaxMessage uploadHeadImg(@RequestBody Map<String,Object> param)throws Exception;

	/**
	 * 上传头像
	 * @param param
	 * @return
	 */
	public AjaxMessage saveOrUpdateUserAttach(Map<String,Object> param)throws Exception;

	/**
	 * @author  张成亮
	 * @date    2018/7/19
	 * @description     查询用户
	 * @param   query 查询条件
	 * @return  用户列表，id存放的是companyUserId
	 **/
	List<BaseShowDTO> listWithCompanyUserIdByQuery(UserQueryDTO query);

}
