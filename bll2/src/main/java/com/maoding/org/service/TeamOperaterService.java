package com.maoding.org.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.org.dto.CompanyUserTableDTO;
import com.maoding.org.dto.TeamOperaterDTO;
import com.maoding.org.entity.TeamOperaterEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：departService
 * 类描述：部门           Service
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:22:43
 */
public interface TeamOperaterService extends BaseService<TeamOperaterEntity>{

	/**
	 * 方法描述：移交管理员
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午9:13:48
	 * @param 
	 * @return
	 */
	public AjaxMessage  transferSys(TeamOperaterDTO dto,String newPassword) throws Exception;

	/**
	 * 方法描述：移交企业负责人
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午9:13:48
	 * @param
	 * @return
	 */
	public AjaxMessage  transferOrgManager(TeamOperaterDTO dto,String newPassword) throws Exception;

	/**
	 * 方法描述：保存系统管理员所以资料
	 * 作者：MaoSF
	 * 日期：2016/11/17
	 * @param:
	 * @return:
	 */
	public AjaxMessage saveSystemManage(TeamOperaterEntity teamOperaterEntity) throws Exception;

	/**
	 * 方法描述：  根据（userId，companyId）查询
	 * 作        者：TangY
	 * 日        期：2016年7月12日-上午4:50:13
	 * @return
	 */
	public TeamOperaterEntity getTeamOperaterByParam(String companyId,String userId)throws Exception;

	/**
	 * 修改管理员密码
	 * @param dto
	 * @return
	 * @throws Exception
     */
	public AjaxMessage updateAdminPassword(TeamOperaterDTO dto) throws Exception;


	CompanyUserTableDTO getSystemManager(String companyId);
}
