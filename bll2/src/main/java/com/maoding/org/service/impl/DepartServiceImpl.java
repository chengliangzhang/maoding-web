package com.maoding.org.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.StringUtil;
import com.maoding.hxIm.constDefine.ImGroupType;
import com.maoding.hxIm.service.ImService;
import com.maoding.org.dao.*;
import com.maoding.org.dto.DepartDTO;
import com.maoding.org.dto.DepartRoleDTO;
import com.maoding.org.dto.OrgTreeDTO;
import com.maoding.org.entity.*;
import com.maoding.org.service.DepartService;
import com.maoding.role.dao.UserPermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 深圳市设计同道技术有限公司
 * 类    名：DepartServiceImpl
 * 类描述：团队（公司）ServiceImpl
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午4:24:38
 */
@Service("departService")
public class DepartServiceImpl extends GenericService<DepartEntity>  implements DepartService{

    @Autowired
    private DepartDao  departDao;

	@Autowired
	private CompanyDao companyDao;
    
    @Autowired
    private OrgDao orgDao;

	@Autowired
	private OrgUserDao orguserDao;
    
    @Autowired
    private CompanyUserDao companyUserDao;

	@Autowired
	private UserPermissionDao userPermissionDao;

	@Autowired
	private ImService imService;


	@SuppressWarnings("unchecked")
	@Override
	public List<DepartDTO> getDepartByCompanyId(Map<String,Object>paraMap) throws Exception{
		 List<DepartEntity> departList=departDao.getDepartByCompanyId(paraMap);
		 List<DepartDTO> departDtoList=new ArrayList<DepartDTO>();
		 departDtoList= BaseDTO.copyFields(departList, DepartDTO.class);
		 return departDtoList;
	}

	/**
	 * 方法描述：使用递归查询公司部门
	 * 作者：MaoSF
	 * 日期：2016/9/18
	 */
	@Override
	public List<DepartDTO> getDepartByCompanyId(Map<String, Object> paraMap, List<DepartDTO> departDTOList) throws Exception{
		if(!paraMap.containsKey("pid")){
			paraMap.put("pid",paraMap.get("companyId"));
		}
		if(departDTOList==null){
			departDTOList = new ArrayList<DepartDTO>();
		}
		List<DepartDTO> list = this.getDepartByCompanyId(paraMap);
		for(int i = 0 ;i<list.size();i++){
			departDTOList.add(list.get(i));
			paraMap.put("pid",list.get(i).getId());
			departDTOList.addAll(getDepartByCompanyId(paraMap,null));
		}
		return departDTOList;
	}


	/**
	 * 方法描述：根据companyId和userId查询Departs（部门）包含公司
	 * 作        者：TangY
	 * 日        期：2016年7月8日-下午3:32:16
	 *
	 * @param paraMap （companyId（公司ID）,userId（用户Id））
	 * @return
	 */
	@Override
	public List<DepartDTO> getDepartByUserIdContentCompany(Map<String, Object> paraMap) throws Exception{
		List<DepartEntity> departList=departDao.getDepartByUserIdContentCompany(paraMap);
		List<DepartDTO> departDtoList=new ArrayList<DepartDTO>();
		departDtoList= BaseDTO.copyFields(departList, DepartDTO.class);
		return departDtoList;
	}

