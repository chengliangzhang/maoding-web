package com.maoding.role.service;


import com.maoding.core.base.service.BaseService;
import com.maoding.role.dto.OperatorDTO;
import com.maoding.role.dto.PermissionDTO;
import com.maoding.role.entity.PermissionEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：PermissionService
 * 类描述：权限表（dao）
 * 作    者：wrb
 * 日    期：2016年11月2日-上午11:38:47
 */
public interface PermissionService extends BaseService<PermissionEntity> {

    /**
     * 方法描述：获取所有权限
     * 作者：MaoSF
     * 日期：2016/11/2
     */
    List<PermissionDTO> getAllPermission();

    /**
     * 方法描述：获取当前角色下下所有的权限Code
     * 作者：MaoSF
     * 日期：2016/11/2
     * @param:userId，companyId
     */
    String getPermissionCodeByUserId(Map<String,Object> map);
    /**
     * 方法描述：获取项目总览权限
     * 作   者：DongLiu
     * 日   期：2018/1/5 10:22
     */
    List<PermissionDTO> getProjectUserPermission(Map<String,Object> map);

    boolean isOrgManager(String companyId,String userId);

    /**
     * 财务支出
     */
    boolean isFinancial(String companyId,String userId);

    /**
     * 财务收款
     */
    boolean isFinancialReceive(String companyId, String userId);

    boolean isOperatorManager(String companyId,String userId);

    boolean isDesignManager(String companyId,String userId);
    boolean haveProjectDeletePermision(String companyId,String userId);

    /**
     * 方法描述：获取当前角色下下所有的权限Code
     * 作者：MaoSF
     * 日期：2016/11/2
     * @param:userId，companyId
     */
    OperatorDTO getPermissionOperator(Map<String,Object> map);
}