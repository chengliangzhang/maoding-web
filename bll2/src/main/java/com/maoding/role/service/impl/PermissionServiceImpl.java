package com.maoding.role.service.impl;


import com.beust.jcommander.internal.Lists;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.constant.RoleConst;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.StringUtil;
import com.maoding.org.service.CompanyService;
import com.maoding.role.dao.PermissionDao;
import com.maoding.role.dto.OperatorDTO;
import com.maoding.role.dto.PermissionDTO;
import com.maoding.role.entity.PermissionEntity;
import com.maoding.role.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：PermissionService
 * 类描述：权限表（dao）
 * 作    者：wrb
 * 日    期：2016年11月2日-上午11:38:47
 */
@Service("permissionService")
public class PermissionServiceImpl extends GenericService<PermissionEntity> implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private CompanyService companyService;

    /**
     * 方法描述：获取所有权限
     * 作者：MaoSF
     * 日期：2016/11/2
     */
    @Override
    public List<PermissionDTO> getAllPermission() {
        return permissionDao.getAllPermission();
    }


    /**
     * 方法描述：获取当前角色下下所有的权限Code
     * 作者：MaoSF
     * 日期：2016/11/2
     *
     * @param:userId，companyId
     */
    @Override
    public String getPermissionCodeByUserId(Map<String, Object> map) {
        String companyId = (String)map.get("companyId");
        String typeId = this.companyService.getOrgTypeId(companyId);
        if(!StringUtil.isNullOrEmpty(typeId)){
            map.put("typeId",typeId);
        }else {
            map.remove("typeId");
        }
        return permissionDao.getPermissionCodeByUserId(map);
    }

    @Override
    public List<PermissionDTO> getProjectUserPermission(Map<String, Object> map) {
        String companyId = (String)map.get("companyId");
        String typeId = this.companyService.getOrgTypeId(companyId);
        if(!StringUtil.isNullOrEmpty(typeId)){
            map.put("typeId",typeId);
        }
        return permissionDao.getProjectUserPermission(map);
    }


    @Override
    public boolean isOrgManager(String companyId, String userId) {
        return isContentPermission(companyId,userId, SystemParameters.ORG_MANAGER_PERMISSION_ID);
    }

    @Override
    public boolean isFinancial(String companyId, String userId) {
        return isContentPermission(companyId,userId, SystemParameters.FINANCIAL_PERMISSION_ID);
    }

    @Override
    public boolean isFinancialReceive(String companyId, String userId) {
        return isContentPermission(companyId,userId, SystemParameters.FINANCIAL_RECEIVE_PERMISSION_ID);
    }

    @Override
    public boolean isOperatorManager(String companyId, String userId) {
        return isContentPermission(companyId,userId, SystemParameters.OPERATOR_MANAGER_PERMISSION_ID);
    }

    @Override
    public boolean isDesignManager(String companyId, String userId) {
        return isContentPermission(companyId,userId, SystemParameters.DESIGN_MANAGER_PERMISSION_ID);
    }

    @Override
    public boolean haveProjectDeletePermision(String companyId, String userId) {
        return isContentPermission(companyId,userId, SystemParameters.PROJECT_DELETE_PERMISSION_ID);
    }

    @Override
    public OperatorDTO getPermissionOperator(Map<String, Object> map) {
        String companyId = (String)map.get("companyId");
        String accountId = (String)map.get("accountId");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", accountId);
        param.put("companyId", companyId);
        String roleCodes = getPermissionCodeByUserId(param);
        String typeId = this.companyService.getOrgTypeId(companyId);
        OperatorDTO operator = new OperatorDTO();
        if(roleCodes!=null){
            if (roleCodes.contains(RoleConst.FINANCE_TYPE)){
                if(typeId==null){
                    operator.setExpCategorySet(1);
                    operator.setBaseFinanceDataSet(1);
                    if(!CollectionUtils.isEmpty(companyService.getChildrenCompany(companyId))) { //如果是单独的组织，存在下属组织，则可以设置分摊
                        operator.setShareCostSet(1);
                    }
                }
            }
        }
        return operator;
    }

    private boolean isContentPermission(String companyId, String userId, String permissionId){
        Map<String,Object> map = new HashMap<>();
        map.put("companyId",companyId);
        map.put("userId",userId);
        map.put("permissionIds", Lists.newArrayList(permissionId));
        String typeId = this.companyService.getOrgTypeId(companyId);
        if(!StringUtil.isNullOrEmpty(typeId)){
            map.put("typeId",typeId);
        }
        if(permissionDao.getCompanyUserIsHasPermission(map)>0){
            return true;
        }
        return false;
    }
}