	public AjaxMessage validateSaveOrUpdateDepart(DepartDTO dto) throws Exception{

		if (StringUtil.isNullOrEmpty(dto.getCompanyId())){
			return new AjaxMessage().setCode("1").setInfo("请选择团队");
		}
		CompanyEntity company = companyDao.selectById(dto.getCompanyId());
		if(company==null){
			return new AjaxMessage().setCode("1").setInfo("请选择团队");
		}
		if (StringUtil.isNullOrEmpty(dto.getCompanyId())){
			return new AjaxMessage().setCode("1").setInfo("请选择团队");
		}

		if(StringUtil.isNullOrEmpty(dto.getDepartName())){
			return new AjaxMessage().setCode("1").setInfo("部门名称不能为空");
		}
		/*if(StringUtil.isNullOrEmpty(dto.getPid())){
			return new AjaxMessage().setCode("1").setInfo("请选择父部门");
		}*/
		return new AjaxMessage().setCode("0");
	}
	@Override
	public AjaxMessage saveOrUpdateDepart(DepartDTO dto) throws Exception {
		AjaxMessage ajax = validateSaveOrUpdateDepart(dto);
		if("1".equals(ajax.getCode())){
			return ajax;
		}
		String accountId = dto.getAccountId();
		DepartEntity entity = new  DepartEntity();
		BaseDTO.copyFields(dto, entity);
		DepartDTO parent = dto.getParentDepart();
		String id = "";

    	//如果是新增
		if(null==entity.getId() || "".equals(entity.getId())){
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("companyId", entity.getCompanyId());
			param.put("pid", parent.getId());
			param.put("departName", entity.getDepartName());
			DepartEntity isExitEntity=departDao.getByDepartNameAndPid(param);

			//判断在同一目录下是否存在名字相同的部门
        	if(isExitEntity!=null){
        		if("0".equals(isExitEntity.getStatus()))
        		{
        			return new AjaxMessage().setCode("1").setInfo("该部门已经存在");
        		}else{
        			isExitEntity.setStatus("0");
        			departDao.updateById(isExitEntity);
					entity = isExitEntity;
        		}
        	}else {
				id=StringUtil.buildUUID();
				entity.setId(id);

				if(StringUtil.isNullOrEmpty(parent.getDepartPath())){
					entity.setDepartPath(id);
				}else{
					entity.setDepartPath(parent.getDepartPath()+"-"+id);
				}
				entity.setOrgType("0");
				entity.setPid(parent.getId());
				entity.setDepartLevel(StringUtil.isNullOrEmpty(parent.getDepartLevel())?"1":(Integer.parseInt(parent.getDepartLevel())+1)+"");
				entity.setCreateBy(accountId);
				//保存OrgEntity,组织基础表
				OrgEntity org=new OrgEntity();
				org.setId(id);//基础表的id和部门表的id一致
				org.setOrgType("1");
				org.setOrgStatus("0");
				org.setCreateBy(entity.getCreateBy());
				orgDao.insert(org);
				//保存部门
				departDao.insert(entity);
			}

			BaseDTO.copyFields(entity, dto);

			OrgTreeDTO tree = new OrgTreeDTO();
			tree.setRealId(entity.getId());
			tree.setId(entity.getId());
			tree.setCompanyId(entity.getCompanyId());
			tree.setText(entity.getDepartName());
			tree.setType("depart");
			tree.setTreeEntity(entity);
			return new AjaxMessage().setCode("0").setInfo("保存成功").setData(tree);
			
		}else{
			return updateDepart(dto);
		}
	}
	
