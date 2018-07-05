package com.maoding.org.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.org.dao.*;
import com.maoding.org.dto.BusinessPartnerDTO;
import com.maoding.org.dto.CompanyRelationAuditDTO;
import com.maoding.org.dto.OrgTreeDTO;
import com.maoding.org.entity.*;
import com.maoding.org.service.CompanyRelationAuditService;
import com.maoding.user.dao.AccountDao;
import com.maoding.user.entity.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
@Service("companyRelationAuditService")
public class CompanyRelationAuditServiceImpl extends GenericService<CompanyRelationAuditEntity>
		implements CompanyRelationAuditService{

	@Autowired
	private CompanyRelationAuditDao companyRelationAuditDao;

	@Autowired
	private CompanyRelationDao companyRelationDao;

	@Autowired
	private OrgDao orgDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private SubCompanyDao subCompanyDao;

	@Autowired
	private BusinessPartnerDao businessPartnerDao;

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private CompanyInviteDao companyInviteDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Override
	public List<CompanyRelationAuditDTO> getCompanyRelationAuditByParam(
			Map<String, Object> parma) {
		return companyRelationAuditDao.getCompanyRelationAuditByParam(parma);
	}

	@Override
	public int getCompanyRelationAuditByParamCount(Map<String, Object> parma) {
		return companyRelationAuditDao.getCompanyRelationAuditByParamCount(parma);
	}

	@Override
	public AjaxMessage applicationOrInvitation(CompanyRelationAuditDTO dto) throws Exception {

		AjaxMessage ajax = this.validateApplicationOrInvitation(dto.getOrgId(),dto.getOrgPid());

		if("1".equals(ajax.getCode())){//如果验证不通过。直接返回
			return ajax;
		}

		CompanyRelationAuditEntity relaEntity=new CompanyRelationAuditEntity();
		BaseDTO.copyFields(dto,relaEntity);
		relaEntity.setId(StringUtil.buildUUID());
		relaEntity.setAuditStatus("2");
		relaEntity.setCreateDate(new Date());
		relaEntity.setCreateBy(dto.getAccountId());
		this.insert(relaEntity);
		return new AjaxMessage().setCode("0").setInfo("处理成功").setData(relaEntity);
	}

	/**
	 *
	 * @param orgId
	 * @param orgPid
     * @return
     */
	public AjaxMessage validateApplicationOrInvitation(String orgId,String orgPid){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("cid",orgId);
		List<CompanyRelationAuditDTO> list =companyRelationAuditDao.getCompanyRelationAuditByParam(map);
		if (list!=null && list.size()>0){
			for(CompanyRelationAuditDTO dto :list){
				if((dto.getOrgId().equals(orgId) && dto.getOrgPid().equals(orgPid)) || (dto.getOrgId().equals(orgPid) && dto.getOrgPid().equals(orgId))){
					if("0".equals(dto.getAuditStatus()))
					{
						return new AjaxMessage().setCode("1").setInfo("已经是分支机构或合作伙伴");
					}else {
						return new AjaxMessage().setCode("1").setInfo("您已经邀请过该团队");
					}
				}else {
					if(dto.getOrgId().equals(orgId) && "0".equals(dto.getAuditStatus())){
						return new AjaxMessage().setCode("1").setInfo("该团队已是其他团队的分支机构或合作伙伴");
					}
				}
			}

		}
		//从上面找父节点,只要有父节点（orgPid）==orgId,则不可加盟

		//理论上，每个orgId只对应一个orgPid，所以list为空，或许只有一条记录
		String currentOrgPid = orgPid;
		boolean isWhile = true;
		try {
			while (isWhile){
				map.clear();
				map.put("orgId", currentOrgPid);
				list = companyRelationAuditDao.getCompanyRelationAuditByParam(map);
				if (list == null || list.size() == 0) {
					return new AjaxMessage().setCode("0");
				}
				CompanyRelationAuditDTO dto = list.get(0);
				if (orgId.equals(dto.getOrgPid())) {
					return new AjaxMessage().setCode("1").setInfo("存在循环加盟，加盟失败");
				}
				currentOrgPid = dto.getOrgPid();

			}
		}catch (Exception e){
			isWhile=false;
		}finally {
			isWhile=false;
		}
		return new AjaxMessage().setCode("1").setInfo("存在循环加盟，加盟失败");
	}

	@Override
	public AjaxMessage processingApplicationOrInvitation(CompanyRelationAuditDTO dto) throws Exception {
		String relationId=dto.getId();
		String orgId = dto.getOrgId();
		String accountId =dto.getAccountId();
		int auditStatus=Integer.parseInt(dto.getAuditStatus());
		if(auditStatus<0){
			companyRelationAuditDao.deleteById(relationId);
			return new AjaxMessage().setCode("0").setInfo("处理成功");
		}else{
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("orgId", orgId);
			if(auditStatus==0){
				//查询是否有父公司
				int count=companyRelationDao.getCompanyRelationByParamCount(map);
				if(count==0){
					//1.更新审核表中的数据
					CompanyRelationAuditEntity relationAuditEntity=new CompanyRelationAuditEntity();
					relationAuditEntity.setId(relationId);
					relationAuditEntity.setAuditStatus(String.valueOf(auditStatus));
					relationAuditEntity.setPassDate(DateUtils.date2Str(DateUtils.date_sdf));
					relationAuditEntity.setUpdateBy(accountId);
					companyRelationAuditDao.updateById(relationAuditEntity);
					//2.把审核完的数据插入到已审核的表中
					CompanyRelationEntity relaEntity = new  CompanyRelationEntity();
					relaEntity.setId(relationId);
					relaEntity.setOrgId(dto.getOrgId());
					relaEntity.setOrgPid(dto.getOrgPid());
					relaEntity.setCreateBy(accountId);
					companyRelationDao.insert(relaEntity);

					//3.添加组织基础表中的数据，id=CompanyRelationEntity.id
					OrgEntity orgEntity = new OrgEntity();
					orgEntity.setId(relaEntity.getId());
					orgEntity.setOrgStatus("0");
					orgEntity.setOrgType(dto.getType());
					orgEntity.setCreateBy(accountId);
					orgDao.insert(orgEntity);
					//4.判断是分支机构还是合作伙伴。分别插入分支机构（合作伙伴数据表中）
					CompanyEntity companyEntity = companyDao.selectById(orgId);
					if("2".equals(dto.getType())){//如果是分支机构
						SubCompanyEntity subCompanyEntity = new SubCompanyEntity();
						subCompanyEntity.setId(relaEntity.getId());
						subCompanyEntity.setNickName(companyEntity.getCompanyName());
						subCompanyEntity.setCreateBy(accountId);
						subCompanyDao.insert(subCompanyEntity);
					}
					if("3".equals(dto.getType())){//如果是合作伙伴
						BusinessPartnerEntity businessPartnerEntity = new BusinessPartnerEntity();
						businessPartnerEntity.setId(relaEntity.getId());
						businessPartnerEntity.setNickName(companyEntity.getCompanyName());
						businessPartnerEntity.setCreateBy(accountId);
						businessPartnerEntity.setCompanyId(companyEntity.getId());
						businessPartnerEntity.setType(3);
						businessPartnerDao.insert(businessPartnerEntity);
					}

					OrgTreeDTO tree = new OrgTreeDTO();
					tree.setRealId(orgId);
					tree.setId(orgId);
					tree.setCompanyId(orgId);
					tree.setText(dto.getCompanyName());
					if("2".equals(dto.getType()))//如果是分支机构
					{
						tree.setType("subCompany");
					}
					if("3".equals(dto.getType()))//如果是分支机构
					{
						tree.setType("partner");
					}
					tree.setTreeEntity(companyDao.selectById(orgId));
					return new AjaxMessage().setCode("0").setInfo("处理成功").setData(tree);
					//
				}else{//如果已经存在挂靠的公司
				/*CompanyRelationAuditEntity relaEntity= new CompanyRelationAuditEntity();
				relaEntity.setId(relationId);*/
					//删除审核表中的数据
					companyRelationAuditDao.deleteById(relationId);
					return new AjaxMessage().setCode("1").setInfo("此公司已有挂靠团队，该数据已被删除");
				}
			}
			else{//拒绝处理
				CompanyRelationAuditEntity relaEntity= new CompanyRelationAuditEntity();
				relaEntity.setId(relationId);
				relaEntity.setAuditStatus(dto.getAuditStatus());
				//删除审核表中的数据
				companyRelationAuditDao.updateById(relaEntity);
				return new AjaxMessage().setCode("0").setInfo("处理成功");
			}
		}
	}




	@Override
	public AjaxMessage applyBusinessPartner(BusinessPartnerDTO dto) throws Exception {

		CompanyInviteEntity companyInviteEntity = this.companyInviteDao.selectById(dto.getInviteId());
		AccountEntity accountEntity = accountDao.getAccountByCellphoneOrEmail(dto.getCellphone());
		if(accountEntity==null || companyInviteEntity==null){
			return AjaxMessage.failed("申请失败");
		}

		String orgPid = companyInviteEntity.getCompanyId();
		String orgId = dto.getCompanyId();

		Map<String,Object> map=new HashMap<String,Object>();

		if("3".equals(companyInviteEntity.getType())){//外部合作组织

			PartnerEntity partnerEntity = new PartnerEntity();
			partnerEntity.setCompanyId(orgId);
			partnerEntity.setId(companyInviteEntity.getId());
			organizationDao.updateById(partnerEntity);

			//删除邀请信息
			this.companyInviteDao.deleteById(dto.getInviteId());
			return new AjaxMessage().setCode("0").setInfo("处理成功");

		}else{//事业合伙人、分公司

			map.clear();
			map.put("orgId", orgId);
			//查询是否有父公司
			int count=companyRelationDao.getCompanyRelationByParamCount(map);
			if(count==0){
				String relationId = StringUtil.buildUUID();
				//1.更新审核表中的数据
				CompanyRelationAuditEntity relationAuditEntity=new CompanyRelationAuditEntity();
				relationAuditEntity.setId(relationId);
				relationAuditEntity.setOrgId(orgId);
				relationAuditEntity.setOrgPid(orgPid);
				relationAuditEntity.setAuditStatus("0");
				relationAuditEntity.setPassDate(DateUtils.date2Str(DateUtils.date_sdf));
				relationAuditEntity.setCreateBy(accountEntity.getId());
				companyRelationAuditDao.insert(relationAuditEntity);
				//2.把审核完的数据插入到已审核的表中
				CompanyRelationEntity relaEntity = new  CompanyRelationEntity();
				BaseDTO.copyFields(relationAuditEntity,relaEntity);
				companyRelationDao.insert(relaEntity);

				//3.添加组织基础表中的数据，id=CompanyRelationEntity.id
				OrgEntity orgEntity = new OrgEntity();
				orgEntity.setId(relaEntity.getId());
				orgEntity.setOrgStatus("0");
				if ("1".equals(companyInviteEntity.getType())) { //分公司
					orgEntity.setOrgType("2");
				}else {
					orgEntity.setOrgType("3");
				}
				orgEntity.setCreateBy(accountEntity.getId());
				orgDao.insert(orgEntity);
				//4.判断是分支机构还是合作伙伴。分别插入分支机构（合作伙伴数据表中）
				CompanyEntity companyEntity = companyDao.selectById(orgId);

				if ("1".equals(companyInviteEntity.getType())) { //分公司
					SubCompanyEntity subCompanyEntity = new SubCompanyEntity();
					subCompanyEntity.setId(relaEntity.getId());
					subCompanyEntity.setNickName(companyEntity.getCompanyName());
					subCompanyEntity.setCreateBy(accountEntity.getId());
					subCompanyDao.insert(subCompanyEntity);
				} else { //合伙人
					BusinessPartnerEntity businessPartnerEntity = new BusinessPartnerEntity();
					businessPartnerEntity.setId(relaEntity.getId());
					businessPartnerEntity.setNickName(companyEntity.getCompanyName());
					businessPartnerEntity.setCreateBy(accountEntity.getId());
					businessPartnerEntity.setCompanyId(companyEntity.getId());
					businessPartnerEntity.setType(3);
					businessPartnerDao.insert(businessPartnerEntity);
				}

				//删除邀请信息
				this.companyInviteDao.deleteById(dto.getInviteId());
				return new AjaxMessage().setCode("0").setInfo("处理成功");
				//
			}else {
				return new AjaxMessage().setCode("1").setInfo("申请失败");
			}
		}


	}

}