	/**
	 * 方法描述：修改部门信息
	 * 作        者：MaoSF
	 * 日        期：2016年7月14日-上午11:58:30
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public AjaxMessage updateDepart(DepartDTO dto)throws Exception {
		DepartEntity entity = new  DepartEntity();
		BaseDTO.copyFields(dto, entity);
		DepartDTO parent = dto.getParentDepart();
		
		String id = entity.getId();
		DepartEntity dbEntity=departDao.selectById(id);//获取数据库中的信息
		if(!dbEntity.getDepartName().equals(entity.getDepartName())){//如果修改了部门名称，则判断
			Map<String,Object> param=new HashMap<String,Object>();
	    	param.put("companyId", entity.getCompanyId());
	    	param.put("pid", parent.getId());
	    	param.put("departName", entity.getDepartName());
	    	DepartEntity isExitEntity=departDao.getByDepartNameAndPid(param);
			//判断在同一目录下是否存在名字相同的部门
        	if(isExitEntity!=null){
        		return new AjaxMessage().setCode("1").setInfo("该部门已经存在");
        	}
		}
		entity.setPid(parent.getId());
		/*entity.setDepartLevel(StringUtil.isNullOrEmpty(parent.getDepartLevel())?"1":(Integer.parseInt(parent.getDepartLevel())+1)+"");
		
		if(!dbEntity.getPid().equals(entity.getPid())){//如果改变当前的父id，则修改当前
			if(StringUtil.isNullOrEmpty(parent.getDepartPath())){
				entity.setDepartPath(id);
	        }else{
	        	entity.setDepartPath(parent.getDepartPath()+"-"+id);
	        }
			//算出修改部门的移动的级别差，所有子部门的departLevel+level
			int level=Integer.parseInt(dbEntity.getDepartLevel()==null?"1":dbEntity.getDepartLevel())-Integer.parseInt(entity.getDepartLevel()==null?"1":entity.getDepartLevel());
			//加上"-"代表只差子部门，否则连自己也查询出来
			List<DepartEntity> departList=departDao.getDepartsByDepartPath(dbEntity.getDepartPath()+"-");
			if(null != departList && departList.size()>0){
				for(int i=0;i<departList.size();i++){
					DepartEntity d=departList.get(i);
					String departPath=d.getDepartPath();
					departPath=departPath.replaceAll(dbEntity.getDepartPath(), entity.getDepartPath());
					d.setDepartLevel((Integer.parseInt(d.getDepartLevel())+level)+"");
					d.setDepartPath(departPath);
					d.setUpdateBy(dto.getAccountId());
					departDao.updateById(d);
				}
			}
		}*/
		entity.setUpdateBy(dto.getAccountId());
		departDao.updateById(entity);

		//创建一级部门群组
		if("1".equals(entity.getDepartLevel())){
			//修改群组
			this.updateGroup(entity.getId(),entity.getDepartName());
		}
		//返回部门节点的树格式的数据
		OrgTreeDTO tree = new OrgTreeDTO();
		tree.setRealId(entity.getId());
		tree.setId(entity.getId());
		tree.setCompanyId(entity.getCompanyId());
		tree.setText(entity.getDepartName());
		tree.setType("depart");
		tree.setTreeEntity(entity);
		return new AjaxMessage().setCode("0").setInfo("保存成功").setData(tree);
	}


	/**
	 * 方法描述：删除部门（递归删除）【删除部门及所有的子部门和人员】
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-上午11:15:52
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public AjaxMessage deleteDepartById(String id,String accountId) throws Exception {
		//1.查询要删除的部门
		DepartEntity departEntity = this.selectById(id);
		//当前用户（管理员）如果管理员在部门中，则不删除管理员的记录
		CompanyUserEntity admin = companyUserDao.getCompanyUserByUserIdAndCompanyId(accountId,departEntity.getCompanyId());
		//查询所有的子部门
		List<DepartEntity> list = departDao.getDepartsByDepartPath(departEntity.getDepartPath()+"-");

		Map<String,Object> param = new HashMap<String,Object>();

		//2.把所有部门的id放在orgIds中
		List<String> orgIds = new ArrayList<String>();
		orgIds.add(id);
		for (int i =0;list!=null&&i<list.size();i++){
			orgIds.add(list.get(i).getId());
		}
		param.put("orgIds",orgIds);
		//3.根据orgIds查询所有的orgUserEntity数据
		List<OrgUserEntity> orgUserEntityList = orguserDao.selectByParam(param);

		//4.把orgUserEntityList中的cuId保存到cuIdList 中，以便删除部门后，是否要删除员工数据
		List<String> cuIdList = new ArrayList<String>();
		for (int j = 0 ;orgUserEntityList!=null&&j<orgUserEntityList.size();j++){
			cuIdList.add(orgUserEntityList.get(j).getCuId());
		}
		//5.删除所有的部门与部门成员之间的关系
		orguserDao.deleteOrgUserByParam(param);
		//6.删除所有此根节点下的部门（包含自己）
		departDao.deleteByDepartPath(departEntity.getDepartPath());
		//7.逐条查询cuIdList下的人员，是否还有部门，若无，删除（状态改为离职状态）
		String info="删除成功!";
		for(String cuId:cuIdList){

				param.clear();
				param.put("cuId",cuId);
				List<OrgUserEntity> cuOrgList = orguserDao.selectByParam(param);
				if(cuOrgList == null || cuOrgList.size()==0){//如果不存在其他部门信息，则删除
					if(admin == null || !cuId.equals(admin.getId()))//不是管理员才删除，否则不设为离职
					{
						CompanyUserEntity companyUserEntity = companyUserDao.selectById(cuId);
						if(companyUserEntity!=null){
							companyUserEntity.setAuditStatus("4");
							companyUserEntity.setUpdateBy(accountId);
							companyUserDao.updateById(companyUserEntity);

							//删除所有的权限
							param.clear();
							param.put("userId",companyUserEntity.getUserId());
							param.put("companyId",companyUserEntity.getCompanyId());
							this.userPermissionDao.deleteByUserId(param);
						}

					}else {
						//如果是管理员，则默认添加到公司下
						OrgUserEntity adminOrgEntity = new OrgUserEntity();
						adminOrgEntity.setId(StringUtil.buildUUID());
						adminOrgEntity.setCompanyId(departEntity.getCompanyId());
						adminOrgEntity.setOrgId(departEntity.getCompanyId());
						adminOrgEntity.setUserId(accountId);
						adminOrgEntity.setCuId(admin.getId());
						orguserDao.insert(adminOrgEntity);
						info+="提示：管理员移至当前公司下面";
				}
			}

		}
		//删除一级部门，就应删除一级部门群
		if("1".equals(departEntity.getDepartLevel())){
			this.removeGroup(departEntity.getId());
		}
		return new AjaxMessage().setCode("0").setInfo(info);
	}

	@Override
	public int getDepartByCompanyIdCount(String companyId) throws Exception {
		return departDao.getDepartByCompanyIdCount(companyId);
	}

	/**
	 * 方法描述：获取具有某些角色的部门（当前人在当前组织下的部门）
	 * 作者：MaoSF
	 * 日期：2016/8/16
	 *
	 * @param paraMap
	 * @param:
	 * @return:
	 */
	@Override
	public List<DepartRoleDTO> getDepartByRole(Map<String, Object> paraMap) {
		return departDao.getDepartByRole(paraMap);
	}

	/**
	 * 方法描述：获取所有组织的角色（在当前公司下）
	 * 作者：MaoSF
	 * 日期：2016/8/16
	 *
	 * @param paraMap
	 * @param:
	 * @return:
	 */
	@Override
	public List<DepartRoleDTO> getOrgRole(Map<String, Object> paraMap) {
		return departDao.getOrgRole(paraMap);
	}

	@Override
	public List<DepartEntity> selectNotCreateGroupDepart(Map<String,Object>paraMap) throws Exception {
		return departDao.selectNotCreateGroupDepart(paraMap);
	}
	@Override
	public List<DepartEntity> selectCreateGroupDepart(Map<String,Object>paraMap) throws Exception {
		return departDao.selectCreateGroupDepart(paraMap);
	}
	@Override
	public List<DepartEntity> selectChildDepartEntity(String departPath) throws Exception {
		return departDao.getDepartsByDepartPath(departPath);
	}

	/**
	 * 方法描述：移除群组
	 * 作者：MaoSF
	 * 日期：2016/11/30
	 */
	private void removeGroup(String orgId) throws Exception{
		this.imService.deleteImGroup(orgId, ImGroupType.DEPARTMENT);
	}

	/**
	 * 方法描述：修改群组
	 * 作者：MaoSF
	 * 日期：2016/11/30
	 */
	private void updateGroup(String orgId,String name) throws Exception{
		this.imService.updateImGroup(orgId,name, ImGroupType.DEPARTMENT);
	}
}
